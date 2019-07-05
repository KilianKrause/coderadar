package io.reflectoring.coderadar.rest.integration.module;

import io.reflectoring.coderadar.graph.projectadministration.domain.ModuleEntity;
import io.reflectoring.coderadar.graph.projectadministration.domain.ProjectEntity;
import io.reflectoring.coderadar.graph.projectadministration.module.repository.CreateModuleRepository;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.CreateProjectRepository;
import io.reflectoring.coderadar.projectadministration.port.driver.module.update.UpdateModuleCommand;
import io.reflectoring.coderadar.rest.integration.ControllerTestTemplate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

class UpdateModuleControllerIntegrationTest extends ControllerTestTemplate {

  @Autowired private CreateProjectRepository createProjectRepository;

  @Autowired private CreateModuleRepository createModuleRepository;

  @Test
  void updateModuleWithId() throws Exception {
    // Set up
    ProjectEntity testProject = new ProjectEntity();
    testProject.setVcsUrl("https://valid.url");
    testProject = createProjectRepository.save(testProject);

    ModuleEntity module = new ModuleEntity();
    module.setPath("test-module");
    module.setProject(testProject);
    module = createModuleRepository.save(module);
    final Long id = module.getId();

    // Test
    UpdateModuleCommand command = new UpdateModuleCommand("new-module-path");
    mvc()
        .perform(
            post("/projects/" + testProject.getId() + "/modules/" + module.getId())
                .content(toJson(command))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andDo(
            result -> {
              ModuleEntity module1 = createModuleRepository.findById(id).get();
              Assertions.assertEquals("new-module-path", module1.getPath());
            })
            .andDo(document("modules/update"));
  }

  @Test
  void updateModuleReturnsErrorWhenModuleNotFound() throws Exception {
    UpdateModuleCommand command = new UpdateModuleCommand("new-module-path");
    mvc()
        .perform(
            post("/projects/0/modules/2")
                .content(toJson(command))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(
            MockMvcResultMatchers.jsonPath("errorMessage").value("Module with id 2 not found."));
  }

  @Test
  void updateModuleReturnsErrorWhenRequestIsInvalid() throws Exception {
    UpdateModuleCommand command = new UpdateModuleCommand("");
    mvc()
        .perform(
            post("/projects/0/modules/1")
                .content(toJson(command))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(MockMvcResultMatchers.status().isBadRequest());
  }


}