'use strict';
/*
 * Before start chatting you need to follow this steps:
 * 1. Initialize QB SDK ( QB.init() );
 * 2. Create user session (QB.createSession());
 * 3. Connect to the chat in the create session callback (QB.chat.connect());
 * 4. Set listeners;
 */

function App(config) {
    this._config = config;
    this.user = null;
    this.token = null;
    this.isDashboardLoaded = false;
    this.room = null;
    // Elements
    this.page = document.querySelector('#page');
    this.sidebar = null;
    this.content = null;
    this.userListConteiner = null;
    this.init(this._config);
    this.loading = true;
}

// Before start working with JS SDK you nead to init it.

App.prototype.init = function (config) {
    // Step 1. QB SDK initialization.
    QB.init(config.credentials.appId, config.credentials.authKey, config.credentials.authSecret, config.appConfig);
};

App.prototype.renderDashboard = function (activeTabName) {
    var self = this,
        renderParams = {
            user: self.user,
            tabName: ''
        };

    if(activeTabName){
        renderParams.tabName = activeTabName;
    }

    helpers.clearView(app.page);

    self.page.innerHTML = helpers.fillTemplate('tpl_dashboardContainer', renderParams);

    var logoutBtn = document.querySelector('.j-logout');
    loginModule.isLoginPageRendered = false;
    self.isDashboardLoaded = true;
    self.content = document.querySelector('.j-content');
    self.sidebar = document.querySelector('.j-sidebar');

    dialogModule.init();

    self.loadWelcomeTpl();

    listeners.setListeners();
    
    /*
    logoutBtn.addEventListener('click', function () {
        QB.users.delete(app.user.id, function(err, user){
            if (err) {
                console.error('Can\'t delete user by id: '+app.user.id+' ', err);
            }

            loginModule.isLogin = false;
            app.isDashboardLoaded = false;

            localStorage.removeItem('user');
            helpers.clearCache();

            QB.chat.disconnect();
            QB.destroySession();
            
            router.navigate('#!/login');
        });
    });
	*/
    this.tabSelectInit();
};

App.prototype.loadWelcomeTpl = function () {
    
    var content = document.querySelector('.j-content'),
        welcomeTpl = helpers.fillTemplate('tpl_welcome');
    
    helpers.clearView(content);
    content.innerHTML = welcomeTpl;
};

App.prototype.tabSelectInit = function () {
		
    var self = this,
        tabs = document.querySelectorAll('.j-sidebar__tab_link');
    
    _.each(tabs, function (item) {
        item.addEventListener('click', function (e) {
            e.preventDefault();
            if (!self.checkInternetConnection()) {
                return false;
            }
                        
            var tabs = document.querySelectorAll('.j-sidebar__tab_link');
            var tab = e.currentTarget;
            _.each(tabs, function (elem) {
                elem.classList.remove('active');
            });

            if($(e.currentTarget).data('type') == 'chat')
        	{            	
            	self.loadChatList(tab);
        	}
            else if($(e.currentTarget).data('type') == 'create')
        	{
            	var tabs = document.querySelectorAll('.j-sidebar__tab_link');
                
                if (tab.classList.contains('active')) {
                    return false;
                }

                _.each(tabs, function (elem) {
                    elem.classList.remove('active');
                });

                tab.classList.add('active');
                
            	self.showUserList();
        	}
        });
    });
};

App.prototype.loadChatList = function (tab) {
    return new Promise(function(resolve, reject){
        var tabs = document.querySelectorAll('.j-sidebar__tab_link');
        
        if (tab.classList.contains('active')) {
            return false;
        }

        _.each(tabs, function (elem) {
            elem.classList.remove('active');
        });

        tab.classList.add('active');
        
        helpers.clearView(dialogModule.dialogsListContainer);
        dialogModule.dialogsListContainer.classList.remove('full');

        dialogModule.loadDialogs(tab.dataset.type).then(function(dialogs) {
            resolve(dialogs);
        }).catch(function(error){
            reject(error);
        });
    });
};

App.prototype.buildCreateDialogTpl = function () {
    var self = this,
        createDialogTPL = helpers.fillTemplate('tpl_newGroupChat');    	
    
    helpers.clearView(self.content);
    
    self.content.innerHTML = createDialogTPL;
    
//    var backToDialog = self.content.querySelector('.j-back_to_dialog');
    
//    backToDialog.addEventListener('click', self.backToDialog.bind(self));
    
    self.userListConteiner = self.content.querySelector('.j-group_chat__user_list');
    
    document.forms.create_dialog.addEventListener('submit', function (e) {
        e.preventDefault();
        
        if (!self.checkInternetConnection()) {
            return false;
        }
        
        if (document.forms.create_dialog.create_dialog_submit.disabled) return false;
        
        document.forms.create_dialog.create_dialog_submit.disabled = true;
        
        var users = self.userListConteiner.querySelectorAll('.selected'),
            type = users.length > 2 ? 2 : 3,
            name = document.forms.create_dialog.dialog_name.value,
            occupants_ids = [];

        _.each(users, function (user) {
            if (user.id !== self.user.id) {
                occupants_ids.push(user.id);
            }
        });

        if (!name && type === 2) {
            var userNames = [];
            
            _.each(occupants_ids, function (id) {
                if (id === self.user.id) {
                    userNames.push(self.user.name || self.user.login);
                } else {
                    userNames.push(userModule._cache[id].name);
                }
            });
            name = userNames.join(', ');
        }

        var params = {
            type: type,
            occupants_ids: occupants_ids.join(',')
        };
        
        if (type !== 3 && name) {
            params.name = name;
        }

        dialogModule.createDialog(params);
    });

    document.forms.create_dialog.dialog_name.addEventListener('input', function(e){
        var titleText = document.forms.create_dialog.dialog_name.value,
            sylmbolsCount = titleText.length;
        if(sylmbolsCount > 40) {
            document.forms.create_dialog.dialog_name.value = titleText.slice(0, 40);
        }
    });
    userModule.initGettingUsers();
};

