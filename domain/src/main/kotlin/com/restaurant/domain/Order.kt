package com.restaurant.domain

private const val DESCRIPTION_LENGTH = 38
private const val QUANTITY_LENGTH = 3
private const val PRICE_LENGTH = 7
private const val AT = " @ "
private const val TIMES = " x "
private const val EQUALS = " = "

private const val PRICE_FORMAT = "£%.2f"

data class Order(
    val id: String,
    val tableNumber: Int,
    val customer: Customer,
    val staff: Staff,
    val items: MutableList<OrderItem>
) {
    fun fullItemisedFormattedBill(): List<String> {
        val bill = mutableListOf<String>()
        var total = 0.0
        items.forEach {
            bill.add(it.formattedBillItem())
            total = total.plus(it.itemCost())
        }
        bill.add("".padStart(lineLength(), '-'))
        bill.add(
            "Total is".padEnd(lineLength() - PRICE_LENGTH, ' ') + String.format(PRICE_FORMAT, total)
                .padStart(PRICE_LENGTH, ' ')
        )
        bill.add("".padStart(lineLength(), '='))

        return bill
    }

    companion object {
        internal fun lineLength(): Int =
            DESCRIPTION_LENGTH + AT.length + PRICE_LENGTH + TIMES.length + QUANTITY_LENGTH + EQUALS.length + PRICE_LENGTH
    }
}

data class OrderItem(
    val name: String,
    val quantity: Int,
    val price: Double
) {
    internal fun itemCost(): Double = price * quantity
    fun formattedBillItem(): String = name.padEnd(DESCRIPTION_LENGTH, ' ').substring(0, DESCRIPTION_LENGTH) +
            AT + String.format(PRICE_FORMAT, price).padStart(PRICE_LENGTH, ' ') +
            TIMES + quantity.toString().padStart(QUANTITY_LENGTH, ' ') +
            EQUALS + String.format(PRICE_FORMAT, itemCost()).padStart(PRICE_LENGTH, ' ')
}