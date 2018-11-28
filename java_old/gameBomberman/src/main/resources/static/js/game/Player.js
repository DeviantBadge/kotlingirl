var Player = function (id, position, texture) {
    this.id = id;
    var img = texture;
    this.alive = true;

    // if you have got unusual pawn, put its size here
    var size = {
        // now i use this parameters
        w: 48,
        h: 48
    };

    var spriteSheet = new createjs.SpriteSheet({
        images: [img],
        frames: {
            width: size.w,
            height: size.h
        },
        animations: {
            idle: [0, 0, 'idle'],
            down: [0, 3, 'down', 0.2],
            left: [4, 7, 'left', 0.2],
            up:   [8, 11, 'up', 0.2],
            right: [12, 15, 'right', 0.2],
            dead: [16, 16, 'dead']
        }
    });

    this.bmp = new createjs.Sprite(spriteSheet);

    if(this._properties.isAligned()) {
        this.bmp.regX = (size.w - this._properties.getAlignHeight()) / 2;
        this.bmp.regY = (size.h - this._properties.getAlignHeight()) / 2;
    }

    if(this._properties.isResized()) {
        this.bmp.scaleX = this._properties.getWidth() / size.w;
        this.bmp.scaleY = this._properties.getHeight()/ size.h;
    }

    this.bmp.x = position.x;
    this.bmp.y = position.y;

    // активация анимации
    this.bmp.gotoAndPlay('idle');
};

Player.prototype._properties = new TextureProperty()
    .setAligned(true);

Player.prototype.remove = function () {
    if(this.bmp.stage !== null)
        this.bmp.stage.removeChild(this.bmp);
};

Player.prototype.animate = function (animation) {
    if (!this.bmp.currentAnimation || this.bmp.currentAnimation.indexOf(animation) === -1) {
        this.bmp.gotoAndPlay(animation);
    }
};

Player.prototype.update = function () {
    if (!this.alive) {
        this.animate('dead');
        return;
    }

    switch (this.direction) {
        case 'UP':
            this.animate('up');
            break;
        case 'DOWN':
            this.animate('down');
            break;
        case 'LEFT':
            this.animate('left');
            break;
        case 'RIGHT':
            this.animate('right');
            break;
        default:
            this.animate('idle');
            break;
    }
};
