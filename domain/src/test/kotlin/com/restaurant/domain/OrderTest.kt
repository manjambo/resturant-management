package com.restaurant.domain

import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain


class OrderTest : BehaviorSpec({

    class Beer : Stock("beer")
    class Fish : Stock("fish")
    class Chips : Stock("chips")
    class SpicyMushroomPeople : Stock("spicy mushroom people")
    class CornBread : Stock("corn bread")
    class ChickenWings : Stock("chicken wings")


    given("given I have an Order a bunch of stuff") {

        val order = Order(
            "0001",
            25,
            Customer.customer("Barry the Barry", 25),
            Staff.floorStaff("Mary"),
            mutableListOf(
                OrderItem("beer", listOf(Beer()), 4, 6.75),
                OrderItem("fish and chips",listOf(Fish(), Chips()), 2, 8.75),
                OrderItem("spicy mushroom people on corn bread", listOf(SpicyMushroomPeople(), CornBread()), 5, 11.25),
                OrderItem("Chicken wings", listOf(ChickenWings()), 5, 2.75)
            )
        )

        `when`("I produce a bill") {
            val bill = order.fullItemisedFormattedBill()
            then("totaling £114.50") {
                bill.size shouldBe 7
                bill.forEach {
                    println(it)
                    it.length shouldBe Order.lineLength()
                }
                penultimateItem(bill) shouldContain "£114.50"
            }
        }
    }
    given("given I have an Order for Fish and Chips @ £8.75 twice") {
        val fishAndChipsOrder = OrderItem("fish and chips", listOf(Fish(),Chips()), 2, 8.75)
        `when`("I produce a bill") {
            val itemCost = fishAndChipsOrder.itemCost()
            then("The total bill should be for £17.50") {
                itemCost shouldBe 17.5
            }
        }
        `when`("preparing an itemised bill") {
            val itemCost = fishAndChipsOrder.formattedBillItem()
            then("The total bill should be for £17.50") {
                itemCost shouldContain "fish and chips"
                itemCost shouldContain "£8.75"
                itemCost shouldContain "2"
                itemCost shouldContain "£17.50"
            }
        }
    }
})

private fun penultimateItem(list: List<String>) = list.dropLast(1).last()