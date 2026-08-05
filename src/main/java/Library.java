
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;

public class Library {
    private Connection db = null;
    private ArrayList<Entry> searchResults;

    public void getLatestEntryId(){}

    public Entry getUrlResponse(Entry entry){}
    public void addEntries(String file_path, String file_type){}

    public ArrayList<Entry> searchLibrary(String query){}

    public ArrayList<Entry> getSearchResults(){}

    public Entry getEntry(id entry_id){}

    public void viewEntry(id entry_id){}

    public void setEntry(id entry_id){}

    public void updateEntry(id entry_id){
        setEntry(entry_id);
    }

    public void deleteEntry(id entry_id){}

}
