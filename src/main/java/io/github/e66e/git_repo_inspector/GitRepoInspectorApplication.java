package io.github.e66e.git_repo_inspector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.resilience.annotation.EnableResilientMethods;

@SpringBootApplication
@EnableResilientMethods
public class GitRepoInspectorApplication {

	static void main(String[] args) {
		SpringApplication.run(GitRepoInspectorApplication.class, args);
	}

}
