/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galgeleg;


import galgeleg.GalgeI;
import java.net.MalformedURLException;
import java.net.URL;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

/**
 * REST Web Service
 *
 * @author Christian Thuren Jensen
 */
@Path("play")
public class GenericResource {


    @Context
    private UriInfo context;

    
    /**
     * Creates a new instance of GenericResource
     */
    public GenericResource() throws Exception {

    }

    /**
     * Retrieves representation of an instance of hangman.GenericResource
     * @return an instance of java.lang.String
     */
    
    @GET
    @Produces(MediaType.APPLICATION_XML)
    public String getXml() {
        return "<bank navn='merkur'><kunder><kunde navn='Jacob' kredit='1000'/></kunder></bank>";
    }

    @Path("tekst")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getTekst() {
        System.out.println("getTekst() blev kaldt fra " + context.getRequestUri());
        return "plain text";
    }

    @Path("json/{userid}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson(@PathParam("userid") String userid, @QueryParam("letter") String letter) throws MalformedURLException {
        
        // Connect to Java server via SOAP
        URL url = new URL("http://ubuntu4.saluton.dk:9924/galgeleg?wsdl");
        QName qname = new QName("http://galgeleg/", "GalgelogikService");
        Service service = Service.create(url, qname);
        GalgeI spil = service.getPort(GalgeI.class);

        // Logic
        spil.gætBogstav(letter);
        
        // Method call
        System.out.println("getJson() blev kaldt fra " + context.getRequestUri());
        
        // JSON File:
        return "{ \n " + 
                "\"userid\" : \"" + userid + "\", \n " + 
                "\"name\" : \"" + "temp placeholder" + "\", \n " + 
                "\"invisibleword\" : " + spil.getSynligtOrd() + " \n " + 
                "\"usedletters\" : " + spil.getBrugteBogstaver() + " \n " + 
                //"\"word\" : " + spil.getOrdet() + " \n " + 
                "\"guessedletter\" : " + letter + " \n " + 
                "\"item\" : " + "placeholder" + " \n " + 
                "}";
        
    }

    /**
     * PUT method for updating or creating an instance of GenericResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_XML)
    public void putXml(String content) {
    }
}
