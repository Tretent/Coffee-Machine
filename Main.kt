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
        const val ESPRESSO_COST = 4

        const val WATER_PER_LATTE = 350
        const val MILK_PER_LATTE = 75
        const val COFFEE_BEANS_PER_LATTE = 20
        const val LATTE_COST = 7

        const val WATER_PER_CAPPUCCINO = 200
        const val MILK_PER_CAPPUCCINO = 100
        const val COFFEE_BEANS_PER_CAPPUCCINO = 12
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

        printStats()
        printMenu()
        when (readMainMenuChoice()) {
            "buy" -> buyAction()
            "fill" -> fillAction()
            "take" -> takeAction()
            else -> {
                println("invalid choice")
                return
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
        print("Write action (buy, fill, take): ")
    }

    private fun buyAction() {
        fun printBuyMenu() {
            print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino: ")
        }

        fun isValidChoice(choice: Int): Boolean = choice in 1..3

        fun readBuyMenuChoice(): Int =
            try {
                printBuyMenu()
                val coffeeChoice = readln().toInt()
                if (!isValidChoice(coffeeChoice)) throw Exception("coffee choice not valid")
                coffeeChoice
            } catch (e: NumberFormatException) {
                println("Please insert a valid number")
                readBuyMenuChoice()
            } catch (e: Exception) {
                println("Please enter a valid choice")
                readBuyMenuChoice()
            }

        when (readBuyMenuChoice()) {
            1 -> makeCoffee(CoffeeType.ESPRESSO)
            2 -> makeCoffee(CoffeeType.LATTE)
            3 -> makeCoffee(CoffeeType.CAPPUCCINO)
        }
        printStats()
    }

    private fun makeCoffee(coffeeType: CoffeeType) {
        when (coffeeType) {
            CoffeeType.ESPRESSO -> {
                updateMaterials(WATER_PER_ESPRESSO, 0, COFFEE_BEANS_PER_ESPRESSO, 1, ESPRESSO_COST, Operation.BUY)
            }
            CoffeeType.LATTE -> {
                updateMaterials(WATER_PER_LATTE, MILK_PER_LATTE, COFFEE_BEANS_PER_LATTE, 1, LATTE_COST, Operation.BUY)
            }
            CoffeeType.CAPPUCCINO -> {
                updateMaterials(
                    WATER_PER_CAPPUCCINO,
                    MILK_PER_CAPPUCCINO,
                    COFFEE_BEANS_PER_CAPPUCCINO,
                    1,
                    CAPPUCCINO_COST,
                    Operation.BUY
                )
            }
        }
    }

    private fun updateMaterials(water: Int = 0, milk: Int = 0, coffeeBeans: Int = 0, cups: Int = 0, money: Int = 0, operation: Operation) {
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
        printStats()
    }

    private fun takeAction() {
        println("I gave you \$$money")
        updateMaterials(operation = Operation.TAKE)
        printStats()
    }
}

fun main() {
    val coffeeMachine = CoffeeMachine()
    coffeeMachine.turnOn()
}
