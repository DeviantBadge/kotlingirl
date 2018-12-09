package com.kotlingirl.serverconfiguration

object GameServiceConstants {
    // **************************************************************************************************
    //                                  Standard http endpoints
    // **************************************************************************************************
    const val GAME_PATH = "/game"
    const val CREATE_PATH = "/create"
    const val APPEND_PLAYER_PATH = "/append_player"
    const val START_PATH = "/start"

    // **************************************************************************************************
    //                                  Standard ws in-game endpoints
    // **************************************************************************************************
    const val WEB_SOCKET_PATH = "/events/connect"
}