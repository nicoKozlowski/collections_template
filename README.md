# Functional Collections Template in Java

A comprehensive starter template and reference implementation for **Functional Programming (FP)** paradigms in Java. This repository contains purely functional, immutable, and side-effect-free implementations of core data structures, moving away from traditional imperative Java collections.

## 🚀 Key Features

All structures in this template are designed with **immutability**, **referential transparency**, and **pure functions** in mind:

*   **Functional Graphs:** Directed and undirected graphs implemented using immutable adjacency maps.
*   **Persistent Lists:** Singly linked structural lists supporting functional operations like `head`, `tail`, `map`, `filter`, and `fold`.
*   **Immutable Sets:** Mathematical set implementations with pure operations for union, intersection, and difference.
*   **Sorted Sets:** Ordered collections maintaining element sort order using functional balancing trees or sorted arrays under the hood.

## 🛠️ Tech Stack

*   **Language:** Java 17+ (utilizing modern features like Records, pattern matching, and sealed interfaces where applicable)
*   **Paradigm:** Pure Functional Programming (FP)

## 📋 Core Concepts Applied

1.  **Immutability:** Operations do not modify existing structures; they return a brand new version instead.
2.  **No Side Effects:** Functions depend solely on their input arguments.
3.  **High-Order Functions:** Heavy use of lambda expressions and functional interfaces (`Function`, `BiFunction`, `Predicate`).

## ⚙️ Getting Started

### Prerequisites
*   JDK 17 or higher
*   Maven or Gradle

### How to use this template
1. Click the **"Use this template"** button at the top of this GitHub repository.
2. Clone your newly created repository:
   ```bash
   git clone https://github.com
   ```
3. Explore the `src/main/java` directory to see the functional implementations and start building your own FP applications.
