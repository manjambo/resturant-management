package com.restaurant.domain

import com.restaurant.domain.Allergen.GLUTEN
import com.restaurant.domain.Allergen.SULPHUR_DIOXIDE
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldBeEmpty
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.time.LocalDateTime

class StockTest : BehaviorSpec({
    class Orange(
        override val lifeOnceOpened: Int=1,
    ) : Stock("orange", lifeOnceOpened = lifeOnceOpened)

    class Sausage(
        override val lifeOnceOpened: Int=5,
    ) : Stock("sausage", lifeOnceOpened = lifeOnceOpened, allergens = listOf(GLUTEN), minimumStock = 25, lowStock = 50)

    class Bacon(
        override val lifeOnceOpened: Int=7,
    ) : Stock("bacon", lifeOnceOpened = lifeOnceOpened, allergens = listOf(SULPHUR_DIOXIDE))

    given("a StockItem") {
        val now = LocalDateTime.now()
        val stock = Stock(
            name = "Generic Item",
            usedFrom = now,
            lifeOnceOpened = 7,
            allergens = listOf(Allergen.PEANUTS, Allergen.EGGS)
        )

        `when`("created with properties") {
            then("it should have the correct name") {
                stock.name shouldBe "Generic Item"
            }

            then("it should have the correct usedFrom date") {
                stock.usedFrom shouldBe now
            }

            then("it should have the correct lifeOnceOpened") {
                stock.lifeOnceOpened shouldBe 7
            }

            then("it should contain the specified allergens") {
                stock.allergens shouldHaveSize 2
                stock.allergens shouldContain Allergen.PEANUTS
                stock.allergens shouldContain Allergen.EGGS
            }
        }

        `when`("created without allergens") {
            val itemWithoutAllergens = Stock(
                name = "Safe Item",
                usedFrom = now,
                lifeOnceOpened = 5
            )

            then("it should have an empty allergens list") {
                itemWithoutAllergens.allergens.shouldBeEmpty()
            }
        }
    }

    given("a Batch") {
        val batch = Batch(
            Pair(Orange(), 50),
            LarderType.FRIDGE,
        )

        `when`("created with fresh items") {
            then("it should contain the correct stock item") {
                batch.item.first.name shouldBe Orange().name
            }

            then("it should have the correct quantity") {
                batch.item.second shouldBe 50
            }

            then("it should be marked as fresh") {
                batch.larderType shouldBe LarderType.FRIDGE
            }
        }

    }

    given("a Delivery") {
        val now = LocalDateTime.now()
        val orange = Orange()
        val sausage = Sausage()

        val batches = listOf(
            Batch(item = Pair(orange, 50), LarderType.FRIDGE),
            Batch(item = Pair(sausage, 30), LarderType.FREEZER)
        )

        val expectedTime = LocalDateTime.of(2024, 1, 25, 14, 0)
        val delivery = Delivery(batches = batches, expected = expectedTime)

        `when`("created with multiple batches") {
            then("it should contain all batches") {
                delivery.batches shouldHaveSize 2
            }

            then("it should have the correct expected delivery time") {
                delivery.expected shouldBe expectedTime
            }

            then("it should contain the orange batch") {
                delivery.batches[0].item.first shouldBe orange
                delivery.batches[0].item.second shouldBe 50
            }

            then("it should contain the sausage batch") {
                delivery.batches[1].item.first shouldBe sausage
                delivery.batches[1].item.second shouldBe 30
            }
        }

        `when`("created with empty batches") {
            val emptyDelivery = Delivery(
                batches = emptyList(),
                expected = expectedTime
            )

            then("it should have no batches") {
                emptyDelivery.batches.shouldBeEmpty()
            }
        }
    }

    given("different StockItem types") {
        val now = LocalDateTime.now()
        val orange = Orange(3)
        val sausage = Sausage()
        val bacon = Bacon()

        `when`("comparing allergens") {
            then("orange should have no allergens") {
                orange.allergens.shouldBeEmpty()
            }

            then("sausage should have different allergens than bacon") {
                sausage.allergens shouldNotBe bacon.allergens
            }

            then("sausage should contain GLUTEN") {
                sausage.allergens shouldContain Allergen.GLUTEN
            }

            then("bacon should contain SULPHUR_DIOXIDE") {
                bacon.allergens shouldContain Allergen.SULPHUR_DIOXIDE
            }
        }

        `when`("comparing shelf life") {
            then("orange should have the shortest life") {
                orange.lifeOnceOpened shouldBe 3
            }

            then("bacon should have the longest life") {
                bacon.lifeOnceOpened shouldBe 7
            }

            then("sausage should have medium life") {
                sausage.lifeOnceOpened shouldBe 5
            }
        }
    }

    given("all Allergen enum values") {
        `when`("checking allergen completeness") {
            then("there should be 14 allergen types") {
                Allergen.entries shouldHaveSize 14
            }

            then("it should contain all major allergens") {
                val allergenNames = Allergen.entries.map { it.name }
                allergenNames shouldContain "PEANUTS"
                allergenNames shouldContain "GLUTEN"
                allergenNames shouldContain "EGGS"
                allergenNames shouldContain "FISH"
                allergenNames shouldContain "SOYBEANS"
                allergenNames shouldContain "MILK"
                allergenNames shouldContain "NUTS"
                allergenNames shouldContain "SESAME"
                allergenNames shouldContain "LUPIN"
                allergenNames shouldContain "CELERY"
                allergenNames shouldContain "SULPHUR_DIOXIDE"
                allergenNames shouldContain "CRUSTACEANS"
                allergenNames shouldContain "MOLLUSCS"
                allergenNames shouldContain "MUSTARD"
            }
        }
    }
})