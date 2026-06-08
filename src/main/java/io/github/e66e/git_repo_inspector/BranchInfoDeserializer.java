package io.github.e66e.git_repo_inspector;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

public class BranchInfoDeserializer extends StdDeserializer<BranchInfo> {

    public BranchInfoDeserializer() {
        super(BranchInfo.class);
    }

    protected BranchInfoDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public BranchInfo deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {
        JsonNode node = p.readValueAsTree();

        String name = node.get("name").asString();
        String hash = node.at("/commit/sha").asString();

        return new BranchInfo(name, hash);
    }
}
