package model;

// system imports
import java.util.Properties;
import java.util.Vector;

public class ArticleTypeCollection extends EntityBase {

    private static final String myTableName = "ArticleType";
    private Vector<ArticleType> articleTypes;

    public ArticleTypeCollection() {
        super(myTableName);
        articleTypes = new Vector<ArticleType>();
    }

    public void updateBookListFromSQL(String query) throws Exception {
        // Reset bookList
        this.articleTypes = new Vector<ArticleType>();

        // Pull the data
        Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

        // Loop through data received and make fill bookList with Book objects
        for (int i = 0; i < allDataRetrieved.size(); i++) {
            this.articleTypes.add(new ArticleType(allDataRetrieved.elementAt(i)));
        }
    }

    // Finder methods
    public Vector<ArticleType> findSomeFilter(String searchQuery) {

        String query = "SELECT * FROM " + myTableName;

        try {
            updateBookListFromSQL(query);
        } catch (Exception e) {
            System.out.println("ERROR: <something>");
        }

        return this.articleTypes;
    }



    @Override
    public Object getState(String key) {
        if (key.equals("ArticleTypes"))
            return this.articleTypes;
        else
        if (key.equals("ArticleTypeCollection"))
            return this;
        return null;
    }

    @Override
    public void stateChangeRequest(String key, Object value) {
        myRegistry.updateSubscribers(key, this);
    }

    @Override
    protected void initializeSchema(String tableName) {

    }

    @Override
    public void updateState(String key, Object value) {
        stateChangeRequest(key, value);
    }
}