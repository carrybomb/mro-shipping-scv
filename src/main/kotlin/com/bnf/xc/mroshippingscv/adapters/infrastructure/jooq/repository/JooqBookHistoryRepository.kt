package com.bnf.xc.mroshippingscv.adapters.infrastructure.jooq.repository

import com.bnf.xc.mroshippingscv.adapters.infrastructure.jooq.JooqSQL
import com.bnf.xc.mroshippingscv.adapters.infrastructure.jooq.mapper.BookHistoryDTOMapper
import com.bnf.xc.mroshippingscv.adapters.infrastructure.jooq.mapper.UserHistoryDTOMapper
import com.bnf.xc.mroshippingscv.application.dto.PageableTotalDTO
import com.bnf.xc.mroshippingscv.application.usecase.condition.BookQueryCondition
import com.bnf.xc.mroshippingscv.application.usecase.condition.UserQueryCondition
import com.bnf.xc.mroshippingscv.domain.model.User
import com.bnf.xc.mroshippingscv.domain.repository.LibraryUserRepositoriesForJooq
import com.bnf.xc.mroshippingscv.core.adapters.infrastructure.jooq.DslContextWrapper
import com.bnf.xc.mroshippingscv.domain.model.Book
import com.bnf.xc.mroshippingscv.domain.repository.LibraryBookRepositoriesForJooq
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Repository

@Repository
class JooqBookHistoryRepository(
    private val dslContextWrapper: DslContextWrapper
) : LibraryBookRepositoriesForJooq {
    override suspend fun getHistoriesPageable(condition: BookQueryCondition): PageableTotalDTO<List<Book>> {
        val whereCondition = JooqSQL.bookHistoryWhereCondition(condition)
        val histories = dslContextWrapper.withDSLContextMany {
            JooqSQL.bookHistorySelect(it, condition, whereCondition)
        }.bufferUntilChanged {
            it[JooqSQL.bt.BOOK_NO]!!
        }.map {
            val dto = BookHistoryDTOMapper.into(
                it
            )
            dto
        }.asFlow().toList()

        val count = getHistoriesCount(condition).toLong()

        val pageable = PageImpl(histories, condition.pageable, count)

        return PageableTotalDTO(histories, pageable.totalPages, pageable.totalElements)
    }

    override suspend fun getHistoriesCount(condition: BookQueryCondition): Int {
        val whereCondition = JooqSQL.bookHistoryWhereCondition(condition)
        return dslContextWrapper.withDSLContextMany {
            JooqSQL.bookHistoriesCount(it, whereCondition)
        }.map { it.into(Int::class.java) }.awaitSingle()
    }
}