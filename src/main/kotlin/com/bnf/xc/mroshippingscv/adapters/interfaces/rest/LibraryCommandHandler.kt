package com.bnf.xc.mroshippingscv.adapters.interfaces.rest

import com.bnf.xc.mroshippingscv.adapters.interfaces.rest.dto.request.CreateUserRequest
import com.bnf.xc.mroshippingscv.adapters.interfaces.rest.dto.request.RentRequest
import com.bnf.xc.mroshippingscv.adapters.interfaces.rest.dto.request.UpdateUserRequest
import com.bnf.xc.mroshippingscv.application.interfaces.LibraryCommandService
import com.bnf.xc.mroshippingscv.utils.ResponseUtils
import com.bnf.xc.prepayment.core.adapters.interfaces.rest.dto.request.CreateBookRequest
import com.bnf.xc.prepayment.core.adapters.interfaces.rest.dto.request.ReturnRequest
import com.bnf.xc.prepayment.core.adapters.interfaces.rest.dto.request.UpdateBookRequest
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import org.springframework.web.reactive.function.server.awaitBody

@Component
class LibraryCommandHandler(val libraryCommandService: LibraryCommandService) {
    suspend fun createBook(req: ServerRequest): ServerResponse = req.awaitBody<CreateBookRequest>()
        .let {
            ResponseUtils.create(
                libraryCommandService.createBook(it)
            )
        }

    suspend fun updateBook(req: ServerRequest): ServerResponse = req.awaitBody<UpdateBookRequest>()
        .let {
            ResponseUtils.ok(
                libraryCommandService.updateBook(it)
            )
        }

    suspend fun createUser(req: ServerRequest): ServerResponse = req.awaitBody<CreateUserRequest>()
        .let {
            ResponseUtils.create(
                libraryCommandService.createUser(it)
            )
        }

    suspend fun updateUser(req: ServerRequest): ServerResponse = req.awaitBody<UpdateUserRequest>()
        .let {
            ResponseUtils.ok(
                libraryCommandService.updateUser(it)
            )
        }

    suspend fun rentBook(req: ServerRequest): ServerResponse = req.awaitBody<RentRequest>()
        .let {
            ResponseUtils.ok(
                libraryCommandService.rentBook(it)
            )
        }

    suspend fun returnBook(req: ServerRequest): ServerResponse = req.awaitBody<ReturnRequest>()
        .let {
            ResponseUtils.ok(
                libraryCommandService.returnBook(it)
            )
        }
}