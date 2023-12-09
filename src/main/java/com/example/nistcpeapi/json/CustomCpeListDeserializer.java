package com.example.nistcpeapi.json;
import com.example.nistcpeapi.models.CPE;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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
    private List<CPE.DeprecatedBy> parseDeprecatedBy(JsonNode node) {
        List<CPE.DeprecatedBy> deprecatedByList = new ArrayList<>();
        JsonNode deprecatedByNode = node.path("deprecatedBy");
        if (deprecatedByNode.isArray()) {
            for (JsonNode itemNode : deprecatedByNode) {
                CPE.DeprecatedBy deprecatedBy = new CPE.DeprecatedBy();
                deprecatedBy.setCpeName(itemNode.path("cpeName").asText());
                deprecatedBy.setCpeNameId(itemNode.path("cpeNameId").asText());
                deprecatedByList.add(deprecatedBy);
            }
        }
        return deprecatedByList;
    }
    private List<CPE.Deprecates> parseDeprecates(JsonNode node) {
        List<CPE.Deprecates> deprecatesList = new ArrayList<>();
        JsonNode deprecatesNode = node.path("deprecates");
        if (deprecatesNode.isArray()) {
            Iterator<JsonNode> elements = deprecatesNode.elements();
            while (elements.hasNext()) {
                JsonNode deprecatesItemNode = elements.next();
                CPE.Deprecates deprecates = new CPE.Deprecates();
                deprecates.setCpeName(deprecatesItemNode.path("cpeName").asText());
                deprecates.setCpeNameId(deprecatesItemNode.path("cpeNameId").asText());
                deprecatesList.add(deprecates);
            }
        }
        return deprecatesList;
    }
}
