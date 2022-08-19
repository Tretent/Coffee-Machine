package machine

class CoffeeMachine(
    private var water: Int = 400,
    private var milk: Int = 540,
    private var coffeeBeans: Int = 120,
    private var cups: Int = 9,
    private var money: Int = 550
) {
    enum class CoffeeType(
        val water: Int,
        val milk: Int,
        val coffeeBeans: Int,
        val cups: Int,
        val cost: Int,
        val choiceNumber: Int
    ) {
        ESPRESSO(250, 0, 16, 1, 4, 1),
        LATTE(350, 75, 20, 1, 7, 2),
        CAPPUCCINO(200, 100, 12, 1, 6, 3),
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
        fun printFailingCauseFor(coffeeType: CoffeeType) {
            println(
                when {
                    water < coffeeType.water -> "Sorry, not enough water!"
                    milk < coffeeType.milk -> "Sorry, not enough milk!"
                    coffeeBeans < coffeeType.coffeeBeans -> "Sorry, not enough coffee beans!"
                    cups < coffeeType.cups -> "Sorry, not enough disposable cups!"
                    else -> throw Exception("unknown cause")
                }
            )
        }

        fun makeCoffee(coffeeType: CoffeeType) {
            updateMaterials(
                coffeeType.water,
                coffeeType.milk,
                coffeeType.coffeeBeans,
                coffeeType.cups,
                coffeeType.cost,
                Operation.BUY
            )
        }

        fun areResourcesEnoughFor(coffeeType: CoffeeType): Boolean =
            water >= coffeeType.water && milk >= coffeeType.milk && coffeeBeans >= coffeeType.coffeeBeans && cups >= coffeeType.cups

        fun findCoffeeByChoiceNumber(choiceNumber: String): CoffeeType =
            CoffeeType.values().first { it.choiceNumber == choiceNumber.toInt() }

        fun isValidChoice(choice: String): Boolean = choice.first() in '1'..'3' || choice == "back"

        fun printBuyMenu() {
            print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu: ")
        }

        fun readBuyMenuChoice(): String = try {
            printBuyMenu()
            val coffeeChoice = readln().trim()
            if (!isValidChoice(coffeeChoice)) throw Exception("coffee choice not valid")
            coffeeChoice
        } catch (e: Exception) {
            println("Please enter a valid choice")
            readBuyMenuChoice()
        }

        when (val choice = readBuyMenuChoice()) {
            "back" -> return
            else -> {
                val coffeeType = findCoffeeByChoiceNumber(choice)
                if (areResourcesEnoughFor(coffeeType)) {
                    println("I have enough resources, making you a coffee!")
                    makeCoffee(coffeeType)
                } else printFailingCauseFor(coffeeType)
            }
        }
    }

    private fun updateMaterials(
        water: Int = 0, milk: Int = 0, coffeeBeans: Int = 0, cups: Int = 0, money: Int = 0, operation: Operation
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

        fun readFillMenuChoice(): List<Int> = try {
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
