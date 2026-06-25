package io.github.e66e.git_repo_inspector;

import com.fasterxml.jackson.annotation.JsonView;
import tools.jackson.databind.annotation.JsonDeserialize;

@JsonView(Views.Public.class)
@JsonDeserialize(using = BranchInfoDeserializer.class)
public record BranchInfo(
        String name,
        String lastCommitSha
) {
}
