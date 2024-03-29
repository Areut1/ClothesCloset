package model;

import impresario.IView;
// system imports
import java.util.Properties;
import java.util.Vector;

public class ArticleTypeCollection extends EntityBase implements IView {

    private static final String myTableName = "ArticleType";
    private Vector<ArticleType> articleTypeList;

    public ArticleTypeCollection() {
        super(myTableName);
        articleTypeList = new Vector<ArticleType>();
    }

    public void findArticleTypes(Properties props) throws Exception {
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
            query += "(alphaCode LIKE \"%" + props.getProperty("alphaCode") + "%\")";
        }
        if ((props.getProperty("description") == null) && (props.getProperty("barcodePrefix") == null) && (props.getProperty("alphaCode") == null)){
            System.out.println("Error: no fields");
        }

        query += " ORDER BY barcodePrefix";

//        System.out.println(query);

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            articleTypeList = new Vector<>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextArticleTypeData = (Properties)allDataRetrieved.elementAt(cnt);

                ArticleType at = new ArticleType(nextArticleTypeData);

                articleTypeList.add(at);
            }

        }
        else
        {
            throw new Exception("No ArticleType found with specified fields");
        }

    }

    public ArticleType get(int i){
        return articleTypeList.get(i);
    }

    public int size(){
        return articleTypeList.size();
    }

    public void findAllArticleTypes() throws Exception {
        String query = "SELECT * FROM " + myTableName + " ORDER BY barcodePrefix";

        Vector allDataRetrieved = getSelectQueryResult(query);

        if (allDataRetrieved != null)
        {
            articleTypeList = new Vector<>();

            for (int cnt = 0; cnt < allDataRetrieved.size(); cnt++)
            {
                Properties nextArticleTypeData = (Properties)allDataRetrieved.elementAt(cnt);

                ArticleType at = new ArticleType(nextArticleTypeData);

                articleTypeList.add(at);
            }

        }
        else
        {
            throw new Exception("No ArticleType found with specified fields");
        }

    }



    @Override
    public Object getState(String key) {
        if (key.equals("ArticleTypes"))
            return this.articleTypeList;
        else
        if (key.equals("ArticleTypeCollection"))
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