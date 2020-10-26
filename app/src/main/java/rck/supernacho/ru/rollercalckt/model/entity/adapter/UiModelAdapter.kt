package rck.supernacho.ru.rollercalckt.model.entity.adapter

import rck.supernacho.ru.rollercalckt.domain.toBigDecimalOrDef
import rck.supernacho.ru.rollercalckt.model.entity.Brand
import rck.supernacho.ru.rollercalckt.model.entity.BrandUi
import rck.supernacho.ru.rollercalckt.model.entity.Material
import rck.supernacho.ru.rollercalckt.model.entity.MaterialUi

fun Material.toUiModel(isWeightEnabled: Boolean): MaterialUi =
        MaterialUi(
                id = this.id,
                name = this.name,
                thickness = this.thickness.toPlainString(),
                weight = this.weight.toPlainString(),
                density = this.density.toPlainString(),
                brand = this.brand.target.name,
                brandId = this.brand.targetId,
                weightVisibility = if(isWeightEnabled) 0 else 8
        )

fun MaterialUi.toMaterial(): Material =
        Material(
                id = this.id,
                name = this.name,
                thickness = this.thickness.toBigDecimalOrDef(),
                weight = this.weight.toBigDecimalOrDef(),
                density = this.density.toBigDecimalOrDef()
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