// specify the package
package model;

// system imports
import java.util.Hashtable;
import java.util.Properties;

import javafx.stage.Stage;
import javafx.scene.Scene;

// project imports
import impresario.IModel;
import impresario.ISlideShow;
import impresario.IView;
import impresario.ModelRegistry;

import event.Event;
import userinterface.MainStageContainer;
import userinterface.View;
import userinterface.ViewFactory;
import userinterface.WindowPosition;



/** The class containing the Clerk for the Clothes Closet application */
//==============================================================
public class Clerk implements IView, IModel
{
    // For Impresario
    private Properties dependencies;
    private ModelRegistry myRegistry;

    // GUI Components
    private Hashtable<String, Scene> myViews;
    private Stage myStage;

    private String loginErrorMessage = "";
    private String transactionErrorMessage = "";


    // constructor for this class
    //----------------------------------------------------------
    public Clerk()
    {
        myStage = MainStageContainer.getInstance();
        myViews = new Hashtable<String, Scene>();

        //Create the Registry object
        myRegistry = new ModelRegistry("Clerk");
        if(myRegistry == null)
        {
            new Event(Event.getLeafLevelClassName(this), "Clerk",
                    "Could not instantiate Registry", Event.ERROR);
        }

        //Set the dependencies
        setDependencies();

        // Set up the initial view
        createAndShowClerkView();
    }

    //-----------------------------------------------------------------------------------
    private void setDependencies()
    {
        dependencies = new Properties();
        dependencies.setProperty("AddArticleType", "AddArticleType");
        dependencies.setProperty("ModifyArticleType", "ModifyArticleType");
        dependencies.setProperty("DeleteArticleType", "DeleteArticleType");
        dependencies.setProperty("AddColor", "AddColor");
        dependencies.setProperty("ModifyColor", "ModifyColor");
        dependencies.setProperty("DeleteColor", "DeleteColor");

        myRegistry.setDependencies(dependencies);
    }

    /**
     * Method called from client to get the value of a particular field
     * held by the objects encapsulated by this object.
     *
     * @param	key	Name of database column (field) for which the client wants the value
     *
     * @return	Value associated with the field
     */
    //----------------------------------------------------------
    public Object getState(String key)
    {
        if (key.equals("LoginError"))
        {
            return loginErrorMessage;
        }
        else
        if (key.equals("TransactionError"))
        {
            return transactionErrorMessage;
        }
        else
            return "";
    }

    //----------------------------------------------------------------
    public void stateChangeRequest(String key, Object value)
    {

        if (key.equals("Login"))
        {
            if (value != null)
            {
                loginErrorMessage = "";

                createAndShowTransactionChoiceView();

            }
        }
        else
        if (key.equals("CancelTransaction"))
        {
            createAndShowTransactionChoiceView();
        }
        else
        if ((key.equals("AddColor")) || (key.equals("AddArticleType")) ||
                (key.equals("ModifyColor")) || (key.equals("ModifyArticleType")) ||
                (key.equals("DeleteColor")) || (key.equals("DeleteArticleType")) ||
                (key.equals("AddInventory")) || (key.equals("ModifyInventory")) ||
                (key.equals("DeleteInventory")))
        {
            String transType = key;


            doTransaction(transType);

        }
        else
        if (key.equals("Logout"))
        {
            myViews.remove("TransactionChoiceView");

            createAndShowClerkView();
        }

        myRegistry.updateSubscribers(key, this);
    }

    /** Called via the IView relationship */
    //----------------------------------------------------------
    public void updateState(String key, Object value)
    {
        stateChangeRequest(key, value);
    }

    //----------------------------------------------------------
    public void doTransaction(String transactionType)
    {
        try
        {
            Transaction trans = TransactionFactory.createTransaction(transactionType);

            trans.subscribe("CancelTransaction", this);
            trans.stateChangeRequest("DoYourJob", "");
        }
        catch (Exception ex)
        {
            transactionErrorMessage = "FATAL ERROR: TRANSACTION FAILURE: Unrecognized transaction!!";
            new Event(Event.getLeafLevelClassName(this), "createTransaction",
                    "Transaction Creation Failure: Unrecognized transaction " + ex.toString(),
                    Event.ERROR);
        }
    }


    //----------------------------------------------------------
    private void createAndShowTransactionChoiceView()
    {
        Scene currentScene = (Scene)myViews.get("TransactionChoiceView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("TransactionChoiceView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("TransactionChoiceView", currentScene);
        }


        // make the view visible by installing it into the frame
        swapToView(currentScene);

    }

    //-----------------------------------------------------------------
    private void createAndShowClerkView()
    {
        Scene currentScene = (Scene)myViews.get("ClerkView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("ClerkView", this); // USE VIEW FACTORY
            currentScene = new Scene(newView);
            myViews.put("ClerkView", currentScene);
        }


        // make the view visible by installing it into the frame
        swapToView(currentScene);

    }

    /** Register objects to receive state updates. */
    //----------------------------------------------------------
    public void subscribe(String key, IView subscriber)
    {
        myRegistry.subscribe(key, subscriber);
    }

    /** Unregister previously registered objects. */
    //----------------------------------------------------------
    public void unSubscribe(String key, IView subscriber)
    {
        myRegistry.unSubscribe(key, subscriber);
    }



    //-----------------------------------------------------------------------------
    public void swapToView(Scene newScene)
    {

        if (newScene == null)
        {
            System.out.println("Clerk.swapToView(): Missing view for display");
            new Event(Event.getLeafLevelClassName(this), "swapToView",
                    "Missing view for display ", Event.ERROR);
            return;
        }

        myStage.setScene(newScene);
        myStage.sizeToScene();


        //Place in center
        WindowPosition.placeCenter(myStage);

    }

}
