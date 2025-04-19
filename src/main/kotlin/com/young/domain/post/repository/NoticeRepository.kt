package com.young.domain.post.repository

import com.young.domain.post.model.Notice
import org.springframework.data.jpa.repository.JpaRepository

interface NoticeRepository : JpaRepository<Notice, Long> {
}