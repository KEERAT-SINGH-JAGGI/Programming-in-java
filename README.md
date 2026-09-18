# CampusEats – College Canteen Management System

CampusEats is a console-based College Canteen Management System developed using Core Java. It allows users to view the canteen menu, place orders, manage order status, save order data, and generate sales reports.

The project demonstrates Core Java concepts including Object-Oriented Programming, Collections, Exception Handling, File Handling, Java Stream API, Lambda Expressions, and Multithreading.

## Features

- View available snacks and beverages
- Place customer orders
- Add multiple items and quantities
- Calculate order totals
- View and track orders
- Process orders using `ExecutorService`
- Save and load orders using CSV file handling
- Generate sales reports using Java Stream API
- Custom exception handling

## Technologies Used

- Core Java
- Java Collections
- Java NIO.2 (`Files`, `Path`)
- Java Stream API
- Lambda Expressions
- `ExecutorService`
- CSV file handling

## Requirements

- Java JDK 8 or later
- Terminal / Command Prompt

No external libraries, frameworks, databases, Maven, or Gradle are required.

## Project Structure

```text
Programming-in-java/
├── README.md
└── campus eats/
    ├── CafeApp.java
    ├── CafeException.java
    ├── MenuItem.java
    ├── Order.java
    └── Storage.java
```

### Main Files

- **`CafeApp.java`** – Main application, console menu, user interaction, order processing, and sales report.
- **`MenuItem.java`** – Contains the `MenuItem` abstraction, `Snack`, `Beverage`, and `Category`.
- **`Order.java`** – Contains `Order`, `OrderLine`, and `OrderStatus` for managing orders.
- **`CafeException.java`** – Contains custom exceptions used by the application.
- **`Storage.java`** – Handles saving and loading orders using CSV files and Java NIO.2.

## How to Run

### 1. Clone the repository

```bash
git clone https://github.com/KEERAT-SINGH-JAGGI/Programming-in-java.git
```

### 2. Enter the repository

```bash
cd Programming-in-java
```

### 3. Enter the Java project folder

```bash
cd "campus eats"
```

### 4. Compile the project

```bash
javac *.java
```

### 5. Run the application

```bash
java CafeApp
```

No additional configuration is required.

## Data Storage

The application uses `orders.csv` for storing saved order information.

The file is **not required to exist before running the application**. If it is missing, the application starts with an empty order history.

The file is created/updated when the application's **Save Orders** functionality is used.

`orders.csv` is runtime-generated data and does not need to be included in the source-code submission.

## Core Java Concepts Demonstrated

### Object-Oriented Programming
- Abstraction
- Encapsulation
- Inheritance
- Polymorphism

### Other Java Concepts
- Classes and Objects
- Enums
- Collections
- Custom Exceptions
- File I/O using NIO.2
- Stream API
- Lambda Expressions
- Multithreading with `ExecutorService`

## Important Runtime Note

Orders are saved only when the application's save functionality is used. Exiting the application without saving can result in unsaved orders being lost.

## Troubleshooting

If `javac` is not recognized, verify that a JDK is installed:

```bash
java -version
javac -version
```

If the application cannot be started, make sure you are inside the `campus eats` directory before compiling and running:

```bash
cd "campus eats"
javac *.java
java CafeApp
```

## Author

**Keerat Singh Jaggi**

B.Tech Computer Science and Engineering (AI/ML)
