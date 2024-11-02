# Registry
This registry serves as service name resolution. It stores services' information, such as port, protocol, and IP.
It was created to solve a specific problem: 
Where can your service/device be found in a private network, when you have no network configuration control and IPs keep changing?

This registry is a solution to that problem. Services can 'register' to the registry, and apps can access this information through the 'retrieve' endpoint. The endpoints are protected with an API_KEY, but it's vulnerable to [playback attacks](https://en.wikipedia.org/wiki/Replay_attack). 
To retrieve information from the registry I've also created a [registry-client library](https://github.com/DiogoAluai/registry-client).

## Configuration
#### Deploying to Fly.io
For provided configuration, you need a mount storage as in "/app_data", which you have to configure in fly.io.
You'll also need to set environment variable "`COOL_REGISTRY_FLY_NAME`" with your fly app name. 
This env will be used for both deploying to fly and to extrapolate url/location to be used by `RegistryClient`.
After that, there's a `final.sh` script you can run to compile and deploy.

#### Deploying to fly.io
Check fly.io installation documentation for flyctl, then:

```bash
$ flyctl apps list # prompts for login, very intuitive
```


Final script will be your friend. Registry requires a volume, which will be automatically created upon running the script.
It will compile the java application locally (you may need to run it as "mvn -s \$MAVEN_HOME/conf/settings.xml clean package"), and then copy this jar from the target folder to fly.io.
Instead of compiling it locally, you can download it from github releases.

```bash
$ final.sh
```

##### Explanation:
Registry persists data to a file. Default file path is `/app_data/hashmap.ser`, and it works in conjunction with provided `fly.io` configuration. 
When running locally, registry would try to create the folder but you require root privileges to do that. So you could run with sudo instead:  `sudo java -jar registry-service-2.0.0.jar`, but why risk it?
A 'local' profile is added that changes filepath to `./app_data/hashmap.ser`.

#### Running locally
To run locally, just pass 'local' as spring profile. You can incorporate `LOCAL_INSTANCE` of `RegistryClient` in your apps.
```bash
$ mvn package # compile

# run
$ java -jar -Dspring.profiles.active=local target/registry-service-2.0.0.jar # adapt jar name as needed
```
