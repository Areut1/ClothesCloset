package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
//---------------------------------------------------------------
public class Inventory extends EntityBase implements IView {
    // Attributes \\
        // Private \\
    private static final String myTableName = "Inventory";

    protected Properties dependencies;
    private String updateStatusMessage = "";

    // Constructors \\
    //---------------------------------------------------------------
    public Inventory(String barcode) throws InvalidPrimaryKeyException {
        super(myTableName);

        setDependencies();

        String query = "SELECT * FROM " + myTableName + " WHERE (barcode = " + barcode + ")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null) {
            int size = allDataRetrieved.size();

            if (size != 1) {
                throw new InvalidPrimaryKeyException("Multiple inventories matching barcode : " + barcode + " found.");
            }  else  {
                Properties retrievedColorData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration<?> allKeys = retrievedColorData.propertyNames();
                while (allKeys.hasMoreElements() == true) {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedColorData.getProperty(nextKey);

                    if (nextValue != null) {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
        } else {
            throw new InvalidPrimaryKeyException("No inventory matching barcode : " + barcode + " found.");
        }
    }
    //---------------------------------------------------------------
    public Inventory(Properties props) {
        super(myTableName);
        setDependencies();
        persistentState = new Properties();
        Enumeration<?> allKeys = props.propertyNames();
        while (allKeys.hasMoreElements()) {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null) {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }
    // Methods \\
        // Public \\
    //---------------------------------------------------------------
    public void changeValue(String key, String value) {
        persistentState.setProperty(key, value);
        System.out.println("The Inventory's " + key + " has been changed to " +
                persistentState.getProperty(key) + "!");
    }
    //---------------------------------------------------------------
    public String getValue(String key) {
        return persistentState.getProperty(key);
    }
    //---------------------------------------------------------------
    private void setDependencies()  {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }
    //---------------------------------------------------------------
    @Override
    public Object getState(String key) {
        if (key.equals("UpdateStatusMessage")) {
            return updateStatusMessage;
        }
        return persistentState.getProperty(key);
    }
    //---------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }
    //---------------------------------------------------------------
    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }
    //---------------------------------------------------------------
    public void update() {
        updateStateInDatabase();
    }
    //---------------------------------------------------------------
    public String toString() {
        return "Gender: " + persistentState.getProperty("gender") +
                "\nSize: " + persistentState.getProperty("size") +
                "\nArticle Type Id: " + persistentState.getProperty("articleType") +
                "\nColor 1 Id: " + persistentState.getProperty("color1") +
                "\nColor 2 Id: " + persistentState.getProperty("color2") +
                "\nBrand: " + persistentState.getProperty("brand") +
                "\nNotes: " + persistentState.getProperty("notes") +
                "\nDonor Lastname: " + persistentState.getProperty("donorLastName") +
                "\nDonor Firstname: " + persistentState.getProperty("donorFirstName") +
                "\nDonor Phone: " + persistentState.getProperty("donorPhone") +
                "\nDonor Email: " + persistentState.getProperty("donorEmail") +
                "\nReceiver NetId: " + persistentState.getProperty("receiverNetId") +
                "\nReceiver Lastname: " + persistentState.getProperty("receiverLastName") +
                "\nReceiver Firstname: " + persistentState.getProperty("receiverFirstName") +
                "\nDate donated: " + persistentState.getProperty("dateDonated") +
                "\nDate taken: " + persistentState.getProperty("dateTaken");
    }
    //---------------------------------------------------------------
    public void display()  {
        System.out.println(toString());
    }
    //---------------------------------------------------------------
    public Vector<String> getEntryListView() {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("barcode"));
        v.addElement(persistentState.getProperty("gender"));
        v.addElement(persistentState.getProperty("size"));
        v.addElement(persistentState.getProperty("articleType"));
        v.addElement(persistentState.getProperty("color1"));
        v.addElement(persistentState.getProperty("color2"));
        v.addElement(persistentState.getProperty("brand"));
        v.addElement(persistentState.getProperty("notes"));
        v.addElement(persistentState.getProperty("status"));
        v.addElement(persistentState.getProperty("donorLastName"));
        v.addElement(persistentState.getProperty("donorFirstName"));
        v.addElement(persistentState.getProperty("donorPhone"));
        v.addElement(persistentState.getProperty("donorEmail"));
        v.addElement(persistentState.getProperty("receiverNetId"));
        v.addElement(persistentState.getProperty("receiverLastName"));
        v.addElement(persistentState.getProperty("receiverFirstName"));
        v.addElement(persistentState.getProperty("dateDonated"));
        v.addElement(persistentState.getProperty("dateTaken"));
        return v;
    }
    // Protected \\
    //----------------------------------------------------------------
    @Override
    protected void initializeSchema(String tableName) {
        if (mySchema == null) {
            mySchema = getSchemaInfo(tableName);
        }
    }
    // Private \\
    //---------------------------------------------------------------
    private void updateStateInDatabase() {
        try {
            if (persistentState.getProperty("barcode") != null) {
                Properties whereClause = new Properties();
                whereClause.setProperty("barcode",
                        persistentState.getProperty("barcode"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Inventory data for inventory barcode : " + persistentState.getProperty("barcode") + " updated successfully in database!";
            } else {
                Integer inventoryBarcodeVal =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("barcode", "" + inventoryBarcodeVal);
                updateStatusMessage = "Inventory data for new inventory : " +  persistentState.getProperty("barcode")
                        + "installed successfully in database!";
            }
        }  catch (SQLException ex) {
            updateStatusMessage = "Error in installing inventory data in database!";
        }
    }
}
