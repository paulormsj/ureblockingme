package apuri.com.br.ureblockingme.fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.regex.Pattern;

import apuri.com.br.ureblockingme.MainActivity;
import apuri.com.br.ureblockingme.R;
import apuri.com.br.ureblockingme.database.UserManager;
import apuri.com.br.ureblockingme.entities.User;

/**
 * Created by paulo.junior on 26/06/2016.
 */
public class UserLoginFragment extends Fragment {

    private TextView txtUserName;
    private TextView txtPassword;
    private Button btContinue;
    private TextView txtEmail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user_login,container,false);
        txtUserName = (TextView)view.findViewById(R.id.txtUserName);
        txtPassword = (TextView)view.findViewById(R.id.txtPassword);
        txtEmail = (TextView)view.findViewById(R.id.txtEmail);
        btContinue = (Button)view.findViewById(R.id.btContinue);
        setupListeners();
        return view;
    }

    private void setupListeners() {

        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                enableContinueIfItIsAllOk();
            }
        };
        txtUserName.addTextChangedListener(watcher);
        txtEmail.addTextChangedListener(watcher);
        txtPassword.addTextChangedListener(watcher);
        btContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Resources res = getResources();
                final ProgressDialog dialog = ProgressDialog.show(UserLoginFragment.this.getContext(),res.getString(R.string.txt_configuring),"",true,false);
                UserManager.getInstance().registerUser(txtUserName.getEditableText().toString(),
                        txtEmail.getEditableText().toString(), txtPassword.getEditableText().toString()
                        , new UserManager.IUserManagerCallback() {
                            @Override
                            public void onRegisterUser(boolean success, User user) {

                                dialog.dismiss();
                                if(success) {
                                    Intent i = new Intent(UserLoginFragment.this.getContext(), MainActivity.class);
                                    startActivity(i);
                                }else
                                    new AlertDialog.Builder(UserLoginFragment.this.getContext()).setTitle("Ops!").setMessage("Ocorreu um erro").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {
                                            dialogInterface.dismiss();
                                        }
                                    }).show();
                            }
                        });

            }
        });
    }

    private void enableContinueIfItIsAllOk() {
        Pattern matcher = Pattern.compile("[a-zA-Z]{3}-[0-9]{4}");
        if(txtUserName.length() > 0 && txtPassword.length() > 0 &&
                txtEmail.length() > 0)
            btContinue.setEnabled(true);
        else
            btContinue.setEnabled(false);
    }
}
