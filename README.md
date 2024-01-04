# About

A simple and effective assertion for building a JSON string in the Policy Manager.

This is intended to be used with the composite assertion: "Run Assertions for Each Item".

# Installation

1. **Clone the repository:**
    ```bash
    git clone https://github.com/geavenx/JSON-Builder-Custom-assertion.git && cd JSON-Builder-Custom-assertion
    ```

2. **Build the project with gradle**
    ```bash
    ./gradlew build
    ```

3. **Send the jar file to the API Gateway**
    The jar is located at *./build/libs/jsonBuilderJava11-1.0-SNAPSHOT.jar*, and should be placed in
    /opt/SecureSpan/Gateway/runtime/modules/lib/

4. **Restart the gateway**
    service ssg restart

#License

This project is under the [GPL License](LICENSE). 

