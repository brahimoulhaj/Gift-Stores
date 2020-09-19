package com.gift.app.data.storages.remote;

import com.gift.app.data.models.AddFavouriteResponse;
import com.gift.app.data.models.AuthResponse;
import com.gift.app.data.models.ChatResponse;
import com.gift.app.data.models.DepartmentsResponse;
import com.gift.app.data.models.FavStoresResponse;
import com.gift.app.data.models.PostChatResponse;
import com.gift.app.data.models.ProductsResponse;
import com.gift.app.data.models.StoresResponse;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetrofitServices {

    @Multipart
    @POST(ApiUrls.Login)
    Observable<AuthResponse> postLogin(@Part("mobile") RequestBody mobile);


    @Multipart
    @POST(ApiUrls.Register)
    Observable<AuthResponse> postRegister(
            @Part("country_code") RequestBody country_code,
            @Part("name") RequestBody name,
            @Part("mobile") RequestBody mobile);


    @GET(ApiUrls.GetDepartments)
    Observable<DepartmentsResponse> getDepartments(
            @Query("uid") String uid,
            @Query("firebasetoken") String firebasetoken
    );

    @GET(ApiUrls.GetStores)
    Observable<StoresResponse> getStores(@Query("department_id") Integer department_id,
                                         @Query("uid") String uid);

    @GET(ApiUrls.GetProducts)
    Observable<ProductsResponse> getProducts(@Query("store_id") Integer store_id);

    @Multipart
    @POST(ApiUrls.AddFavourite)
    Observable<AddFavouriteResponse> postAddFav(@Part("store_id") Integer store_id,
                                                @Part("uid") RequestBody uid);

    @Multipart
    @POST(ApiUrls.DelFavourite)
    Observable<AddFavouriteResponse> postDeleteFav(
            @Part("store_id") Integer store_id,
            @Part("uid") RequestBody uid);


    @GET(ApiUrls.GetFavourites)
    Observable<FavStoresResponse> getFavStores(@Query("uid") String uid);


    @GET(ApiUrls.GetChat)
    Observable<ChatResponse> getChat(@Query("chat_id") String chat_id, @Query("mobile") String mobile);


    @Multipart
    @POST(ApiUrls.PostChat)
    Observable<PostChatResponse> postChat(
            @Part("mobile") RequestBody mobile,
            @Part("message") RequestBody message,
            @Part("photo") MultipartBody.Part photo
    );
}
