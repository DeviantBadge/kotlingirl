package com.kotlingirl.serverconfiguration.util.extensions

import com.alibaba.fastjson.JSON

fun Any.toJsonString(): String = JSON.toJSONString(this)

fun <T : Any> String.fromJsonString(clazz: Class<T>): T = JSON.parseObject(this, clazz)