package io.github.e66e.git_repo_inspector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;
import java.util.concurrent.StructuredTaskScope;

@HttpExchange(url = "${github.baseurl}",
        accept = "application/vnd.github+json",
        headers = {"X-GitHub-Api-Version=${github.version}",
                   "User-Agent=${spring.application.name}"})
public interface InspectorService {

    static final Logger log = LoggerFactory.getLogger(InspectorService.class);

    @Retryable(
            includes = {RestClientResponseException.class},
            maxRetries = 4,
            multiplier = 1.1
    )
    @GetExchange("/users/{username}/repos")
    List<Repository> getUserRepos(@PathVariable String username)
            throws RestClientResponseException;

    @Retryable(
        includes = {RestClientResponseException.class},
        maxRetries = 4,
        multiplier = 1.1
    )
    @GetExchange("/repos/{username}/{repo}/branches")
    List<BranchInfo> getBranches(@PathVariable String username, @PathVariable String repo)
            throws RestClientResponseException;

    default List<Repository> getAllNonForkRepos(final String username) {
        log.info("Fetching all repositories for user: {}.", username);
        List<Repository> repos = getUserRepos(username);

        log.info("Filtering forked repositories for user: {}.", username);
        List<Repository> filteredRepos = repos.parallelStream()
                .filter(repo -> !repo.isFork())
                .toList();

        log.info("Fetching branches for repositories.");
        try (var scope = StructuredTaskScope.open()) {
            filteredRepos.forEach(repo -> {
                        log.info("Fetching branches for repo: {}.", repo);
                        scope.fork(() -> repo.addBranches(getBranches(username, repo.repositoryName())));
                    }
            );
            scope.join();
        } catch (InterruptedException e) {
            log.warn("Interrupted while fetching repositories for user: {}.", username);
            throw new FetchingBranchesException("Error occurred while fetching branches.");
        }

        return filteredRepos;
    }
}
