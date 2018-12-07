package com.kotlingirl.serverconfiguration.elements.messages

import com.alibaba.fastjson.annotation.JSONField

data class GameServiceResponse(
        @JSONField val id: Int
)