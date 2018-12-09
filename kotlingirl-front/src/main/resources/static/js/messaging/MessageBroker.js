var MessageBroker = function () {
    this.handler = {
        'Pawn': this.handlePawn,
        'Bomb': this.handleBomb,
        'Wood': this.handleTile,
        'Wall': this.handleTile,
        'Fire': this.handleFire,
        'Bonus': this.handleBonus
    }
};

MessageBroker.prototype.handleReplica = function (msg) {
    // console.log(msg);
    var gameObjects = JSON.parse(msg.data);
    // console.log(gameObjects);
    GM.game.gc(gameObjects);
};

MessageBroker.prototype.handleGameOver = function (msg) {
    GM.finishGame(msg.data);
};

MessageBroker.prototype.handlePossess = function (msg) {
    gInputEngine.possessed = parseInt(msg.data);
};

MessageBroker.prototype.handlePawn = function(obj) {
    var player = GM.game.players.find(function (el) {
        return el.id === obj.id;
    });
    var position = gMessageBroker.mirrorPosition(obj.position);
    var direction = obj.direction;
    if (player) {
        player.bmp.x = position.x;
        player.bmp.y = position.y;
        player.direction = direction;
        player.alive = obj.alive;
    } else {
        player = new Player(obj.id, position, textureManager.asset.pawn);
        // here we push to create element at screen
        GM.gameStage.addChild(player.bmp);
        // here we push to array of object, to use it in future
        GM.game.players.push(player);
    }
};

MessageBroker.prototype.handleBomb = function(obj) {
    var bomb = GM.game.bombs.find(function (el) {
        return el.id === obj.id;
    });
    var position = gMessageBroker.mirrorPosition(obj.position);

    if (bomb) {
        bomb.bmp.x = position.x;
        bomb.bmp.y = position.y;
    } else {
        bomb = new Bomb(obj.id, position, textureManager.asset.bomb);
        // here we push to create element at screen
        GM.gameStage.addChild(bomb.bmp);
        // here we push to array of object, to use it in future
        GM.game.bombs.push(bomb);
    }
};

MessageBroker.prototype.handleBonus = function(obj) {
    var bonus = GM.game.bonuses.find(function (el) {
        return el.id === obj.id;
    });
    var position = gMessageBroker.mirrorPosition(obj.position);

    if (bonus) {
        bonus.bmp.x = position.x;
        bonus.bmp.y = position.y;
    } else {

        switch (obj.bonusType) {
            case 'SPEED':
                bonus = new Bonus(obj.id, position, textureManager.asset.bonus.speed);
                break;
            case 'BOMBS':
                bonus = new Bonus(obj.id, position, textureManager.asset.bonus.bombs);
                break;
            case 'RANGE':
                bonus = new Bonus(obj.id, position, textureManager.asset.bonus.explosion);
                break;
            default:
                console.error('Smth was wrong with bonus types!');
                throw "Smth was wrong with bonus types!";
        }

        GM.gameStage.addChild(bonus.bmp);
        GM.game.bonuses.push(bonus);
    }
};

MessageBroker.prototype.handleTile = function (obj) {
    var tile = GM.game.tiles.find(function (el) {
        return el.id === obj.id;
    });
    // console.log(tile);
    var position = gMessageBroker.mirrorPosition(obj.position);
    if (tile) {
        tile.material = obj.type;
    } else {
        switch (obj.type) {
            case 'Wall':
                tile = new Tile(obj.id, position, textureManager.asset.tile.wall);
                break;
            case 'Wood':
                tile = new Tile(obj.id, position, textureManager.asset.tile.wood);
                break;
            default:
                break;
        }

        GM.gameStage.addChild(tile.bmp);
        GM.game.tiles.push(tile);
    }
};

MessageBroker.prototype.handleFire = function (obj) {
    var fire = GM.game.fires.find(function (el) {
        return el.id === obj.id;
    });

    var position = gMessageBroker.mirrorPosition(obj.position);
    if (!fire) {
        fire = new Fire(obj.id, position, textureManager.asset.fire);
        fire.animate();
        GM.gameStage.addChild(fire.bmp);
        GM.game.fires.push(fire);
    }
};

MessageBroker.prototype.mirrorPosition = function (origin) {
    return {
        x: origin.x,
        y: -origin.y + GM.getHeightInPixel() - GM.getTileSize()
    }
};

MessageBroker.prototype.move = function (direction) {
    var template = {
        topic: "MOVE",
        gameId: GM.gameId,
        data: {
            direction: direction.toUpperCase()
        }
    };

    return JSON.stringify(template);
};

MessageBroker.prototype.plantBomb = function () {
    var template = {
        topic: "PLANT_BOMB",
        gameId: GM.gameId,
        data: {}
    };

    return JSON.stringify(template);
};

// Experimental
MessageBroker.prototype.jump = function () {
    var template = {
        topic: "JUMP",
        gameId: GM.gameId,
        data: {}
    };
    return JSON.stringify(template);
};

gMessageBroker = new MessageBroker();
