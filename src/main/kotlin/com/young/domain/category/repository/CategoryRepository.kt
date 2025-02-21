package com.young.domain.category.repository

import com.young.domain.category.domain.entity.Category
import org.springframework.data.repository.CrudRepository

interface CategoryRepository : CrudRepository<Category, Long> {
    fun findByParentId(parentId: Long): List<Category>

    fun findByNameContainingIgnoreCase(name: String): List<Category>
    fun findByIdInAndNameContainingIgnoreCase(ids: List<Long>, name: String): List<Category>
    fun findByParentIdAndName(parentId: Long, name: String): Category?
}