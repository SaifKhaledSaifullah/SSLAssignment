package Fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import com.saif.sslassignment.MainActivity;
import com.saif.sslassignment.R;

import java.util.ArrayList;

import Adapter.MyRecyclerViewAdapter;
import Calculation.ResultOutput;
import Dialogs.CoreDialog;
import Model.AdData;
import Preference.SSLSharedPreferences;
import Utils.FragmentUtilities;

public class FragmentAdData extends Fragment implements View.OnClickListener, MyRecyclerViewAdapter.ItemClickListener {
    private View view;
    private RecyclerView recyclerView;
    private MyRecyclerViewAdapter adapter;
    private TextView txtEmail;
    private EditText etAdLength;
    private EditText etTimeSlot;
    private EditText etAdNumber;
    private Button btnLogOut;
    private Button btnAdd;
    private Button btnShowResult;
    private int adCounter;

    private ArrayList<AdData> adList;
    private ArrayList<Integer> adDurations;
    private ArrayList<String> adNames;
    private SSLSharedPreferences sharedPreference;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ad_data, container, false);
        sharedPreference = SSLSharedPreferences.getSharedPreferences(getActivity());
        adCounter = 1;
        // Initializing Views and Set Click listeners
        initializeViewsAndSetClickListners(view);

        // initializing lists
        adList = new ArrayList<AdData>();
        adDurations = new ArrayList<Integer>();
        adNames = new ArrayList<String>();

        // set up the RecyclerView
        setUpRecycleView();

        return view;
    }

    private void setUpRecycleView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new MyRecyclerViewAdapter(getActivity(), adList);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                layoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);
    }

    private void initializeViewsAndSetClickListners(View view) {
        recyclerView = view.findViewById(R.id.rvAd);

        txtEmail = view.findViewById(R.id.txtEmail);
        txtEmail.setText(sharedPreference.getUserEmail());

        etAdLength = view.findViewById(R.id.etAdLength);
        etTimeSlot = view.findViewById(R.id.etTimeSlot);
        etAdNumber = view.findViewById(R.id.etAdNumber);

        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnShowResult = view.findViewById(R.id.btnShowResult);

        btnLogOut.setOnClickListener(this);
        btnAdd.setOnClickListener(this);
        btnShowResult.setOnClickListener(this);


    }

    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(getActivity(), "You clicked " + adapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnLogOut:
                sharedPreference.setLoginState(false);
                sharedPreference.setUserEmail("");
                FragmentLogin fragmentLogin = new FragmentLogin();
                new FragmentUtilities(getActivity())
                        .replaceFragmentWithoutBackTrace(R.id.container, fragmentLogin);

                break;
            case R.id.btnAdd:
                if (!etAdLength.getText().toString().equals("")) {
                    Log.e("SSS", "Ad length: " + etAdLength.getText().toString());
                    if (adCounter < 11) {
                        adList.add(new AdData("Ad " + adCounter, Integer.parseInt(etAdLength.getText().toString())));
                        adDurations.add(Integer.parseInt(etAdLength.getText().toString()));
                        adNames.add("Ad " + adCounter);

                        adapter.notifyDataSetChanged();
                        recyclerView.scrollToPosition(adapter.getItemCount() - 1);
                        adCounter++;
                    } else {
                        btnAdd.setClickable(false);
                    }
                } else {
                    etAdLength.requestFocus();
                    Snackbar.make(MainActivity.viewContainer,
                            "Enter Ad length",
                            Snackbar.LENGTH_SHORT).show();
                }

                break;
            case R.id.btnShowResult:
                String resultSring = "";

                String timeSlot = etTimeSlot.getText().toString();
                String noOfAds = etAdNumber.getText().toString();
                if (!timeSlot.equals("") && !noOfAds.equals("")) {
                    if (noOfAds.equals("0")) {
                        etAdNumber.requestFocus();
                        Snackbar.make(MainActivity.viewContainer,
                                "Minimum 1 Ad should be shown",
                                Snackbar.LENGTH_SHORT).show();
                    } else {
                        ResultOutput results = new ResultOutput();
                        ArrayList<int[]> output = results.printCombination(adDurations, adDurations.size(), Integer.parseInt(noOfAds), Integer.parseInt(timeSlot));
                        if (output.size() < 1) {
                            new CoreDialog(getActivity(), "No Ad is found to be shown within given time slot");
                        } else {
                            for (int i = 0; i < output.size(); i++) {
                                String s = "";
                                for (int j = 0; j < output.get(i).length; j++) {
                                    s = s + adNames.get(adDurations.indexOf(output.get(i)[j])) + " ";
                                }
                                resultSring = resultSring + s + "\n";
                                Log.e("ResultOutput:", s);
                            }
                            new CoreDialog(getActivity(), resultSring);
                        }


                    }

                } else {
                    Snackbar.make(MainActivity.viewContainer,
                            "Enter Time slot and Number of Ads",
                            Snackbar.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
