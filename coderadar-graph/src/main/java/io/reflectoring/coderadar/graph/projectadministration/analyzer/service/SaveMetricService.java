package io.reflectoring.coderadar.graph.projectadministration.analyzer.service;

import io.reflectoring.coderadar.analyzer.domain.MetricValue;
import io.reflectoring.coderadar.graph.projectadministration.analyzer.repository.SaveMetricRepository;
import io.reflectoring.coderadar.projectadministration.port.driven.analyzer.SaveMetricPort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("SaveMetricServiceNeo4j")
public class SaveMetricService implements SaveMetricPort {

  private SaveMetricRepository saveMetricRepository;

  @Autowired
  public SaveMetricService(SaveMetricRepository saveMetricRepository) {
    this.saveMetricRepository = saveMetricRepository;
  }

  @Override
  public void saveMetricValue(MetricValue metricValue) {
    saveMetricRepository.save(metricValue);
  }
}