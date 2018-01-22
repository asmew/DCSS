import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;


/*
a class for the table holding the lots. creating an uneditable table was necessary so the values in the cells
of the table couldn't be altered.
*/


public class aTable extends JTable {

    public aTable(Object[][] data, Object[] columns){
        //Set the model
        setModel (new disableEdit(data, columns));
        init();
    }

    /*
    Initialise all of the properties of the table
     */
    private void init(){
        setRowSelectionAllowed(true);
        setDefaultRenderer(String.class, new DefaultTableCellRenderer() {{
            setHorizontalAlignment(JLabel.CENTER);
        }});
        JTableHeader header = getTableHeader();
        header.setReorderingAllowed(false);
        header.setResizingAllowed(false);


    }

    private class disableEdit extends DefaultTableModel {

        public disableEdit(Object[][] data, Object[] columns){
            super(data, columns);
        }
        //Set the cells as not editable
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }

    }





}
