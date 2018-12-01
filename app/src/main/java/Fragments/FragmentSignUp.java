package Fragments;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.saif.sslassignment.MainActivity;
import com.saif.sslassignment.R;

import java.util.Calendar;

import Model.CommunicationPresenterImpl;
import Model.ServerResponse;
import Network.ApiInterface;
import Network.RetrofitApiClient;
import Presentar.CommunicationPresnter;
import Utils.FragmentUtilities;
import View.NetworkComView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FragmentSignUp extends Fragment implements View.OnClickListener, NetworkComView {
    private static final String TAG = FragmentSignUp.class.getSimpleName();
    private View view;

    private CommunicationPresnter communicationPresnter;

    // UI reference
    private ProgressBar signup_progress;
    private EditText etName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etDOB;
    private Button btnSignUp;

    // Network Call
    private ApiInterface apiInterface;

    private String userName;
    private String email;
    private String password;
    private String dob;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sign_up, container, false);
        //Create an instance of ApiInterface
        apiInterface = RetrofitApiClient.getClient().create(ApiInterface.class);
        communicationPresnter = new CommunicationPresenterImpl(FragmentSignUp.this);

        // Initializing Views
        initializingUIViews(view);
        return view;
    }

    /*
     * Initializing UI Views
     * @param view
     */
    private void initializingUIViews(View view) {
        signup_progress = view.findViewById(R.id.signup_progress);
        etName = view.findViewById(R.id.etName);
        etEmail = view.findViewById(R.id.etEmail);
        etPassword = view.findViewById(R.id.etPassword);
        etDOB = view.findViewById(R.id.etDOB);
        btnSignUp = view.findViewById(R.id.btnSignUp);

        etDOB.setOnClickListener(this);
        btnSignUp.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.etDOB:

                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                if (dayOfMonth < 10) {
                                    etDOB.setText(year + "/" + (monthOfYear + 1) + "/0" + dayOfMonth);
                                } else {
                                    etDOB.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                }


                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
                break;
            case R.id.btnSignUp:
                userName = etName.getText().toString();
                email = etEmail.getText().toString();
                password = etPassword.getText().toString();
                dob = etDOB.getText().toString();
                communicationPresnter.signUp(userName, email, password, dob);
                break;
        }

    }

    @Override
    public void emailValidationError() {
        etEmail.requestFocus();
        Snackbar.make(MainActivity.viewContainer,
                "Enter a valid Email",
                Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void emailValidationSusscess() {
        signup_progress.setVisibility(View.VISIBLE);
        attempSignUp(userName, email, password, dob);

    }

    @Override
    public void dataValidationError() {
        Snackbar.make(MainActivity.viewContainer,
                "Fill up all the fields",
                Snackbar.LENGTH_SHORT).show();

    }
     /*
     * Core method for Communicating with server for sign up
     * @param name: User Name
     * @param email: User Email
     * @param password: Password
     * @param dob: Date Of Birth
     */
    private void attempSignUp(String name, String email, String password, String dob) {
        Call<ServerResponse> call = apiInterface.getSignUpValidity(name, email, password, dob);

        call.enqueue(new Callback<ServerResponse>() {

            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {

                signup_progress.setVisibility(View.GONE);
                ServerResponse validity = response.body();
                if (validity.getCode().equals("200")) {
                    Toast.makeText(getActivity(), validity.getMessage().get(0), Toast.LENGTH_LONG).show();
                    FragmentLogin fragmentLogin = new FragmentLogin();
                    new FragmentUtilities(getActivity()).replaceFragmentWithoutBackTrace(R.id.container, fragmentLogin);
                } else {
                    Snackbar.make(MainActivity.viewContainer,
                            getActivity().getResources().getString(R.string.error_message),
                            Snackbar.LENGTH_SHORT).show();
                }
                Log.e(TAG, validity.getCode().toString());
                Log.e(TAG, validity.getMessage().toString());

            }

            @Override
            public void onFailure(Call call, Throwable t) {
                signup_progress.setVisibility(View.GONE);
                Snackbar.make(MainActivity.viewContainer,
                        getActivity().getResources().getString(R.string.no_internet_connection_message),
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}
