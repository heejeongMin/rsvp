package com.min.rsvp.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@EnableWebSecurity(debug = true)
@Configuration
class WebConfig {

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        userDetailsService: UserDetailsService,
    ): SecurityFilterChain {
        http.cors {}
            .sessionManagement { sessionManagement ->
                sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authorizeHttpRequests { authorizeRequests ->
                authorizeRequests
                    .requestMatchers("/user/login", "/user/logout", "/rsvp/form/**", "/rsvp/response/**", "/error").permitAll()
                    .anyRequest().authenticated()
            }
            .addFilterBefore(JwtAuthenticationFilter(userDetailsService), UsernamePasswordAuthenticationFilter::class.java)
            .csrf { csrf -> csrf.disable() }
        return http.build()
    }

    @Bean
    fun addCorsConfig(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedMethods("*")
                    .allowedOriginPatterns("http://localhost:3000")
                    .allowCredentials(true)
            }
        }
    }
}