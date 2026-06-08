package io.github.e66e.git_repo_inspector;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.logging.Logger;

@Service
public class InspectorService {

    private final RestClient restClient;

    private static final Logger logger = Logger.getLogger(InspectorService.class.getName());

    @Autowired
    public InspectorService(RestClient.Builder builder,
                            @Value("${git.baseurl}") String baseUrl) {

        this.restClient = builder
                .baseUrl(baseUrl)
                .defaultHeader("Accept", "application/vnd.github+json")
                .defaultHeader("X-GitHub-Api-Version", "2026-03-10")
                .defaultRequest(request -> {
                    request.attribute("per_page", 100);
                    request.attribute("type", "all");
                })
                .build();
    }

    public List<Repository> getAllNonForkRepos(final String username) {
        JsonNode node = this.restClient.get()
                .uri("/users/{username}/repos", username)
                .retrieve()
                .body(JsonNode.class);

        if (node == null) {
            return List.of();
        }

        Predicate<JsonNode> isFork = n -> n.has("fork") && n.get("fork").asBoolean();

        // Filtering response by fork field
        ObjectMapper om = new ObjectMapper();
        List<Repository> repos = node.valueStream()
                .filter(isFork.negate())
                .map(n -> om.readValue(n.toString(), Repository.class))
                .toList();


        // Getting branches for each repo
        repos.forEach(repo -> {
            List<BranchInfo> infos = restClient.get()
                .uri("/repos/{username}/{repo}/branches", username, repo.repositoryName())
                .retrieve()
                .body(new ParameterizedTypeReference<>() {});
            repo.addBranches(Objects.requireNonNullElseGet(infos, List::of));
        });

        logger.info("User: " + username + "\t size: " + repos.size());

        return repos;
    }
}
