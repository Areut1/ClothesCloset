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
        Scene currentScene = myViews.get("ListResultsView");
        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("ListResultsView", this);
            currentScene = new Scene(newView);
            myViews.put("ListResultsView", currentScene);
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
            case "Transaction" -> "ListAllDonated";
            case "Barcode" -> barcode;
            default -> null;
        };
    }

    //---------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob" -> doYourJob();

        }
        myRegistry.updateSubscribers(key, this);
    }

    //---------------------------------------------------------------
    public void processTransaction() throws InvalidPrimaryKeyException {
        //Run query and get collection results

    }
    //---------------------------------------------------------------

}
