package com.bnf.xc.mroshippingscv.application.interfaces

import com.bnf.xc.mroshippingscv.application.usecase.condition.BookQueryCondition
import com.bnf.xc.mroshippingscv.application.usecase.condition.UserQueryCondition
import com.bnf.xc.mroshippingscv.domain.model.Book
import com.bnf.xc.mroshippingscv.domain.model.User
import com.bnf.xc.mroshippingscv.domain.repository.LibraryBookRepositories
import com.bnf.xc.mroshippingscv.domain.repository.LibraryUserRepositories
import org.springframework.data.domain.PageImpl
import org.springframework.stereotype.Service

interface LibraryQueryService {
    suspend fun getBooks(condition: BookQueryCondition): PageImpl<Book>
    suspend fun getUsers(condition: UserQueryCondition): PageImpl<User>
}

@Service
class LibraryQueryServiceImpl(
    private val libraryBookRepositories: LibraryBookRepositories,
    private val libraryUserRepositories: LibraryUserRepositories
) : LibraryQueryService {
    override suspend fun getBooks(condition: BookQueryCondition): PageImpl<Book> {
        val content = libraryBookRepositories.getBooks(condition)

        return PageImpl(content, condition.pageable, content.count().toLong())
    }

    override suspend fun getUsers(condition: UserQueryCondition): PageImpl<User> {
        val content = libraryUserRepositories.getUsers(condition)

        return PageImpl(content, condition.pageable, content.count().toLong())
    }
}