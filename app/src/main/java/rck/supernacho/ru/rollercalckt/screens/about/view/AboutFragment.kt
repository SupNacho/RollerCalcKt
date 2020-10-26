package rck.supernacho.ru.rollercalckt.screens.about.view


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.net.toUri
import com.yandex.metrica.YandexMetrica
import kotlinx.android.synthetic.main.fragment_about.*
import rck.supernacho.ru.rollercalckt.R

class AboutFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_about, container, false)
        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        tv_about_policy.setOnClickListener {
            YandexMetrica.reportEvent("privacy policy", "clicked")
            startActivity(Intent(Intent.ACTION_VIEW, getString(R.string.privacy_policy_url).toUri()))
        }
    }
    }
