package model;

import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

import static java.lang.Integer.parseInt;

//---------------------------------------------------------------
public class AddInventoryTransaction extends Transaction{
    // GUI Components

    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private Inventory newInventory;

    private ArticleTypeCollection atCol;
    private ColorCollection cCol;
    private Properties barcode;
    private String ID;

    private int transCount = 0;
    //---------------------------------------------------------------
    protected AddInventoryTransaction() throws Exception {
        super();
        createCollections();
    }
    //---------------------------------------------------------------
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
        Scene currentScene = myViews.get("SearchInventoryBarcodeView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchInventoryBarcodeView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchInventoryBarcodeView", currentScene);
            currentScene.getStylesheets().add("userinterface/stylesheet.css");
            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }
    //---------------------------------------------------------------
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
            case "ArticleTypeList" -> atCol;
            case "ColorList" -> cCol;
            case "Inventory" -> newInventory;
            case "Barcode" -> barcode;
            case "Transaction" -> "AddInventory";
            case "ID" -> ID;
            default -> null;
        };
    }
    //---------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob" -> doYourJob();
            case "AddInventory" -> processTransaction((Properties) value);
            case "SubmitBarcode" -> processBarcode((String) value);
            case "GetID" -> getID((String)value);
        }

        myRegistry.updateSubscribers(key, this);
    }
    //---------------------------------------------------------------
    public void getID(String barcode5){
        InventoryCollection iCol = new InventoryCollection();
        try {
            iCol.retrieveBarcode5(barcode5);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (iCol.size() > 0){
            Inventory inv = iCol.get(iCol.size() - 1);
            String id = inv.getValue("barcode").substring(5);

            Integer newID = parseInt(id);
            newID++;

            String finalID = "" + newID;

            if (finalID.length() == 2){
                finalID = "0" + finalID;
            }
            if (finalID.length() == 1){
                finalID = "00" + finalID;
            }

            ID = finalID;
        }
        else if (iCol.size() == 0){
            ID = "001";
        }
        else{
            throw new RuntimeException();
        }

    }

    public void processTransaction(Properties props)
    {
        Properties finalProps = new Properties();


        //TODO: if any of these are null, set to null???
        String barcodeString = barcode.getProperty("gender") + barcode.getProperty("articleType") + barcode.getProperty("color1") + barcode.getProperty("id");
        finalProps.setProperty("barcode", barcodeString);
        finalProps.setProperty("gender", barcode.getProperty("gender"));
        finalProps.setProperty("articleType", barcode.getProperty("articleType"));
        finalProps.setProperty("color1", barcode.getProperty("color1"));
        finalProps.setProperty("color2", props.getProperty("color2"));
        finalProps.setProperty("size", props.getProperty("size"));
        finalProps.setProperty("brand", props.getProperty("brand"));
        finalProps.setProperty("notes", props.getProperty("notes"));
        finalProps.setProperty("status", "Donated");
        finalProps.setProperty("donorLastName", props.getProperty("donorLastName"));
        finalProps.setProperty("donorFirstName", props.getProperty("donorFirstName"));
        finalProps.setProperty("donorPhone", props.getProperty("donorPhone"));
        finalProps.setProperty("donorEmail", props.getProperty("donorEmail"));
//        finalProps.setProperty("donationDate", )

        newInventory = new Inventory(finalProps);
        System.out.println(newInventory);

        newInventory.update();
        createAndShowView("AddInventoryReceipt");
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
