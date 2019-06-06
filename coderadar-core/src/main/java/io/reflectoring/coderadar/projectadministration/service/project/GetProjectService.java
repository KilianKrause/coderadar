package io.reflectoring.coderadar.projectadministration.service.project;

import io.reflectoring.coderadar.projectadministration.ProjectNotFoundException;
import io.reflectoring.coderadar.projectadministration.domain.Project;
import io.reflectoring.coderadar.projectadministration.port.driven.project.GetProjectPort;
import io.reflectoring.coderadar.projectadministration.port.driver.project.get.GetProjectResponse;
import io.reflectoring.coderadar.projectadministration.port.driver.project.get.GetProjectUseCase;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service("GetProjectService")
public class GetProjectService implements GetProjectUseCase {

  private final GetProjectPort port;

  public GetProjectService(@Qualifier("GetProjectServiceNeo4j") GetProjectPort port) {
    this.port = port;
  }

  @Override
  public GetProjectResponse get(Long id) {
    Optional<Project> project = port.get(id);

    if (project.isPresent()) {
      Project persistedProject = project.get();
      GetProjectResponse response = new GetProjectResponse();
      response.setId(persistedProject.getId());
      response.setName(persistedProject.getName());
      response.setVcsUsername(persistedProject.getVcsUsername());
      response.setVcsPassword(persistedProject.getVcsPassword());
      response.setVcsOnline(persistedProject.isVcsOnline());
      response.setVcsUrl(persistedProject.getVcsUrl());
      response.setStart(persistedProject.getVcsStart());
      response.setEnd(persistedProject.getVcsEnd());
      return response;
    } else {
      throw new ProjectNotFoundException(id);
    }
  }
}