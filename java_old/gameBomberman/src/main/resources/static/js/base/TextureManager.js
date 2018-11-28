var TextureManager = function () {
    this.asset = {
        pawn: null,
        bomb: null,
        fire: null,
        tile: {},
        bonus: {}
    };
};

// TODO several skins
TextureManager.prototype.load = function (afterload) {
    var queue = new createjs.LoadQueue();
    var self = this;
    queue.addEventListener("complete", function () {
        self.asset.pawn = queue.getResult("pawn");
        self.asset.tile.grass = queue.getResult("tile_grass");
        self.asset.tile.wall = queue.getResult("tile_wall");
        self.asset.tile.wood = queue.getResult("tile_wood");
        self.asset.bomb = queue.getResult("bomb");
        self.asset.fire = queue.getResult("fire");
        self.asset.bonus.speed = queue.getResult("bonus_speed");
        self.asset.bonus.bombs = queue.getResult("bonus_bomb");
        self.asset.bonus.explosion = queue.getResult("bonus_explosion");
        if(afterload !== undefined)
            afterload();
    });
    queue.loadManifest([
        {id: "pawn", src: "img/betty.png"},
        {id: "tile_grass", src: "img/tile_grass.png"},
        {id: "tile_wall", src: "img/tile_wall.png"},
        {id: "tile_wood", src: "img/crateWood.png"},
        {id: "bomb", src: "img/bomb.png"},
        {id: "fire", src: "img/fire.png"},
        {id: "bonus_speed", src: "img/bonus_speed.png"},
        {id: "bonus_bomb", src: "img/bonus_bomb.png"},
        {id: "bonus_explosion", src: "img/bonus_explosion.png"}
    ]);
};

textureManager = new TextureManager();