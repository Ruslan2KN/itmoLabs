import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import model.PointRequest;
import model.ServerResponse;

public class JsonService {
    private final Gson gson = new Gson();

    public PointRequest pointRequest(String json) throws JsonSyntaxException{
        return gson.fromJson(json, PointRequest.class);
    }

    public String toJson(ServerResponse response){
        return gson.toJson(response);
    }

}
