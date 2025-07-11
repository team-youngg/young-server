package com.young.global.config

import com.young.global.security.jwt.filter.JwtAuthenticationFilter
import com.young.global.security.jwt.filter.JwtExceptionFilter
import com.young.global.security.jwt.handler.JwtAccessDeniedHandler
import com.young.global.security.jwt.handler.JwtAuthenticationEntryPoint
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.access.hierarchicalroles.RoleHierarchy
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource


@Configuration
class SecurityConfig(
    private val jwtAccessDeniedHandler: JwtAccessDeniedHandler,
    private val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    private val jwtAuthenticationFilter: JwtAuthenticationFilter,
    private val jwtExceptionFilter: JwtExceptionFilter
) {
    @Bean
    fun passwordEncoder() = BCryptPasswordEncoder()

    @Bean
    fun roleHierarchy(): RoleHierarchy = RoleHierarchyImpl.fromHierarchy("ROLE_ADMIN > ROLE_USER")

    @Bean
    fun filterChain(http: HttpSecurity): SecurityFilterChain = http
        .csrf { it.disable() }
        .cors { it.configurationSource(corsConfigurationSource()) }
        .formLogin { it.disable() }
        .httpBasic { it.disable() }
        .rememberMe { it.disable() }
        .logout { it.disable() }

        .exceptionHandling {
            it.accessDeniedHandler(jwtAccessDeniedHandler)
            it.authenticationEntryPoint(jwtAuthenticationEntryPoint)
        }

        .authorizeHttpRequests {
            it
                .requestMatchers(HttpMethod.GET, "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                .requestMatchers(HttpMethod.POST, "/auth/sign-in", "/auth/sign-up", "/auth/reissue").anonymous()
                .requestMatchers(HttpMethod.GET, "/users/me").authenticated()
                .requestMatchers("/uploads/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/orders").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/items/category").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/items/category").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/items/category").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/items/category/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/items/category/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/items/category/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.PUT, "/banner/**").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/items/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/items/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/items/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/items").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/items").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/items").hasRole("ADMIN")

                .requestMatchers(HttpMethod.PATCH, "/orders/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.GET, "/orders").hasRole("ADMIN")

                .requestMatchers(HttpMethod.POST, "/notices").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/notices").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/notices").hasRole("ADMIN")
                .requestMatchers(HttpMethod.POST, "/notices/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PATCH, "/notices/**").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/notices/**").hasRole("ADMIN")

                .anyRequest().permitAll()
        }

        .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
        .addFilterBefore(jwtExceptionFilter, jwtAuthenticationFilter::class.java)

        .build()


    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource = UrlBasedCorsConfigurationSource().apply {
        registerCorsConfiguration("/**", CorsConfiguration().apply {
            allowedOriginPatterns = listOf("*")
            allowedMethods = listOf("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
            allowedHeaders = listOf("*")
            allowCredentials = true
            maxAge = 3600
        })
    }
}