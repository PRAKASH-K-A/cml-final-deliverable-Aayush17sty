## MANIPAL INSTITUTE OF TECHNOLOGY

## MANIPAL – 576104

## DEPARTMENT OF COMPUTER SCIENCE & ENGG.

## CERTIFICATE

This is to certify that Ms./Mr. .................................................................. Reg.
No............................... Section: .................. Roll No..............................has satisfactorily
completed the lab exercises prescribed for Capital Market Technology Lab [CSF 2241] of Third
Year B. Tech. Degree at MIT, Manipal, in the academic year 2025-2026.

Date: .........................................

```
Signature Signature
Faculty in Charge Head of the Department
```

## LAB NO. TITLE PAGE NO. REMARKS

```
Lab 1
```
```
Lab Essentials and Environment Setup
(Installation of Java 8+, MySQL, QuickFIX/J,
MiniFix Simulator, and Angular Environment)
```
```
Lab 2
```
```
FIX Protocol Connectivity and Microservice
Skeleton
(Creating the initial Order Service to accept FIX
4.4 connections)
```
```
Lab 3
```
```
High-Volume Message Ingestion
(Handling NewOrderSingle messages and designing
for high-throughput message generation)
```
```
Lab 4
```
```
Real-Time Frontend Integration (Angular)
(Building the UI and rendering data received via
WebSockets)
```
```
Lab 5
```
```
Data Persistence and Audit Trail
(Pushing Order/Trade data to the MySQL
Database for permanent storage)
```
```
Lab 6
```
```
Domain Modeling and Schema Optimization
(Refactoring Entity classes (Orders, Executions)
and finalizing the DB Schema)
```
```
Lab 7
```
```
The Matching Engine: Core Algorithms
(Implementing the Logic to match Buy vs. Sell
orders based on Price/Time priority)
```
```
Lab 8
```
```
Execution Reporting and Feedback Loops
(Generating Execution Reports and updating the
Front End screen with Trade confirmations)
```
```
Lab 9
```
```
Performance Engineering and Telemetry
(Measuring latency and the ability to handle 500K
orders/2M trades)
```

**Lab 10**

```
System Resilience and Disruption Handling
(Testing fault tolerance and active/passive options
for service failures)
```
**Lab 11**

```
Quantitative Finance: Black-Scholes
Implementation
(Simulating option pricing based on equity
executions and reference data)
```
**Lab 12**

```
Capstone: System Integration and Final
Demonstration
(Hosting the complete Microservices architecture
for a live end-to-end demo)
```

**1. Introduction**

The Capital Market Technology Lab is an advanced systems architecture course designed to bridge
the gap between Computer Science and Financial Technology (FinTech). In this course, students
will build a simulation of a "plain vanilla" trading application capable of handling high-frequency
transactions, specifically scaling to support 500,000 orders and 2 million trades.

Moving beyond simple coding exercises, this lab focuses on constructing a cloud-native
microservices architecture. Students will engineer a complete Order Management System (OMS)
that receives electronic orders via industry-standard protocols, matches Buy/Sell orders using low-
latency algorithms, and visualizes real-time financial data. The semester culminates in the
implementation of a quantitative finance module, where students apply the Black-Scholes model
to price options based on live trade executions.

**2. Lab Objectives**

The primary objectives of this laboratory course are:

- **Scalable Architecture Design** : To develop a robust system capable of ingesting high-
    volume data streams without data loss or significant latency.
- **Protocol Implementation** : To implement the Financial Information eXchange (FIX) 4.
    protocol for electronic message exchange, integrating a client-side system with a backend
    service.
- **Algorithmic Processing** : To design responsive algorithms for order matching (matching
    engine) and mathematical modeling for option pricing.
- **Full-Stack Integration** : To create a seamless data pipeline connecting a Java-based
    backend, a persistent MySQL database store, and a responsive Angular user interface.

# 3. Technology Stack

# Category Technologies/Tools Notes

```
Operating
```
# System

# CentOS 9 / RedHat 9 Linux^ environment^ for^ server-side^

```
stability.
Backend
```
# Language Core^ Java^8 (or^ higher)^

```
Used for microservices and the
QuickFIX/J engine.
```

```
Frontend
Framework Angular^
```
```
For building a responsive User
Interface.
```
# Database MySQL Persistent^ store^ trailsfor^ transaction. audit^

# Protocols FIX^ 4.4^ (QuickFIX/J),REST^ WebSockets,^

```
QuickFIX/J for order exchange;
WebSockets for real-time UI
updates.
Simulation
Tools
```
```
MiniFix (or similar) Used^ to^ generate^ electronic^ orders^
and order modifications.
IDE Eclipse / Visual Studio Integrated^ Development^
Environment.
```
**4. Learning Outcomes**

By the end of the lab, you will be able to:

- **Design Microservices** : Architect and deploy fault-tolerant microservices that manage the
    complete lifecycle of a financial order.
- **Data Modeling** : Model complex financial entities (Orders, Executions) and optimize
    database schema for high-performance storage and retrieval.
- **Real-Time Processing** : Handle latency-sensitive real-time data using WebSockets and
    efficient memory management.
- **Domain Compliance** : Apply the specific nuances of the FIX protocol and financial
    mathematical models (Black-Scholes) to software implementation.
- **System Telemetry** : Implement logging and telemetry to monitor performance, errors, and
    troubleshooting in a high-throughput environment.


# Instructions for the Capital Market Technology Lab

**Pre-Lab Session Guidelines**

1. Ensure you carry your Capital Market Technology Lab Manual and required stationery to
    each session.
2. Arrive on time and adhere to the institution’s dress code.
3. Sign the Lab Attendance Register promptly upon arrival.
4. Sit at your assigned system and confirm your attendance as instructed.
5. Maintain lab discipline and always observe professional behavior.
6. Review the assigned module or exercise beforehand to come prepared for implementation
    and discussion.

**During the Lab Session**

1. Follow the instructions provided for each assigned lab activity precisely.
2. Upon completion of your exercise, demonstrate the working code and output to the faculty-
    in-charge for verification.
3. After approval, neatly copy the program, inputs/outputs, and observations into your lab
    record book.
4. Keep prescribed textbooks, lab manuals, or reference notes accessible for quick
    consultation when necessary.

**General Instructions for Lab Exercises**

1. All exercises must be completed individually. Group coding or code sharing is strictly
    prohibited.
2. Your lab observation record must include:
    o Clear and structured code
    o Input and output data


```
o Evidence of API responses or system behavior (where applicable)
```
3. Plagiarism, code duplication, or copying from others will result in academic penalties.
4. Exercises are categorized into:
    o Solved Examples – reference material explained during lab
    o Assigned Lab Tasks – to be completed during the session
    o Additional Exercises – for practice outside lab hours
5. If a session is missed, obtain faculty approval for a repeat session. Note: Credit is awarded
    only for the original scheduled lab unless permitted by exception.
6. Lab tests and examinations may include new or modified versions of exercises beyond
    those listed in the manual.

**Prohibited Activities**

1. Do not bring mobile phones or personal electronic gadgets into the lab unless explicitly
    authorized.
2. Do not leave the lab without permission from the faculty or lab instructor.

# Evaluation Plan (Total: 100 Marks)

- **Continuous Assessment** : 85 marks
- **Lab Viva** : 15 marks

**Module-wise Assessment**

```
LA
B
NO.
```
```
TITLE Marks REMARK
S
```
```
Evaluation checklist
```
```
Lab
1
```
```
Lab Essentials and
Environment Setup
(Installation of Java 8+,
MySQL, QuickFIX/J, MiniFix
Simulator, and Angular
Environment)
```
**Nil** (^) **Java 8+, MySQL, Node.js, Angular CLI, and QuickFIX/J
installed without errors
Environment variables (JAVA_HOME, PATH) configured
correctly
QuickFIX/J JARs recognized by IDE/build system
EnvCheck.java runs successfully and initializes QuickFIX/J**


**Lab
2**

```
FIX Protocol Connectivity
and Microservice Skeleton
(Creating the initial Order
Service to accept FIX 4.
connections)
```
(^6) **FIX configuration file (order-service.cfg) loads correctly
Fix Gateway Order Service Application interface methods
implemented (onLogon, onLogout, fromApp, etc.)
Fix Gateway Order Service starts as a FIX Acceptor
without runtime exceptions
MiniFix successfully establishes Logon + Heartbeat session
Fix Gateway Order Service starts as a FIX Acceptor
without runtime exceptions
Lab
3
High-Volume Message
Ingestion**
_(Handling NewOrderSingle
messages and designing for
high-throughput message
generation)_
10 Key Design
decisions
**NewOrderSingle FIX messages parsed correctly (Tags 11,
54, 38, 55, 44) and passed to Order Service
Invalid orders (negative qty/price) correctly rejected
ExecutionReport(35=8 / New) sent back for valid orders
Internal Order POJO and lightweight in-memory store
(thread-safe) for parsed orders.
Application remains responsive under 10–20 orders/sec
stress test
Lab
4
Real-Time Frontend
Integration (Angular)**
_(Building the UI and rendering
data received via WebSockets)_
(^8) **WebSocket server runs and accepts UI connections
Angular WebSocket service receives JSON messages
without errors
Live Order Grid updates automatically on incoming orders
End-to-end flow works: FIX → OrderService → WebSocket
→ Angular UI
Lab
5
Data Persistence and Audit
Trail**
_(Pushing Order/Trade data to
the MySQL Database for
permanent storage)_
(^5) **orders table created with correct schema
Asynchronous queue used (no DB operations inside FIX
callback) decoupled ingestion from storage
All order attributes persisted correctly
No slowdown observed during bursts of 100+ orders
Lab
6
Domain Modeling and
Schema Optimization**
_(Refactoring Entity classes
(Orders, Executions) and
finalizing the DB Schema)_
(^10) **Normalized & optimized schema for Orders, Executions,
Security Master, Customer Master.
Refactored Java domain models aligned with database
schema (Order, Execution, Security, Customer).**
(^) **of symbols.In-memory Security Master preload for ultra - fast validation
Foundational Order Book structure (preparing for matching
logic)
Lab
7
The Matching Engine: Core
Algorithms**
15 Key
containers,
data
structures,
**Price-Time Priority matching implemented correctly
Partial and multi-level fills handled accurately
OrderBook cleans up empty price levels
Trade events generated with correct quantity and trade**


