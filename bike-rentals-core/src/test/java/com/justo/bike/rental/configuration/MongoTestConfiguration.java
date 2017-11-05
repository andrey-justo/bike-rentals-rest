package com.justo.bike.rental.configuration;

import java.io.IOException;
import java.net.UnknownHostException;

import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.Mongo;
import com.mongodb.MongoClient;

import de.flapdoodle.embed.mongo.MongodExecutable;
import de.flapdoodle.embed.mongo.MongodProcess;
import de.flapdoodle.embed.mongo.MongodStarter;
import de.flapdoodle.embed.mongo.config.MongodConfigBuilder;
import de.flapdoodle.embed.mongo.distribution.Version;

@Configuration
public class MongoTestConfiguration {

    private static final String DB_NAME = "mutant_tests";
    
    @Bean
    public MongodProcess mongoProcess() throws UnknownHostException, IOException {
        MongodStarter starter = MongodStarter.getDefaultInstance();
        MongodExecutable mongoExecutable = starter.prepare(new MongodConfigBuilder().version(Version.V3_4_1).build());
        return mongoExecutable.start(); 
    }
    
    @Bean
    public Mongo mongo() throws UnknownHostException {
        Mongo mongo = new MongoClient("127.0.0.1", 27017);
        mongo.getDB(DB_NAME);
        
        return mongo;
    }
    
    @Bean
    @Autowired
    public MongoTemplate mongoTemplate(Mongo mongo) {
        return new MongoTemplate(mongo, DB_NAME);
    }
    
    @PreDestroy
    public void destroy() {
        
    }
}
