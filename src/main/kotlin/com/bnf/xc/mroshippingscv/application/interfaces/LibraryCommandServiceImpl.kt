package com.bnf.xc.mroshippingscv.application.interfaces

import com.bnf.xc.mroshippingscv.adapters.interfaces.rest.dto.request.CreateUserRequest
import com.bnf.xc.mroshippingscv.adapters.interfaces.rest.dto.request.RentRequest
import com.bnf.xc.mroshippingscv.adapters.interfaces.rest.dto.request.UpdateUserRequest
import com.bnf.xc.mroshippingscv.domain.model.Book
import com.bnf.xc.mroshippingscv.domain.model.Point
import com.bnf.xc.mroshippingscv.domain.model.RentHistory
import com.bnf.xc.mroshippingscv.domain.model.User
import com.bnf.xc.mroshippingscv.domain.repository.LibraryBookRepositories
import com.bnf.xc.mroshippingscv.domain.repository.LibraryPointRepositories
import com.bnf.xc.mroshippingscv.domain.repository.LibraryRentRepositories
import com.bnf.xc.mroshippingscv.domain.repository.LibraryUserRepositories
import com.bnf.xc.mroshippingscv.utils.Utils
import com.bnf.xc.prepayment.core.adapters.interfaces.rest.dto.request.CreateBookRequest
import com.bnf.xc.prepayment.core.adapters.interfaces.rest.dto.request.ReturnRequest
import com.bnf.xc.prepayment.core.adapters.interfaces.rest.dto.request.UpdateBookRequest
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactive.awaitSingle
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

interface LibraryCommandService {
    suspend fun createBook(body: CreateBookRequest): String
    suspend fun updateBook(body: UpdateBookRequest): String
    suspend fun createUser(body: CreateUserRequest): String
    suspend fun updateUser(body: UpdateUserRequest): String
    suspend fun rentBook(body: RentRequest): String
    suspend fun returnBook(body: ReturnRequest): String
    suspend fun midnightScheduler()
}

