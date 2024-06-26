package model;

import exception.InvalidPrimaryKeyException;
import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;
//---------------------------------------------------------------
public class AddArticleTypeTransaction extends Transaction{
    // GUI Components

    private String transactionErrorMessage = "";
    private String updateStatusMessage = "";
    private ArticleType newArticleType;

    //---------------------------------------------------------------
    protected AddArticleTypeTransaction() throws Exception {
        super();
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
        Scene currentScene = myViews.get("AddArticleTypeView");

        if (currentScene == null)
        {
            // create our initial view
            View newView = null;
            try {
                newView = ViewFactory.createView("AddArticleTypeView", this);
            } catch (InvalidPrimaryKeyException e) {
                throw new RuntimeException(e);
            }
            currentScene = new Scene(newView);
            myViews.put("AddArticleTypeView", currentScene);
            currentScene.getStylesheets().add("userinterface/stylesheet.css");

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
        Scene newScene = myViews.get("AddArticleTypeReceipt");

        if (newScene == null)
        {
            // create our initial view
            View newView = null;
            try {
                newView = ViewFactory.createView("AddArticleTypeReceipt", this);
            } catch (InvalidPrimaryKeyException e) {
                throw new RuntimeException(e);
            }
            newScene = new Scene(newView);
            myViews.put("AddArticleTypeReceipt", newScene);
            newScene.getStylesheets().add("userinterface/stylesheet.css");
        }
        swapToView(newScene);
    }
    //---------------------------------------------------------------
    @Override
    public Object getState(String key) {
        if (key.equals("TransactionError") == true)
        {
            return transactionErrorMessage;
        }
        else
        if (key.equals("UpdateStatusMessage") == true)
        {
            return updateStatusMessage;
        }
        else
        if (key.equals("ArticleType") == true)
        {
            return newArticleType;
        }
        return null;
    }
    //---------------------------------------------------------------
    @Override
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("AddArticleType") == true)
        {
            processTransaction((Properties)value);
        }

        myRegistry.updateSubscribers(key, this);
    }
    //---------------------------------------------------------------
    public void processTransaction(Properties props)
    {
        newArticleType = new ArticleType(props);
        newArticleType.update();
        createAndShowReceiptView();
    }
}
