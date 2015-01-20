# demo-manual-composition
Demo application of our SANER 2015 paper

## Installation 
In order to run this application, you need to have Glasshfish installed on your system, and have the 
executables in the Glasshfish's bin/ folder available in your PATH.

### Compilation
Run `mvn package`, which will generate two WAR files, in 
`basic-services/target/jaxws-manual-composition-basic-services.war` and 
`compositions/target/jaxws-manual-composition.war`.

You will need to deploy them using `asadmin deploy`.

### Database setup
You just have to run `init-db.sh` to initialize the database that the web services needs.

