package rck.supernacho.ru.rollercalckt.screens.material.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.skydoves.balloon.createBalloon
import com.skydoves.balloon.showAlignLeft
import com.skydoves.balloon.showBalloon
import com.yandex.metrica.YandexMetrica
import kotlinx.android.synthetic.main.fragment_material.*
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.closestKodein
import rck.supernacho.ru.rollercalckt.R
import rck.supernacho.ru.rollercalckt.databinding.FragmentMaterialBinding
import rck.supernacho.ru.rollercalckt.model.entity.MeasureSystem
import rck.supernacho.ru.rollercalckt.screens.material.view.adapter.MaterialListAdapter
import rck.supernacho.ru.rollercalckt.screens.material.view.event.ClickEvent
import rck.supernacho.ru.rollercalckt.screens.setBalloonSettings
import rck.supernacho.ru.rollercalckt.screens.utils.BalloonType
import rck.supernacho.ru.rollercalckt.screens.utils.RCViewModelFactory


class MaterialFragment : Fragment(), KodeinAware {

    override val kodein: Kodein by closestKodein()

    private val viewModel: MaterialsViewModel by lazy {
        ViewModelProvider(this, RCViewModelFactory(kodein)).get(MaterialsViewModel::class.java)
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
        rv_materials.adapter = MaterialListAdapter(viewModel)
        viewModel.materialsList.observe(viewLifecycleOwner, {
            (rv_materials.adapter as MaterialListAdapter).submitList(it)
        })

        viewModel.actionState.observe(viewLifecycleOwner, {
            when(it){
                is ClickEvent.EditClick -> {
                    YandexMetrica.reportEvent("edit pressed", "{\"Edited material\":\"${it.material}\"")
                    findNavController().navigate(MaterialFragmentDirections.toManageMaterial(it.material.id))
                }
                is ClickEvent.AddClick -> {
                    YandexMetrica.reportEvent("Start add material","{\"started\"}")
                    findNavController().navigate(MaterialFragmentDirections.toManageMaterial())
                }
                is ClickEvent.SelectClick -> {
                    YandexMetrica.reportEvent("Select material","{\"selected\":\"true\"}")
                    findNavController().navigate(R.id.navigation_home)
                }
                is ClickEvent.DismissDialog -> Unit
                is ClickEvent.BalloonClick -> {
                    YandexMetrica.reportEvent("Balloon Showed", "{\"Balloon type\":\"${it.type.name}\"")
                    showBalloon(it.view, it.type)
                }
            }
        })

        sv_sortMaterial.run {
            setOnChangeListener { viewModel.filterByText(it) }
            setNameSortAction { viewModel.sortByName() }
            setThickSortAction { viewModel.sortByThick() }
            setWeightSortAction { viewModel.sortByWeight() }
            setDensitySortAction { viewModel.sortByDensity() }
        }

    }

    private fun showBalloon(view: View, type: BalloonType){
        when(type){
            BalloonType.THICK -> {
                val (pcs, oneMicron) = if (viewModel.preferences.getSettings().measureSystem == MeasureSystem.IMPERIAL)
                    Pair(
                            requireContext().getString(R.string.calc_measure_imperial),
                            requireContext().getString(R.string.one_micron_imp)
                    )
                else
                    Pair(
                            requireContext().getString(R.string.calc_measure_metric),
                            requireContext().getString(R.string.one_micron_m)
                    )
                val thickBalloon = createBalloon(requireContext()) { setBalloonSettings(view, viewLifecycleOwner, requireContext().getString(R.string.thick_balloon, pcs, oneMicron, pcs, pcs), false) }
                view.showAlignLeft(thickBalloon)
            }
            BalloonType.WEIGHT -> {
                val weightBalloon = createBalloon(requireContext()) { setBalloonSettings(view, viewLifecycleOwner, requireContext().getString(R.string.weight_balloon), false) }
                view.showAlignLeft(weightBalloon)
            }
            BalloonType.DENSITY -> {
                val densityBalloon = createBalloon(requireContext()) { setBalloonSettings(view, viewLifecycleOwner, requireContext().getString(R.string.density_balloon), false) }
                view.showAlignLeft(densityBalloon)
            }
        }
    }
}
