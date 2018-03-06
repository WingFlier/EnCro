package wingfly.com.encro;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.preference.ListPreference;

import com.takisoft.fix.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by Tiko on 3/6/2018.
 */

public class UserPreferences extends PreferenceFragmentCompat
{
    @Override
    public void onCreatePreferencesFix(@Nullable Bundle savedInstanceState, String rootKey)
    {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        ListPreference friendList = (ListPreference) findPreference("friendList");
        final String[] entries = {
                "hello", "h", "he", "hell"
        };
        friendList.setEntries(entries);
        friendList.setEntryValues(entries);
    }
}