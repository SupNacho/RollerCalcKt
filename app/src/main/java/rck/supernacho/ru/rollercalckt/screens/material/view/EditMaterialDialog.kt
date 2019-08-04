package rck.supernacho.ru.rollercalckt.screens.material.view

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.edit_material_dialog.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.databinding.EditMaterialDialogBinding
import rck.supernacho.ru.rollercalckt.screens.material.view.event.ClickEvent
import rck.supernacho.ru.rollercalckt.screens.utils.RCViewModelFactory

class EditMaterialDialog: DialogFragment(), KodeinAware {
    override val kodein: Kodein by closestKodein()

    private val viewModel: EditMaterialViewModel by lazy {
        ViewModelProviders.of(this, RCViewModelFactory(kodein)).get(EditMaterialViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding: EditMaterialDialogBinding = DataBindingUtil.inflate(inflater, R.layout.edit_material_dialog, container, false)
        binding.viewModel = viewModel
        arguments?.let {
            viewModel.materialId = it.getLong(MATERIAL_ID)
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.actionState.observe(this, Observer {
            when(it){
                is ClickEvent.DismissDialog -> dismiss()
                else -> {}
            }
        })
    }

    override fun onResume() {
        super.onResume()
        setWindowSize()
    }

    private fun setWindowSize() {
        val displayMetrics = DisplayMetrics()
        activity?.windowManager?.defaultDisplay?.getMetrics(displayMetrics)
        val displayWidth = displayMetrics.widthPixels
        val layoutParams = WindowManager.LayoutParams()
        dialog.window?.let { layoutParams.copyFrom(it.attributes) }
        val dialogWidth = displayWidth * 0.9f
        layoutParams.width = dialogWidth.toInt()
        dialog.window?.let { it.attributes = layoutParams }
    }

    companion object {

        private const val MATERIAL_ID = "material_id"

        fun getInstance(materialId: Long? = null): DialogFragment{
            val instance = EditMaterialDialog()
            materialId?.let {
                instance.arguments = Bundle().apply {
                    putLong(MATERIAL_ID, it)
                }
            }
            return instance
        }
    }
}