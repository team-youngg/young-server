package com.young.domain.image.domain.entity

import jakarta.persistence.*

@Entity
@Table(name = "banner_images")
class BannerImage (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @Column(nullable = false)
    var url: String,
)