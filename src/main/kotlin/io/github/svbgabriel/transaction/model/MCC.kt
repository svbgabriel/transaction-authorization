package io.github.svbgabriel.transaction.model

enum class MCC {
    FOOD,
    MEAL,
    CASH,
    ;

    companion object {
        fun getMCCValue(value: String): MCC =
            when (value) {
                "5411", "5412" -> FOOD
                "5811", "5812" -> MEAL
                else -> CASH
            }
    }
}
