package com.bnf.xc.mroshippingscv.domain.repository

import com.bnf.xc.mroshippingscv.application.usecase.condition.BookQueryCondition
import com.bnf.xc.mroshippingscv.application.usecase.condition.UserQueryCondition
import com.bnf.xc.mroshippingscv.domain.model.Book
import com.bnf.xc.mroshippingscv.domain.model.Point
import com.bnf.xc.mroshippingscv.domain.model.RentHistory
import com.bnf.xc.mroshippingscv.domain.model.User
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface LibraryBookRepositories {
    suspend fun createBook(book: Book)
    suspend fun findByBookId(bookNo: Long): Mono<Book>
    suspend fun findByBookIds(bookNos: List<Long>): Flux<Book>
    suspend fun updateBook(book: Book)
    suspend fun saveBooks(books: List<Book>)
    suspend fun getBooks(condition: BookQueryCondition): List<Book>
}

interface LibraryUserRepositories {
    suspend fun createUser(user: User)
    suspend fun findUserInfo(userId: String): User
    suspend fun findByUsersInfo(userIds: List<String>): List<User>
    suspend fun updateUser(user: User)
    suspend fun getUsers(condition: UserQueryCondition): List<User>
    suspend fun saveUsers(users: List<User>)
}

interface LibraryPointRepositories {
    suspend fun insertPoint(point: Point)
    suspend fun savePoints(points: List<Point>)
}

interface LibraryRentRepositories {
    suspend fun rentBook(rentData: RentHistory)
    suspend fun saveBooks(rentData: List<RentHistory>)
    suspend fun returnBooks(returnData: List<RentHistory>)
    suspend fun findByRentNos(bookNos: List<Long>): List<RentHistory>
    suspend fun findAll(): List<RentHistory>
    suspend fun scheduledRentHistory(): List<RentHistory>
}