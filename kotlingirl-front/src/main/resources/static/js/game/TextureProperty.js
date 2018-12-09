var TextureProperty = function () {
    this._aligned = false;
    this._alignWidth = GM.getTileSize();
    this._alignHeight = GM.getTileSize();

    this._resized = false;
    this._width = GM.getTileSize();
    this._height = GM.getTileSize();
};

TextureProperty.prototype.setAligned = function (bool) {
    this._aligned = bool;
    return this;
};
TextureProperty.prototype.isAligned = function () {
    return this._aligned;
};

TextureProperty.prototype.setResized = function (bool) {
    this._resized = bool;
    return this;
};
TextureProperty.prototype.isResized = function () {
    return this._resized;
};

TextureProperty.prototype.setAlignWidth = function (integer) {
    this._alignWidth = integer;
    return this;
};
TextureProperty.prototype.getAlignWidth = function () {
    return this._alignWidth;
};

TextureProperty.prototype.setAlignHeight = function (integer) {
    this._alignHeight = integer;
    return this;
};
TextureProperty.prototype.getAlignHeight = function () {
    return this._alignHeight;
};

TextureProperty.prototype.setWidth = function (integer) {
    this._width = integer;
    return this;
};
TextureProperty.prototype.getWidth = function () {
    return this._width;
};

TextureProperty.prototype.setHeight = function (integer) {
    this._height = integer;
    return this;
};
TextureProperty.prototype.getHeight = function () {
    return this._height;
};