package ch.erni.community.footsign.util;

import ch.erni.community.footsign.dto.CustomPlayerDTO;
import ch.erni.community.footsign.dto.TeamPlayersDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * Created by cepe on 06.03.2015.
 */

@Component
public class GraphBuilder {

	public String serializeDataForChart(String categoryName, String categoryValue, Map<String, Integer> values) {
		ObjectMapper mapper = new ObjectMapper();
        
        // create header
        ArrayNode parentArray = mapper.createArrayNode();
        ArrayNode first = mapper.createArrayNode();
        first.add(categoryName);
        first.add(categoryValue);
        parentArray.add(first);
        
        // create data
        if (values != null) {

            values.forEach((k, v) -> {
                ArrayNode arr = mapper.createArrayNode();
                arr.add(k);
                arr.add(v);
                parentArray.add(arr);
            });

        }
        return parentArray.toString();
    }

	public <VALUE extends Number> String serializeDataForChart(String categoryName, String categoryValue, List<CustomPlayerDTO<VALUE>> values) {
		ObjectMapper mapper = new ObjectMapper();

        // create header
        ArrayNode parentArray = mapper.createArrayNode();
        ArrayNode first = mapper.createArrayNode();
        first.add(categoryName);
		first.add(categoryValue);
		parentArray.add(first);

        // create data
        if (values != null) {

            values.forEach(v -> {
				ArrayNode arr = mapper.createArrayNode();
				arr.add(v.getPlayer().getFullName());
				arr.add(v.getValue().doubleValue());
				parentArray.add(arr);
			});

        }
        return parentArray.toString();
    }

    public <VALUE extends Number> String serializeDataForTeamChart(String categoryName, String categoryValue, List<TeamPlayersDTO<VALUE>> values) {
        ObjectMapper mapper = new ObjectMapper();

        // create header
        ArrayNode parentArray = mapper.createArrayNode();
        ArrayNode first = mapper.createArrayNode();
        first.add(categoryName);
        first.add(categoryValue);
        parentArray.add(first);

        // create data
        if (values != null) {

            values.forEach(v -> {
                ArrayNode arr = mapper.createArrayNode();
                arr.add(v.getTeamDomainNames());
                arr.add(v.getValue().intValue());
                parentArray.add(arr);
            });

        }
        return parentArray.toString();
    }

}
