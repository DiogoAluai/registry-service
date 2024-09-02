package daluai.services.registry_service;


import daluai.lib.network_utils.RequestResult;
import daluai.lib.registry_api.Coms;
import daluai.lib.registry_api.Service;
import daluai.lib.registry_api.ServiceType;
import daluai.lib.services_parent.DaluaiServiceRestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.HashMap;

import static daluai.lib.registry_api.Coms.ENDPOINT_DEREGISTER;
import static daluai.lib.registry_api.Coms.ENDPOINT_REGISTER;
import static daluai.lib.registry_api.Coms.ENDPOINT_RESET;
import static daluai.lib.registry_api.Coms.ENDPOINT_RETRIEVE;
import static daluai.lib.registry_api.Coms.ENDPOINT_RETRIEVE_ALL;
import static daluai.lib.registry_api.Service.RELAYED_SERVICE_SUFFIX;

/**
 * Registry.
 * Relay aware registry, for fetching and storing Services' information, such as type and url.
 */
@RestController
class RegistryServiceController extends DaluaiServiceRestController {

    private static final String PROFILE_LOCAL = "local";

    private final static Logger LOG = LoggerFactory.getLogger(RegistryServiceController.class);

    private final ServiceHashMapStorage serviceRegistryMap;
    private final Object lock = new Object();

    @Autowired
    public RegistryServiceController(Environment environment) {
        boolean hasLocalProfile = Arrays.asList(environment.getActiveProfiles()).contains(PROFILE_LOCAL);
        this.serviceRegistryMap = new ServiceHashMapStorage(hasLocalProfile);
    }

    @Override
    public String getServiceName() {
        return Coms.SERVICE_NAME;
    }

    /**
     * Retrieves requested service.
     * If not found it will check for relayed implementations, i.e. service with "-relay" suffix.
     */
    @GetMapping(ENDPOINT_RETRIEVE + "/{service}")
    public Service retrieve(@PathVariable String service) {
        synchronized (lock) {
            return retrieveService(service);
        }
    }

    /**
     * Retrieves requested service.
     * If not found it will check for relayed implementations, i.e. service with "-relay" suffix.
     */
    private Service retrieveService(String service) {
        Service result = serviceRegistryMap.get(service);
        if (result != null) {
            return result;
        }
        // ~ if service is not found, search for relayed implementation
        return serviceRegistryMap.load()
                .values()
                .stream()
                .filter(s -> s.name().contains(service) && s.name().contains(RELAYED_SERVICE_SUFFIX))
                .findFirst()
                .orElse(null);
    }

    /**
     * Fetch all services.
     */
    @GetMapping(ENDPOINT_RETRIEVE_ALL)
    public HashMap<String, Service> retrieveAll() {
        return serviceRegistryMap.retrieveAll();
    }

    /**
     * Store service information.
     */
    @PostMapping(ENDPOINT_REGISTER)
    public void register(@RequestBody Service service) {
        LOG.info("Registering service: " + service);
        serviceRegistryMap.put(service.name(), service);
    }

    /**
     * Delete service information.
     */
    @GetMapping(ENDPOINT_DEREGISTER + "/{serviceName}")
    public void deregister(@PathVariable String serviceName) {
        LOG.info("Registering service: " + serviceName);
        synchronized (lock) {
            serviceRegistryMap.remove(serviceName);
        }
    }

    /**
     * Delete all services.
     */
    @GetMapping(ENDPOINT_RESET)
    public RequestResult reset() {
        LOG.warn("Resetting registry");
        synchronized (lock) {
            serviceRegistryMap.clear();
        }
        return RequestResult.OK;
    }

    /**
     * Add test service, for testing purposes.
     */
    @GetMapping("/test")
    public void test() {
        // asd, the new foo!
        Service testService = new Service("asd", "asd", "asd", "port", ServiceType.PRIVATE);
        LOG.info("Registering service: " + testService);
        synchronized (lock) {
            serviceRegistryMap.put(testService.name(), testService);
        }
    }
}