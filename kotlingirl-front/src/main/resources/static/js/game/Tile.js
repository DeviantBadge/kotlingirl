var Tile = function (id, position, texture) {
    this.id = id;
    var img = texture;

    this.bmp = new createjs.Bitmap(img);

    if(this._properties.isAligned()) {
        this.bmp.regX = (img.width - this._properties.getAlignHeight()) / 2;
        this.bmp.regY = (img.height - this._properties.getAlignHeight()) / 2;
    }

    if(this._properties.isResized()) {
        this.bmp.scaleX = this._properties.getWidth() / img.width;
        this.bmp.scaleY = this._properties.getHeight()/ img.height;
    }

    this.bmp.x = position.x;
    this.bmp.y = position.y;
};

Tile.prototype._properties = new TextureProperty()
    .setAligned(true);

Tile.prototype.remove = function () {
    if(this.bmp.stage !== null)
        this.bmp.stage.removeChild(this.bmp);
};

Tile.prototype.update = function() {
    // empty implementation
};

// todo это касается всех объектов, можно попробовать их все таки инициализировать красиво, не дублируя везде код