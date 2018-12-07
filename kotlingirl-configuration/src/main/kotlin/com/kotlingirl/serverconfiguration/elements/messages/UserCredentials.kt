package com.kotlingirl.serverconfiguration.elements.messages

import com.alibaba.fastjson.annotation.JSONField

data class UserCredentials(
        @JSONField val name: String? = null
)