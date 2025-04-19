# 📦 Fibonacci Heap

A Java implementation of a **Fibonacci Heap**, a special priority‑queue data structure that supports:

- 🔹 **O(1)** amortized insertion  
- 🔹 **O(1)** amortized decrease‑key  
- 🔹 **O(log n)** amortized removal of the minimum element  

## 📋 What It Does

- **insert(key)** – Adds a new key to the heap.  
- **removeMin()** – Removes and returns the smallest key, then restructures the heap.  
- **decreaseKey(node, newKey)** – Lowers the key of an existing node.  
- Internal operations: cut, cascadingCut, consolidate, and link to maintain heap properties.  
- Uses a circular, doubly‑linked root list and tree linking by degree.  

## 🚀 How to Run

1. Make sure you have **Java 17+** installed.  
2. Clone this repository and open it in your IDE or compile via command line.  
3. In your `src` folder, place **FibonacciHeap.java**.  
4. Compile and run the `main()` method in `FibonacciHeap.java`:


## 📞 Example Usage

```java
public static void main(String[] args) {
    FibonacciHeap heap = new FibonacciHeap();

    heap.insert(10);
    heap.insert(3);
    heap.insert(15);
    heap.insert(6);

   // → Minimum key: 3
   System.out.println("Minimum key: " + heap.getMin());

   // → Removed min: 3
   System.out.println("Removed min: " + heap.removeMin());

   // → New minimum key: 6
   System.out.println("New minimum key: " + heap.getMin());

}
```
