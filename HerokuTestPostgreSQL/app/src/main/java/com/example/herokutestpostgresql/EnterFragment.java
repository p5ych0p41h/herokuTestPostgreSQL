package com.example.herokutestpostgresql;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.herokutestpostgresql.entity.User;
import com.example.herokutestpostgresql.jdbc.UserJdbc;

public class EnterFragment extends Fragment {

    private EditText editTextLoginEnter;
    private EditText editTextPasswordEnter;
    private Button buttonRegistrationEnter;
    private Button buttonEnterEnter;
    private CheckBox checkboxAutoLogin;
    private UserJdbc userJdbc;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_enter, container, false);

        editTextLoginEnter = view.findViewById(R.id.edit_text_login_enter);
        editTextPasswordEnter = view.findViewById(R.id.edit_text_password_enter);
        buttonRegistrationEnter = view.findViewById(R.id.button_registration_enter);
        buttonEnterEnter = view.findViewById(R.id.button_enter_enter);
        checkboxAutoLogin = view.findViewById(R.id.check_box_auto_login);
        userJdbc = new UserJdbc(getActivity().getBaseContext());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch(view.getId()){
                    case R.id.button_enter_enter:
                        String login = editTextLoginEnter.getText().toString();
                        String password = editTextPasswordEnter.getText().toString();
                        User user = userJdbc.search(login, password);
                        if(user == null)
                            Toast.makeText(getActivity().getBaseContext(),
                                    "not found",
                                    Toast.LENGTH_SHORT).show();
                        else
                            Toast.makeText(getActivity().getBaseContext(),
                                    user.getLogin() + ", " + user.getPassword() + " done",
                                    Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.button_registration_enter:
                        MainActivity.loadFragment(new RegistrationFragment());
                        break;
                }
            }
        };

        buttonRegistrationEnter.setOnClickListener(listener);
        buttonEnterEnter.setOnClickListener(listener);

        return view;
    }
}