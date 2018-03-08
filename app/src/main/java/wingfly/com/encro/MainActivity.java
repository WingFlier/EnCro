package wingfly.com.encro;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import wingfly.com.encro.Settings.UserPreferences;
import wingfly.com.encro.databases.DbHandler;
import wingfly.com.encro.fragments.DecryptFragment;
import wingfly.com.encro.fragments.EncryptFragment;

public class MainActivity extends AppCompatActivity
{

    /* TODO future plans
    *  add user and generate a key for each of them (need some synchronization to add same key on the other device) maybe bluetooth?
    *  or give them opportunity to enter the key manually 16 length long
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
        DbHandler.newInstance(MainActivity.this);
    }
}