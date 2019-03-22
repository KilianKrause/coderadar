package org.wickedsource.coderadar.core;

import java.nio.file.Path;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.coderadar.core.common.ResourceNotFoundException;
import org.wickedsource.coderadar.core.configuration.CoderadarConfiguration;
import org.wickedsource.coderadar.project.domain.ProjectRepository;
import org.wickedsource.coderadar.projectadministration.domain.Project;

/** Centralizes the access to local working directories needed for some tasks. */
@Service
public class WorkdirManager {

  private CoderadarConfiguration config;

  private ProjectRepository projectRepository;

  @Autowired
  public WorkdirManager(CoderadarConfiguration config, ProjectRepository projectRepository) {
    this.config = config;
    this.projectRepository = projectRepository;
  }

  /**
   * Returns the path to the local GIT repository for the specified project. Creates the folder if
   * it does not exist.
   *
   * @param projectId ID of the project whose working directory to find.
   * @return path to the local GIT repository of the specified project.
   */
  public Path getLocalGitRoot(Long projectId) {
    Optional<Project> project = projectRepository.findById(projectId);
    if (project.isPresent()) {
      Path workdir = config.getWorkdir().resolve("projects/" + project.get().getWorkdirName());
      createDirIfNecessary(workdir);
      return workdir;
    } else {
      throw new ResourceNotFoundException("Project with id:" + projectId + " not found");
    }
  }

  private void createDirIfNecessary(Path workdir) {
    if (!workdir.toFile().exists()) {
      workdir.toFile().mkdirs();
    }
  }
}
