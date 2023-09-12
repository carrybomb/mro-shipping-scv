package com.bnf.xc.mroshippingscv.adapters.infrastructure.jooq

import com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated.tables.references.BOOK_TB
import com.bnf.xc.mroshippingscv.application.usecase.condition.UserQueryCondition
import com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated.tables.references.USER_TB
import com.bnf.xc.mroshippingscv.application.usecase.condition.BookQueryCondition
import org.jooq.*
import org.jooq.impl.DSL.noCondition

object JooqSQL {
    val ut = USER_TB.`as`("uh")
    val bt = BOOK_TB.`as`("bt")

    fun userHistoryWhereCondition(queryCondition: UserQueryCondition): Condition {
        var condition = noCondition()

        condition = condition.and(ut.USER_NAME.like("%${queryCondition.userName}%"))

        return condition
    }

    fun userHistoriesCount(dslContext: DSLContext, condition: Condition): SelectConditionStep<Record1<Int>> {
        return dslContext
            .selectCount()
            .from(ut)
            .where(condition)
    }

    fun userHistorySelect(
        dslContext: DSLContext,
        queryCondition: UserQueryCondition,
        condition: Condition
    ): SelectLimitPercentAfterOffsetStep<Record> {
        val fields = userHistoryFields()

        return dslContext
            .select(fields)
            .from(ut)
            .where(condition)
            .orderBy(ut.USER_NAME.asc(), ut.USER_ID.asc())
            .offset(queryCondition.pageable.offset)
            .limit(queryCondition.pageable.pageSize)
    }

    fun bookHistoryWhereCondition(queryCondition: BookQueryCondition): Condition {
        var condition = noCondition()

        condition = condition.and(bt.BOOK_NAME.like("%${queryCondition.bookName}%"))
            .and(bt.AUTHOR.like("%${queryCondition.author}%"))

        return condition
    }

    fun bookHistoriesCount(dslContext: DSLContext, condition: Condition): SelectConditionStep<Record1<Int>> {
        return dslContext
            .selectCount()
            .from(bt)
            .where(condition)
    }

    fun bookHistorySelect(
        dslContext: DSLContext,
        queryCondition: BookQueryCondition,
        condition: Condition
    ): SelectLimitPercentAfterOffsetStep<Record> {
        val fields = bookHistoryFields()

        return dslContext
            .select(fields)
            .from(bt)
            .where(condition)
            .orderBy(bt.BOOK_NAME.asc(), bt.BOOK_NO.asc())
            .offset(queryCondition.pageable.offset)
            .limit(queryCondition.pageable.pageSize)
    }

    private fun userHistoryFields(): List<SelectFieldOrAsterisk> {
        return listOf(
            ut.USER_ID,
            ut.USER_PW,
            ut.USER_NAME,
            ut.USER_BIRTH,
            ut.USER_POINT,
            ut.USER_QUALIFICATION,
            ut.USER_RANK,
            ut.USER_TEL,
            ut.USER_CREATE_DATE,
            ut.USER_UPDATE_DATE,
            ut.USER_RENT_COUNT
        )
    }

    private fun bookHistoryFields(): List<SelectFieldOrAsterisk> {
        return listOf(
            bt.BOOK_NO,
            bt.BOOK_NAME,
            bt.RENT_STATUS,
            bt.AUTHOR,
            bt.CREATE_DATE,
            bt.PURCHASE_DATE,
            bt.UPDATE_DATE,
            bt.EXIST,
            bt.BOOK_RANK
        )
    }
}