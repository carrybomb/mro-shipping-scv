package com.bnf.xc.mroshippingscv.adapters.infrastructure.r2dbc.repository.book

import com.bnf.xc.mroshippingscv.adapters.infrastructure.r2dbc.repository.SpringDataR2dbcRentRepositories
import com.bnf.xc.mroshippingscv.application.usecase.condition.BookQueryCondition
import com.bnf.xc.mroshippingscv.domain.model.Book
import com.bnf.xc.mroshippingscv.domain.model.RentHistory
import com.bnf.xc.mroshippingscv.domain.repository.LibraryRentRepositories
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.data.relational.core.query.isEqual
import org.springframework.stereotype.Repository
import java.time.LocalDate
import java.time.LocalDateTime

@Repository
class R2dbcRentRepository(
    val r2dbcTemplate: R2dbcEntityTemplate,
    val springDataR2dbcRentRepositories: SpringDataR2dbcRentRepositories
) : LibraryRentRepositories {
    override suspend fun rentBook(rentData: RentHistory) {
        r2dbcTemplate.insert(rentData).awaitSingle()
    }

    override suspend fun saveBooks(rentData: List<RentHistory>) {
        springDataR2dbcRentRepositories.saveAll(rentData).asFlow().toList()
    }

    override suspend fun returnBooks(returnData: List<RentHistory>) {
        springDataR2dbcRentRepositories.saveAll(returnData).asFlow().toList()
    }

    override suspend fun findByRentNos(bookNos: List<Long>): List<RentHistory> {
        return springDataR2dbcRentRepositories.findByBookNoFkIsIn(bookNos).asFlow().toList()
            .sortedByDescending { it.rentNo }
            .distinctBy { it.bookNoFk }
    }

    override suspend fun findAll(): List<RentHistory> {
        return springDataR2dbcRentRepositories.findAll().asFlow().toList()
    }

    override suspend fun scheduledRentHistory(): List<RentHistory> {
        val criteria = Criteria.where("return_date").isNull
            .and("expect_return_date").lessThan(LocalDate.now())

        return r2dbcTemplate.select(Query.query(criteria), RentHistory::class.java).asFlow().toList()
            .sortedByDescending { it.bookNoFk }.distinctBy { it.bookNoFk }
    }
}
