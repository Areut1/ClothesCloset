package model;

import impresario.IView;

import java.util.Properties;
import java.util.Vector;

public class ColorCollection extends EntityBase implements IView {
    private static final String myTableName = "Color";
    Vector<Color> colorList;

    public ColorCollection() {
        super(myTableName);
        colorList = new Vector<>();
    }

    public void findColors(Properties props) throws Exception {
//        String query = "SELECT * FROM " + myTableName + " WHERE ";
//
//        //start generic query, add on for each requirement
//        if (props.getProperty("bookTitle") != null){
//            //add on to query
//            query += "(bookTitle LIKE \"%" + props.getProperty("bookTitle") + "%\")";
//        }
//        if (props.getProperty("author") != null){
//            //add on to query
//            if (props.getProperty("bookTitle") != null){
//                query += " AND ";
//            }
//            query += "(author = \"" + props.getProperty("author") + "\")";
//
//        }
//        if (props.getProperty("pubYear") != null){
//            //add on to query
//            if ((props.getProperty("bookTitle") != null) || (props.getProperty("author") != null)){
//                query += " AND ";
//            }
//            query += "(pubYear = \"" + props.getProperty("pubYear") + "\")";
//        }
//        if ((props.getProperty("bookTitle") == null) && (props.getProperty("author") == null) && (props.getProperty("pubYear") == null)){
//            System.out.println("Error: no fields");
//        }
//
//        query += " ORDER BY author";
//
////        System.out.println(query);
//
//        Vector allDataRetrieved = getSelectQueryResult(query);
//
//        if (allDataRetrieved != null)
//        {
//            colorList = new Vector<>();
//
//            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
//            {
//                Properties nextColorData = (Properties)allDataRetrieved.elementAt(cnt);
//
//                Color color = new Color(nextBookData);
//
//                colorList.add(color);
//            }
//
//        }
//        else
//        {
//            throw new Exception("No Colors found with specified fields");
//        }

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
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }
}
