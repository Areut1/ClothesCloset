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

    private Properties barcode;


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
        Scene currentScene = myViews.get("SearchInventoryBarcodeView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchInventoryBarcodeView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchInventoryBarcodeView", currentScene);

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
            case "Transaction" -> "ModifyInventory";
            default -> null;
        };
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob" -> doYourJob();
            case "ModifyInventory" -> processTransaction((Properties) value);
            case "SubmitBarcode" -> processBarcode((String) value);
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



        createAndShowView("ModifyInventoryView");
    }

}


/*
    Modify Transaction Process:
        User provides barcode
        Screen showing information of corresponding Inventory
        Comboboxes and textfields to change information
        Update Inventory in database
        Receipt screen


        Screens:
            Barcode entry screen
            TextField modify screen
            Receipt Screen


 */

