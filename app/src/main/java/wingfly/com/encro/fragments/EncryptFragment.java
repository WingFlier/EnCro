package wingfly.com.encro.fragments;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import wingfly.com.encro.Constants;
import wingfly.com.encro.R;
import wingfly.com.encro.encryption.Encryptor;

public class EncryptFragment extends Fragment
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private AutoCompleteTextView textView;
    private Button button;

    public EncryptFragment()
    {
    }

    public static EncryptFragment newInstance(String param1, String param2)
    {
        EncryptFragment fragment = new EncryptFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_encrypt, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        textView = getView().findViewById(R.id.data_to_encr);
        button = getView().findViewById(R.id.btn_encr_copy);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                final Context context = getActivity().getApplicationContext();
                SharedPreferences defaultSharedPreferences =
                        PreferenceManager.getDefaultSharedPreferences(
                                context);


                String data = textView.getText().toString();
                if (data.equals("")) return;

                String friendList = defaultSharedPreferences.getString("friendList", null);
                if (friendList == null)
                {
                    Toast.makeText(context, "Choose a person from settings", Toast.LENGTH_SHORT).show();
                    return;
                }

                String encrypted = Encryptor.encrypt(friendList, data);

                // copy encrypted to clipboard
                ((ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE))
                        .setPrimaryClip(ClipData.newPlainText("desc", encrypted));
                Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();

                ((InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE))
                        .hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

    }
}