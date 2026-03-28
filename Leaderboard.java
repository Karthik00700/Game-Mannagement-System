package main;


public class Leaderboard {
    private static class Node {
        String  name;
        int     score;
        boolean isActive;
        boolean isTournament;
        Node    next; 

        Node(String name, int score, boolean isActive, boolean isTournament) {
            this.name         = name;
            this.score        = score;
            this.isActive     = isActive;
            this.isTournament = isTournament;
            this.next         = null;
        }
    }
    
    PlayerMan[] topPlayersList = new PlayerMan[5];     //calling array of objects which is the data
    private Node head;  // start of the linked list

 
    public void generateLeaderboard(PlayerMan[] allPlayers) {

        // Step 1: Count eligible players
        int count = 0;
        //Counts total no of active and tournament players
        for (int i = 0; i < allPlayers.length; i++) {
            if (allPlayers[i].isActive && allPlayers[i].isTournament) {
                count++;
            }
        }
        
        //if count is 0 then it is returning the same entered array
        if (count == 0) {
            topPlayersList = new PlayerMan[5];
            head = null;
            return;
        }

        // Step 2: Store eligible players in a temporary array with count of both the players as size
        PlayerMan[] eligible = new PlayerMan[count];
        int index = 0;
        //adding players in temp array
        for (int i = 0; i < allPlayers.length; i++) {
            if (allPlayers[i].isActive && allPlayers[i].isTournament) {
                eligible[index++] = allPlayers[i];
            }
        }

     // Step 3: Selection Sort — descending by score
        for (int i = 0; i < eligible.length - 1; i++) {
            // Assume the current index has the maximum score
            int maxIndex = i;

            for (int j = i + 1; j < eligible.length; j++) {
                // Compare to find the actual maximum score in the remaining array
                if (eligible[j].score > eligible[maxIndex].score) {
                    maxIndex = j;
                }
            }

            // Swap the found maximum with the element at index i
            PlayerMan temp = eligible[maxIndex];
            eligible[maxIndex] = eligible[i];
            eligible[i] = temp;
        }

     // Step 4: Copy top 5 into topPlayersList
        topPlayersList = new PlayerMan[5];

        
        // this is used in case the number of player  less than 5
        int limit; 
        
        //if less than 5 enter the number of players available
        if (eligible.length < 5) {
            limit = eligible.length;
        }
        //else enter only the top 5 not other people from 6
        else {
            limit = 5;
        }
        //adding players into the list after sorting
        for (int i = 0; i < limit; i++) {
            topPlayersList[i] = eligible[i];
        }

        // Step 5: Build linked list from topPlayersList so display methods
        //can use it for traversal
        buildLinkedList();
    }

    private void buildLinkedList() {
        head = null;           // clear any old chain
        Node tail = null;      // we keep track of the last node so we can append

     
        for (int i = 0; i < topPlayersList.length; i++) {
            if (topPlayersList[i] == null) continue; // skip empty slots

            // Create a new node for this player
            Node newNode = new Node(
                topPlayersList[i].name,
                topPlayersList[i].score,
                topPlayersList[i].isActive,
                topPlayersList[i].isTournament
            );

            if (head == null) {
                // First node — it becomes both head and tail
                head = newNode;
                tail = newNode;
            } else {
                // Append to the end of the chain
                tail.next = newNode;   // old tail now points to new node
                tail = newNode;        // new node is the new tail
            }
        }
    }

    public void displayTop5() {
        System.out.println("\n=== TOP 5 TOURNAMENT LEADERBOARD ===");

        if (head == null) {
            System.out.println("No active tournament players found.");
            System.out.println("=====================================");
            return;
        }

        Node current = head;   // start at the beginning of the chain
        int rank = 1;

        while (current != null) {           // keep going until the chain ends
            System.out.println(
                rank + ". " + current.name +
                " | Score: " + current.score
            );
            current = current.next;         // move to the next link
            rank++;
        }
        System.out.println("=====================================");
    }

    public void displayActive() {
        System.out.println("\n=== ACTIVE PLAYERS IN LEADERBOARD ===");

        if (head == null) {
            System.out.println("Leaderboard is empty.");
            System.out.println("======================================");
            return;
        }

        Node    current = head;
        int     rank    = 1;
        boolean found   = false;

        while (current != null) {
            if (current.isActive) {          
            	
                String tournamentStatus;
                if (current.isTournament) {
                    tournamentStatus = "Yes";
                } else {
                    tournamentStatus = "No";
                }

                System.out.println(
                    rank + ". " + current.name +
                    " | Score: " + current.score +
                    " | Tournament: " + tournamentStatus
                );
                found = true;
            }
            current = current.next;
            rank++;
        }

        if (!found) System.out.println("No active players in leaderboard.");
        System.out.println("======================================");
    }

    public void displayTournament() {
        System.out.println("\n=== TOURNAMENT PLAYERS IN LEADERBOARD ===");

        if (head == null) {
            System.out.println("Leaderboard is empty.");
            System.out.println("==========================================");
            return;
        }

        Node    current = head;
        int     rank    = 1;
        boolean found   = false;

        while (current != null) {
            if (current.isTournament) {              
                String activeStatus;
                if (current.isActive) {
                    activeStatus = "Yes";
                } else {
                    activeStatus = "No";
                }

                System.out.println(
                    rank + ". " + current.name +
                    " | Score: " + current.score +
                    " | Active: " + activeStatus
                );
                found = true;
            }
            current = current.next;
            rank++;
        }

        if (!found) System.out.println("No tournament players in leaderboard.");
        System.out.println("==========================================");
    }
}
