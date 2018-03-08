package wingfly.com.encro.Settings;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.widget.Toast;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

import wingfly.com.encro.Constants;
import wingfly.com.encro.POJO_s.Friend;
import wingfly.com.encro.R;
import wingfly.com.encro.databases.Database;
import wingfly.com.encro.databases.DbHandler;
import wingfly.com.encro.encryption.Encryptor;

public class UserPreferences extends PreferenceFragmentCompat
{
    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.preferences, rootKey);
        final Database database = DbHandler.newInstance(getActivity().getApplicationContext());

        EditTextPreference add = (EditTextPreference) findPreference("add_person");
        add.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener()
        {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue)
            {
                /*TODO yet we're just adding newValue to the db
                but we should ask whether synchronize via bluetooth or enter the encryption key manually
                * TODO never let user have 2 identical keys
                */

                if (newValue == null || newValue.toString().trim().equals("")) return false;
                Friend friend = new Friend(String.valueOf(newValue), Encryptor.encrypt(Constants.NDK_KEY, Constants.randomStr()));
                // add a friend to db
                database.add(friend);
                Toast.makeText(getActivity().getApplicationContext(), "new friend added", Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // need this clickListener for changing editText value to "" onClick
        add.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                ((EditTextPreference) preference).setText("");
                return true;
            }
        });

        final ListPreference friendList = (ListPreference) findPreference("friendList");


        friendList.setEntries(database.getFriendNames());
        friendList.setEntryValues(database.getFriendKeys());

//        friendList.findIndexOfValue()

        friendList.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                String[] friendNames = database.getFriendNames();
                String[] friendKeys = database.getFriendKeys();
                if (friendNames.length < 1)
                {
                    Toast.makeText(getActivity().getApplicationContext(), "No friends added", Toast.LENGTH_SHORT).show();
                    return false;
                }
                friendList.setEntries(friendNames);
                friendList.setEntryValues(friendKeys);
                return true;
            }
        });
    }
}