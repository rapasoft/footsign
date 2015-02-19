package ch.erni.community.footsign.configuration;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

/**
 * @author rap
 */
@Configuration
@EnableNeo4jRepositories(basePackages = {"ch.erni.community.footsign.repository"})
public class DataConfiguration extends Neo4jConfiguration {

	public DataConfiguration() {
		setBasePackage("ch.erni.community.footsign");
	}

	@Bean
	GraphDatabaseService graphDatabaseService() {
		return new GraphDatabaseFactory().newEmbeddedDatabase("accessingdataneo4j.db");
	}

}
