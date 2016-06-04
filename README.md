## `token-game`

### Overview

This is the parent project of a group of subprojects that implement a [Nim](https://en.wikipedia.org/wiki/Nim) server, AI, and command-line client.

I wrote it in a single 24-hour sitting.

Subproject | Description
---------- | -----------
`api` | the Thrift RPC API bewteen the `server` and all clients
`ai` | an AI player/client of the `server`
`cli-client` | a basic command-line client of the `server`
`server` | a server that hosts multiple concurrent Nim games


### How to run

Each of the subprojects (except `api`) may be started from their project home directories 
using the command:

```
./gradlew run
```

For optimal experience, the `cli-client` should be run with the additional `-q` flag:

```
./gradlew -q run
```

The server and clients will default to `localhost:9090` for the server URL and port.  For more 
information on optional command line parameters, please see the individual `README.me` files
in each of the subprojects.

Note that in order to compile the project, you will need to install the
Thrift compiler.  See [api/README.md](api/README.md)

### Gameplay

The game starts with a random number of tokens in a random number of buckets.
2 players alternate turns; the first to play is chosen at random.

On a player's turn, (s)he must remove 1 or more (up to all of the token in a
bucket) from a single bucket with tokens.  The turn then passes to the other
player.

The player who takes the last token wins!


### Development environment

This project was built and tested using the following environment:

Environment | Versions
----------- | --------
OS | OS X Yosemite 10.10.4, OS X El Capitain 10.11.3
Java | 1.7.0_80-b15, 1.8.0_66-b17

