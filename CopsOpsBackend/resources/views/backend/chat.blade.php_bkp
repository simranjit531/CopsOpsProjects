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
  		<div id="page"></div>
  		<!-- Underscore templates  -->
    <script type="text/template" id="tpl_login">
        <div class="login__wrapper">
            <div class="login__container">
                <div class="login__inner">
                    <img src="{{ asset('img/loader2.gif') }}" alt="QuickBlox">
                    <div class="login__top"  style="display:none;">
                        <a href="" class="login__logo">
                            <img src="{{ asset('img/loader2.gif') }}" alt="QuickBlox">
                        </a>
                        <h1>Quickblox Chat Sample</h1>
                        <h3>Please enter your username and user group.
                            Users within the same group will be able
                            to communicate, create chats with each other
                        </h3>
                    </div>

                    <form name="loginForm" class="login__form"  style="display:none;">
                        <div class="login_form__row">
                            <label for="userName">User name</label>
                            <input type="text" id="userLogin" name="userLogin" value="{{ Auth::user()->user_id }}"/>
                        </div>
                        <div class="login_form__row">
                            <label for="userName">User name</label>
                            <input type="text" id="userName" name="userName" value="{{ Auth::user()->first_name.' '.Auth::user()->last_name }}"/>
                        </div>
                        <div class="login_form__row">
                            <label for="userGroup">User group</label>
                            <input type="text" id="userGroup" name="userGroup" value="copops"/>
                        </div>
                        <div class="login_form__row">
                            <label for="userGroup">User password</label>
                            <input type="text" id="userPassword" name="userPassword" value="webAppPass"/>
                        </div>
                        <div class="login_form__row">
                            <label for="userGroup">User email</label>
                            <input type="text" id="userEmail" name="userEmail" value="{{ Auth::user()->email_id }}"/>
                        </div>

                        <div class="login__button_wrap">
                            <button type="submit" name="login_submit" class="btn m-login__button j-login__button">
                                login
                            </button>
                        </div>
                    </form>
                </div>
                <div class="login__footer" style="display:none;">
                    <div class="footer__logo_wrap">
                        <a href="" class="footer__logo">
                            <img src="{{ asset('img/loader2.gif') }}" alt="QuickBlox">
                        </a>
                        <p>by QuickBlox</p>
                    </div>
                    <span class="footer__version">v. <%= version %></span>
                </div>
            </div>
        </div>
    </script>

    <script type="text/template" id="tpl_dashboardContainer">
        <div class="dashboard">
            <div class="sidebar j-sidebar active">
                <div class="sidebar__inner">
                    <!--
                    <div class="sidebar__header">
                        <a href="#" class="dashboard__logo">
                            <img src="{{ asset('img/loader2.gif') }}" alt="QuickBlox">
                        </a>
                        <div class="dashboard__status_wrap">
                            <h2 class="dashboard__title"><%- user.user_tags %></h2>
                            <p class="dashboard__status j-dashboard_status">
                                Logged in as <%- user.name %>
                            </p>
                        </div>                        
                    </div>
                    -->
                    <div class="sidebar__content chat_list_left">
                        <ul class="sidebar__tabs">
                           <li class="sidebar__tab m-sidebar__tab_new pull-left">
                                <a href="#" class="sidebar__tab_link j-sidebar__tab_link j-sidebar__create_dialog <% tabName === "create" ? print('active') : '' %>" data-type="create">
                                    <i class="fa fa-address-book-o" aria-hidden="true"></i>
                                </a>
                            </li>

                            <li class="pull-left search_chat">
                                <div class="input-group">
                                    <span class="input-group-addon"><i class="fa fa-search" aria-hidden="true"></i></span>
                                    <input type="text" name="search" class="form-control">
                                </div>
                            </li>

                            <li class="sidebar__tab pull-right">
                                <a href="#" class="sidebar__tab_link j-sidebar__tab_link <% tabName === "chat" ? print('active') : '' %>" data-type="chat"><i class="fa fa-comments-o" aria-hidden="true"></i></a>
                            </li>
                            <!--
                            <li class="sidebar__tab">
                                <a href="#" class="sidebar__tab_link j-sidebar__tab_link <% tabName === "public" ? print('active') : '' %>" data-type="public">public chats</a>
                            </li>
                            -->
                            
                        </ul>
                        <ul class="sidebar__dilog_list j-sidebar__dilog_list">
                        </ul>
                        <div class="chat__user_list">


                        </div>
                    </div>
                </div>
            </div>
            <div class="content j-content">
                
            </div>
        </div>
    </script>

    <script  type="text/template" id="tpl_welcome">
        <div class="content__title j-content__title j-welcome">
            {{ trans('pages.welcomecopopschat')}}
        </div>
        <div class="notifications j-notifications hidden"></div>
        <div class="content__inner j-content__inner">
            <div class="welcome__message">
                <p>{{ trans('pages.startchating')}}</p>
            </div>
        </div>
    </script>

    <script type="text/template" id="tpl_userConversations">
        <li class="dialog__item j-dialog__item" id="<%= dialog._id %>" data-name="<%- dialog.name %>">
            <a class="dialog__item_link" href="#!/dialog/<%= dialog._id %>">
                <span class="dialog__avatar m-user__img_<%= dialog.color %> m-type_<%= dialog.type === 2 ? 'group' : 'chat' %>" style="display:none;">
                    <% if(dialog.type === 2) { %>
                        <i class="material-icons">supervisor_account</i>
                    <% } else { %>
                        <i class="material-icons">account_circle</i>
                    <% } %>
                </span>
                <span class="dialog__info">
                    <span class="dialog__name"><%- dialog.name %></span>
                    <span class="dialog__last_message j-dialog__last_message <%= dialog.attachment ? 'attachment' : ''%>"><%- dialog.last_message%></span>
                </span>
                <span class="dialog_additional_info">
                    <span class="dialog__last_message_date j-dialog__last_message_date">
                        <%= dialog.last_message_date_sent %>
                    </span>
                    <span class="dialog_unread_counter j-dialog_unread_counter <% !dialog.unread_messages_count ? print('hidden') : '' %>">
                        <% dialog.unread_messages_count ? print(dialog.unread_messages_count) : '' %>
                    </span>
                </span>
            </a>
        </li>
    </script>

    <script type="text/template" id="tpl_conversationContainer">
        <div class="content__title j-content__title j-dialog">
            <button class="open_sidebar j-open_sidebar"></button>
            <h1 class="dialog__title j-dialog__title"><%- title %></h1>
            <div class="action_links">
                
                <a href="#!/dialog/<%- _id %>/edit" class="add_to_dialog j-add_to_dialog <% type !== 2 ? print('hidden') : ''%>">
                    <i class="material-icons"></i>
                </a>
                
                <a href="#" class="quit_fom_dialog_link j-quit_fom_dialog_link <% type === 1 ? print('hidden') : ''%>" data-dialog="<%- _id %>" style="display:none;">
                    <i class="material-icons"></i>
                </a>
                
            </div>
        </div>
        <div class="notifications j-notifications hidden"></div>
        <div class="content__inner j-content__inner">
            <div class=" messages j-messages"></div>
            <form name="send_message" class="send_message" autocomplete="off">
                    <textarea name="message_feald" class="message_feald" id="message_feald" autocomplete="off"
                              autocorrect="off" autocapitalize="off" placeholder="Type a message" autofocus></textarea>
                <div class="message__actions">
                    <div class="attachments_preview j-attachments_preview"></div>
                    
                    <label for="attach_btn" class="attach_btn">
                        <i class="fa fa-cloud-upload" aria-hidden="true"></i>
                        <input type="file" id="attach_btn" name="attach_file" class="attach_input" accept="image/*"/>
                    </label>
                    
                    <button class="send_btn" style="display:none;">send</button>
                </div>
            </form>
        </div>
    </script>

    <script type="text/template" id="tpl_UpdateDialogContainer">
        <div class="content__title j-content__title update_dialog__header j-update_dialog">
            <a href="#!/dialog/<%- _id %>" class="back_to_dialog j-back_to_dialog">
                <i class="material-icons">arrow_back</i>
            </a>
            <form action="#" name="update_chat_name" class="update_chat_name_form">
                <input type="text" name="update_chat__title" class="update_chat__title_input j-update_chat__title_input" value="<%- title %>" disabled>
                <button type="button" class="update_chat__title_button j-update_chat__title_button">
                    <i class="material-icons m-user_icon">create</i>
                </button>
            </form>
        </div>
        <div class="notifications j-notifications hidden"></div>
        <div class="content__inner j-content__inner">
            <p class="update__chat__description"><span class="update__chat_counter j-update__chat_counter">0</span>&nbsp;user selected:</p>
            <div class="update_chat__user_list j-update_chat__user_list">
            </div>
            <form action="" name="update_dialog" class="dialog_form m_dialog_form_update j-update_dialog_form">
                <button type="button" class="btn m-update_dialog_btn_cancel j-update_dialog_btn_cancel" name="update_dialog_cancel">cancel</button>
                <button class="btn m-update_dialog_btn j-update_dialog_btn"  type="submit" name="update_dialog_submit" disabled>add member</button>
            </form>
        </div>
    </script>

    <script type="text/template" id="tpl_editChatUser">
        <div class="user__item <% selected ? print('disabled selected') : ''%>" id="<%= id %>">
            <span class="user__avatar m-user__img_<%= color %>">
                <i class="material-icons m-user_icon">account_circle</i>
            </span>
            <div class="user__details">
                <p class="user__name"><%= name %></p>
                <% if (last_request_at) { %>
                <!--<p class="user__last_seen"><%= last_request_at %></p>-->
                <% } %>
            </div>
        </div>
    </script>

    <script type="text/template" id="tpl_message">console.log(message);
        <div class="message__wrap" id="<%= message.id %>" data-status="<%= message.status %>">
            <!--
            <span class="message__avatar m-user__img_<%= sender ? sender.color : 'not_loaded' %>">
                <i class="material-icons">account_circle</i>
            </span>
            -->
            <div class="message__content">
                <div class="message__sender_and_status">
                    <% if(message.sender_id == app.user.id) { style="style='padding-left:5px; border-left: 4px solid blue;'" %>
                    <p class="message__sender_name" style="color:blue; font-weight:bold;">Me</p>    
                    <% } else { style="style='padding-left:5px; border-left: 4px solid green;'" %>
                    <p class="message__sender_name"  style="color:green; font-weight:bold; "><%- sender ? sender.name : 'unknown user (' + message.sender_id + ')' %></p>
                    <% } %>
                    
                    <p class="message__status j-message__status"><%= message.status %></p>
                </div>
                <div class="message__text_and_date" <%= style %>>
                    <div class="message__text_wrap">
                        <% if (message.message) { %>
                        <p class="message__text"><%= message.message %></p>
                        <% } %>
                        <% if (message.attachments.length) { %>
                        <div class="message__attachments_wtap">
                            <% _.each(message.attachments, function(attachment){ %>
                            <a href="<%= attachment.src %>" class=".lokesh-lightbox" data-lightbox="image-1">
                            <img src="<%= attachment.src %>" alt="attachment" class="message_attachment" >
                            </a>
                            <% }); %>
                        </div>
                        <% } %>
                    </div>
                    <div class="message__timestamp">
                        <%= message.date_sent %>
                    </div>
                </div>
            </div>
        </div>
    </script>

    <script type="text/template" id="tpl_notificationMessage">
        <div class="message__wrap" id="<%= id %>">
            <p class="m-notification_message"><%= text %></p>
        </div>
    </script>

    <script type="text/template" id="tpl_newGroupChat">
        <div class="content__title j-content__title j-create_dialog">
            <!--
            <button class="back_to_dialog j-back_to_dialog">
                <i class="material-icons">arrow_back</i>
            </button>            
            <h1 class="group_chat__title">New Group Chat</h1>
            -->
        </div>
        <div class="notifications j-notifications hidden"></div>
        <div class="content__inner j-content__inner">
            <!--<p class="group__chat__description">Select participants:</p>-->
            <div class="group_chat__user_list j-group_chat__user_list">
            </div>
            <form action="" name="create_dialog" class="dialog_form m-dialog_form_create j-create_dialog_form" style="display:none;">
                <input class="dialog_name" name="dialog_name" type="text"  autocomplete="off"
                       autocorrect="off" autocapitalize="off" placeholder="Add conversation name" disabled>
                <button class="btn m-create_dialog_btn j-create_dialog_btn"  type="submit" name="create_dialog_submit">create</button>
            </form>
        </div>
    </script>
    
    <script type="text/template" id="tpl_userList">        
        <div class="notifications j-notifications hidden"></div>
        <div class="content__inner j-content__inner">
            <!--<p class="group__chat__description">Select participants:</p>-->
            <div class="group_chat__user_list j-group_chat__user_list">
            </div>
            <form action="" name="create_dialog" class="dialog_form m-dialog_form_create j-create_dialog_form" style="display:none;">
                <input class="dialog_name" name="dialog_name" type="text"  autocomplete="off"
                       autocorrect="off" autocapitalize="off" placeholder="Add conversation name" disabled>
                <button class="btn m-create_dialog_btn j-create_dialog_btn"  type="submit" name="create_dialog_submit">create</button>
            </form>
        </div>
    </script>
    
    

    <script type="text/template" id="tpl_newGroupChatUser">
        <div class="user__item <% user.selected ? print('disabled selected') : ''%>" id="<%= user.id %>">
            <span class="user__avatar m-user__img_<%= user.color %>" style="display:none;">
                <i class="material-icons m-user_icon">account_circle</i>
            </span>
            <div class="user__details">
                <p class="user__name"><%- user.name %></p>
                <% if (user.last_request_at) { %>
                <!--<p class="user__last_seen"><%= user.last_request_at %></p>-->
                <% } %>
            </div>
        </div>
    </script>

    <script type="text/template" id="tpl_message__typing">
        <div class="message__wrap m-typing j-istyping" id="is__typing">
            <% _.each(users, function(user){  %>
                <span class="message__avatar <%- typeof user === 'number' ? 'm-typing_unknown m-typing_' + user : 'm-user__img_' + user.color %>">
                    <i class="material-icons">account_circle</i>
                </span>
            <% }); %>
            <% if (users.length){ %>
            <div id="fountainG">
                <div id="fountainG_1" class="fountainG"></div>
                <div id="fountainG_2" class="fountainG"></div>
                <div id="fountainG_3" class="fountainG"></div>
            </div>
            <% } %>
        </div>
    </script>

    <script type="text/template" id="tpl_attachmentPreview">
        <div class="attachment_preview__wrap m-loading" id="<%= id %>">
            <img class="attachment_preview__item" src="<%= src %>">
        </div>
    </script>

    <script type="text/template" id="tpl_attachmentLoadError">
        <p class="attachment__error">Can't load attachment...</p>
    </script>

    <script type="text/template" id="tpl_lost_connection">
        <div class="titile">Waiting for network.</div>
        <div class="spinner"><img src="{{ asset('img/loader2.gif') }}" alt="wating"></div>
    </script>

    <script type="text/template" id="tpl_loading">
        <div class="loading__wrapper">
            <div class="loading_inner">
                <img class="loading__logo" src="{{ asset('img/loader2.gif') }}" alt="QB_logo">
                <p class="loading__description">{{ trans("pages.Loading")}}.....</p>
            </div>
        </div>
    </script>
  		
  </div>	
</div>

  
@endsection

@section('before-styles')
<link rel="stylesheet" href="{{ asset('css/plugins/quickblox/style.css') }}">
<link href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css" rel="stylesheet" integrity="sha384-wvfXpqpZZVQGK6TAh5PVlGOfQNHSoD2xbE+QkPxCAFlNEevoEH3Sl0sibVcOQVnN" crossorigin="anonymous">
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


<script>
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
	*/
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
</script>
         
@endsection