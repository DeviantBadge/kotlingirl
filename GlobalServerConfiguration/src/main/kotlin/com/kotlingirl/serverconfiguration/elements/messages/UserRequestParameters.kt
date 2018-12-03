package com.kotlingirl.serverconfiguration.elements.messages

import com.alibaba.fastjson.annotation.JSONField

data class UserRequestParameters(
        @JSONField val parameter: String? = null
)