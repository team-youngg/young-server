package com.young.domain.item.repository

import com.young.domain.item.domain.entity.Category
import org.springframework.data.repository.CrudRepository

interface CategoryRepository : CrudRepository<Category, Long> {
    fun findByParentId(parentId: Long): List<Category>
}