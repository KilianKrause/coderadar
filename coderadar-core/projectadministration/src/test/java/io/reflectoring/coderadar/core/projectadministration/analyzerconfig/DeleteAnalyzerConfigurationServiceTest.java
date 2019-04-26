package io.reflectoring.coderadar.core.projectadministration.analyzerconfig;

import io.reflectoring.coderadar.core.projectadministration.port.driven.analyzerconfig.DeleteAnalyzerConfigurationPort;
import io.reflectoring.coderadar.core.projectadministration.service.analyzerconfig.DeleteAnalyzerConfigurationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
class DeleteAnalyzerConfigurationServiceTest {
  @Mock private DeleteAnalyzerConfigurationPort port;
  private DeleteAnalyzerConfigurationService testSubject;

  @BeforeEach
  void setup() {
    testSubject = new DeleteAnalyzerConfigurationService(port);
  }

  @Test
  void deleteAnalyzerConfigurationWithIdOne() {
    testSubject.deleteAnalyzerConfiguration(1L);

    Mockito.verify(port, Mockito.times(1)).deleteAnalyzerConfiguration(1L);
  }
}
