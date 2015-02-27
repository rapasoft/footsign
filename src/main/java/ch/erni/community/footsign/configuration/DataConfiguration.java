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

	// TODO @rap: load from properties file
	public static final String NEO4J_DB_PATH = "C:\\neo4j.db";

	public DataConfiguration() {
		setBasePackage("ch.erni.community.footsign");
	}

	@Bean
	static GraphDatabaseService graphDatabaseService() {
		return new GraphDatabaseFactory().newEmbeddedDatabase(NEO4J_DB_PATH);
	}

}
