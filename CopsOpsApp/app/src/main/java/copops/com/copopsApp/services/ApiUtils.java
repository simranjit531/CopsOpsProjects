package copops.com.copopsApp.services;

public class ApiUtils {

    private ApiUtils() {}

    private static final String BASE_URL = "http://appinn-project2.com/copops/public/";
//    private static final String BASE_URL = "http://13.233.74.84";


    public static Service getAPIService() {

        return WebServiceCall.getClient(BASE_URL).create(Service.class);
    }
}