import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

import java.io.BufferedWriter;
import java.io.FileWriter;

public class Library {
    private Connection db;
    private ArrayList<Entry> search_results;

    public Connection getConnection(){
        return this.db;
    }

    public void setConnection(Connection db){
        this.db = db;
    }

    public void getLatestEntryId(){}

    public Entry getUrlResponse(Entry entry){
        return new Entry();
    }

    public void addEntries(String file_path, String file_type){
        // Open file.

        // Start for-loop.
        // Compare each line to URL regex.

        // Reject if fail and continue.

        // Document doc = jsoup(url);
        // String title = doc.title;
        // System.out.println(typeof(doc.body));

        // Store fields in db.
        // Entry entry = new Entry();
        // entry.setUrl(url);
        // entry.setTitle(title);
        // entry.setContent(content);

        // End for-loop.

        // Close file.
    }

    public ArrayList<Entry> searchLibrary(String query){
        // Connect to db.

        // MATCH(...) AGAINST

        // ArrayList<Entry> search_results = new ArrayList<Entry>(10);

        // Start for-loop.
        /*
        result = db_results.get(i);
        Entry entry = new Entry(result.entry_id, result.url, result.title, result.content, result.notes);
        search_results.add(entry);
         */

        // return search_results;
        return new ArrayList<Entry>();
    }

    public ArrayList<Entry> getSearchResults(){
        return search_results;
    }

    public void printSearchResults(){
        ArrayList<Entry> search_results = this.getSearchResults();

        // Start for-loop.
        /*
        entry = search_results.get(i);
        System.out.println( "Entry ID: " + entry.getEntryId() + "\n" +
                            "URL: "      + entry.getUrl()     + "\n" +
                            "Title: "    + entry.getTitle()   + "\n" +
                            "Content: "  + entry.getContent() + "\n" +
                            "Notes: "    + entry.getNotes()   + "\n\n"
        );
         */
    }

    public Entry getEntry(int entry_id){
        return new Entry();
    }

    public void viewEntry(int entry_id){}

    public void setEntry(int entry_id){}

    public void updateEntry(int entry_id){
        setEntry(entry_id);
    }

    public void deleteEntry(int entry_id){
        // Are you sure?
        // System.in
        // if (answer.toLowerCase().equals("y"))
            // DELETE FROM TABLE %s where entry_id = %s
    }

    public static void testFetch(){
        String testUrl = "https://en.wikipedia.org/wiki/Main_Page";
        String titleFileName = "fetched-title.txt";
        String bodyFileName = "fetched-body.txt";

        try (
                BufferedWriter titleFile = new BufferedWriter(new FileWriter(titleFileName));
                BufferedWriter bodyFile = new BufferedWriter(new FileWriter(bodyFileName))
        ){
            Document doc = Jsoup.connect(testUrl).get();
            String title = doc.title();

            // Page title.
            System.out.println(title);
            titleFile.write(title);

            // Get page body.
            Element body = doc.body();

            // Formatted text, with whitespace characters.
            String wholeBody = body.wholeText();
            bodyFile.write(wholeBody);

            System.out.println("Successfully wrote to the files.");
        } catch (IOException e) {
            System.out.println("Error writing files.");
        } finally {
            System.out.println("Ending test fetch.");
        }

    }

}
