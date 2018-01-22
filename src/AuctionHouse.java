import net.jini.core.lease.Lease;
import net.jini.space.JavaSpace;
import net.jini.core.transaction.server.TransactionManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.rmi.RMISecurityManager;
import java.util.ArrayList;
import net.jini.core.lease.Lease;


import net.jini.core.discovery.LookupLocator;
import net.jini.core.lookup.ServiceRegistrar;
import net.jini.core.lookup.ServiceTemplate;
import net.jini.core.lookup.ServiceMatches;


import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AuctionHouse extends JFrame{

    private final ArrayList<auctionObject> lots = new ArrayList<auctionObject>();

    private final JavaSpace space;

    public static void main(String[] args){

        String id = JOptionPane.showInputDialog(null,"Enter your ID: ", "Enter ID here");
        if(id == null){
            System.err.println("Invalid ID");
            System.exit(1);
        }

        Users.setUser(id);
        new AuctionHouse();

    }

    public AuctionHouse(){

        String hostname = JOptionPane.showInputDialog(null, "Enter the space you'd like to connect to, otherwise leave as localhost: ", "localhost");
        space = SpaceUtils.getSpace(hostname);
        if (space == null){
            System.err.println("Failed to find the javaspace");
            System.exit(1);
        }

        /*
            Initialise a Counter is in the space if one does not exist
            This is for itemIDs to be incremented correctly so each lot has its own unique ID
         */
        try{
            Object counter = space.read(new SpaceCounter(), null, 1000);
            if(counter == null){
                space.write(new SpaceCounter(0), null, Lease.FOREVER);
            }
        } catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }


        setTitle (iVariables.WINDOW_NAME + " - Welcome, User " + Users.getUser());

        //Check for the window closing and exit the process.
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent evt) {
                System.exit(0);
            }
        });

        Container cp = getContentPane();
        cp.setLayout (new BorderLayout ());

        JPanel panel = new JPanel(new CardLayout());

        final UI ui = new UI(lots, panel);

        panel.add(ui);
        cp.add(panel);
        pack();
        setVisible(true);


        //Look for any lots already in the Space and add them to the table
        new Thread(() -> {
            DefaultTableModel model = ui.getModel();
            try{
                SpaceCounter counter = (SpaceCounter) space.read(new SpaceCounter(), null, iVariables.STANDARD_TIMEOUT);
                for(int i = 0; i <= counter.getNumber(); i++){
                    auctionObject objTemp = new auctionObject(i+1,null,null,null,null,false, null);

                    auctionObject aLot = (auctionObject) space.readIfExists(objTemp, null, 1000);

                    if(aLot != null){
                        lots.add(aLot);
                        model.addRow(aLot.ArrayTransform());
                    }

                }
            } catch(Exception e){
                e.printStackTrace();
            }
        }).start();






    }


}