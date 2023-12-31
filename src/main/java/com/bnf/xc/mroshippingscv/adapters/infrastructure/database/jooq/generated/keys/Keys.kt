/*
 * This file is generated by jOOQ.
 */
package com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated.keys


import com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated.tables.BookTb
import com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated.tables.PointHistory
import com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated.tables.RentHistory
import com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated.tables.UserTb

import org.jooq.ForeignKey
import org.jooq.Record
import org.jooq.UniqueKey
import org.jooq.impl.DSL
import org.jooq.impl.Internal



// -------------------------------------------------------------------------
// UNIQUE and PRIMARY KEY definitions
// -------------------------------------------------------------------------

val BOOK_TB_PK: UniqueKey<Record> = Internal.createUniqueKey(BookTb.BOOK_TB, DSL.name("book_tb_pk"), arrayOf(BookTb.BOOK_TB.BOOK_NO), true)
val RENT_HISTORY_PK: UniqueKey<Record> = Internal.createUniqueKey(RentHistory.RENT_HISTORY, DSL.name("rent_history_pk"), arrayOf(RentHistory.RENT_HISTORY.RENT_NO), true)
val USER_TB_PK: UniqueKey<Record> = Internal.createUniqueKey(UserTb.USER_TB, DSL.name("user_tb_pk"), arrayOf(UserTb.USER_TB.USER_ID), true)

// -------------------------------------------------------------------------
// FOREIGN KEY definitions
// -------------------------------------------------------------------------

val POINT_HISTORY__POINT_LIST_TB_FK: ForeignKey<Record, Record> = Internal.createForeignKey(PointHistory.POINT_HISTORY, DSL.name("point_list_tb_fk"), arrayOf(PointHistory.POINT_HISTORY.USER_ID_FK), com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated.keys.USER_TB_PK, arrayOf(UserTb.USER_TB.USER_ID), true)
val RENT_HISTORY__RENT_HISTORY_FK: ForeignKey<Record, Record> = Internal.createForeignKey(RentHistory.RENT_HISTORY, DSL.name("rent_history_fk"), arrayOf(RentHistory.RENT_HISTORY.BOOK_NO_FK), com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated.keys.BOOK_TB_PK, arrayOf(BookTb.BOOK_TB.BOOK_NO), true)
val RENT_HISTORY__RENT_HISTORY_FK_1: ForeignKey<Record, Record> = Internal.createForeignKey(RentHistory.RENT_HISTORY, DSL.name("rent_history_fk_1"), arrayOf(RentHistory.RENT_HISTORY.USER_ID_FK), com.bnf.xc.mroshippingscv.adapters.infrastructure.database.jooq.generated.keys.USER_TB_PK, arrayOf(UserTb.USER_TB.USER_ID), true)
