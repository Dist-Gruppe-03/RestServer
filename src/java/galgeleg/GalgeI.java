package galgeleg;

import java.util.ArrayList;
import javax.jws.WebMethod;
import javax.jws.WebService;

/**
 * REST Web Service
 *
 * @author Christian Thuren Jensen
 */

@WebService
public interface GalgeI extends java.rmi.Remote {

    @WebMethod public ArrayList<String> getBrugteBogstaver();

    @WebMethod public String getSynligtOrd();

    @WebMethod public String getOrdet();

    @WebMethod public int getAntalForkerteBogstaver();

    @WebMethod public boolean erSidsteBogstavKorrekt();

    @WebMethod public boolean erSpilletVundet();

    @WebMethod public boolean erSpilletTabt();

    @WebMethod public boolean erSpilletSlut();

    @WebMethod public void nulstil();

    @WebMethod public void opdaterSynligtOrd();

    @WebMethod public void gætBogstav(String bogstav);

    @WebMethod public void logStatus();
    
    @WebMethod boolean hentBruger(String brugernavn, String adgangskode);
}