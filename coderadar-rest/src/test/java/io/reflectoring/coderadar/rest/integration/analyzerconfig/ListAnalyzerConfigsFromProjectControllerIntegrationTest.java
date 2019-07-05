package io.reflectoring.coderadar.rest.integration.analyzerconfig;

import io.reflectoring.coderadar.graph.projectadministration.analyzerconfig.repository.CreateAnalyzerConfigurationRepository;
import io.reflectoring.coderadar.graph.projectadministration.domain.AnalyzerConfigurationEntity;
import io.reflectoring.coderadar.graph.projectadministration.domain.ProjectEntity;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.CreateProjectRepository;
import io.reflectoring.coderadar.projectadministration.port.driver.analyzerconfig.get.GetAnalyzerConfigurationResponse;
import io.reflectoring.coderadar.rest.integration.ControllerTestTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;

import static io.reflectoring.coderadar.rest.integration.JsonHelper.fromJson;
import static io.reflectoring.coderadar.rest.integration.ResultMatchers.containsResource;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

class ListAnalyzerConfigsFromProjectControllerIntegrationTest extends ControllerTestTemplate {

  @Autowired private CreateProjectRepository createProjectRepository;

  @Autowired private CreateAnalyzerConfigurationRepository createAnalyzerConfigurationRepository;

  @Test
  void listAnalyzerConfigurationsFromProject() throws Exception {
    // Set up
    ProjectEntity testProject = new ProjectEntity();
    testProject.setVcsUrl("https://valid.url");
    testProject = createProjectRepository.save(testProject);

    AnalyzerConfigurationEntity analyzerConfiguration = new AnalyzerConfigurationEntity();
    analyzerConfiguration.setProject(testProject);
    analyzerConfiguration.setAnalyzerName("analyzer");
    analyzerConfiguration.setEnabled(true);

    createAnalyzerConfigurationRepository.save(analyzerConfiguration);

    AnalyzerConfigurationEntity analyzerConfiguration2 = new AnalyzerConfigurationEntity();
    analyzerConfiguration2.setProject(testProject);
    analyzerConfiguration2.setAnalyzerName("analyzer2");
    analyzerConfiguration2.setEnabled(false);

    createAnalyzerConfigurationRepository.save(analyzerConfiguration2);

    testProject.setAnalyzerConfigurations(
        Arrays.asList(analyzerConfiguration, analyzerConfiguration2));
    testProject = createProjectRepository.save(testProject);

    // Test
    mvc()
        .perform(get("/projects/" + testProject.getId() + "/analyzers"))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(containsResource(GetAnalyzerConfigurationResponse[].class))
        .andExpect(
            result -> {
              GetAnalyzerConfigurationResponse[] configurationResponses =
                  fromJson(
                      result.getResponse().getContentAsString(),
                      GetAnalyzerConfigurationResponse[].class);
              Assertions.assertEquals(2, configurationResponses.length);
            })
            .andDo(document("analyzerConfiguration/get"));
  }

  @Test
  void listAnalyzerConfigurationsReturnsErrorWhenProjectNotFound() throws Exception {
    mvc()
        .perform(get("/projects/1/analyzers"))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(
            MockMvcResultMatchers.jsonPath("errorMessage").value("Project with id 1 not found."));
  }
}