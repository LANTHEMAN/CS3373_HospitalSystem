package edu.wpi.cs3733d18.teamF.sr;

import edu.wpi.cs3733d18.teamF.api.sr.ServiceRequestSingleton;
import edu.wpi.cs3733d18.teamF.db.DatabaseHandler;
import edu.wpi.cs3733d18.teamF.db.DatabaseSingleton;

public class SynchronizationSingleton {
    DatabaseHandler dbHandler;

    private SynchronizationSingleton() {
        this.dbHandler = DatabaseSingleton.getInstance().getDbHandler();
    }

    public static SynchronizationSingleton getInstance() {
        return LazyInitializer.INSTANCE;
    }

    private static class LazyInitializer {
        static final SynchronizationSingleton INSTANCE = new SynchronizationSingleton();
    }


    public void removeUserPermissions(String username) {
        ServiceRequestSingleton.getInstance().removeUsernameLanguageInterpreter(username);
        ServiceRequestSingleton.getInstance().removeUsernameReligiousServices(username);
        ServiceRequestSingleton.getInstance().removeUsernameSecurityRequest(username);
    }

    public void addUserLanguagePermission(String username){
        ServiceRequestSingleton.getInstance().addUsernameLanguageInterpreter(username);
    }

    public void addUserReligiousPermission(String username){
        ServiceRequestSingleton.getInstance().addUsernameLanguageInterpreter(username);
    }

    public void addUserSecurityPermission(String username){
        ServiceRequestSingleton.getInstance().addUsernameLanguageInterpreter(username);
    }

    public void setCurrUserAPI(String username, String privilege){
    }

    public void syncAPIToLocal(){

    }

    public void syncLocalToAPI(){

    }
}
