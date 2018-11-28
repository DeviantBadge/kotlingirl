var Bomb = function (id, position, texture) {
    this.id = id;
    var img = texture;
    // todo this.strength = strength;

    // if you have got unusual bomb, put its size here
    var size = {
        // now i use this parameters
        w: 28,
        h: 28
    };

    var spriteSheet = new createjs.SpriteSheet({
        images: [img],
        frames: {
            width: size.w,
            height: size.h
        },
        animations: {
            // количество анимаций скорее всего тоже вручную вводить придется
            idle: [0, 4, "idle", 0.2]
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

Bomb.prototype._properties = new TextureProperty()
    .setAligned(true);

Bomb.prototype.remove = function() {
    if(this.bmp.stage !== null)
        this.bmp.stage.removeChild(this.bmp);
};

Bomb.prototype.update = function () {
  // empty implementation
};
