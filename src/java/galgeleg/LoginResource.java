package galgeleg;

import java.io.StringReader;
import java.net.MalformedURLException;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.json.JsonString;
import javax.json.JsonStructure;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;

/**
 * REST Web Service
 *
 * @author Christian Thuren Jensen
 */
@Path("login")
public class LoginResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of LoginResource
     */
    public LoginResource() {

    }

    /*
     * Login resource that consumes an JSON object with username and password.
     * If the user exists, it will return a JSON object with a link to the users active game.
     * If the user is not found, it will not return anything.
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String login(String JSONFile) throws MalformedURLException {

        // Create instance of Userbase, to Authentificate and create UUID
        Userbase userbase = new Userbase();
        
        //Split JSON file : 
        JsonReader reader = Json.createReader(new StringReader(JSONFile));
        JsonStructure jsonst = reader.read();
        JsonObject object = (JsonObject) jsonst;
        JsonString JSONusername = (JsonString) object.get("username");
        JsonString JSONpassword = (JsonString) object.get("password");
        // Convert username and password to String without quotation marks
        String username = JSONusername.toString().replaceAll("\"", "");
        String password = JSONpassword.toString().replaceAll("\"", "");

        if (userbase.userAuthentification(username, password)) {
            // Create dynamic link to game by using UriInfo
            String path = context.getAbsolutePath().toString();
            int index = path.lastIndexOf('/');
            String newpath = path.substring(0, index);
            // Add new path
            return "{ \"gamepath\" : \"" + newpath + "/play/" + Userbase.user.get(username) + "\" }";
        } else {
            return null;
        }
    }
}
