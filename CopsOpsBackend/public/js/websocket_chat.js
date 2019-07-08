let chat_user = {
	'username':$('input[type="hidden"][name="hidden_user_full_name"]').val(),
	'id': $('input[type="hidden"][name="hidden_id"]').val()
}

function register_client()
{
	/**
	   * Create a registration package to send to the
	   * server.
	   */
	  let pkg = {
	    'user': chat_user, /* Defined in index.php */
	    'type': 'register',
	  }

	  pkg = JSON.stringify(pkg)
	  
	  /**
	   * Send the package to the server
	   */
	  if (ws.conn && ws.conn.readyState === WebSocket.OPEN) 
	  {
	    ws.conn.send(pkg)
	  }
}

function request_userlist()
{
	setInterval(function () 
	{
		if (ws.conn.readyState !== WebSocket.CLOSING && ws.conn.readyState !== WebSocket.CLOSED) 
		{
			/**
			 * Create a package to request the list of users
			 **/
			let searchterm = $('input[type="text"][name="_search_term"]').val();
			
			let pkg = {
				'user': chat_user, /* Defined in index.php */
				'type': 'userlist',
				'term': searchterm
		    }
	
		    /**
		     * We need a object copy of package
		     * to send to dialog_output() but we
		     * also want to turn the original package
		     * into a string so we can send it over the
		     * socket to the server.
		     *
		     * @type {{user, message: any}}
		     **/
		     pkg = JSON.stringify(pkg)
		     
		     if (ws.conn && ws.conn.readyState === WebSocket.OPEN) 
		     {
		        ws.conn.send(pkg)
		     }
	    }
	}, 1000)
}

function request_recent_chat()
{
	var lang =  $('input[type="hidden"][name="_hidden_language"]').val();
	
	let pkg = { 'user': chat_user, 'type': 'recentchats', 'lang':lang}

	pkg = JSON.stringify(pkg)
		  
	  /**
	   * Send the package to the server
	   */
	  if (ws.conn && ws.conn.readyState === WebSocket.OPEN) 
	  {
	    ws.conn.send(pkg)
	  }
}


function process_request(request)
{

	
}

function process_response(response)
{	
    if (response.type === 'message') 
    {    	
		console.log(response); 
    	message(response);
    	refreshLastMessage(response);
    } 
    else if (response.type === 'userlist') 
    {
    	populate_users(response);
    }
    else if (response.type === 'chathistory') 
    {
    	populate_chat_history(response);
    }
    else if (response.type === 'recentchats') 
    {
		console.log(response.recentchat);
    	populate_recent_chat(response.recentchat);
    }
    
    $('.loading').hide();
}

