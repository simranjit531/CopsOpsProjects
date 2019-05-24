package copops.com.copopsApp.services;

public class ApiUtils {

    private ApiUtils() {}



// TESTING URL(VELOCIS)
 // private static final String BASE_URL = "http://205.147.98.85/";
// PRODUCTION URL
   private static final String BASE_URL = "http://93.90.201.167/";
//Live Copops
 //   private static final String BASE_URL = "http://82.165.253.201/";


    public static Service getAPIService() {

        return WebServiceCall.getClient(BASE_URL).create(Service.class);
    }
}