package com.tr;

import com.mongodb.Mongo;
import com.tr.ldap.LdapConfig;
import com.tr.ldap.LdapService;
import com.unboundid.ldap.sdk.LDAPConnection;
import com.unboundid.ldap.sdk.LDAPConnectionPool;
import com.unboundid.ldap.sdk.LDAPException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@ComponentScan (basePackages = "com.tr.*")
@Configuration
@PropertySource("classpath:/config.properties")
@EnableMongoRepositories
public class AppConfiguration {

    @Autowired private LdapService ldapService;
    @Autowired private LdapConfig ldapConfig;

    public @Bean
    MongoDbFactory mongoDbFactory() throws Exception {
        return new SimpleMongoDbFactory(new Mongo(), "syncTrials");
    }

    public @Bean
    MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoDbFactory());
        return mongoTemplate;
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    public LDAPConnectionPool ldapConnectionPool() throws LDAPException {
        LDAPConnection ldapConnection = ldapService.createConnection(ldapConfig);
        return new LDAPConnectionPool(ldapConnection, 50);
    }
}
