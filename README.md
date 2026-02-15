# ATM-Interface

# 🏧 ATM Interface System

A realistic, Java-based desktop application that simulates the interface and functionality of an Automated Teller Machine (ATM). Built with **Java Swing**, this project features a unique UI design where the application controls are embedded directly into a high-quality ATM monitor background, creating an immersive banking experience.

## ✨ Key Features

* **Immersive UI Design:**
* **Realistic Overlay:** The application GUI is perfectly positioned and styled to fit inside the screen of a digital ATM background image.
* **Dynamic Assets:** Fetches the background image directly from the cloud, keeping the application lightweight.


* **Secure Authentication:**
* Login system validating User ID and PIN.
* Specific user profiles with pre-loaded balances.


* **Core Banking Operations:**
* **Withdraw Cash:** Validates sufficient funds before processing.
* **Deposit Cash:** Instantly updates account balance.
* **Fund Transfer:** Securely transfer money between registered users.
* **Balance Inquiry:** Check current available funds.


* **Transaction History:**
* Generates a mini-statement showing the date, type, amount, and balance for recent transactions.


* **Session Management:**
* Secure logout functionality to return to the welcome screen.



## 🛠️ Technology Stack

* **Language:** Java (JDK 8+)
* **GUI Framework:** Java Swing & AWT (Abstract Window Toolkit)
* **Networking:** `java.net` for loading remote background assets.
* **Data Structure:** `HashMap` for efficient in-memory user and account management.

## 🔑 Demo Credentials

Use the following test accounts to explore the system:

| User ID | PIN | Account Name | Initial Balance |
| --- | --- | --- | --- |
| **user1** | `1234` | Aditya Jadhav | ₹5,000.00 |
| **user2** | `5678` | Shivani Chauhan | ₹7,500.00 |
| **user3** | `9012` | Pranav Bahir | ₹3,200.00 |
| **admin** | `0000` | Administrator | ₹10,000.00 |

## 🚀 How to Run

1. **Clone the Repository:**
bash
git clone https://github.com/your-username/ATM-Interface-System.git



2. **Open in IDE:**
* Open the project folder in VS Code, IntelliJ IDEA, or Eclipse.


3. **Compile and Run:**
* Navigate to the directory containing the file.
* Compile the code:
bash
javac ATMSystemGUI_Fixed.java

* Run the application:
bash
java ATMSystemGUI_Fixed





* Note: An active internet connection is required for the application to load the ATM background image on startup.

## 📂 Project Structure

* `ATMSystemGUI_Fixed.java` - The complete source code containing:
* `BackgroundPanel`: Handles image rendering.
* `Bank`, `Account`, `Transaction`: Core banking logic.
* `ATMInterface`: The main GUI controller.