function message(message)
{
	let _sender_id = $('input[type="hidden"][name="hidden_id"]').val();
	let _receiver_id = $('input[type="hidden"][name="_receiver_id"]').val();
	let _receiver_name = $('input[type="hidden"][name="_receiver_name"]').val();
	let frenchme = $('input[type="hidden"][name="frenchme"]').val();
	
	let html = '';
//	console.log(message);
//	console.log(_receiver_id);
	if(message.from_user == "ME")
	{
		html +='<div class="admin-chat"><h3>'+frenchme+'</h3><div class="chat-text-sec">';
		if(message.message_type == 'IMAGE')
		{
			html +='<a href="'+v.message+'" data-lightbox="messages"><img class="uploaded-image" src="'+message.message+'"/></a>';
		}
		else if(v.message_type == 'VIDEO')
		{
			html +="<a href='"+v.message+"' data-toggle='modal' data-target='#myModal_"+k+"'>";
			// html +='<i class="fa fa-film fa-4x" aria-hidden="true"></i>';	
			html +='<img src="'+BASE_URL+'/images/play.jpg">';						    
			html +="</a>";					
			html +=`<div id="myModal_`+k+`" class="modal fade" role="dialog">
						<div class="modal-dialog">				  					  
							<div class="modal-content">
								<div class="modal-body">
									<video autoplay id="v_`+k+`"  controls>
										<source src="`+v.message+`" type="video/mp4">
										<source src="`+v.message+`" type="video/ogg">
									</video>
								</div>
							</div>
						</div>
					</div>`;
		}
		else if(v.message_type == 'PDF')
		{
			html +="<a href='"+v.message+"' >";
			html +='<i class="fa fa-file-pdf-o fa-4x"></i>';					    
			html +="</a>";
		}
		else
		{
			html += '<p>'+message.message+'</p>';
		}
		html +='</div></div>';
		//console.log("Reaching If");
	}		
	else 
	{
		html += '<div class="client-chat"><h3>'+message.to_username+'</h3><div class="chat-text-sec">';
		if(message.message_type == 'IMAGE')
		{
			html +='<a href="'+v.message+'" data-lightbox="messages"><img class="uploaded-image" src="'+message.message+'"/></a>';
		}
		else if(message.message_type == 'VIDEO')
		{
			var k = $.now();
			html +="<a href='"+message.message+"' data-toggle='modal' data-target='#myModal_"+k+"'>";
			// html +='<i class="fa fa-film fa-4x" aria-hidden="true"></i>';
			html +='<img src="'+BASE_URL+'/images/play.jpg">';						    
			html +="</a>";					
			html +=`<div id="myModal_`+k+`" class="modal fade" role="dialog">
						<div class="modal-dialog">				  					  
							<div class="modal-content">
								<div class="modal-body">
									<video autoplay id="v_`+k+`"  controls>
										<source src="`+message.message+`" type="video/mp4">
										<source src="`+message.message+`" type="video/ogg">
									</video>
								</div>
							</div>
						</div>
					</div>`;

		}
		else if(message.message_type == 'PDF')
		{
			html +="<a href='"+message.message+"' >";
			html +='<i class="fa fa-file-pdf-o fa-4x"></i>';					    
			html +="</a>";
		}
		else
		{
			html += '<p>'+message.message+'</p>';
		}
		html +='</div></div>';
	}
	
	if(_receiver_id == message.user.id) $('.chatBox').append(html);
	//$('.nav-tabs a[data-type="recentchats"]').trigger('click');
		
	$('.uploaded-image').click(function(){
	    lightbox.start($(this));
	    return false;
	});
	
	if($('.chatBox').length > 0) $('.chatBox').scrollTop($('.chatBox')[0].scrollHeight);
}


function populate_users(response)
{	
	//console.log("Populate user");
	if(response.users.length > 0)
	{
		var html = '';
		var _receiver_id = $('input[type="hidden"][name="_receiver_id"]').val();
		//console.log(_receiver_id);
		$(response.users).each(function(k,v){			
			let aClass= '';
			if(typeof _receiver_id !="undefined"){
				if(_receiver_id.length > 0 && (_receiver_id == v.id)) aClass = 'active';
			} 
			html += '<li data-username="'+v.username+'" data-user-id="'+v.id+'" class="'+aClass+'"><p>'+v.username+'</p> <span id="chatCountUser">1</span></li>'
		});
		$('.users-listing').html(html);
		
		$("#chatCount").html(response.totalMessageCount);
		$('.loading').hide();
	}
}

function populate_recent_chat(chat)
{
	console.log(chat);
	if(chat.length > 0)
	{
		var html = '';
		$(chat).each(function(k,v){
			let loggedInUser = $('input[type="hidden"][name="hidden_id"]').val();
			let receiver_id = v.receiver_id == loggedInUser ? v.sender_id : v.receiver_id;
			
			if(v.message_type == "IMAGE" || v.message_type == "VIDEO" || v.message_type == "PDF") {
				html += '<li data-username="'+v.user+'" data-user-id="'+receiver_id+'"><p>'+v.user+'<span>Attachment</span></p> <span>'+v.time+'</span><span class="recentUnreadCount">'+v.unread+'</span></li>';
			}
			else {
				html += '<li data-username="'+v.user+'" data-user-id="'+receiver_id+'"><p>'+v.user+'<span>'+v.message+'</span></p> <span>'+v.time+'</span><span class="recentUnreadCount">'+v.unread+'</span></li>';
			}
			
		});
		
		$('.recent-listing').html(html);
	}
}


