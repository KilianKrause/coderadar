package org.wickedsource.coderadar.project.rest;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.wickedsource.coderadar.core.common.ResourceNotFoundException;
import org.wickedsource.coderadar.project.domain.ProjectRepository;
import org.wickedsource.coderadar.projectadministration.domain.Project;

@Component
public class ProjectVerifier {

  private ProjectRepository projectRepository;

  @Autowired
  public ProjectVerifier(ProjectRepository projectRepository) {
    this.projectRepository = projectRepository;
  }

  /**
   * Loads the project with the specified ID from the database or throws an exception if it doesn't.
   */
  public Project loadProjectOrThrowException(Long projectId) {
    if (projectId == null) {
      throw new IllegalArgumentException("projectId must not be null!");
    }
    Optional<Project> project = projectRepository.findById(projectId);
    if (!project.isPresent()) {
      throw new ResourceNotFoundException();
    } else {
      return project.get();
    }
  }

  /** Checks if the Project with the given ID exists without loading it from the database. */
  public void checkProjectExistsOrThrowException(Long projectId) {
    if (projectId == null) {
      throw new IllegalArgumentException("projectId must not be null!");
    }
    int count = projectRepository.countById(projectId);
    if (count == 0) {
      throw new ResourceNotFoundException();
    }
  }
}
