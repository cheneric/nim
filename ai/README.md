## ai

### Overview

This project implements a single-threaded "Token Game" AI client.

### Algorithm

It implements the following algorithm:

	1. Let the "Token Sum" be defined as the binary sum of the number of tokens in
all of the buckets but not carrying.  (That is, write the number of tokens in
each bucket as a binary number, add them, but do not carry while adding.)

	2. If possible, make a move so that the "Token Sum" will be 0 after the move.

	3. Otherwise, remove a single token from a random bucket.


### How to run

You can run this command-line client from the project directory using the following command:

```
./gradlew run (-Pargs="(aiName) (serverUrl) (serverPort)")
```

The optional positional arguments are as follows

argument | default
-------- | -------
aiName | AI
serverUrl | localhost
serverPort | 9090

