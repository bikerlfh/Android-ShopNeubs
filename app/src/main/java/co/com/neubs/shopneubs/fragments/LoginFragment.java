package co.com.neubs.shopneubs.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import co.com.neubs.shopneubs.R;
import co.com.neubs.shopneubs.classes.APIRest;
import co.com.neubs.shopneubs.interfaces.IServerCallback;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {


    private EditText mUsename,mPassword;
    private Button mBtnLogin;

    private View mProgressView;
    private View mLoginFormView;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        /*Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);*/
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_login, container, false);

        mLoginFormView = view.findViewById(R.id.login_fragment_form);
        mProgressView = view.findViewById(R.id.login_fragment_progress);

        mUsename = (EditText) view.findViewById(R.id.txt_username);
        mPassword = (EditText) view.findViewById(R.id.txt_password);
        mBtnLogin = (Button) view.findViewById(R.id.btn_login);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();

            }
        });
        return view;
    }

    private void attemptLogin() {

        mBtnLogin.setEnabled(false);
        // Reset errors.
        mUsename.setError(null);
        mPassword.setError(null);

        // Store values at the time of the login attempt.
        String username = mUsename.getText().toString();
        String password = mPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPassword.setError(getString(R.string.error_invalid_password));
            focusView = mPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            mUsename.setError(getString(R.string.error_field_required));
            focusView = mUsename;
            cancel = true;
        }
        /*else if (!isEmailValid(username)) {
            mUsernameView.setError(getString(R.string.error_invalid_email));
            focusView = mUsernameView;
            cancel = true;
        }*/

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        }
        else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);

            Map<String,String> params1 = new HashMap<String, String>();
            params1.put("username",username);
            params1.put("password",password);

            APIRest.Async.post("rest-auth/login/", params1, new IServerCallback() {
                @Override
                public void onSuccess(String json) {
                    try {
                        JSONObject jsonObject =new JSONObject(json);
                        String token = jsonObject.getString("token");


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(getActivity(),json,Toast.LENGTH_LONG).show();
                    showProgress(false);

                }

                @Override
                public void onError(String message_error, String response) {
                    showProgress(false);
                    if(APIRest.Async.badRequest()){
                        mPassword.setError(getString(R.string.error_incorrect_password));
                        mPassword.requestFocus();
                    }
                    else if(APIRest.Async.timeOut()){
                        Toast.makeText(getActivity(), getString(R.string.error_timeout_api), Toast.LENGTH_LONG).show();
                    }
                    else{
                        Toast.makeText(getActivity(), "ERRROR:" + message_error, Toast.LENGTH_LONG).show();
                    }
                    mBtnLogin.setEnabled(true);
                }
            });
        }
    }
    private boolean isPasswordValid(String password) {
        return password.length() > 7;
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

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
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
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }
}
