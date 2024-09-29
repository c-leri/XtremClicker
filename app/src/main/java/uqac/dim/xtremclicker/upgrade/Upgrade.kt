package uqac.dim.xtremclicker.upgrade

import android.graphics.drawable.Drawable

class Upgrade private constructor(
    val id: Long,
    val name: String,
    val icon: Drawable,
    val description: String,
    val basePrice: Long,
    val priceIncrementFactor: Double,
    val flatBonus: Long,
    val factorBonus: Double
) {
    class Builder(
        private val id: Long
    ) {
        private var name: String? = null
        private var icon: Drawable? = null
        private var description: String? = null
        private var basePrice: Long? = null
        private var priceIncrementFactor: Double? = null
        private var flatBonus: Long? = null
        private var factorBonus: Double? = null

        fun name(name: String): Builder {
            this.name = name
            return this
        }

        fun icon(icon: Drawable?): Builder {
            if (icon == null) throw IllegalStateException()

            this.icon = icon
            return this
        }

        fun description(description: String): Builder {
            this.description = description
            return this
        }

        fun basePrice(basePrice: Long): Builder {
            this.basePrice = basePrice
            return this
        }

        fun priceIncrementFactor(priceIncrementFactor: Double): Builder {
            this.priceIncrementFactor = priceIncrementFactor
            return this
        }

        fun flatBonus(flatBonus: Long): Builder {
            this.flatBonus = flatBonus
            return this
        }

        fun factorBonus(factorBonus: Double): Builder {
            this.factorBonus = factorBonus
            return this
        }

        fun build(): Upgrade {
            if (this.name == null || this.icon == null || this.description == null || this.basePrice == null || this.priceIncrementFactor == null || this.flatBonus == null || this.factorBonus == null) {
                throw IllegalStateException()
            }

            return Upgrade(
                this.id,
                this.name!!,
                this.icon!!,
                this.description!!,
                this.basePrice!!,
                this.priceIncrementFactor!!,
                this.flatBonus!!,
                this.factorBonus!!
            )
        }
    }
}
