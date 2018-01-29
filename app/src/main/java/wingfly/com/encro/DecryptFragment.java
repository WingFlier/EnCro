package wingfly.com.encro;


import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class DecryptFragment extends Fragment implements View.OnClickListener
{
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    AutoCompleteTextView decryptData;
    Button btnDecrypt;
    TextView txtPaste, txtResult, resultCopy;

    public DecryptFragment()
    {
    }

    public static DecryptFragment newInstance(String param1, String param2)
    {
        DecryptFragment fragment = new DecryptFragment();
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_decrypt, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        txtPaste = getView().findViewById(R.id.txtPaste);
        resultCopy = getView().findViewById(R.id.resultCopy);
        txtResult = getView().findViewById(R.id.txtResult);
        btnDecrypt = getView().findViewById(R.id.btnDecrypt);
        decryptData = getView().findViewById(R.id.decryptData);

        txtPaste.setOnClickListener(this);
        btnDecrypt.setOnClickListener(this);
        resultCopy.setOnClickListener(this);
    }

    @Override
    public void onClick(View view)
    {
        switch (view.getId())
        {
            case R.id.txtPaste:
                ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                decryptData.setText(clipboard.getPrimaryClip()
                        .getItemAt(0).getText().toString().trim());
                break;
            case R.id.btnDecrypt:
                String data = decryptData.getText().toString();
                txtResult.setText(Encryptor.decrypt(Constants.KEY, data));
                break;
            case R.id.resultCopy:
                // copy txtResult.getText().toString() to clipboard
                String text = txtResult.getText().toString();
                if (!text.equals(""))
                {
                    ((ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE))
                            .setPrimaryClip(ClipData.newPlainText("desc", text
                            ));
                    Toast.makeText(getContext(), "Copied", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
