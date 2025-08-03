package com.newzhxu.press.common

/**
 * @author zheng2580369@gmail.com
 */
data class Result<T>(
    val success: Boolean,
    val message: String?,
    val pageInfo: PageInfo?,
    val data: T?
) {
}

data class PageInfo(
    val pageNumber: Int,
    val pageSize: Int,
    val total: Long,
    val totalPages: Int,
) {}

fun success(data: Any? = null, message: String? = null, pageInfo: PageInfo? = null): Result<*> {
    return Result(true, message, pageInfo, data)

}

fun failure(message: String? = null, pageInfo: PageInfo? = null): Result<*> {
    return Result(false, message, pageInfo, null)
}