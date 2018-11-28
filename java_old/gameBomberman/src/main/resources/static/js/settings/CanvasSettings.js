var CanvasSettings = function () {
    this._tileSize = 32;
    this._tiles = {
        w: 27,
        h: 17
    };
};

CanvasSettings.prototype.getTileSize = function () {
    return this._tileSize;
};

CanvasSettings.prototype.getWidthInPixel = function () {
    return this._tiles.w * this._tileSize;
};

CanvasSettings.prototype.getWidthInTiles = function () {
    return this._tiles.w;
};

CanvasSettings.prototype.getHeightInPixel = function () {
    return this._tiles.h * this._tileSize;
};

CanvasSettings.prototype.getHeightInTiles = function () {
    return this._tiles.h;
};
