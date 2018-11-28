// ***********************************************
// we can init in constructor here, because we
// don`t create an instance of loginWindow while
// page is loading
// ***********************************************

var LoginWindow = function () {
    this.background = null;
    this.window = null;
    this.loginCard = null;
    this.registerCard = null;
    this.disposed = false;
    this.initialized = false;


    this.canvas = null;
    this.stage = null;

    this.signInRequest = null;
    this.registerRequest = null;

    this.bomb = null;
    this.fire = null;
    this.blown = true;
};

LoginWindow.prototype.initialize = function (clusterSettings) {
    if(this.initialized)
        return;
    this.initialized = true;

    this.background = $("#login-background");
    this.window = $("#loginWindow");
    this.loginCard = $("#loginCard");
    this.registerCard = $("#registerCard");



    this.canvas = null;
    this.stage = null;
    this.initStage();

    this.signInRequest = {
        url: clusterSettings.signInUrl(),
        method: "POST",
        contentType: 'application/json',
        crossDomain: true,
        async: true
    };

    this.registerRequest = {
        url: clusterSettings.registerUrl(),
        method: "POST",
        contentType: 'application/json',
        crossDomain: true,
        async: true
    };

    this.toggleBomb();
};

LoginWindow.prototype.initStage = function () {
    this.canvas = $("#loginCanvas");
    this.stage = new createjs.Stage("loginCanvas");
    this.updateStageSize();
    this.stage.enableMouseOver();

    var self = this;
    createjs.Ticker.addEventListener('tick', function (event) {
        self.update(event);
    });
};

LoginWindow.prototype.updateStageSize = function() {
    this.stage.canvas.width = this.canvas.width();
    this.stage.canvas.height = this.canvas.height();
};

LoginWindow.prototype.update = function(event) {
    this.stage.update();
};

LoginWindow.prototype.openRegister = function () {
    console.log("register opened");
    this.loginCard.css("display", "none");
    this.registerCard.css("display", "block");
    this.updateStageSize();

};

LoginWindow.prototype.openSignIn = function () {
    console.log("sign in opened");
    this.loginCard.css("display", "block");
    this.registerCard.css("display", "none");
    this.updateStageSize();
};

LoginWindow.prototype.toggleWindow = function () {
    if(this.disposed) {
        this.background.appendTo('#game');
        this.background.toggleClass("transparent");
        this.window.toggleClass('disposed-top');
        this.disposed = false;
    } else {
        this.disposed = true;
        this.background.toggleClass("transparent");
        this.window.toggleClass('disposed-top');
    }
};

LoginWindow.prototype.toggleBomb = function () {
    if(this.bomb === undefined || this.bomb === null) {
        this.bomb = new Bomb(0, {x: this.window.width() / 2, y: 64}, textureManager.asset.bomb);
        this.bomb.bmp.regX += GM.getTileSize() / 2;
        this.bomb.bmp.regY += GM.getTileSize() / 2;
    }
    if (this.fire === null || this.fire === undefined) {
        this.fire = new Fire(0, {x: this.window.width() / 2, y: 64}, textureManager.asset.fire);
        this.fire.bmp.regX += GM.getTileSize() / 2;
        this.fire.bmp.regY += GM.getTileSize() / 2;
    }
    if(this.blown) {
        this.stage.addChild(this.bomb.bmp);
        this.stage.update();
        this.blown = false;
    } else {
        this.stage.removeChild(this.bomb.bmp);
        this.fire.animate();
        this.stage.addChild(this.fire.bmp);
        this.stage.update();
        this.blown = true;
    }
};

LoginWindow.prototype.signIn = function () {
    this.toggleBomb();
    GUI.toggleLoading();

    // fire request
    console.log("signing in");
    var playerName = $("#inputUsername").val();
    var playerPassword = $("#inputPassword").val();
    var userData = {
        name: playerName,
        password: playerPassword
    };

    this.signInRequest.data = JSON.stringify(userData);
    var self = this;
    $.ajax(this.signInRequest).done(function(response) {
        console.log(response);
        GM.credentials.name = playerName;
        GM.credentials.password = playerPassword;

        self.toggleBomb();
        self.toggleWindow();
        GUI.toggleLoading();
        GUI.mainMenu.toggleWindow();
    }).fail(function (jqXHR, textStatus) {
        var response = jqXHR.responseJSON;
        if(response === undefined) {
            response = jqXHR.responseText;
            if(response === undefined) {
                alert("Failed to Sign In");
            } else {
                alert(response)
            }
        } else {
            alert("Failed to SignIn .\n" +
                "Error message: " + response.errorMessage + "\n" +
                "Probable solution: " + response.solution);
        }
        self.toggleBomb();
        GUI.toggleLoading();
    });
};

LoginWindow.prototype.register = function () {
    this.toggleBomb();
    GUI.toggleLoading();

    // fire request
    console.log("registering");
    var playerName = $("#newUsername").val();
    var playerPassword = $("#newUserPassword").val();
    var passwordCopy = $("#newUserPasswordCopy").val();
    var userData = {
        name: playerName,
        password: playerPassword,
        passwordCopy: passwordCopy
    };

    this.registerRequest.data = JSON.stringify(userData);
    var self = this;
    $.ajax(this.registerRequest).done(function(response) {
        console.log(response);
        GM.credentials.name = playerName;
        GM.credentials.password = playerPassword;

        self.toggleBomb();
        self.toggleWindow();
        GUI.toggleLoading();
        GUI.mainMenu.toggleWindow();
    }).fail(function (jqXHR, textStatus) {
        var response = jqXHR.responseJSON;
        if(response === undefined) {
            response = jqXHR.responseText;
            if(response === undefined) {
                alert("Failed to Register.");
            } else {
                alert(response)
            }
        } else {
            alert("Failed to Register.\n" +
                "Error message: " + response.errorMessage + "\n" +
                "Probable solution: " + response.solution);
        }
        self.toggleBomb();
        GUI.toggleLoading();
    });
};

LoginWindow.prototype.handleResponse = function (response) {

};