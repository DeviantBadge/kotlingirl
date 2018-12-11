var MatchMaker = function (clusterSetting) {
    this.clusterSettings = clusterSetting;
};

MatchMaker.prototype.getSettings = function (parameters) {
    return {
        url: this.clusterSettings.matchMakerUrl() + parameters.gameType,
        method: "POST",
        contentType: 'application/json',
        crossDomain: true,
        async: false
    };
};

MatchMaker.prototype.getGame = function (parameters) {
    switch (parameters.gameType) {
        case "casual":
        case "ranked":
            break;
        default:
            console.error("Unknown game type - " + parameters.gameType);
            return;
    }
    GM.credentials.name = Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
    parameters.credentials = GM.credentials;
    var settings = this.getSettings(parameters);
    settings.data = JSON.stringify(parameters);

    console.log(JSON.stringify(parameters));

    var mmResponse = null;
    $.ajax(settings).done(function(response) {
        mmResponse = response;
        console.log("response - " + response);
    }).fail(function (jqXHR, textStatus) {
        alert("Matchmaker request failed");
    });

    return mmResponse;
};

gMatchMaker = new MatchMaker(gClusterSettings);