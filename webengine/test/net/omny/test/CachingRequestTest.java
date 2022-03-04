package net.omny.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import net.omny.cache.CachingRequest;

public class CachingRequestTest {

    public static final byte[] RESPONSE = "HTTP/1.1 200 OK\r\n\r\n<html></html>\r\n\r\n".getBytes();
    private CachingRequest cachingRequest;

    @Before
    public void before() {
        this.cachingRequest = new CachingRequest();
    }

    @Test
    public void testCache0() throws InterruptedException {
        // Store it, set time of cache at 0ms
        cachingRequest.cacheRequest("/", RESPONSE, 10);
        
        // Simulate inactive state of the server
        // The server hasn't receive a request for 50 ms
        // And the cache for the request with path "/" is set to 10ms
        Thread.sleep(50); 

        cachingRequest.updateCache(); // it must remove the response from the cache
        assertEquals(0, cachingRequest.countRequest("/"));
    }

    @Test
    public void testCache1000() {
        // Store it, set time of cache at 0ms
        cachingRequest.cacheRequest("/", RESPONSE, 10000);
        cachingRequest.updateCache(); // it must remove the response from the cache
        assertEquals(1, cachingRequest.countRequest("/"));
    }


    @Test
    public void testCacheMultiple() throws IllegalAccessException {
        // Store it, set time of cache at 0ms
        cachingRequest.cacheRequest("/", RESPONSE, 10000);
        cachingRequest.updateCache(); // it must remove the response from the cache
        assertEquals(1, cachingRequest.countRequest("/"));
        cachingRequest.cacheRequest("/");
        cachingRequest.cacheRequest("/");
        cachingRequest.cacheRequest("/");
        cachingRequest.cacheRequest("/");
        assertEquals(5, cachingRequest.countRequest("/"));
    }

}
