package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;
//---------------------------------------------------------------
public class CheckoutInventoryTransaction extends Transaction{
    // GUI Components
    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";

    private Inventory oldInventory;
    private InventoryCollection collectionInventory;

    //---------------------------------------------------------------
    protected CheckoutInventoryTransaction() throws Exception {
        super();
    }
    //---------------------------------------------------------------
    @Override
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelModify", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");

        myRegistry.setDependencies(dependencies);
    }
    //---------------------------------------------------------------
    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("CheckoutInventoryView");
        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("CheckoutInventoryView", this);
            currentScene = new Scene(newView);
            myViews.put("CheckoutInventoryView", currentScene);
            currentScene.getStylesheets().add("userinterface/stylesheet.css");
        }
        return currentScene;
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
            newScene.getStylesheets().add("userinterface/stylesheet.css");
        }
        swapToView(newScene);
    }
    //---------------------------------------------------------------
    @Override
    public Object getState(String key) {
        return switch (key) {
            case "TransactionError" -> transactionErrorMessage;
            case "UpdateStatusMessage" -> updateStatusMessage;
            case "Inventory" -> oldInventory;
            case "InventoryCollection" -> collectionInventory;
            default -> null;
        };
    }
    //---------------------------------------------------------------
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
            case "ConfirmCheckoutInventoryChoice" -> processConfirm((Properties) value);
        }
        myRegistry.updateSubscribers(key, this);
    }
    //---------------------------------------------------------------
    public void processTransaction(Properties props) {
        oldInventory.changeValue("barcode", props.getProperty("barcode"));
        oldInventory.changeValue("gender", props.getProperty("gender"));
        oldInventory.changeValue("size", props.getProperty("size"));
        oldInventory.changeValue("articleType", props.getProperty("articleType"));
        oldInventory.changeValue("color1", props.getProperty("color1"));
        oldInventory.changeValue("color2", props.getProperty("color2"));
        oldInventory.changeValue("brand", props.getProperty("brand"));
        oldInventory.changeValue("notes", props.getProperty("notes"));
        oldInventory.changeValue("status", props.getProperty("status"));
        oldInventory.changeValue("donorLastName", props.getProperty("donorLastName"));
        oldInventory.changeValue("donorFirstName", props.getProperty("donorFirstName"));
        oldInventory.changeValue("donorPhone", props.getProperty("donorPhone"));
        oldInventory.changeValue("donorEmail", props.getProperty("donorEmail"));
        oldInventory.changeValue("receiverNetId", props.getProperty("receiverNetId"));
        oldInventory.changeValue("receiverLastName", props.getProperty("receiverLastName"));
        oldInventory.changeValue("receiverFirstName", props.getProperty("receiverFirstName"));
        oldInventory.changeValue("dateDonated", props.getProperty("dateDonated"));
        oldInventory.changeValue("dateTaken", props.getProperty("dateTaken"));
        oldInventory.update();

        System.out.println("\nERROR\nadjust swap to view name in CheckoutInventoryTransaction!!!");
        System.exit(9000);
        // FIX THIS TO MOVE TO RECEIPT SCREEN
        // createAndShowView("<CHANGE FOR WHATEVER THE VIEW CHECKOUT RECEIPT SCREEN NAME IS>");
    }
    //---------------------------------------------------------------
    public void processSearch(Properties props) throws Exception {
        collectionInventory = new InventoryCollection();
        try {
            collectionInventory.findInventory(props);
        } catch (Exception e) {
            throw new Exception("Unable to search for Inventory");
        }
        System.out.println("\nERROR\nadjust swap to view name in CheckoutInventoryTransaction!!!");
        System.exit(9000);
        createAndShowView("SelectColorView");
    }
    //---------------------------------------------------------------
    public void processConfirm(Properties props) {
        String id = props.getProperty("colorId");
        try {
            oldInventory = new Inventory(id);
        } catch (InvalidPrimaryKeyException e) {
            throw new RuntimeException(e);
        }
        System.out.println("\nERROR\nadjust swap to view name in CheckoutInventoryTransaction!!!");
        System.exit(9000);
        // FIX THIS TO MOVE TO RECEIPT SCREEN
        // createAndShowView("<CHANGE FOR WHATEVER THE VIEW CHECKOUT RECEIPT SCREEN NAME IS>");
        createAndShowView("ModifyColorView");
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

     */
}
