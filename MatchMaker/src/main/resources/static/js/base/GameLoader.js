var GameLoader = function () {
};

GameLoader.prototype.loadAll = function () {
    textureManager.load(this._loadAfterTextures);
};

GameLoader.prototype._loadAfterTextures = function () {
    GM.load();
    GUI.load();
};


gameLoader = new GameLoader();