const RECONNECT_IN_SEC = 10
let socket_host = '93.90.201.167';
let socket_port = '8080';

let ws = {
  /**
	 * Start the connection
	 * 
	 * @type {WebSocket}
	 */
  conn: null,
}

WebSocket.prototype.reconnect = function (callback) 
{
	if (this.readyState === WebSocket.OPEN || this.readyState !== WebSocket.CONNECTING) 
	{
		this.close()
	}

	let seconds = RECONNECT_IN_SEC
	//let container = dom('.connection_alert .error_reconnect_countdown')
	let countHandle = setInterval(() => 
	{
		if (--seconds <= 0) 
		{
			clearInterval(countHandle)
			delete container
			callback()
		}
		//container.text(seconds.toString())
	}, 1000)
}

let connect = function () 
{
	if (ws.conn) 
	{
		if (ws.conn.readyState === WebSocket.OPEN || ws.conn.readyState == WebSocket.CONNECTING) 
		{
			ws.conn.close()
	    }
	    delete ws.conn
	}

	ws.conn = new WebSocket('ws://' + socket_host + ':' + socket_port)

	/**
	 * Connection has been established
	 * 
	 * @param {Event}
	 * event The onopen event
	 **/
	ws.conn.onopen = function (event) 
	{
		console.log('Connection established!');

	    /**
		 * Register te client to the server. This allows the server to return a
		 * list of chat clients to list on the side.
		 */
	    
	    register_client()

	    /**
		 * Request the user list from the server. If the server replies the user
		 * list will be populated.
		 */
	    request_userlist()
	}

	/**
	 * A new message (read package) has been received.
	 * 
	 * @param {Event}
	 * event the onmessage event
	 **/
	ws.conn.onmessage = function (event) 
	{
		let pkg = JSON.parse(event.data)
		/* Set total message count */
		if((pkg.totalMessageCount)) $('#chatCount').html(pkg.totalMessageCount);
		//console.log(pkg);
		process_response(pkg);
	}

	/**
	 * Notify the user that the connection is closed and disable the chat
	 * bar.
	 * 
	 * @param {Event}
	 *            event The onclose event
	 **/
	ws.conn.onclose = function (event) 
	{
		console.log('Connection closed!')

//	    dom('.client_chat').prop('disabled', true)
//	    dom('.connection_alert').show()
//	    clear_userlist()

	    if (event.target.readyState === WebSocket.CLOSING || event.target.readyState === WebSocket.CLOSED) 
	    {
	    	event.target.reconnect(connect)
	    }
	}

	/**
	 * Display a message in the terminal if we run into an error.
	 * 
	 * @param {Event}
	 *            event The error event
	 **/
	
	ws.conn.onerror = function (event) 
	{
		console.log('We have received an error!')
	}
}

document.addEventListener('DOMContentLoaded', connect)