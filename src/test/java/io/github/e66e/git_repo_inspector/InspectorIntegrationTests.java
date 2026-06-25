package io.github.e66e.git_repo_inspector;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.servlet.client.RestTestClient;

import java.util.List;

@SpringBootTest(classes = InspectorIntegrationTests.AppConfiguration.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@WireMockTest(httpPort =  8081)
@AutoConfigureRestTestClient
class InspectorIntegrationTests {

    record RepositoryResponse(
            String repositoryName,
            String ownerLogin,
            List<BranchInfoResponse> branches
    ) {
    }

    record BranchInfoResponse(
            String name,
            String lastCommitSha
    ) {}

    @Autowired
    private RestTestClient restClient;

    @Test
    void givenWireMockStub_whenGetTestUser_thenReturnsAllData() {
        List<RepositoryResponse> repos = this.restClient.get()
                .uri("/testuser")
                .exchange()
                .expectStatus().isOk()
                .expectBody(new ParameterizedTypeReference<List<RepositoryResponse>>() {})
                .returnResult()
                .getResponseBody();

        Assertions.assertNotNull(repos);
        Assertions.assertEquals(2, repos.size());
        Assertions.assertTrue(repos.stream().anyMatch(repo -> repo.repositoryName().equals("repo_1")));
        Assertions.assertTrue(repos.stream().anyMatch(repo -> repo.repositoryName().equals("repo_2")));

        RepositoryResponse repo1 = repos.stream().filter(repo -> repo.repositoryName().equals("repo_1")).findFirst().get();
        RepositoryResponse repo2 = repos.stream().filter(repo -> repo.repositoryName().equals("repo_2")).findFirst().get();

        BranchInfoResponse r1b = repo1.branches().stream().filter(branch -> branch.name().equals("main")).findFirst().get();

        Assertions.assertNotNull(r1b);
        Assertions.assertEquals("main", r1b.name());
        Assertions.assertEquals("53b298245fefb4f3509a077e573486b6145e63c8", r1b.lastCommitSha());

        BranchInfoResponse r2b = repo2.branches().stream().filter(branch -> branch.name().equals("main")).findFirst().get();

        Assertions.assertNotNull(r2b);
        Assertions.assertEquals("main", r2b.name());
        Assertions.assertEquals("85c86e8f939b977bb673f776de00be5d051c2299", r2b.lastCommitSha());
    }

    @Test
    void givenWireMockStub_whenGetNonExistingUser_thenReturnNotFound() {
        ErrorResponse expectedError = new ErrorResponse(404, "Not Found");

        restClient.get()
                .uri("/notexistingtestuser")
                .exchange()
                .expectStatus().is4xxClientError()
                .expectBody(ErrorResponse.class)
                .isEqualTo(expectedError);
    }

    @SpringBootApplication
    static class AppConfiguration {}
}

