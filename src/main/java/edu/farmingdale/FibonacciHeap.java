package edu.farmingdale;

// A Fibonacci Heap is a type of priority queue that supports fast insertions,
// removal of the minimum element, and decrease-key operations.
// This implementation shows the basic methods: insert, removeMin, and decreaseKey.

import java.util.ArrayList;
import java.util.List;

public class FibonacciHeap {
    // Inner class representing a node in the Fibonacci Heap.
    // Each node stores its key (or priority), the number of children,
    // pointers to its parent and one of its children, and pointers to its left and right siblings.
    // The 'mark' flag is used to help manage the decreaseKey operation.
    private static class Node {
        int key;           // The value or priority stored in the node.
        int degree;        // The number of children this node has.
        Node parent;       // Pointer to the parent node.
        Node child;        // Pointer to one of its children (any child works).
        Node left;         // Pointer to the left sibling (in a circular list).
        Node right;        // Pointer to the right sibling (in a circular list).
        boolean mark;      // Flag to indicate if this node has lost a child.

        // Constructor: creates a new node with the given key.
        // Sets up the node as a circular list of one element.
        Node(int key) {
            this.key = key;
            this.degree = 0;
            this.parent = null;
            this.child = null;
            this.left = this;   // Initially, the node points to itself.
            this.right = this;
            this.mark = false;
        }
    }

    private Node min;  // Pointer to the node with the smallest key.
    private int n;     // Total number of nodes in the heap.

    // Constructor: creates an empty Fibonacci Heap.
    public FibonacciHeap() {
        min = null;
        n = 0;
    }

    // Inserts a new key into the Fibonacci Heap.
    // A new node is created and added to the root list.
    public void insert(int key) {
        Node node = new Node(key);
        // If the heap is empty, this node becomes the min.
        if (min == null) {
            min = node;
        } else {
            // Insert the new node into the circular doubly-linked list of roots.
            node.left = min;
            node.right = min.right;
            min.right.left = node;
            min.right = node;
            // Update the min pointer if this node's key is smaller.
            if (node.key < min.key) {
                min = node;
            }
        }
        n++;  // Increase the total number of nodes.
    }

    // Removes and returns the minimum key from the Fibonacci Heap.
    // After removing the minimum node, the heap is restructured (consolidated).
    public Integer removeMin() {
        Node z = min;  // z is the node with the smallest key.
        if (z != null) {
            // If z has any children, add them to the root list.
            if (z.child != null) {
                Node x = z.child;
                do {
                    Node next = x.right; // Save x's next sibling.
                    // Remove x from the child list.
                    x.left.right = x.right;
                    x.right.left = x.left;
                    // Add x to the root list.
                    x.left = min;
                    x.right = min.right;
                    min.right.left = x;
                    min.right = x;
                    // x is now a root; remove its parent pointer.
                    x.parent = null;
                    x = next;
                } while (x != z.child);
            }
            // Remove z from the root list.
            z.left.right = z.right;
            z.right.left = z.left;
            // If z was the only node, set the heap to empty.
            if (z == z.right) {
                min = null;
            } else {
                // Otherwise, set a new starting point and consolidate the heap.
                min = z.right;
                consolidate();
            }
            n--;  // Decrease the total number of nodes.
            return z.key;
        }
        return null;
    }

    // Decreases the key of node x to the new value newKey.
    // This is useful in many algorithms like Dijkstra's.
    // Assumes the caller has a reference to node x.
    public void decreaseKey(Node x, int newKey) {
        if (newKey > x.key) {
            // The new key must be smaller than the current key.
            throw new IllegalArgumentException("New key is greater than current key");
        }
        x.key = newKey;  // Update the key.
        Node y = x.parent;
        // If x is not a root and its new key is smaller than its parent's key,
        // cut x from its parent.
        if (y != null && x.key < y.key) {
            cut(x, y);
            cascadingCut(y);
        }
        // If the new key is the smallest, update the min pointer.
        if (x.key < min.key) {
            min = x;
        }
    }

