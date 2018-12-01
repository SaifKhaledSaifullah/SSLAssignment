package Model;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.saif.sslassignment.MainActivity;

import Presentar.CommunicationPresnter;
import View.NetworkComView;

public class CommunicationPresenterImpl implements CommunicationPresnter {
    private NetworkComView networkComView;

    public CommunicationPresenterImpl (NetworkComView networkComView) {
        this.networkComView = networkComView;
    }

    @Override
    public void signIn(String userEmail, String passWord) {
        if(!userEmail.equals("")&& !passWord.equals(""))
        {
            if(isEmailValid(userEmail))
            {
              networkComView.emailValidationSusscess();

            }
            else{
                networkComView.emailValidationError();
            }
        }else{
            networkComView.dataValidationError();

        }

    }

    @Override
    public void signUp(String userName, String userEmail, String passWord, String dob) {

        if (!userName.equals("") && !userEmail.equals("") && !passWord.equals("") && !dob.equals("")) {
            if (isEmailValid(userEmail)) {
                networkComView.emailValidationSusscess();
            } else {
                networkComView.emailValidationError();
            }
        } else {
            networkComView.dataValidationError();
        }

    }
    private boolean isEmailValid(String email) {

        return (email.contains("@")&&email.contains(".com"));
    }
}
