# RestApi
This application creates a REST API using Play and supports serving lines out of a file to clients.
## Building
You need ```sbt``` to build and deploy this application. Download the project, unzip, and execute the following script from inside the unzipped folder
```$xslt
./build.sh
```
This will pull in all the dependencies and deploy the application.

## Running
In order to run the application, execute the following script
```$xslt
./start.sh <path of file>
```
which will start the server and start serving lines out of the specified file.

## Sample Calls
Line numbers start from 1. The following request will get the first line from file
```$xslt
http://localhost:9000/lines/1
```
If you provide a line number less than 1 or greater than the total number of lines in the file, you will get HTTP 413 and an index out of bounds message.
The same message will be displayed if your file is empty.