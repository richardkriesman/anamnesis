package com.team4.anamnesis;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.team4.anamnesis.components.TextInputDialog;
import com.team4.anamnesis.components.TextInputDialogCompletedListener;
import com.team4.anamnesis.components.TextInputDialogValidationListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // get activity context
        final Context context = this;

        // initialize navigation view
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.getMenu().getItem(0).setCheckable(false);

        // initialize create deck button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) { // button was clicked

                // prompt the user to name the new flashcard deck
                TextInputDialog dialog = new TextInputDialog(context, R.string.home_dialog_new_deck_title,
                        R.string.home_dialog_new_deck_hint);

                // validate text input when the user taps "Ok"
                dialog.setOnValidateListener(new TextInputDialogValidationListener() {
                    @Override
                    public String onValidate(@NotNull String text) {
                        return text.length() > 0 ? null : "Type a name";
                    }
                });

                // listen for completion
                dialog.setOnCompletedListener(new TextInputDialogCompletedListener() {
                    @Override
                    public void onComplete(@NotNull String name) { // we now have the name of the deck

                        // TODO: create a new flashcard deck using the data layer


                        // TODO: actually create a new UI item for the deck
                        Button button2 = findViewById(R.id.button2);
                        button2.setVisibility(View.VISIBLE);

                        // display a snackbar affirming the new deck was created
                        Resources res = getResources();
                        String snackbarText = String.format(res.getString(R.string.home_snackbar_new_deck), name);
                        Snackbar.make(view, snackbarText, Snackbar.LENGTH_LONG).setAction("Action",
                                null).show();

                    }
                });

                // show the dialog
                dialog.show();
            }
        });

        // initialize drawer layout
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_manage) {
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void launchSecondActivity(MenuItem item) {

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        Intent settings = new Intent(this, SettingsActivity.class);

        drawer.closeDrawers();

        startActivity(settings);
    }
}
