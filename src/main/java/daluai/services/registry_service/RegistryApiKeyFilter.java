package daluai.services.registry_service;

import daluai.lib.registry_api.Coms;
import daluai.lib.services_parent.ApiKeyFilter;
import org.springframework.stereotype.Component;

@Component
public class RegistryApiKeyFilter extends ApiKeyFilter {
    @Override
    protected String getApiKey() {
        return Coms.API_KEY;
    }
}
