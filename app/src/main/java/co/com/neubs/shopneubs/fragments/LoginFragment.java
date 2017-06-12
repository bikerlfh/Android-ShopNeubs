package co.com.neubs.shopneubs.fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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
import co.com.neubs.shopneubs.classes.SessionManager;
import co.com.neubs.shopneubs.classes.models.Usuario;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {


    private EditText txtUsername,txtPassword;
    private Button btnLogin;

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

        txtUsername = (EditText) view.findViewById(R.id.txt_username);
        txtPassword = (EditText) view.findViewById(R.id.txt_password);
        btnLogin = (Button) view.findViewById(R.id.btn_login);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
        return view;
    }

    private void attemptLogin() {
        // Reset errors.
        txtUsername.setError(null);
        txtPassword.setError(null);

        // Store values at the time of the login attempt.
        String username = txtUsername.getText().toString();
        String password = txtPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            txtPassword.setError(getString(R.string.error_invalid_password));
            focusView = txtPassword;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(username)) {
            txtUsername.setError(getString(R.string.error_field_required));
            focusView = txtUsername;
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

            TaskLogin taskLogin = new TaskLogin();
            taskLogin.execute(params1);

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


    private class TaskLogin extends AsyncTask<Map,Void,Boolean>{

        private String messageError;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgress(true);
        }

        @Override
        protected Boolean doInBackground(Map... params) {
            try {
                Map<String, String> parametros = params[0];
                String json = APIRest.Sync.post(APIRest.URL_LOGIN, parametros, null);
                if (APIRest.Sync.ok()) {
                    String token = (String)APIRest.getObjectFromJson(json,"key");
                    //String email = parametros.get("email");
                    String username = parametros.get("username");
                    Usuario usuario = new Usuario(getActivity());
                    // Si no existe el usuaro, se debe consultar y guardar
                    //if (!usuario.getByEmail(email) && !usuario.getByUserName(username)) {
                    if (!usuario.getByUserName(username)) {
                        // Se consulta el usuario;
                        String url = "usuario/"+ username+"/";
                        json = APIRest.Sync.get(url, null);
                        usuario = APIRest.serializeObjectFromJson(json, Usuario.class);
                        if (usuario != null) {
                            usuario.initDbManager(getActivity());
                            usuario.save();
                        }
                        else
                            return false;
                    }

                    // Se guarda la session
                    SessionManager session = SessionManager.getInstance();
                    if (session.createUserSession(getActivity(), usuario.getIdUsuario(), token)) {
                        return true;
                    }
                }
            }
            catch (Exception e){
                Log.d("TASKLOGIN",e.getMessage());
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            showProgress(false);
            if (result) {
                getActivity().finish();
            }
            else{
                if (APIRest.Sync.badRequest()){
                    txtPassword.setError(getString(R.string.error_incorrect_password));
                    txtPassword.requestFocus();
                }
                else
                    Toast.makeText(getActivity(),getString(R.string.error_connection_server),Toast.LENGTH_LONG).show();
            }
        }
    }
}