App.prototype.backToDialog = function (e) {
    var self = this;
    self.sidebar.classList.add('active');
    document.querySelector('.j-sidebar__create_dialog').classList.remove('active');
    
    if (dialogModule.dialogId) {
        router.navigate('/dialog/' + dialogModule.dialogId);
    } else {
        router.navigate('/dashboard');
    }
};

App.prototype.noInternetMessage = function () {
    var notifications = document.querySelector('.j-notifications');
    
    notifications.classList.remove('hidden');
    notifications.innerHTML = helpers.fillTemplate('tpl_lost_connection');
};

App.prototype.checkInternetConnection = function () {
    if (!navigator.onLine) {
        alert('No internet connection!');
        return false;
    }
    return true;
};

App.prototype.showUserList = function () {
    
	var user = JSON.parse(localStorage.getItem('user'));
	
	var self = this,
    params = {
        tags: user.tag_list,
        per_page: 100
    };

	return new Promise(function(resolve, reject){
    QB.users.get(params, function (err, responce) {
        if (err) {
            reject(err);
        }

        var userList = responce.items.map(function(data){
            return userModule.addToCache(data.user);
        });
        
        var html = '';
        $(userList).each(function(k,v){
        	var disabled = ''; if(app.user.id == v.id) { disabled=' disabled'; }
        	html +='<div class="user__item_1'+disabled+'" id="'+v.id+'" style="border-bottom: 2px solid #e7e7e7;">';
        	html +='<span class="user__avatar m-user__img_10" style="display:none;">';
        	html +='<i class="material-icons m-user_icon">account_circle</i>';
        	html +='</span>';
        	html +='<div class="user__details">';
    		html +='<p class="user__name">'+v.name+'</p>';                
			html +='<p class="user__last_seen">'+v.last_request_at+'</p>';                
			html +='</div>';
			html +='</div>';
        });
        
        html +='<form action="" name="create_dialog" class="dialog_form m-dialog_form_create j-create_dialog_form" style="display:none;">';
		html +='<input class="dialog_name" name="dialog_name" type="text" autocomplete="off" autocorrect="off" autocapitalize="off" placeholder="Add conversation name" disabled="">';
		html +='<button class="btn m-create_dialog_btn j-create_dialog_btn" type="submit" name="create_dialog_submit">create</button>';
		html +='</form>';
		
        $('.j-sidebar__dilog_list').html(html);
        
        
        
        $(document).on('click', '.user__item_1', function(e){
        	
        	$('.user__item_1').each(function(){
            	console.log("Removed");
            	$(this).removeClass('selected');
            })
            
        	$(this).addClass('selected');

//        	$('button[type="submit"][name="create_dialog_submit"]').trigger('click');
        	
        	
        	userModule.content = document.querySelector('.j-sidebar');
            userModule.userListConteiner = userModule.content.querySelector('.j-sidebar__dilog_list');
        	
        	
        	document.forms.create_dialog.create_dialog_submit.disabled = true;
            
            var users = userModule.userListConteiner.querySelectorAll('.selected'),
                type = users.length > 2 ? 2 : 3,
                name = document.forms.create_dialog.dialog_name.value,
                occupants_ids = [];
                
            _.each(users, function (user) {
                occupants_ids.push(user.id);
            });
            console.log(occupants_ids);
            if (!name && type === 2) {
                var userNames = [];
                
                _.each(occupants_ids, function (id) {
                    if (id === self.user.id) {
                        userNames.push(self.user.name || self.user.login);
                    } else {
                        userNames.push(userModule._cache[id].name);
                    }
                });
                name = userNames.join(', ');
            }

            var params = {
                type: type,
                occupants_ids: occupants_ids.join(',')
            };
            
            if (type !== 3 && name) {
                params.name = name;
            }
            console.log(params);
            
            QB.chat.dialog.create(params, function (err, createdDialog) {
                if (err) {
                    console.error(err);
                } else {
                	console.log(createdDialog._id);
                	router.navigate('#!/dialog/'+createdDialog._id);
//                	dialogModule.renderMessages(createdDialog._id);
                } 	
            });
        });
        	       
        resolve(userList);
    });
});
};



// QBconfig was loaded from QBconfig.js file
var app = new App(QBconfig);
