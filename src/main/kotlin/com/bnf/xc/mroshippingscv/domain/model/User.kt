package com.bnf.xc.mroshippingscv.domain.model

import com.bnf.xc.mroshippingscv.domain.enum.RankType
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Table(name = "user_tb")
data class User(
    @Id
    val userId: String,
    val userName: String,
    val userTel: String,
    val userBirth: LocalDate,
    var userRank: RankType = RankType.NEWBIE,
    val userPw: String,
    var userPoint: Long = 0,
    val userCreateDate: LocalDateTime = LocalDateTime.now(),
    var userUpdateDate: LocalDateTime? = null,
    // 대여 자격 유무(true->자격o, false->자격x)
    var userQualification: Boolean = true,
    var userRentCount: Int = 0
) {
    fun updateUser(): User {
        userUpdateDate = LocalDateTime.now()
        return this
    }

    fun rent(count: Int): User {
        userRentCount += count
        userPoint += count * 200
        if (userRank != RankType.MASTER) {
            when {
                userPoint in 0..2000 -> userRank = RankType.NEWBIE
                userPoint in 2000..4999 -> userRank = RankType.SPROUT
                userPoint > 5000 -> userRank = RankType.TREE
            }
        }
        userUpdateDate = LocalDateTime.now()
        return this
    }

    fun returnCount(count: Int): User {
        userRentCount -= count
        userUpdateDate = LocalDateTime.now()
        return this
    }

    fun scheduledMinusPoint(count: Int): User {
        userPoint -= count * 200
        if (userRank != RankType.MASTER) {
            when {
                userPoint < 0 -> userQualification = false
                userPoint < 2000 -> userRank = RankType.NEWBIE
                userPoint in 2000..4999 -> userRank = RankType.SPROUT
                userPoint > 5000 -> userRank = RankType.TREE
            }
        }
        userUpdateDate = LocalDateTime.now()
        return this
    }
}