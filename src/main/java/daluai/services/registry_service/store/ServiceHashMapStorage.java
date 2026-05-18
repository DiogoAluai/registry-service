package daluai.services.registry_service.store;

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
import java.util.Optional;

import static daluai.services.registry_service.FileSystemUtils.tryCreatingFile;

/**
 * Map wrapper that persists data in dedicated file.
 * <br>
 * Not proud of this one, the class does too much.
 * Locks every public method, to prevent parallel shenanigans
 */
public class ServiceHashMapStorage extends HashMap<String, Service> {

    private static final Object lock = new Object();

    private static final Logger LOG = LoggerFactory.getLogger(ServiceHashMapStorage.class);
    private static final String STORE_FILE_PATH =
            Optional.ofNullable(System.getenv("registry.store.path")).orElse("/app_data/hashmap.ser");

    private final File storageFile;

    public ServiceHashMapStorage() {
        this.storageFile = new File(STORE_FILE_PATH);
        if (!storageFile.exists()) {
            // we can at least try
            tryCreatingFile(storageFile);
        }
    }

    @Override
    public Service get(Object key) {
        synchronized (lock) {
            return load().get(key);
        }
    }

    @Override
    public Service put(String key, Service value) {
        synchronized (lock) {
            var map = load();
            var service = map.put(key, value);
            save(map);
            return service;
        }
    }

    @Override
    public void clear() {
        synchronized (lock) {
            save(new HashMap<>());
        }
    }

    @Override
    public Service remove(Object key) {
        synchronized (lock) {
            var map = load();
            var service = map.remove(key);
            save(map);
            return service;
        }
    }

    public HashMap<String, Service> retrieveAll() {
        synchronized (lock) {
            return load();
        }
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
            LOG.info("HashMap retrieved: {}", registryHashMap);
            return registryHashMap;
        } catch (Exception e) {
            LOG.info("Impossible to retrieve storage. Using empty HashMap");
            var emptyMap = new HashMap<String, Service>();
            save(emptyMap);
            return emptyMap; // Return an empty map in case of failure
        }
    }

}
