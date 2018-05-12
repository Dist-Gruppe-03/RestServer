/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package galgeleg;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.xml.namespace.QName;
import javax.xml.ws.Service;

public class Userbase {

    static HashMap<String, String> user = new HashMap<>();

    public Userbase() {

    }

    public boolean userAuthentification(String username, String password) throws MalformedURLException {
        URL url = new URL("http://localhost:9924/galgeleg?wsdl");
        QName qname = new QName("http://galgeleg/", "GalgelogikService");
        Service service = Service.create(url, qname);
        GalgeI spil = service.getPort(GalgeI.class);

        // Check if user exists: 
        if (spil.hentBruger(username, password)) {
            // Add user to webservers hashmap
            if (user.containsKey(username) == false) {
                // Give user unique ID, to prevent bruteforcing:
                user.put(username, hashGenerator());
                System.out.println("opretter ");
            }
            return true;
        }
        return false;
    }

    private String hashGenerator() {
        long id = System.currentTimeMillis();
        return "11223344";
    }

}
