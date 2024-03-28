package model;

import impresario.IView;
// system imports
import java.util.Properties;
import java.util.Vector;

public class InventoryCollection extends EntityBase implements IView {

    private static final String myTableName = "Inventory";
    private Vector<Inventory> InventoryList;

    public InventoryCollection() {
        super(myTableName);
        InventoryList = new Vector<Inventory>();
    }

    public void findInventory(Properties props) throws Exception {
        String query = "SELECT * FROM " + myTableName + " WHERE ";

        //start generic query, add on for each requirement
//        if (props.getProperty("description") != null){
//            //add on to query
//            query += "(description LIKE \"%" + props.getProperty("description") + "%\")";
//        }
//        if (props.getProperty("barcodePrefix") != null){
//            //add on to query
//            if (props.getProperty("description") != null){
//                query += " AND ";
//            }
//            query += "(barcodePrefix = \"" + props.getProperty("barcodePrefix") + "\")";
//
//        }
//        if (props.getProperty("alphaCode") != null){
//            //add on to query
//            if ((props.getProperty("description") != null) || (props.getProperty("barcodePrefix") != null)){
//                query += " AND ";
//            }
//            query += "(alphaCode LIKE \"%" + props.getProperty("alphaCode") + "%\")";
//        }
//        if ((props.getProperty("description") == null) && (props.getProperty("barcodePrefix") == null) && (props.getProperty("alphaCode") == null)){
//            System.out.println("Error: no fields");
//        }
//
//        query += " ORDER BY barcodePrefix";

//        System.out.println(query);

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            InventoryList = new Vector<>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextArticleTypeData = (Properties)allDataRetrieved.elementAt(cnt);

//                Inventory in = new ArticleType(nextArticleTypeData);
//
//                InventoryList.add(in);
            }

        }
        else
        {
            throw new Exception("No Inventory found with specified fields");
        }

    }



    @Override
    public Object getState(String key) {
        if (key.equals("Inventory"))
            return this.InventoryList;
        else
        if (key.equals("InventoryCollection"))
            return this;
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }

    @Override
    protected void initializeSchema(String tableName) {

    }

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }
}