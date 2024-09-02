# Your own Cool Registry

## Configuration
#### Deploying to Fly.io
For provided configuration, you need a mount storage as in "/app_data", which you have to configure in fly.io.
You'll also need to set environment variable "`COOL_REGISTRY_FLY_NAME`" with your fly app name. 
This env will be used for both deploying to fly and to extrapolate url/location to be used by `RegistryClient`.
After that, there's a `final.sh` script you can run to compile and deploy.

##### Explanation:
Registry persists data to a file. Default file path is `/app_data/hashmap.ser`, and it works in conjunction with provided `fly.io` configuration. 
When running locally, registry would try to create the folder but you require root privileges to do that. So you could run with sudo instead:  `sudo java -jar registry-service-2.0.0.jar`, but why risk it?
A 'local' profile is added that changes filepath to `./app_data/hashmap.ser`.

#### Running locally
To run locally, just pass 'local' as spring profile. You can incorporate `LOCAL_INSTANCE` of `RegistryClient` in your apps.
```bash
# compile
mvn package

# run
java -jar -Dspring.profiles.active=local target/registry-service-2.0.0.jar # adapt jar name as needed
```
