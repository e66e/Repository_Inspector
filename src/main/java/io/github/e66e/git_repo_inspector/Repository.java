package io.github.e66e.git_repo_inspector;

import tools.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(using = RepositoryDeserializer.class)
public record Repository(
        String repositoryName,
        String ownerLogin,
        boolean isFork,
        List<BranchInfo> branches
) {

    public void addBranches(List<BranchInfo> branches) {
        this.branches.addAll(branches);
    }

    public RepositoryDTO mapToDTO() {
        return new RepositoryDTO(this.repositoryName, this.ownerLogin, this.branches);
    }
}
