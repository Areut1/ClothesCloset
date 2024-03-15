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
    protected void createAndShowReceiptView()
    {
        Scene newScene = myViews.get("DeleteArticleTypeReceipt");

        if (newScene == null)
        {
            // create our initial view
            View newView = ViewFactory.createView("DeleteArticleTypeReceipt", this);
            newScene = new Scene(newView);
            myViews.put("DeleteArticleTypeReceipt", newScene);

        }
        swapToView(newScene);
    }


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
            return oldArticleType;
        }
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        if (key.equals("DoYourJob") == true)
        {
            doYourJob();
        }
        else
        if (key.equals("DeleteArticleType") == true)
        {
            processTransaction((Properties)value);
        }

        myRegistry.updateSubscribers(key, this);
    }

    public void processTransaction(Properties props)
    {
        oldArticleType = new ArticleType(props);
        oldArticleType.deleteValue();
        createAndShowReceiptView();
    }
}

