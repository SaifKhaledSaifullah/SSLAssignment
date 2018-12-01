package Network;

import Model.ServerResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface ApiInterface {

    // For User Log In
    @FormUrlEncoded
    @POST("testapi/user_information.php")
    Call<ServerResponse> getUserValidity(@Field("email") String email, @Field("password") String password);

    // For Create User
    @FormUrlEncoded
    @POST("testapi/create_user.php")
    Call<ServerResponse> getSignUpValidity(@Field("name") String name, @Field("email") String email,
                                           @Field("password") String password, @Field("dob") String dob);
}
