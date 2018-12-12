package com.kotlingirl.serverconfiguration.elements.messages

import com.alibaba.fastjson.annotation.JSONField


data class UserRequest (
        @JSONField val userId: Long? = null,
        @JSONField val parameters: UserRequestParameters? = null
)
