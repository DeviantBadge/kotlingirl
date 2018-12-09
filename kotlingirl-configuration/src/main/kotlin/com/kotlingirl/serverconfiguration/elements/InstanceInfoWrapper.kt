package com.kotlingirl.serverconfiguration.elements

import org.springframework.cloud.client.ServiceInstance
import java.net.URI

class InstanceInfoWrapper(
        private val instanceId: String,
        private val serviceInstance: ServiceInstance): ServiceInstance {


    override fun getInstanceId(): String = instanceId

    override fun getServiceId(): String? = serviceInstance.serviceId

    /**
     * @return The hostname of the registered service instance.
     */
    override fun getHost(): String? = serviceInstance.serviceId

    override fun getPort(): Int = serviceInstance.port

    override fun isSecure(): Boolean = serviceInstance.isSecure

    override fun getUri(): URI? = serviceInstance.uri

    override fun getMetadata(): Map<String, String>? = serviceInstance.metadata

    override fun getScheme(): String? = serviceInstance.scheme

}