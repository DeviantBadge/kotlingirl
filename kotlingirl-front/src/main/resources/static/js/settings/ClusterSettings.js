var ClusterSetting = function () {
    this.gameServer = {
        protocol: 'http',
        host: 'localhost',
        port: '8762',
        path: '/events/connect'
    };

    this.matchMaker = {
        protocol: 'http',
        host: 'localhost',
        port: '8762',
        path: '/play/'
    };

    this.signIn = {
        protocol: 'http',
        host: 'localhost',
        port: '8762',
        path: '/users/signIn'
    };

    this.register = {
        protocol: 'http',
        host: 'localhost',
        port: '8762',
        path: '/users/register'
    };
};
// todo make gui

ClusterSetting.prototype.gameServerPath = function() {
    return makePath(this.gameServer)
};

ClusterSetting.prototype.matchMakerPath = function() {
    return makePath(this.matchMaker)
};

ClusterSetting.prototype.signInPath = function() {
    return makePath(this.signIn)
};

ClusterSetting.prototype.registerPath = function() {
    return makePath(this.register)
};

function makeUrl(data) {
    return data['protocol'] + "://" + data['host'] + ":" + data['port'] + data['path']
}

function makePath(data) {
    return data['path']
}

var gClusterSettings = new ClusterSetting();
