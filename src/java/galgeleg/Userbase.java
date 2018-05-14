package galgeleg;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.UUID;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceException;

/**
 * REST Web Service
 *
 * @author Christian Thuren Jensen
 */
public class Userbase {

    static HashMap<String, String> user = new HashMap<>();

    public boolean userAuthentification(String username, String password) throws MalformedURLException {
        try {
        URL url = new URL(Links.url);
        QName qname = new QName("http://galgeleg/", "GalgelogikService");
        Service service = Service.create(url, qname);
        GalgeI spil = service.getPort(GalgeI.class);

        // Check if user exists: 
        if (spil.hentBruger(username, password)) {
            // Add user to webservers hashmap
            if (!user.containsKey(username)) {
                // Give user unique ID, to prevent bruteforcing:
                String uniqueID = hashGenerator();
                user.put(username, uniqueID);
                System.out.println(username + " created with UUID: " + uniqueID);
            } else {
                System.out.println(username + " already exists with UUID: " + user.get(username));
            }
            if (spil.erSpilletSlut(username)) {
                spil.nulstil(username);
            }
            return true;
        }
        // If user does not exist
        System.out.println(username + " was not found.");
        
        } catch (WebServiceException e) {
            System.out.println("Could not connect to Java server. (" + e.getCause() + ")");
        }
        return false;
    }

    private String hashGenerator() {
        String uniqueID = UUID.randomUUID().toString();
        return uniqueID;
    }
}
