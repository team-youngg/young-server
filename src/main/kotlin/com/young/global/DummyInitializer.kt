package com.young.global

import com.young.domain.category.domain.entity.Category
import com.young.domain.category.domain.entity.ItemCategory
import com.young.domain.category.repository.CategoryRepository
import com.young.domain.category.repository.ItemCategoryRepository
import com.young.domain.image.domain.entity.ItemImage
import com.young.domain.image.repository.ItemImageRepository
import com.young.domain.item.domain.entity.Item
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

//    @PostConstruct
    @Transactional
    fun init() {
        // 이미 카테고리가 존재하면 더미 데이터 생성하지 않음
        if (categoryRepository.count() > 0) return

        // 1) 루트 카테고리 3개 (예: 상의, 하의, 아우터)
        val top = categoryRepository.save(Category(name = "top", parentId = null))
        val bottom = categoryRepository.save(Category(name = "bottom", parentId = null))
        val outer = categoryRepository.save(Category(name = "outer", parentId = null))

        // 2) 서브 카테고리
        val hoodie = categoryRepository.save(Category(name = "hoodie", parentId = top.id!!))
        val jeans = categoryRepository.save(Category(name = "jeans", parentId = bottom.id!!))
        val coat = categoryRepository.save(Category(name = "coat", parentId = outer.id!!))

        // 3) 아이템 생성 (성별 정보 없이)
        val item1 = itemRepository.save(
            Item(
                name = "오버핏 후드티",
                description = "기모 안감, 겨울용",
                price = 45000,
                detail = "오버핏 스타일, 남녀 공용",
                gender = "women" // 혹은 gender 필드 자체를 제거했을 경우 제외
            )
        )
        val item2 = itemRepository.save(
            Item(
                name = "슬림핏 청바지",
                description = "신축성 좋은 슬림핏 청바지",
                price = 59000,
                detail = "스판 원단, 다크 블루",
                gender = "men"
            )
        )
        val item3 = itemRepository.save(
            Item(
                name = "롱 코트",
                description = "겨울철 필수 아이템, 롱 코트",
                price = 120000,
                detail = "울 80%, 방풍 기능",
                gender = "all"
            )
        )

        // 4) 아이템-카테고리 연결
        itemCategoryRepository.save(ItemCategory(item = item1, categoryId = hoodie.id!!))
        itemCategoryRepository.save(ItemCategory(item = item2, categoryId = jeans.id!!))
        itemCategoryRepository.save(ItemCategory(item = item3, categoryId = coat.id!!))

        // 5) 아이템 이미지 추가
        val images = listOf(
            ItemImage(url = "https://example.com/images/hoodie1.jpg", item = item1),
            ItemImage(url = "https://example.com/images/hoodie2.jpg", item = item1),
            ItemImage(url = "https://example.com/images/jeans1.jpg", item = item2),
            ItemImage(url = "https://example.com/images/jeans2.jpg", item = item2),
            ItemImage(url = "https://example.com/images/coat1.jpg", item = item3),
            ItemImage(url = "https://example.com/images/coat2.jpg", item = item3)
        )
        itemImageRepository.saveAll(images)

        // 6) 옵션(재고) 정보 생성
        // item1
        val itemOption1 = itemOptionRepository.save(ItemOption(item = item1, stock = 50))
        val itemOption1Size = itemOptionRepository.save(ItemOption(item = item1, stock = 50))
        // item2
        val itemOption2 = itemOptionRepository.save(ItemOption(item = item2, stock = 40))
        val itemOption2Size = itemOptionRepository.save(ItemOption(item = item2, stock = 40))
        // item3
        val itemOption3 = itemOptionRepository.save(ItemOption(item = item3, stock = 30))
        val itemOption3Size = itemOptionRepository.save(ItemOption(item = item3, stock = 30))

        val optionValues = listOf(
            // item1: color
            ItemOptionValue(itemOption = itemOption1, type = ItemOptionType.COLOR, value = "Black", detail = "000000"),
            ItemOptionValue(itemOption = itemOption1, type = ItemOptionType.COLOR, value = "Gray", detail = "C0C0C0"),
            // item1: size
            ItemOptionValue(itemOption = itemOption1Size, type = ItemOptionType.SIZE, value = "M", detail = "95"),
            ItemOptionValue(itemOption = itemOption1Size, type = ItemOptionType.SIZE, value = "L", detail = "100"),

            // item2: color
            ItemOptionValue(itemOption = itemOption2, type = ItemOptionType.COLOR, value = "Dark Blue", detail = "00008B"),
            ItemOptionValue(itemOption = itemOption2, type = ItemOptionType.COLOR, value = "Light Blue", detail = "ADD8E6"),
            // item2: size
            ItemOptionValue(itemOption = itemOption2Size, type = ItemOptionType.SIZE, value = "32", detail = "82cm"),
            ItemOptionValue(itemOption = itemOption2Size, type = ItemOptionType.SIZE, value = "34", detail = "86cm"),

            // item3: color
            ItemOptionValue(itemOption = itemOption3, type = ItemOptionType.COLOR, value = "Camel", detail = "C19A6B"),
            ItemOptionValue(itemOption = itemOption3, type = ItemOptionType.COLOR, value = "Gray", detail = "808080"),
            // item3: size
            ItemOptionValue(itemOption = itemOption3Size, type = ItemOptionType.SIZE, value = "L", detail = "100"),
            ItemOptionValue(itemOption = itemOption3Size, type = ItemOptionType.SIZE, value = "XL", detail = "105")
        )
        itemOptionValueRepository.saveAll(optionValues)
    }
}