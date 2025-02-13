package com.young.domain.review.domain.entity

import com.young.domain.option.domain.entity.ItemOption
import com.young.domain.order.domain.entity.OrderItemOption
import com.young.domain.user.domain.entity.User
import com.young.global.common.BaseEntity
import jakarta.persistence.*

@Entity
@Table(name = "reviews")
class Review (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    var star: Float,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    val author: User,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "option_id", nullable = false)
    val itemOption: OrderItemOption,

    var comment: String,

    @OneToMany(mappedBy = "review", cascade = [CascadeType.ALL], orphanRemoval = true)
    val images: MutableList<ReviewImage> = mutableListOf(),
) : BaseEntity() {
    fun addImages(reviewImages: List<ReviewImage>) {
        reviewImages.forEach { image ->
            images.add(image)
        }
    }
}