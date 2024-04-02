package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class DeleteInventoryTransaction extends Transaction{
    // GUI Components

    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
//    private Inventory oldInventory;
//
//    private InventoryCollection iCol;


    protected DeleteInventoryTransaction() throws Exception {
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
        Scene currentScene = myViews.get("SearchInventoryView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchInventoryView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchInventoryView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------
    protected void createAndShowView(String view)
    {
        Scene newScene = myViews.get(view);

        if (newScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView(view, this);
            newScene = new Scene(newView);
            myViews.put(view, newScene);

        }
        swapToView(newScene);
    }


    @Override
    public Object getState(String key) {
        return switch (key) {
            case "TransactionError" -> transactionErrorMessage;
            case "UpdateStatusMessage" -> updateStatusMessage;
//            case "Inventory" -> oldInventory;
//            case "InventoryCollection" -> iCol;
            case "Transaction" -> "DeleteInventory";
            default -> null;
        };
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob" -> doYourJob();
            case "DeleteInventory" -> processTransaction((Properties) value);
            case "SearchTableInventory" -> {
                try {
                    processSearch((Properties) value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            case "ConfirmInventoryChoice" -> processConfirm((Properties) value);
//            case "StartOver" -> createAndShowView("SearchArticleTypeView");
        }

        myRegistry.updateSubscribers(key, this);
    }

    public void processTransaction(Properties props)
    {
        //set status to inactive
//        oldInventory.changeValue("status", "Inactive");
//        oldInventory.update();
        createAndShowView("DeleteInventoryReceipt");
    }

    public void processSearch(Properties props) throws Exception {

//        iCol = new InventoryCollection();
//        try {
//            iCol.findInventory(props);
//        } catch (Exception e) {
//            throw new Exception("Unable to search for Inventory");
//        }

        createAndShowView("SelectInventoryView");

    }

    public void processConfirm(Properties props){

        String id = props.getProperty("inventoryId");

//        try {
//            oldInventory = new Inventory(id);
//        } catch (InvalidPrimaryKeyException e) {
//            throw new RuntimeException(e);
//        }
        createAndShowView("DeleteInventoryView");


    }



    /*
    Delete process:

        User selects delete transaction
        transaction class is made
        search view is created - text fields to search for color to delete
        button is clicked to change state
        process search transaction to get color collection
        create selection view with collection displayed
        user selects record and hits button
        state change request to show confirm view
        confirm button is pressed to process full transaction, deleting color
        receipt view is shown
        button on receipt view takes screen back to transaction choice view



        StateChangeRequest Order:
            SearchTableArticleType
            ConfirmArticleTypeChoice
            DeleteArticleType


     */
}

