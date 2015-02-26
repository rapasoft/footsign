package ch.erni.community;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

/**
 * @author rap
 */
@EnableAutoConfiguration
@ComponentScan
@Configuration
@EnableNeo4jRepositories(basePackages = {"ch.erni.community.footsign.repository"})
public class TestDataConfiguration extends Neo4jConfiguration {

	@Bean
	GraphDatabaseService graphDatabaseService() {
		return new TestGraphDatabaseFactory()
				.newImpermanentDatabaseBuilder()
				.setConfig(GraphDatabaseSettings.nodestore_mapped_memory_size, "10M")
				.setConfig(GraphDatabaseSettings.string_block_size, "60")
				.setConfig(GraphDatabaseSettings.array_block_size, "300")
				.newGraphDatabase();
	}

}

