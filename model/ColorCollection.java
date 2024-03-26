package model;

import impresario.IView;

import java.util.Properties;
import java.util.Vector;

public class ColorCollection extends EntityBase implements IView {
    private static final String myTableName = "Color";
    Vector<Color> colorList;

    public ColorCollection() {
        super(myTableName);
        colorList = new Vector<Color>();
    }

    public void findColors(Properties props) throws Exception {
        String query = "SELECT * FROM " + myTableName + " WHERE ";

        //start generic query, add on for each requirement
        if (props.getProperty("description") != null){
            //add on to query
            query += "(description LIKE \"%" + props.getProperty("description") + "%\")";
        }
        if (props.getProperty("barcodePrefix") != null){
            //add on to query
            if (props.getProperty("description") != null){
                query += " AND ";
            }
            query += "(barcodePrefix = \"" + props.getProperty("barcodePrefix") + "\")";

        }
        if (props.getProperty("alphaCode") != null){
            //add on to query
            if ((props.getProperty("description") != null) || (props.getProperty("barcodePrefix") != null)){
                query += " AND ";
            }
            query += "(alphaCode = \"" + props.getProperty("alphaCode") + "\")";
        }
        if ((props.getProperty("description") == null) && (props.getProperty("barcodePrefix") == null) && (props.getProperty("alphaCode") == null)){
            System.out.println("Error: no fields");
        }

        query += " ORDER BY barcodePrefix";

//        System.out.println(query);

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            colorList = new Vector<>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextColorData = (Properties)allDataRetrieved.elementAt(cnt);

                Color color = new Color(nextColorData);

                colorList.add(color);
            }

        }
        else
        {
            throw new Exception("No Colors found with specified fields");
        }

    }


    @Override
    public Object getState(String key) {
        if (key.equals("Colors"))
            return colorList;
        else
        if (key.equals("ColorCollection"))
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
