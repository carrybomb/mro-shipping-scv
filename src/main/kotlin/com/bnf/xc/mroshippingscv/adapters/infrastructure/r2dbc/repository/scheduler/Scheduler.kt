package com.bnf.xc.mroshippingscv.adapters.infrastructure.r2dbc.repository.scheduler

import com.bnf.xc.mroshippingscv.application.interfaces.LibraryCommandService
import kotlinx.coroutines.runBlocking
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class Scheduler(
    private val libraryCommandService: LibraryCommandService
) {
    // cron 초 분 시간 일 월 요일
    @Scheduled(cron = "0 0 0 * * *")
    fun midnightScheduler() {
        try {
            runBlocking { libraryCommandService.midnightScheduler() }
        } catch (e: Exception) {
            System.out.println("에러 처리 해야함 :" + e.message)
        }
    }
}