function populate_chat_history(history)
{	
	//console.log(history);
	let frenchme = $('input[type="hidden"][name="frenchme"]').val();
	if((history.message).length > 0)
	{
		var html = '';
		
		$(history.message).each(function(k,v){
			if(v.sender == "ME")
			{
				html += '<div class="admin-chat" data-message-id="'+v.message_id+'"><h3>'+frenchme+'</h3><div class="chat-text-sec">';
				if(v.message_type == 'IMAGE')
				{
					html +='<a href="'+v.message+'" data-lightbox="messages">';
					html +='<i class="fa fa-picture-o fa-4x" aria-hidden="true"></i>';					
					html +='</a>';
						
				}
				else if(v.message_type == 'VIDEO')
				{
					html +="<a href='"+v.message+"' data-toggle='modal' data-target='#myModal_"+k+"'>";
					// html +='<i class="fa fa-film fa-4x" aria-hidden="true"></i>';	
					html +='<img src="'+BASE_URL+'/images/play.jpg">';					    
					html +="</a>";					
					html +=`<div id="myModal_`+k+`" class="modal fade" role="dialog">
								<div class="modal-dialog">				  					  
					  				<div class="modal-content">
										<div class="modal-body">
											<video autoplay id="v_`+k+`"  controls>
												<source src="`+v.message+`" type="video/mp4">
												<source src="`+v.message+`" type="video/ogg">
											</video>
										</div>
					  				</div>
								</div>
							</div>`;



				}
				else if(v.message_type == 'PDF')
				{
					html +="<a href='"+v.message+"'  target='_blank'>";
					html +='<i class="fa fa-file-pdf-o fa-4x"></i>';					    
					html +="</a>";
				}
				else
				{
					html +='<p>'+v.message+'</p>';
				}
				html +='</div></div>';
			}
			else
			{
				html += '<div class="client-chat"  data-message-id="'+v.message_id+'"><h3>'+v.sender+'</h3><div class="chat-text-sec">';
				
				if(v.message_type == 'IMAGE')
				{
					html +='<a href="'+v.message+'" data-lightbox="messages">';
					html +='<i class="fa fa-picture-o fa-4x" aria-hidden="true"></i>';					
					html +='</a>';
				}
				else if(v.message_type == 'VIDEO')
				{
					html +="<a href='"+v.message+"' data-toggle='modal' data-target='#myModal_"+k+"'>";
					// html +='<i class="fa fa-film fa-4x" aria-hidden="true"></i>';		
					html +='<img src="'+BASE_URL+'/images/play.jpg">';				    
					html +="</a>";

					html +=`<div id="myModal_`+k+`" class="modal fade" role="dialog">
								<div class="modal-dialog">				  					  
					  				<div class="modal-content">
										<div class="modal-body">
											<video autoplay id="v_`+k+`"  controls>
												<source src="`+v.message+`" type="video/mp4">
												<source src="`+v.message+`" type="video/ogg">
											</video>
										</div>
					  				</div>
								</div>
							</div>`;
				}
				else if(v.message_type == 'PDF')
				{
					html +="<a href='"+v.message+"' target='_blank'>";
					html +='<i class="fa fa-file-pdf-o fa-4x"></i>';					    
					html +="</a>";
				}
				else
				{
					html +='<p>'+v.message+'</p>';
				}
				
				html +='</div></div>';
			}
		});
		//console.log($(".chatBox .client-chat").length);
		if ($(".client-chat").length > 0 || $(".admin-chat").length > 0 )
		{ 			
			$(".chatBox").prepend(html);
		}
		else
		{
				$('.chatBox').html(html);
		}
		
		if($('.chatBox').length > 0) $('.chatBox').scrollTop($('.chatBox')[0].scrollHeight);
		
		update_seen_status();	
		
		$('.nav-tabs a[data-type="recentchats"]').trigger('click');
		$('input[type="hidden"][name="page_length"]').val(history.page);	

		
	}
	
	$('.uploaded-image').click(function(){
	    lightbox.start($(this));
	    return false;
	});
}

function refreshLastMessage(message)
{
	var recentCount = $('.recent-listing li[data-user-id='+message.user.id+']').find('.recentUnreadCount').html();

	recentCount = (parseInt(recentCount) + 1);

	$('.recent-listing li[data-user-id='+message.user.id+']').find('.recentUnreadCount').html(recentCount);
	
	var msg = message.message
	console.log(msg);
	if(message.message_type != "TEXT") msg = "Attachment";
	$('.recent-listing li[data-user-id='+message.user.id+']').children('p').children('span').html(msg);
}





/**
 * Send a chat message to the server
 */
