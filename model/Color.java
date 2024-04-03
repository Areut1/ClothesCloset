package model;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

import exception.InvalidPrimaryKeyException;
import impresario.IView;
//==============================================================
public class Color extends EntityBase implements IView {
	// Attributes \\
		// Private \\
	private static final String myTableName = "Color";

	protected Properties dependencies;
	private String updateStatusMessage = "";
	// Constructor for this class
	//----------------------------------------------------------------
	public Color(String colorId) throws InvalidPrimaryKeyException {
		super(myTableName);
		
		setDependencies();

		String query = "SELECT * FROM " + myTableName + " WHERE (colorId = " + colorId + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		if (allDataRetrieved != null) {
			int size = allDataRetrieved.size();

			if (size != 1) {
				throw new InvalidPrimaryKeyException("Multiple colors matching id : " + colorId + " found.");
			}
			else
			{
				Properties retrievedColorData = allDataRetrieved.elementAt(0);
				persistentState = new Properties();

				Enumeration<?> allKeys = retrievedColorData.propertyNames();
				while (allKeys.hasMoreElements() == true) {
					String nextKey = (String)allKeys.nextElement();
					String nextValue = retrievedColorData.getProperty(nextKey);

					if (nextValue != null) {
						persistentState.setProperty(nextKey, nextValue);
					}
				}

			}
		} else {
			throw new InvalidPrimaryKeyException("No color matching id : " + colorId + " found.");
		}
	}
	//-------------------------------------------------------------------------------
	public Color(Properties props) {
		super(myTableName);
		setDependencies();
		persistentState = new Properties();
		Enumeration<?> allKeys = props.propertyNames();
		while (allKeys.hasMoreElements()) {
			String nextKey = (String)allKeys.nextElement();
			String nextValue = props.getProperty(nextKey);

			if (nextValue != null) {
				persistentState.setProperty(nextKey, nextValue);
			}
		}
	}
	//-----------------------------------------------------------------------------------
	public void changeValue(String key, String value){
		persistentState.setProperty(key, value);
		System.out.println("The Color's " + key + " has been changed to " +
				persistentState.getProperty(key) + "!");
	}
	//-----------------------------------------------------------------------------------
	/*getValue
	 * Method takes key string and returns corresponding value
	 */
	public String getValue(String key){
		return persistentState.getProperty(key);
	}
	//-----------------------------------------------------------------------------------
	private void setDependencies()
	{
		dependencies = new Properties();

		myRegistry.setDependencies(dependencies);
	}
	//-----------------------------------------------------------------------------------
	@Override
	public Object getState(String key) {
		if (key.equals("UpdateStatusMessage")) {
			return updateStatusMessage;
		}
		return persistentState.getProperty(key);
	}
	//------------------------------------------------------------------------------------
	@Override
	public void stateChangeRequest(String key, Object value) {
		myRegistry.updateSubscribers(key, this);
	}
	//------------------------------------------------------------------------------------
	@Override
	public void updateState(String key, Object value) {
		stateChangeRequest(key, value);
	}
	//-------------------------------------------------------------------------------------
	public void update() {
		updateStateInDatabase();
	}
	//-------------------------------------------------------------------------------------
	private void updateStateInDatabase()  {
		try {
			if (persistentState.getProperty("colorId") != null) {
				Properties whereClause = new Properties();
				whereClause.setProperty("colorId",
						persistentState.getProperty("colorId"));
				updatePersistentState(mySchema, persistentState, whereClause);
				updateStatusMessage = "Color data for color id : " + persistentState.getProperty("colorId") + " updated successfully in database!";
			} else {
				Integer colorIdVal =
						insertAutoIncrementalPersistentState(mySchema, persistentState);
				persistentState.setProperty("colorId", "" + colorIdVal);
				updateStatusMessage = "Color data for new color : " +  persistentState.getProperty("colorId")
						+ "installed successfully in database!";
			}
		}
		catch (SQLException ex) {
			updateStatusMessage = "Error in installing color data in database!";
		}
	}
	//---------------------------------------------------------------------------------------
	public String toString()
	{
		return "Description: " + persistentState.getProperty("description") + "\n Barcode Prefix: " +
				persistentState.getProperty("barcodePrefix") + "\n Alpha Code: " +
				persistentState.getProperty("alphaCode");
	}
	//----------------------------------------------------------------------------------------
	public void display()
	{
		System.out.println(toString());
	}
	//-----------------------------------------------------------------------------------------
	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();

		v.addElement(persistentState.getProperty("colorId"));
		v.addElement(persistentState.getProperty("description"));
		v.addElement(persistentState.getProperty("barcodePrefix"));
		v.addElement(persistentState.getProperty("alphaCode"));
		v.addElement(persistentState.getProperty("status"));

		return v;
	}
	//------------------------------------------------------------------------------------------
	@Override
	protected void initializeSchema(String tableName) {
		if (mySchema == null) {
			mySchema = getSchemaInfo(tableName);
		}
	}

}
