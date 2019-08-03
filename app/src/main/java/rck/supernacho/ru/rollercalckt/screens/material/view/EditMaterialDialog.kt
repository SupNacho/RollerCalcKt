package rck.supernacho.ru.rollercalckt.screens.material.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import rck.supernacho.ru.rollercalckt.R

class EditMaterialDialog: DialogFragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.edit_material_dialog, container, false)
    }
}