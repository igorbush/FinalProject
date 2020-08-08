package com.example.finalproject.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.finalproject.R;
import com.example.finalproject.models.TaskItem;
import com.example.finalproject.fragments.AccountFragment;
import com.example.finalproject.fragments.AudioNotesFragment;
import com.example.finalproject.fragments.TasksFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Stack;


public class MainActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private BottomNavigationView bottomNavigationView;
    private Fragment accountFragment;
    private Fragment tasksFragment;
    private Fragment audioNotesFragment;
    private FrameLayout frameLayout;
    private String userId;
    public static Stack<TaskItem> taskStack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        taskStack = new Stack<>();
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(this, RegisterActivity.class));
        } else {
            userId = auth.getCurrentUser().getUid();
        }
        Bundle bundle = new Bundle();
        bundle.putString("userId", userId);
        accountFragment = new AccountFragment();
        accountFragment.setArguments(bundle);
        tasksFragment = new TasksFragment();
        tasksFragment.setArguments(bundle);
        audioNotesFragment = new AudioNotesFragment();
        frameLayout = findViewById(R.id.frame_container);
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.tasks_menu_button);
        updateFragment(tasksFragment);
        bottomNavigationView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.account_menu_button:
                                getSupportActionBar().setTitle(R.string.app_bar_profile);
                                updateFragment(accountFragment);
                                break;
                            case R.id.tasks_menu_button:
                                getSupportActionBar().setTitle(R.string.app_bar_tasks);
                                updateFragment(tasksFragment);
                                break;
                            case R.id.audio_notes_menu_button:
                                getSupportActionBar().setTitle(R.string.app_bar_audio_notes);
                                updateFragment(audioNotesFragment);
                                break;
                            case R.id.exit_menu_button:
                                getSupportActionBar().setTitle(R.string.app_bar_exit);
                                auth.signOut();
                                startActivity(new Intent(MainActivity.this, RegisterActivity.class));
                                break;
                        }
                        return true;
                    }
                });
    }

    private void updateFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_container, fragment).commit();
    }
}