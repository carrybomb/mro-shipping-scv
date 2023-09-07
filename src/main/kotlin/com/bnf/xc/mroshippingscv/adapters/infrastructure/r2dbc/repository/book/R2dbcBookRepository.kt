package com.bnf.xc.mroshippingscv.adapters.infrastructure.r2dbc.repository.book

import com.bnf.xc.mroshippingscv.adapters.infrastructure.r2dbc.repository.SpringDataR2dbcBookRepositories
import com.bnf.xc.mroshippingscv.application.usecase.condition.BookQueryCondition
import com.bnf.xc.mroshippingscv.domain.model.Book
import com.bnf.xc.mroshippingscv.domain.repository.LibraryBookRepositories
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Repository
class R2dbcBookRepository(
    val r2dbcTemplate: R2dbcEntityTemplate,
    val springDataR2dbcBookRepository: SpringDataR2dbcBookRepositories,
) : LibraryBookRepositories {

    // 책 단건 생성
    override suspend fun createBook(book: Book) {
        r2dbcTemplate.insert(book).awaitSingle()
    }

    // 책 단건 조회(bookNo로 조회)
    override suspend fun findByBookId(bookNo: Long): Mono<Book> {
        return springDataR2dbcBookRepository.findByBookNo(bookNo)
    }

    override suspend fun findByBookIds(bookNos: List<Long>): Flux<Book> {
        return springDataR2dbcBookRepository.findByBookNoIsIn(bookNos)
    }

    // 책 단건 수정
    override suspend fun updateBook(book: Book) {
        r2dbcTemplate.update(book).awaitSingle()
    }

    override suspend fun saveBooks(books: List<Book>) {
        springDataR2dbcBookRepository.saveAll(books).asFlow().toList()
    }

    // 책 페이징 조회
    override suspend fun getBooks(condition: BookQueryCondition): List<Book> {
        val criteria = Criteria.where("book_name").like("%${condition.bookName}%")
            .and("author").like("%${condition.author}%")
        return r2dbcTemplate.select(Query.query(criteria), Book::class.java).asFlow().toList()
    }
}

