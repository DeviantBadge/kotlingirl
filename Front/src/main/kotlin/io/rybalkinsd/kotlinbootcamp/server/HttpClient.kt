package io.rybalkinsd.kotlinbootcamp.server

import com.kohttp.dsl.httpPost
import io.rybalkinsd.kotlinbootcamp.server.proxies.GameServiceProxy
import io.rybalkinsd.kotlinbootcamp.util.logger
import okhttp3.Response
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class HttpClient {
    val log = logger()
    // todo how to get it automatic?
    val HOST = "localhost"
    val PORT = 8090

    @Autowired
    lateinit var gameServiceProxy: GameServiceProxy;


    fun getGameId(count: Int): Int? {
        val response: Response = httpPost {
            host = HOST
            port = PORT
            path = "/game/create"
            param {
                "playerCount" to count
            }
            body { form {  } }
        }
        if (response.isSuccessful)
            return response.body()?.string()?.toInt()
        else {
            log.warn("Request to GameService was not successful, error code: ${response.code()}, " +
                    "error message: ${response.body()?.string()}")
            return null
        }
    }

    fun startGame(gameId: Int){
        httpPost {
            host = HOST
            port = PORT
            path = "/game/start"
            param {
                "gameId" to gameId
            }
            body { form {  } }
        }
    }

    fun buildUrl() =
            "https://$HOST:$PORT"
}