package com.bnf.xc.mroshippingscv.adapters.interfaces.rest.config

import com.bnf.xc.mroshippingscv.adapters.interfaces.rest.LibraryCommandHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class LibraryCommandRouterConfig(private val libraryCommandHandler: LibraryCommandHandler) {
    @Bean
    fun libraryCommandRouter() = coRouter {
        "/api/library".nest {
            accept(MediaType("application", "vnd.xc.v1+json")).nest {
                // 책 등록
                POST("/book", libraryCommandHandler::createBook)
                // 유저 등록
                POST("/user", libraryCommandHandler::createUser)
                // 책 대여
                POST("/book/rental", libraryCommandHandler::rentBook)

                // 책 수정
                PUT("/book", libraryCommandHandler::updateBook)
                // 유저 수정
                PUT("/user", libraryCommandHandler::updateUser)
                // 책 반납
                PUT("/book/return", libraryCommandHandler::returnBook)
            }
        }
    }
}