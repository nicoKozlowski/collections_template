# Java Functional Data Structures & Algorithms (Learning Repository)

This repository contains my personal implementations of core **Abstract Data Types (ADTs), Data Structures, and Algorithms in Java**, built strictly under a **pure functional programming (FP)** paradigm. 

The codebase served as an active, hands-on learning laboratory to transition away from traditional imperative styles and deeply master functional design principles.

## 🚀 Repository Purpose & Active Learning Philosophy
* **Active Learning Journey:** This repository represents **work-in-progress, exploratory learning**. It is a snapshot of my growth in functional programming.
* **Pragmatic Evolution:** In alignment with an active learning mindset, **not every single class is fully completed or optimized**. The focus was on architectural experimentation, grasping complex FP constraints, and conceptual mastery rather than delivering a production-ready library. 
* **Design & Imperfect Code:** Because this is a learning ground, you will find code that evolved over time. It is deliberately left authentic to showcase my problem-solving trajectory, architectural refactoring choices, and how I learn complex computational models.

---

## 🛠️ Included Topic Modules
The template repository covers fundamental computer science structures rebuilt from the ground up:
* **ADT List:** Functional singly linked list implementations in Java.
* **Stack & Queue:** Immutable, side-effect-free linear data structures.
* **ADT Set via ADT List:** Designing sets using list foundations under functional constraints.
* **Binary Search Trees (BST) & ADT SortedSet:** Tree-based structures utilizing pure functions without mutations.
* **ADT Map (ListSet & TreeSet):** Associative arrays mapping keys to values using functional underlying sets.
* **Graphs:** Functional representations of nodes, edges, and foundational graph traversals.
* **Functional Utilities (`fpinjava`):** Leverages specialized book-based classes (such as `Result`, optional monads, and persistent states) to handle errors and control flow safely without exceptions.

---

## ⚖️ Pure Functional Architecture (The Constraints)
To enforce strict algebraic correctness and immutable data pipelines, the entire codebase adheres to uncompromising architectural constraints. 

### Core Restrictions:
* 🚫 **No Imperative Control Flow:** Zero loops (`while`, `for`). All iterations are handled via recursion or higher-order functions (e.g., `fold`, `map`, `filter`).
* 🚫 **No State Mutations:** Zero mutation operators (`++`, `--`, `+=`, `-=`). 
* 🔒 **Absolute Immutability:** *All* variables and *all* data fields are explicitly declared `final`.
* 🔒 **Encapsulated Instantiation:** All class constructors are `private` and `final`, forcing instantiation through explicit static factory methods.
* 🚫 **No Side-Effects (Voidless):** No method returns `void`. Every operation is an expression that yields a deterministic value or transformation context.
* 🌿 **Total Functions:** Every `if` statement strictly demands an `else` branch (heavily favoring the ternary operator `? :`) to ensure all code execution paths return valid, predictable states.

---

## 🧠 The "Why": Trade-offs of Violating FP Constraints
A key part of this learning experience was understanding *why* these constraints matter. Violating these rules in a functional architecture introduces significant hidden costs:

| Constraint Violated | Resulting Disadvantage / Architectural Risk |
| :--- | :--- |
| **Using Loops & Mutations** (`while`, `for`, `++`, `+=`) | Introduces mutable state. Code becomes highly prone to **race conditions** in multithreaded environments. It breaks **referential transparency**, making debugging and mathematical verification significantly harder. |
| **Non-final Variables / Fields** | Allows data to be modified unexpectedly from the outside or across execution contexts, leading to **hidden side-effects** and tightly coupled code modules. |
| **Public/Non-final Constructors** | Exposes internal layout structures and breaks **encapsulation**, making it harder to enforce structural invariants (e.g., ensuring a tree always remains balanced upon creation). |
| **Methods returning `void`** | Signals a **side-effect** (e.g., modifying global state or I/O). It breaks functional composition because you cannot chain methods together if one returns nothing. |
| **Dangling `if` statements (No `else`)** | Leads to **partial functions** that are only defined for certain inputs. This introduces unpredictable implicit states (like `null` or uninitialized paths), breaking software reliability. |

---

## 📦 Dependencies & Build Information
* **Apache Commons Lang:** The testing helper class `list.JqwikUtils` requires the `SerializationUtils` class from the **Apache Commons Lang 3.9 API**.
* **Build System:** A `src/test/java/BUILD` file is included natively. If you are not utilizing **Bazel** as your build tool, this file can be safely deleted without impacting compilation.
