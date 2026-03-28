package main;
import java.util.Scanner;


public class Matchmakingqueue {

    // =========================================================================
    // PART 1 — Inner Node class
    // =========================================================================
    private static class Node {
        PlayerMan player; // the actual player object
        Node      next;   // pointer to the next person in line

        Node(PlayerMan player) {
            this.player = player;
            this.next   = null;
        }
    }

    // =========================================================================
    // PART 2 — Fields
    // =========================================================================

    private Node head;  // FRONT of the queue — dequeue happens here
    private Node tail;  // BACK  of the queue — enqueue happens here
    private int  size;  // how many players are currently waiting

    // =========================================================================
    // PART 3 — Constructor
    // =========================================================================

    public Matchmakingqueue() {
        head = null;
        tail = null;
        size = 0;
    }

    // =========================================================================
    // PART 4 — Core Queue Operations
    // =========================================================================

    //ADDING PLAYERS TO THE QUEE
    public void enqueue(PlayerMan player) {
        // Check if player is already in the queue
        Node check = head;
        while (check != null) {
            if (check.player.id == player.id) {
                System.out.println(player.name + " is already in the matchmaking queue.");
                return;
            }
            check = check.next;
        }

        Node newNode = new Node(player);

        if (tail == null) {
            // Queue was empty — new node is both front and back
            head = newNode;
            tail = newNode;
        } else {
            tail.next = newNode; // current last node points to new node
            tail      = newNode; // tail pointer moves to new node
        }
        size++;
        System.out.println(player.name + " has joined the matchmaking queue. (Position: " + size + ")");
    }

    //AFTER MATCHMAKING REMOVEING THEM FROM QUEUE+
    private PlayerMan dequeue() {
        if (head == null) return null; // queue is empty

        PlayerMan player = head.player; // save the front player
        head = head.next;               // move head one step forward

        if (head == null) {
            tail = null; // queue is now empty — reset tail too
        }
        size--;
        return player;
    }

    public PlayerMan peek() {
        return (head == null) ? null : head.player;
    }

    public boolean isEmpty() { return size == 0; }
    public int     getSize() { return size; }

    // =========================================================================
    // PART 5 — Remove a specific player from the queue (by ID)
    // =========================================================================

    public void removeFromQueue(int id) {
        if (head == null) {
            System.out.println("Queue is empty.");
            return;
        }

        // Special case: target is at the front
        if (head.player.id == id) {
            System.out.println(head.player.name + " has left the queue.");
            head = head.next;
            if (head == null) tail = null;
            size--;
            return;
        }

        // Walk the chain looking for the node before the target
        Node current = head;
        while (current.next != null) {
            if (current.next.player.id == id) {
                System.out.println(current.next.player.name + " has left the queue.");
                if (current.next == tail) {
                    tail = current; // the node being removed was the tail
                }
                current.next = current.next.next; // skip over the target
                size--;
                return;
            }
            current = current.next;
        }
        System.out.println("Player with ID " + id + " not found in queue.");
    }

    // =========================================================================
    // PART 6 — Display Queue
    // =========================================================================

    public void displayQueue() {
        System.out.println("\n=== MATCHMAKING QUEUE (" + size + " players waiting) ===");

        if (head == null) {
            System.out.println("Queue is empty.");
            System.out.println("=================================================");
            return;
        }

        Node current  = head;
        int  position = 1;

        while (current != null) {
            System.out.println(
                "  Position " + position + ": " +
                current.player.name +
                " (ID: " + current.player.id + ")" +
                " | Score: " + current.player.score
            );
            current = current.next;
            position++;
        }
        System.out.println("=================================================");
    }

    // =========================================================================
    // PART 7 — Start Matchmaking (pair up players FIFO)
    // =========================================================================
    public void startMatchmaking() {
        System.out.println("\n=== STARTING MATCHMAKING ===");

        if (size == 0) {
            System.out.println("No players in the queue.");
            System.out.println("============================");
            return;
        }

        if (size == 1) {
            System.out.println("Only 1 player in queue. Need at least 2 to start a match.");
            System.out.println("============================");
            return;
        }

        int matchNumber = 1;

        // Keep pairing while there are at least 2 players
        while (size >= 2) {
            PlayerMan player1 = dequeue(); // first player from front
            PlayerMan player2 = dequeue(); // second player from front

            System.out.println(
                "Match " + matchNumber + ": " +
                player1.name + " (Score: " + player1.score + ")" +
                "  VS  " +
                player2.name + " (Score: " + player2.score + ")"
            );
            matchNumber++;
        }

        // Handle leftover player
        if (size == 1) {
            System.out.println("\n" + peek().name + " is waiting — no opponent available this round.");
        }

        System.out.println("============================");
    }

    // =========================================================================
    // PART 8 — Menu interaction (called from Main)
    // =========================================================================

    public void joinQueue(PlayerMan[] players, Scanner sc) {
        if (players.length == 0) {
            System.out.println("No players registered. Add players first.");
            return;
        }

        // Show only active players as candidates
        System.out.println("\n--- Active Players Available for Matchmaking ---");
        boolean anyActive = false;
        for (PlayerMan p : players) {
            if (p.isActive) {
                System.out.println("  ID: " + p.id + " | Name: " + p.name + " | Score: " + p.score);
                anyActive = true;
            }
        }

        if (!anyActive) {
            System.out.println("No active players available for matchmaking.");
            return;
        }

        int id = PlayerMan.getValidInt(sc, "Enter Player ID to add to queue: ");

        // Find the player in the roster
        for (PlayerMan p : players) {
            if (p.id == id) {
                if (!p.isActive) {
                    System.out.println("Player is not active and cannot join matchmaking.");
                    return;
                }
                enqueue(p);
                return;
            }
        }
        System.out.println("Player ID not found.");
    }

    public void leaveQueue(Scanner sc) {
        if (isEmpty()) {
            System.out.println("Queue is already empty.");
            return;
        }
        displayQueue();
        int id = PlayerMan.getValidInt(sc, "Enter Player ID to remove from queue: ");
        removeFromQueue(id);
    }
}