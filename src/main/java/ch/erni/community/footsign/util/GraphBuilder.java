package ch.erni.community.footsign.util;

import ch.erni.community.footsign.dto.CustomPlayerDTO;
import ch.erni.community.footsign.dto.TeamPlayersDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class GraphBuilder {

	private String serializeData(String categoryName, String categoryValue, MappingFunction<ArrayNode, ObjectMapper> mappingFunction) {
		ObjectMapper mapper = new ObjectMapper();

		// create header
		ArrayNode parentArray = mapper.createArrayNode();
		ArrayNode first = mapper.createArrayNode();
		first.add(categoryName);
		first.add(categoryValue);
		parentArray.add(first);

		mappingFunction.apply(parentArray, mapper);

		return parentArray.toString();
	}

	public String serializeDataForChart(String categoryName, String categoryValue, Map<String, Integer> values) {
		return serializeData(categoryName, categoryValue, (parentArray, mapper) -> {
			if (values != null) {

				values.forEach((k, v) -> {
					ArrayNode arr = mapper.createArrayNode();
					arr.add(k);
					arr.add(v);
					parentArray.add(arr);
				});

			}
		});
	}

	public <VALUE extends Number> String serializeDataForChart(String categoryName, String categoryValue, List<CustomPlayerDTO<VALUE>> values) {
		return serializeData(categoryName, categoryValue, (parentArray, mapper) -> {
			if (values != null) {
				values.forEach(v -> {
					ArrayNode arr = mapper.createArrayNode();
					arr.add(v.getPlayer().getFullName());
					arr.add(v.getValue().doubleValue());
					parentArray.add(arr);
				});

			}
		});
	}

	public <VALUE extends Number> String serializeDataForTeamChart(String categoryName, String categoryValue, List<TeamPlayersDTO<VALUE>> values) {
		return serializeData(categoryName, categoryValue, (parentArray, mapper) -> {
			if (values != null) {

				values.forEach(v -> {
					ArrayNode arr = mapper.createArrayNode();
					arr.add(v.getTeamDomainNames());
					arr.add(v.getValue().intValue());
					parentArray.add(arr);
				});

			}
		});
	}

	interface MappingFunction<ARRAY_NODE, MAPPER> {
		void apply(ARRAY_NODE array_node, MAPPER mapper);
	}

}
