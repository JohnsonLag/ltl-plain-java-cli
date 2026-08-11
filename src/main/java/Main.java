import java.util.Scanner;

public class Main {
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
        if (answer.toLowerCase().equals("q") ||
                answer.toLowerCase().equals("quit")
        ){
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        String run_mode = "interactive";
        String answer = "quit";
//        String[] command_arr = args.split(" ");
        Scanner scanner = new Scanner(System.in, System.getProperty("stdin.encoding"));

//        if (command_arr.length == 1){
        if (run_mode.toLowerCase().equals("interactive")){
//            run_mode = "interactive";

            do {
                System.out.println();
                Main.print_event_loop_dialogue("simple");
                System.out.print("\nInput: ");
                answer = scanner.nextLine();
            } while(Main.continue_or_quit(answer) && run_mode.equals("evaluate_and_exit") == false);

            scanner.close();
        }

        return;
    }
}
