package model;

import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class DeleteArticleTypeTransaction extends Transaction{
    // GUI Components

    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private ArticleType oldArticleType;

    private ArticleTypeCollection atCol;


    protected DeleteArticleTypeTransaction() throws Exception {
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
        Scene currentScene = myViews.get("DeleteArticleTypeView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("DeleteArticleTypeView", this);
            currentScene = new Scene(newView);
            myViews.put("DeleteArticleTypeView", currentScene);

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
            case "ArticleType" -> oldArticleType;
            default -> null;
        };
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        switch (key) {
            case "DoYourJob" -> doYourJob();
            case "DeleteArticleType" -> processTransaction((Properties) value);
            case "SearchTableArticleType" -> {
                try {
                    processSearch((Properties) value);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            case "ConfirmArticleTypeChoice" -> processConfirm((Properties) value);
        }

        myRegistry.updateSubscribers(key, this);
    }

    public void processTransaction(Properties props) //process based on object or id?
    {
        //set status to inactive
        oldArticleType = new ArticleType(props);
        oldArticleType.changeValue("status", "Inactive"); //not sure if this will work
        createAndShowView("DeleteArticleTypeReceipt");
    }

    public void processSearch(Properties props) throws Exception {

        atCol = new ArticleTypeCollection();
        try {
            atCol.findArticleTypes(props);
        } catch (Exception e) {
            throw new Exception("Unable to search for ArticleTypes");
        }

        createAndShowView("SearchArticleTypeView");

    }

    public void processConfirm(Properties props){

        createAndShowView("SearchArticleTypeConfirm");

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



        StateChangeRequest Order:
            SearchTableArticleType
            ConfirmArticleTypeChoice
            DeleteArticleType


     */
}

