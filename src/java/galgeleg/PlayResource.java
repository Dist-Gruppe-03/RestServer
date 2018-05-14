package galgeleg;

import static galgeleg.Userbase.user;
import java.net.URL;
import java.util.Map;
import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.FormParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * REST Web Service
 *
 * @author Christian Thuren Jensen
 */
@Path("play")
public class PlayResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of PlayResource
     */
    public PlayResource() {

    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getTekst() {
        System.out.println("getTekst() blev kaldt fra " + context.getRequestUri());
        return " " + user;
    }

    // Guess letter or reset game
    @Path("{uuid}")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    public String updateGame(@PathParam("uuid") String uuid,
            @DefaultValue("") @FormParam("letter") String letter,
            @DefaultValue("") @FormParam("reset") String reset) throws Exception {

        String response = "";

        // Connect to Java server via SOAP
        URL url = new URL(Links.url);
        QName qname = new QName("http://galgeleg/", "GalgelogikService");
        Service service = Service.create(url, qname);
        GalgeI spil = service.getPort(GalgeI.class);

        // Get username from unique identifier
        String username = null;
        boolean isGameActive = false;

        // Check if hashmap contains users, if indeed, then find the user
        if (!user.isEmpty()) {
            for (Map.Entry<String, String> entry : user.entrySet()) {
                if (uuid.equals(entry.getValue())) {
                    username = entry.getKey();
                    isGameActive = true;
                    // Write result in log
                    System.out.println("UUID: " + uuid + " belongs to " + username);
                }
            }
        } else {
            // Write result in log
            System.out.println("UUID: " + uuid + " does not belong to any user.");
        }

        // Logic
        if (isGameActive) {
            letter = letter.toLowerCase();
            if (letter.matches("[a-zæøåA-ZÆØÅ]") && letter.length() == 1) {
                if (spil.getBrugteBogstaver(username).contains(letter)) {
                    response = "Du har allerede gættet på: " + letter.toUpperCase();
                } else {
                    spil.gætBogstav(letter.toLowerCase(), username);
                    response = "Der gættes på: " + letter.toUpperCase();
                    if (spil.erSidsteBogstavKorrekt(username)) {

                        if (spil.erSpilletVundet(username) == true) {
                            response = "Du har vundet, ordet var: " + spil.getOrdet(username);
                        }
                    } else {

                        if (spil.erSpilletTabt(username) == true) {
                            response = "Du har tabt, ordet var: " + spil.getOrdet(username);
                        }
                    }
                }
            } else {
                response = "Ikke ét bogstav, prøv igen";
                // Prevent anything other than a letter to be guessed
                letter = "";
            }
            if (reset.equals("true")) {
                spil.nulstil(username);
                response = "";
            }
        }
        // Method call
        System.out.println("updateGame() called from " + context.getRequestUri());
        return response;
    }

    @Path("{uuid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("uuid") String uuid) throws Exception {

        // Connect to Java server via SOAP
        URL url = new URL(Links.url);
        QName qname = new QName("http://galgeleg/", "GalgelogikService");
        Service service = Service.create(url, qname);
        GalgeI spil = service.getPort(GalgeI.class);

        // Get username from unique identifier
        String username = null;
        boolean isGameActive = false;

        // Check if hashmap contains users, if indeed, then find the user
        if (!user.isEmpty()) {
            for (Map.Entry<String, String> entry : user.entrySet()) {
                if (uuid.equals(entry.getValue())) {
                    username = entry.getKey();
                    isGameActive = true;
                    // Write result in log
                    System.out.println("UUID: " + uuid + " belongs to " + username);
                }
            }
        } else {
            // Write result in log
            System.out.println("UUID: " + uuid + " does not belong to any user.");
        }

        // Method call
        System.out.println("JSON : Called from " + context.getRequestUri());

        if (isGameActive) {
            // JSON File:
            return "{ \n "
                    + "\"userid\" : \"" + username + "\", \n "
                    + "\"name\" : \"" + spil.getName(username) + "\", \n "
                    + "\"invisibleword\" : \"" + spil.getSynligtOrd(username) + "\", \n "
                    + "\"usedletters\" : \"" + spil.getBrugteBogstaver(username) + "\", \n "
                    + "\"gameover\" : \"" + spil.erSpilletSlut(username) + "\", \n "
                    + "\"wrongletters\" : \"" + spil.getAntalForkerteBogstaver(username) + "\" \n "
                    + "}";
        } else {
            // If user is not found
            return null;
        }
    }
}
