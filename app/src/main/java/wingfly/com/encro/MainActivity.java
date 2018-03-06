package wingfly.com.encro;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
{

    /* TODO future plans
    *  add user and generate a key for each of them (need some synchronization to add same key on his device too)
    *  change user you are now talking (change key for encryption in the background)
    *  delete a user?
    *  database for storing users and their keys (encrypted of course)
    *  store key for user encryption in ndk
    * */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener()
    {

        public Fragment fragment;

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item)
        {
            switch (item.getItemId())
            {
                case R.id.encrypt:
                    fragment = EncryptFragment.newInstance(null, null);
//                TODO    SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                    break;
                case R.id.decrypt:
                    fragment = DecryptFragment.newInstance(null, null);
                    break;
                case R.id.settings:
                    fragment = new UserPreferences();
                    break;
            }
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_layout, fragment, null).commit();
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.encrypt);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportActionBar().hide();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.frame_layout, EncryptFragment.newInstance(null, null), null).commit();
    }
}