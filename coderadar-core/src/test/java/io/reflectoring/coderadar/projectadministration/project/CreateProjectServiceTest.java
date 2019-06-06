package io.reflectoring.coderadar.projectadministration.project;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import io.reflectoring.coderadar.CoderadarConfigurationProperties;
import io.reflectoring.coderadar.projectadministration.ProjectStillExistsException;
import io.reflectoring.coderadar.projectadministration.domain.Project;
import io.reflectoring.coderadar.projectadministration.port.driven.project.CreateProjectPort;
import io.reflectoring.coderadar.projectadministration.port.driven.project.GetProjectPort;
import io.reflectoring.coderadar.projectadministration.port.driver.project.create.CreateProjectCommand;
import io.reflectoring.coderadar.projectadministration.service.project.CreateProjectService;
import io.reflectoring.coderadar.vcs.port.driver.CloneRepositoryUseCase;
import java.io.File;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.task.TaskExecutor;

class CreateProjectServiceTest {

  private CreateProjectPort createProjectPort = mock(CreateProjectPort.class);
  private GetProjectPort getProjectPort = mock(GetProjectPort.class);
  private CloneRepositoryUseCase cloneRepositoryUseCase = mock(CloneRepositoryUseCase.class);
  private CoderadarConfigurationProperties coderadarConfigurationProperties =
      mock(CoderadarConfigurationProperties.class);
  private TaskExecutor taskExecutor = mock(TaskExecutor.class);

  @Test
  void returnsNewProjectId() {
    CreateProjectService testSubject =
        new CreateProjectService(
            createProjectPort,
            getProjectPort,
            cloneRepositoryUseCase,
            coderadarConfigurationProperties,
            taskExecutor);

    when(coderadarConfigurationProperties.getWorkdir())
        .thenReturn(new File("coderadar-workdir").toPath());
    CreateProjectCommand command =
        new CreateProjectCommand(
            "project", "username", "password", "http://valid.url", true, new Date(), new Date());

    Project project = new Project();
    project.setName("project");
    project.setVcsUrl("http://valid.url");
    project.setVcsUsername("username");
    project.setVcsPassword("password");
    project.setVcsOnline(true);
    project.setVcsStart(new Date());
    project.setVcsEnd(new Date());

    when(createProjectPort.createProject(any())).thenReturn(1L);
    when(getProjectPort.get(project.getName())).thenReturn(Optional.empty());

    Long projectId = testSubject.createProject(command);

    Assertions.assertEquals(1L, projectId.longValue());
  }

  @Test
  void returnsErrorWhenProjectWithNameStillExists() {
    CreateProjectService testSubject =
        new CreateProjectService(
            createProjectPort,
            getProjectPort,
            cloneRepositoryUseCase,
            coderadarConfigurationProperties,
            taskExecutor);

    when(coderadarConfigurationProperties.getWorkdir())
        .thenReturn(new File("coderadar-workdir").toPath());
    CreateProjectCommand command =
        new CreateProjectCommand(
            "project", "username", "password", "http://valid.url", true, new Date(), new Date());

    Project project = new Project();
    project.setName("project");
    project.setVcsUrl("http://valid.url");
    project.setVcsUsername("username");
    project.setVcsPassword("password");
    project.setVcsOnline(true);
    project.setVcsStart(new Date());
    project.setVcsEnd(new Date());

    when(createProjectPort.createProject(any())).thenReturn(1L);
    when(getProjectPort.get(project.getName())).thenReturn(Optional.of(new Project()));

    Assertions.assertThrows(
        ProjectStillExistsException.class,
        () -> {
          testSubject.createProject(command);
        });
  }
}