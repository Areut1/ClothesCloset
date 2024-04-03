package model;

import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;
//---------------------------------------------------------------
public class AddColorTransaction extends Transaction{
    // GUI Components

    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private Color newColor;
    //---------------------------------------------------------------
    protected AddColorTransaction() throws Exception {
        super();
    }
    //---------------------------------------------------------------
    @Override
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelAdd", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");

        myRegistry.setDependencies(dependencies);
    }
    //---------------------------------------------------------------
    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("AddColorView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("AddColorView", this);
            currentScene = new Scene(newView);
            myViews.put("AddColorView", currentScene);
            currentScene.getStylesheets().add("userinterface/stylesheet.css");
            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }
    //------------------------------------------------------
    protected void createAndShowReceiptView()
    {
        Scene newScene = myViews.get("AddColorReceipt");

        if (newScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("AddColorReceipt", this);
            newScene = new Scene(newView);
            myViews.put("AddColorReceipt", newScene);
            newScene.getStylesheets().add("userinterface/stylesheet.css");
        }
        swapToView(newScene);
    }
    //---------------------------------------------------------------
    @Override
    public Object getState(String key) {
        if (key.equals("TransactionError") == true)
        {
            return transactionErrorMessage;
        }
        else
        if (key.equals("UpdateStatusMessage") == true)
        {
            return updateStatusMessage;
        }
        else
        if (key.equals("Color") == true)
        {
            return newColor;
        }
        return null;
    }
    //---------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("AddColor") == true)
        {
            processTransaction((Properties)value);
        }

        myRegistry.updateSubscribers(key, this);
    }
    //---------------------------------------------------------------
    public void processTransaction(Properties props)
    {
        newColor = new Color(props);
        newColor.update();
        createAndShowReceiptView();
    }
}
