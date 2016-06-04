## server

### Overview

This project implements a Nim server using [Apache Thrift](https://thrift.apache.org/)
to expose an RPC API to clients.

### Design goals

The primary design goals for the initial design were the following:

* allow for multiple, heterogeneous clients in the future; thus:
  * the API should be technology-agnostic
  * only game state should be passed between the server and clients - not display data
* lay the foundation for strong game security:
  * each player receives a unique player-game token for each game (s)he is playing - 
    in the event that a single player-game token is discovered, this limits the 
    compromise to a single player in a single game
* support multiple simultaneous games on the server 
* allow for a single user to play multiple games simultaneously (with future multithreaded clients)
* thread-safety with multiple clients
* high performance by limiting concurrency locks to a single game rather than at a user or 
  server level

### Benefits of Thrift

* well-defined and self-documenting schema
* RPC calls with exceptions
* variety of transport serialization formats from easy-to-read text to compact binary
* support for [16 programming langauges](http://wiki.apache.org/thrift/LibraryFeatures?action=show&redirect=LanguageSupport)

### How to run

You can run the server from the project directory using the following command:

```
./gradlew run (-Pargs="(portNumber)")
```

The optional positional arguments are as follows:

argument | default
-------- | -------
portNumber | 9090
```
