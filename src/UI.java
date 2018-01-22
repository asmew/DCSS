import net.jini.core.transaction.server.TransactionManager;
import net.jini.space.JavaSpace;
import net.jini.core.transaction.TransactionFactory;
import net.jini.core.transaction.Transaction;
import net.jini.core.lease.Lease;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.text.ParseException;

/*

The main UI class to display current auctions available

 */



public class UI extends JPanel {


    //Common variable instances stored privately
    private final JavaSpace space;
    private final TransactionManager tman;
    private final aTable table;
    private final ArrayList<auctionObject> lots;


    //A method to clear anything in the bid box of anything that isnt a number
    public static Number getAsNumber(JTextComponent in) {
        try {
            String input = in.getText().replaceAll("^[^0-9|\\.-]", "");
            return NumberFormat.getInstance().parse(input);
        } catch(ParseException e){
            return null;
        }
    }

    public DefaultTableModel getModel(){
        return((DefaultTableModel) table.getModel());
    }


    public UI(final ArrayList<auctionObject> lots, final JPanel panel){
        super(new BorderLayout());
        //Updating transaction manager, space and lots ArrayList to current
        this.tman = SpaceUtils.getManager();
        this.space = SpaceUtils.getSpace();
        this.lots = lots;

        JPanel fieldInputPanel = new JPanel(new GridLayout(6, 3));
        fieldInputPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));


        // Item name input
        final JTextField name = new JTextField("", 5);
        fieldInputPanel.add(new JLabel("Item Name: "));
        fieldInputPanel.add(name);

        // Description Input
        final JTextField description = new JTextField("", 5);
        fieldInputPanel.add(new JLabel("Description: "));
        fieldInputPanel.add(description);

        // Opening Bid Input
        final JTextField price = new JTextField("", 6);
        fieldInputPanel.add(new JLabel("Opening Bid: "));
        fieldInputPanel.add(price);

        final JCheckBox fixed = new JCheckBox();
        fieldInputPanel.add(new JLabel("Fixed Price?"));
        fieldInputPanel.add(fixed);

        //Creating a field for outputting information to the user
        final JTextField Output = new JTextField("", 6);
        fieldInputPanel.add(new JLabel("Output: "));
        Output.setEditable(false);
        fieldInputPanel.add(Output);

        final JTextField Sale = new JTextField("", 6);
        JPanel newPanel2 = new JPanel(new FlowLayout());
        newPanel2.add(new JLabel("Selected Lot: "));
        newPanel2.add(Sale);
        Sale.setEditable(false);
        JButton seeItem = new JButton();
        seeItem.setText("View and Bid");
        newPanel2.add(seeItem);
        add(newPanel2, BorderLayout.EAST);

        // Add the layout to the panel
        add(fieldInputPanel, BorderLayout.NORTH);

        table = new aTable(new String[0][5], new String[] {
                "Item ID", "Seller ID", "Name", "Current Bid", "Fixed Price", "Available"
        });

        JScrollPane lotList = new JScrollPane(
                table,
                JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER
        );
        add(lotList, BorderLayout.SOUTH);

        JButton addItem = new JButton();
        addItem.setText("Sell a new Item");

        JPanel newPanel = new JPanel(new FlowLayout());
        newPanel.add(addItem);
        add(newPanel, BorderLayout.WEST);

        addItem.addActionListener(evt -> {
            String Name = name.getText();
            String Description = description.getText();
            Number Price = getAsNumber(price);
            Double ActualPrice = Price == null ? 0: Price.doubleValue();
            Boolean FixedPrice = fixed.isSelected();

            //check the values inputted to see if they're valid
            if(Name.length() == 0){
                Output.setText("Please enter a product name");
                return;
            }
            if(Description.length() == 0){
                Output.setText("Please enter a Description for your item");
                return;
            }
            if(ActualPrice == 0){
                Output.setText("Please enter a valid price for your item");
                return;
            }

            Transaction transaction = null;

            try{
                //Create a transaction to handle the new auction being added
                Transaction.Created tmanager = TransactionFactory.create(tman,iVariables.STANDARD_TIMEOUT);
                transaction = tmanager.transaction;

                //Take the latest Counter down to get a new itemID
                SpaceCounter spaceCounter = (SpaceCounter) space.take(new SpaceCounter(), transaction, iVariables.STANDARD_TIMEOUT);


                //Create a new auction on the space based on the info added by the user
                auctionObject newAuction = new auctionObject(spaceCounter.iterate(), Users.getUser(), Name, Description, ActualPrice, false, FixedPrice);

                //Now the object is created, write it and the Counter back to the space to be used again
                space.write(newAuction, transaction, iVariables.AUCTION_TIMEOUT);
                space.write(spaceCounter, transaction, Lease.FOREVER);

                //Commit the Transaction
                transaction.commit();
                Output.setText("New Lot Added!");

                //Add the newly added object to your list of current lots
                lots.add(newAuction);
                getModel().addRow(newAuction.ArrayTransform());

                //Clear the values in the input boxes just to be clean
                price.setText("");
                description.setText("");
                name.setText("");

            } catch(Exception e){
                e.printStackTrace();
                //Abort transactions in progress if needed
                try {
                    if(transaction != null){
                        transaction.abort();
                    }
                } catch(Exception ex){
                    ex.printStackTrace();
                }
            }
        });

    }

}
