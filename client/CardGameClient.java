import java.io.*;
import java.net.*;
import java.util.Scanner;


public class CardGameClient {

    
    public CardGameClient(String serverAddress) throws Exception {
        try {
            String response;
            Socket s = new Socket("localhost", 59898);
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            BufferedReader brs = new BufferedReader(new InputStreamReader(s.getInputStream()));
            
            DataOutputStream toServer = new DataOutputStream(s.getOutputStream());
            
            System.out.println("List of commands:");
            System.out.println("Compare: Checks your value against opponent, will end the game");
            System.out.println("Hit: Get a card to add to your value");
            while (true) {
                System.out.println("input data:");
                String st = br.readLine();  //input
                System.out.println("transmitting to server...");
                toServer.writeBytes(st + "\n");     //Note: the \n is needed because the input in the server will recognize the last entry without it.
                //what is the server response?
                response = brs.readLine();   
                System.out.println("Server Response: " + response);
                if (response.equals("You Lose") || response.equals("Victory") || response.equals("Tie Game")) {
                    break;
                }
            }
        }
        catch (UnknownHostException e) {
        }
        catch (IOException e) {
        }
    }

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("Pass the server IP as the sole command line argument");
            return;
        }
        System.out.println("well something is happening");
        CardGameClient client = new CardGameClient(args[0]);

    }
}