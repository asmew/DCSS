import net.jini.space.JavaSpace;
import net.jini.core.transaction.server.TransactionManager;
import java.rmi.RMISecurityManager;
import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;

public final class SpaceUtils {

    private static JavaSpace space;
    private static TransactionManager tman;
    private static String hostname = "localhost";



    public static JavaSpace getSpace(String hostname) {

        //if we already have a space saved, reuse it
        if (space != null){
            return space;
        }

        JavaSpace js = null;
        try {
            LookupLocator l = new LookupLocator("jini://" + hostname);

            ServiceRegistrar sr = l.getRegistrar();

            Class c = Class.forName("net.jini.space.JavaSpace");
            Class[] classTemplate = {c};

            js = (JavaSpace) sr.lookup(new ServiceTemplate(null, classTemplate, null));

            if (js != null){
                space = js;
                System.out.println("Space Found at location " + hostname);
            }

        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
        return js;
    }

    public static JavaSpace getSpace() {
        return getSpace(hostname);
    }


    public static TransactionManager getManager(String hostname) {
        //if we have a transaction manager already, reuse it
        if(tman!=null){
            return tman;
        }

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new RMISecurityManager());
        }

        TransactionManager tm = null;
        try {
            LookupLocator l = new LookupLocator("jini://" + hostname);

            ServiceRegistrar sr = l.getRegistrar();

            Class c = Class.forName("net.jini.core.transaction.server.TransactionManager");
            Class[] classTemplate = {c};

            tm = (TransactionManager) sr.lookup(new ServiceTemplate(null, classTemplate, null));

            if(tm!=null){
                tman = tm;
                System.out.println("Transaction Manager found");
            }

        } catch (Exception e) {
            System.err.println("Error: " + e);
        }
        return tm;
    }

    public static TransactionManager getManager() {
        return getManager(hostname);
    }
}

