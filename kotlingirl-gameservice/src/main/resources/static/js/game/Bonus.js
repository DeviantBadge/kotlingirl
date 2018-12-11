var Bonus = function (id, position, texture) {
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

Bonus.prototype._properties = new TextureProperty()
    .setAligned(true);

Bonus.prototype.remove = function() {
    if(this.bmp.stage !== null)
        this.bmp.stage.removeChild(this.bmp);
};

Bonus.prototype.update = function() {
    // empty implementation
};
