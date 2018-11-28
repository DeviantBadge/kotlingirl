// ***********************************************
// we can init in constructor here, because we
// don`t create an instance of loginWindow while
// page is loading
// ***********************************************

// TODO settings
var MainMenu = function () {
    this.background = null;
    this.window = null;

    this.disposed = false;
    this.initialized = false;

    this.canvas = null;
    this.stage = null;

    this.signInRequest = null;
    this.registerRequest = null;

    this.casualSettings = null;
    this.rankedSettings = null;

    this.buttonSize = 150;
    this.settingsHeight = 50;
    this.settingsOffsetTop = 275;
    this.playButtonOffsetTop = 75;

    this.casualButton = null;
    this.rankedButton = null;
    this.casualParameters = null;
    this.rankedParameters = null;
};

MainMenu.prototype.initialize = function (clusterSettings) {
    if(this.initialized)
        return;
    this.initialized = true;

    this.background = $("#mainMenu-background");
    this.window = $("#mainMenuWindow");
    this.casualSettings = $("#casualSettings");
    this.rankedSettings = $("#rankedSettings");


    this.canvas = null;
    this.stage = null;
    this.initStage();

    this.casualParameters = new CasualParameters();
    this.rankedParameters = new RankedParameters();
    this.initButtons();
};

MainMenu.prototype.initStage = function () {
    this.canvas = $("#mainMenuCanvas");
    this.stage = new createjs.Stage("mainMenuCanvas");
    this.updateStageSize();
    this.stage.enableMouseOver();

    var self = this;
    createjs.Ticker.addEventListener('tick', function (event) {
        self.update(event);
    });
};

MainMenu.prototype.initButtons = function () {
    this.initCasualSettingsButton();
    this.initRankedSettingsButton();
    this.initCasualPlayButton();
    this.initRankedPlayButton();
    this.stage.update();
};

MainMenu.prototype.initCasualSettingsButton = function () {
    this.casualSettings.css("top", this.settingsOffsetTop + "px");
    this.casualSettings.css("left", this.stage.canvas.width / 4 + "px");
    this.casualSettings.css("width", this.buttonSize + "px");
    this.casualSettings.css("height", this.settingsHeight + "px");
    // onclick = openCasualSettings
};

MainMenu.prototype.initRankedSettingsButton = function () {
    this.rankedSettings.css("top", this.settingsOffsetTop + "px");
    this.rankedSettings.css("left", this.stage.canvas.width * 3 / 4 + "px");
    this.rankedSettings.css("width", this.buttonSize + "px");
    this.rankedSettings.css("height", this.settingsHeight + "px");
    // onclick = openRankedSettings
};

MainMenu.prototype.initCasualPlayButton = function () {
    const self = this;
    this.casualButton = new PlayButton(
        this.stage.canvas.width / 4 - this.buttonSize / 2,
        this.playButtonOffsetTop,
        this.buttonSize, this.buttonSize,
        "Casual", 0, function() {
            self.playCasual();
        }
    );
    this.casualButton.activate(this.stage);
    // this.stage.canvas.width / 4 - this.buttonSize / 2, this.playButtonOffsetTop, this.buttonSize, this.buttonSize, "rgba(0, 0, 0, 0.5)"
};

MainMenu.prototype.initRankedPlayButton = function () {
    const self = this;
    this.rankedButton = new PlayButton(
        this.stage.canvas.width * 3 / 4 - this.buttonSize / 2,
        this.playButtonOffsetTop,
        this.buttonSize, this.buttonSize,
        "Ranked", 1, function() {
            self.playRanked();
        }
    );
    this.rankedButton.activate(this.stage);
    // this.stage.canvas.width * 3 / 4 - this.buttonSize / 2, this.playButtonOffsetTop, this.buttonSize, this.buttonSize, "rgba(0, 0, 0, 0.5)"
};

MainMenu.prototype.updateStageSize = function() {
    this.stage.canvas.width = this.canvas.width();
    this.stage.canvas.height = this.canvas.height();
};

MainMenu.prototype.update = function(event) {
    this.stage.update();
};

MainMenu.prototype.toggleWindow = function () {
    if(this.disposed) {
        this.background.appendTo('#game');
        this.background.toggleClass("transparent");
        this.window.toggleClass('disposed-top');
        this.disposed = false;
    } else {
        this.disposed = true;
        this.background.toggleClass("transparent");
        this.window.toggleClass('disposed-top');
    }
};

//**********************************************************
// Buttons on click functions
//**********************************************************

MainMenu.prototype.playCasual = function () {
    console.log("Wanna play casual match? hahahahahahaaaaaaaa");
    const gameID = gMatchMaker.getSessionId(this.casualParameters);
    if (gameID !== null) {
        this.toggleWindow();
        GM.startGame(gameID);
    }
};

MainMenu.prototype.playRanked = function () {
    console.log("Wanna play ranked match? hahahahahahaaaaaaaa");
    const gameID = gMatchMaker.getSessionId(this.rankedParameters);
    if (gameID !== null) {
        this.toggleWindow();
        GM.startGame(gameID);
    }
};

MainMenu.prototype.openCasualSettings = function () {
    console.log("Wanna open casual settings? hahahahahahaaaaaaaa");
};

MainMenu.prototype.openRankedSettings = function () {
    console.log("Wanna open ranked settings? hahahahahahaaaaaaaa");
};