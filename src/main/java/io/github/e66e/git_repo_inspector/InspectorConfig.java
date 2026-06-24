package io.github.e66e.git_repo_inspector;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.service.registry.ImportHttpServices;

@ImportHttpServices(group = "github", types = {InspectorService.class})
@Configuration
public class InspectorConfig {
}
