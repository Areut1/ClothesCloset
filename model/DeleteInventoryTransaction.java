package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;
//---------------------------------------------------------------
public class DeleteInventoryTransaction extends Transaction{
    // GUI Components
    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private Inventory oldInventory;
    private InventoryCollection iCol;
    private Properties barcode;
//---------------------------------------------------------------
    protected DeleteInventoryTransaction() throws Exception {
        super();
    }
    //---------------------------------------------------------------
    @Override
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelDelete", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");

        myRegistry.setDependencies(dependencies);
    }
    //---------------------------------------------------------------
    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("SearchInventoryView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchInventoryView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchInventoryView", currentScene);
            currentScene.getStylesheets().add("userinterface/stylesheet.css");
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
            case "InventoryCollection" -> iCol;
            case "Barcode" -> barcode;
            case "Transaction" -> "DeleteInventory";
            default -> null;
        };
    }
    //---------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob" -> doYourJob();
            case "DeleteInventory" -> processTransaction((Properties) value);
            case "SubmitBarcode" -> processBarcode((String) value);
//            case "StartOver" -> createAndShowView("SearchArticleTypeView");
        }

        myRegistry.updateSubscribers(key, this);
    }
    //---------------------------------------------------------------
    public void processTransaction(Properties props)
    {
        //set status to inactive
        oldInventory.changeValue("status", "Removed");
        oldInventory.update();
        createAndShowView("DeleteInventoryReceipt");
    }
    //---------------------------------------------------------------
    public void processBarcode(String barcodeString){
        char[] barcodeArr = barcodeString.toCharArray();

        String gender = "" + barcodeArr[0];
        String articleType = "" + barcodeArr[1] + barcodeArr[2];
        String color = "" + barcodeArr[3] + barcodeArr[4];
        String id = "" + barcodeArr[5] + barcodeArr[6] + barcodeArr[7];

        barcode = new Properties();
        barcode.setProperty("gender", gender);
        barcode.setProperty("articleType", articleType);
        barcode.setProperty("color1", color);
        barcode.setProperty("id", id);

        Properties barcodeProp = new Properties();
        barcodeProp.setProperty("barcode", barcodeString);

        iCol = new InventoryCollection();
        try {
            iCol.findInventory(barcodeProp);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (iCol.size() == 1){
            oldInventory = iCol.get(0);
            createAndShowView("DeleteInventoryView");
        }
        else{
            throw new RuntimeException();
        }
    }
}

