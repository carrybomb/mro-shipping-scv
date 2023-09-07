package com.bnf.xc.mroshippingscv.adapters.interfaces.rest

import com.bnf.xc.mroshippingscv.application.interfaces.LibraryQueryService
import com.bnf.xc.mroshippingscv.application.usecase.condition.BookQueryCondition
import com.bnf.xc.mroshippingscv.application.usecase.condition.UserQueryCondition
import com.bnf.xc.mroshippingscv.utils.ResponseUtils
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse

@Component
class LibraryQueryHandler(val libraryQueryService: LibraryQueryService) {

    suspend fun getBooks(req: ServerRequest): ServerResponse {
        val page = req.queryParam("page").orElse("1").toInt()
        val size = req.queryParam("size").orElse("10").toInt()
        val bookName = req.queryParam("bookName").orElse("")
        val author = req.queryParam("author").orElse("")
        return BookQueryCondition(
            pageable = PageRequest.of(
                page - 1,
                size
            ),
            bookName = bookName,
            author = author,
        ).let {
            ResponseUtils.ok(
                libraryQueryService.getBooks(it)
            )
        }
    }

    suspend fun getUsers(req: ServerRequest): ServerResponse {
        val page = req.queryParam("page").orElse("1").toInt()
        val size = req.queryParam("size").orElse("10").toInt()
        val userName = req.queryParam("userName").orElse("")
        return UserQueryCondition(
            pageable = PageRequest.of(
                page - 1,
                size
            ),
            userName = userName
        ).let {
            ResponseUtils.ok(
                libraryQueryService.getUsers(it)
            )
        }
    }

}