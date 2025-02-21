package com.young.global

import com.young.domain.category.domain.entity.Category
import com.young.domain.item.domain.entity.Item
import com.young.domain.category.domain.entity.ItemCategory
import com.young.domain.category.repository.CategoryRepository
import com.young.domain.category.repository.ItemCategoryRepository
import com.young.domain.image.domain.entity.ItemImage
import com.young.domain.image.repository.ItemImageRepository
import com.young.domain.item.repository.ItemRepository
import com.young.domain.option.domain.entity.ItemOption
import com.young.domain.option.domain.entity.ItemOptionValue
import com.young.domain.option.domain.enums.ItemOptionType
import com.young.domain.option.repository.ItemOptionRepository
import com.young.domain.option.repository.ItemOptionValueRepository
import jakarta.annotation.PostConstruct
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DummyInitializer(
    private val categoryRepository: CategoryRepository,
    private val itemRepository: ItemRepository,
    private val itemCategoryRepository: ItemCategoryRepository,
    private val itemImageRepository: ItemImageRepository,
    private val itemOptionRepository: ItemOptionRepository,
    private val itemOptionValueRepository: ItemOptionValueRepository
) {
    @PostConstruct
    @Transactional
    fun init() {
        if (categoryRepository.count() > 0) return // 이미 데이터가 있으면 실행 안 함

        // ✅ 1. 카테고리 추가 (여성, 남성, 전체)
        val women = categoryRepository.save(Category(name = "men", parentId = null))
        val men = categoryRepository.save(Category(name = "women", parentId = null))
        val all = categoryRepository.save(Category(name = "none", parentId = null))

        // ✅ 2. 서브 카테고리 추가
        val topsWomen = categoryRepository.save(Category(name = "top", parentId = women.id!!))
        val bottomsWomen = categoryRepository.save(Category(name = "bottom", parentId = women.id!!))
        val outerWomen = categoryRepository.save(Category(name = "outer", parentId = women.id!!))

        val topsMen = categoryRepository.save(Category(name = "top", parentId = men.id!!))
        val bottomsMen = categoryRepository.save(Category(name = "bottom", parentId = men.id!!))
        val outerMen = categoryRepository.save(Category(name = "outer", parentId = men.id!!))

        val topsAll = categoryRepository.save(Category(name = "top", parentId = all.id!!))
        val bottomsAll = categoryRepository.save(Category(name = "bottom", parentId = all.id!!))
        val outerAll = categoryRepository.save(Category(name = "outer", parentId = all.id!!))

        // ✅ 3. 상세 카테고리 추가
        val hoodieWomen = categoryRepository.save(Category(name = "hoodie", parentId = topsWomen.id!!))
        val hoodieMen = categoryRepository.save(Category(name = "hoodie", parentId = topsMen.id!!))
        val hoodieAll = categoryRepository.save(Category(name = "hoodie", parentId = topsAll.id!!))

        val jeansWomen = categoryRepository.save(Category(name = "jeans", parentId = bottomsWomen.id!!))
        val jeansMen = categoryRepository.save(Category(name = "jeans", parentId = bottomsMen.id!!))
        val jeansAll = categoryRepository.save(Category(name = "jeans", parentId = bottomsAll.id!!))

        val coatWomen = categoryRepository.save(Category(name = "coat", parentId = outerWomen.id!!))
        val coatMen = categoryRepository.save(Category(name = "coat", parentId = outerMen.id!!))
        val coatAll = categoryRepository.save(Category(name = "coat", parentId = outerAll.id!!))

        // ✅ 4. 아이템 추가 (옷 위주로 확장)
        val item1 = itemRepository.save(
            Item(name = "오버핏 후드티", description = "기모 안감, 겨울용", price = 45000, detail = "오버핏 스타일, 남녀 공용")
        )
        val item2 = itemRepository.save(
            Item(name = "슬림핏 청바지", description = "신축성 좋은 슬림핏 청바지", price = 59000, detail = "스판 원단, 다크 블루")
        )
        val item3 = itemRepository.save(
            Item(name = "롱 코트", description = "겨울철 필수 아이템, 롱 코트", price = 120000, detail = "울 80%, 방풍 기능")
        )

        // ✅ 3. 아이템을 카테고리에 연결
        itemCategoryRepository.save(ItemCategory(item = item1, categoryId = hoodieAll.id!!))
        itemCategoryRepository.save(ItemCategory(item = item2, categoryId = jeansMen.id!!))
        itemCategoryRepository.save(ItemCategory(item = item3, categoryId = coatAll.id!!))

        // ✅ 4. 아이템 이미지 추가
        val images = listOf(
            ItemImage(url = "https://example.com/images/hoodie1.jpg", item = item1),
            ItemImage(url = "https://example.com/images/hoodie2.jpg", item = item1),
            ItemImage(url = "https://example.com/images/jeans1.jpg", item = item2),
            ItemImage(url = "https://example.com/images/jeans2.jpg", item = item2),
            ItemImage(url = "https://example.com/images/coat1.jpg", item = item3),
            ItemImage(url = "https://example.com/images/coat2.jpg", item = item3)
        )
        itemImageRepository.saveAll(images)

        val itemOption1 = itemOptionRepository.save(ItemOption(item = item1, stock = 50))
        val itemOption12 = itemOptionRepository.save(ItemOption(item = item1, stock = 50))
        val itemOption2 = itemOptionRepository.save(ItemOption(item = item2, stock = 40))
        val itemOption22 = itemOptionRepository.save(ItemOption(item = item2, stock = 40))
        val itemOption3 = itemOptionRepository.save(ItemOption(item = item3, stock = 30))
        val itemOption32 = itemOptionRepository.save(ItemOption(item = item3, stock = 30))

        val optionValues = listOf(
            ItemOptionValue(itemOption = itemOption1, type = ItemOptionType.COLOR, value = "Black", detail = "000000"),
            ItemOptionValue(itemOption = itemOption1, type = ItemOptionType.COLOR, value = "Gray", detail = "C0C0C0"),
            ItemOptionValue(itemOption = itemOption12, type = ItemOptionType.SIZE, value = "M", detail = "95"),
            ItemOptionValue(itemOption = itemOption12, type = ItemOptionType.SIZE, value = "L", detail = "100"),

            ItemOptionValue(itemOption = itemOption2, type = ItemOptionType.COLOR, value = "Dark Blue", detail = "00008B"),
            ItemOptionValue(itemOption = itemOption2, type = ItemOptionType.COLOR, value = "Light Blue", detail = "ADD8E6"),
            ItemOptionValue(itemOption = itemOption22, type = ItemOptionType.SIZE, value = "32", detail = "82cm"),
            ItemOptionValue(itemOption = itemOption22, type = ItemOptionType.SIZE, value = "34", detail = "86cm"),

            ItemOptionValue(itemOption = itemOption3, type = ItemOptionType.COLOR, value = "Camel", detail = "C19A6B"),
            ItemOptionValue(itemOption = itemOption3, type = ItemOptionType.COLOR, value = "Gray", detail = "808080"),
            ItemOptionValue(itemOption = itemOption32, type = ItemOptionType.SIZE, value = "L", detail = "100"),
            ItemOptionValue(itemOption = itemOption32, type = ItemOptionType.SIZE, value = "XL", detail = "105")
        )
        itemOptionValueRepository.saveAll(optionValues)
    }
}
