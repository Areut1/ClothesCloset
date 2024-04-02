
// specify the package

// system imports
import java.util.Locale;
import java.util.ResourceBundle;
import java.io.FileOutputStream;
import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

// project imports
import event.Event;
import event.EventLog;
import common.PropertyFile;

import model.Clerk;
import userinterface.MainStageContainer;
import userinterface.WindowPosition;


/** The class containing the main program  for the ATM application */
//==============================================================
public class ClothesCloset extends Application
{

	private Clerk myClerk;		// the main behavior for the application

	/** Main frame of the application */
	private Stage mainStage;


	// start method for this class, the main application object
	//----------------------------------------------------------
	public void start(Stage primaryStage)
	{
	   System.out.println("Clothes Closet Version 1.00");
	   System.out.println("Copyright 2024");

           // Create the top-level container (main frame) and add contents to it.
	   MainStageContainer.setStage(primaryStage, "Brockport Clothes Closet version 1.00");
	   mainStage = MainStageContainer.getInstance();
	   mainStage.getIcons().add(new Image("/images/Brockport_Icon.png"));
	   mainStage.setResizable(false);

	   // Finish setting up the stage (ENABLE THE GUI TO BE CLOSED USING THE TOP RIGHT
	   // 'X' IN THE WINDOW), and show it.
           mainStage.setOnCloseRequest(new EventHandler <javafx.stage.WindowEvent>() {
            @Override
            public void handle(javafx.stage.WindowEvent event) {
                System.exit(0);
            }
           });

           try
	   {
		myClerk = new Clerk();
	   }
	   catch(Exception exc)
	   {
		System.err.println("ClothesCloset.ClothesCloset - could not create Clerk!");
		new Event(Event.getLeafLevelClassName(this), "ClothesCloset.<init>", "Unable to create Clerk object", Event.ERROR);
		exc.printStackTrace();
	   }


  	   WindowPosition.placeCenter(mainStage);

           mainStage.show();
	}


	/** 
	 * The "main" entry point for the application. Carries out actions to
	 * set up the application
	 */
	//----------------------------------------------------------
    	public static void main(String[] args)
	{

		launch(args);
	}

}
