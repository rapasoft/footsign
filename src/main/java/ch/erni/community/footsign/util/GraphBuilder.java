package ch.erni.community.footsign.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * Created by cepe on 06.03.2015.
 */

@Component
public class GraphBuilder {
    
    public String dataForPieGraph(String categoryName, String categoryValue, Map<String, Integer> values) {
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
}