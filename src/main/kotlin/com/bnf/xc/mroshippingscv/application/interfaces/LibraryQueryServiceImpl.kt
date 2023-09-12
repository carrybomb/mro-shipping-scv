package com.bnf.xc.mroshippingscv.application.interfaces

import com.bnf.xc.mroshippingscv.application.dto.PageableTotalDTO
import com.bnf.xc.mroshippingscv.application.usecase.condition.BookQueryCondition
import com.bnf.xc.mroshippingscv.application.usecase.condition.UserQueryCondition
import com.bnf.xc.mroshippingscv.domain.model.Book
import com.bnf.xc.mroshippingscv.domain.model.User
import com.bnf.xc.mroshippingscv.domain.repository.LibraryBookRepositoriesForJooq
import com.bnf.xc.mroshippingscv.domain.repository.LibraryUserRepositoriesForJooq
import org.springframework.stereotype.Service

interface LibraryQueryService {
    suspend fun getBooks(condition: BookQueryCondition): PageableTotalDTO<List<Book>>
    suspend fun getUsers(condition: UserQueryCondition): PageableTotalDTO<List<User>>
}

@Service
class LibraryQueryServiceImpl(
    private val libraryBookRepositoriesForJooq: LibraryBookRepositoriesForJooq,
    private val libraryUserRepositoriesForJooq: LibraryUserRepositoriesForJooq
) : LibraryQueryService {
    override suspend fun getBooks(condition: BookQueryCondition): PageableTotalDTO<List<Book>> {
        return libraryBookRepositoriesForJooq.getHistoriesPageable(condition)
    }

    override suspend fun getUsers(condition: UserQueryCondition): PageableTotalDTO<List<User>> {
        return libraryUserRepositoriesForJooq.getHistoriesPageable(condition)
    }
}