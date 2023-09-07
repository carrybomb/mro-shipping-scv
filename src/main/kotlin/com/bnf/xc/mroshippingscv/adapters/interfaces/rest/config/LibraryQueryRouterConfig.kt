package com.bnf.xc.mroshippingscv.adapters.interfaces.rest.config

import com.bnf.xc.mroshippingscv.adapters.interfaces.rest.LibraryQueryHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class LibraryQueryRouterConfig(private val libraryQueryHandler: LibraryQueryHandler) {
    @Bean
    fun libraryQueryRouter() = coRouter {
        "/api/library".nest {
            accept(MediaType("application", "vnd.xc.paging.v1+json")).nest {
                // 책 페이징 조회
                GET("/book", libraryQueryHandler::getBooks)
                // 유저 페이징 조회
                GET("/user", libraryQueryHandler::getUsers)
            }
        }
    }
}