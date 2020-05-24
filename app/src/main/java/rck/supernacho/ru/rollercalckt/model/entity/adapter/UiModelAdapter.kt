package rck.supernacho.ru.rollercalckt.model.entity.adapter

import rck.supernacho.ru.rollercalckt.model.entity.Brand
import rck.supernacho.ru.rollercalckt.model.entity.BrandUi
import rck.supernacho.ru.rollercalckt.model.entity.Material
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi
import java.math.BigDecimal

fun Material.toUiModel(): MaterialUi =
        MaterialUi(
                id = this.id,
                name = this.name,
                thickness = this.thickness.toPlainString(),
                weight = this.weight.toPlainString(),
                density = this.density.toPlainString(),
                brand = this.brand.target.name,
                brandId = this.brand.targetId
        )

fun MaterialUi.toMaterial(): Material =
        Material(
                id = this.id,
                name = this.name,
                thickness = this.thickness?.toBigDecimal() ?: BigDecimal.ZERO,
                weight = this.weight?.toBigDecimal() ?: BigDecimal.ZERO,
                density = this.density?.toBigDecimal() ?: BigDecimal.ZERO
        ).apply {
            this@toMaterial.brandId?.let { brand.targetId = it }
                    ?: let { brand.target = Brand(name = this@toMaterial.brand) }
        }

fun Brand.toUiModel(): BrandUi =
        BrandUi(
                id = this.id,
                name = this.name
        )

fun BrandUi.toBrand(): Brand =
        Brand(
                id = this.id,
                name = this.name
        )