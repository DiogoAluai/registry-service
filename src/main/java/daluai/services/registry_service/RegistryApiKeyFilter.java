package daluai.services.registry_service;

import daluai.lib.network_utils.property.PropertyManager;
import daluai.lib.services_parent.ApiKeyFilter;
import org.springframework.stereotype.Component;

import static daluai.lib.network_utils.property.PropertyKeys.API_KEY;

@Component
public class RegistryApiKeyFilter extends ApiKeyFilter {
    @Override
    protected String getApiKey() {
        return PropertyManager.DEFAULT_INSTANCE.getProperty(API_KEY);
    }
}
