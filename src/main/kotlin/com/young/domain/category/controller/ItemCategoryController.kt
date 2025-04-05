package com.young.domain.category.controller

import com.young.domain.category.domain.entity.ItemCategory
import com.young.domain.category.dto.response.CategoryResponse
import com.young.domain.category.service.ItemCategoryService
import com.young.domain.category.service.UpdateCategoryRequest
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@Tag(name = "카테고리", description = "카테고리 api")
@RestController
@RequestMapping("/items/category")
class ItemCategoryController(
    private val categoryService: ItemCategoryService
) {
    @Operation(summary = "카테고리 추가", description = "새로운 카테고리를 생성합니다")
    @PostMapping
    fun createCategory(@RequestBody request: CreateCategoryRequest)
    = categoryService.createCategory(request.name, request.parentId)

//    @Operation(summary = "카테고리 삭제", description = "카테고리를 삭제합니다.")
//    @DeleteMapping("/{categoryId}")
//    fun deleteCategory(@PathVariable categoryId: Long) = categoryService.deleteCategory(categoryId)

    @Operation(summary = "카테고리 배정", description = "아이템에 카테고리를 배정합니다.")
    @PostMapping("/assign")
    fun assignItemToCategory(@RequestBody request: AssignItemRequest): ItemCategory
    = categoryService.assignItemToCategory(request.itemId, request.categoryId)

    @Operation(summary = "카테고리 배정 취소", description = "아이템에 카테고리 배정을 취소(삭제)합니다.")
    @DeleteMapping("/assign")
    fun removeItemFromCategory(
        @RequestParam itemId: Long,
        @RequestParam categoryId: Long
    ) = categoryService.removeItemFromCategory(itemId, categoryId)

    @Operation(summary = "카테고리 조회", description = "모든 카테고리를 조회합니다.")
    @GetMapping
    fun getCategories() = categoryService.getMergedCategories()

//    @Operation(summary = "카테고리 추가", description = "새로운 카테고리를 생성합니다.")
//    @PostMapping
//    fun createSubCategoryForAllGenders(
//        @Valid @RequestBody request: CreateSubCategoryRequest
//    ) {
//        categoryService.createSubCategoryForAllGenders(request.parent, request.child)
//    }

    @Operation(summary = "카테고리 수정", description = "카테고리의 이름 수정")
    @PatchMapping("/{categoryId}")
    fun updateCategory(@PathVariable categoryId: Long, @RequestBody request: UpdateCategoryRequest)
    = categoryService.updateCategoryName(request, categoryId)
}

data class CreateSubCategoryRequest(
    val parent: String,
    val child: String
)

data class CreateCategoryRequest(
    val name: String,
    val parentId: Long? = null
)

data class AssignItemRequest(
    val itemId: Long,
    val categoryId: Long
)