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
import java.util.HashMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Library {
    private Connection db;
    private String infileName;
    private String tableName;
    private ArrayList<Entry> search_results;
    
    public static Connection getConnection(){
        return this.db;
    }
    
    public static void setConnection(Connection db){
        this.db = db;
    }

    public String getInfileName() {
        return infileName;
    }

    public void setInfileName(String infileName) {
        this.infileName = infileName;
    }

    public String getTableName(){
        return this.tableName;
    }

    public void setTableName(String tableName){
        this.tableName = tableName;
    }

    public int getLatestEntryId(){
		Connection conn = Library.getConnection();
		String line = "SELECT entry_id FROM ? ORDER BY entry_id DESC LIMIT 1";
		int entryId = 0;
		
		try {
			PreparedStatement ps = conn.prepareStatement(line);
			ps.setString(1, this.getTableName);
			ResultSet rs = ps.executeQuery();
			entryId = rs.getInt("entry_id");
		} catch (Exception e){
			// throw new RuntimeException(e);
			System.out.println(e);
		}
		
		return entryId;
		
	}

    public static Entry getUrlResponse(Entry entry){
        return new Entry();
    }

    public void createEntries(HashMap<String, String> hashMap){
		int entryId = 0;
		String entryUrl = "", entryTitle = "", entryBody = "";
		Connection conn = Library.getConnection();
		PreparedStatement ps;
		
        // Open file.
		String infileName = hashMap.get("infile");
		File infile = new File(infileName);
		
        // Start for-loop.
			// Compare each line to URL regex.
			// Reject if fail and continue.
		
		try (Scanner fileReader = new Scanner(infile)) {
			while (fileReader.hasNextLine()) {
				entryUrl = fileReader.nextLine();
				System.out.println(entryUrl);

				// Document doc = Jsoup.connect(entryUrl).get();

				// // Page title.
				// String title = doc.title();
				// System.out.println(title);

				// // Get page body.
				// // Formatted text, with whitespace characters.
				// Element body = doc.body();
				// String wholeBody = body.wholeText();
				
				entryId = this.getLatestEntryId();
				
				String line = "INSERT INTO ? " +
					"(entry_id, entry_url, entry_title, entry_body) " + 
					"VALUES(?, ?, ?, ?)";
				// ps = conn.prepareStatement(line);
				// ps.setInt(1, entryId);
				// ps.setString(2, entryUrl);
				// ps.setString(3, entryTitle);
				// ps.setString(4, entryBody);
				
				// int result = ps.executeUpdate();
				
				if (result == 0){
					System.out.println("Could not execute INSERT to create entry for url: " + entryUrl);
				}
			}
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
		} finally {
			ps.close();
			fileReader.close();
		}

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

    public Entry getEntry(int entryId){
		Connection conn = Library.getConnection();
        String line = "SELECT row_id, entry_url, entry_title, entry_body, entry_notes " +
			"FROM ? WHERE entry_id = ?";
		Entry entry = new Entry();
		
		try {
			PreparedStatement ps = conn.prepareStatement(line);
			
			ps.setString(1, this.getTableName());
			ps.setInt(2, entryId);
			
			ResultSet rs = ps.executeQuery();
			
			int rowId = rs.getInt("row_id");
			String entryId = rs.getString("entry_url");
			String entryId = rs.getString("entry_title");
			String entryId = rs.getString("entry_body");
			String entryId = rs.getString("entry_notes");
			
			entry.setRowId(rowId);
			entry.setEntryId(entryId);
			entry.setEntryUrl(entryUrl);
			entry.setEntryTitle(entryTitle);
			entry.setEntryBody(entryBody);
			entry.setEntryNotes(entryNotes);
			
		} catch (Exception e){
			// throw new RuntimeException(e);
			System.out.println(e);
		}	
		
		return new Entry();
    }

    public void readEntry(Entry entry){
		System.out.println("Entry ID: " + entry.getEntryId());
		System.out.println("Entry title: " + entry.getEntryTitle());
		System.out.println("Entry URL: " + entry.getEntryUrl());
		System.out.println("Entry body:\n" + entry.getEntryBody());
		System.out.println("Entry notes:\n" + entry.getEntryNotes());
	}

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

    public static HashMap<String, String> parseCreate(String line, String[] arr, HashMap<String, String> hashMap) {
        String keyword;
		String defaultInfileName = "links.txt";
        int i = 0, len = arr.length;

        hashMap.put("keyword", "create");
        hashMap.put("infile", defaultInfileName);

        for (i = 1; i < len; i++) {
            keyword = arr[i++];
            switch (keyword) {
                case "infile":
                    hashMap.replace(keyword, arr[i]);
                    break;
                default:
                    System.out.println("Unrecognized term: " + arr[1]);
                    break;
            }
        }

        return hashMap;
    }

    public static int parseRead(String line, String[] arr) {
        return arr[1];
    }
	
	public void testConnection(){
		System.out.println("Testing database connection. Latest entry id: " + this.getLatestEntryId());
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

    public static void parseAndRun(String line, String[] arr){
        String firstWord = arr[0].toLowerCase();

        switch (firstWord) {
            case "create":
                HashMap<String, String> createHashMap = Library.parseCreate(line, arr);
//                Library.createEntries(createHashMap);
                break;
            case "testfetch":
                Utilities.testFetch(); // up to date, not Library's
                break;
            case "get":
            case "read":
            case "view":
				int readEntryId = Library.parseRead(line, arr);
                Entry readEntry = Library.getEntry(readEntryId);
				Library.readEntry(readEntry);
                break;
            case "q":
                break;
            default:
                break;
        }
    }

}
