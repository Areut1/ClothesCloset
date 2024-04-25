package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Properties;

//---------------------------------------------------------------
public class ListAllDonatedTransaction extends Transaction{

    // GUI Components
    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private Properties barcode;
    private String ID;
    private InventoryCollection iCol;
    private Inventory oldInventory;


    //help me fix this

    //---------------------------------------------------------------
    protected ListAllDonatedTransaction() throws Exception {
        super();
        processTransaction();
    }

    //---------------------------------------------------------------
    @Override
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelList", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");

        myRegistry.setDependencies(dependencies);
    }

    //---------------------------------------------------------------
    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("InventoryCollectionView");
        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("InventoryCollectionView", this);
            currentScene = new Scene(newView);
            myViews.put("InventoryCollectionView", currentScene);
            currentScene.getStylesheets().add("userinterface/stylesheet.css");
        }
        return currentScene;
    }

    //---------------------------------------------------------------
    @Override
    public Object getState(String key) {
        return switch (key) {
            case "TransactionError" -> transactionErrorMessage;
            case "UpdateStatusMessage" -> updateStatusMessage;
            case "Transaction" -> "ListAllDonated";
            case "InventoryCollection" -> iCol;
            default -> null;
        };
    }

    //---------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("DoYourJob")) {
            doYourJob();
        }
        myRegistry.updateSubscribers(key, this);
    }

    //---------------------------------------------------------------
    public void processTransaction(){
        //Run query and get collection results
        iCol = new InventoryCollection();
        try {
            iCol.findDonated();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    //---------------------------------------------------------------

}
