package model;

import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class DeleteColorTransaction extends Transaction{
    // GUI Components

    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private Color color;


    protected DeleteColorTransaction() throws Exception {
        super();
    }

    @Override
    protected void setDependencies() {
        dependencies = new Properties();
        dependencies.setProperty("CancelDelete", "CancelTransaction");
        dependencies.setProperty("OK", "CancelTransaction");

        myRegistry.setDependencies(dependencies);
    }

    @Override
    protected Scene createView() {
        Scene currentScene = myViews.get("DeleteColorView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("DeleteColorView", this);
            currentScene = new Scene(newView);
            myViews.put("DeletColorView", currentScene);

            return currentScene;
        }
        else
        {
            return currentScene;
        }
    }

    //------------------------------------------------------
    protected void createAndShowReceiptView()
    {
        Scene newScene = myViews.get("DeleteColorReceipt");

        if (newScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("DeleteColorReceipt", this);
            newScene = new Scene(newView);
            myViews.put("DeleteColorReceipt", newScene);

        }
        swapToView(newScene);
    }

    protected void createAndShowSearchView(){

    }

    protected void createAndShowSelectView(){

    }

    protected void createAndShowConfirmView(){

    }


    @Override
    public Object getState(String key) {
        return switch (key) {
            case "TransactionError" -> transactionErrorMessage;
            case "UpdateStatusMessage" -> updateStatusMessage;
            case "Color" -> color;
            default -> null;
        };
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob" -> doYourJob();
            case "DeleteColor" -> processTransaction((Properties) value);
        }

        myRegistry.updateSubscribers(key, this);
    }

    //TODO: work on processTransaction to delete color
    public void processTransaction(Properties props)
    {

        createAndShowReceiptView();
    }

    public void processSearch(Properties props){

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
