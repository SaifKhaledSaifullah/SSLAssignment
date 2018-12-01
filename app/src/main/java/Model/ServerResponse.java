package Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ServerResponse {

    @SerializedName("code")
    String codeString;
    @SerializedName("message")
    List<String> messageList;

    public String getCode(){
        return codeString;
    }

    public List<String> getMessage() {
        return messageList;
    }
}
