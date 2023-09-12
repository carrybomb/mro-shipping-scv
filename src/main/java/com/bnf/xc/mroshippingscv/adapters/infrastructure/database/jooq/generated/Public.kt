/*
 * This file is generated by jOOQ.
 */
package com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated


import com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated.sequences.POINT_LIST_TB_POINT_NO_SEQ
import com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated.tables.BookTb
import com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated.tables.PointHistory
import com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated.tables.RentHistory
import com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated.tables.UserTb

import kotlin.collections.List

import org.jooq.Catalog
import org.jooq.Sequence
import org.jooq.Table
import org.jooq.impl.SchemaImpl


/**
 * This class is generated by jOOQ.
 */
@Suppress("UNCHECKED_CAST")
open class Public : SchemaImpl("public", DefaultCatalog.DEFAULT_CATALOG) {
    public companion object {

        /**
         * The reference instance of <code>public</code>
         */
        val PUBLIC: Public = Public()
    }

    /**
     * The table <code>public.book_tb</code>.
     */
    val BOOK_TB: BookTb get() = BookTb.BOOK_TB

    /**
     * The table <code>public.point_history</code>.
     */
    val POINT_HISTORY: PointHistory get() = PointHistory.POINT_HISTORY

    /**
     * The table <code>public.rent_history</code>.
     */
    val RENT_HISTORY: RentHistory get() = RentHistory.RENT_HISTORY

    /**
     * The table <code>public.user_tb</code>.
     */
    val USER_TB: UserTb get() = UserTb.USER_TB

    override fun getCatalog(): Catalog = DefaultCatalog.DEFAULT_CATALOG

    override fun getSequences(): List<Sequence<*>> = listOf(
        POINT_LIST_TB_POINT_NO_SEQ
    )

    override fun getTables(): List<Table<*>> = listOf(
        BookTb.BOOK_TB,
        PointHistory.POINT_HISTORY,
        RentHistory.RENT_HISTORY,
        UserTb.USER_TB
    )
}
