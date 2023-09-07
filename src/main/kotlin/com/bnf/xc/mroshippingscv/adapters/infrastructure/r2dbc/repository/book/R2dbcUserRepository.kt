package com.bnf.xc.mroshippingscv.adapters.infrastructure.r2dbc.repository.book

import com.bnf.xc.mroshippingscv.adapters.infrastructure.r2dbc.repository.SpringDataR2dbcUserRepositories
import com.bnf.xc.mroshippingscv.application.usecase.condition.UserQueryCondition
import com.bnf.xc.mroshippingscv.domain.model.User
import com.bnf.xc.mroshippingscv.domain.repository.LibraryUserRepositories
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.r2dbc.core.R2dbcEntityTemplate
import org.springframework.data.relational.core.query.Criteria
import org.springframework.data.relational.core.query.Query
import org.springframework.stereotype.Repository

@Repository
class R2dbcUserRepository(
    val r2dbcTemplate: R2dbcEntityTemplate,
    val springDataR2dbcUserRepository: SpringDataR2dbcUserRepositories,
) : LibraryUserRepositories {
    override suspend fun createUser(user: User) {
        r2dbcTemplate.insert(user).awaitSingle()
    }

    override suspend fun findUserInfo(userId: String): User {
        return springDataR2dbcUserRepository.findByUserId(userId).awaitSingle()
    }

    override suspend fun findByUsersInfo(userIds: List<String>): List<User> {
        return springDataR2dbcUserRepository.findByUserIdIsIn(userIds).asFlow().toList()
    }

    override suspend fun updateUser(user: User) {
        r2dbcTemplate.update(user).awaitSingle()
    }

    override suspend fun saveUsers(users: List<User>) {
        springDataR2dbcUserRepository.saveAll(users).asFlow().toList()
    }

    // 유저 페이징 조회
    override suspend fun getUsers(condition: UserQueryCondition): List<User> {
        val criteria = Criteria.where("user_name").like("%${condition.userName}%")

        return r2dbcTemplate.select(Query.query(criteria), User::class.java).asFlow().toList()
    }
}