package main;

import java.util.Scanner;
import java.util.InputMismatchException;

public class PlayerMan {
    int id;
    String name;
    int score;
    boolean isActive;           
    boolean isTournament; 

    // Constructor
    public PlayerMan(int id, String name, int score, boolean isActive, boolean isTournament) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.isActive = isActive;
        this.isTournament = isTournament;
    }

    //Add Player
    public static PlayerMan[] addPlayer(PlayerMan[] oldArray, Scanner sc) {
    	// ID
        int id = getValidInt(sc, "Enter ID: ");
        
        // NAME
        System.out.print("Enter Name: ");
        String name = sc.next();
        
        // SCORE
        int score = getValidInt(sc, "Enter Score: ");
        
        // ACTIVE
        boolean isActive = getBooleanInput(sc, "Is Active? (y/n): ");
        
        // TOURNAMNET
        boolean isTournamentPlayer = getBooleanInput(sc, "Is Tournament Player? (y/n): ");
        
        
        //INCREASING THE SIZE OF THE ARRAY(copying old elements into new)
        PlayerMan[] newArray = new PlayerMan[oldArray.length + 1];
        for (int i = 0; i < oldArray.length; i++) {
            newArray[i] = oldArray[i];
        }

        //Adding new player at the end of the array
        newArray[newArray.length - 1] = new PlayerMan(id, name, score, isActive, isTournamentPlayer);
        System.out.println("Player added.");
        return newArray;
    }
    
    //Update data
    public static void updatePlayer(PlayerMan[] players, Scanner sc) {
        System.out.print("Enter ID to update: ");
        int id = sc.nextInt();
        sc.nextLine(); // Consume the leftover newline from nextInt()

        for (int i = 0; i < players.length; i++) {
            if (players[i].id == id) {
                // Update Name
                System.out.print("Enter New Name(Press Enter to skip) [" + players[i].name + "]: ");
                String newName = sc.nextLine();
                if (!newName.isEmpty()) {
                    players[i].name = newName;
                }

                // Update Score
                System.out.print("Enter New Score(Press Enter to skip) [" + players[i].score + "]: ");
                String newScoreStr = sc.nextLine();//we use nextLine here instead of nextInt because we need to raed /n here 
                if (!newScoreStr.isEmpty()) {
                    players[i].score = Integer.parseInt(newScoreStr);//parseInt is used to convert string to integer
                }
                
                // Active status
                System.out.print("Update Active Status? (y/n): ");
                String activeChoice = sc.nextLine().trim().toLowerCase();
                if (activeChoice.equals("y")) {
                    players[i].isActive = getBooleanInput(sc, "Is Active? (y/n): ");
                }
                
                // Tournament status
                System.out.print("Update Tournament Status? (y/n): ");
                String tourneyChoice = sc.nextLine().trim().toLowerCase();
                if (tourneyChoice.equals("y")) {
                    players[i].isTournament = getBooleanInput(sc, "Is Tournament Player? (y/n): ");
                }

                System.out.println("Update complete.");
                return;
            }
        }
        System.out.println("ID not found.");
    }

    //Delete PLayer
    public static PlayerMan[] deletePlayer(PlayerMan[] oldArray, Scanner sc) {
        System.out.print("Enter ID to delete: ");
        int id = sc.nextInt();
        
        //Searching for Index While Keeping -1 as default in case array is not found
        int index = -1;
        for (int i = 0; i < oldArray.length; i++) {
            if (oldArray[i].id == id) {
                index = i;
                break;
            }
        }

        //if id not found
        if (index == -1) {
            System.out.println("ID not found.");
            return oldArray;
        }
        
        //Decreasing the size of the array
        PlayerMan[] newArray = new PlayerMan[oldArray.length - 1];
        int k = 0;
        
        //Adding all elements except the selected id
        for (int i = 0; i < oldArray.length; i++) {
            if (i != index) {
                newArray[k++] = oldArray[i];
            }
        }

        System.out.println("Deleted.");
        return newArray;
    }
    
    //Display All
    public static void display(PlayerMan[] players) {
        if (players.length == 0) {
            System.out.println("List is empty.");
            return;
        }
        for (PlayerMan p : players) {
            System.out.println("ID: " + p.id
                    + " | Name: " + p.name
                    + " | Score: " + p.score
                    + " | Active: " + (p.isActive ? "Yes" : "No")
                    + " | Tournament: " + (p.isTournament ? "Yes" : "No"));
        }
    }
    
    //Display Active Players
    public static void displayActive(PlayerMan[] players) {
        System.out.println("--- Active Players ---");
        boolean found = false;
        for (PlayerMan p : players) {
            if (p.isActive) {
                System.out.println("ID: " + p.id + " | Name: " + p.name + " | Score: " + p.score
                        + " | Tournament: " + (p.isTournament ? "Yes" : "No"));
                found = true;
            }
        }
        if (!found) System.out.println("No active players.");
    }
    
    //Display Tournament Players
    public static void displayTournament(PlayerMan[] players) {
        System.out.println("--- Tournament Players ---");
        boolean found = false;
        for (PlayerMan p : players) {
            if (p.isTournament) {
                System.out.println("ID: " + p.id + " | Name: " + p.name + " | Score: " + p.score
                        + " | Active: " + (p.isActive ? "Yes" : "No"));
                found = true;
            }
        }
        if (!found) System.out.println("No tournament players.");
    }
    
    
    //*******************
    //ERROR HANDLING
    //*******************
    
    //(Integer Values)
    public static int getValidInt(Scanner sc, String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                return sc.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("Error: Enter a number.");
                sc.next();
            }
        }
    }
    
    //(for boolean values)
    public static boolean getBooleanInput(Scanner sc, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = sc.next().trim().toLowerCase();
            if (input.equals("y")) return true;
            if (input.equals("n")) return false;
            System.out.println("Please enter 'y' or 'n'.");
        }
    }
}