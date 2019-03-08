package copops.com.copopsApp.services;

public class ApiUtils {

    private ApiUtils() {}


   private static final String BASE_URL = "http://205.147.98.85/copops/public/";
  // private static final String BASE_URL = "http://93.90.201.167/";
 // private static final String BASE_URL = "http://appinn-project2.com/copops/public/";

    public static Service getAPIService() {

        return WebServiceCall.getClient(BASE_URL).create(Service.class);
    }
}