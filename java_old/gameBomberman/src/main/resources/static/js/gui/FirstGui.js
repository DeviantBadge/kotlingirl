var FirstGui = function () {
    this.loginWindow = null;
    this.mainMenu = null;
    this.loadingScreen = null;
};

FirstGui.prototype.load = function () {
    this.loginWindow = new LoginWindow();
    this.loginWindow.initialize(gClusterSettings);

    this.mainMenu = new MainMenu();
    this.mainMenu.initialize(gClusterSettings);
    this.mainMenu.toggleWindow();

    this.loadingScreen = new LoadingScreen();
    this.loadingScreen.initialize();
    this.loadingScreen.toggleWindow();
};

FirstGui.prototype.toggleLoading = function () {
    this.loadingScreen.toggleWindow();
};

GUI = new FirstGui();