function send_message () 
{
	/**
	 * Catch the chat text
	 * @type {string}
	 **/
	let chat_message = $('.client_chat').val()

	if (typeof chat_message === 'undefined' || chat_message.length === 0) 
	{
		$('.client_chat ').addClass('error');
		setTimeout(() => {
			$('.client_chat ').removeClass('error');
		}, 500)
		return
	}

	let chat_user = {
		'username':$('input[type="hidden"][name="hidden_user_full_name"]').val(),
		'id': $('input[type="hidden"][name="hidden_id"]').val()
	}
	
	let to_user = $('input[type="hidden"][name="_receiver_id"]').val();
	/**
	 * Create a package to send to the
	 * server.
	 **/
	let pkg = {
		'user': chat_user,
		'message': chat_message,
		'to_user': to_user,
		'type': 'message',
		'message_type':'TEXT'
	}

	let frenchme = $('input[type="hidden"][name="frenchme"]').val()
	//console.log(pkg);
	if (pkg.to_user.length > 0) {
		html = '<div class="admin-chat"><h3>'+frenchme+'</h3><div class="chat-text-sec"><p>'+pkg.message+'</p></div></div>';
	    $('.chatBox').append(html);
	    
	    if($('.chatBox').length > 0) $('.chatBox').scrollTop($('.chatBox')[0].scrollHeight);
	} 
	
	let pkg_object = pkg
	pkg = JSON.stringify(pkg)

	/**
	 * Send the package to the server
	 **/
	if (ws.conn && ws.conn.readyState === WebSocket.OPEN) {
		ws.conn.send(pkg)
	}

	/**
	 * Display the message we just wrote
	 * to the screen.
	 **/
	//dialog_output(pkg_object)
//	message(pkg_object);
	/**
	 * Empty the chat input bar
	 * we don't need it anymore.
	 **/
	$('.client_chat').val('')
}


$('.nav-tabs a').click(function(){
	if($(this).data('type') == "recentchats")
	{
		request_recent_chat();
	}
	else if($(this).data('type') == "userlist")
	{
		request_userlist();
	}
});


$(document).on('click', '.users-listing li, .recent-listing li', function(){

	var _receiver_id = $(this).data('userId');
	var _receiver_username = $(this).data('username');
	//console.log(_receiver_id);
	/*
	 * Check receiver id
	 */
	let receiver_id = $('input[type="hidden"][name="_receiver_id"]').val();
	//console.log(receiver_id);
	if(_receiver_id != receiver_id){
		$('input[type="hidden"][name="page_length"]').val(0)
		$('.chatBox').html('');
	}
	
	
	$('input[type="hidden"][name="_receiver_id"]').val(_receiver_id);
	$('input[type="hidden"][name="_receiver_name"]').val(_receiver_username);
	$('.chat-heading').html(_receiver_username);
	
	$('.users-listing li').removeClass('active');
	$('.recent-listing li').removeClass('active');
	$(this).addClass('active');
	
	request_paginated_chat_history();
});


$("#upload_document").on("click", function(){	
	$("input[type='file'][name='upload_document']").trigger("click");
});

$("input[type='file'][name='upload_document']").on("change", function(){
	
	$('.loading1').show();
	
	var $this = $(this);
	//	console.log($(this)[0]['files']);
	if(($this[0].files[0].size/1000000) > 10){ alert("Files less than 10 MB are only allowed"); $('.loading1').hide(); return false; }
	var formData = new FormData();
	formData.append('_token', $('input[type="hidden"][name="_token"]').val());
	formData.append('upload_document', $(this)[0]['files'][0]);
	
    $.ajax({
        type:'POST',
        url: $(this).data('attr'),
        data:formData,
        cache:false,
        contentType: false,
		processData: false,
		// async:false,
		beforeSend: function() {
			$('.loading1').show();
		},
        success:function(data){
			
            if(data.status == true)
            {
				console.log(data);

            	// Image is uploaded successfully, let's send message
            	let to_user = $('input[type="hidden"][name="_receiver_id"]').val();
            	
				var file = (data.fileName);
							
				let extension = file.substr( (file.lastIndexOf('.') +1) );
							            	
            	let message_type = 'TEXT';
            	if(extension == 'png' || extension == 'jpeg' || extension == 'jpg' || extension == 'bmp' || extension == 'JPEG') message_type = 'IMAGE';
            	else if(extension == 'pdf') message_type = 'PDF';
            	else if(extension == 'mp4' || extension == 'mp3' || extension == 'webm') message_type = 'VIDEO';
            	
            	let pkg = {
            		'user': chat_user,
            		'message': data.fileName,
            		'to_user': to_user,
            		'type': 'message',
					'message_type': message_type,
					'actual':data.actual
            	}
				send_uploaded_document_message (pkg);
            }            
			
			
            
        },
        error: function(data){
		   alert("File size should be less than 10 MB");
		   $('.loading1').hide();
		   return false;
        }
    });
    $this.val('');
});

