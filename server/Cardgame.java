import java.io.*;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.Executors;



public class Cardgame {

    public static void main(String[] args) throws Exception {
        try (var listener = new ServerSocket(59898)) {
            System.out.println("Card game operational.");
            var pool = Executors.newFixedThreadPool(20);
            while (true) {
                Game game = new Game();
                pool.execute(game.new Player(listener.accept()));
            }
        }
    }
}

class Game {
    int playerValue = 0;
    int handvalue = 0;
    String[] deck = new String[52];
    
    public String[] deckSet() {
        deck[0] =  deck[1] = deck[2] = deck[3] = "A";
        deck[4] =  deck[5] = deck[6] = deck[7] = "2";
        deck[8] =  deck[9] = deck[10] = deck[11] = "3";
        deck[12] =  deck[13] = deck[14] = deck[15] = "4";
        deck[16] =  deck[17] = deck[18] = deck[19] = "5";
        deck[20] =  deck[21] = deck[22] = deck[23] = "6";
        deck[24] =  deck[25] = deck[26] = deck[27] = "7";
        deck[28] =  deck[29] = deck[30] = deck[31] = "8";
        deck[32] =  deck[33] = deck[34] = deck[35] = "9";
        deck[36] =  deck[37] = deck[38] = deck[39] = "10";
        deck[40] =  deck[41] = deck[42] = deck[43] = "J";
        deck[44] =  deck[45] = deck[46] = deck[47] = "Q";
        deck[48] =  deck[49] = deck[50] = deck[51] = "K";
        return deck;
    }

    class Player implements Runnable {
        Socket socket;
        Scanner input;
        PrintWriter output;
        String[] hand = new String[10];
        double a = 10 + (Math.random() * (21 - 10));
        int enemyChallengeValue = (int) a;
        
        //test
        //DataOutputStream outPlayer1;


        public Player(Socket socket) {
            this.socket = socket;
        }

        

        @Override
        public void run() {
            try {
                setup();
                processCommands();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    output.println("socket closing");
                     socket.close();
                } catch (IOException e){

                }
            }
        }
        private void setup() throws IOException {
            deckSet();
            input = new Scanner(socket.getInputStream());
            output = new PrintWriter(socket.getOutputStream(),true);
            //test
            //outPlayer1 = new DataOutputStream(socket.getOutputStream());
        }


        private void processCommands() {
            var command = "";
            while (input.hasNextLine()) {
                command = "";
                command = input.nextLine();

                System.out.println("processing commands");
                System.out.println("command was: " + command);
                
                if(playerValue > 21) {
                    output.println("You Lose");
                    return;
                }
                else if (command.startsWith("Hit")) {
                    processHit();
                    output.println("hand value: " + playerValue);
                    //try {
                    //    outPlayer1.writeBytes("something actually transferred");
                    //} catch (IOException e) {
                    //}
                }
                else if (command.startsWith("Compare")) {
                    if(playerValue > 21) {
                        output.println("You Lose");
                        return;
                    }
                    if(playerValue > enemyChallengeValue) {
                        output.println("Victory");   
                        return;
                    }
                    if(playerValue < enemyChallengeValue) {
                        output.println("You Lose");
                        return;
                    }
                    else {
                        output.println("Tie Game"); 
                        return;
                    }
                }
            }   
        }

        private void processHit() {
            try {
                    double randomNumber = 0 + (Math.random() * (51 - 0));
                    int randomNumberChanged = (int) randomNumber;
                    System.out.println(randomNumberChanged);
                    hand[handvalue] = deck[randomNumberChanged];
                    System.out.println("handvalue:" + handvalue);
                    System.out.println("the value gained at:" + randomNumberChanged);
                    System.out.println("inside is:" + deck[randomNumberChanged]);
                    checkHand();
                
            } catch (IllegalStateException e){
                output.println("Message " + e.getMessage());
            }
            handvalue++;
            //output.println("Current value of hand:" + playerValue);
        }
        
        
        private void checkHand() {
            playerValue = 0;
            for(int i = 0; i < hand.length - 1; i++) {
                System.out.println("checking hand at " + i + " which is " + hand[i]);
                if (hand[i] == "A") {
                    playerValue = playerValue + 1;
                }
                if (hand[i] == "2") {
                    playerValue = playerValue + 2;
                }
                if (hand[i] == "3") {
                    playerValue = playerValue + 3;
                }
                if (hand[i] == "4") {
                    playerValue = playerValue + 4;
                }
                if (hand[i] == "5") {
                    playerValue = playerValue + 5;
                }
                if (hand[i] == "6") {
                    playerValue = playerValue + 6;
                }
                if (hand[i] == "7") {
                    playerValue = playerValue + 7;
                }
                if (hand[i] == "8") {
                    playerValue = playerValue + 8;
                }
                if (hand[i] == "9") {
                    playerValue = playerValue + 9;
                }
                if (hand[i] == "10" || hand[i] == "J" || hand[i] == "Q" || hand[i] == "K") {
                    playerValue = playerValue + 10;
                }
            }
            System.out.println("current value of player hand: " + playerValue);

            /*if(playerValue > 21) {
                output.println("You Lose");
                try {
                    socket.close();
               } catch (IOException e){

               }
            }
            */
        }

    }

}