# Git Repository Inspector

A simple Spring Boot proxy application that fetches a GitHub user's non-fork repositories along with branch names and the last commit SHA for each branch.

## Tech stack

- Java 25
- Spring Boot 4
- Gradle Kotlin DSL

## Libraries

- Lombok
- WireMock

## API Usage

Application starts on <code>http://localhost:8080</code>.

### List non-fork repositories for a user

**Exposed endpoint:**

```
GET      /{username}
```
Example response:

```json
[
    {
        "repository_name": "repoName",
        "owner_login": "username",
        "branches": [
            {
                "name": "branchName",
                "last_commit_hash": "commitSha"
            }
        ]
    }
]
```

User not found:

```json
{
    "status": 404,
    "message": "Not Found"
}
```

The backend consumes the public GitHub API v3 (https://api.github.com). No pagination is supported, all matching repositories are returned in a single response (up to 100 entries).

## Testing

Integration tests use WireMock to simulate GitHub API.

The tests cover 2 scenarios:

- Happy path -- the user sends a request and gets data in a format described above.
- Unhappy path -- the username doesn't exist, so the user gets 404 response with a message.