    // Cuts node x from its parent y and moves x to the root list.
    // This is done when the heap order is violated.
    private void cut(Node x, Node y) {
        // Remove x from y's child list.
        if (x.right == x) {
            // x was the only child; set y.child to null.
            y.child = null;
        } else {
            // Remove x from the circular list of children.
            x.right.left = x.left;
            x.left.right = x.right;
            // If x was pointed to by y.child, update the pointer.
            if (y.child == x) {
                y.child = x.right;
            }
        }
        y.degree--;  // Decrease y's child count.
        // Add x to the root list.
        x.left = min;
        x.right = min.right;
        min.right.left = x;
        min.right = x;
        x.parent = null;  // x is now a root.
        x.mark = false;   // Reset the mark flag.
    }

    // Performs cascading cuts on node y.
    // If y has already lost a child (marked), cut it from its parent as well.
    private void cascadingCut(Node y) {
        Node z = y.parent;
        if (z != null) {
            if (!y.mark) {
                // Mark y if it hasn't lost a child before.
                y.mark = true;
            } else {
                // If y is already marked, cut it from its parent.
                cut(y, z);
                // Continue cascading the cut.
                cascadingCut(z);
            }
        }
    }

    // Consolidates the trees in the root list so that there is at most one tree per degree.
    // This step keeps the structure balanced and efficient.
    private void consolidate() {
        // Use the golden ratio (phi) to calculate an upper bound for the maximum degree.
        double phi = (1 + Math.sqrt(5)) / 2;
        int arraySize = n > 0 ? ((int) Math.floor(Math.log(n) / Math.log(phi))) + 2 : 1;
        Node[] A = new Node[arraySize];

        // Copy nodes from the root list into a temporary list.
        // This avoids issues when modifying the root list while iterating.
        List<Node> nodes = new ArrayList<>();
        if (min != null) {
            Node current = min;
            do {
                nodes.add(current);
                current = current.right;
            } while (current != min);
        }

        // Merge trees with the same degree.
        for (Node w : nodes) {
            Node x = w;
            int d = x.degree;
            while (A[d] != null) {
                Node y = A[d];
                // Make sure x has the smaller key.
                if (x.key > y.key) {
                    Node temp = x;
                    x = y;
                    y = temp;
                }
                // Merge tree y into tree x.
                link(y, x);
                A[d] = null;
                d++;
                if (d >= A.length) {
                    throw new RuntimeException("Consolidate: degree exceeded array size");
                }
            }
            A[d] = x;
        }

        // Rebuild the root list from the array A and update the min pointer.
        min = null;
        for (int i = 0; i < A.length; i++) {
            if (A[i] != null) {
                // Make the tree's root a single-node circular list.
                A[i].left = A[i];
                A[i].right = A[i];
                if (min == null) {
                    min = A[i];
                } else {
                    // Insert A[i] into the root list.
                    A[i].left = min;
                    A[i].right = min.right;
                    min.right.left = A[i];
                    min.right = A[i];
                    // Update min if this node has a smaller key.
                    if (A[i].key < min.key) {
                        min = A[i];
                    }
                }
            }
        }
    }

    // Links node y to node x by making y a child of x.
    // This method is used during consolidation to merge trees with the same degree.
    private void link(Node y, Node x) {
        // Remove y from the root list.
        y.left.right = y.right;
        y.right.left = y.left;
        // Set x as y's parent.
        y.parent = x;
        if (x.child == null) {
            // If x has no children, y becomes the first child.
            x.child = y;
            y.left = y;
            y.right = y;
        } else {
            // Otherwise, insert y into x's circular child list.
            y.left = x.child;
            y.right = x.child.right;
            x.child.right.left = y;
            x.child.right = y;
        }
        x.degree++;    // Increase the number of children of x.
        y.mark = false; // Reset y's mark flag.
    }

    // Helper method for testing: returns the minimum key in the heap.
    public Integer getMin() {
        return (min != null) ? min.key : null;
    }

    // Main method to demonstrate the usage of the Fibonacci Heap.
    public static void main(String[] args) {
        FibonacciHeap heap = new FibonacciHeap();

        // Insert some keys into the heap.
        heap.insert(10);
        heap.insert(3);
        heap.insert(15);
        heap.insert(6);

        // Print the minimum key (expected output: 3).
        System.out.println("Minimum key: " + heap.getMin());

        // Remove the minimum key and print it.
        System.out.println("Removed min: " + heap.removeMin());
        // Print the new minimum key after removal.
        System.out.println("New minimum key: " + heap.getMin());

    }
}
