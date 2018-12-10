var ClusterSetting = function () {
    this.gameServer = {
        protocol: 'http',
        host: 'localhost',
        port: '8080',
        path: '/events/connect'
    };

    this.matchMaker = {
        protocol: 'http',
        host: 'localhost',
        port: '8080',
        path: '/matchmaker/'
    };

    this.signIn = {
        protocol: 'http',
        host: 'localhost',
        port: '8080',
        path: '/registry/signIn'
    };

    this.register = {
        protocol: 'http',
        host: 'localhost',
        port: '8080',
        path: '/registry/register'
    };
};
// todo make gui

ClusterSetting.prototype.gameServerUrl = function() {
    return makeUrl(this.gameServer)
};

ClusterSetting.prototype.matchMakerUrl = function() {
    return makeUrl(this.matchMaker)
};

ClusterSetting.prototype.signInUrl = function() {
    return makeUrl(this.signIn)
};

ClusterSetting.prototype.registerUrl = function() {
    return makeUrl(this.register)
};

function makeUrl(data) {
    return data['protocol'] + "://" + data['host'] + ":" + data['port'] + data['path']
}

var gClusterSettings = new ClusterSetting();
