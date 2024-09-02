package daluai.services.registry_service;

import daluai.lib.registry_api.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import static daluai.services.registry_service.FileSystemUtils.tryCreatingFile;

/**
 * Map wrapper that persists data in dedicated file.
 */
public class ServiceHashMapStorage extends HashMap<String, Service> {

    private static final Logger LOG = LoggerFactory.getLogger(ServiceHashMapStorage.class);
    private static final String PROD_FILE_PATH = "/app_data/hashmap.ser";
    private static final String LOCAL_FILE_PATH = "app_data/hashmap.ser";

    private final File storageFile;


    public ServiceHashMapStorage(boolean hasLocalProfile) {
        String filePath = hasLocalProfile ? LOCAL_FILE_PATH : PROD_FILE_PATH;
        this.storageFile = new File(filePath);
        if (!storageFile.exists()) {
            // we can try at least
            tryCreatingFile(storageFile);
        }
    }

    @Override
    public Service get(Object key) {
        return load().get(key);
    }

    @Override
    public Service put(String key, Service value) {
        var map = load();
        var service = map.put(key, value);
        save(map);
        return service;
    }

    @Override
    public void clear() {
        save(new HashMap<>());
    }

    @Override
    public Service remove(Object key) {
        var map = load();
        var service = map.remove(key);
        save(map);
        return service;
    }

    public HashMap<String, Service> retrieveAll() {
        return load();
    }

    private void save(HashMap<String, Service> map) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(storageFile))) {
            oos.writeObject(map);
        } catch (IOException e) {
            LOG.error("Error saving service map", e);
        }
    }

    @SuppressWarnings("unchecked")
    public HashMap<String, Service> load() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(storageFile))) {
            HashMap<String, Service> registryHashMap = (HashMap<String, Service>) ois.readObject();
            System.out.println("[REGISTRY] HashMap retrieved: " + registryHashMap);
            return registryHashMap;
        } catch (Exception e) {
            System.out.println("[REGISTRY] Impossible to retrieve storage. Using empty HashMap");
            var emptyMap = new HashMap<String, Service>();
            save(emptyMap);
            return emptyMap; // Return an empty map in case of failure
        }
    }

}
