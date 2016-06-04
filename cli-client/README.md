## cli-client

### Overview

This project is a basic implementation of a text-based, single-threaded 
Nim client.  It uses the Thrift client API in order to make blocking
calls to Thrift endpoints on a `server`.

### How to run

You can run this command-line client from the project directory using the following command:

```
./gradlew -q run (-Pargs="(playerName) (serverUrl) (serverPort)")
```

The optional positional arguments are as follows

argument | default
-------- | -------
playerName | Anonymous
serverUrl | localhost
serverPort | 9090

