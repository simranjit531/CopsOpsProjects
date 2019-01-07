function _create_session()
{
	var user = localStorage.getItem('user');
	user = JSON.parse(user);	
	
	QB.createSession(function(csErr, csRes) {
		var userRequiredParams = {
                'login':user.login,
                'password': user.password
            };

		if (csErr) { alert("Unable to initialize chat, please try again"); } 
        else 
        {
        	app.token = csRes.token;
        	QB.login(userRequiredParams, function(loginErr, loginUser){        		
        		if(loginErr) { alert("Unable to initialize chat, please try again"); }
        		else { loginSuccess(loginUser); }                
        	});	
        }
	});	

}

/* Login success */
function loginSuccess(userData)
{
	var user = localStorage.getItem('user');
	user = JSON.parse(user);	
    app.user = userData;
    QB.chat.connect({userId: app.user.id, password: user.password}, function(err, roster){
    	if (err) { alert("Unable to initialize chat, please try again"); } 
    	else { get_user_list(); }         
    });
}

function get_user_list()
{
	params = {
        tags: app.user.user_tags,
        per_page: 100
    };
	
	var html = '';
	QB.users.get(params, function (err, response) { 
		$(response.items).each(function(k,v){
			if(v.user.id == app.user.id) return 1;
			html +='<li><a href="javascript:void(0);">'+v.user.full_name+'</a></li>';
		})
		
		$('#contact-lists').html(html);
	});
}


