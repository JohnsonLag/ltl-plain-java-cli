import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import java.io.File;                  // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors

public class Main {
    private Library library;
	
	public setLibrary(Library library){
		this.library = library;
	}
	
	public getLibrary(){
		return this.library;
	}
	
	private static String[] simple_event_loop_dialogue = {
            "1. quit (q)\n",
            "2. create input-file <file_name>\n",
            "3. read <entry_id>\n",
            "4. update <entry_id>\n",
            "5. delete <entry_id>\n",
            "6. search query \"<query>\" [output-to [file_name]] \n",
    };

    public static String[] getDialogue(String type){
        if (type.toLowerCase().equals("simple")){
            return simple_event_loop_dialogue;
        }
        return simple_event_loop_dialogue;
    }

    public static void print_event_loop_dialogue(String type){
        String [] dialogue = Main.getDialogue("simple");
        int i = 0;
        for(i =0; i < dialogue.length; i++){
            System.out.print(dialogue[i]);
        }
    }

    public static boolean continue_or_quit(String answer){
        if (answer.toLowerCase().equals("q")
                || answer.toLowerCase().equals("quit")
                || answer.equals("1")
        ){
            return false;
        }

        return true;
    }

    public static void printExitMessage(){
        System.out.println("Exiting program.");
    }

    public static void parseAndRun(String line, String[] arr){
        String firstWord = arr[0].toLowerCase();
        HashMap<String, String> hashMap;

        switch (firstWord) {
            case "create":
                hashMap = Library.parseCreate(line, arr);
				// this.getLibrary().createEntries(hashMap);
//                Library.createEntries(hashMap);
                break;
            case "testfetch":
                Utilities.testFetch();
                break;
            default:
                break;
        }
    }

    public static void main(String[] args) {
        String run_mode = "interactive";
        String answer = "quit";
        
        File envFile = new File(".env");
        Connection conn = null;
        Library ltl = new Library();
        String dbUrl = "", dbUser = "", dbPassword = "", defaultTable = "";
		
		Main.setLibrary(ltl);
		
		// Reading the .env file.
        try (Scanner envReader = new Scanner(envFile)) {

            // Get .env credentials.
            while (envReader.hasNextLine()){
                String line = envReader.nextLine();
                String[] arr = new String[2];
                arr = line.split("=");

                switch(arr[0]){
                    case "DB_URL":
                        dbUrl = arr[1];
                        break;
                    case "DB_USER":
                        dbUser = arr[1];
                        break;
                    case "DB_PASSWORD":
                        dbPassword = arr[1];
                        break;
                    case "DEFAULT_TABLE":
                        defaultTableName = arr[1];
                        break;
                    default:
                        System.out.println("Unrecognized option: " + arr[0]);
                        break;
                }
            }

            String connString = "" + dbUrl + "?" + "user=" + dbUser + "&password=" + dbPassword;
            conn = DriverManager.getConnection(connString);
            ltl.setConnection(conn);
            ltl.setTableName(defaultTableName);

        } catch (SQLException ex) {
            System.out.println("Could not connect to database.");

            String cleanedMessage = ex.getMessage();
            cleanedMessage = cleanedMessage.replaceAll(dbUrl, "DB_URL");
            cleanedMessage = cleanedMessage.replaceAll(dbUser, "DB_USER");
            cleanedMessage = cleanedMessage.replaceAll(dbPassword, "DB_PASSWORD");

            System.out.println("SQLException: " + cleanedMessage);
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
            Main.printExitMessage();
            return;
        } catch (FileNotFoundException e) {
            System.out.println("File not found.");
            Main.printExitMessage();
            throw new RuntimeException(e);
        }

		// Event loop.
        try (Scanner scanner = new Scanner(System.in, System.getProperty("stdin.encoding"))) {
            if (run_mode.toLowerCase().equals("interactive")) {
                do {
                    System.out.println();
                    Main.print_event_loop_dialogue("simple");
                    System.out.print("\nInput: ");
                    answer = scanner.nextLine();

                    String[] stringArr = answer.split(" ");
                    Library.parseAndRun(answer, stringArr);
                    
                } while (Main.continue_or_quit(answer) && run_mode.equals("evaluate_and_exit") == false);
            }
			
			else {
				// String[] command_arr = args.split(" ");
			}
        } catch (Exception ex) {
                System.out.println("Exception: " + ex);
        } finally {
            Main.printExitMessage();
        }

        return;
    }
}
