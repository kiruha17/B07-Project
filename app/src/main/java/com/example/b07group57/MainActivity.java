package com.example.b07group57;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

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

        if (savedInstanceState == null) {
            loadFragment(new LoginHomeFragment());
        }
    }

    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 1) {
            getSupportFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
