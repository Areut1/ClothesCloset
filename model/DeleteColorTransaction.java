package model;

import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class DeleteColorTransaction extends Transaction{
    // GUI Components

    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private Color color;


    protected DeleteColorTransaction() throws Exception {
        super();
    }

    @Override
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelDelete", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");

        myRegistry.setDependencies(dependencies);
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("DeleteColorView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("DeleteColorView", this);
            currentScene = new Scene(newView);
            myViews.put("DeletColorView", currentScene);

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
        Scene newScene = myViews.get("DeleteColorReceipt");

        if (newScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("DeleteColorReceipt", this);
            newScene = new Scene(newView);
            myViews.put("DeleteColorReceipt", newScene);

        }
        swapToView(newScene);
    }


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
            return color;
        }
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("DeleteColor") == true)
        {
            processTransaction((Properties)value);
        }

        myRegistry.updateSubscribers(key, this);
    }

    //TODO: work on processTransaction to delete color
    public void processTransaction(Properties props)
    {
        color = new Color(props);
        color.update();
        createAndShowReceiptView();
    }
}
