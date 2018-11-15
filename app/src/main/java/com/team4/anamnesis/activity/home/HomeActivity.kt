package com.team4.anamnesis.activity.home

import android.content.Intent
import android.opengl.Visibility
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.team4.anamnesis.R
import com.team4.anamnesis.activity.SettingsActivity
import com.team4.anamnesis.activity.group.GroupActivity
import com.team4.anamnesis.component.TextInputDialog
import com.team4.anamnesis.component.TextInputDialogCompletedListener
import com.team4.anamnesis.component.TextInputDialogValidationListener
import com.team4.anamnesis.db.AppDatabase
import com.team4.anamnesis.db.entity.Group
import org.jetbrains.anko.doAsync
import android.preference.PreferenceActivity



class HomeActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private val adapter: HomeGroupAdapter = HomeGroupAdapter(this)
    private lateinit var drawer: DrawerLayout
    private lateinit var emptyTitle: TextView
    private lateinit var emptySubtitle: TextView
    private val manager: GridLayoutManager = GridLayoutManager(this, 2)
    private lateinit var model: HomeModel
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // instantiate AppDatabase
        AppDatabase.init(applicationContext)

        // instantiate ViewModel
        model = ViewModelProviders.of(this).get(HomeModel::class.java)

        // instantiate navigation view
        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.menu.getItem(0).isCheckable = false
        drawer = findViewById(R.id.drawer_layout)

        // instantiate create group button
        val fab = findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener { view -> // button was clicked
            onCreateGroupClicked(view)
        }

        // instantiate drawer layout
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close)
        drawer.addDrawerListener(toggle)
        toggle.syncState()
        navigationView.setNavigationItemSelectedListener(this)

        // instantiate recyclerview
        recyclerView = findViewById(R.id.home_recyclerview)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = manager
        emptyTitle = findViewById(R.id.home_empty_title)
        emptySubtitle = findViewById(R.id.home_empty_subtitle)

        // listen for group card click
        adapter.onGroupClicked = {
            onCardClicked(it)
        }

        // listen for group card delete button click
        adapter.onGroupDeleteClicked = {
            onCardDeleteClicked(it)
        }

        // listen for group card rename button click
        adapter.onGroupRenameClicked = {
            onCardRenameClicked(it)
        }

        // listen for changes to groups
        model.groups.observe(this, Observer {
            adapter.setData(it)

            // show/hide empty text if empty
            if (it.isEmpty()) {
                emptyTitle.visibility = View.VISIBLE
                emptySubtitle.visibility = View.VISIBLE
                recyclerView.visibility = View.GONE
            } else {
                emptyTitle.visibility = View.GONE
                emptySubtitle.visibility = View.GONE
                recyclerView.visibility = View.VISIBLE
            }

        })

    }

    override fun onBackPressed() {
        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    fun onCardClicked(group: Group) {
        val intent = Intent(this, GroupActivity::class.java)
        intent.putExtra("group", group)
        startActivity(intent)
    }

    fun onCardDeleteClicked(group: Group) {

        // delete the group
        doAsync {
            model.deleteGroup(group)
        }

        // show a snackbar about the group being deleted
        val snackbarText = String.format(resources.getString(R.string.home_snackbar_deleted_group), group.name)
        Snackbar.make(recyclerView, snackbarText, Snackbar.LENGTH_LONG)
                .setAction(R.string.home_snackbar_deleted_group_action) {
                    doAsync {
                        model.createGroup(group) // user clicked undo button, recreate the group
                    }
                }
                .show()

    }

    fun onCardRenameClicked(group: Group) {
        val dialog = TextInputDialog(this, R.string.home_dialog_rename_group_title,
                R.string.home_dialog_new_group_hint)

        // validate text input when the user taps "Ok"
        dialog.onValidateListener = object : TextInputDialogValidationListener() {
            override fun onValidate(text: String): String? {
                return if (text.isNotEmpty())
                    null else "Type a name"
            }
        }

        // listen for completion
        dialog.onCompletedListener = object : TextInputDialogCompletedListener() {
            override fun onComplete(text: String) { // we now have the new name of the group

                // edit the group
                val oldName: String = group.name
                group.name = text
                doAsync {
                    model.updateGroup(group)
                }

                // display a snackbar affirming the new deck was created
                val snackbarText = String.format(resources.getString(R.string.home_snackbar_renamed_group), oldName,
                        text)
                Snackbar.make(recyclerView, snackbarText, Snackbar.LENGTH_LONG).show()

            }
        }

        // present the dialog to the user
        dialog.show()
    }

    fun onCreateGroupClicked(view: View) {

        // prompt the user to name the new group
        val dialog = TextInputDialog(this@HomeActivity, R.string.home_dialog_new_group_title,
                R.string.home_dialog_new_group_hint)

        // validate text input when the user taps "Ok"
        dialog.onValidateListener = object : TextInputDialogValidationListener() {
            override fun onValidate(text: String): String? {
                return if (text.isNotEmpty())
                    null else "Type a name"
            }
        }

        // listen for completion
        dialog.onCompletedListener = object : TextInputDialogCompletedListener() {
            override fun onComplete(text: String) { // we now have the name of the group

                // create a new group
                val group = Group(name = text)
                doAsync {
                    model.createGroup(group)
                }

                // display a snackbar affirming the new deck was created
                val snackbarText = String.format(resources.getString(R.string.home_snackbar_new_group), text)
                Snackbar.make(view, snackbarText, Snackbar.LENGTH_LONG).setAction("Action", null).show()

            }
        }

        // show the dialog
        dialog.show()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    fun onDrawerSettingsClick(menuItem: MenuItem) {
        val intent = Intent(this, com.team4.anamnesis.SettingsActivity::class.java)
        intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, com.team4.anamnesis.SettingsActivity.GeneralPreferenceFragment::class.java.name)
        intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true)
        startActivityFromHome(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId

        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        val id = item.itemId

        if (id == R.id.nav_manage) {
        }

        val drawer = findViewById<DrawerLayout>(R.id.drawer_layout)
        drawer.closeDrawer(GravityCompat.START)
        return true
    }

    /**
     * Starts a new activity, ensuring the drawer has been properly closed.
     */
    fun startActivityFromHome(intent: Intent) {
        drawer.closeDrawers()
        startActivity(intent)
    }

}
