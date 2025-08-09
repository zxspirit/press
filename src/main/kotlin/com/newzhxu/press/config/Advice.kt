package com.newzhxu.press.config

import com.newzhxu.press.common.ResponseInfo
import com.newzhxu.press.common.Result
import com.newzhxu.press.common.failure
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice

/**
 * @author zheng2580369@gmail.com
 */
@RestControllerAdvice
class GlobalExceptionHandler {
    val logger: Logger = LoggerFactory.getLogger(GlobalExceptionHandler::class.java)

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun methodArgumentNotValidException(exception: MethodArgumentNotValidException): Result<Void> {
        logger.error("methodArgumentNotValidException", exception)
        val responseInfos = exception.fieldErrors.map { ResponseInfo(400, it.field + ":" + it.defaultMessage) }.toSet()
        return failure<Void>().apply { this.errors.addAll(responseInfos) }
    }

    @ExceptionHandler(IllegalArgumentException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun illegalArgumentException(exception: IllegalArgumentException): Result<Void> {
        logger.error("IllegalArgumentException", exception)
        return failure<Void>().apply { this.errors.add(ResponseInfo(400, exception.message ?: "Illegal Argument")) }
    }

}