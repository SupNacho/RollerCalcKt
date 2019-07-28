package rck.supernacho.ru.rollercalckt.screens.material.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_calculation.*
import kotlinx.android.synthetic.main.fragment_material.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import rck.supernacho.ru.rollercalckt.MainActivity
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.databinding.FragmentMaterialBinding
import rck.supernacho.ru.rollercalckt.screens.material.view.adapter.MaterialListAdapter
import rck.supernacho.ru.rollercalckt.screens.utils.RCViewModelFactory


class MaterialFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val viewModel: MaterialsViewModel by lazy {
        ViewModelProviders.of(this, RCViewModelFactory(kodein)).get(MaterialsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding: FragmentMaterialBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_material, container, false)
        binding.viewModel = viewModel
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rv_materials.layoutManager = LinearLayoutManager(context).apply { orientation = RecyclerView.VERTICAL }
        rv_materials.adapter = MaterialListAdapter()
        viewModel.materialsList.observe(this, Observer {
            (rv_materials.adapter as MaterialListAdapter).submitList(it)
        })
    }
}