@Service
class LibraryCommandServiceImpl(
    private val libraryBookRepositories: LibraryBookRepositories,
    private val libraryUserRepositories: LibraryUserRepositories,
    private val libraryRentRepositories: LibraryRentRepositories,
    private val libraryPointRepositories: LibraryPointRepositories
) : LibraryCommandService {

    @Transactional
    override suspend fun createBook(body: CreateBookRequest): String {
        with(body) {
            libraryBookRepositories.createBook(
                Book(
                    bookName = bookName,
                    author = author,
                    createDate = LocalDate.parse(createDate, DateTimeFormatter.ISO_DATE),
                    purchaseDate = LocalDateTime.now(),
                    bookRank = bookRank
                )
            )
        }
        return "등록성공"
    }

    @Transactional
    override suspend fun updateBook(body: UpdateBookRequest): String {
        val bookInfo = libraryBookRepositories.findByBookId(body.bookNo).awaitSingle()
        with(body) {
            libraryBookRepositories.updateBook(
                Book(
                    bookNo = bookNo,
                    bookName = bookName,
                    rentStatus = rentStatus,
                    author = author,
                    createDate = bookInfo.createDate,
                    purchaseDate = bookInfo.purchaseDate,
                    exist = exist,
                    bookRank = bookRank
                ).updateBook()
            )
        }
        return "수정성공"
    }

    @Transactional
    override suspend fun createUser(body: CreateUserRequest): String {
        with(body) {
            libraryUserRepositories.createUser(
                User(
                    userId = userId,
                    userPw = userPw,
                    userBirth = LocalDate.parse(userBirth),
                    userName = userName,
                    userTel = userTel
                )
            )
        }
        return "등록성공"
    }

    @Transactional
    override suspend fun updateUser(body: UpdateUserRequest): String {
        val userInfo = libraryUserRepositories.findUserInfo(body.userId)
        with(body) {
            libraryUserRepositories.updateUser(
                User(
                    userId = userId,
                    userPw = userPw,
                    userTel = userTel,
                    userName = userName,
                    userBirth = LocalDate.parse(userBirth),
                    userPoint = userInfo.userPoint,
                    userCreateDate = userInfo.userCreateDate,
                    userQualification = userInfo.userQualification,
                    userRank = userInfo.userRank,
                    userRentCount = userInfo.userRentCount
                ).updateUser()
            )
        }
        return "수정성공"
    }

    @Transactional
    override suspend fun rentBook(body: RentRequest): String {
        val userInfo = libraryUserRepositories.findUserInfo(body.userId)
        val bookInfo = libraryBookRepositories.findByBookIds(body.bookNos).asFlow().toList()
        val filterBookInfo = Utils.matchingRank(userInfo, bookInfo)

        when {
            filterBookInfo.second == -1 -> return "에러 처리 해야함 : 대여 권한이 정지된 계정입니다."
            filterBookInfo.second != bookInfo.count() -> return "에러 처리 해야함 : 대여권한 없는 책 포함되있음."
            userInfo.userRentCount + bookInfo.count() > userInfo.userRank.rentCount -> return "에러처리 해야함 : 수량 오버"
        }

        if (filterBookInfo.second != 0) {
            libraryRentRepositories.saveBooks(filterBookInfo.first.map { data ->
                RentHistory(
                    userIdFk = userInfo.userId,
                    bookNoFk = data.bookNo
                )
            })
            libraryUserRepositories.updateUser(
                User(
                    userId = userInfo.userId,
                    userPw = userInfo.userPw,
                    userTel = userInfo.userTel,
                    userName = userInfo.userName,
                    userBirth = userInfo.userBirth,
                    userPoint = userInfo.userPoint,
                    userCreateDate = userInfo.userCreateDate,
                    userQualification = userInfo.userQualification,
                    userRank = userInfo.userRank,
                    userRentCount = userInfo.userRentCount
                ).rent(filterBookInfo.second)
            )
            libraryBookRepositories.saveBooks(filterBookInfo.first.map { data ->
                Book(
                    bookNo = data.bookNo,
                    bookName = data.bookName,
                    rentStatus = true,
                    author = data.author,
                    createDate = data.createDate,
                    purchaseDate = data.purchaseDate,
                    exist = data.exist,
                    bookRank = data.bookRank
                ).updateBook()
            })
            libraryPointRepositories.insertPoint(
                Point(
                    userIdFk = userInfo.userId,
                    variablePoint = (filterBookInfo.second * 200).toLong(),
                    cumulativePoint = userInfo.userPoint + (filterBookInfo.second * 200)
                )
            )
        }
        return "대여성공"
    }

    // 책 반납 > 반납 성공 > 유저 책 수량 변경, 책 대여 상태 변경
    @Transactional
    override suspend fun returnBook(body: ReturnRequest): String {
        val bookInfo = libraryBookRepositories.findByBookIds(body.bookNos).asFlow().toList()
        val rentInfo = libraryRentRepositories.findByRentNos(body.bookNos)
        val userInfo =
            libraryUserRepositories.findByUsersInfo(rentInfo.sortedBy { it.userIdFk }.map { data -> data.userIdFk })

        when {
            rentInfo.find { data -> data.returnDate != null } != null -> return "에러 처리 해야함 : 대여가 안된 책이 있습니다."
        }

        libraryRentRepositories.returnBooks(rentInfo.map { data ->
            RentHistory(
                rentNo = data.rentNo,
                userIdFk = data.userIdFk,
                bookNoFk = data.bookNoFk,
                rentDate = data.rentDate,
                returnDate = LocalDate.now(),
                expectReturnDate = data.expectReturnDate
            )
        })

        libraryBookRepositories.saveBooks(bookInfo.map { data ->
            Book(
                bookNo = data.bookNo,
                bookName = data.bookName,
                author = data.author,
                createDate = data.createDate,
                purchaseDate = data.purchaseDate,
                exist = data.exist,
                bookRank = data.bookRank
            ).updateBook()
        })

        userInfo.map { data ->
            libraryUserRepositories.updateUser(
                User(
                    userId = data.userId,
                    userPw = data.userPw,
                    userTel = data.userTel,
                    userName = data.userName,
                    userBirth = data.userBirth,
                    userPoint = data.userPoint,
                    userCreateDate = data.userCreateDate,
                    userQualification = data.userQualification,
                    userRank = data.userRank,
                    userRentCount = data.userRentCount
                ).returnCount(rentInfo.count { it.userIdFk == data.userId })
            )
        }

        libraryUserRepositories.saveUsers(userInfo.map { data ->
            User(
                userId = data.userId,
                userPw = data.userPw,
                userTel = data.userTel,
                userName = data.userName,
                userBirth = data.userBirth,
                userPoint = data.userPoint,
                userCreateDate = data.userCreateDate,
                userQualification = data.userQualification,
                userRank = data.userRank,
                userRentCount = data.userRentCount
            ).returnCount(rentInfo.count { it.userIdFk == data.userId })
        })

        return "반납성공"
    }

    @Transactional
    override suspend fun midnightScheduler() {
        val scheduledRentHistory = libraryRentRepositories.scheduledRentHistory()
        val userInfo = libraryUserRepositories.findByUsersInfo(scheduledRentHistory.map { data -> data.userIdFk })

        libraryUserRepositories.saveUsers(
            userInfo.map { user ->
                User(
                    userId = user.userId,
                    userPw = user.userPw,
                    userTel = user.userTel,
                    userName = user.userName,
                    userBirth = user.userBirth,
                    userPoint = user.userPoint,
                    userCreateDate = user.userCreateDate,
                    userQualification = user.userQualification,
                    userRank = user.userRank,
                    userRentCount = user.userRentCount
                ).scheduledMinusPoint(scheduledRentHistory.count { it.userIdFk == user.userId })
            }
        )
        libraryPointRepositories.savePoints(
            userInfo.map { user ->
                Point(
                    userIdFk = user.userId,
                    variablePoint = -(scheduledRentHistory.count { it.userIdFk == user.userId } * 200).toLong(),
                    cumulativePoint = user.userPoint - (scheduledRentHistory.count { it.userIdFk == user.userId } * 200)
                )
            }
        )
    }
}