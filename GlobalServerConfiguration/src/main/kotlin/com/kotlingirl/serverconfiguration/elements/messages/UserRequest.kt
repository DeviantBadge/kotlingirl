package com.kotlingirl.serverconfiguration.elements.messages

import com.alibaba.fastjson.annotation.JSONField


data class UserRequest (
        @JSONField val credentials: UserCredentials? = null,
        @JSONField val parameters: UserRequestParameters? = null)
