package model;

import impresario.IView;
// system imports
import java.util.Properties;
import java.util.Vector;
//---------------------------------------------------------------
public class InventoryCollection extends EntityBase implements IView {
    private static final String myTableName = "Inventory";
    private Vector<Inventory> inventoryList;
    public InventoryCollection() {
        super(myTableName);
        inventoryList = new Vector<Inventory>();
    }
    //---------------------------------------------------------------
    public void findInventory(Properties props) throws Exception {
        String query = "SELECT * FROM " + myTableName + " WHERE ";

        //start generic query, add on for each requirement
        if (props.getProperty("barcode") != null){
            //add on to query
            query += "(barcode = \"" + props.getProperty("barcode") + "\")";
        }
        if (props.getProperty("barcode") == null){
            System.out.println("Error: no fields");
        }

//        System.out.println(query);

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            inventoryList = new Vector<>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextInventoryData = (Properties)allDataRetrieved.elementAt(cnt);

                Inventory in = new Inventory(nextInventoryData);

                inventoryList.add(in);
            }

        }
        else
        {
            throw new Exception("No Inventory found with specified fields");
        }

    }

    public void retrieveBarcode5(String barcode) throws Exception {
        String query = "SELECT * FROM " + myTableName + " WHERE ";

        //start generic query, add on for each requirement
        if (barcode != null){
            //add on to query
            query += "(barcode LIKE \"" + barcode + "___\")";
        }
        if (barcode == null){
            System.out.println("Error: no fields");
        }

        query += " ORDER BY barcode";

//        System.out.println(query);

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            inventoryList = new Vector<>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextInventoryData = (Properties)allDataRetrieved.elementAt(cnt);

                Inventory in = new Inventory(nextInventoryData);

                inventoryList.add(in);
            }

        }
        else
        {
            throw new Exception("No Inventory found with specified fields");
        }
    }

    public void findDonated() throws Exception {
        String query = "SELECT * FROM " + myTableName + " WHERE (status = 'Donated')";

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            inventoryList = new Vector<>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextInventoryData = (Properties)allDataRetrieved.elementAt(cnt);

                Inventory in = new Inventory(nextInventoryData);

                inventoryList.add(in);
            }

        }
        else
        {
            throw new Exception("No Inventory found with specified fields");
        }

    }

    public void findCheckedOut(String startDate, String endDate) throws Exception {
        String query = "SELECT * FROM " + myTableName + " WHERE (dateTaken BETWEEN '" + startDate + "' AND '" + endDate + "')";

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            inventoryList = new Vector<>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextInventoryData = (Properties)allDataRetrieved.elementAt(cnt);

                Inventory in = new Inventory(nextInventoryData);

                inventoryList.add(in);
            }

        }
        else
        {
            throw new Exception("No Inventory found with specified fields");
        }

    }

    //---------------------------------------------------------------
    public int size(){
        return inventoryList.size();
    }
    //---------------------------------------------------------------
    public Inventory get(int i){
        return inventoryList.get(i);
    }
    //---------------------------------------------------------------
    @Override
    public Object getState(String key) {
        if (key.equals("Inventory"))
            return this.inventoryList;
        else
        if (key.equals("InventoryCollection"))
            return this;
        return null;
    }

    public void clearCollection(){
        inventoryList.clear();
    }
    //---------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }
    //---------------------------------------------------------------
    @Override
    protected void initializeSchema(String tableName) { }
    //---------------------------------------------------------------
    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }
}