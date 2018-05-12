/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galgeleg;

import java.net.MalformedURLException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
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

    /**
     * Retrieves representation of an instance of galgeleg.LoginResource
     *
     * @return an instance of java.lang.String
     */
    @GET
    @Path("test")
    @Produces(MediaType.TEXT_PLAIN)
    public String getTekst() {
        System.out.println("getTekst() blev kaldt fra " + context.getRequestUri());
        return "plain text " + Userbase.user;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    public String post(String JSONFILE) throws MalformedURLException {

        System.out.println(JSONFILE);
        
        
        //Split JSON file : 
        String username = "";
        String password = "";
        
        
        Userbase userbase = new Userbase();

        if (userbase.userAuthentification(username, password)) {
            // Create dynamic link to game by using UriInfo
            String path = context.getAbsolutePath().toString();
            int index = path.lastIndexOf('/');
            String newpath = path.substring(0, index);
            // Add new path
            return newpath + "/play/json/" + Userbase.user.get(username);
        } else {
            return null;
        }
    }
}
