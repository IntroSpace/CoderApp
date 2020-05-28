package com.diamond.avenue;

import android.annotation.SuppressLint;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.diamond.avenue.ui.ScreenOne;
import com.diamond.avenue.ui.ScreenThree;
import com.diamond.avenue.ui.ScreenTwo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.view.menu.ActionMenuItemView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class LessonsActivity extends AppCompatActivity {

    private String[] mScreenTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;

    private ActionBarDrawerToggle mDrawerToggle;
    private CharSequence mDrawerTitle;
    private CharSequence mTitle;
    private int pos = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lessons);

        mTitle = mDrawerTitle = getTitle();
        mScreenTitles = getResources().getStringArray(R.array.menu);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for the list view
        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
                R.layout.drawer_list_item, mScreenTitles));
        // Set the list's click listener
        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

        mDrawerToggle = new ActionBarDrawerToggle(
                this, /* host Activity */
                mDrawerLayout, /* DrawerLayout object */
                new Toolbar(this), /* nav drawer icon to replace 'Up' caret */
                R.string.drawer_open, /* "open drawer" description */
                R.string.drawer_close /* "close drawer" description */
        ) {

            /** Called when a drawer has settled in a completely closed state. */
            @SuppressLint("RestrictedApi")
            public void onDrawerClosed(View view) {
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            @SuppressLint("RestrictedApi")
            public void onDrawerOpened(View drawerView) {
                supportInvalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        // Set the drawer toggle as the DrawerListener
        //mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

        // Initialize the first fragment when the application first loads.
        if (savedInstanceState == null) {
            selectItem(0);
        }
    }

    /* The click listener for ListView in the navigation drawer */
    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
            selectItem(i);
        }
    }

    /** Swaps fragments in the main content view */
    public void selectItem(int position) {
        // Update the main content by replacing fragments
        if (pos != position) {
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new ScreenOne();
                    pos = position;
                    break;
                case 1:
                    fragment = new ScreenTwo();
                    pos = position;
                    break;
                case 2:
                    fragment = new ScreenThree();
                    pos = position;
                    break;
                default:
                    break;
            }

            // Insert the fragment by replacing any existing fragment
            if (fragment != null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.content_frame, fragment).commit();

                // Highlight the selected item, update the title, and close the drawer
                mDrawerList.setItemChecked(position, true);
                setTitle(mScreenTitles[position]);
            } else {
                // Error
                Log.e(this.getClass().getName(), "Error. Fragment is not created");
            }
        }
        mDrawerLayout.closeDrawer(mDrawerList);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.open_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.open) {
            if (mDrawerLayout.isDrawerOpen(mDrawerList)) mDrawerLayout.closeDrawer(mDrawerList);
            else mDrawerLayout.openDrawer(mDrawerList);
        }

        return super.onOptionsItemSelected(item);
    }
}