package org.learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.guava.GuavaCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring boot main Application
 * 
 */
@SpringBootApplication
@EnableTransactionManagement
@EnableCaching
@EnableScheduling
@EnableAsync
public class Application 
{
    public static void main( String[] args )
    {
        SpringApplication.run(Application.class, args);
    }
    
    /*@Bean (for non-prod environments)
    public CacheManager cacheManager() {
    	ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager("greetings");
    	return cacheManager;
    }*/
    
    @Bean//for prod env but which has single node. It is for local caching. For multiple nodes, we should have distributed cache like elasticcache
    public CacheManager cacheManager() {
    	GuavaCacheManager cacheManager = new GuavaCacheManager("greetings");
    	return cacheManager;
    }
}