```
(Implementing the Logic to
match Buy vs. Sell orders
based on Price/Time priority)
```
```
memory
manageme
nt decisions
```
**price(Trade must occur at resting order price, not aggressor price)** (^)
**Lab
8
Execution Reporting and
Feedback Loops**
_(Generating Execution
Reports and updating the
Front End screen with Trade
confirmations)_
(^5) **ExecutionReport (Trade) constructed with correct FIX
fields
Trade records persisted asynchronously to executions table
UI shows trade updates in real time (trade blotter / ticker)
Both FIX client and UI receive consistent execution data.
Complete trading loop achieved: Order → Match → Trade → Persist → UI/FIX notifications.Complete trading loop achieved:
Order → Match → Trade → Persist → UI/FIX notifications.
Lab
9
Performance Engineering
and Telemetry**
_(Measuring latency and the
ability to handle 500K
orders/2M trades)_
(^5) **Tick-to-Trade latency measured with nano-precision
timestamps
PerformanceMonitor prints averaged latency per 1000
orders
10,000-order run completed without crashes
VisualVM analysis done: GC behavior, CPU hotspots,
memory growth
Lab
10
System Resilience and
Disruption Handling**
_(Testing fault tolerance and
active/passive options for
service failures)_
6 This one is
tricky. If
successfull
y
implemente
d, this
should
fetch at
least 10
marks but
considering
the
difficulty
not
everyone
may be
able to
achieve
this. Need
inputs from
faculty to
really
suggest
here.
**FIX session settings prevent sequence resets on reconnect
Application recovers after network failure or forced kill
ResendRequest (35=2) triggered when sequence gap
detected
Database reconnect logic handles transient outages
gracefully
Order service disruption by killing the main order service
and peer or passive taking over
Lab
11
Quantitative Finance: Black-
Scholes Implementation**
_(Simulating option pricing
based on equity executions
and reference data)_
(^5) **Black Scholes function implemented and tested with known
values
System recalculates option price on each trade event
Angular UI receives and visualizes option updates in real
time
Option price adjusts correctly as spot price changes**


**Lab
12**

```
Capstone: System
Integration and Final
Demonstration
(Hosting the complete
Microservices architecture for
a live end-to-end demo)
```
```
10 Effective
demonstrati
on with test
data that
would
really bring
out the
efforts put
in.
```
1. **Fully deployed 3-tier microservice system** : FIX
    gateway → OMS → DB → Angular UI.
2. **Complete demo workflow** : orders, matches, trades,
    option pricing, persistence.
3. **Scalability demo** : thousands of orders with stable
    memory and acceptable latency.
4. **Final submission package** : source code, configs, DB
    schema, and consolidated lab report.


## LAB 1: LAB ESSENTIALS AND ENVIRONMENT SETUP

## 1. OBJECTIVE

The objective of this foundational session is to establish the complete software and hardware
ecosystem required for the **Capital Market Technology Lab**. Unlike standard software labs, a
trading system requires a specific robust configuration involving a high-performance database, a
low-latency messaging engine, and a Linux-based server environment.
In this lab, you will configure a **CentOS/RedHat** workstation to serve as the host for a
microservices architecture. You will install and verify the **MySQL** persistence layer, the **Java**
development environment for backend services, the **Angular** framework for the frontend, and the
**QuickFIX/J** engine for financial communication. Success in this lab is critical; an improperly
configured environment will lead to latency issues and build failures in later sessions.

**2. PREREQUISITES**
    - **OS Access:** Root/Administrator access to a **CentOS 9** or **RedHat 9** environment.
    - **Knowledge:** Proficiency with Linux terminal commands (yum, systemctl, tar, nano/vim)
       and Java classpath management.
**3. ARCHITECTURE FOCUS**

This lab focuses on the **Infrastructure Layer** of the system. Before you can build functional
microservices (such as the Order Service or Execution Service), the underlying Operating System,
Database persistence layer, and Communication Protocol engines must be operational.
You are effectively setting up the "ground floor" of a multi-tier architecture:

1. **Data Tier:** MySQL (Persistence).
2. **Application Tier:** Java + QuickFIX/J (Business Logic).
3. **Presentation Tier:** Angular (User Interface).
**4. SOFTWARE & INFRASTRUCTURE STACK**

Ensure you have the following components ready for installation.


```
Component Specification Role in System
```
```
OS CentOS 9 / RedHat 9 The stable server environment mimicking
production trading servers.
```
```
Language
```
```
Java 8 (OpenJDK 1.8) or
higher
```
```
The core language for high-performance backend
microservices.
```
```
Database MySQL 8.0+ Stores the audit trail of every Order and Trade execution.
```
```
FIX Engine QuickFIX/J (v2.x) Libraries (core, messages) to parse FIX 4.
protocol messages.
```
```
Simulator MiniFix / B2BITS Acts as the external Client/Exchange to generate order traffic.
```
```
Frontend Angular CLI + Node.js Framework for building the real-time trader
dashboard.
```
## IDE

```
Eclipse / IntelliJ / VS
Code Integrated Development Environment.^
```
## 5. TECHNICAL THEORY

To understand why this specific stack is chosen, consider the requirements of a **Capital Markets
System** :
**A. Deterministic Performance (Java/Linux)**

Financial systems require stability. We use **Linux (CentOS)** because of its efficient kernel
scheduling and memory management, which are crucial when processing 500,000 orders. **Java** is
selected for its mature ecosystem and the QuickFIX/J library, which is the industry standard open-
source FIX engine.


**B. Persistence & Audit Trails (MySQL)**

In trading, every action must be auditable. If a crash occurs, we must know exactly which orders
were open. **MySQL** provides the relational integrity needed to store complex relationships
between _Orders_ , _Executions_ , and _Clients_.

**C. The FIX Protocol Engine**

You are not writing raw socket code to parse strings like **8=FIX.4.4|35=D....** You are using
**QuickFIX/J** , a specialized engine that handles:

- **Session Management:** Sequence numbers, heartbeats, and re-connection logic.
- **Validation:** Ensuring messages adhere to the FIX 4.4 specification.
- **Parsing:** Converting raw data streams into Java Objects.
**6. STEP-BY-STEP IMPLEMENTATION**

**Step A: Operating System & Java Setup**

1. **OS Verification:** Open a terminal and verify your distribution to ensure compatibility.
    cat /etc/os-release
2. **Java Installation:** Install the OpenJDK development kit. This provides the javac
    compiler needed for your backend.
    sudo yum install java-1.8.0-openjdk-devel
3. **Environment Variables:** Configure JAVA_HOME in your .bashrc or .bash_profile.
    export JAVA_HOME=$(dirname $(dirname $(readlink - f $(which java))))
    export PATH=$PATH:$JAVA_HOME/bin
4. **Verification:**
    java - version
    _Output must confirm "1.8" or higher._

**Step B: Database Installation (MySQL)**

1. **Install MySQL:**
    sudo yum install mysql-server
2. **Start Service:** Enable the service to start on boot, then start it immediately.


```
sudo systemctl enable --now mysqld
```
3. **Secure Installation:** Run the security script to set the root password and remove insecure
    defaults.
    sudo mysql_secure_installation
4. **Create Lab Database:** Log in to the MySQL shell and create the system database. This
    will be populated in Lab 3.
    mysql - u root - p
    mysql> CREATE DATABASE trading_system;
    mysql> EXIT;

**Step C: FIX Engine Configuration (QuickFIX/J)**

1. **Download:** Download the latest binary distribution of **QuickFIX/J** (e.g., version 2.3.1)
    from the official website.
2. **Extract:** Unzip the archive to a known location, e.g., /opt/quickfixj/ or your user home
    directory.
3. **Locate JARs:** Identify the three critical JAR files you will need for the semester:
    o quickfixj-core.jar (The engine)
    o quickfixj-messages-all.jar (The dictionary of FIX messages)
    o slf4j-api.jar and slf4j-simple.jar (Required for logging)

**Step D: Simulator Installation (MiniFix)**

1. **Install:** Install **MiniFix** (or the B2BITS FIX Client Simulator) on your workstation.
2. **Launch Test:** Open the application. You do not need to configure the connection yet
    (that is Lab 2), but you must verify the GUI opens without crashing. This tool will act as
    the "Trader" sending Buy/Sell orders to your backend.

**Step E: Frontend Environment (Angular)**

1. **Node.js:** Install Node.js (Version 14 LTS or higher).
    sudo dnf module install nodejs:


2. **Angular CLI:** Install the Angular Command Line Interface globally.
    sudo npm install - g @angular/cli
3. **Project Init:** Create a skeleton directory for the User Interface.
    ng new trading-ui
    # Select 'No' for strict type checking if asked, to simplify learning.
    # Select 'CSS' for stylesheet format.
**7. VALIDATION & TESTING**

To strictly validate that your environment is correctly set up and all paths are linked, perform the
following "Smoke Test". This ensures your IDE can see the external QuickFIX libraries.

