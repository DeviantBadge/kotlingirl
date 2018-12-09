package com.kotlingirl.serverconfiguration.elements.messages

import com.alibaba.fastjson.annotation.JSONField
import java.net.URI

data class MatchMakerGameResponse(
        @JSONField val server: String,
        @JSONField val gameId: Int
)