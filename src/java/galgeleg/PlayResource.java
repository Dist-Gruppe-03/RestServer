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
import javax.ws.rs.core.Response;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

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

    // Guess letter or reset game
    @Path("{uuid}")
    @POST
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_PLAIN)
    public Response updateGame(@PathParam("uuid") String uuid,
            @DefaultValue("") @FormParam("letter") String letter,
            @DefaultValue("") @FormParam("reset") String reset) throws Exception {

        String response = "";

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
                    //System.out.println("UUID: " + uuid + " belongs to " + username);
                }
            }
        }

        try {
            // Connect to Java server via SOAP
            URL url = new URL(Links.url);
            QName qname = new QName("http://galgeleg/", "GalgelogikService");
            Service service = Service.create(url, qname);
            GalgeI spil = service.getPort(GalgeI.class);

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
                                spil.highscoreCheck(username, spil.getAntalForkerteBogstaver(username));
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

        } catch (WebServiceException e) {
            System.out.println("Could not connect to Java server. (" + e.getCause() + ")");
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Could not connect to Java server. (" + e.getCause() + ")").build();
        }

        // Method call
        System.out.println("updateGame(" + reset + letter + ") called from " + context.getRequestUri());
        return Response.ok(response, MediaType.TEXT_PLAIN).build();
    }

    @Path("{uuid}")
    @GET
    public Response getGame(@DefaultValue("") @PathParam("uuid") String uuid) throws Exception {

        String gameJSON = "";

        // Get username from unique identifier
        String username = null;
        boolean isGameActive = false;

        // Method call
        System.out.println("getGame() called from " + context.getRequestUri());

        // Check if hashmap contains users, if indeed, then find the user
        if (!user.isEmpty()) {
            for (Map.Entry<String, String> entry : user.entrySet()) {
                if (uuid.equals(entry.getValue())) {
                    username = entry.getKey();
                    isGameActive = true;
                    // Write result in log
                    //System.out.println("UUID: " + uuid + " belongs to " + username);
                }
            }
        }

        try {
            // Connect to Java server via SOAP
            URL url = new URL(Links.url);
            QName qname = new QName("http://galgeleg/", "GalgelogikService");
            Service service = Service.create(url, qname);
            GalgeI spil = service.getPort(GalgeI.class);

            if (isGameActive) {
                // JSON File:
                gameJSON = "{ \n "
                        + "\"userid\" : \"" + username + "\", \n "
                        + "\"name\" : \"" + spil.getName(username) + "\", \n "
                        + "\"invisibleword\" : \"" + spil.getSynligtOrd(username) + "\", \n "
                        + "\"usedletters\" : \"" + spil.getBrugteBogstaver(username) + "\", \n "
                        + "\"gameover\" : \"" + spil.erSpilletSlut(username) + "\", \n "
                        + "\"wrongletters\" : \"" + spil.getAntalForkerteBogstaver(username) + "\" \n "
                        + "}";
                // Return JSON object with HTTP statuscode 200 OK
                return Response.ok(gameJSON, MediaType.APPLICATION_JSON).build();
            } else {
                // If user is not found - return HTTP statuscode 404 NOT FOUND
                return Response.status(Response.Status.NOT_FOUND).build();
            }
        } catch (WebServiceException e) {
            System.out.println("Could not connect to Java server. (" + e.getCause() + ")");
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Could not connect to Java server. (" + e.getCause() + ")").build();
        }
    }
}
