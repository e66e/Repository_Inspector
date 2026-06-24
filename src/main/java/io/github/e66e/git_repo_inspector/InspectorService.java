package io.github.e66e.git_repo_inspector;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange(url = "${github.baseurl}",
        accept = "application/vnd.github+json",
        headers = {"X-GitHub-Api-Version=${github.version}",
                   "User-Agent=${spring.application.name}"})
public interface InspectorService {

    @GetExchange("/users/{username}/repos")
    List<Repository> getUserRepos(@PathVariable String username);

    @GetExchange("/repos/{username}/{repo}/branches")
    List<BranchInfo> getBranches(@PathVariable String username, @PathVariable String repo);

    default List<RepositoryDTO> getAllNonForkRepos(final String username) {
        List<Repository> repos = getUserRepos(username);
        repos = repos.parallelStream().filter(repo -> !repo.isFork()).toList();

        repos.parallelStream().forEach(repo -> {
            List<BranchInfo> branchInfos = this.getBranches(username, repo.repositoryName());
            repo.addBranches(branchInfos);
        });

        return repos.parallelStream().map(Repository::mapToDTO).toList();
    }
}
