package io.github.e66e.git_repo_inspector;

public record ErrorResponse(
        int status,
        String message
) {
}
