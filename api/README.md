## api

### Overview

This project contains the definition of the Thrift API between server and clients.  
The interface definition layer (IDL) is located in:

```
src/main/thrift/token.thrift
```

In order to compile the Thrift IDL into code, you must have the 
[Thrift compiler](https://thrift.apache.org/tutorial/) installed on your machine.
Once you do, you can generate and compile the generated files using the following command:

```
./gradlew build
```

The generated files will be located in:

```
build/generated-sources/thrift
```
