package com.kotlingirl.serverconfiguration.util.extensions

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.serializer.SerializerFeature

/*
internal object JsonHelper {
    val mapper = ObjectMapper().registerKotlinModule()

    fun toJson(data: Any): String = mapper.writeValueAsString(data)

    inline fun <reified T: Any> fromJson(json: String): T =
            mapper.readValue(json)
}
*/

fun Any.toJsonString(): String = JSON.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect)

/**
 * Reason why i done this, because fastjson skips default constructor if it is kotlin class
 * see [com.alibaba.fastjson.util.JavaBeanInfo.build] with parameters
 * (Class<?>, Type, PropertyNamingStrategy, boolean, boolean, boolean)
 * for more information
 */
fun <T : Any> String.fromJsonString(clazz: Class<T>): T =
        JSON.parseObject(replace("\\{\\s*\\}".toRegex(),"{\"\":\"\"}"), clazz)

inline fun <reified T : Any> String.fromJsonString(): T =
        JSON.parseObject(replace("\\{\\s*\\}".toRegex(),"{\"\":\"\"}"), T::class.java)