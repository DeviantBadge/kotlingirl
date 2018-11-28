/**
 * There are a lot of magic numbers in this file
 * most of them are related to element position on canvas
 * Will be very grateful for merge request with fix of this numbers
 *
 */
var Menu = function (stage) {
    this.stage = stage;
    this.elements = [];
};

Menu.prototype.show = function () {
    this.drawBackground();
};

Menu.prototype.hide = function () {
    for (var i = 0; i < this.elements.length; i++) {
        this.stage.removeChild(this.elements[i]);
    }
    this.elements = [];
};

Menu.prototype.showGameOver = function (text) {
    this.show();
    this.showGameOverText(text);
};


Menu.prototype.drawBackground = function () {
    var canvasRect = new createjs.Graphics()
        .beginFill("rgba(0, 0, 0, 0.5)")
        .drawRect(0, 0, GM.getWidthInPixel(), GM.getHeightInPixel());

    var background = new createjs.Shape(canvasRect);
    this.stage.addChild(background);
    this.elements.push(background);
};

Menu.prototype.showGameOverText = function (text) {
    var gameOverText = new createjs.Text(text, "40px Helvetica", "#ff4444");
    gameOverText.x = (GM.getWidthInPixel() - gameOverText.getMeasuredWidth()) / 2;
    var shiftFromUpside = 60;
    gameOverText.y = shiftFromUpside;
    this.stage.addChild(gameOverText);
    this.elements.push(gameOverText);
};