$('.client_chat').on('keypress', function (evt) {
	if (evt.keyCode === 13) {
		send_message()
    }
});

/**
 * Submit has been pressed execute sending
 * to server.
 **/
$('.btn-send.chat_btn').on('click', function () {
	send_message();
})


function send_uploaded_document_message (message) 
{	
	if (message.to_user.length > 0) {
		html = '<div class="admin-chat"><h3>Me</h3>';
		html +='<div class="chat-text-sec">';
		if(message.message_type == 'IMAGE')
		{
			html +='<a href="'+message.actual+'" data-lightbox="messages">';
			html +='<i class="fa fa-picture-o fa-4x" aria-hidden="true"></i>';
			html +='</a>';
		}
		else if(message.message_type == 'VIDEO')
		{
			var k = $.now();
			html +="<a href='"+message.actual+"' data-toggle='modal' data-target='#myModal_"+k+"'>";
			// html +='<i class="fa fa-film fa-4x" aria-hidden="true"></i>';	
			html +='<img src="'+BASE_URL+'/images/play.jpg">';					    
			html +="</a>";
			html +=`<div id="myModal_`+k+`" class="modal fade" role="dialog">
								<div class="modal-dialog">				  					  
					  				<div class="modal-content">
										<div class="modal-body">
											<video autoplay id="v_`+k+`"  controls>
												<source src="`+message.actual+`" type="video/mp4">
												<source src="`+message.actual+`" type="video/ogg">
											</video>
										</div>
					  				</div>
								</div>
							</div>`;
		}
		else if(message.message_type == 'PDF')
		{
			html +="<a href='"+message.actual+"'  target='_blank'>";
			html +='<i class="fa fa-file-pdf-o fa-4x"></i>';					    
			html +="</a>";
		}
		else if(message.message_type == 'TEXT')
		{
			html +='<p>'+message.message+'</p>';
		}
		html +='</div></div>';
	    $('.chatBox').append(html);
	    
	    // $('.uploaded-image').click(function(){
		//     lightbox.start($(this));
		//     return false;
		// });
	    
		$('.chatBox').scrollTop($('.chatBox')[0].scrollHeight);
		$('.loading1').hide();
		// setTimeout(function(){  }, 10000);
	} 
	
	let pkg = message
	pkg = JSON.stringify(pkg)

	/**
	 * Send the package to the server
	 **/
	if (ws.conn && ws.conn.readyState === WebSocket.OPEN) {
		ws.conn.send(pkg)
	}

	/**
	 * Display the message we just wrote
	 * to the screen.
	 **/
	//dialog_output(pkg_object)
//	message(pkg_object);
	/**
	 * Empty the chat input bar
	 * we don't need it anymore.
	 **/
	$('.client_chat').val('')
}


$('input[type="text"][name="_search_term"]').on("keyup", function(){
	
	let searchterm = $(this).val();
	
//	if(searchterm == "") return false;
	
	let pkg = {
		'user': chat_user, /* Defined in index.php */
		'type': 'userlist',
		'term': searchterm
    }

    /**
     * We need a object copy of package
     * to send to dialog_output() but we
     * also want to turn the original package
     * into a string so we can send it over the
     * socket to the server.
     *
     * @type {{user, message: any}}
     **/
     pkg = JSON.stringify(pkg)
     //console.log(pkg);
     if (ws.conn && ws.conn.readyState === WebSocket.OPEN) 
     {
        ws.conn.send(pkg)
     }
	
});

/*
$(".chatBox").scroll(function() {
		if($(this).scrollTop() + $(this).innerHeight() <= $(this)[0].scrollHeight) {
				console.log("To Reached");
				//request_paginated_chat_history();
		} 
});
*/

