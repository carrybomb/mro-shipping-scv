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
import java.lang.Exception
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
        return "success"
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
        return "success"
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
        return "success"
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
        return "success"
    }

    @Transactional
    override suspend fun rentBook(body: RentRequest): String {
        val userInfo = libraryUserRepositories.findUserInfo(body.userId)
        val bookInfo = libraryBookRepositories.findByBookIds(body.bookNos).asFlow().toList()
        val filterBookInfo = Utils.matchingRank(userInfo, bookInfo)

        when {
            // 대여자격이 없는 사용자일 경우 filterBookInfo.second = -1, 자격이 있을 경우 filterBookInfo.second = 등급에 맞는 책 수량
            filterBookInfo.second == -1 -> throw Exception("대여자격이 없는 회원입니다.")
            // 입력받은 책 수량이랑 대여 가능 등급 필터링 된 책 수량 비교
            filterBookInfo.second != bookInfo.count() -> throw Exception("대여 불가 등급 책 포함되어있습니다.")
            // 사용자 현재 대여 책 수량 + 입력받은 책수량 > 유저등급에 따른 대여 가능 책수량
            userInfo.userRentCount + bookInfo.count() > userInfo.userRank.rentCount -> throw Exception("수량 오버")
        }

        if (filterBookInfo.second != 0) {
            libraryRentRepositories.saveBooks(filterBookInfo.first.map { data ->
                RentHistory(
                    userIdFk = userInfo.userId,
                    bookNoFk = data.bookNo
                )
            })

            libraryUserRepositories.updateUser(
                userInfo.rent(filterBookInfo.second)
            )

            libraryBookRepositories.saveBooks(filterBookInfo.first.map { data ->
                data.updateStatus(true)
            })

            libraryPointRepositories.insertPoint(
                Point(
                    userIdFk = userInfo.userId,
                    // 변동 포인트
                    variablePoint = (filterBookInfo.second * 200).toLong(),
                    // 누적 포인트
                    cumulativePoint = userInfo.userPoint + (filterBookInfo.second * 200)
                )
            )
        }
        return "success"
    }

    // 책 반납 > 반납 성공 > 유저 책 수량 변경, 책 대여 상태 변경
    @Transactional
    override suspend fun returnBook(body: ReturnRequest): String {
        // 입력 받은 bookNo에 대한 책 정보들
        val bookInfo = libraryBookRepositories.findByBookIds(body.bookNos).asFlow().toList()
        // 입력 받은 bookNo에 대한 대여 기록
        val rentInfo = libraryRentRepositories.findByRentNos(body.bookNos)
        // 위에서 조회한 rentInfo에 포함된 유저들 정보 모두 조회
        val userInfo =
            libraryUserRepositories.findByUsersInfo(rentInfo.sortedBy { it.userIdFk }.map { data -> data.userIdFk })

        when {
            rentInfo.find { data -> data.returnDate != null } != null -> throw Exception("대여가 안된 책이 있습니다.")
        }

        // 대여 기록에 반납일자 입력
        libraryRentRepositories.returnBooks(rentInfo.map { data ->
            data.returnBook()
        })

        // 책 대여 상태 false, update 일자 수정
        libraryBookRepositories.saveBooks(bookInfo.map { data ->
            data.updateStatus(false)
        })

        // 반납 수량 만큼 대여 수량 감소
        libraryUserRepositories.saveUsers(userInfo.map { data ->
            data.returnCount(rentInfo.count { it.userIdFk == data.userId })
        })

        return "success"
    }

    @Transactional
    override suspend fun midnightScheduler() {
        val scheduledRentHistory = libraryRentRepositories.scheduledRentHistory()
        val userInfo = libraryUserRepositories.findByUsersInfo(scheduledRentHistory.map { data -> data.userIdFk })

        libraryUserRepositories.saveUsers(
            userInfo.map { user ->
                user.scheduledMinusPoint(scheduledRentHistory.count { it.userIdFk == user.userId })
            }
        )
        libraryPointRepositories.savePoints(
            userInfo.map { user ->
                Point(
                    userIdFk = user.userId,
                    variablePoint = -(scheduledRentHistory.count { it.userIdFk == user.userId } * 300).toLong(),
                    cumulativePoint = user.userPoint - (scheduledRentHistory.count { it.userIdFk == user.userId } * 300)
                )
            }
        )
    }
}