package wingfly.com.encro.Settings;

import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.text.InputType;
import android.util.Log;
import android.view.View;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
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
                /*
                * TODO never let user have 2 identical keys
                */

                if (newValue == null || newValue.toString().trim().equals("")) return false;
                dialogMultiChoice(database, newValue);
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
                    Snackbar.make(getView(), "No friends", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return false;
                }
                friendList.setEntries(friendNames);
                friendList.setEntryValues(friendKeys);
                return true;
            }
        });

        final ListPreference friendListForShare = (ListPreference) findPreference("share");


        friendListForShare.setEntries(database.getFriendNames());
        friendListForShare.setEntryValues(database.getFriendKeys());

//        friendList.findIndexOfValue()

        friendListForShare.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener()
        {
            @Override
            public boolean onPreferenceClick(Preference preference)
            {
                String[] friendNames = database.getFriendNames();
                String[] friendKeys = database.getFriendKeys();
                if (friendNames.length < 1)
                {
                    Snackbar.make(getView(), "No friends", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                    return false;
                }
                friendListForShare.setEntries(friendNames);
                friendListForShare.setEntryValues(friendKeys);
                return true;
            }
        });


    }

    private void dialogMultiChoice(final Database database, final Object newValue)
    {
        final MaterialDialog.ListCallbackSingleChoice callbackSingleChoice = new MaterialDialog.ListCallbackSingleChoice()
        {
            @Override
            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text)
            {
                Log.e("logging_tag", text + " " + which);
                if (which == 0)
                {
                    manualChoose();
                } else
                {
                    //TODO so now here we paste shared encrypted  key
                    //TODO decrypt before using and ask for friend name
                    //TODO one more edittext dialog
                    ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                    String key = clipboard.getPrimaryClip()
                            .getItemAt(0).getText().toString().trim();

                    Friend friend = new Friend(String.valueOf(newValue),
                            key);
//                                add a friend to db
                    database.add(friend);
                }
                return true;
            }

            private void manualChoose()
            {
                final String[] text = new String[1];
                new MaterialDialog.Builder(getContext())
                        .title(R.string.input)
                        .inputRange(16, 16, Color.RED)
                        .content("Make sure to enter same key on both devices or share the key")
                        .neutralText("share key")
                        .inputType(InputType.TYPE_CLASS_TEXT)
                        .input("Key here", null, new MaterialDialog.InputCallback()
                        {
                            @Override
                            public void onInput(@NonNull MaterialDialog dialog, CharSequence input)
                            {
                                /* leave blank and validate in onClick function*/
                                // idk why forced to change to array
                                text[0] = String.valueOf(input);
                            }
                        })
                        .autoDismiss(false)
                        .alwaysCallInputCallback()
                        .onNeutral(new MaterialDialog.SingleButtonCallback()
                        {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                            {
                                if (text[0].length() == 16)
                                {
                                    shareText(Encryptor.encrypt(Constants.NDK_KEY, text[0]));
                                }
                            }
                        })
                        .onPositive(new MaterialDialog.SingleButtonCallback()
                        {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which)
                            {
                                Friend friend = new Friend(String.valueOf(newValue),
                                        Encryptor.encrypt(Constants.NDK_KEY, text[0]));
//                                add a friend to db
                                database.add(friend);
                                Snackbar.make(getView(), "New friend added", Snackbar.LENGTH_SHORT)
                                        .setAction("Action", null).show();
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        };
        new MaterialDialog.Builder(getContext())
                .title(R.string.dialog_title)
                .items(R.array.items)
                .cancelable(false)
                .itemsCallbackSingleChoice(-1, callbackSingleChoice)
                .positiveText(R.string.choose)
                .show();
    }

    public void shareText(String text)
    {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
        startActivity(Intent.createChooser(sharingIntent, "title"));
    }
}