$('.chatBox').on("scroll", function() {
    var scrollPos = $('.chatBox').scrollTop();
    if (scrollPos <= 0) {
    	request_paginated_chat_history();
    } 
});



$('.chatBox').scroll(function() {
	update_seen_status();
	//
	/*
	let result = '';
	$('.chatBox div').each(function()
	{		
		if(inViewport(this, false)){
			
			if($(this).data('messageId') != undefined){
				result += $(this).data('messageId')+',';
			}		
		}
	});
	let seenId = result.replace(/,\s*$/, "");
	
	let pkg = {
			'user': chat_user, /* Defined in index.php 
			'type': 'seencount',
			'seen': seenId
    }

    /**
     * We need a object copy of package
     * to send to dialog_output() but we
     * also want to turn the original package
     * into a string so we can send it over the
     * socket to the server.
     *
     * @type {{user, message: any}}
     **///*
	/*
     pkg = JSON.stringify(pkg)
     //console.log(pkg);
     if (ws.conn && ws.conn.readyState === WebSocket.OPEN) 
     {
        ws.conn.send(pkg)
     }
	*/
	
});


function inViewport(element, detectPartial) 
{
    element = $(element);
    detectPartial = (!!detectPartial); // if null or undefined, default to false

    var viewport = $(window),
        vpWidth = viewport.width(),
        vpHeight = viewport.height(),
        vpTop = viewport.scrollTop(),
        vpBottom = vpTop + vpHeight,
        vpLeft = viewport.scrollLeft(),
        vpRight = vpLeft + vpWidth,
		
        elementOffset = element.offset(),
        elementTopArea = elementOffset.top+((detectPartial) ? element.height() : 0),
        elementBottomArea = elementOffset.top+((detectPartial) ? 0 : element.height()),
        elementLeftArea = elementOffset.left+((detectPartial) ? element.width() : 0),
        elementRightArea = elementOffset.left+((detectPartial) ? 0 : element.width());

       return ((elementBottomArea <= vpBottom) && (elementTopArea >= vpTop)) && ((elementRightArea <= vpRight) && (elementLeftArea >= vpLeft));
}

function request_paginated_chat_history()
{
		$('.loading').hide();
		
		//$('.chatBox').html('');
			
		
		$('input[type="text"][name="_text_message"]').prop('disabled', false);
		$('input[type="file"][name="upload_document"]').prop('disabled', false);
		
		let chat_user = {
			'username':$('input[type="hidden"][name="hidden_user_full_name"]').val(),
			'id': $('input[type="hidden"][name="hidden_id"]').val()
		}
		
		let to_user = $('input[type="hidden"][name="_receiver_id"]').val();
		let page = parseInt($('input[type="hidden"][name="page_length"]').val());
		//console.log(page);
		/**
		 * Create a package to send to the
		 * server.
		 **/
		let pkg = {
			'user': chat_user,		
			'to_user': to_user,
			'type': 'chathistory',
			'page' : page,
		}
			
		let pkg_object = pkg
		pkg = JSON.stringify(pkg)

		/**
		 * Send the package to the server
		 **/
		if (ws.conn && ws.conn.readyState === WebSocket.OPEN) {
			ws.conn.send(pkg)
		}
}



function update_seen_status()
{
	let result = '';
	$('.chatBox div').each(function()
	{		
		if(inViewport(this, false)){
			
			if($(this).data('messageId') != undefined){
				result += $(this).data('messageId')+',';
			}		
		}
	});
	let seenId = result.replace(/,\s*$/, "");
	
	let to_user = $('input[type="hidden"][name="_receiver_id"]').val();
	let pkg = {
			'user': chat_user, /* Defined in index.php */
			'type': 'seencount',
			'seen': seenId,
			'to_user':to_user
    }

    $('.nav-tabs a[data-type="recentchats"]').trigger('click');

    /**
     * We need a object copy of package
     * to send to dialog_output() but we
     * also want to turn the original package
     * into a string so we can send it over the
     * socket to the server.
     *
     * @type {{user, message: any}}
     **/
     pkg = JSON.stringify(pkg)
     //console.log(pkg);
     if (ws.conn && ws.conn.readyState === WebSocket.OPEN) 
     {
        ws.conn.send(pkg)
     }
}