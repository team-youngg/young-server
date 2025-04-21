package com.young.domain.review.repository

import com.young.domain.item.domain.entity.Item
import com.young.domain.option.domain.entity.ItemOption
import com.young.domain.review.domain.entity.Review
import com.young.domain.user.domain.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<Review, Long> {
    fun findAllByItemOption(itemOption: ItemOption): List<Review>
    fun findAllByAuthor(author: User): List<Review>
    fun findAllByItemOption_Item(item: Item): List<Review>
}