package com.bnf.xc.mroshippingscv.adapters.infrastructure.jooq.repository

import com.bnf.xc.mroshippingscv.adapters.infrastructure.jooq.JooqSQL
import com.bnf.xc.mroshippingscv.adapters.infrastructure.jooq.mapper.UserHistoryDTOMapper
import com.bnf.xc.mroshippingscv.application.dto.PageableTotalDTO
import com.bnf.xc.mroshippingscv.application.usecase.condition.UserQueryCondition
import com.bnf.xc.mroshippingscv.domain.model.User
import com.bnf.xc.mroshippingscv.domain.repository.LibraryUserRepositoriesForJooq
import com.bnf.xc.mroshippingscv.core.adapters.infrastructure.jooq.DslContextWrapper
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Repository

@Repository
class JooqUserHistoryRepository(
    private val dslContextWrapper: DslContextWrapper
) : LibraryUserRepositoriesForJooq {
    override suspend fun getHistoriesPageable(condition: UserQueryCondition): PageableTotalDTO<List<User>> {
        val whereCondition = JooqSQL.userHistoryWhereCondition(condition)
        val histories = dslContextWrapper.withDSLContextMany {
            JooqSQL.userHistorySelect(it, condition, whereCondition)
        }.bufferUntilChanged {
            it[JooqSQL.ut.USER_ID]!!
        }.map {
            val dto = UserHistoryDTOMapper.into(
                it
            )
            dto
        }.asFlow().toList()

        val count = getHistoriesCount(condition).toLong()

        val pageable = PageImpl(histories, condition.pageable, count)

        return PageableTotalDTO(histories, pageable.totalPages, pageable.totalElements)
    }

    override suspend fun getHistoriesCount(condition: UserQueryCondition): Int {
        val whereCondition = JooqSQL.userHistoryWhereCondition(condition)
        return dslContextWrapper.withDSLContextMany {
            JooqSQL.userHistoriesCount(it, whereCondition)
        }.map { it.into(Int::class.java) }.awaitSingle()
    }
}