1. **Create a Test Class:** In your IDE, create a Java class named EnvCheck.java.
2. **Add Libraries:** Add the QuickFIX/J JARs to your project's Build Path / Classpath.
3. **Code:**
    import quickfix.Message;
    import quickfix.SessionID;
    public class EnvCheck {
    public static void main(String[] args) {
    System.out.println("--- ENVIRONMENT DIAGNOSTIC ---");

```
// Check Java Version
System.out.println("Java Version: " + System.getProperty("java.version"));
```
```
// Check QuickFIX Library Accessibility
try {
// Attempt to instantiate a core QuickFIX object
quickfix.Message msg = new quickfix.Message();
// Attempt to use a basic FIX type
SessionID session = new SessionID("FIX.4.4", "SENDER", "TARGET");
System.out.println("QuickFIX/J Library: DETECTED & FUNCTIONAL");
System.out.println("Test Message Constructed: " + msg.toString());
```

```
} catch (NoClassDefFoundError | Exception e) {
System.err.println("CRITICAL ERROR: QuickFIX/J libraries not found in
Classpath.");
e.printStackTrace();
}
}
}
```
4. **Run:** Execute the code. If you see "DETECTED & FUNCTIONAL", your backend setup
    is successful.
**8. ASSESSMENT**

Submit a concise PDF report containing the following evidence to prove your environment is
ready:

- **Screenshot 1 (Terminal):** Output of java - version and systemctl status mysqld (showing
    "Active: running").
- **Screenshot 2 (IDE):** The EnvCheck.java program running successfully in your IDE
    console.
- **Screenshot 3 (GUI):** The MiniFix simulator open on your desktop next to a browser
    window running the default Angular page (http://localhost:4200).


## LAB 2: FIX PROTOCOL CONNECTIVITY AND MICROSERVICE SKELETON

## 1. OBJECTIVE

The objective of this session is to build the first critical microservice of the system: the **Order
Service**. You will implement a FIX Engine "Acceptor" using the **QuickFIX/J** library. By the
end of this lab, your service will be able to establish a session with the MiniFix simulator, handle
the initial handshake (Logon), and maintain connectivity via Heartbeat messages.

**2. PREREQUISITES**
    - **Completion of Lab 1:** Functional Java environment and QuickFIX/J libraries available
       in the classpath.
    - **Network Concept:** Basic understanding of TCP/IP sockets, ports (specifically localhost),
       and client-server architecture.
**3. ARCHITECTURE FOCUS**

This lab focuses on the **Ingestion Layer**. You are building the "Order Service" (Server side),
which listens for incoming connections from the "FIX Simulator" (Client side).
_(Note: In the final system, this service acts as the Gateway for all electronic orders.)_

**4. SOFTWARE STACK**
    - **IDE:** Eclipse, IntelliJ IDEA, or Visual Studio.
    - **Library:** QuickFIX/J Core (quickfixj-core.jar, quickfixj-messages-all.jar).
    - **Simulator:** MiniFix (configured as an Initiator) or B2BITS FIX Client Simulator.
    - **Protocol:** FIX 4.4.
**5. TECHNICAL THEORY**
    - **Initiator vs. Acceptor:** In FIX architecture, the **Initiator** (Client) initiates the network
       connection. The **Acceptor** (Server) listens on a specific port. Your Order Service will be
       the **Acceptor**.


- **Session Layer:** Before trading data can be exchanged, a FIX Session must be
    established. This involves a specific handshake:
       1. **Logon (MsgType 35=A):** The client sends a Logon message containing
          credentials and heartbeat intervals.
       2. **Acknowledgment:** The server validates the credentials and responds with its own
          Logon message.
       3. **Heartbeats (MsgType 35=0):** Both sides send periodic "I am alive" messages to
          keep the session open.
**6. STEP-BY-STEP IMPLEMENTATION**

**Step A: Project Structure & Dependencies**

1. Create a new Java Project (or Maven Module) named OrderService.
2. If using Maven, add the QuickFIX/J dependency to your pom.xml:
3. <dependency>
4. <groupId>org.quickfixj</groupId>
5. <artifactId>quickfixj-core</artifactId>
6. <version>2.3.1</version>
7. </dependency>
    _(Note: If not using Maven, manually add the JARs from Lab 1 to your Build Path)._

**Step B: Configuration File (order-service.cfg)**
Create a file named order-service.cfg in your project root. This defines _how_ the engine runs.

- **BeginString:** FIX.4.4 (The protocol version).
- **SenderCompID:** EXEC_SERVER (Your Service's ID).
- **TargetCompID:** MINIFIX_CLIENT (The Simulator's ID).
- **ConnectionType:** acceptor.
- **SocketAcceptPort:** 9876 (The port to listen on).

**Example Configuration Content:**
[DEFAULT]
ConnectionType=acceptor


StartTime=00:00:
EndTime=00:00:
HeartBtInt=
SenderCompID=EXEC_SERVER
TargetCompID=MINIFIX_CLIENT
UseDataDictionary=Y
DataDictionary=FIX44.xml

[SESSION]
BeginString=FIX.4.
SocketAcceptPort=

**Step C: Implementing the Application Interface**

Create a class OrderApplication.java that implements quickfix.Application. You must override
the following core methods:
import quickfix.Application;
import quickfix.Message;
import quickfix.SessionID;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.RejectLogon;
import quickfix.UnsupportedMessageType;

public class OrderApplication implements Application {
@Override
public void onCreate(SessionID sessionId) {
System.out.println("Session Created: " + sessionId);
}
@Override


public void onLogon(SessionID sessionId) {
System.out.println("LOGON Success: " + sessionId);
}
@Override
public void onLogout(SessionID sessionId) {
System.out.println("LOGOUT: " + sessionId);
}
@Override
public void toAdmin(Message message, SessionID sessionId) {
// Used for administrative messages (Heartbeats, Logons)
}
@Override
public void fromAdmin(Message message, SessionID sessionId) throws FieldNotFound,
IncorrectDataFormat, IncorrectTagValue, RejectLogon {
// Received administrative messages
}
@Override
public void toApp(Message message, SessionID sessionId) throws DoNotSend {
// Outgoing business messages
}
@Override
public void fromApp(Message message, SessionID sessionId) throws FieldNotFound,
IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
// Incoming business messages (New Orders will arrive here later)
System.out.println("Message Received: " + message.toString());
}
}

**Step D: The Main Entry Point**

Create a class AppLauncher.java. This will start the server.
import quickfix.DefaultMessageFactory;


import quickfix.FileStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.SessionSettings;
import quickfix.SocketAcceptor;

public class AppLauncher {

public static void main(String[] args) {
try {
SessionSettings settings = new SessionSettings("order-service.cfg");
OrderApplication application = new OrderApplication();
FileStoreFactory storeFactory = new FileStoreFactory(settings);
ScreenLogFactory logFactory = new ScreenLogFactory(settings);
DefaultMessageFactory messageFactory = new DefaultMessageFactory();
SocketAcceptor acceptor = new SocketAcceptor(application, storeFactory, settings,
logFactory, messageFactory);
acceptor.start();
System.out.println("Order Service started. Listening on port 9876...");

// Keep the process running
System.in.read();
acceptor.stop();
} catch (Exception e) {
e.printStackTrace();
}
}
}

**Step E: Simulator Configuration**

Open your MiniFix (or FIX Client) configuration. You must reverse the IDs to match your
server:


- **Connection Type:** Initiator
- **SenderCompID:** MINIFIX_CLIENT (Must match the TargetCompID in your Java
    code)
- **TargetCompID:** EXEC_SERVER (Must match the SenderCompID in your Java code)
- **Port:** 9876
- **Host:** localhost (or 127.0.0.1)
**7. VALIDATION & TESTING**
1. **Start the Java Application:** Run AppLauncher. You should see logs indicating the
engine has started and is listening on port 9876.
2. **Start the Simulator:** Launch MiniFix and click "Connect".
3. **Observe the Handshake:**
o Look at your Java Console. You should see an incoming connection event.
o Verify the onLogon method was triggered and printed "LOGON Success".
o Wait 30 seconds to see if Heartbeat messages are appearing in the logs.
**8. ASSESSMENT**

Submit a lab report containing:

- **Code Snippet:** The order-service.cfg file you created.
- **Evidence:** A screenshot of the Java console showing "Logon - MINIFIX_CLIENT" and
    subsequent Heartbeat messages.
- **Analysis:** Briefly explain what would happen if you changed the SenderCompID in the
    simulator but not in your Java code.


## LAB 3: HIGH-VOLUME MESSAGE INGESTION

## 1. OBJECTIVE

The objective of this session is to transform your "Order Service" from a passive listener into an
active processing engine. You will implement the business logic required to parse incoming
**NewOrderSingle (MsgType=D)** messages, validate their contents, and generate the appropriate
protocol responses.

In a high-frequency trading context, this service must be capable of ingesting bursts of traffic (up
to 500K orders) without crashing. You will learn to extract financial data from the FIX tag-value
stream and map it to internal Java objects.

**2. PREREQUISITES**
    - **Completion of Lab 2:** The Order Service is running and can successfully Logon/Logout
       with MiniFix.
    - **FIX Dictionary Knowledge:** Understanding of core tags: 55 (Symbol), 54 (Side), 38
       (OrderQty), 44 (Price).
    - **Java Collections:** Familiarity with ConcurrentHashMap for thread-safe in-memory
       storage.
**3. ARCHITECTURE FOCUS**

This lab focuses on the **Application Layer** logic within the Order Service.

1. **Ingestion:** The QuickFIX engine calls your fromApp method.
2. **Parsing:** You extract data from the generic Message object.
3. **Validation:** You ensure the order is compliant (e.g., Price > 0).
4. **Acknowledgment:** You send an ExecutionReport (MsgType=8) back to the client to
    confirm receipt. **Crucial:** If you do not acknowledge the order, the client will assume it is
    lost and may resend it, causing duplicates.


## 4. SOFTWARE STACK

- **Language:** Java 8+ (Core logic).
- **Library:** QuickFIX/J.
- **Tool:** MiniFix (configured to send Order Traffic, not just heartbeats).

## 5. TECHNICAL THEORY

**A. The "New Order Single" (35=D)**

This is the fundamental message in electronic trading. It represents a request from a client to buy
or sell a security.

- **ClOrdID (11):** Client Order ID. Unique ID assigned by the _Client_. You must preserve
    this.
- **Symbol (55):** The ticker (e.g., "MSFT").
- **Side (54):** 1 = Buy, 2 = Sell.
- **OrderQty (38):** Number of shares.
- **Price (44):** Limit price (optional for Market orders, but mandatory for this lab).

**B. The "Execution Report" (35=8)**

For every action in the system, the server must respond with an Execution Report.

- **ExecType (150):** What happened? (0 = New, F = Trade, 8 = Rejected).
- **OrdStatus (39):** Current status of the order.

## 6. STEP-BY-STEP IMPLEMENTATION

**Step A: Modeling the Internal Order**


Create a simple Java POJO (Plain Old Java Object) to represent an order in your system. This
decouples your internal logic from the external FIX format.

public class Order {

private String clOrdID;

private String symbol;

private char side;

private double price;

private double quantity;

// Constructor, Getters, and Setters

}

**Step B: Handling the fromApp Callback**

Modify your OrderApplication.java file. You need to inspect the MsgType of incoming messages
and route them to a processor function.

import quickfix.field.MsgType;

@Override

public void fromApp(Message message, SessionID sessionId) throws FieldNotFound,
IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {

// 1. Identify Message Type

String msgType = message.getHeader().getString(MsgType.FIELD);

if (msgType.equals(MsgType.ORDER_SINGLE)) {

processNewOrder(message, sessionId);

} else {

System.out.println("Received unknown message type: " + msgType);


## }

## }

**Step C: Parsing Logic**

Implement the processNewOrder method. This is where you "crack" the message.

import quickfix.field.*;

private void processNewOrder(Message message, SessionID sessionId) {

try {

// 2. Extract Fields using QuickFIX types

String clOrdId = message.getString(ClOrdID.FIELD);

String symbol = message.getString(Symbol.FIELD);

char side = message.getChar(Side.FIELD);

double qty = message.getDouble(OrderQty.FIELD);

double price = message.getDouble(Price.FIELD);

System.out.printf("ORDER RECEIVED: ID=%s Side=%s Sym=%s Px=%.2f
Qty=%.0f%n",

clOrdId, (side == '1'? "BUY" : "SELL"), symbol, price, qty);

// 3. Validation (Simple Rule: Price and Qty must be positive)

if (qty <= 0 || price <= 0) {

sendReject(message, sessionId, "Invalid Price or Qty");

} else {

acceptOrder(message, sessionId);

}


} catch (FieldNotFound e) {

e.printStackTrace();

}

}

**Step D: Sending the Acknowledgment**

You must construct a response message to tell MiniFix "We got your order."

private void acceptOrder(Message request, SessionID sessionId) {

try {

// Create an ExecutionReport (MsgType = 8)

quickfix.fix44.ExecutionReport ack = new quickfix.fix44.ExecutionReport();

// Mandatory Fields mapping

ack.set(new OrderID("GEN_" + System.currentTimeMillis())); // Server generated ID

ack.set(new ExecID("EXEC_" + System.currentTimeMillis()));

ack.set(new ClOrdID(request.getString(ClOrdID.FIELD))); // Echo back the Client's ID

ack.set(new Symbol(request.getString(Symbol.FIELD)));

ack.set(new Side(request.getChar(Side.FIELD)));

// Status fields: "New"

ack.set(new ExecType(ExecType.NEW));

ack.set(new OrdStatus(OrdStatus.NEW));

// Quantity accounting

ack.set(new LeavesQty(request.getDouble(OrderQty.FIELD)));

ack.set(new CumQty(0));

ack.set(new AvgPx(0));


// Send back to the specific session

Session.sendToTarget(ack, sessionId);

} catch (Exception e) {

e.printStackTrace();

}

}

**Step E: MiniFix Configuration**

1. Open MiniFix.
2. Navigate to the "Orders" tab.
3. Configure a predefined order:
    o Symbol: GOOG
    o Side: Buy
    o Qty: 100
    o Price: 150.50
4. Click "Send".
**7. VALIDATION & TESTING**
1. **Console Check:** Run your Java application. Send an order from MiniFix. Your console
should print the formatted string: ORDER RECEIVED: ID=... Side=BUY Sym=GOOG
....
2. **Protocol Check:** Look at the MiniFix "Events" or "Messages" log.
o **Success:** You see a Green line (incoming message) with MsgType 8
(ExecutionReport) and Status New.
o **Failure:** The order remains in "Pending New" state indefinitely (yellow/orange),
meaning you forgot to send the ACK.


3. **Stress Test:** Use the "Auto-generate" feature in MiniFix to send 10 orders per second.
    Ensure your console logs keep up without freezing.
**8. ASSESSMENT**

Submit a lab report containing:

- **Code:** The processNewOrder method logic.
- **Screenshot:** The MiniFix "Trade/Order Blotter" showing orders with status "New"
    (Confirmed) rather than "Pending".
- **Question:** Why do we need to echo back the ClOrdID (Tag 11) in the response? What
    happens if the Client sends two orders and we swap the IDs in the response?


## LAB 4: REAL-TIME FRONTEND INTEGRATION (ANGULAR)

## 1. OBJECTIVE

The objective of this session is to bridge the backend processing engine with a user-facing
dashboard. In financial systems, traders cannot wait for a page reload to see if their order was
accepted. Data must be **pushed** to the UI immediately.

You will implement a **WebSocket** server in your Java backend and an **RxJS** - based WebSocket
service in your Angular frontend. By the end of this lab, orders sent from MiniFix will appear
instantaneously on a web grid.

## 2. PREREQUISITES

- **Completion of Lab 3:** Your backend successfully accepts and parses orders.
- **Angular Environment:** Node.js and Angular CLI installed (from Lab 1).
- **Concepts:** JSON (JavaScript Object Notation) and asynchronous programming
    (Observables).

## 3. ARCHITECTURE FOCUS

This lab connects the **Application Tier** to the **Presentation Tier**.

[FIX Simulator] -> (FIX) -> [Order Service] -> (Internal Event) -> [WebSocket Server] -> (JSON)

- > [Angular UI]

## 4. SOFTWARE STACK

- **Backend:** Java-WebSocket Library (org.java-websocket).
- **Frontend:** Angular 12+ (TypeScript).


- **Data Format:** JSON (via Jackson or Gson library for serialization).

## 5. TECHNICAL THEORY

**A. HTTP vs. WebSockets**

- **HTTP (REST):** Request-Response. The client asks "Any new data?", Server says
    "Yes/No". Inefficient for trading (polling latency).
- **WebSocket:** Full-duplex. A persistent TCP connection stays open. The server can push
    data to the client whenever an event occurs.

**B. The Observable Pattern**

Angular uses RxJS. Instead of handling a single event, we subscribe to a _stream_ of events. The
WebSocket connection is treated as an infinite stream of incoming order data.

**6. STEP-BY-STEP IMPLEMENTATION**

**Part I: Backend (Java) Changes**

Step A: Add Dependencies

Add the WebSocket library and a JSON library (Gson) to your pom.xml or Build Path.

<dependency>

<groupId>org.java-websocket</groupId>

<artifactId>Java-WebSocket</artifactId>

<version>1.5.3</version>

</dependency>

<dependency>

<groupId>com.google.code.gson</groupId>

<artifactId>gson</artifactId>


<version>2.8.9</version>

</dependency>

**Step B** : Implement the WebSocket Server

Create a class OrderBroadcaster.java extending WebSocketServer.

import org.java_websocket.server.WebSocketServer;

import org.java_websocket.WebSocket;

import org.java_websocket.handshake.ClientHandshake;

import java.net.InetSocketAddress;

public class OrderBroadcaster extends WebSocketServer {

public OrderBroadcaster(int port) {

super(new InetSocketAddress(port));

}

@Override

public void onOpen(WebSocket conn, ClientHandshake handshake) {

System.out.println("UI Connected: " + conn.getRemoteSocketAddress());

}

@Override

public void onMessage(WebSocket conn, String message) {

// We generally don't expect messages FROM the UI in this lab

}


// ... implement onClose and onError with basic logging ...

public void broadcastOrder(Order order) {

// Convert Order object to JSON string

String json = new com.google.gson.Gson().toJson(order);

// Send to all connected UIs

broadcast(json);

}

}

**Step C** : Integrate with FIX Logic

In your AppLauncher.java:

1. Instantiate OrderBroadcaster server = new OrderBroadcaster(8080);
2. Call server.start();
3. Pass this server instance to your OrderApplication.
4. In processNewOrder (from Lab 3), call broadcaster.broadcastOrder(myOrderPojo)
    immediately after parsing.

**Part II: Frontend (Angular) Implementation**

Step D: Create the Service

Generate a service to manage the connection.

ng generate service services/websocket

In websocket.service.ts:

import { Injectable } from '@angular/core';


import { Subject } from 'rxjs';

@Injectable({

providedIn: 'root'

})

export class WebsocketService {

private socket: WebSocket;

// A Subject is a multicast Observable (allows multiple components to listen)

public messages: Subject<any> = new Subject<any>();

constructor() {

this.socket = new WebSocket('ws://localhost:8080');

this.socket.onmessage = (event) => {

console.log('Raw data received:', event.data);

this.messages.next(JSON.parse(event.data));

};

this.socket.onopen = (event) => {

console.log('Connected to Order Service');

};

}

}

**Step E** : Create the Component

Generate a component for the order grid.

ng generate component components/order-grid


In order-grid.component.ts:

import { Component, OnInit } from '@angular/core';

import { WebsocketService } from '../../services/websocket.service';

@Component({ ... })

export class OrderGridComponent implements OnInit {

orders: any[] = [];

constructor(private wsService: WebsocketService) { }

ngOnInit(): void {

// Subscribe to the stream

this.wsService.messages.subscribe(newOrder => {

// Add new order to the top of the list

this.orders.unshift(newOrder);

});

}

}

**Step F** : The HTML Template

In order-grid.component.html, create a simple table.

<div class="container">

<h2>Live Order Blotter</h2>

<table>

<thead>

<tr>


<th>ID</th>

<th>Symbol</th>

<th>Side</th>

<th>Qty</th>

<th>Price</th>

</tr>

</thead>

<tbody>

<tr *ngFor="let order of orders">

<td>{{ order.clOrdID }}</td>

<td>{{ order.symbol }}</td>

<td [style.color]="order.side === '1'? 'green' : 'red'">

{{ order.side === '1'? 'BUY' : 'SELL' }}

</td>

<td>{{ order.quantity }}</td>

<td>{{ order.price }}</td>

</tr>

</tbody>

</table>

</div>


## 7. VALIDATION & TESTING

1. **Launch Order Service:** Ensure the console says "Order Service started..." AND
    "WebSocket Server started on port 8080".
2. **Launch Angular:** Run ng serve and open [http://localhost:4200.](http://localhost:4200.)
3. **Check Connection:** Your Java console should log "UI Connected: /127.0.0.1...".
4. **End-to-End Test:**
    o Open MiniFix.
    o Send a Buy Order for "IBM".
    o **Result:** The row for "IBM" should appear instantly on your web page without
       you refreshing the browser.
**8. ASSESSMENT**

Submit a video recording (30 seconds) showing:

1. The empty Web UI.
2. You clicking "Send" in MiniFix.
3. The order appearing on the Web UI and the Java Console simultaneously.


## LAB 5: DATA PERSISTENCE AND AUDIT TRAIL

## 1. OBJECTIVE

The objective of this session is to implement the **Persistence Layer** of your Order Management
System. In financial markets, every transaction must be recorded for regulatory audit (e.g.,
SEC/SEBI regulations).

However, database operations are significantly slower (milliseconds) than network I/O
(microseconds). Writing to the database directly within your FIX message callback (fromApp) will
block the engine and cause high latency. You will learn to implement an **Asynchronous Database
Writer** pattern to persist data without slowing down the trading engine.

**2. PREREQUISITES**
    - **Completion of Lab 4:** A working system that parses orders.
    - **Database Access:** The MySQL database trading_system created in Lab 1.
    - **Java Concurrency:** Understanding of BlockingQueue and Thread.
**3. ARCHITECTURE FOCUS**

This lab separates the **Ingestion Path** from the **Storage Path**.

- **Critical Path:** FIX Engine -> Order Parsing -> Validation -> Ack -> Queue. (Must be
    fast).
- **Slow Path:** Queue -> Database Writer -> MySQL. (Can lag slightly behind real-time).
**4. SOFTWARE STACK**
- **Database:** MySQL 8.0+.
- **Driver:** MySQL Connector/J.
- **Java APIs:** JDBC (Java Database Connectivity).
**5. TECHNICAL THEORY**

**Synchronous vs. Asynchronous Logging**


- **Synchronous:** The system waits for the INSERT query to finish before sending the
    Acknowledgment back to the client. _Risk:_ If the DB stalls, the trading stops.
- **Asynchronous:** The system places the data object into a memory buffer (Queue) and
    immediately Acknowledges the client. A separate thread picks items off the queue and
    inserts them. _Benefit:_ The trading engine runs at memory speed.
**6. STEP-BY-STEP IMPLEMENTATION**

**Step A: Database Configuration**

1. Add the MySQL Connector dependency to your pom.xml:
<dependency>
<groupId>mysql</groupId>
<artifactId>mysql-connector-java</artifactId>
<version>8.0.28</version>
</dependency>
2. Create the orders table in MySQL (if not done in Lab 6 prep):
CREATE TABLE orders (
order_id VARCHAR(50) PRIMARY KEY, -- Server generated ID
cl_ord_id VARCHAR(50), -- Client's ID
symbol VARCHAR(20),
side CHAR(1),
price DECIMAL(15, 2),
quantity DECIMAL(15, 0),
status VARCHAR(20),


```
timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```
**Step B: The Database Manager**

Create a Singleton class DatabaseManager.java to handle the connection.

import java.sql.Connection;

import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.SQLException;

public class DatabaseManager {

private static final String URL = "jdbc:mysql://localhost:3306/trading_system";

private static final String USER = "root";

private static final String PASS = "your_password"; // Use env variables in production!

public static void insertOrder(Order order) {

String sql = "INSERT INTO orders (order_id, cl_ord_id, symbol, side, price, quantity,
status) VALUES (?, ?, ?, ?, ?, ?, ?)";

try (Connection conn = DriverManager.getConnection(URL, USER, PASS);

PreparedStatement pstmt = conn.prepareStatement(sql)) {

pstmt.setString(1, order.getOrderId());


pstmt.setString(2, order.getClOrdID());

pstmt.setString(3, order.getSymbol());

pstmt.setString(4, String.valueOf(order.getSide()));

pstmt.setDouble(5, order.getPrice());

pstmt.setDouble(6, order.getQuantity());

pstmt.setString(7, "NEW");

pstmt.executeUpdate();

} catch (SQLException e) {

e.printStackTrace();

}

}

}

**Step C: The Asynchronous Worker**

Create a class OrderPersister.java that runs on its own thread.

import java.util.concurrent.BlockingQueue;

public class OrderPersister implements Runnable {

private final BlockingQueue<Order> queue;

private volatile boolean running = true;

public OrderPersister(BlockingQueue<Order> queue) {

this.queue = queue;


## }

@Override

public void run() {

System.out.println("Persistence Worker Started...");

while (running) {

try {

// take() blocks until an item is available

Order order = queue.take();

DatabaseManager.insertOrder(order);

System.out.println("Persisted Order: " + order.getClOrdID());

} catch (InterruptedException e) {

Thread.currentThread().interrupt();

}

}

}

public void stop() {

this.running = false;

}

}

**Step D: Wiring it Together**

Modify AppLauncher.java to start the worker thread.

// 1. Create the shared queue

BlockingQueue<Order> dbQueue = new java.util.concurrent.LinkedBlockingQueue<>();


// 2. Start the Consumer (Worker)

OrderPersister persister = new OrderPersister(dbQueue);

new Thread(persister).start();

// 3. Pass the Queue to the Producer (Application)

OrderApplication app = new OrderApplication(dbQueue);

// (Update OrderApplication constructor to accept this queue)

Modify OrderApplication.java to push to the queue.

private void processNewOrder(Message message, SessionID sessionId) {

// ... parse logic ...

// Send ACK first (Low Latency)

acceptOrder(message, sessionId);

// Then queue for storage (Async)

dbQueue.offer(myOrderPojo);

}

**7. VALIDATION & TESTING**
    1. **Latency Check:** Send a batch of 100 orders from MiniFix. The acknowledgments in
       MiniFix should be near-instant, even if the "Persisted Order" logs in your console appear
       slightly slower.


2. **Data Integrity:** Open MySQL Workbench or Command Line. Run SELECT * FROM
    orders;. Verify that all 100 orders are present with correct details.
3. **Crash Recovery (Thought Experiment):** If you kill the Java process _immediately_ after
    the ACK is sent but before the DB write, what happens? (This is a common interview
    question for this architecture).
**8. ASSESSMENT**

Submit a lab report containing:

- **Code Snippet:** The OrderPersister run loop.
- **Screenshot:** Result of the SQL query showing populated data.
- **Analysis:** Why did we use LinkedBlockingQueue? What would happen if we used a
    queue with a fixed capacity (e.g., ArrayBlockingQueue) and the database went offline
    while orders kept coming?


## LAB 6: DOMAIN MODELING AND SCHEMA OPTIMIZATION

## 1. OBJECTIVE

The objective of this session is to finalize the **Data Model** for the entire semester. You will refactor
your simple Order class into a robust entity and design the complete Database Schema to support
Executions, Reference Data, and Customer details.

This lab prepares the "Static Data" required for the **Matching Engine** (Lab 7). Without knowing
which stocks exist (Security Master) or which clients are authorized (Customer Master), the
matching engine cannot function.

**2. PREREQUISITES**
    - **Completion of Lab 5:** Basic DB connectivity is working.
    - **Source Specification:** Refer to Page 11 of the provided _Software Lab - Trading_
       _Application_ PDF for the exact schema requirements.
**3. ARCHITECTURE FOCUS**

This lab focuses on **Data Organization**. You are defining the contract between the Application
Logic and the Storage Layer. You will also populate "Reference Data" (Static data that doesn't
change often, like the list of S&P 500 stocks).

**4. SOFTWARE STACK**
    - **Database:** MySQL.
    - **Tool:** MySQL Workbench or DBeaver (for visual schema design).
    - **Java:** Refactoring existing POJOs.
**5. TECHNICAL THEORY**

**A. Normalization vs. Denormalization**

In trading systems, we often **denormalize** slightly for speed. For example, instead of joining the
Order table with the Customer table every time we need to check a credit limit, we might cache
Customer limits in memory.


**B. The Four Pillars of OMS Data**

1. **Order:** An instruction to trade.
2. **Execution:** A realized trade (Result of a match).
3. **Security Master:** Definition of tradable instruments (e.g., Symbol AAPL, Type
    Common Stock).
4. **Customer Master:** Definition of clients (e.g., Client Fidelity, Limit 1M).
**6. STEP-BY-STEP IMPLEMENTATION**

**Step A: Finalizing the Database Schema**

Run the following SQL scripts to create the system tables as specified in the course
documentation.

**1. Security Master (Ref Data)**

CREATE TABLE security_master (

symbol VARCHAR(20) PRIMARY KEY,

security_type VARCHAR(20), -- e.g., 'CS' (Common Stock)

description VARCHAR(100),

underlying VARCHAR(20), -- For options later

lot_size INT DEFAULT 100

);

**2. Customer Master (Ref Data)**

CREATE TABLE customer_master (

customer_code VARCHAR(20) PRIMARY KEY,

customer_name VARCHAR(100),


customer_type VARCHAR(20), -- 'INSTITUTIONAL' or 'RETAIL'

credit_limit DECIMAL(20, 2)

);

**3. Execution Table (Transactional)**

CREATE TABLE executions (

exec_id VARCHAR(50) PRIMARY KEY,

order_id VARCHAR(50), -- Foreign Key to Orders

symbol VARCHAR(20),

side CHAR(1),

exec_qty INT,

exec_price DECIMAL(15, 2),

match_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

CONSTRAINT fk_order FOREIGN KEY (order_id) REFERENCES orders(order_id)

);

**Step B: Refactoring Java Entities**

Update your Java classes to match these tables. You need a Security class and a Customer class.

The OrderBook Structure (Preparation for Lab 7)

You need a way to hold orders in memory for matching. Create the skeleton for the Order Book:

import java.util.concurrent.ConcurrentSkipListMap;

public class OrderBook {


private String symbol;

// Bids: Sorted High to Low (Descending) - Best Bid is Highest Price

private ConcurrentSkipListMap<Double, List<Order>> bids =

new ConcurrentSkipListMap<>(Collections.reverseOrder());

// Asks: Sorted Low to High (Ascending) - Best Ask is Lowest Price

private ConcurrentSkipListMap<Double, List<Order>> asks =

new ConcurrentSkipListMap<>();

// Methods to add/remove orders will be added in Lab 7

}

_Note: We use ConcurrentSkipListMap because it keeps keys (Prices) sorted, which is essential
for price-time priority matching._

**Step C: Populating Reference Data**

Insert dummy data into your database so you can test validation logic.

INSERT INTO security_master (symbol, security_type, lot_size) VALUES

('GOOG', 'CS', 1),

('MSFT', 'CS', 1),

('IBM', 'CS', 1);

INSERT INTO customer_master (customer_code, customer_type) VALUES


## ('CLIENT_A', 'RETAIL'),

## ('CLIENT_B', 'INSTITUTIONAL');

**Step D: Validation Logic Integration**

Modify your OrderApplication.java from Lab 3. Before accepting an order, validate that the
Symbol exists in your SecurityMaster table.

// Load securities into a HashMap on startup for fast lookup

private Map<String, Security> validSecurities = new HashMap<>();

public void onStart() {

// Select * from security_master and populate map

}

private void processNewOrder(...) {

if (!validSecurities.containsKey(incomingSymbol)) {

sendReject(message, sessionId, "Unknown Security Symbol");

return;

}

// ... proceed ...

}

**7. VALIDATION & TESTING**
    1. **Reference Data Check:** Send an order for "GOOG". It should be accepted (if "GOOG"
       is in your DB).


2. **Validation Check:** Send an order for "INVALID_CO". It should be rejected with a
    standard FIX Reject (MsgType=3) message explaining "Unknown Security".
3. **Schema Check:** Verify that the executions table is created correctly with the Foreign
    Key constraint.
**8. ASSESSMENT**

Submit a lab report containing:

- **ER Diagram:** A simple Entity-Relationship diagram showing how Orders, Executions,
    and Security Master relate.
- **Code:** The Java code snippet showing how you preload the Security Master data into
    memory at application startup.
- **SQL:** The raw SQL used to create the executions table.


## LAB 7: THE MATCHING ENGINE - CORE ALGORITHMS

## 1. OBJECTIVE

The primary objective of this session is to implement the core logic of an exchange: the **Matching
Engine**. This component acts as the "brain" of your trading system. Up until now, your system has
functioned as a passive data ingestion pipeline, accepting orders, validating them, and storing them
in a database. Now, it must transition into an active processing engine that compares incoming
Buy and Sell orders to generate trades in real-time.

You will implement the **Price-Time Priority** algorithm, which is the global industry standard for
"Central Limit Order Books" (CLOB). The engine’s responsibility is deterministic and binary: it
must determine if an incoming Buy order is priced high enough to match an existing Sell order (or
vice versa) and execute the trade immediately. If no match is possible, the order must be queued
for future execution. This lab shifts focus from architectural plumbing to algorithmic efficiency
and fairness.

**2. PREREQUISITES**
    - **Completion of Lab 6:** Your OrderBook data structures should be defined and ready for
       logic implementation. You should have a clear separation between the Order entity and the
       OrderBook container.
    - **Algorithm Knowledge:** A solid understanding of **Sorted Maps** (specifically Red-Black
       trees or Skip Lists) and **Queues**. You must understand why searching a sorted list is
       $O(\log n)$ while searching an unsorted list is $O(n)$.
    - **Theory: "Crossing the Spread"**. You must understand the market dynamic where a trade
       only occurs if a Buyer is willing to pay the Seller's asking price (Bid $\ge$ Ask). If the Bid
       is lower than the Ask, no trade occurs, and the spread remains open.
**3. ARCHITECTURE FOCUS**

This lab takes place entirely within the **Execution Service** (or the logic layer of the Order Service).
It represents the transition from state management to event processing.


- **Input:** A fully validated NewOrderSingle object. At this stage, we assume the order has
    passed all credit checks and symbol validation.
- **Process:**
    1. **Lookup:** Retrieve the specific Order Book for the symbol (e.g., "GOOG").
    2. **Match:** Attempt to execute against the "Opposite Side" of the book.
    3. **Rest:** If the order is not fully filled, place the remainder on the "Same Side" of the
       book.
- **Output:** A list of Execution objects (Trades). These objects represent the "fill" events
    that will eventually be sent back to the client and persisted to the database.
**4. SOFTWARE STACK**

To ensure low latency and thread safety, specific Java collections are required:

- **Java Collections:**
    o ConcurrentSkipListMap: We use this instead of a standard TreeMap because it
       offers thread-safe, sorted storage. It orders keys (Prices) naturally, allowing us to
       instantly access the "Best Bid" or "Best Ask" without iterating through the entire
       list.
    o LinkedList: Used as the value in the map (List<Order>) to maintain **Time**
       **Priority**. The first order added to the list is the first one matched (FIFO).
- **Synchronization:** While ConcurrentSkipListMap handles concurrent access to the map
    structure, the matching logic itself (checking a price, reducing quantity, removing an
    order) is a compound operation. We will use synchronized blocks or Reentrant Locks to
    ensure that two threads do not try to match against the same resting order simultaneously.
**5. TECHNICAL THEORY**

**Price-Time Priority**

The fairness of a market depends on strict adherence to these two rules:


1. **Price Priority (The "Best" Price Wins):**
    o **Buy Side:** The highest price is the best. A buyer offering $100 is prioritized over
       a buyer offering $99. Therefore, Bids are sorted in **Descending** order.
    o **Sell Side:** The lowest price is the best. A seller asking $100 is prioritized over a
       seller asking $101. Therefore, Asks are sorted in **Ascending** order.
2. **Time Priority (First In, First Out):**
    o If multiple orders exist at the exact same price level (e.g., three sellers all asking
       $100.50), the system must match the order that arrived first. This encourages
       traders to submit liquidity early. This is implemented by appending new orders to
       the _end_ of the list at that price node.

**The Matching Loop**

When an order arrives, we do not immediately book it. We must first check if it is "marketable"
(executable). This prevents "crossed markets" where a Bid is higher than an Ask.

- **Scenario:** _Incoming Buy Order for 100 shares @ $100._
- **Step 1:** Check the **Best Sell** (Lowest Ask). Is there a seller asking $100 or less?
- **Step 2 (If Match):**
    o Generate a Trade event for the overlapping quantity.
    o Decrement the quantity of the resting Sell order.
    o Decrement the quantity of the incoming Buy order.
    o Repeat the loop if the Buy order still has quantity remaining.
- **Step 3 (If No Match):** If the Best Sell is $101 (or the book is empty), the Buy order
    cannot trade. The remaining quantity is added to the Bid side of the book at $100,
    becoming a new source of liquidity.
**6. STEP-BY-STEP IMPLEMENTATION**

**Step A: The Order Book Structure (Refinement)**


Ensure your OrderBook.java (started in Lab 6) has the correct sorting logic. Note the use of
Collections.reverseOrder() for the Bids map—this ensures that the "first key" is always the
highest price.

import java.util.*;

import java.util.concurrent.*;

public class OrderBook {

// Bids: High Price first (Descending Order).

// Key: Price, Value: List of Orders at that price (Time Priority)

private NavigableMap<Double, List<Order>> bids = new
ConcurrentSkipListMap<>(Collections.reverseOrder());

// Asks: Low Price first (Ascending Order).

private NavigableMap<Double, List<Order>> asks = new ConcurrentSkipListMap<>();

// synchronized ensures only one thread can modify the book for this symbol at a time

public synchronized List<Execution> match(Order incoming) {

List<Execution> executions = new ArrayList<>();

if (incoming.getSide() == '1') { // Buy Order

// Attempt to match against the Asks (Sellers)

matchOrder(incoming, asks, executions);

} else { // Sell Order


// Attempt to match against the Bids (Buyers)

matchOrder(incoming, bids, executions);

}

// If the order is not fully filled after matching, add the remainder to the book

if (incoming.getQuantity() > 0) {

addToBook(incoming);

}

return executions;

}

// Helper to add resting orders to the correct map

private void addToBook(Order order) {

NavigableMap<Double, List<Order>> side = (order.getSide() == '1')? bids : asks;

// ComputeIfAbsent creates the list if this is the first order at this price

side.computeIfAbsent(order.getPrice(), k -> new LinkedList<>()).add(order);

}

}

**Step B: The Matching Logic**


Implement the matchOrder helper method. This is the core algorithmic component. Pay close
attention to the while loop condition—we continue matching as long as the incoming order has
quantity _and_ there is liquidity on the other side.

private void matchOrder(Order incoming, NavigableMap<Double, List<Order>> oppositeSide,
List<Execution> trades) {

// Continue loop while:

// 1. The incoming order still needs to be filled (Qty > 0)

// 2. The opposite book is not empty (There is someone to trade with)

while (incoming.getQuantity() > 0 && !oppositeSide.isEmpty()) {

// Peek at the best available price on the other side

Double bestPrice = oppositeSide.firstKey();

// Check Price Logic: Does the limit price allow this trade?

boolean isBuy = (incoming.getSide() == '1');

// If Buying: We want to buy Low. If BestAsk > MyLimit, I can't afford it. Stop.

if (isBuy && incoming.getPrice() < bestPrice) break;

// If Selling: We want to sell High. If BestBid < MyLimit, they aren't paying enough. Stop.

if (!isBuy && incoming.getPrice() > bestPrice) break;

// If we are here, a match is possible!


// Get the list of orders at this price level

List<Order> ordersAtLevel = oppositeSide.get(bestPrice);

// Match against the first order in the list (Time Priority / FIFO)

Order resting = ordersAtLevel.get(0);

// Calculate Trade Quantity: The max we can trade is the smaller of the two sizes

double tradeQty = Math.min(incoming.getQuantity(), resting.getQuantity());

// Create Execution Record.

// CRITICAL: The trade happens at the RESTING order's price, not the aggressor's price.

Execution exec = new Execution(incoming, resting, tradeQty, bestPrice);

trades.add(exec);

// Update Order Objects (Decrement Qty) in memory

incoming.reduceQty(tradeQty);

resting.reduceQty(tradeQty);

// Cleanup: Remove filled orders from the book to keep the map clean

if (resting.getQuantity() == 0) {

ordersAtLevel.remove(0); // Remove the filled order from the list


// If that was the last order at that price, remove the price level entirely

if (ordersAtLevel.isEmpty()) {

oppositeSide.remove(bestPrice);

}

}

}

}

**Step C: Integration**

Update OrderApplication.java to manage the collection of books. Since an exchange trades
multiple stocks, we need a registry.

1. **Data Structure:** Maintain a Map<String, OrderBook> (One book per Symbol). This
    isolates concurrency—heavy trading in "GOOG" won't block trading in "IBM".
2. **Process Flow (processNewOrder):**
    o Extract the Symbol from the incoming FIX message.
    o Retrieve (or create) the OrderBook for that symbol.
    o Call book.match(order) to get the list of trades.
    o (In Lab 8, you will send these Execution objects back to the client).
**7. VALIDATION & TESTING**

Validation for this lab relies on console logs to prove the logic is sound.

1. **Scenario 1 (Resting Order):**
    o **Action:** Send a Sell Order for MSFT @ 100 (Qty: 100).


```
o Expected Result: The console should show the order was received, but no trade
should be logged. The order is simply added to the Ask side of the MSFT book.
```
2. **Scenario 2 (Aggressive Match):**
    o **Action:** Send a Buy Order for MSFT @ 101 (Qty: 100).
    o **Analysis:** The Buyer is willing to pay $101, but the Seller is asking $100. The
       system should give the Buyer the "price improvement".
    o **Expected Result:** The console should log **"TRADE EXECUTED: MSFT 100**
       **shares @ $100"**.
    o **Note:** The trade happens at $100 (the resting price), not $101. This is a key
       validation point—if your system trades at $101, your algorithm is incorrect.
3. **Scenario 3 (Partial Fill):**
    o **Action:** Send a Buy Order for MSFT @ 102 (Qty: 150).
    o **Expected Result:**
       ▪ Assuming the book is empty after Scenario 2, send a new Sell @ 102 for
          50 shares.
       ▪ Then send the Buy 150.
       ▪ Result: Trade 50 @ 102. Remaining 100 shares are added to Bids @ 102.
**8. ASSESSMENT**
- **Code:** Submit the matchOrder logic in a clear Java file.
- **Trace:** Provide a log trace showing the following sequence to prove Price-Time priority:
1. Sell 100 @ 50.00 (Resting Order A - Added to Book)
2. Sell 100 @ 51.00 (Resting Order B - Added to Book)
3. Buy 150 @ 52.00 (Aggressive Order)
4. **Result Log:** Should clearly show two separate trades:


▪ Trade 1: 100 shares @ 50.00 (Matches A)

▪ Trade 2: 50 shares @ 51.00 (Matches B)

▪ Remaining Buy Order (50 shares) added to book @ 52.00.


## LAB 8: EXECUTION REPORTING AND FEEDBACK LOOPS

## 1. OBJECTIVE

The objective of this session is to complete the transaction lifecycle. In Lab 7, you generated
matches (trades). Now, you must communicate these trades to the outside world.

You will implement the logic to send **Execution Reports (MsgType=8, ExecType=Trade)**
back to the FIX Client (MiniFix) and update the Angular UI to show the "Fills". This feedback
loop is critical; without it, the trader does not know they own the stock.

**2. PREREQUISITES**
    - **Completion of Lab 7:** The Matching Engine is generating Execution objects.
    - **FIX Protocol:** Understanding of ExecType=F (Trade) vs ExecType=0 (New).
**3. ARCHITECTURE FOCUS**

This lab touches all layers:

1. **Logic:** MatchingEngine produces a Trade.
2. **Persistence:** Save Trade to MySQL executions table.
3. **Communication:** Send FIX Message to Client.
4. **UI:** Push JSON update via WebSocket.
**4. SOFTWARE STACK**
- **Backend:** Java (QuickFIX/J, JDBC).
- **Frontend:** Angular (RxJS).
**5. STEP-BY-STEP IMPLEMENTATION**

**Step A: Handling the Matches**

In OrderApplication.java, after calling book.match(order), iterate through the results.

List<Execution> trades = orderBook.match(newOrder);


for (Execution trade : trades) {

// 1. Persist to DB (Async)

dbQueue.offer(trade);

// 2. Notify User Interface

broadcaster.sendTradeUpdate(trade);

// 3. Send FIX Message to Client

sendFillReport(trade);

}

**Step B: Constructing the FIX Trade Message**

You need to send _two_ execution reports for every trade: one to the Buyer and one to the Seller.
(For this lab, assuming MiniFix acts as both, we send updates for the incoming order).

private void sendFillReport(Execution trade) {

try {

quickfix.fix44.ExecutionReport fixTrade = new quickfix.fix44.ExecutionReport();

// Critical Fields for a Fill

fixTrade.set(new OrderID(trade.getOrderId()));

fixTrade.set(new ExecID(trade.getExecId()));

fixTrade.set(new ExecType(ExecType.TRADE)); // 'F'

fixTrade.set(new OrdStatus(OrdStatus.FILLED)); // '2'


fixTrade.set(new Symbol(trade.getSymbol()));

fixTrade.set(new Side(trade.getSide()));

// Trade Details

fixTrade.set(new LastPx(trade.getPrice()));

fixTrade.set(new LastQty(trade.getQuantity()));

fixTrade.set(new CumQty(trade.getQuantity()));

fixTrade.set(new LeavesQty(0)); // Assuming full fill for simplicity

Session.sendToTarget(fixTrade, currentSessionID);

} catch (Exception e) {

e.printStackTrace();

}

}

**Step C: Angular UI Updates**

1. **Backend:** Ensure your OrderBroadcaster (Lab 4) has a method sendTradeUpdate that
    wraps the execution in JSON.
2. **Frontend:** Update your websocket.service.ts or component to handle a new message type
    TRADE.

**Frontend Component Logic:**

this.wsService.messages.subscribe(msg => {

if (msg.type === 'ORDER') {


this.orders.unshift(msg.data);

} else if (msg.type === 'TRADE') {

this.trades.unshift(msg.data); // Add to a new "Trade History" table

alert(`Fill! ${msg.data.symbol}: ${msg.data.quantity} @ ${msg.data.price}`);

}

});

**Step D: Database Update**

Update your OrderPersister (Lab 5) to handle Execution objects as well as Order objects. You
may need a generic interface or check instanceof.

if (obj instanceof Execution) {

DatabaseManager.insertExecution((Execution) obj);

}

**6. VALIDATION & TESTING**
    1. **MiniFix Verification:**
       o Send Buy 100 @ 50.
       o Send Sell 100 @ 50.
       o **Result:** The Sell order row in MiniFix should turn **Blue** (Filled). The Status
          column should read Filled.
    2. **UI Verification:**
       o The Angular page should pop up an alert or add a row to the "Recent Trades"
          table showing "100 @ 50".
    3. **Database Verification:**
       o SELECT * FROM executions; should show the new record.


## 7. ASSESSMENT

Submit a screenshot of the **MiniFix Blotter** showing an order with OrdStatus=Filled and
ExecType=Trade.


## LAB 9: PERFORMANCE ENGINEERING AND TELEMETRY

## 1. OBJECTIVE

The objective of this session is to measure and optimize the performance of your Order
Management System. The syllabus requires demonstrating scalability up to **500,000 orders**.

You will instrument your code to measure **Tick-to-Trade Latency** (the time from receiving a FIX
message to sending the Execution Report). You will then run stress tests using MiniFix's auto-
generation features and identify bottlenecks.

**2. PREREQUISITES**
    - **Completion of Lab 8:** A fully functional matching system.
    - **Tools:** Java VisualVM (included in JDK) or JConsole.
**3. ARCHITECTURE FOCUS**

This lab focuses on **Non-Functional Requirements** :

- **Throughput:** Orders per second (OPS).
- **Latency:** Microseconds ($\mu$s) per order.
- **Stability:** Memory usage under load (Garbage Collection).
**4. TECHNICAL THEORY**

**The Metric: Tick-to-Trade**

In high-frequency trading, this is the "Gold Standard" metric.

$$Latency = Time_{Egress} - Time_{Ingress}$$

- **Ingress:** Timestamp taken immediately inside fromApp.
- **Egress:** Timestamp taken immediately before Session.sendToTarget.
**5. STEP-BY-STEP IMPLEMENTATION**

**Step A: Instrumenting the Code**


Modify OrderApplication.java to capture nanosecond-precision timestamps.

public void fromApp(Message message, SessionID sessionId) {

long ingressTime = System.nanoTime(); // Start Clock

// ... processing ...

// ... matching ...

// ... DB queueing ...

processNewOrder(message, sessionId, ingressTime);

}

// In sendFillReport or acceptOrder

private void sendResponse(Message response, long ingressTime) {

Session.sendToTarget(response, ...);

long egressTime = System.nanoTime(); // Stop Clock

long latency = egressTime - ingressTime;

PerformanceMonitor.recordLatency(latency);

}

**Step B: The Performance Monitor**

Create a simple class to aggregate stats. Printing to the console every order is too slow!

import java.util.concurrent.atomic.AtomicLong;

public class PerformanceMonitor {

private static AtomicLong totalLatency = new AtomicLong(0);


private static AtomicLong count = new AtomicLong(0);

public static void recordLatency(long nanos) {

totalLatency.addAndGet(nanos);

long currentCount = count.incrementAndGet();

if (currentCount % 1000 == 0) { // Log every 1000 orders

double avgMicros = (totalLatency.get() / currentCount) / 1000.0;

System.out.printf("Processed %d orders. Avg Latency: %.2f us%n", currentCount,
avgMicros);

}

}

}

**Step C: Running the Stress Test**

1. **Configure MiniFix:**
    o Find the "Order Generator" or "Script" tab.
    o Set Rate: **100 orders per second** (Start slow).
    o Set Total: **10,000 orders**.
    o Symbol: Rotate between GOOG, MSFT, IBM (to prevent one order book lock
       from bottlenecking).
2. **Launch:** Start the generation.

**Step D: Profiling with VisualVM**

1. Start your AppLauncher.


2. Open **JVisualVM** (type jvisualvm in terminal).
3. Connect to your AppLauncher process.
4. **Monitor Tab:** Watch the **Heap Memory**.
    o _Observation:_ Do you see a "Sawtooth" pattern? This is the Garbage Collector
       (GC) running.
    o _Analysis:_ If the GC runs too often, your latency spikes.
5. **Sampler Tab:** Click "CPU". Find which method takes the most time. Is it
    DatabaseManager? Is it OrderBook.match?
**6. TUNING EXERCISE**

(Optional Challenge for higher grades)

If your DB queue is filling up (Memory rising), the Database Writer is too slow.

- **Fix:** Implement **JDBC Batch Inserts**. Instead of inserting 1 order at a time, wait for 100
    orders and insert them in one SQL transaction.
**7. ASSESSMENT**

Submit a Performance Report containing:

1. **Latency Graph:** A plot (from Excel/Python) of Average Latency vs. Throughput (run
    tests at 100, 500, and 1000 orders/sec).
2. **Screenshots:** JVisualVM showing CPU and Memory usage during the 10,000 order
    burst.
3. **Conclusion:** What is the bottleneck in your system? (Hint: It is almost certainly the
    Database or the Console Logging).


## LAB 10: SYSTEM RESILIENCE AND DISRUPTION HANDLING

## 1. OBJECTIVE

The objective of this session is to engineer **Fault Tolerance** into your system. In capital markets,
systems fail. Networks drop, databases lock up, and processes crash. A robust Order Management
System (OMS) must recover automatically without losing financial data.

You will simulate "Chaos Engineering" scenarios: killing the FIX connection mid-stream and
verifying that the **Sequence Number Reset** logic functions correctly to recover missing orders.

**2. PREREQUISITES**
    - **Completion of Lab 9:** A high-performance system is running.
    - **Concepts:** FIX Session State (Sequence Numbers), TCP Reconnection strategies.
**3. ARCHITECTURE FOCUS**

This lab focuses on the **Session Layer Reliability**.

- **Scenario:** The Client sends Order #50. The Network fails. The Server never receives
    #50.
- **Recovery:** When the network restores, the Client sends Order #51. The Server notices a
    "Gap" (Expected #50, Got #51) and sends a **Resend Request (35=2)**.
**4. TECHNICAL THEORY**

**FIX Sequence Numbers (Tags 34)**

Every FIX message has a unique, incrementing integer ID (Tag 34).

- **SenderCompID:** Keeps track of "Next Expected Msg Seq Num".
- **TargetCompID:** Keeps track of "Next Target Msg Seq Num".
- **Gap Detection:** If Incoming_Msg_Seq_Num > Expected_Seq_Num, a message was lost.
    The engine handles this automatically _if configured correctly_.
**5. STEP-BY-STEP IMPLEMENTATION**


**Step A: Configuration Tuning**

Open your order-service.cfg. Ensure the following parameters are set to allow persistent
sessions.

# Persist sequence numbers to disk so they survive a crash

FileStorePath=target/data/ordermatch

# Timestamps to keep the session active across restarts

StartTime=00:00:00

EndTime=00:00:00

UseLocalTime=Y

# Resilience Settings

ResetOnLogon=N # CRITICAL: Do NOT reset numbers on reconnect

ResetOnLogout=N

ResetOnDisconnect=N

**Step B: Database Resilience**

Modify your DatabaseManager to handle connection drops. If MySQL restarts, your Java app
should not die.

public static Connection getConnection() throws SQLException {

try {

if (conn == null || conn.isClosed()) {

conn = DriverManager.getConnection(URL, USER, PASS);


## }

} catch (SQLException e) {

System.err.println("DB Connection Lost! Retrying...");

// Simple exponential backoff logic could go here

}

return conn;

}

**Step C: The "Chaos" Test Plan**

You cannot code resilience; you must test it. Prepare the following test steps:

1. **Baseline:** Connect MiniFix. Send 10 Orders. Verify Sequence # is 10.
2. **Disruption:**
    o **Action:** Force Kill the Java Process (kill -9 PID) OR Unplug the Ethernet/Disable
       Wi-Fi.
    o **Action:** While disconnected, attempt to send 5 orders from MiniFix. (They will
       buffer or fail locally).
3. **Recovery:**
    o **Action:** Restart the Java Process.
    o **Observation:** Watch the logs immediately upon Logon.
**6. VALIDATION & TESTING**
1. **Log Analysis:**
o When MiniFix reconnects, your Java Console should show:

MsgSeqNum too high, expecting 11 but received 16


```
o Followed by:
```
Sending ResendRequest FROM: 11 TO: 0

```
o Followed by:
```
PossDup(Y) messages.

2. **Data Integrity:** Check the Database. Did the 5 orders sent during the "outage" eventually
    arrive? If yes, your system is Fault Tolerant.
**7. ASSESSMENT**

Submit a **Resilience Log** : A text file copy of your QuickFIX logs showing the ResendRequest
(35=2) and the subsequent recovery of messages.


## LAB 11: QUANTITATIVE FINANCE - BLACK-SCHOLES IMPLEMENTATION

## 1. OBJECTIVE

The objective of this session is to implement the **Financial Math** component of the syllabus. You
will create a module that prices "Call Options" and "Put Options" in real-time based on the equity
trades occurring in your Matching Engine.

You will implement the **Black-Scholes-Merton** formula. As the stock price of "GOOG" changes
in your matching engine, the price of a "GOOG $2000 Call" on your UI must update dynamically.

**2. PREREQUISITES**
    - **Completion of Lab 8:** Trades are being broadcast to the UI.
    - **Math:** Basic understanding of Normal Distribution ($N(d_1)$).
**3. ARCHITECTURE FOCUS**

This is a **Derived Data Service**.

- **Source:** Execution stream (The "Spot Price" $S$).
- **Reference:** Static Data (Volatility $\sigma$, Risk-free rate $r$, Strike $K$, Time $T$).
- **Output:** Option Price stream.
**4. TECHNICAL THEORY**

**The Formula**

For a Call Option $C$:

$$C = S \cdot N(d_1) - K \cdot e^{-rT} \cdot N(d_2)$$

Where:

- $$d_1 = \frac{\ln(S/K) + (r + \sigma^2/2)T}{\sigma\sqrt{T}}$$
- $$d_2 = d_1 - \sigma\sqrt{T}$$
- $N(x)$ = Cumulative Distribution Function of Standard Normal Distribution.


## 5. STEP-BY-STEP IMPLEMENTATION

**Step A: The Math Library**

Create a utility class BlackScholes.java.

public class BlackScholes {

// Cumulative Normal Distribution Function

public static double N(double z) {

if (z > 6.0) { return 1.0; }

if (z < -6.0) { return 0.0; }

double b1 = 0.31938153;

double b2 = -0.356563782;

double b3 = 1.781477937;

double b4 = -1.821255978;

double b5 = 1.330274429;

double p = 0.2316419;

double c2 = 0.3989423;

double a = Math.abs(z);

double t = 1.0 / (1.0 + a * p);

double b = c2 * Math.exp((-z) * (z) / 2.0);

double n = ((((b5 * t + b4) * t + b3) * t + b2) * t + b1) * t;

n = 1.0 - b * n;

if (z < 0.0) n = 1.0 - n;

return n;

}


public static double callPrice(double S, double K, double T, double r, double sigma) {

double d1 = (Math.log(S / K) + (r + sigma * sigma / 2.0) * T) / (sigma * Math.sqrt(T));

double d2 = d1 - sigma * Math.sqrt(T);

return S * N(d1) - K * Math.exp(-r * T) * N(d2);

}

}

**Step B: The Pricing Service**

Integrate this into your application flow.

1. Assume static parameters: $K=100$, $r=0.05$ (5%), $\sigma=0.2$ (20%), $T=1.0$ (1
    year).
2. When a **Trade** occurs (from Lab 8), take the trade price as $S$.
3. Calculate the new Call Price.

// Inside processTrade()

double spotPrice = trade.getPrice(); // The "S"

double strike = 100.0; // The "K"

double callPx = BlackScholes.callPrice(spotPrice, strike, 1.0, 0.05, 0.2);

// Broadcast this "Derivative Data" to UI

broadcaster.sendOptionUpdate("GOOG_JAN_100_CALL", callPx);

**Step C: UI Display**

Add a tile to your Angular Dashboard: "Theoretical Option Value".

Subscribe to the OPTION_UPDATE event and display the number.


## 6. VALIDATION & TESTING

1. **Unit Test:**
    o $S=100, K=100, T=1, r=0.05, \sigma=0.2$.
    o Result should be approx **$10.45**.
2. **System Test:**
    o Trade GOOG @ 100 -> UI shows Option @ 10.45.
    o Trade GOOG @ 110 -> UI Option Price should **increase** (approx 17.66).
    o Trade GOOG @ 90 -> UI Option Price should **decrease** (approx 5.09).
**7. ASSESSMENT**

Submit a screenshot of your UI showing the "Option Price" updating alongside the "Last Traded
Price" ticker.


## LAB 12: CAPSTONE - SYSTEM INTEGRATION AND FINAL DEMO

## 1. OBJECTIVE

The objective of this final session is to host and demonstrate the complete functionality of the
Order Management System. This is your "Go Live" event. You will demonstrate the full pipeline:
high-volume ingestion, matching, persistence, and real-time derivative pricing.

**2. PREREQUISITES**
    - **Completion of Labs 1-11.**
    - **Clean Database:** Truncate all tables (DELETE FROM orders; DELETE FROM
       executions;) before starting the demo.
**3. THE DEMO SCRIPT (CHECKLIST)**

Prepare your workstation to demonstrate the following 5 scenarios to the evaluator:

**Scenario 1: Infrastructure Health**

- Show systemctl status mysqld (Active).
- Show Java Application running without errors.
- Show Angular UI loaded in Chrome.

**Scenario 2: The "Happy Path" Trade**

- **Action:** In MiniFix, send **Buy 100 GOOG @ 150**.
- **Verify:** Order appears on UI as "Open".
- **Action:** In MiniFix, send **Sell 100 GOOG @ 150**.
- **Verify:**
    o UI shows "Trade Executed".
    o Option Price updates.
    o MiniFix status turns to "Filled".


**Scenario 3: The Order Book Logic**

- **Action:** Send **Buy 100 @ 100**.
- **Action:** Send **Buy 100 @ 101** (Better Price).
- **Action:** Send **Sell 100 @ 100**.
- **Verify:** The Sell order matches the **$101** Buy order (Price Priority), NOT the $100 order.

**Scenario 4: High Volume Scalability**

- **Action:** Configure MiniFix Generator: **500 Orders/Sec** for 10 seconds (5,000 orders).
- **Verify:**
    o UI updates rapidly (it might lag slightly, which is acceptable).
    o **No Crashes** in the Java Console.
    o After the test, SELECT COUNT(*) FROM orders matches the sent count.

**Scenario 5: Telemetry & Latency**

- **Action:** Open your Latency Logs (Lab 9).
- **Verify:** Show the "Average Latency" metric. Explain to the evaluator if it is in
    Microseconds or Milliseconds.
**4. FINAL DELIVERABLE**

Submit a **Project Archive (ZIP)** containing:

1. **Source Code:** Java (src) and Angular (src).
2. **Configuration:** order-service.cfg.
3. **Database Dump:** trading_system_schema.sql.
4. **Lab Report:** A summary document containing the Assessment Screenshots from Labs 1-
    11.


