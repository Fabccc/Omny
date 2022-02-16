package net.omny.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.Set;

import lombok.AllArgsConstructor;
import net.omny.server.WebServer;

public class CachingRequest {

    private static final byte[] EMPTY = {}; 

    private Map<String, CachedRequest> cache = new HashMap<>();

    public CachingRequest(WebServer webServer){
        webServer.getThreadPool().scheduleAtFixedRate(() -> {
            updateCache();
        }, 50, 2*1000, TimeUnit.MILLISECONDS);
        // Update the cache each 2 seconds
    }

    public CachingRequest(){
    }

    /**
     * 
     * @param path     The path of the URL of the request
     * @return
     */
    public int countRequest(String path) {
        if (!cache.containsKey(path))
            return 0;
        var rq = cache.get(path);
        return rq.isTimedOut() ? 0 : rq.count.get();
    }

    public void cacheRequest(String path) throws IllegalAccessException{
        if (cache.containsKey(path)) {
            var rq = cache.get(path);
            rq.updateAt = System.currentTimeMillis();
            rq.count.incrementAndGet();
        }else{
            throw new IllegalAccessException("You must cache the request first with the content");
        }
    }

    /**
     * 
     * the time is refreshed for each request performed to the caching system
     * 
     * @param path    The path of the URL of the request
     * @param content The content of the FULL REQUEST (headers and body)
     * @param time    How long the request must be cached when no request are done
     *                to the caching system
     */
    public void cacheRequest(String path, byte[] content, long time) {
        if (cache.containsKey(path)) {
            var rq = cache.get(path);
            rq.updateAt = System.currentTimeMillis();
            rq.count.incrementAndGet();
        } else {
            cache.put(path, new CachedRequest(content, path, time, System.currentTimeMillis(), new AtomicInteger(1)));
        }
    }

    /**
     * Retrieve and update the time of the caching request
     * 
     * 
     * @param path The path of the URL of the request
     * @return the full request as byte array if caching is found, otherwise empty
     *         byte array
     */
    public byte[] get(String path) {
        if (!cache.containsKey(path))
            return EMPTY;
        return cache.get(path).content;
    }

    public void updateCache() {
        Set<Entry<String, CachedRequest>> cacheEntry = this.cache.entrySet();


        for (var entry : cacheEntry) {
            var rq = entry.getValue();
            if (rq.isTimedOut()) {
                // This URL hasn't been request for a while
                // We must remove it from the the cache
                this.cache.remove(entry.getKey());
            }
        }
    }

    @AllArgsConstructor
    private class CachedRequest {

        public byte[] content;
        public String url;
        public long time;
        public long updateAt;
        public AtomicInteger count;

        public boolean isTimedOut(){
            return updateAt + time <  System.currentTimeMillis();
        }

    }

}
