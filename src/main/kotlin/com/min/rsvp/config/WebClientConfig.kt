package com.min.rsvp.config


import io.netty.channel.ChannelOption
import io.netty.handler.logging.LogLevel
import io.netty.handler.timeout.ReadTimeoutHandler
import io.netty.handler.timeout.WriteTimeoutHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import reactor.netty.resources.ConnectionProvider
import reactor.netty.transport.logging.AdvancedByteBufFormat
import java.time.Duration
import java.util.concurrent.TimeUnit


@Configuration
class WebClientConfig(
    private val connectionTimeout: Int = 5000,
    private val responseTimeout: Long = 5000L,
    private val readTimeout: Long = 5000L,
    private val writeTimeout: Long = 5000L,
    private val maxIdleTimeout: Long = 3,
) {


    @Bean
    fun kakaoTokenClient(): WebClient {
        return WebClient.builder()
            .clientConnector(ReactorClientHttpConnector(getHttpClient()))
            .build()
    }

    private fun getHttpClient(): HttpClient {
        val connectionProvider: ConnectionProvider = ConnectionProvider.builder("WallexAdapterConnectionProvider")
            .maxIdleTime(Duration.ofSeconds(maxIdleTimeout))
            .build()

        var httpClient: HttpClient = HttpClient.create(connectionProvider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectionTimeout)
            .wiretap(this.javaClass.canonicalName, LogLevel.DEBUG, AdvancedByteBufFormat.TEXTUAL)
            .responseTimeout(Duration.ofMillis(responseTimeout))
            .doOnConnected { conn ->
                conn.addHandlerLast(ReadTimeoutHandler(readTimeout, TimeUnit.MILLISECONDS))
                    .addHandlerLast(WriteTimeoutHandler(writeTimeout, TimeUnit.MILLISECONDS))
            }

        return httpClient
    }
}
