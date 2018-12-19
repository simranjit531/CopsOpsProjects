package copops.com.copopsApp.services;



import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class WebServiceCall {

    public static  Retrofit retrofit;

    public static Retrofit getClient(String baseUrl) {

        retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

        return retrofit;
    }
}
