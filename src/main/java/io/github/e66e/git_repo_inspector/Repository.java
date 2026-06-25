package io.github.e66e.git_repo_inspector;

import com.fasterxml.jackson.annotation.JsonView;
import tools.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(using = RepositoryDeserializer.class)
public record Repository(
        @JsonView(Views.Public.class)
        String repositoryName,
        @JsonView(Views.Public.class)
        String ownerLogin,
        @JsonView(Views.Internal.class)
        boolean isFork,
        @JsonView(Views.Public.class)
        List<BranchInfo> branches
) {
    public void addBranches(List<BranchInfo> branches) {
        this.branches.addAll(branches);
    }
}
