# HangmanOnline

![Screenshot 2023-03-04 122502](https://user-images.githubusercontent.com/73821401/222897252-c9f6c0c1-0943-41ce-91d9-bb9594784b9e.png)

HangmanOnline is a version of the classic game Hangman, but that can be played remotly from users.

### The structure

The system is designed as a Distributed System, that implements a client - server architecture.
The application realize a web-server in **Javalin**, that provides a set of **ReST API** consultable at the address */doc/ui* of the localhost.
All the API were documented with **Swagger** Plugin.


### Details

The application also have a test suite realized via **JUnit 5** and a report of the project is available in italian in the folder "doc".

### Deploy istruction

All the istructions for running the application are available in the report, in the section "Deploy Istruction".
