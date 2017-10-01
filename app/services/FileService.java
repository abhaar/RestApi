package services;

import com.typesafe.config.ConfigFactory;
import org.ehcache.Cache;
import org.ehcache.CacheManager;
import org.ehcache.config.builders.ResourcePoolsBuilder;
import play.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;

import static org.ehcache.config.builders.CacheConfigurationBuilder.newCacheConfigurationBuilder;
import static org.ehcache.config.builders.CacheManagerBuilder.newCacheManagerBuilder;

public class FileService {
    private final String filename;
    private CacheManager cacheManager;
    private final Long cacheSize;
    private final long count;

    public FileService() {
        filename = ConfigFactory.load().getString("filename");
        cacheSize = ConfigFactory.load().getLong("cacheSize");
        count = setCount();
        initializeCache();
        Logger.info("Using file at location " + filename);
        Logger.info("Cache size set at " + cacheSize);
        Logger.info("Total lines in file = " + count);
    }

    public Optional<String> getLine(long index) {
        if (index > count || index < 1) return Optional.empty();
        try {
            if (!cache().containsKey(index)) {
                Logger.info("Could not find value in Cache. Reading from file...");
                Optional<String> line = Files.lines(Paths.get(filename)).skip(index - 1).findFirst();
                line.ifPresent(s -> cache().put(index, s));
            } else {
                Logger.info("Fetching entry from Cache!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.of(cache().get(index));
    }

    private void initializeCache() {
        try {
            cacheManager = newCacheManagerBuilder()
                    .withCache("fileCache",
                            newCacheConfigurationBuilder(Long.class, String.class, ResourcePoolsBuilder.heap(cacheSize)))
                    .build(true);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private Cache<Long, String> cache() {
        return cacheManager.getCache("fileCache", Long.class, String.class);
    }

    private long setCount() {
        try {
            return Files.lines(Paths.get(filename)).count();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Long.MAX_VALUE;
    }
}
