package com.bnf.xc.mroshippingscv.utils

//import com.bnf.xc.mroshippingscv.domain.enum.BookType
import com.bnf.xc.mroshippingscv.domain.enum.RankType
import com.bnf.xc.mroshippingscv.domain.model.*
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.bodyValueAndAwait

object Utils {
    fun matchingRank(userInfo: User, bookInfo: List<Book>): Pair<List<Book>, Int> {
        var filterBook: List<Book> = emptyList()

        if (userInfo.userQualification) {
            when (userInfo.userRank) {
                RankType.MASTER -> {
                    filterBook = bookInfo
                }

                RankType.TREE -> {
                    filterBook =
                        bookInfo.filter { it.bookRank == RankType.NEWBIE || it.bookRank == RankType.SPROUT || it.bookRank == RankType.TREE }
                }

                RankType.SPROUT -> {
                    filterBook = bookInfo.filter { it.bookRank == RankType.NEWBIE || it.bookRank == RankType.SPROUT }
                }

                RankType.NEWBIE -> {
                    filterBook = bookInfo.filter { it.bookRank == RankType.NEWBIE }
                }
            }
        } else return Pair(filterBook, -1)

        return Pair(filterBook, filterBook.count())
    }
}