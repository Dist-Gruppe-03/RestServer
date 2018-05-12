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
public interface GalgeI {

    @WebMethod public int getPersonalHighscore(String username);
    
    @WebMethod public String getName(String username);
    
    @WebMethod public ArrayList<String> getBrugteBogstaver(String bruger);

    @WebMethod public String getSynligtOrd(String bruger);

    @WebMethod public String getOrdet(String bruger);

    @WebMethod public int getAntalForkerteBogstaver(String bruger);

    @WebMethod public boolean erSidsteBogstavKorrekt(String bruger);

    @WebMethod public boolean erSpilletVundet(String bruger);

    @WebMethod public boolean erSpilletTabt(String bruger);

    @WebMethod public boolean erSpilletSlut(String bruger);

    @WebMethod public void nulstil(String bruger);

    @WebMethod public void opdaterSynligtOrd(String bruger);

    @WebMethod public void gætBogstav(String bogstav, String bruger);

    @WebMethod public void logStatus(String bruger);
    
    @WebMethod boolean hentBruger(String brugernavn, String adgangskode);

    @WebMethod public void highscoreCheck(String bruger, int score);
    
    @WebMethod public void hentOrdFraDr(String bruger);

}