package machine

class CoffeeMachine(
    private var water: Int = 400,
    private var milk: Int = 540,
    private var coffeeBeans: Int = 120,
    private var cups: Int = 9,
    private var money: Int = 550
) {
    companion object {
        const val WATER_PER_ESPRESSO = 250
        const val COFFEE_BEANS_PER_ESPRESSO = 16
        const val CUPS_PER_ESPRESSO = 1
        const val ESPRESSO_COST = 4

        const val WATER_PER_LATTE = 350
        const val MILK_PER_LATTE = 75
        const val COFFEE_BEANS_PER_LATTE = 20
        const val CUPS_PER_LATTE = 1
        const val LATTE_COST = 7

        const val WATER_PER_CAPPUCCINO = 200
        const val MILK_PER_CAPPUCCINO = 100
        const val COFFEE_BEANS_PER_CAPPUCCINO = 12
        const val CUPS_PER_CAPPUCCINO = 1
        const val CAPPUCCINO_COST = 6
    }

    enum class CoffeeType {
        ESPRESSO, LATTE, CAPPUCCINO
    }

    enum class Operation {
        BUY, FILL, TAKE
    }

    fun turnOn() {
        fun readMainMenuChoice(): String = readln()

        while (true) {
            printMenu()
            when (readMainMenuChoice()) {
                "buy" -> buyAction()
                "fill" -> fillAction()
                "take" -> takeAction()
                "remaining" -> printStats()
                "exit" -> return
                else -> {
                    println("invalid choice")
                    return
                }
            }
        }
    }

    private fun printStats() {
        println()
        println("The coffee machine has:")
        println("$water ml of water")
        println("$milk ml of milk")
        println("$coffeeBeans g of coffee beans")
        println("$cups disposable cups")
        println("\$$money of money")
    }

    private fun printMenu() {
        println()
        print("Write action (buy, fill, take, remaining, exit): ")
    }

    private fun buyAction() {
        fun printBuyMenu() {
            print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
        }

        fun isValidChoice(choice: String): Boolean = choice.first() in '1'..'3' || choice == "back"

        fun readBuyMenuChoice(): String =
            try {
                printBuyMenu()
                val coffeeChoice = readln().trim()
                if (!isValidChoice(coffeeChoice)) throw Exception("coffee choice not valid")
                coffeeChoice
            } catch (e: Exception) {
                println("Please enter a valid choice")
                readBuyMenuChoice()
            }

        when (readBuyMenuChoice()) {
            "1" -> if (areResourcesEnoughFor(CoffeeType.ESPRESSO)) {
                println("I have enough resources, making you a coffee!")
                makeCoffee(CoffeeType.ESPRESSO)
            } else printFailingCauseFor(
                CoffeeType.ESPRESSO
            )
            "2" -> if (areResourcesEnoughFor(CoffeeType.LATTE)) {
                println("I have enough resources, making you a coffee!")
                makeCoffee(CoffeeType.LATTE)
            } else printFailingCauseFor(
                CoffeeType.LATTE
            )
            "3" -> if (areResourcesEnoughFor(CoffeeType.CAPPUCCINO)) {
                println("I have enough resources, making you a coffee!")
                makeCoffee(CoffeeType.CAPPUCCINO)
            } else printFailingCauseFor(
                CoffeeType.CAPPUCCINO
            )
            "back" -> return
        }
    }

    private fun areResourcesEnoughFor(coffeeType: CoffeeType): Boolean {
        val espressoConditions =
            water >= WATER_PER_ESPRESSO && coffeeBeans >= COFFEE_BEANS_PER_ESPRESSO && cups >= CUPS_PER_ESPRESSO
        val latteConditions =
            water >= WATER_PER_LATTE && milk >= MILK_PER_LATTE && coffeeBeans >= COFFEE_BEANS_PER_LATTE && cups >= CUPS_PER_LATTE
        val cappuccinoConditions =
            water >= WATER_PER_CAPPUCCINO && milk >= MILK_PER_CAPPUCCINO && coffeeBeans >= COFFEE_BEANS_PER_CAPPUCCINO && cups >= CUPS_PER_CAPPUCCINO

        return when (coffeeType) {
            CoffeeType.ESPRESSO -> espressoConditions
            CoffeeType.LATTE -> latteConditions
            CoffeeType.CAPPUCCINO -> cappuccinoConditions
        }
    }

    private fun printFailingCauseFor(coffeeType: CoffeeType) {
        when (coffeeType) {
            CoffeeType.ESPRESSO -> {
                when {
                    water < WATER_PER_ESPRESSO -> println("Sorry, not enough water!")
                    coffeeBeans < COFFEE_BEANS_PER_ESPRESSO -> println("Sorry, not enough coffee beans!")
                    cups < CUPS_PER_ESPRESSO -> println("Sorry, not enough disposable cups!")
                }
            }
            CoffeeType.LATTE -> {
                when {
                    water < WATER_PER_LATTE -> println("Sorry, not enough water!")
                    milk < MILK_PER_LATTE -> println("Sorry, not enough milk!")
                    coffeeBeans < COFFEE_BEANS_PER_LATTE -> println("Sorry, not enough coffee beans!")
                    cups < CUPS_PER_LATTE -> println("Sorry, not enough disposable cups!")
                }
            }
            CoffeeType.CAPPUCCINO -> {
                when {
                    water < WATER_PER_CAPPUCCINO -> println("Sorry, not enough water!")
                    milk < MILK_PER_CAPPUCCINO -> println("Sorry, not enough milk!")
                    coffeeBeans < COFFEE_BEANS_PER_CAPPUCCINO -> println("Sorry, not enough coffee beans!")
                    cups < CUPS_PER_CAPPUCCINO -> println("Sorry, not enough disposable cups!")
                }
            }
        }
    }

    private fun makeCoffee(coffeeType: CoffeeType) {
        when (coffeeType) {
            CoffeeType.ESPRESSO -> {
                updateMaterials(
                    WATER_PER_ESPRESSO,
                    0,
                    COFFEE_BEANS_PER_ESPRESSO,
                    CUPS_PER_ESPRESSO,
                    ESPRESSO_COST,
                    Operation.BUY
                )
            }
            CoffeeType.LATTE -> {
                updateMaterials(
                    WATER_PER_LATTE,
                    MILK_PER_LATTE,
                    COFFEE_BEANS_PER_LATTE,
                    CUPS_PER_LATTE,
                    LATTE_COST,
                    Operation.BUY
                )
            }
            CoffeeType.CAPPUCCINO -> {
                updateMaterials(
                    WATER_PER_CAPPUCCINO,
                    MILK_PER_CAPPUCCINO,
                    COFFEE_BEANS_PER_CAPPUCCINO,
                    CUPS_PER_CAPPUCCINO,
                    CAPPUCCINO_COST,
                    Operation.BUY
                )
            }
        }
    }

    private fun updateMaterials(
        water: Int = 0,
        milk: Int = 0,
        coffeeBeans: Int = 0,
        cups: Int = 0,
        money: Int = 0,
        operation: Operation
    ) {
        when (operation) {
            Operation.BUY -> {
                this.water -= water
                this.milk -= milk
                this.coffeeBeans -= coffeeBeans
                this.cups -= cups
                this.money += money
            }
            Operation.FILL -> {
                this.water += water
                this.milk += milk
                this.coffeeBeans += coffeeBeans
                this.cups += cups
            }
            Operation.TAKE -> {
                this.money = 0
            }
        }
    }

    private fun fillAction() {
        fun isValidWaterQuantity(waterQuantity: Int): Boolean = waterQuantity >= 0
        fun isValidMilkQuantity(milkQuantity: Int): Boolean = milkQuantity >= 0
        fun isValidCoffeeBeansQuantity(coffeeBeansQuantity: Int): Boolean = coffeeBeansQuantity >= 0
        fun isValidCupsQuantity(cupsQuantity: Int): Boolean = cupsQuantity >= 0

        fun readFillMenuChoice(): List<Int> =
            try {
                print("Write how many ml of water do you want to add: ")
                val refilledWater = readln().toInt()
                if (!isValidWaterQuantity(refilledWater)) throw Exception("refilled water choice not valid")
                print("Write how many ml of milk do you want to add: ")
                val refilledMilk = readln().toInt()
                if (!isValidMilkQuantity(refilledMilk)) throw Exception("refilled milk choice not valid")
                print("Write how many grams of coffee beans do you want to add: ")
                val refilledCoffeeBeans = readln().toInt()
                if (!isValidCoffeeBeansQuantity(refilledCoffeeBeans)) throw Exception("refilled coffee beans choice not valid")
                print("Write how many disposable cups of coffee do you want to add: ")
                val refilledCups = readln().toInt()
                if (!isValidCupsQuantity(refilledCups)) throw Exception("refilled cups choice not valid")
                listOf(refilledWater, refilledMilk, refilledCoffeeBeans, refilledCups)
            } catch (e: NumberFormatException) {
                println("Please insert a valid number")
                readFillMenuChoice()
            } catch (e: Exception) {
                println("Please enter a non-negative quantity")
                readFillMenuChoice()
            }

        val (refilledWater, refilledMilk, refilledCoffeeBeans, refilledCups) = readFillMenuChoice()
        updateMaterials(refilledWater, refilledMilk, refilledCoffeeBeans, refilledCups, operation = Operation.FILL)
    }

    private fun takeAction() {
        println("I gave you \$$money")
        updateMaterials(operation = Operation.TAKE)
    }
}

fun main() {
    val coffeeMachine = CoffeeMachine()
    coffeeMachine.turnOn()
}
