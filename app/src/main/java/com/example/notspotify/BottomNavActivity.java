package com.example.notspotify;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

/**
 * Bottom navigation activity
 */
public class BottomNavActivity extends AppCompatActivity {

    // Initialize variables
    private BottomNavigationView mMainNav;
    private FrameLayout mMainFrame;
    private BrowseFragment browseFragment;
    private LibraryFragment libraryFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_nav);

        // Bind variables
        mMainFrame = findViewById(R.id.main_frame);
        mMainNav = findViewById(R.id.main_nav);

        browseFragment = new BrowseFragment();
        libraryFragment = new LibraryFragment();
        profileFragment = new ProfileFragment();

        // Set start up fragment to browse fragment
        setFragment(browseFragment);

        // On click listener for navigation bar
        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch(menuItem.getItemId()) {
                    case R.id.navigation_browse:
                        setFragment(browseFragment);
                        return true;
                    case R.id.navigation_library:
                        setFragment(libraryFragment);
                        return true;
                    case R.id.navigation_profile:
                        setFragment(profileFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    /**
     * Set the fragment to whateve fragment is passed in
     * @param fragment The fragment to display
     */
    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}
