@extends('backend.layouts.backendapp')
@section('content')
<!-- Content Header (Page header) -->
    <div class="content-header">
      <div class="container-fluid">
        <div class="row mb-2">
          <div class="col-sm-6">
            <h1 class="m-0 text-dark">{{ trans('pages.discussion') }}</h1>
          </div><!-- /.col -->
          <div class="col-sm-6">
            <ol class="breadcrumb float-sm-right">
              <li class="breadcrumb-item"><a href="#">{{ trans('pages.home')
						}}</a></li>
              <li class="breadcrumb-item active">{{ trans('pages.discussion') }}</li>
              <input type="hidden" name="hidden_user_id" value="{{ Auth::user()->user_id }}"/>
              <input type="hidden" name="hidden_user_full_name" value="{{ Auth::user()->first_name.' '.Auth::user()->last_name }}"/>
              <input type="hidden" name="hidden_usergroup" value="copops"/>
              <input type="hidden" name="hidden_email" value="{{ Auth::user()->email_id }}"/>
            </ol>
          </div><!-- /.col -->
      </div>
  </div>
  </div>
  
  <div class="container-fluid">
  		<div class="cc-inbox-app" data-height="435px" data-width="100%"></div>

  </div>
</div>

  
@endsection

@section('before-styles')
<link rel="stylesheet" href="{{ asset('css/plugins/quickblox/style.css') }}">
<link href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">


<style>
.cc-root .cc-inbox .chatcamp-left-panel {
    width: 25% !important;
    min-width: 280px !important;
    height: 100%;
    float: left;
}
.cc-root #ifc-app.cc-inbox .cc-window-container {
    max-width: calc(100% - 280px) !important;
    width: 75% !important;
    height: 100%;
}
.chatcamp-widget-record-main, .cc-list-header-image, .cc-list-header-actions, .cc-list-header-image svg, .header-actionsm, .chatcamp-widget-emoji, .chatcamp-widget-emoji svg{
  display: none !important;

}
.message-content img{width:200px!important;height: 200px!important;}
.message-content video{width:200px!important;}

</style>
@endsection

@section('after-scripts')
<script src="https://unpkg.com/navigo@4.3.6/lib/navigo.min.js" defer></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/underscore.js/1.8.3/underscore.js" defer></script>
<script src="{{ asset('js/plugins/quickblox/quickblox.min.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/QBconfig.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/user.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/dialog.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/message.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/listeners.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/helpers.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/app.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/login.js') }}" defer></script>
<script src="{{ asset('js/plugins/quickblox/route.js') }}" defer></script>

<!-- Lightbox -->
<link href="{{ asset('js/plugins/lightbox2/src/css/lightbox.css') }}" rel="stylesheet">
<script src="{{ asset('js/plugins/lightbox2/src/js/lightbox.js') }}"></script>
<script src="https://cdn.chatcamp.io/js/chatcamp-ui.min.js"></script>

<script>
 function getQueryStringValue (key) {
        return decodeURIComponent(window.location.search.replace(new RegExp("^(?:.*[&\\?]" + encodeURIComponent(key).replace(/[\.\+\*]/g, "\\$&") + "(?:\\=([^&]*))?)?.*$", "i"), "$1"));
      }

// Create user object from query parameters
      let user = {
        id: getQueryStringValue('id') || '1'
      }
      if(getQueryStringValue('displayName')) {
        user.displayName = getQueryStringValue('displayName')
      }

      // Initialize ChatCamp
      window.ChatCampUi.init({
        appId: "6512253349478264832",
        user: {
          id: "{{ Auth::user()->user_id }}",
          displayName: "{{ Auth::user()->first_name }}" // optional
          // avatarUrl: USER_AVATAR_URL // optional
          // accessToken: USER_ACCESS_TOKEN // optional
        },         
        ui: {
          theme: {
            primaryBackground: "#264b73",
            primaryText: "#ffffff",
            secondaryBackground: "#ffffff",
            secondaryText: "#000000",
            tertiaryBackground: "#f4f7f9",
            tertiaryText: "#263238"
          },
          roster: {
            tabs: ['recent', 'users'],
            render: true,
            defaultMode: 'open' // other possible values are minimize, hidden
          }
          // channel: {
          //   showAttachFile: true,
          //   showVideoCall: true,
          //   showVoiceRecording: true
          // }
        }
      })

/*  
$(function(){
  console.log("Chat Count");
  var data = JSON.stringify({id:'{{ Auth::user()->user_id }}'});

  var xhr = new XMLHttpRequest();
  xhr.withCredentials = true;

  xhr.addEventListener("readystatechange", function () {
    if (this.readyState === this.DONE) {
      console.log(this.responseText);
    }
  });

  xhr.open("POST", "https://api.chatcamp.io//api/2.0/group_channels.messages_count");

  xhr.send(data);
});
/*    
$(function(){
	_auto_login();	
});

function _auto_login()
{
	/*
	var login = $('input[type="hidden"][name="hidden_user_id"]').val();
	var userName = $('input[type="hidden"][name="hidden_user_full_name"]').val();
	var userGroup = $('input[type="hidden"][name="hidden_usergroup"]').val();
	var email = $('input[type="hidden"][name="hidden_email"]').val();
	
	var user = {
        login: login,
        email : email,
        password: 'webAppPass',
        full_name: userName,
        tag_list: userGroup
    };

	localStorage.setItem('user', JSON.stringify(user));
	
	_create_session();
	
	$('.container-fluid').find('button[type="submit"][name="login_submit"]').trigger('click');
// 	login_submit

	$('.lokesh-lightbox').click(function(){
	    lightbox.start($(this));
	    return false;
	});
}

$(document).on('click', '.user__item', function(e){
	e.preventDefault();
	console.log("trigger");

	$('.container-fluid').find('button[type="submit"][name="create_dialog_submit"]').trigger('click');
});

$(document).on('keyup', 'input[type="text"][name="search"]', function(){
	params = {
	        full_name: $(this).val(),
	        per_page: 10
	    };
    
	QB.users.get(params, function (err, responce) {
        var userList = responce.items.map(function(data){
            return userModule.addToCache(data.user);
       });

        app.generate_user_list(userList, app.user.id);
	});

	
	
});
*/

$(function(){
  setTimeout(function(){  $('.cc-list-header-text').html('Chat'); }, 1000);
  
})

</script>
         
@endsection