package com.example.b07group57;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* A simple way to write data to the database, you can figure out the rest, the URL is our database URL
        Make sure to look up how to structure a database (just ask GPT honestly)

        FirebaseDatabase db = FirebaseDatabase.getInstance("https://b07-group-57-default-rtdb.firebaseio.com/");
        DatabaseReference myRef = db.getReference("message");

        myRef.setValue("Hello!");
        */

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.tracker) {
                loadFragment(new CalendarFragment());
            } else if (item.getItemId() == R.id.gauge_nav) {
                loadFragment(new EcoGaugeFragment());
            } else if (item.getItemId() == R.id.hub_nav) {
                loadFragment(new EcoHubFragment());
            } else if (item.getItemId() == R.id.balance_nav) {
                loadFragment(new EcoBalanceFragment());
            } else if (item.getItemId() == R.id.agent_nav) {
                loadFragment(new ExampleFeatureFragment());
            }
            return true;
        });

        showNavigationBar(false);

        if (savedInstanceState == null) {
            loadFragment(new HomePageFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    public void showNavigationBar(boolean show) {
        bottomNavigationView.setVisibility(show ? View.VISIBLE : View.GONE);;
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }

    private boolean isLoginFragmentVisible() {
        return getSupportFragmentManager().findFragmentByTag("LoginFragment") != null;
    }
}
