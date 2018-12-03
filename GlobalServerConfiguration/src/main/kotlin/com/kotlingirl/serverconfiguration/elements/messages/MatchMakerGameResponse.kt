package com.kotlingirl.serverconfiguration.elements.messages

import com.alibaba.fastjson.annotation.JSONField
import java.net.URI

data class MatchMakerGameResponse(
        @JSONField val serviceUri: URI,
        @JSONField val gameId: Int
)