package com.restaurant.domain

import com.restaurant.domain.Allergen.GLUTEN
import com.restaurant.domain.Allergen.SULPHUR_DIOXIDE
import java.time.LocalDateTime

open class Stock(
    open val name: String,
    open val usedFrom: LocalDateTime,
    open val lifeOnceOpened: Int,
    open val unit: String,
    open val allergens: List<Allergen> = listOf(),
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

enum class LarderType { FREEZER, FRIDGE, CUPBOARD, }

data class Batch(
    val item: Pair<Stock, Int>,
    val frozen: Boolean,
    val fresh: Boolean,
)

data class Delivery(
    val batches: List<Batch>,
    val expected: LocalDateTime,
)

data class Orange(
    override val usedFrom: LocalDateTime,
    override val lifeOnceOpened: Int,
) : Stock("orange", usedFrom, lifeOnceOpened, "each")

data class Sausage(
    override val usedFrom: LocalDateTime,
    override val lifeOnceOpened: Int,
) : Stock("sausage", usedFrom, lifeOnceOpened, unit = "each", listOf(GLUTEN))

data class Bacon(
    override val usedFrom: LocalDateTime,
    override val lifeOnceOpened: Int,
) : Stock("bacon", usedFrom, lifeOnceOpened, unit = "rasher", listOf(SULPHUR_DIOXIDE))

/*
data class <StockItem>(
    override val name: String,
    override val usedFrom: LocalDateTime,
    override val lifeOnceOpened: Int,
) : StockItem(name, usedFrom, lifeOnceOpened)
*/