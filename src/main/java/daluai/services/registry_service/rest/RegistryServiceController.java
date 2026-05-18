package daluai.services.registry_service.rest;


import daluai.lib.network_utils.RequestResult;
import daluai.lib.registry_api.Coms;
import daluai.lib.registry_api.Service;
import daluai.lib.registry_api.ServiceType;
import daluai.lib.services_parent.DaluaiServiceRestController;
import daluai.services.registry_service.store.ServiceHashMapStorage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static daluai.lib.registry_api.Coms.ENDPOINT_DEREGISTER;
import static daluai.lib.registry_api.Coms.ENDPOINT_REGISTER;
import static daluai.lib.registry_api.Coms.ENDPOINT_RESET;
import static daluai.lib.registry_api.Coms.ENDPOINT_RETRIEVE;
import static daluai.lib.registry_api.Coms.ENDPOINT_RETRIEVE_ALL;

/**
 * Registry
 */
@RestController
class RegistryServiceController extends DaluaiServiceRestController {

    private final static Logger LOG = LoggerFactory.getLogger(RegistryServiceController.class);

    private final ServiceHashMapStorage serviceRegistryMap;

    @Autowired
    public RegistryServiceController(Environment environment) {
        this.serviceRegistryMap = new ServiceHashMapStorage();
    }

    @Override
    public String getServiceName() {
        return Coms.SERVICE_NAME;
    }

    /**
     * Retrieves requested service.
     */
    @GetMapping(ENDPOINT_RETRIEVE + "/{service}")
    public Service retrieve(@PathVariable String service) {
        return serviceRegistryMap.get(service);
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
        LOG.info("Registering service: {}", service);
        serviceRegistryMap.put(service.name(), service);
    }

    /**
     * Delete service information.
     */
    @GetMapping(ENDPOINT_DEREGISTER + "/{serviceName}")
    public void deregister(@PathVariable String serviceName) {
        LOG.info("Deregistering service: {}", serviceName);
        serviceRegistryMap.remove(serviceName);
    }

    /**
     * Delete all services.
     */
    @GetMapping(ENDPOINT_RESET)
    public RequestResult reset() {
        LOG.warn("Resetting registry");
        serviceRegistryMap.clear();
        return RequestResult.OK;
    }

    /**
     * Add test service, for testing purposes.
     */
    @GetMapping("/test")
    public void test() {
        // asd, the new foo!
        Service testService = new Service("asd", "asd", "asd", "port", ServiceType.PRIVATE);
        LOG.info("Registering test service: {}", testService);
        serviceRegistryMap.put(testService.name(), testService);
    }
}