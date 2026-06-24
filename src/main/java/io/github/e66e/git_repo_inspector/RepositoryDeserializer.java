package io.github.e66e.git_repo_inspector;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

import java.util.ArrayList;

public class RepositoryDeserializer extends StdDeserializer<Repository> {
    protected RepositoryDeserializer(Class<?> vc) {
        super(vc);
    }

    public RepositoryDeserializer() {
        super(Repository.class);
    }

    @Override
    public Repository deserialize(JsonParser p, DeserializationContext ctxt) throws JacksonException {

        JsonNode node = p.readValueAsTree();

        String username = node.at("/owner/login").asString();
        String name = node.get("name").asString();
        boolean isFork = node.get("fork").asBoolean();

        return new Repository(
                name,
                username,
                isFork,
                new ArrayList<>());
    }
}
