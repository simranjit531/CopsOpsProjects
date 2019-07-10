package copops.com.copopsApp.mapnavigation;





import java.util.List;

public interface Parser {
    List<Route> parse() throws RouteException;
}