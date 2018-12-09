var RankedParameters = function () {
    this.playerAmount = 2;
    this.gameType = "ranked";
    this.credentials = {};
};

RankedParameters.prototype.reset = function (amount) {
    this.playerAmount = amount;
    return this;
};

RankedParameters.prototype.reset = function () {
    this.playerAmount = 2;
    return this;
};

