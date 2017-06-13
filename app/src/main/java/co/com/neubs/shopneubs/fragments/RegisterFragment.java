package co.com.neubs.shopneubs.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.classes.APIValidations;
import co.com.neubs.shopneubs.classes.models.APISincronizacion;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RegisterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegisterFragment extends Fragment {

    private EditText txtUsername, txtEmail,txtPassword,txtPasword1;
    private Button btnRegister;

    private View mProgressView;
    private View mRegisterFormView;

    public RegisterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RegisterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RegisterFragment newInstance() {
        RegisterFragment fragment = new RegisterFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_register, container, false);

        txtUsername = (EditText) view.findViewById(R.id.txt_username_register);
        txtEmail = (EditText) view.findViewById(R.id.txt_email_register);
        txtPassword = (EditText) view.findViewById(R.id.txt_password_register);
        txtPasword1 = (EditText) view.findViewById(R.id.txt_password1_register);
        btnRegister = (Button) view.findViewById(R.id.btn_register);

        mRegisterFormView = view.findViewById(R.id.register_fragment_form);
        mProgressView = view.findViewById(R.id.register_fragment_progress);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrar();
            }
        });
        return view;
    }

    private void registrar() {
        // Reset errors.
        txtUsername.setError(null);
        txtEmail.setError(null);
        txtPassword.setError(null);
        txtPasword1.setError(null);

        // Store values at the time of the login attempt.
        String username = txtUsername.getText().toString();
        String email = txtEmail.getText().toString();
        String password = txtPassword.getText().toString();
        String password1 = txtPasword1.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (TextUtils.isEmpty(username)) {
            txtUsername.setError(getString(R.string.error_field_required));
            focusView = txtUsername;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            txtEmail.setError(getString(R.string.error_field_required));
            focusView = txtEmail;
            cancel = true;
        }
        else if (!isEmailValid(email)) {
            txtEmail.setError(getString(R.string.error_invalid_email));
            focusView = txtEmail;
            cancel = true;
        }
        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            txtPassword.setError(getString(R.string.error_invalid_password));
            focusView = txtPassword;
            cancel = true;
        }

        if (!TextUtils.isEmpty(password1) && !isPasswordValid(password1)) {
            txtPasword1.setError(getString(R.string.error_invalid_password));
            focusView = txtPasword1;
            cancel = true;
        }

        if (!password.equals(password1)) {
            txtPasword1.setError(getString(R.string.error_invalid_password1));
            focusView = txtPasword1;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            Map<String,String> params = new HashMap<String, String>();
            params.put("username",username);
            params.put("email",email);
            params.put("password",password);

            /*TaskRegistro taskRegistro =new TaskRegistro();
            taskRegistro.execute(params);*/

            APIRest.Async.post(APIRest.URL_REGISTER, params, new IServerCallback() {
                @Override
                public void onSuccess(String json) {
                    showProgress(false);
                    Toast.makeText(getActivity(),getString(R.string.created_register),Toast.LENGTH_LONG).show();
                    getActivity().finish();
                }

                @Override
                public void onError(String message_error, APIValidations apiValidations) {
                    showProgress(false);
                    if (APIRest.Async.badRequest()){
                        // Si no hay errores de campos, se visualiza el error de credenciales
                        if (apiValidations.isNoFieldError())
                            Toast.makeText(getActivity(),apiValidations.getValidationNonFieldError(),Toast.LENGTH_LONG).show();
                        else {
                            if (apiValidations.isEmail()) {
                                txtEmail.setError(apiValidations.getValidationEmail());
                                txtEmail.requestFocus();
                            }
                            if (apiValidations.isUsername()) {
                                txtUsername.setError(apiValidations.getValidationUsername());
                                txtUsername.requestFocus();
                            }
                        }
                    }
                    else
                        Toast.makeText(getActivity(),getString(R.string.error_default),Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private boolean isPasswordValid(String password) {
        return password.length() > 7;
    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mRegisterFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mRegisterFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
