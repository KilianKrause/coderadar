package io.reflectoring.coderadar.core.projectadministration.filepattern;

import io.reflectoring.coderadar.core.projectadministration.domain.FilePattern;
import io.reflectoring.coderadar.core.projectadministration.domain.InclusionType;
import io.reflectoring.coderadar.core.projectadministration.port.driven.filepattern.ListFilePatternsOfProjectPort;
import io.reflectoring.coderadar.core.projectadministration.port.driver.filepattern.get.GetFilePatternResponse;
import io.reflectoring.coderadar.core.projectadministration.service.filepattern.ListFilePatternsOfProjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(SpringExtension.class)
class ListFilePatternsOfProjectServiceTest {
  @Mock private ListFilePatternsOfProjectPort port;
  @InjectMocks private ListFilePatternsOfProjectService testSubject;

  @Test
  void returnsTwoFilePatternsFromProject() {
    List<FilePattern> filePatterns = new ArrayList<>();
    FilePattern filePattern1 = new FilePattern();
    filePattern1.setId(1L);
    filePattern1.setPattern("**/*.java");
    filePattern1.setInclusionType(InclusionType.INCLUDE);
    FilePattern filePattern2 = new FilePattern();
    filePattern2.setId(2L);
    filePattern2.setPattern("**/*.xml");
    filePattern2.setInclusionType(InclusionType.EXCLUDE);
    filePatterns.add(filePattern1);
    filePatterns.add(filePattern2);

    Mockito.when(port.listFilePatterns(1L)).thenReturn(filePatterns);

    List<GetFilePatternResponse> response = testSubject.listFilePatterns(1L);

    Assertions.assertEquals(filePatterns.size(), response.size());
    Assertions.assertEquals(filePattern1.getId(), response.get(0).getId());
    Assertions.assertEquals(filePattern1.getPattern(), response.get(0).getPattern());
    Assertions.assertEquals(filePattern1.getInclusionType(), response.get(0).getInclusionType());
    Assertions.assertEquals(filePattern2.getId(), response.get(1).getId());
    Assertions.assertEquals(filePattern2.getPattern(), response.get(1).getPattern());
    Assertions.assertEquals(filePattern2.getInclusionType(), response.get(1).getInclusionType());
  }
}
