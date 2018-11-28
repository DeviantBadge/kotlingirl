var Fire = function (id, position, texture) {
    this.id = id;
    var img = texture;


    // if you have got unusual fire, put its size here
    var size = {
        // now i use this parameters
        w: 38,
        h: 38
    };

    var spriteSheet = new createjs.SpriteSheet({
        images: [img],
        frames: {
            width: size.w,
            height: size.h
        },
        animations: {
            // количество анимаций скорее всего тоже вручную вводить придется
            idle: [0, 5, 'idle', 0.4]
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

    // удаление элемента после анимации
    var self = this;
    this.bmp.addEventListener('animationend', function() {
        self.remove();
    });
};

Fire.prototype._properties = new TextureProperty()
    .setAligned(true);

Fire.prototype.animate = function (animation) {
    if(animation !== null && animation !== undefined)
        this.bmp.gotoAndPlay(animation);
    else
        this.bmp.gotoAndPlay('idle');
};

Fire.prototype.remove = function () {
    if(this.bmp.stage !== null)
        this.bmp.stage.removeChild(this.bmp);
};

Fire.prototype.update = function() {
    // empty implementation
};
