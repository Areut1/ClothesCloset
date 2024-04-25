package model;

import javafx.scene.Scene;
import userinterface.View;
import userinterface.ViewFactory;

import java.util.Properties;

public class ListAllCheckedOutTransaction extends Transaction {
        // GUI Components
        private String transactionErrorMessage = "";
        private String updateStatusMessage = "";
        private InventoryCollection iCol;


        //help me fix this

        //---------------------------------------------------------------
        protected ListAllCheckedOutTransaction() throws Exception {
            super();
            createCollection();
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
            Scene currentScene = myViews.get("DateRequestView");
            if (currentScene == null)
            {
                // create our initial view
                View newView = ViewFactory.createView("DateRequestView", this);
                currentScene = new Scene(newView);
                myViews.put("DateRequestView", currentScene);
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
                case "Transaction" -> "ListAllCheckedOut";
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
            if (key.equals("DateRequest")) {
                processTransaction((Properties) value);
            }
            myRegistry.updateSubscribers(key, this);
        }

        //---------------------------------------------------------------
        public void createCollection(){
            //Run query and get collection results
            iCol = new InventoryCollection();
        }

        public void processTransaction(Properties props) {
            try {
                iCol.findCheckedOut(props.getProperty("startDate"), props.getProperty("endDate"));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
}
