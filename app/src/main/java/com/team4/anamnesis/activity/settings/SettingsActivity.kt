package com.team4.anamnesis.activity.settings

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Switch
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.team4.anamnesis.R
import com.team4.anamnesis.db.entity.Pref
import com.team4.anamnesis.db.viewModel.PrefModel
import org.jetbrains.anko.doAsync

const val PREF_ANIMATIONS_ENABLED: String = "animations_enabled"
const val PREF_FONT_SIZE: String = "font_size"

class SettingsActivity : AppCompatActivity() {

    private lateinit var animationToggle: Switch
    private lateinit var fontSize: LinearLayout
    private lateinit var fontSizeSubtitle: TextView

    private lateinit var model: PrefModel

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity__settings)
        actionBar?.setDisplayHomeAsUpEnabled(true)

        // instantiate model
        model = ViewModelProviders.of(this).get(PrefModel::class.java)

        // load views
        animationToggle = findViewById(R.id.settings__animations)
        fontSize = findViewById(R.id.settings__font_size)
        fontSizeSubtitle = findViewById(R.id.settings__font_size__subtitle)

        // observe font size
        fontSizeSubtitle.text = "12 pt" // set default size
        model[PREF_FONT_SIZE].observe(this, Observer {
            if (it.isNotEmpty()) {
                fontSizeSubtitle.text = it[0].value + " pt"
            }
        })

        // observe animation toggle
        animationToggle.isChecked = true // set default value
        model[PREF_ANIMATIONS_ENABLED].observe(this, Observer {
            if (it.isNotEmpty()) {
                animationToggle.isChecked = it[0].toBoolean()
            }
        })

        // listen for animation toggle changes
        animationToggle.setOnCheckedChangeListener { _, b ->
            onAnimationToggleChanged(b)
        }

        // listen for font size changes
        fontSize.setOnClickListener {
            onFontSizeClicked(it)
        }

    }

    private fun onAnimationToggleChanged(isChecked: Boolean) {
        val pref = Pref(name = PREF_ANIMATIONS_ENABLED, value = isChecked.toString())
        doAsync {
            model.set(pref)
        }
    }

    private fun onFontSizeClicked(view: View) {

        // get string array from resources
        val values: Array<String> = resources.getStringArray(R.array.pref__font_size__values)

        // create a new popup menu and populate it with font size values
        val popupMenu = PopupMenu(this, view)
        for (value in values) {
            val item: MenuItem = popupMenu.menu.add(value) // add a new menu item for this value

            item.setOnMenuItemClickListener {
                val itemValue: Int = it.title.toString().split(" ")[0].toInt() // font size pt value

                // change the font size
                val pref = Pref(name = PREF_FONT_SIZE, value = itemValue.toString())
                doAsync {
                    model.set(pref)
                }

                true
            }

        }

        // display the popup menu
        popupMenu.show()

    }

}
