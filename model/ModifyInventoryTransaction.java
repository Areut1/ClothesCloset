package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class ModifyInventoryTransaction extends Transaction{
    // GUI Components

    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private Inventory oldInventory;

    private InventoryCollection iCol;


    protected ModifyInventoryTransaction() throws Exception {
        super();
    }

    @Override
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelModify", "CancelTransaction");
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
            case "Inventory" -> oldInventory;
            case "InventoryCollection" -> iCol;
            default -> null;
        };
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob" -> doYourJob();
            case "ModifyInventory" -> processTransaction((Properties) value);
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
//        oldInventory.changeValue("description", props.getProperty("description"));
//        oldInventory.changeValue("barcodePrefix", props.getProperty("barcodePrefix"));
//        oldInventory.changeValue("alphaCode", props.getProperty("alphaCode"));


        oldInventory.update();
        createAndShowView("ModifyArticleTypeReceipt");
    }

    public void processSearch(Properties props) throws Exception {

        iCol = new InventoryCollection();
        try {
            iCol.findInventory(props);
        } catch (Exception e) {
            throw new Exception("Unable to search for Inventory");
        }

        createAndShowView("SelectInventoryView");

    }

    public void processConfirm(Properties props){

        String id = props.getProperty("inventoryId");

        try {
            oldInventory = new Inventory(id);
        } catch (InvalidPrimaryKeyException e) {
            throw new RuntimeException(e);
        }
        createAndShowView("ModifyInventoryView");


    }

}


/*
    Modify Transaction Process:
        User provides barcode
        Screen showing information of corresponding Inventory
        Comboboxes and textfields to change information
        Confirm screen
        Update Inventory in database
        Receipt screen


        Screens:
            Barcode entry screen
            TextField modify screen
            Confirm screen
            Receipt Screen


 */

