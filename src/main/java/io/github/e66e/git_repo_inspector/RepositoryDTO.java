package io.github.e66e.git_repo_inspector;

import java.util.List;

public record RepositoryDTO(
        String repositoryName,
        String ownerLogin,
        List<BranchInfo> branches
) {
}
