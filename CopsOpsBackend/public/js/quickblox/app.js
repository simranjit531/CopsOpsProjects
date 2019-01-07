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

App.prototype.init = function (config) {
    // Step 1. QB SDK initialization.
    QB.init(config.credentials.appId, config.credentials.authKey, config.credentials.authSecret, config.appConfig);
};

//QBconfig was loaded from QBconfig.js file
var app = new App(QBconfig);