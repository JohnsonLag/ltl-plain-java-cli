import java.sql.*;
import java.util.ArrayList;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

import java.util.HashMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Library {
    private Connection db;
    private String infileName;
    private String tableName;
    private ArrayList<Entry> searchResults;
    private ArrayList<Entry> manyEntries;

    public Connection getConnection(){
        return this.db;
    }
    
    public void setConnection(Connection db){
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
		Connection conn = this.getConnection();
        String tableName = this.getTableName() + " ";
		String line = "SELECT entry_id FROM " + tableName + "ORDER BY entry_id DESC LIMIT 1";
		int entryId = 0;
		
		try {
			PreparedStatement ps = conn.prepareStatement(line);

			ResultSet rs = ps.executeQuery();
			rs.next();

            entryId = rs.getInt("entry_id");
		} catch (Exception e){
			 System.out.println(e);
//			 throw new RuntimeException(e);
		}
		
		return entryId;
		
	}

    public static Entry getUrlResponse(Entry entry){
        return new Entry();
    }

    public void createEntries(HashMap<String, String> hashMap){
		Connection conn = this.getConnection();
		PreparedStatement ps;
		
        // Open file.
		String infileName = hashMap.get("infile");
		File infile = new File(infileName);

        System.out.println("Inserting entries.");

        try (Scanner fileReader = new Scanner(infile)) {
            while (fileReader.hasNextLine()) {
                String entryUrl = fileReader.nextLine();

                int entryId = this.getLatestEntryId() + 1;

                // Compare each line to URL regex.
                // Reject if fail and continue.

                // Get document attributes.
                Document doc = Jsoup.connect(entryUrl).get();
                String entryTitle = doc.title();

                Element body = doc.body();
                String entryBody = body.wholeText(); // Formatted text, with whitespace characters.

                String tableName = this.getTableName() + " ";
                String line = "INSERT INTO " + tableName +
					"(entry_id, entry_url, entry_title, entry_body) " + 
					"VALUES(?, ?, ?, ?)";
				 ps = conn.prepareStatement(line);
				 ps.setInt(1, entryId);
				 ps.setString(2, entryUrl);
				 ps.setString(3, entryTitle);
				 ps.setString(4, entryBody);
				
				 int result = ps.executeUpdate();
				
				if (result == 0){
					System.out.println("Could not execute INSERT to create entry for url: " + entryUrl);
				} else if (result == 1){
                    System.out.println("Successful insertion for url: " + entryUrl);
                }
			}
		} catch (FileNotFoundException e) {
			System.out.println(e);
//			System.out.println("An error occurred.");
//			e.printStackTrace();
		} catch (IOException e) {
            System.out.println(e);
//            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println(e);
//            throw new RuntimeException(e);
        }
    }

    public ArrayList<Entry> searchLibrary(String searchString){
        ArrayList<Entry> searchResults = new ArrayList<Entry>(10);
        Connection conn = this.getConnection();
        Entry entry = new Entry();
        PreparedStatement ps;

        String tableName = this.getTableName() + " ";
        String line = "SELECT entry_id, entry_url, entry_title, entry_body, entry_notes FROM " + tableName + "WHERE MATCH(entry_url, entry_title, entry_body, entry_notes) AGAINST (?) IN NATURAL LANGUAGE MODE)";

        System.out.println("Retrieving results.");
        try {
            ps = conn.prepareStatement(line);
            ps.setString(1, searchString);

            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()){
                int entryId = rs.getInt("entry_id");
                String entryUrl = rs.getString("entry_url");
                String entryTitle = rs.getString("entry_title");
                String entryBody = rs.getString("entry_body");
                String entryNotes = rs.getString("entry_notes");

                entry.setEntryId(entryId);
                entry.setEntryUrl(entryUrl);
                entry.setEntryTitle(entryTitle);
                entry.setEntryBody(entryBody);
                entry.setEntryNotes(entryNotes);

                searchResults.add(entry);
            }

            System.out.println(searchResults.size() + " result(s) found.");
        } catch (SQLException e) {
            System.out.println(e);
//            throw new RuntimeException(e);
        }

         return searchResults;
    }

    public ArrayList<Entry> getSearchResults(){
        return searchResults;
    }

    public void setSearchResults(ArrayList<Entry> searchResults){
        this.searchResults = searchResults;
    }

    public ArrayList<Entry> getManyEntries(){
        return manyEntries;
    }

    public void setManyEntries(ArrayList<Entry> manyResults){
        this.manyEntries = manyEntries;
    }

    public void printSearchResults(){
        ArrayList<Entry> searchResults = this.getSearchResults();

        for (Entry entry : searchResults){
            System.out.println(
                    "Entry ID: " + entry.getEntryId() + "\n" +
                    "URL: "      + entry.getEntryUrl()     + "\n" +
                    "Title: "    + entry.getEntryTitle()   + "\n" +
//                    "Content: "  + entry.getEntryBody() + "\n" +
                    "Notes: "    + entry.getEntryNotes()   + "\n\n"
            );
        }
    }

    public ArrayList<Entry> queryManyEntries(){
        ArrayList<Entry> manyResults = new ArrayList<Entry>(10);
        Connection conn = this.getConnection();
        Entry entry = new Entry();
        PreparedStatement ps;

        String tableName = this.getTableName() + " ";
        String line = "SELECT entry_id, entry_url, entry_title, entry_body, entry_notes FROM " + tableName + "LIMIT 50";

        System.out.println("Retrieving results.");
        try {
            ps = conn.prepareStatement(line);

            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()){
                int entryId = rs.getInt("entry_id");
                String entryUrl = rs.getString("entry_url");
                String entryTitle = rs.getString("entry_title");
                String entryBody = rs.getString("entry_body");
                String entryNotes = rs.getString("entry_notes");

                entry.setEntryId(entryId);
                entry.setEntryUrl(entryUrl);
                entry.setEntryTitle(entryTitle);
                entry.setEntryBody(entryBody);
                entry.setEntryNotes(entryNotes);

                manyResults.add(entry);
            }

            System.out.println(manyResults.size() + " result(s) found.");
        } catch (SQLException e) {
            System.out.println(e);
//            throw new RuntimeException(e);
        }

        this.setManyEntries(manyResults);
        return manyResults;
    }

    public void printManyEntries(){
        ArrayList<Entry> manyEntries = this.getManyEntries();

        for (Entry entry : manyEntries){
            System.out.println(
                    "Entry ID: " + entry.getEntryId() + "\n" +
                            "URL: "      + entry.getEntryUrl()     + "\n" +
                            "Title: "    + entry.getEntryTitle()   + "\n" +
//                    "Content: "  + entry.getEntryBody() + "\n" +
                            "Notes: "    + entry.getEntryNotes()   + "\n\n"
            );
        }
    }

    public static void readEntry(Entry entry){
		System.out.println("\nEntry ID: " + entry.getEntryId());
		System.out.println("Entry title: " + entry.getEntryTitle());
		System.out.println("Entry URL: " + entry.getEntryUrl());
		System.out.println("Entry body:\n\n" + entry.getEntryBody());
		System.out.println("\nEntry notes:\n\n" + entry.getEntryNotes());
	}

    public Entry getEntry(int entryId){
        Connection conn = this.getConnection();
        Entry entry = new Entry();

        try {

            tableName = this.getTableName() + " ";

            String line = "SELECT row_id, entry_url, entry_title, entry_body, entry_notes " + "FROM " + tableName + "WHERE entry_id = ?";
            PreparedStatement ps = conn.prepareStatement(line);

            ps.setInt(1, entryId);

            ResultSet rs = ps.executeQuery();
            rs.next();

            int rowId = rs.getInt("row_id");
            String entryUrl = rs.getString("entry_url");
            String entryTitle = rs.getString("entry_title");
            String entryBody = rs.getString("entry_body");
            String entryNotes = rs.getString("entry_notes");

            entry.setRowId(rowId);
            entry.setEntryId(entryId);
            entry.setEntryUrl(entryUrl);
            entry.setEntryTitle(entryTitle);
            entry.setEntryBody(entryBody);
            entry.setEntryNotes(entryNotes);

        } catch (Exception e){
            throw new RuntimeException(e);
        }

        return entry;
    }

    public void setEntry(int entryId){


    }

    public void updateEntry(int entryId){
        setEntry(entryId);
    }

    public void deleteEntry(int entryId){
        Connection conn = this.getConnection();
        Scanner scanner = new Scanner(System.in);
        String answer = "";
        PreparedStatement ps;

        do {
            System.out.println("Are you sure you want to delete entry " + entryId + "?");
            answer = scanner.nextLine();

            try {
                if (answer.equalsIgnoreCase("y")){
                    String tableName = this.getTableName() + " ";
                    String line = "DELETE FROM " + tableName + "WHERE entry_id = ?";
                    ps = conn.prepareStatement(line);
                    ps.setInt(1, entryId);

                    int result = ps.executeUpdate();

                    if (result == 0){
                        System.out.println("Could not execute DELETE for entry " + entryId + ".");
                    } else if (result == 1){
                        System.out.println("Successful deletion for entry " + entryId + ".");
                    }
                } else if (answer.equalsIgnoreCase("n")){
                    System.out.println("Entry will not be deleted.");
                    return;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } while (answer.equalsIgnoreCase("y") || answer.equalsIgnoreCase("n"));

        scanner.close();
    }

    public static void parseCreate(String line, String[] arr, HashMap<String, String> hashMap) {
        String keyword;
		String defaultInfileName = "links.txt";
        int i = 0, len = arr.length;

        hashMap.put("keyword", "create");
        hashMap.put("infile", defaultInfileName);

        for (i = 1; i < len; i++) {
            keyword = arr[i++];
            switch (keyword) {
                case "infile":
                case "input-file":
                    hashMap.replace(keyword, arr[i]);
                    break;
                default:
                    System.out.println("Unrecognized term: " + arr[1]);
                    break;
            }
        }
    }

    public static String parseRead(String line, String[] arr) {
        String value = arr[1];
        try {
            int resultId = Integer.parseInt(value);
            return resultId + "";
        } catch (Exception e) {
            if (value.equalsIgnoreCase("search") ||
                    value.equalsIgnoreCase("results") ||
                    value.equalsIgnoreCase("search-results")
            ){
                return "search-results";
            }
            return "unrecognized";
        }
    }
	
	public void testConnection(){
		System.out.println("Testing database connection. Latest entry id: " + this.getLatestEntryId());
	}

    public void parseAndRun(String line, String[] arr){
        String firstWord = arr[0].toLowerCase();

        switch (firstWord) {
            case "create":
            case "c":
                HashMap<String, String> createHashMap = new HashMap<>();
                Library.parseCreate(line, arr, createHashMap);
                this.createEntries(createHashMap);
                break;
            case "q":
                break;
            case "get":
            case "read":
            case "r":
            case "view":
                String readResult = Library.parseRead(line, arr);

                if (readResult.equalsIgnoreCase("search-results")) {
                    this.printSearchResults();
                } else if (readResult.equalsIgnoreCase("unrecognized")) {
                    System.out.println("Unrecognized read option: " + arr[1]);
                } else if (readResult.equalsIgnoreCase("all")) {
                    this.queryManyEntries();
                    this.printManyEntries();
                } else {
                    int readEntryId = Integer.parseInt(readResult);
                    Entry readEntry = this.getEntry(readEntryId);
                    Library.readEntry(readEntry);
                }
                break;
            case "testfetch":
            case "tfetch":
                Utilities.testFetch();
                break;
            case "latest-entry-id":
            case "latestid":
            case "lid":
                int id = this.getLatestEntryId();
                System.out.println("Latest entry id: " + id);
                break;
            default:
                break;
        }
    }

}
