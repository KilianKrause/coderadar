package io.reflectoring.coderadar.graph.projectadministration.filepattern.service;

import io.reflectoring.coderadar.core.projectadministration.domain.FilePattern;
import io.reflectoring.coderadar.core.projectadministration.port.driven.filepattern.CreateFilePatternPort;
import io.reflectoring.coderadar.graph.projectadministration.filepattern.repository.CreateFilePatternRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("CreateFilePatternServiceNeo4j")
public class CreateFilePatternService implements CreateFilePatternPort {

  private CreateFilePatternRepository createFilePatternRepository;

  @Autowired
  public CreateFilePatternService(CreateFilePatternRepository createFilePatternRepository) {
    this.createFilePatternRepository = createFilePatternRepository;
  }

  @Override
  public Long createFilePattern(FilePattern filePattern) {
    return createFilePatternRepository.save(filePattern).getId();
  }
}