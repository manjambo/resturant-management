package com.restaurant.kitchen

import com.restaurant.domain.Order
import com.restaurant.domain.Customer
import com.restaurant.domain.OrderItem
import com.restaurant.domain.Staff

class FloorService {
    fun prepareOrder(order: Order): String =
        "Preparing order ${order.id} for ${order.customer.name} at table ${order.tableNumber}"
    fun serve(order: Order) {

    }
    fun takeOrder(customer: Customer, server: Staff, orderItems: MutableList<OrderItem>): Order {
        return Order(
            "0001",
            12,
            customer,
            server,
            orderItems,
        )
    }
    fun addItemToOrder(order: Order, orderItem: OrderItem) { order.items.add(orderItem) }
}