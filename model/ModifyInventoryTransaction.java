package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

import static java.lang.Integer.parseInt;

//---------------------------------------------------------------
public class ModifyInventoryTransaction extends Transaction{
    // GUI Components
    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private Inventory oldInventory;
    private InventoryCollection iCol;
    private Properties barcode;
    private ColorCollection cCol;
    private ArticleTypeCollection atCol;
    private String ID;
    private String barcodeString;
    private String originalBarcode;
    //---------------------------------------------------------------
    protected ModifyInventoryTransaction() throws Exception {
        super();
        createCollections();
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
            case "ArticleTypeList" -> atCol;
            case "ColorList" -> cCol;
            case "Inventory" -> oldInventory;
            case "InventoryCollection" -> iCol;
            case "Barcode" -> barcode;
            case "Transaction" -> "ModifyInventory";
            case "ID" -> ID;
            default -> null;
        };
    }
    //---------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob" -> doYourJob();
            case "ModifyInventory" -> processTransaction((Properties) value);
            case "SubmitBarcode" -> processBarcode((String) value);
            case "GetID" -> getID((String)value);
    //case "StartOver" -> createAndShowView("SearchArticleTypeView");
        }

        myRegistry.updateSubscribers(key, this);
    }

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

        System.out.println(ID);

    }
    //---------------------------------------------------------------
    public void processTransaction(Properties props) {

        originalBarcode = props.getProperty("originalBarcode");
        oldInventory.oldBarcode = originalBarcode;

        oldInventory.changeValue("barcode", props.getProperty("barcode"));
        oldInventory.changeValue("gender", props.getProperty("gender"));
        oldInventory.changeValue("size", props.getProperty("size"));
        oldInventory.changeValue("articleType", props.getProperty("articleType"));
        oldInventory.changeValue("color1", props.getProperty("color1"));

        oldInventory.changeValue("color2", props.getProperty("color2"));
        oldInventory.changeValue("brand", props.getProperty("brand"));
        oldInventory.changeValue("notes", props.getProperty("notes"));
//        oldInventory.changeValue("status", props.getProperty("status"));
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

        System.out.println(oldInventory);

        createAndShowView("InventoryReceipt");
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
        ID = id;

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
            createAndShowView("ModifyInventoryInputView");
        }
        else{
            throw new RuntimeException();
        }
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

