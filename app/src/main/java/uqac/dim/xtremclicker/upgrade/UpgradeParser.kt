package uqac.dim.xtremclicker.upgrade

import android.content.res.Resources
import android.content.res.XmlResourceParser
import androidx.core.content.res.ResourcesCompat
import uqac.dim.xtremclicker.R


object UpgradeParser {
    private const val UPGRADE_TAG = "upgrade"
    private const val NAME_TAG = "name"
    private const val ICON_TAG = "icon"
    private const val DESCRIPTION_TAG = "description"
    private const val BASE_PRICE_TAG = "base-price"
    private const val PRICE_INCREMENT_FACTOR_TAG = "price-increment-factor"
    private const val FLAT_BONUS_TAG = "flat-bonus"
    private const val FACTOR_BONUS_TAG = "factor-bonus"
    private const val ID_ATTRIBUTE = "id"
    private const val VALUE_ATTRIBUTE = "value"


    private val UPGRADE_PROPERTIES_TAGS = arrayOf(
        NAME_TAG,
        ICON_TAG,
        DESCRIPTION_TAG,
        BASE_PRICE_TAG,
        PRICE_INCREMENT_FACTOR_TAG,
        FLAT_BONUS_TAG,
        FACTOR_BONUS_TAG
    )

    fun getUpgrades(resources: Resources): List<Upgrade> {
        val parser = resources.getXml(R.xml.upgrades)

        val result = ArrayList<Upgrade>()
        var currentBuilder: Upgrade.Builder? = null

        var eventType = parser.eventType
        while (eventType != XmlResourceParser.END_DOCUMENT) {
            if (eventType == XmlResourceParser.START_TAG) {
                if (parser.name == UPGRADE_TAG) {
                    if (parser.attributeCount < 1 || parser.getAttributeName(
                            0
                        ) != ID_ATTRIBUTE
                    ) throw IllegalStateException()

                    currentBuilder = Upgrade.Builder(parser.getAttributeValue(0).toLong())
                } else if (parser.name in UPGRADE_PROPERTIES_TAGS) {
                    if (currentBuilder == null || parser.attributeCount < 1 || parser.getAttributeName(
                            0
                        ) != VALUE_ATTRIBUTE
                    ) throw IllegalStateException()

                    currentBuilder = when (parser.name) {
                        NAME_TAG -> currentBuilder.name(
                            resources.getString(
                                parser.getAttributeResourceValue(
                                    0,
                                    0
                                )
                            )
                        )

                        ICON_TAG -> currentBuilder.icon(
                            ResourcesCompat.getDrawable(
                                resources, parser.getAttributeResourceValue(0, 0), null
                            )
                        )

                        DESCRIPTION_TAG -> currentBuilder.description(
                            resources.getString(
                                parser.getAttributeResourceValue(
                                    0,
                                    0
                                )
                            )
                        )

                        BASE_PRICE_TAG -> currentBuilder.basePrice(
                            parser.getAttributeValue(
                                0
                            ).toLong()
                        )

                        PRICE_INCREMENT_FACTOR_TAG -> currentBuilder.priceIncrementFactor(
                            parser.getAttributeValue(
                                0
                            ).toDouble()
                        )

                        FLAT_BONUS_TAG -> currentBuilder.flatBonus(
                            parser.getAttributeValue(
                                0
                            ).toLong()
                        )

                        FACTOR_BONUS_TAG -> currentBuilder.factorBonus(
                            parser.getAttributeValue(
                                0
                            ).toDouble()
                        )

                        else -> throw IllegalStateException()
                    }
                }
            } else if (eventType == XmlResourceParser.END_TAG && parser.name == UPGRADE_TAG) {
                if (currentBuilder == null) throw IllegalStateException()

                result.add(currentBuilder.build())
            }

            eventType = parser.next()
        }

        parser.close()

        return result
    }
}