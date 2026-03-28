package main;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        PlayerMan[]      players     = new PlayerMan[0];
        Leaderboard      leaderboard = new Leaderboard();
        Matchmakingqueue queue       = new Matchmakingqueue();
        ActionStack      undoStack   = new ActionStack(); // past states
        ActionStack      redoStack   = new ActionStack(); // future states (after undo)

        int mainChoice;

        do {
            printMainMenu();
            mainChoice = PlayerMan.getValidInt(sc, "Choose an option: ");
            System.out.println();

            switch (mainChoice) {

                // ==============================================================
                case 1: // Players Menu
                // ==============================================================
                    int playerChoice;
                    do {
                        printPlayersMenu(undoStack, redoStack);
                        playerChoice = PlayerMan.getValidInt(sc, "Choose an option: ");
                        System.out.println();

                        switch (playerChoice) {

                            // --------------------------------------------------
                            case 1: // Add Player
                            // --------------------------------------------------
                                // Step 1: save current state to undo history
                                undoStack.push(players);
                                // Step 2: wipe redo history (new action breaks redo chain)
                                redoStack.clear();
                                // Step 3: perform the action
                                players = PlayerMan.addPlayer(players, sc);
                                // Step 4: sync leaderboard
                                leaderboard.generateLeaderboard(players);
                                break;

                            // --------------------------------------------------
                            case 2: // Update Player
                            // --------------------------------------------------
                                undoStack.push(players);
                                redoStack.clear();
                                PlayerMan.updatePlayer(players, sc);
                                leaderboard.generateLeaderboard(players);
                                break;

                            // --------------------------------------------------
                            case 3: // Delete Player
                            // --------------------------------------------------
                                undoStack.push(players);
                                redoStack.clear();
                                players = PlayerMan.deletePlayer(players, sc);
                                leaderboard.generateLeaderboard(players);
                                break;

                            // --------------------------------------------------
                            case 4: // Display All
                            // --------------------------------------------------
                                PlayerMan.display(players);
                                break;

                            // --------------------------------------------------
                            case 5: // UNDO
                            // --------------------------------------------------
                                if (undoStack.isEmpty()) {
                                    System.out.println("Nothing to undo.");
                                } else {
                                    // Save current state so redo can come back to it
                                    redoStack.push(players);
                                    // Restore the previous state
                                    players = undoStack.pop();
                                    leaderboard.generateLeaderboard(players);
                                    System.out.println("Undo successful. Roster restored to previous state.");
                                    PlayerMan.display(players);
                                }
                                break;

                            // --------------------------------------------------
                            case 6: // REDO
                            // --------------------------------------------------
                                if (redoStack.isEmpty()) {
                                    System.out.println("Nothing to redo.");
                                } else {
                                    // Save current state so undo can come back
                                    undoStack.push(players);
                                    // Jump forward to the undone state
                                    players = redoStack.pop();
                                    leaderboard.generateLeaderboard(players);
                                    System.out.println("Redo successful. Roster moved forward.");
                                    PlayerMan.display(players);
                                }
                                break;

                            // --------------------------------------------------
                            case 0: // Back
                            // --------------------------------------------------
                                System.out.println("Returning to Main Menu...");
                                break;

                            default:
                                System.out.println("Invalid option. Try again.");
                        }
                        System.out.println();

                    } while (playerChoice != 0);
                    break;

                // ==============================================================
                case 2: // Leaderboard Menu
                // ==============================================================
                    int lbChoice;
                    do {
                        printLeaderboardMenu();
                        lbChoice = PlayerMan.getValidInt(sc, "Choose an option: ");
                        System.out.println();

                        switch (lbChoice) {
                            case 1:
                                leaderboard.displayTop5();
                                break;

                            case 2:
                                leaderboard.displayActive();
                                break;

                            case 0:
                                System.out.println("Returning to Main Menu...");
                                break;

                            default:
                                System.out.println("Invalid option. Try again.");
                        }
                        System.out.println();

                    } while (lbChoice != 0);
                    break;

                // ==============================================================
                case 3: // Matchmaking Menu
                // ==============================================================
                    int mmChoice;
                    do {
                        printMatchmakingMenu();
                        mmChoice = PlayerMan.getValidInt(sc, "Choose an option: ");
                        System.out.println();

                        switch (mmChoice) {
                            case 1:
                                queue.joinQueue(players, sc);
                                break;

                            case 2:
                                queue.leaveQueue(sc);
                                break;

                            case 3:
                                queue.displayQueue();
                                break;

                            case 4:
                                queue.startMatchmaking();
                                break;

                            case 0:
                                System.out.println("Returning to Main Menu...");
                                break;

                            default:
                                System.out.println("Invalid option. Try again.");
                        }
                        System.out.println();

                    } while (mmChoice != 0);
                    break;

                // ==============================================================
                case 0: // Exit
                // ==============================================================
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
            System.out.println();

       } while (mainChoice != 0);

        sc.close();
    }

    // --------------------------------------------------------------------------
    // Menu printers
    // --------------------------------------------------------------------------

    private static void printMainMenu() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║    PLAYER MANAGEMENT SYSTEM      ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. Players Menu                 ║");
        System.out.println("║  2. Leaderboard Menu             ║");
        System.out.println("║  3. Matchmaking Menu             ║");
        System.out.println("║  0. Exit                         ║");
        System.out.println("╚══════════════════════════════════╝");
    }

    private static void printPlayersMenu(ActionStack undoStack, ActionStack redoStack) {
        System.out.println("┌──────────────────────────────────┐");
        System.out.println("│          PLAYERS MENU            │");
        System.out.println("├──────────────────────────────────┤");
        System.out.println("│  1. Add Player                   │");
        System.out.println("│  2. Update Player                │");
        System.out.println("│  3. Delete Player                │");
        System.out.println("│  4. Display All Players          │");
        System.out.printf( "│  5. Undo (%2d state(s) available) │%n", undoStack.getSize());
        System.out.printf( "│  6. Redo (%2d state(s) available) │%n", redoStack.getSize());
        System.out.println("│  0. Back to Main Menu            │");
        System.out.println("└──────────────────────────────────┘");
    }

    private static void printLeaderboardMenu() {
        System.out.println("┌──────────────────────────────────┐");
        System.out.println("│        LEADERBOARD MENU          │");
        System.out.println("├──────────────────────────────────┤");
        System.out.println("│  1. Show Top 5                   │");
        System.out.println("│  2. Show Active Players          │");
        System.out.println("│  0. Back to Main Menu            │");
        System.out.println("└──────────────────────────────────┘");
    }

    private static void printMatchmakingMenu() {
        System.out.println("┌──────────────────────────────────┐");
        System.out.println("│        MATCHMAKING MENU          │");
        System.out.println("│     (FIFO Queue System)          │");
        System.out.println("├──────────────────────────────────┤");
        System.out.println("│  1. Join Queue                   │");
        System.out.println("│  2. Leave Queue                  │");
        System.out.println("│  3. View Queue                   │");
        System.out.println("│  4. Start Matchmaking            │");
        System.out.println("│  0. Back to Main Menu            │");
        System.out.println("└──────────────────────────────────┘");
    }
}