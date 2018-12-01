package Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.saif.sslassignment.MainActivity;
import com.saif.sslassignment.R;

import Model.ServerResponse;
import Network.ApiInterface;
import Network.RetrofitApiClient;
import Preference.SSLSharedPreferences;
import Utils.FragmentUtilities;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentLogin extends Fragment {
    private static final String TAG = FragmentLogin.class.getSimpleName();
    private SSLSharedPreferences sharedPreference;
    private View view;

    // UI references.
    private TextView tvSignUp;
    private EditText etEmail;
    private EditText etPassword;
    private Button btnSignIn;
    private ProgressBar login_progress;

    // Network Call
    private ApiInterface apiInterface;




    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_login,container,false);

        // Instance of Shared Preference
        sharedPreference=SSLSharedPreferences.getSharedPreferences(getActivity());

        //Create an instance of ApiInterface
        apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);

        // set Up UI references
        setupUI(view);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkInputValidity();
            }
        });
        tvSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               FragmentSignUp fragmentSignUp=new FragmentSignUp();
               new FragmentUtilities(getActivity()).replaceFragment(R.id.container, fragmentSignUp);
            }
        });
        return view;
    }

    private void checkInputValidity() {
        String email=etEmail.getText().toString();
        String passwrord=etPassword.getText().toString();

        if(!email.equals("")&& !passwrord.equals(""))
        {
            if(isEmailValid(email))
            {
                login_progress.setVisibility(View.VISIBLE);
                attempLogIn(email,passwrord);
            }
            else{
                etEmail.requestFocus();
                Snackbar.make(MainActivity.viewContainer,
                        "Enter a valid Email",
                        Snackbar.LENGTH_SHORT).show();
            }
        }else{
            Snackbar.make(MainActivity.viewContainer,
                    "Enter Email and Password",
                    Snackbar.LENGTH_SHORT).show();
        }
    }

    private void attempLogIn(String email, String password) {
        Call<ServerResponse> call = apiInterface.getUserValidity(email,password);

        call.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                login_progress.setVisibility(View.GONE);
                ServerResponse validity = response.body();
                if(validity.getCode().equals("200"))
                {
                    sharedPreference.setLoginState(true);
                    sharedPreference.setUserEmail(etEmail.getText().toString());
                    FragmentAdData fragmentAdData=new FragmentAdData();
                    new FragmentUtilities(getActivity()).replaceFragmentWithoutBackTrace(R.id.container, fragmentAdData);
                }
                else{
                    Snackbar.make(MainActivity.viewContainer,
                            getActivity().getResources().getString(R.string.error_message),
                            Snackbar.LENGTH_SHORT).show();
                }
                Log.e(TAG,validity.getCode().toString());
                Log.e(TAG,validity.getMessage().toString());

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                login_progress.setVisibility(View.GONE);
                Snackbar.make(MainActivity.viewContainer,
                        getActivity().getResources().getString(R.string.no_internet_connection_message),
                        Snackbar.LENGTH_SHORT).show();
            }
        });

    }

    private void setupUI(View view) {
        tvSignUp = view.findViewById(R.id.tvSignUp);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        btnSignIn=view.findViewById(R.id.btnSignIn);
        login_progress=view.findViewById(R.id.login_progress);
    }
    private boolean isEmailValid(String email) {

        return (email.contains("@")&&email.contains(".com"));
    }
}
