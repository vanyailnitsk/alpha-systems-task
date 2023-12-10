package com.example.nistcpeapi.json;
import com.example.nistcpeapi.models.CPE;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.*;

public class CustomCpeListDeserializer extends StdDeserializer<List<CPE>> {

    public CustomCpeListDeserializer() {
        this(null);
    }

    public CustomCpeListDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public List<CPE> deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);

        List<CPE> cpeList = new ArrayList<>();
        if (node.isArray()) {
            Iterator<JsonNode> elements = node.elements();
            while (elements.hasNext()) {
                JsonNode productNode = elements.next();
                JsonNode cpeNode = productNode.path("cpe");
                CPE cpe = objectMapper.treeToValue(cpeNode, CPE.class);
                cpe.setDeprecatedBy(parseDeprecatedBy(cpeNode));
                cpe.setDeprecates(parseDeprecates(cpeNode));
                cpeList.add(cpe);
            }
        }
        return cpeList;
    }
    private Set<CPE> parseDeprecatedBy(JsonNode node) {
        Set<CPE> deprecatedByList = new HashSet<>();
        JsonNode deprecatedByNode = node.path("deprecatedBy");
        if (deprecatedByNode.isArray()) {
            for (JsonNode itemNode : deprecatedByNode) {
                CPE deprecatedBy = new CPE();
                deprecatedBy.setCpeName(itemNode.path("cpeName").asText());
                deprecatedBy.setCpeNameId(UUID.fromString(itemNode.path("cpeNameId").asText()));
                deprecatedByList.add(deprecatedBy);
                if (deprecatedBy.getCpeNameId().equals(UUID.fromString("BB60A0E4-B292-495C-8A06-2F0CC2A76620"))) {
                    System.out.println(deprecatedBy);
                    System.out.println(deprecatedByList);
                }
            }
        }
        return deprecatedByList;
    }
    private Set<CPE> parseDeprecates(JsonNode node) {
        Set<CPE> deprecatesList = new HashSet<>();
        JsonNode deprecatesNode = node.path("deprecates");
        if (deprecatesNode.isArray()) {
            Iterator<JsonNode> elements = deprecatesNode.elements();
            while (elements.hasNext()) {
                JsonNode deprecatesItemNode = elements.next();
                CPE deprecates = new CPE();
                deprecates.setCpeName(deprecatesItemNode.path("cpeName").asText());
                deprecates.setCpeNameId(UUID.fromString(deprecatesItemNode.path("cpeNameId").asText()));
                deprecatesList.add(deprecates);
            }
        }
        return deprecatesList;
    }
}

