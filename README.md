# Registry
This registry serves as service name resolution. It stores services' information, such as port, protocol, and IP.
It was created to solve a specific problem: 
Where can your service/device be found in a private network, when you have no network configuration control and IPs keep changing?

This registry is a solution to that problem. Services can 'register' to the registry, and apps can access this information through the 'retrieve' endpoint. The endpoints are protected with an API_KEY, but it's vulnerable to [playback attacks](https://en.wikipedia.org/wiki/Replay_attack). 
To retrieve information from the registry I've also created a [registry-client library](https://github.com/DiogoAluai/registry-client).


#### Deploying docker image
```bash
docker build -t registry .
docker run -d -p HOST_PORT:8080 registry
```
