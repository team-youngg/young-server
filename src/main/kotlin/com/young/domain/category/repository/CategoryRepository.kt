package com.young.domain.category.repository

import com.young.domain.category.domain.entity.Category
import org.springframework.data.repository.CrudRepository

interface CategoryRepository : CrudRepository<Category, Long> {
    fun findByParentId(parentId: Long): List<Category>
}