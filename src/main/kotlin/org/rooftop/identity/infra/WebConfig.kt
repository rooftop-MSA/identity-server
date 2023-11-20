package org.rooftop.identity.infra

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.converter.protobuf.ProtobufHttpMessageConverter


@Configuration
class WebConfig {

    @Bean
    fun protobufMessageConverter(): ProtobufHttpMessageConverter = ProtobufHttpMessageConverter()
}
