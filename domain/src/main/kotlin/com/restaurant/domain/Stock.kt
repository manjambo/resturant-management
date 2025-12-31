package com.restaurant.domain

import com.restaurant.domain.Allergen.GLUTEN
import com.restaurant.domain.Allergen.SULPHUR_DIOXIDE
import java.time.LocalDateTime

open class Stock(
    open val name: String,
    open val usedFrom: LocalDateTime = LocalDateTime.now(),
    open val lifeOnceOpened: Int = 1,
    open val unit: String = "each",
    open val allergens: List<Allergen> = listOf(),
    open val lowStock: Int = 25,
    open val minimumStock: Int = 10,
)

enum class Allergen {
    PEANUTS, GLUTEN, EGGS, FISH, SOYBEANS, MILK, NUTS, SESAME,
    LUPIN, CELERY, SULPHUR_DIOXIDE, CRUSTACEANS, MOLLUSCS, MUSTARD,
}

data class Larder(
    val larderType: LarderType,
    val items: MutableList<Pair<Stock, Int>>,
) {
    fun useItem(item: Pair<Stock, Int>) {
        item.first.name
        LarderType.entries.forEach { updateLarderByType(item, it) }
    }

    private fun updateLarderByType(item: Pair<Stock, Int>, larderType: LarderType) {
        val larder = larderByType(larderType)
        larder.items.find { it.first == item.first }?.let {
            larder.items.remove(item)
            larder.items.add(item.copy(first = item.first, second = it.second - item.second))
        }
    }

    //    TODO: database
    fun larderByType(larderType: LarderType): Larder = Larder(larderType, items = mutableListOf())
}

enum class LarderType { FREEZER, FRIDGE, FRESH, CUPBOARD, }

data class Batch(
    val item: Pair<Stock, Int>,
    val larderType: LarderType
)

data class Delivery(
    val batches: List<Batch>,
    val expected: LocalDateTime,
)