package com.kotlingirl.serverconfiguration.util.extensions

import com.alibaba.fastjson.JSON

fun Any.toJsonString(): String = JSON.toJSONString(this)

/**
 * Reason why i done this, because fastjson skips default constructor if it is kotlin class
 * see [com.alibaba.fastjson.util.JavaBeanInfo.build] with parameters
 * (Class<?>, Type, PropertyNamingStrategy, boolean, boolean, boolean)
 * for more information
 */
fun <T : Any> String.fromJsonString(clazz: Class<T>): T =
        JSON.parseObject(replace("\\{\\s*\\}".toRegex(),"{\"\":\"\"}"), clazz)