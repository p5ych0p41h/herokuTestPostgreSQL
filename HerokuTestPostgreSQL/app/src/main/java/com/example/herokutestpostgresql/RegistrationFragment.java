package com.example.herokutestpostgresql;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.herokutestpostgresql.entity.User;
import com.example.herokutestpostgresql.jdbc.UserJdbc;

import java.util.List;

public class RegistrationFragment extends Fragment {

    private EditText editTextEmailRegistration;
    private EditText editTextPasswordRegistration;
    private Button buttonRegistrationRegistration;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_registration, container, false);

        editTextEmailRegistration = view.findViewById(R.id.edit_text_email_registration);
        editTextPasswordRegistration = view.findViewById(R.id.edit_text_password_registration);
        buttonRegistrationRegistration = view.findViewById(R.id.button_registration_registration);
        UserJdbc userJdbc = new UserJdbc(getActivity().getBaseContext());

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editTextEmailRegistration.getText().toString();
                String password = editTextPasswordRegistration.getText().toString();

                userJdbc.insert(email, password);
                editTextEmailRegistration.setText("");
                editTextPasswordRegistration.setText("");
                Toast.makeText(getActivity().getBaseContext(), "reg done", Toast.LENGTH_SHORT);
            }
        };

        buttonRegistrationRegistration.setOnClickListener(listener);

        return view;
    }
}