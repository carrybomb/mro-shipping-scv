package com.bnf.xc.mroshippingscv.adapters.infrastructure.r2dbc.repository

import com.bnf.xc.mroshippingscv.domain.model.Book
import com.bnf.xc.mroshippingscv.domain.model.Point
import com.bnf.xc.mroshippingscv.domain.model.RentHistory
import com.bnf.xc.mroshippingscv.domain.model.User
import org.springframework.data.repository.reactive.ReactiveCrudRepository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface SpringDataR2dbcBookRepositories : ReactiveCrudRepository<Book, Long> {
    fun findByBookNo(bookNo: Long): Mono<Book>
    fun findByBookNoIsIn(bookNo: List<Long>): Flux<Book>
}

interface SpringDataR2dbcUserRepositories : ReactiveCrudRepository<User, String> {
    fun findByUserId(userId: String): Mono<User>
    fun findByUserIdIsIn(userId: List<String>): Flux<User>
}

interface SpringDataR2dbcRentRepositories : ReactiveCrudRepository<RentHistory, Long> {
    fun findByBookNoFkIsIn(bookNoFk: List<Long>): Flux<RentHistory>
}

interface SpringDataR2dbcPointRepositories : ReactiveCrudRepository<Point, Long> {
}