// specify the package
package model;

/** The class containing the TransactionFactory for the ATM application */
//==============================================================
public class TransactionFactory
{
	//----------------------------------------------------------
	public static Transaction createTransaction(String transType)
		throws Exception
	{
		Transaction retValue = null;

		switch (transType) {
			case "InsertBook":
//				retValue = new InsertBookTransaction();
				break;
			case "InsertPatron":
//				retValue = new InsertPatronTransaction();
				break;
			case "SearchBooks":
//				retValue = new SearchBooksTransaction();
				break;
			case "SearchPatrons":
//				retValue = new SearchPatronsTransaction();
				break;
		}

		return retValue;
	}
}
