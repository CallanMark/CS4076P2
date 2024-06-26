package seanie.mark.cs4076p1server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import seanie.mark.cs4076p1server.exceptions.IncorrectActionException;

public class CS4076P1Server {


    private static ServerSocket servSock;
    private static final int PORT = 6558;


    public static void main(String[] args) {

        try{
            servSock = new ServerSocket(PORT);
        } catch(IOException e){
            System.out.println("Unable to attach port!");
            System.exit(1);
        }

        do{
            run();
        }while(true);
    }
    private static void run(){
        Socket link;

        try{
            link = servSock.accept();

            BufferedReader in = new BufferedReader(new InputStreamReader(link.getInputStream()));
            PrintWriter out = new PrintWriter(link.getOutputStream(), true);

            List<Module> currentModules = new ArrayList<>();
            
            boolean running = true;

            while(running) {

                String message = in.readLine();
                System.out.println("Message received: " + message);

                try {

                    String action = message.substring(0, 2);
                    String details = message.substring(2);

                    List<String> possibleActions = Arrays.asList("ac", "rc", "ds", "st");
                    if (!possibleActions.contains(action)) {
                        throw new IncorrectActionException("Incorrect action\n");
                    }

                    switch (action) {
                        case "ac" -> out.println(ActionHandler.addClass(details));
                        case "rc" -> out.println(ActionHandler.removeClass(details));
                        case "ds" -> out.println(ActionHandler.displaySchedule(details));
                        case "st" -> {
                            System.out.println("TERMINATE");
                            out.println("TERMINATE");
                            in.close();
                            out.close();
                            link.close();
                            running = false;
                        }
                    }
                } catch (IncorrectActionException e) {
                    out.println(e.getMessage());
                }
            }
        }catch(IOException e){
            System.out.println("Error when connecting to client" + Arrays.toString(e.getStackTrace()));
        }
    }
}

