package com.bnf.xc.mroshippingscv.adapters.infrastructure.jooq.mapper

import com.bnf.xc.mroshippingscv.adapters.infrastructure.jooq.JooqSQL
import com.bnf.xc.mroshippingscv.domain.enum.RankType
import com.bnf.xc.mroshippingscv.domain.model.Book
import org.jooq.Record

object BookHistoryDTOMapper {
    fun into(rs: List<Record>): Book {
        return Book(
            bookNo = rs[0][JooqSQL.bt.BOOK_NO]!!,
            bookName = rs[0][JooqSQL.bt.BOOK_NAME]!!,
            bookRank = RankType.valueOf(rs[0][JooqSQL.bt.BOOK_RANK]!!),
            author = rs[0][JooqSQL.bt.AUTHOR]!!,
            createDate = rs[0][JooqSQL.bt.CREATE_DATE]!!,
            purchaseDate = rs[0][JooqSQL.bt.PURCHASE_DATE]!!,
            updateDate = rs[0][JooqSQL.bt.UPDATE_DATE],
            rentStatus = rs[0][JooqSQL.bt.RENT_STATUS]!!,
            exist = rs[0][JooqSQL.bt.EXIST]!!,
        )
    }
}