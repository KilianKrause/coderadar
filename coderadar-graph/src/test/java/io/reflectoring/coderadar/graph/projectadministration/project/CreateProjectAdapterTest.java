package io.reflectoring.coderadar.graph.projectadministration.project;

import static org.mockito.Mockito.*;

import io.reflectoring.coderadar.graph.projectadministration.domain.ProjectEntity;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.CreateProjectRepository;
import io.reflectoring.coderadar.graph.projectadministration.project.service.CreateProjectAdapter;
import io.reflectoring.coderadar.projectadministration.domain.Project;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Create project")
class CreateProjectAdapterTest {
  private CreateProjectRepository createProjectRepository = mock(CreateProjectRepository.class);

  private CreateProjectAdapter createProjectAdapter;

  @BeforeEach
  void setUp() {
    createProjectAdapter = new CreateProjectAdapter(createProjectRepository);
  }

  @Test
  @DisplayName("Should save project when passing a valid project entity")
  void shouldSaveProjectWhenPassingAValidProjectEntity() {
    Project project = new Project();
    ProjectEntity projectEntity = new ProjectEntity();
    when(createProjectRepository.save(projectEntity)).thenReturn(projectEntity);

    createProjectAdapter.createProject(project);

    verify(createProjectRepository, times(1)).save(projectEntity);
  }

  @Test
  @DisplayName("Should return ID when saving project")
  void shouldReturnIdWhenSavingProject() {
    ProjectEntity mockedItem = new ProjectEntity();
    mockedItem.setId(1L);
    Project newItem = new Project();
    when(createProjectRepository.save(any(ProjectEntity.class))).thenReturn(mockedItem);

    Long returnedId = createProjectAdapter.createProject(newItem);

    verify(createProjectRepository, times(1)).save(any());
    verifyNoMoreInteractions(createProjectRepository);
    Assertions.assertEquals(new Long(1L), returnedId);
  }
}