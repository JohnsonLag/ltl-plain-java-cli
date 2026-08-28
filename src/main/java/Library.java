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
//			e.printStackTrace();
		} catch (IOException e) {
            System.out.println(e);
//            throw new RuntimeException(e);
        } catch (SQLException e) {
            System.out.println(e);
//            throw new RuntimeException(e);
        }
    }

    public ArrayList<Entry> searchLibrary(HashMap<String, String> hashMap){
        ArrayList<Entry> searchResults = new ArrayList<Entry>(10);
        Connection conn = this.getConnection();
        Entry entry = new Entry();
        PreparedStatement ps;

        String tableName = this.getTableName() + " ";
        String line = "SELECT entry_id, entry_url, entry_title, entry_body, entry_notes FROM " + tableName + "WHERE MATCH(entry_url, entry_title, entry_body, entry_notes) AGAINST (? IN NATURAL LANGUAGE MODE)";

        System.out.println("Retrieving results.");
        try {
            ps = conn.prepareStatement(line);
            ps.setString(1, hashMap.get("searchString"));

            ps.execute();
            ResultSet rs = ps.getResultSet();

            while (rs.next()){
                int entryId = rs.getInt("entry_id");
                String entryUrl = rs.getString("entry_url");
                String entryTitle = rs.getString("entry_title");
                String entryBody = rs.getString("entry_body");
                String entryNotes = rs.getString("entry_notes");

                searchResults.add(new Entry(entryId, entryUrl, entryTitle, entryBody, entryNotes));
            }

            System.out.println(searchResults.size() + " search result(s) found. Type and enter \"read results\" to view search result(s).");
            this.setSearchResults(searchResults);
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
        this.printEntries(searchResults);
    }

    public void printEntries(ArrayList<Entry> entries){
        if (entries == null){
            System.out.println("No entries to read.");
            return;
        }

        int size = entries.size();

        if (size <= 0){
            System.out.println("0 entries to read.");
            return;
        }

        for (Entry entry : entries){
            System.out.println(
                    "Entry ID: " + entry.getEntryId() + "\n" +
                    "URL: "      + entry.getEntryUrl()     + "\n" +
                    "Title: "    + entry.getEntryTitle()   + "\n" +
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

                manyResults.add(new Entry(entryId, entryUrl, entryTitle, entryBody, entryNotes));
            }

            System.out.println(manyResults.size() + " result(s) found.\n");
        } catch (SQLException e) {
            System.out.println(e);
//            throw new RuntimeException(e);
        }

        this.setManyEntries(manyResults);
        return manyResults;
    }

    public void printEntry(Entry entry){
        this.readEntry(entry);
    }

    public void readEntry(Entry entry){
		if (entry.getEntryId() == -1){
            System.out.println("Entry not found.");
            return;
        }

        System.out.println();
        System.out.println("Entry ID: " + entry.getEntryId());
        System.out.println("Entry URL: " + entry.getEntryUrl());
        System.out.println("Entry title: " + entry.getEntryTitle());
		System.out.println("Entry body:\n" + entry.getEntryBody());
		System.out.println("Entry notes:\n" + entry.getEntryNotes());
	}

    public void readEntryMinusBody(Entry entry){
        if (entry.getEntryId() == -1){
            System.out.println("Entry not found.");
            return;
        }

        System.out.println();
        System.out.println("Entry ID: " + entry.getEntryId());
        System.out.println("Entry URL: " + entry.getEntryUrl());
        System.out.println("Entry title: " + entry.getEntryTitle());
		System.out.println("Entry notes:\n" + entry.getEntryNotes());
	}

    public Entry getEntry(int entryId){
        Connection conn = this.getConnection();
        Entry entry = new Entry();

        System.out.println("Getting entry " + entryId);
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
            System.out.println(e);
//            throw new RuntimeException(e);
        }

        return entry;
    }

    // Update entry in database.
    public void setEntry(int entryId){
        Scanner scanner = new Scanner(System.in);
        Entry entry = this.getEntry(entryId);
        String answer = "";
        PreparedStatement ps = null;
        Connection conn = this.getConnection();

        System.out.println("Current entry:");
        this.readEntryMinusBody(entry);

        System.out.println();
        System.out.println("Editable fields: URL, title, notes.\n");

        System.out.print("Updated entry URL: ");
        String entryUrl = scanner.nextLine();

        System.out.print("Updated entry title: ");
        String entryTitle = scanner.nextLine();

        System.out.print("Updated entry notes:\n");
        String entryNotes = scanner.nextLine();

        System.out.println();
        System.out.println("After modifications:");
        System.out.println("Entry URL: " + entryUrl);
        System.out.println("Entry title: " + entryTitle);
        System.out.println("Entry notes:\n" + entryNotes);

        do {
            System.out.println();
            System.out.println("Save changes? Y/y for yes, N/n for no: ");
            answer = scanner.nextLine();

            try {
                if (answer.equalsIgnoreCase("y")){
                    String tableName = this.getTableName() + " ";
                    String line = "UPDATE " + tableName + "SET entry_url = ?, entry_title = ?, entry_notes = ? WHERE entry_id = ?";
                    ps = conn.prepareStatement(line);
                    ps.setString(1, entryUrl);
                    ps.setString(2, entryTitle);
                    ps.setString(3, entryNotes);
                    ps.setInt(4, entryId);

                    int result = ps.executeUpdate();

                    if (result == 0){
                        System.out.println("Could not execute UPDATE for entry " + entryId + ".");
                    } else if (result == 1){
                        System.out.println("Successful update for entry " + entryId + ".");
                    }
                } else if (answer.equalsIgnoreCase("n")){
                    System.out.println("Entry will not be updated.");
                    return;
                }
            } catch (SQLException e) {
                System.out.println(e);
//                throw new RuntimeException(e);
            }

        } while (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("n"));

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
                System.out.println(e);
//                throw new RuntimeException(e);
            }

        } while (!answer.equalsIgnoreCase("y") && !answer.equalsIgnoreCase("n"));

        scanner.close();
    }

    public void parseCreate(String line, String[] arr, HashMap<String, String> hashMap) {
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
            } else if (value.equalsIgnoreCase("many")){
                return "many";
            } else {
                return "unrecognized";
            }
        }
    }

	public void testConnection(){
		System.out.println("Testing database connection. Latest entry id: " + this.getLatestEntryId());
	}

    public void parseSearch(String line, HashMap<String, String> hashMap){
        String[] arr = line.split("\"");
        hashMap.put("searchString", arr[1]);
        return;
    }

    public void parseAndRun(String line, String[] arr){
        String firstWord = arr[0].toLowerCase();

        switch (firstWord) {
            case "q":
                break;
            case "c":
            case "create":
                HashMap<String, String> createHashMap = new HashMap<>();
                this.parseCreate(line, arr, createHashMap);
                this.createEntries(createHashMap);
                break;
            case "r":
            case "read":
                String readResult = Library.parseRead(line, arr);
                if (readResult.equalsIgnoreCase("search-results")) {
                    this.printSearchResults();
                } else if (readResult.equalsIgnoreCase("unrecognized")) {
                    System.out.println("Unrecognized read option: " + arr[1]);
                } else if (readResult.equalsIgnoreCase("many")) {
                    ArrayList<Entry> manyEntries = this.queryManyEntries();
                    this.printEntries(manyEntries);
                } else {
                    int readEntryId = Integer.parseInt(readResult);
                    Entry readEntry = this.getEntry(readEntryId);
                    this.readEntry(readEntry);
                }
                break;
            case "u":
            case "update":
                String updateResult = Library.parseRead(line, arr);
                try {
                    int updateEntryId = Integer.parseInt(updateResult);
                    this.updateEntry(updateEntryId);
                } catch (NumberFormatException e) {
                    System.out.println("Unrecognized update option: " + updateResult);
                }
                break;
            case "d":
            case "delete":
                String deleteResult = Library.parseRead(line, arr);
                try {
                    int deleteEntryId = Integer.parseInt(deleteResult);
                    this.deleteEntry(deleteEntryId);
                } catch (NumberFormatException e) {
                    System.out.println("Unrecognized delete option: " + deleteResult);
                }
                break;
            case "sl":
            case "search":
                HashMap<String, String> searchHashMap = new HashMap<>();
                this.parseSearch(line, searchHashMap);
                this.searchLibrary(searchHashMap);
                break;
            default:
                break;
        }
    }

}
