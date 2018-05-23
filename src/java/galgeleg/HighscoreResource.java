/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galgeleg;

import java.net.MalformedURLException;
import java.net.URL;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
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
@Path("highscore")
public class HighscoreResource {

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of HighscoreResource
     */
    public HighscoreResource() {

    }

    // Return highscore JSON object via GET HTTP Method
    @GET
    public Response getHighscore() throws MalformedURLException {
        String highscoreJSON = "";
        try {
            // Connect to gameserver via SOAP
            URL url = new URL(Links.url);
            QName qname = new QName("http://galgeleg/", "GalgelogikService");
            Service service = Service.create(url, qname);
            GalgeI spil = service.getPort(GalgeI.class);

            // 
            System.out.println("getHighscore called from " + context.getRequestUri());

            // Get data from multidimensional array into JSON object
            String[][] highscoreArray = spil.getHighscores();
            highscoreJSON += "{ \"highscores\" :\n";
            highscoreJSON += "  [ \n";
            for (int i = 0; i <= highscoreArray.length; i++) {
                highscoreJSON += "    { \"username\" : \"" + highscoreArray[0][i] + "\", \"score\" : \"" + highscoreArray[1][i] + "\" }";
                if (i == highscoreArray.length) {
                    highscoreJSON += "\n";
                } else {
                    highscoreJSON += ",\n";
                }
            }
            highscoreJSON += "  ]\n}";

        } catch (WebServiceException e) {
            System.out.println("Could not connect to Java server. (" + e.getCause() + ")");
            return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity("Could not connect to Java server. (" + e.getCause() + ")").build();
        }
            return Response.ok(highscoreJSON, MediaType.APPLICATION_JSON).build();
    }
}
