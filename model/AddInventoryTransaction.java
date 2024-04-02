package model;

import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class AddInventoryTransaction extends Transaction{
    // GUI Components

    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private Inventory newInventory;

    private ArticleTypeCollection atCol;
    private ColorCollection cCol;
    private Properties barcode;

    private int transCount = 0;


    protected AddInventoryTransaction() throws Exception {
        super();
        createCollections();
    }

    private void createCollections() throws Exception {
        atCol = new ArticleTypeCollection();
        cCol = new ColorCollection();
        try {
            atCol.findAllArticleTypes();
            cCol.findAllColors();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


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
        Scene currentScene = myViews.get("AddInventoryBarcodeView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("AddInventoryBarcodeView", this);
            currentScene = new Scene(newView);
            myViews.put("AddInventoryBarcodeView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

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
            case "ArticleTypeList" -> atCol;
            case "ColorList" -> cCol;
            case "Inventory" -> newInventory;
            case "Barcode" -> barcode;
            default -> null;
        };
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob" -> doYourJob();
            case "AddInventory" -> processTransaction((Properties) value);
            case "SubmitBarcode" -> processBarcode((String) value);
        }

        myRegistry.updateSubscribers(key, this);
    }

    public void processTransaction(Properties props)
    {
        props.setProperty("barcode", barcode.getProperty("barcode"));
        props.setProperty("gender", barcode.getProperty("gender"));
        props.setProperty("color1", barcode.getProperty("color1"));

        newInventory = new Inventory(props);
        newInventory.update();
        createAndShowView("AddInventoryReceipt");
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

        switch (transCount) {
            case 0 -> {
                transCount++;
                createAndShowView("AddInventoryConfirmView");
            }
            case 1 -> createAndShowView("AddInventoryInputView");
        }

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
