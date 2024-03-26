package model;

import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class AddInventoryTransaction extends Transaction{
    // GUI Components

    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
//    private Inventory newInventory;


    protected AddInventoryTransaction() throws Exception {
        super();
    }

    @Override
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelAdd", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");

        myRegistry.setDependencies(dependencies);
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("AddInventoryView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("AddInventoryView", this);
            currentScene = new Scene(newView);
            myViews.put("AddInventoryView", currentScene);

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
        Scene newScene = myViews.get("AddInventoryReceipt");

        if (newScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("AddInventoryReceipt", this);
            newScene = new Scene(newView);
            myViews.put("AddInventoryReceipt", newScene);

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
//        if (key.equals("Inventory") == true)
//        {
//            return newInventory;
//        }
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("AddInventory") == true)
        {
            processTransaction((Properties)value);
        }

        myRegistry.updateSubscribers(key, this);
    }

    public void processTransaction(Properties props)
    {
//        newInventory = new Inventory(props);
//        newInventory.update();
        createAndShowReceiptView();
    }

    public void processBarcode(String barcode){

    }
}



/*
        Add Inventory Process:
            User submits 5-digit barcode
            Confirm proper format
            1st digit is gender
            2nd and 3rd digits are ArticleType barcode prefix
            4th and 5th digits are Color barcode prefix
            Get corresponding records from database
            Confirm with user that correct information is correct
            User inputs size, color 2, brand name, notes, donor first + last name, phone and email
            Create new inventory object
            Update in database
            Show receipt
 */
