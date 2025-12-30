package com.restaurant.domain

open class Person(
    open val name: String,
)

data class Customer(
    override val name: String,
    val age: Int,
) : Person(name) {
    companion object {
        fun customer(name: String, age: Int): Customer = Customer(name, age)
    }
}

open class Staff(
    override val name: String,
    val role: RoleType,
    val area: AreaType,
) : Person(name) {
    companion object {
        fun floorStaff(name: String): Staff {
            return Staff(name, RoleType.WORKER, AreaType.FLOOR)
        }
    }
}

enum class RoleType {
    SUPERVISOR, WORKER,
}

enum class AreaType {
    BAR, FLOOR, KITCHEN,
}

data class Site(
    val name: String,
    val staff: List<Staff>,
)