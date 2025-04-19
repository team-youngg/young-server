package com.young.domain.post.error

import com.young.global.exception.CustomError

enum class PostError (override val status: Int, override val message: String) : CustomError {
    INQUIRY_NOT_FOUND(400, "문의를 찾을 수 없습니다."),
    NOTICE_NOT_FOUND(400, "공지를 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(400, "댓글을 찾을 수 없습니다.")
}