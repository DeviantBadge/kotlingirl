package com.kotlingirl.serverconfiguration.elements.messages

import com.alibaba.fastjson.annotation.JSONField

data class UserRequest (
        @JSONField val name: String?
)