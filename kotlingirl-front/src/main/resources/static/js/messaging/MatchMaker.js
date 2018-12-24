var MatchMaker = function (clusterSetting) {
    this.clusterSettings = clusterSetting;
};

MatchMaker.prototype.getSettings = function (parameters) {
    return {
        // url: this.clusterSettings.matchMakerPath() + parameters.gameType,
        url: "/play/" + parameters.gameType,
        method: "POST",
        contentType: 'application/json',
        crossDomain: true,
        async: false
    };
};

MatchMaker.prototype.getGame = function (parameters) {
    GUI.toggleLoading();
    switch (parameters.gameType) {
        case "casual":
        case "ranked":
            break;
        default:
            console.error("Unknown game type - " + parameters.gameType);
            return;
    }
    GM.credentials.name = Math.floor((1 + Math.random()) * 0x10000).toString(16).substring(1);
    parameters.userId = GM.userId;
    var settings = this.getSettings(parameters);
    settings.data = JSON.stringify(parameters);

    console.log(JSON.stringify(parameters));

    var mmResponse = null;
    $.ajax(settings).done(function(response) {
        mmResponse = response;
        console.log("response - " + response);
        GUI.toggleLoading();
    }).fail(function (jqXHR, textStatus) {
        alert("Matchmaker request failed");
        GUI.toggleLoading();
    });

    return mmResponse;
};

gMatchMaker = new MatchMaker(gClusterSettings);