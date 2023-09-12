package com.bnf.xc.mroshippingscv.adapters.infrastructure.jooq.mapper

import com.bnf.xc.mroshippingscv.adapters.infrastructure.jooq.JooqSQL
import com.bnf.xc.mroshippingscv.domain.enum.RankType
import com.bnf.xc.mroshippingscv.domain.model.User
import org.jooq.Record

object UserHistoryDTOMapper {
    fun into(rs: List<Record>): User {
        return User(
            userId = rs[0][JooqSQL.ut.USER_ID]!!,
            userPw = rs[0][JooqSQL.ut.USER_PW]!!,
            userName = rs[0][JooqSQL.ut.USER_NAME]!!,
            userTel = rs[0][JooqSQL.ut.USER_TEL]!!,
            userBirth = rs[0][JooqSQL.ut.USER_BIRTH]!!,
            userRank = RankType.valueOf(rs[0][JooqSQL.ut.USER_RANK]!!),
            userPoint = rs[0][JooqSQL.ut.USER_POINT]!!,
            userCreateDate = rs[0][JooqSQL.ut.USER_CREATE_DATE]!!,
            userUpdateDate = rs[0][JooqSQL.ut.USER_UPDATE_DATE],
            userQualification = rs[0][JooqSQL.ut.USER_QUALIFICATION]!!,
            userRentCount = rs[0][JooqSQL.ut.USER_RENT_COUNT]!!
        )
    }
}