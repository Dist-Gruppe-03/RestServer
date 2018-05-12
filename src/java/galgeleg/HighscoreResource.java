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
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

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

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getHighscore() throws MalformedURLException {

        URL url = new URL(Links.url);
        QName qname = new QName("http://galgeleg/", "GalgelogikService");
        Service service = Service.create(url, qname);
        GalgeI spil = service.getPort(GalgeI.class);

        System.out.println("Getting highscores - " + context.getRequestUri());

        String[][] test = spil.getHighscores();
        for (int i = 0; i < 10; i++) {
            System.out.println(test[0][i] + " " + test[1][i]);
        }

        String hs = "";
        for (int i = 0; i < 10; i++) {
            hs += test[0][i] + " " + test[1][i] + "\n";
        }
        return hs;
    }
}