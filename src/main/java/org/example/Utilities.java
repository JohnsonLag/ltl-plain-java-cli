package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;

public class Utilities {
    // Print to program log or both console and program log.
    private String user_log_type = "console_and_log";

    // On the console, print standard, quietly, or verbosely.
    private String user_log_amount = "standard";

    private String log_file_file_name;

    public String getUserLogType() {
        return user_log_type;
    }

    public void setUserLogType(String user_log_type) {
        if (Objects.equals(user_log_type, "standard") ||
                Objects.equals(user_log_type, "quiet") ||
                Objects.equals(user_log_type, "verbose")
        ){
            this.user_log_type = user_log_type;
        }

        else {
            System.out.println("Unrecognized user log type: " + user_log_type);

            // And print to log.
//             System.out.println("Unrecognized user log type: " + user_log_type);

        }
    }

    public String getUserLogAmount() {
        return user_log_amount;
    }

    public void setUserLogAmount(String user_log_amount) {
        this.user_log_amount = user_log_amount;
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
