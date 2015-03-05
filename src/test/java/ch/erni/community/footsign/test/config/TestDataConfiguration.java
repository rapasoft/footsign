package ch.erni.community.footsign.test.config;

import ch.erni.community.footsign.nodes.User;
import ch.erni.community.footsign.repository.UserRepositoryCustom;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseSettings;
import org.neo4j.test.TestGraphDatabaseFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.neo4j.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.config.Neo4jConfiguration;

/**
 * @author rap
 */
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "ch.erni.community.footsign")
@EnableNeo4jRepositories(basePackages = {"ch.erni.community.footsign.repository"},
		excludeFilters = @ComponentScan.Filter(value = UserRepositoryCustom.class, type = FilterType.ASSIGNABLE_TYPE))
public class TestDataConfiguration {

	@Bean
	GraphDatabaseService graphDatabaseService() {
		return new TestGraphDatabaseFactory()
				.newImpermanentDatabaseBuilder()
				.setConfig(GraphDatabaseSettings.nodestore_mapped_memory_size, "10M")
				.setConfig(GraphDatabaseSettings.string_block_size, "60")
				.setConfig(GraphDatabaseSettings.array_block_size, "300")
				.newGraphDatabase();
	}

	@Configuration
	static class Neo4jTestConfig extends Neo4jConfiguration {

		Neo4jTestConfig() {
			setBasePackage(User.class.getPackage().getName());
		}

	}

}

