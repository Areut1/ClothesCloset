package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

//---------------------------------------------------------------
public class CheckOutInventoryTransaction extends Transaction{

    // GUI Components
    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private Properties barcode;
    private String ID;
    private InventoryCollection iCol;
    private Inventory oldInventory;


    //help me fix this

    //---------------------------------------------------------------
    protected CheckOutInventoryTransaction() throws Exception {
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
        Scene currentScene = myViews.get("SearchInventoryBarcodeView");
        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("SearchInventoryBarcodeView", this);
            currentScene = new Scene(newView);
            myViews.put("SearchInventoryBarcodeView", currentScene);
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
            case "Transaction" -> "CheckOut";
            case "Barcode" -> barcode;
            default -> null;
        };
    }

    //---------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob" -> doYourJob();
            case "SubmitBarcode" -> processBarcode((String) value);
            case "CheckOutInventory" -> {
                try {
                    processTransaction((Properties) value);
                } catch (InvalidPrimaryKeyException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        myRegistry.updateSubscribers(key, this);
    }

    //---------------------------------------------------------------
    public void processTransaction(Properties props) throws InvalidPrimaryKeyException {
        // Get today's Date
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dateTaken = LocalDate.now().format(formatter);

        // This is what makes the Checkout real so to speak
        oldInventory.changeValue("status", "Received");
        oldInventory.changeValue("dateTaken", dateTaken);

        // Filling in other information
        oldInventory.changeValue("receiverNetId", props.getProperty("receiverNetId"));
        oldInventory.changeValue("receiverLastName", props.getProperty("receiverLastName"));
        oldInventory.changeValue("receiverFirstName", props.getProperty("receiverFirstName"));

        // Push to database
        oldInventory.update();

        // Show receipt view
        createAndShowView("InventoryReceipt");
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
            oldInventory.oldBarcode = barcodeString;
            createAndShowView("ReceiverInfoInputView");
        }
        else{
            throw new RuntimeException();
        }
    }

    //---------------------------------------------------------------

}
