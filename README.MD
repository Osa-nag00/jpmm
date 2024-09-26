# JAR Password Manager

## Work In Progress (WIP)

## Overview

The JAR Password Manager is a simple command-line application designed to securely manage your passwords and account information. With the ability to add, delete, and retrieve account credentials.

## Features

- **Store Passwords**: Save account credentials securely in a database.
- **Retrieve Passwords**: Easily fetch passwords and copy them to your clipboard.
- **Manage Accounts**: Add or delete accounts as needed.
- **Import/Export**: Import accounts from a Microsoft Passwords CSV file or export them to one.

## Getting Started

### Prerequisites

- Java 8 or higher
- Maven 3.6.3 (for building)

### Installation

1. Clone the repository:

   ```bash
   git clone <repository-url>
   cd <repository-directory>
   ```

2. Build the Project

   ```bash
   mvn clean package
   ```

3. Run the application

   ```bash
    java -jar target/jpmm-x.x.jar
   ```

### Usage

- Upon starting the application, you'll be prompted to enter the name of the account or a command.
- To retrieve a password, simply enter the account name.
- To manage your accounts, use the following commands:
  - `.A` or `.a` : Add a new account
  - `.D` or `.d` : Delete an existing account
  - `.I` or `.i` : Import accounts from a CSV file
  - `.E` or `.e` : Export accounts to a CSV file

#### Example

```less
Enter the Name of the account or enter a Command:
os
Which Accounts:
1: costco (username)
2: microsoft (username)
3: postman (username)
```

## win-start-script

Inside this folder is a bat script to run the any version of the jar. This is if you would like to run the application on windows and not have to open a terminal to run the jar. Create a shortcut the the .bat and search it through windows. The `jpmm.bat` script should go in the same directory as the `jpmm-x.x.jar`

## Folder Structure

The structure of the folder should look some like this:

```bash
jpmm/
   |--jpmm-x.x.jar
   |--jpmm.bat
   |--db/
       |--passwords.db
```
