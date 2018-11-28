// TODO make time past from loading beginning
var LoadingScreen = function () {
    this.initialized = false;
    this.disposed = false;
    this.loadingCanvas = null;
    this.loadingStage = null;
    this.boy = null;
    this.boyRadius = 50;
    this.loopDuration = 2; // 5 seconds
};

LoadingScreen.prototype.initialize = function () {
    if(this.initialized)
        return;
    this.initialized = true;

    this.initStage();
    this.drawBackground();
    this.initBoy();
    this.activateListener();
};

LoadingScreen.prototype.initStage = function () {
    this.loadingCanvas = $("#loadingScreen");
    this.loadingStage = new createjs.Stage("loadingScreen");
    this.loadingStage.canvas.width = this.loadingCanvas.width();
    this.loadingStage.canvas.height = this.loadingCanvas.height();
    this.loadingStage.enableMouseOver();
};

LoadingScreen.prototype.activateListener = function () {
    // todo centralise that
    var self = this;
    createjs.Ticker.addEventListener('tick', function (event) {
        self.update(event);
    });
};

LoadingScreen.prototype.update = function(event) {
    this.updateBoyRotation(event.delta);
    this.loadingStage.update();
};

LoadingScreen.prototype.updateBoyRotation = function(delta) {
    this.boy.bmp.rotation += (360 * delta) / (1000 * this.loopDuration);
};

LoadingScreen.prototype.toggleWindow = function () {
    if(this.disposed) {
        this.loadingCanvas.css("display", "block");
        this.disposed = false;
    } else {
        this.loadingCanvas.css("display", "none");
        this.disposed = true;
    }
};

LoadingScreen.prototype.initBoy = function () {
    if(this.boy !== null && this.boy !== undefined) {
        this.loadingStage.removeChild(this.boy.bmp);
        this.boy = null;
    }
    this.boy = new Player(0, {x: GM.getWidthInPixel() / 2, y: GM.getHeightInPixel() / 2}, textureManager.asset.pawn);
    this.boy.animate("right");
    this.boy.bmp.regX += GM.getTileSize() / 2;
    this.boy.bmp.regY += GM.getTileSize() / 2 + this.boyRadius;

    this.loadingStage.addChild(this.boy.bmp);
    this.loadingStage.update();
};

LoadingScreen.prototype.drawBackground = function () {
    var canvasRect = new createjs.Graphics()
        .beginFill("rgba(0, 0, 0, 0.5)")
        .drawRect(0, 0, this.loadingStage.canvas.width, this.loadingStage.canvas.height);

    var background = new createjs.Shape(canvasRect);
    this.loadingStage.addChild(background);
};