package com.newzhxu.press.common

/**
 * @author zheng2580369@gmail.com
 */
data class Result<T>(
    val success: Boolean,
    val pageInfo: PageInfo?,
    val data: T?,
    val errors: MutableSet<ResponseInfo> = mutableSetOf(),
    val messages: MutableSet<ResponseInfo> = mutableSetOf(),
) {

}

class ResponseInfo(
    val code: Int? = null,
    val message: String? = null,
)

data class PageInfo(
    val pageNumber: Int,
    val pageSize: Int,
    val total: Long,
    val totalPages: Int,
) {}

fun <T> success(data: T? = null, message: String? = null, pageInfo: PageInfo? = null): Result<T> {
    return Result(true, pageInfo, data)

}

fun <T> failure(pageInfo: PageInfo? = null): Result<T> {
    return Result(false, pageInfo, null)
}