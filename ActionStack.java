package main;


public class ActionStack {

    // =========================================================================
    // PART 1 — Inner Node class
    // =========================================================================

    private static class Node {
        PlayerMan[] snapshot; // complete copy of the players array at that moment
        Node        below;    // the node that was pushed before this one

        Node(PlayerMan[] snapshot) {
            this.snapshot = snapshot;
            this.below    = null;
        }
    }

    // =========================================================================
    // PART 2 — Fields
    // =========================================================================

    private Node top;  // the most recently pushed node (top of the stack)
    private int  size; // how many snapshots are stored

    private static final int MAX_HISTORY = 20; // cap to avoid infinite memory use

    // =========================================================================
    // PART 3 — Constructor
    // =========================================================================

    public ActionStack() {
        top  = null;
        size = 0;
    }

    // =========================================================================
    // PART 4 — Core Stack Operations
    // =========================================================================

    
    public void push(PlayerMan[] players) {
        // Deep copy: create a new array and copy each PlayerMan reference
        PlayerMan[] snapshot = deepCopy(players);

        Node newNode = new Node(snapshot);
        newNode.below = top;  // new node sits on top of the current top
        top           = newNode;
        size++;

        // If we exceed the history limit, drop the oldest entry (bottom of stack)
        if (size > MAX_HISTORY) {
            trimBottom();
        }
    }

    public PlayerMan[] pop() {
        if (top == null) return null;

        PlayerMan[] snapshot = top.snapshot; // save the data to return
        top  = top.below;                    // move top pointer down one level
        size--;
        return snapshot;
    }

    public PlayerMan[] peek() {
        return (top == null) ? null : top.snapshot;
    }

    public boolean isEmpty() { return top == null; }
    public int     getSize() { return size; }

    public void clear() {
        top  = null;
        size = 0;
    }

    // =========================================================================
    // PART 5 — Helpers
    // =========================================================================

    private PlayerMan[] deepCopy(PlayerMan[] original) {
        PlayerMan[] copy = new PlayerMan[original.length];
        for (int i = 0; i < original.length; i++) {
            PlayerMan p = original[i];
            // Create a completely new PlayerMan with the same values
            copy[i] = new PlayerMan(p.id, p.name, p.score, p.isActive, p.isTournament);
        }
        return copy;
    }

    private void trimBottom() {
        if (top == null || top.below == null) {
            // 0 or 1 nodes — nothing to trim
            return;
        }
        Node current = top;
        while (current.below != null && current.below.below != null) {
            current = current.below;
        }
        current.below = null; // detach the last node
        size--;
    }

    // =========================================================================
    // PART 6 — Display (for debugging / status)
    // =========================================================================

    /**
     * Shows how many undo/redo states are currently stored.
     */
    public void printStatus(String label) {
        System.out.println("  " + label + ": " + size + " state(s) saved");
    }
}
