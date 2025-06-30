package com.young.domain.order.repository

import com.young.domain.order.domain.entity.Order
import com.young.domain.order.domain.entity.OrderItem
import com.young.domain.order.domain.entity.OrderItemOption
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderItemOptionRepository : JpaRepository<OrderItemOption, Long> {
    fun findByOrderItem(orderItem: OrderItem): List<OrderItemOption>

    @Query("""
        SELECT oio
          FROM OrderItemOption oio
          JOIN oio.orderItem oi
          JOIN oi.order ord
         WHERE ord = :order
    """)
    fun findByOrder(order: Order): List<OrderItemOption>

    /** 옵션 ID 목록 + 주문으로 필터 (부분취소 검증용) */
    @Query("""
        SELECT oio
          FROM OrderItemOption oio
          JOIN oio.orderItem oi
          JOIN oi.order ord
         WHERE oio.id IN :ids
           AND ord = :order
    """)
    fun findByIdsAndOrder(ids: List<Long>, order: Order): List<OrderItemOption>
}