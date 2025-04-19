# Fibonacci Heap in Java

A simple Java implementation of a **Fibonacci Heap**, a priority queue that’s great for:
- Fast insertions
- Fast removal of the minimum element
- Fast decrease‑key operations

Useful for algorithms like Dijkstra’s shortest path.

---

## What’s in This Project?

- **FibonacciHeap.java**  
  - `insert(int key)` – add a new value  
  - `removeMin()` – remove and return the smallest value  
  - `decreaseKey(Node x, int newKey)` – lower the key of an existing node  
  - Internal helpers (`consolidate`, `cut`, `link`, etc.) to keep operations fast  

All code lives in the `edu.farmingdale` package and is fully commented.

---

## How It Works

- Nodes are in a circular, doubly linked list.
- Each node stores:
  - `key` (its value or priority)  
  - pointers to parent, one child, left/right siblings  
  - a `mark` flag used during decrease‑key  
- Removing the minimum “zips together” its children into the root list, then **consolidates** to merge trees of equal degree.

---

## Example Usage

```java
public static void main(String[] args) {
    FibonacciHeap heap = new FibonacciHeap();

    heap.insert(10);
    heap.insert(3);
    heap.insert(15);
    heap.insert(6);

    System.out.println("Min key: " + heap.getMin());       // 3
    System.out.println("Removed min: " + heap.removeMin()); // 3
    System.out.println("New min: " + heap.getMin());       // 6
}
```

---

## Requirements
- Java 8 or higher
-No external libraries (pure Java)

## Why Use a Fibonacci Heap?
- They’re mostly academic, but they demonstrate:
- Amortized analysis in data structures
-Key operations used in advanced graph algorithms
