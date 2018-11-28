var ServerProxy = function () {
    this.handler = {
        'REPLICA': gMessageBroker.handleReplica,
        'POSSESS': gMessageBroker.handlePossess,
        'GAME_OVER': gMessageBroker.handleGameOver
    };
};



ServerProxy.prototype.setupMessaging = function() {
    var self = this;
    gInputEngine.subscribe('up', function () {
        self.socket.send(gMessageBroker.move('up'))
    });
    gInputEngine.subscribe('down', function () {
        self.socket.send(gMessageBroker.move('down'))
    });
    gInputEngine.subscribe('left', function () {
        self.socket.send(gMessageBroker.move('left'))
    });
    gInputEngine.subscribe('right', function () {
        self.socket.send(gMessageBroker.move('right'))
    });
    gInputEngine.subscribe('bomb', function () {
        self.socket.send(gMessageBroker.plantBomb());
    });
    gInputEngine.subscribe('jump', function () {
        self.socket.send(gMessageBroker.jump());
    });
};

ServerProxy.prototype.connectToGameServer = function(gameId) {
    this.socket = new SockJS(gClusterSettings.gameServerUrl()
        + "?name=" + GM.credentials.name
        + "&gameId=" + GM.gameId
    );
    var self = this;
    var prepared = false;

    this.socket.onmessage = function (event) {
        var msg = JSON.parse(event.data);
        if (self.handler[msg.topic] === undefined) {
            return;
        }
        self.handler[msg.topic](msg);
    };

    this.socket.onopen = function () {
        console.log("Connected");
        while (!prepared){
        }

        var template = {
            topic: "READY",
            gameId: GM.gameId,
            data: {
            }
        };
        self.socket.send(JSON.stringify(template));
    };

    this.socket.onclose = function (event) {
        console.log('Code: ' + event.code + ' cause: ' + event.reason);
    };

    this.socket.onerror = function (error) {
        console.log("Error " + error.message);
    };

    this.setupMessaging();
    prepared = true;
};
