package com.newzhxu.press.common

/**
 * Custom exception class for the Press application.
 * This class extends RuntimeException and can be used to throw exceptions
 * specific to the Press application context.
 * It can be used to encapsulate error messages and provide additional context
 * when exceptions occur.
 * @author zheng2580369@gmail.com
 */
abstract class CommonException : RuntimeException {
    constructor() : super()

    constructor(message: String) : super(message)

    constructor(message: String, cause: Throwable) : super(message, cause)

    constructor(cause: Throwable) : super(cause)
    constructor(message: String, cause: Throwable, enableSuppression: Boolean, writableStackTrace: Boolean) : super(
        message,
        cause,
        enableSuppression,
        writableStackTrace
    )
}