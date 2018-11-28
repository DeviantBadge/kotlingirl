// todo - make something like admin viewer? where you can see game states
var GameMaster = function () {
    this.asset = {
        pawn: null,
        bomb: null,
        fire: null,
        tile: {},
        bonus: {}
    };

    this.menu = null;
    this.game = null;
    this.gameStage = null;
    this.gameField = null;

    this.credentials = {
        name : "",
        password : ""
    };
    this.gameId = "";
};

// ************************************************
// here we change size of game field
// ************************************************

GameMaster.prototype = new CanvasSettings();

GameMaster.prototype.setWidthInTiles = function(width) {
    this._tiles.w = width.toFixed();
    this.updateGameSize();
};

GameMaster.prototype.setHeightInTiles = function(height) {
    this._tiles.w = height.toFixed();
    this.updateGameSize();
};

// size must be as {w: width, h: height}
GameMaster.prototype.setSizeInTiles = function(size) {
    this._tiles = size;
    this.updateGameSize();
};

GameMaster.prototype.setWidthInPixels = function(width) {
    this._tiles.w = (width / 32).toFixed();
    this.updateGameSize();
};

GameMaster.prototype.setHeightInPixels = function(height) {
    this._tiles.w = (height / 32).toFixed();
    this.updateGameSize();
};

// size must be as {w: width, h: height}
GameMaster.prototype.setSizeInPixels = function(size) {
    this._tiles = {
        w: (size.w / 32).toFixed(),
        h: (size.h / 32).toFixed()
    };
    this.updateGameSize();
};

GameMaster.prototype.updateGameSize = function() {
    this.gameStage.canvas.height = this.getHeightInPixel();
    this.gameStage.canvas.width = this.getWidthInPixel();
    this.gameField.css('width' , this.getWidthInPixel() + 'px', 'important');
    this.gameField.css('height', this.getHeightInPixel() + 'px', 'important');
};


// ************************************************
// load
// ************************************************

GameMaster.prototype.load = function () {
    this.gameStage = loadStage();
    this.gameField = $("#game");

    this.updateGameSize();
    this.initCanvas();
};

function loadStage() {
    var stage = new createjs.Stage("canvas");
    stage.enableMouseOver();
    return stage;
}

GameMaster.prototype.initCanvas = function () {
    this.menu = new Menu(this.gameStage);
    this.menu.show();
    this.gameStage.update();
};

// ************************************************
// here we manage our game state
// ************************************************

GameMaster.prototype.startGame = function (gameID) {
    this.menu.hide();
    this.gameId = gameID;
    this.game = new Game(this.gameStage);
    this.game.start(gameID);
};

GameMaster.prototype.finishGame = function (gameOverText) {
    if (this.game !== null) {
        this.game.finish();
        this.game = null;
    }
    this.menu.showGameOver(gameOverText);
    this.gameStage.update();
};

GM = new GameMaster();