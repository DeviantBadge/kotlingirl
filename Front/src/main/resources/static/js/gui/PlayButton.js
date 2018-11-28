var PlayButton = function (x, y, width, height, text, animNum, onclick) {
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;

    this.buttonText = text;
    this.elements = [];
    this.animationNum = animNum;
    this.onclick = onclick;

    this.pawn1 = null;
    this.pawn2 = null;
    this.background = null;

    this.textDownShift = 20;
    this.pawnMargin = 20;

    this._initElements();
};

PlayButton.prototype.activate = function(stage) {
    for (var i = 0; i < this.elements.length; i++) {
        stage.addChild(this.elements[i]);
    }
};

PlayButton.prototype.deactivate = function() {
    for (var i = 0; i < this.elements.length; i++) {
        if(this.elements[i].stage !== null)
            this.elements[i].stage.removeChild(this.elements[i]);
    }
};

PlayButton.prototype._initElements = function() {
    this._initRect("rgba(0, 0, 0, 0.5)");
    this._initPlayText();
    this._initAnimation();
};

PlayButton.prototype._initAnimation = function() {
    this._initPawns();
    switch (this.animationNum) {
        case 0:
            this.pawn1.animate("down");
            this.pawn2.animate("down");
            break;
        case 1:
            this.pawn1.animate("right");
            this.pawn2.animate("left");
            break;
        default:
            this.pawn1.animate("idle");
            this.pawn2.animate("idle");
            break;
    }
};

PlayButton.prototype._initPawns = function() {
    var position = {x: 0, y: 0};
    position.x = this.x + (this.width - this.pawnMargin) / 2 - GM.getTileSize();
    position.y = this.y + this.height / 3 - GM.getTileSize() / 2;
    this.pawn1 = new Player(1, position, textureManager.asset.pawn);
    position.x = position.x + this.pawnMargin + GM.getTileSize();
    this.pawn2 = new Player(1, position, textureManager.asset.pawn);
    this.elements.push(this.pawn1.bmp, this.pawn2.bmp);
};

PlayButton.prototype._initPlayText = function () {
    var playText = new createjs.Text(this.buttonText, "32px Helvetica", "#ff4444");
    playText.x = this.x + (this.width - playText.getMeasuredWidth()) / 2;
    playText.y = (this.y + this.height) - (playText.getMeasuredHeight() + this.textDownShift);
    this.elements.push(playText);
};

PlayButton.prototype._initRect = function (color) {
    var canvasRect = new createjs.Graphics()
        .beginFill(color)
        .drawRect(this.x, this.y, this.width, this.height);
    this.background = new createjs.Shape(canvasRect);

    this.elements.push(this.background);
    this.setCursor(this.background);

    this.background.addEventListener('click', this.onclick);
};

PlayButton.prototype.setOnClick = function (onclick) {
    this.onclick = onclick;
    this.background.addEventListener('click', onclick);
};

PlayButton.prototype.setCursor = function (btn) {
    btn.addEventListener('mouseover', function () {
        document.body.style.cursor = 'pointer';
    });
    btn.addEventListener('mouseout', function () {
        document.body.style.cursor = 'auto';
    });
};
