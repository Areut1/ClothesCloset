// specify the package
package model;

// system imports
import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;
import javax.swing.JFrame;

// project imports
import event.Event;
import exception.InvalidPrimaryKeyException;
import database.*;

import impresario.IView;

import userinterface.ViewFactory;

/** The class containing the Patron for pair programming1: Database access from java */
//==============================================================
public class ArticleType extends EntityBase implements IView
{
    private static final String myTableName = "ArticleType";

    protected Properties dependencies;

    // GUI Components

    private String updateStatusMessage = "";

    // constructor for this class
    //----------------------------------------------------------
    public ArticleType(String articleTypeId)
            throws InvalidPrimaryKeyException
    {
        super(myTableName);

        setDependencies();
        String query = "SELECT * FROM " + myTableName + " WHERE (articleTypeId = " + articleTypeId + ")";

        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // You must get one Article Type at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one Article Type. More than that is an error
            if (size != 1)
            {
                throw new InvalidPrimaryKeyException("Multiple Article Types matching id : "
                        + articleTypeId + " found.");
            }
            else
            {
                // copy all the retrieved data into persistent state
                Properties retrievedArticleTypeData = allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedArticleTypeData.propertyNames();
                while (allKeys.hasMoreElements() == true)
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedArticleTypeData.getProperty(nextKey);

                    if (nextValue != null)
                    {
                        persistentState.setProperty(nextKey, nextValue);
                    }
                }

            }
        }
        // If no Article found for this id, throw an exception
        else
        {
            throw new InvalidPrimaryKeyException("No article type matching id : "
                    + articleTypeId + " found.");
        }
    }

    // Can also be used to create a NEW Article Type (if the system it is part of
    // allows for a new patron to be set up)
    //----------------------------------------------------------
    public ArticleType(Properties props)
    {
        super(myTableName);

        setDependencies();
        persistentState = new Properties();
        Enumeration<?> allKeys = props.propertyNames();
        while (allKeys.hasMoreElements())
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null)
            {
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }

    //--------------METHODS--------------------------------------------------------------
    /*changeValue
     * Method takes key and value strings and changes value in property object
     */

    public void changeValue(String key, String value){
        persistentState.setProperty(key, value);
        System.out.println("The Article Type's " + key + " has been changed to " +
        persistentState.getProperty(key) + "!");
    }

    //========================================================================
    /*getValue
     * Method takes key string and returns corresponding value
     */

    public String getValue(String key){
        return persistentState.getProperty(key);
    }

    //--------------------------------------------------------------------------------------------
    private void setDependencies()
    {
        dependencies = new Properties();

        myRegistry.setDependencies(dependencies);
    }

    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("UpdateStatusMessage"))
            return updateStatusMessage;

        return persistentState.getProperty(key);
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {
        myRegistry.updateSubscribers(key, this);
    }

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }



    //-----------------------------------------------------------------------------------
    public static int compare(ArticleType a, ArticleType b)
    {
        String aNum = (String)a.getState("name");
        String bNum = (String)b.getState("name");

        return aNum.compareTo(bNum);
    }

    //-----------------------------------------------------------------------------------
    public void update()
    {
        updateStateInDatabase();
    }

    //-----------------------------------------------------------------------------------
    private void updateStateInDatabase()
    {
        try
        {
            if (persistentState.getProperty("articleTypeId") != null)
            {
                // update
                Properties whereClause = new Properties();
                whereClause.setProperty("articleTypeId",
                        persistentState.getProperty("articleTypeId"));
                updatePersistentState(mySchema, persistentState, whereClause);
                updateStatusMessage = "Article Type data for article type id : " + persistentState.getProperty("articleTypeId") + " updated successfully in database!";
            }
            else
            {
                // insert
                Integer articleTypeIdVal =
                        insertAutoIncrementalPersistentState(mySchema, persistentState);
                persistentState.setProperty("articleTypeId", "" + articleTypeIdVal);
                updateStatusMessage = "Data for new article type, given id : " +  persistentState.getProperty("articleTypeId")
                        + " installed successfully in database!";
            }
        }
        catch (SQLException ex)
        {
            updateStatusMessage = "Error in installing article type data in database!";
        }
        //DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
    }

    public String toString()
    {
        return "Description: " + persistentState.getProperty("description") + "\n Barcode Prefix: " +
                persistentState.getProperty("barcodePrefix") + "\n Alpha Code: " +
                persistentState.getProperty("alphaCode");
    }

    public void display()
    {
        System.out.println(toString());
    }


    /**
     * This method is needed solely to enable the Account information to be displayable in a table
     *
     */
    //--------------------------------------------------------------------------
    public Vector<String> getEntryListView()
    {
        Vector<String> v = new Vector<String>();

        v.addElement(persistentState.getProperty("articleTypeId"));
        v.addElement(persistentState.getProperty("description"));
        v.addElement(persistentState.getProperty("barcodePrefix"));
        v.addElement(persistentState.getProperty("alphaCode"));
        v.addElement(persistentState.getProperty("status"));

        return v;
    }

    //-----------------------------------------------------------------------------------
    protected void initializeSchema(String tableName)
    {
        if (mySchema == null)
        {
            mySchema = getSchemaInfo(tableName);
        }
    }
}

