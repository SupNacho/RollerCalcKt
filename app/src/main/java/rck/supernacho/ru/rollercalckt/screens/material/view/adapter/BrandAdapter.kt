package rck.supernacho.ru.rollercalckt.screens.material.view.adapter

import android.content.Context
import android.widget.ArrayAdapter
import rck.supernacho.ru.rollercalckt.model.entity.BrandUi

class BrandAdapter(context: Context, val resourceId: Int, items: List<BrandUi>): ArrayAdapter<BrandUi>(context, resourceId, items) {

}