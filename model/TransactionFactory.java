// specify the package
package model;
/** The class containing the TransactionFactory for the ClothesCloset application */
//==============================================================
public class TransactionFactory
{
	//----------------------------------------------------------
	public static Transaction createTransaction(String transType)
		throws Exception
	{
		Transaction retValue = null;

		switch (transType) {
			case "AddColor":
				retValue = new AddColorTransaction();
				break;
			case "ModifyColor":
				retValue = new ModifyColorTransaction();
				break;
			case "DeleteColor":
				retValue = new DeleteColorTransaction();
				break;
			case "AddArticleType":
				retValue = new AddArticleTypeTransaction();
				break;
			case "ModifyArticleType":
				retValue = new ModifyArticleTypeTransaction();
				break;
			case "DeleteArticleType":
				retValue = new DeleteArticleTypeTransaction();
				break;
			case "AddInventory":
				retValue = new AddInventoryTransaction();
				break;
			case "ModifyInventory":
				retValue = new ModifyInventoryTransaction();
				break;
			case "DeleteInventory":
				retValue = new DeleteInventoryTransaction();
				break;
//			case "CheckOut":
//				retValue = new CheckOutTransaction();
//			case "ListAllAvailable":
//				retValue = new ListInventoryTransaction();
//			case "ListAllCheckOut":
//				retValue = new ListCheckOutTransaction();
		}
		return retValue;
	}
}
