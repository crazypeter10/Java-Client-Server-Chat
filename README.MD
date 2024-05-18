# Java Multi-Client Chat Application

## Description
This chat application allows real-time communication between multiple clients through a central server. The application is built using Java and demonstrates the use of sockets, threads and GUIs.

## Technologies Used
- **Java Sockets**: For network communication between clients and server.
- **Swing**: For the graphical user interface.
- **ConcurrentHashMap**: For thread-safe management of clients connected to the server.
- **Thread**: Used to allow multiple client connections at the same time.
- **SimpleDateFormat**: To mark timestamps on messages.

## Features
- **Multi-client connection**: The server can simultaneously accept and manage multiple connections from clients.
- **Graphical interface**: Each client has a graphical interface through which it can send and receive messages.
- **Message Logging**: All messages are logged in a `log.txt` file with their timestamp.
- **Welcome Messages**: Every customer gets a welcome message when they join the chat.

## How to Roll
1. **Compile the code**:
```bash
   javac *.java
```
2. **Launch Server**:
```bash
java Server

```
3. **Open Multiple Clients**:
```bash
java Client
```

Repeat for each new Clients.