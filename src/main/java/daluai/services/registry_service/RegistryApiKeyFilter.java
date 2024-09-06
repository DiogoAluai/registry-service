package daluai.services.registry_service;

import daluai.lib.network_utils.property.PropertyKeys;
import daluai.lib.services_parent.EncryptedApiKeyFilter;
import org.springframework.stereotype.Component;

import static daluai.lib.network_utils.property.PropertyKeys.REGISTRY_API_KEY_SHA512_DIGEST;
import static daluai.lib.registry_api.Coms.REGISTRY_PROPERTY_MANAGER;

@Component
public class RegistryApiKeyFilter extends EncryptedApiKeyFilter {
    @Override
    protected String getAESSecret() {
        return REGISTRY_PROPERTY_MANAGER.getProperty(PropertyKeys.REGISTRY_API_KEY_AES_SECRET);
    }

    @Override
    protected String getHashedApiKey() {
        return REGISTRY_PROPERTY_MANAGER.getProperty(REGISTRY_API_KEY_SHA512_DIGEST);
    }
}
