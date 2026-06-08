package io.github.e66e.git_repo_inspector;

import tools.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(using = BranchInfoDeserializer.class)
public record BranchInfo(
        String name,
        String lastCommitSha
) {
}
