package io.reflectoring.coderadar.graph.query.adapter;

import io.reflectoring.coderadar.graph.projectadministration.domain.ModuleEntity;
import io.reflectoring.coderadar.graph.projectadministration.domain.ProjectEntity;
import io.reflectoring.coderadar.graph.projectadministration.module.repository.ModuleRepository;
import io.reflectoring.coderadar.graph.projectadministration.project.repository.ProjectRepository;
import io.reflectoring.coderadar.graph.query.domain.MetricValueForCommitTreeQueryResult;
import io.reflectoring.coderadar.graph.query.repository.MetricQueryRepository;
import io.reflectoring.coderadar.projectadministration.ProjectNotFoundException;
import io.reflectoring.coderadar.query.domain.MetricTree;
import io.reflectoring.coderadar.query.domain.MetricTreeNodeType;
import io.reflectoring.coderadar.query.domain.MetricValueForCommit;
import io.reflectoring.coderadar.query.port.driven.GetMetricTreeForCommitPort;
import io.reflectoring.coderadar.query.port.driver.GetMetricsForCommitCommand;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class GetMetricTreeForCommitAdapter implements GetMetricTreeForCommitPort {

  private final MetricQueryRepository metricQueryRepository;
  private final ProjectRepository projectRepository;
  private final ModuleRepository moduleRepository;

  public GetMetricTreeForCommitAdapter(
      MetricQueryRepository metricQueryRepository,
      ProjectRepository projectRepository,
      ModuleRepository moduleRepository) {
    this.metricQueryRepository = metricQueryRepository;
    this.projectRepository = projectRepository;
    this.moduleRepository = moduleRepository;
  }

  MetricTree get(
      ProjectEntity project, String commitHash, List<String> metrics, boolean includeFileHashes) {
    List<MetricValueForCommitTreeQueryResult> result;
    if (includeFileHashes) {
      result =
          metricQueryRepository.getMetricTreeForCommitWithFileHashes(
              project.getId(), commitHash, metrics);
    } else {
      result = metricQueryRepository.getMetricTreeForCommit(project.getId(), commitHash, metrics);
    }
    List<ModuleEntity> moduleEntities = // project already has the modules??
        moduleRepository.findModulesInProjectSortedDesc(project.getId());
    List<MetricTree> moduleChildren = processModules(moduleEntities, result);
    MetricTree rootModule = processRootModule(result);
    rootModule.getChildren().addAll(findChildModules(project.getModules(), moduleChildren));
    rootModule.setMetrics(aggregateChildMetrics(rootModule.getChildren()));
    return rootModule;
  }

  @Override
  public MetricTree get(GetMetricsForCommitCommand command, long projectId) {
    ProjectEntity projectEntity =
        projectRepository
            .findByIdWithModules(projectId)
            .orElseThrow(() -> new ProjectNotFoundException(projectId));
    return get(projectEntity, command.getCommit(), command.getMetrics(), false);
  }

  /**
   * Creates a metric tree for each module in the project
   *
   * @param moduleEntities All module entities in the project
   * @param metricValues The calculated metric values for each file in the project.
   * @return A list of metric trees with aggregated metrics for each module.
   */
  private List<MetricTree> processModules(
      List<ModuleEntity> moduleEntities, List<MetricValueForCommitTreeQueryResult> metricValues) {
    List<MetricTree> moduleChildren = new ArrayList<>();
    for (ModuleEntity moduleEntity : moduleEntities) {
      MetricTree metricTree = new MetricTree();
      metricTree.setType(MetricTreeNodeType.MODULE);
      metricTree.setName(moduleEntity.getPath());

      Map<String, Long> aggregatedMetrics = new LinkedHashMap<>();
      List<MetricValueForCommitTreeQueryResult> processedFiles = new ArrayList<>();
      for (MetricValueForCommitTreeQueryResult commitTreeQueryResult : metricValues) {
        if (commitTreeQueryResult.getPath().startsWith(moduleEntity.getPath())) {
          MetricTree metricTreeFile = new MetricTree();
          metricTreeFile.setName(commitTreeQueryResult.getPath());
          metricTreeFile.setType(MetricTreeNodeType.FILE);
          for (String metric : commitTreeQueryResult.getMetrics()) {
            String[] temp = metric.split("=");
            MetricValueForCommit metricValueForCommit =
                new MetricValueForCommit(temp[0], Long.parseLong(temp[1]));
            metricTreeFile.getMetrics().add(metricValueForCommit);
            aggregatedMetrics.putIfAbsent(metricValueForCommit.getMetricName(), 0L);
            aggregatedMetrics.put(
                metricValueForCommit.getMetricName(),
                aggregatedMetrics.get(metricValueForCommit.getMetricName())
                    + metricValueForCommit.getValue());
          }
          metricTree.getChildren().add(metricTreeFile);
          processedFiles.add(commitTreeQueryResult);
        }
      }
      metricValues.removeAll(processedFiles);
      for (Map.Entry<String, Long> metric : aggregatedMetrics.entrySet()) {
        metricTree.getMetrics().add(new MetricValueForCommit(metric.getKey(), metric.getValue()));
      }
      moduleChildren.add(metricTree);
    }
    return moduleChildren;
  }

  /**
   * Processes the metric values contained in the root module and creates a metric tree
   *
   * @param metricValues The metric values that belong to the root module.
   * @return A MetricTree for the root module.
   */
  private MetricTree processRootModule(List<MetricValueForCommitTreeQueryResult> metricValues) {
    MetricTree rootModule = new MetricTree();
    rootModule.setType(MetricTreeNodeType.MODULE);
    rootModule.setName("root");

    for (MetricValueForCommitTreeQueryResult value : metricValues) {
      MetricTree metricTreeFile = new MetricTree();

      metricTreeFile.setName(value.getPath());
      metricTreeFile.setType(MetricTreeNodeType.FILE);

      for (String metric : value.getMetrics()) {
        String[] temp = metric.split("=");
        MetricValueForCommit metricValueForCommit =
            new MetricValueForCommit(temp[0], Long.parseLong(temp[1]));
        metricTreeFile.getMetrics().add(metricValueForCommit);
      }
      rootModule.getChildren().add(metricTreeFile);
    }
    return rootModule;
  }

  /**
   * Aggregates all of the metrics in the given metric trees.
   *
   * @param children The trees to aggregate.
   * @return A list of aggregated metric values.
   */
  private List<MetricValueForCommit> aggregateChildMetrics(List<MetricTree> children) {
    List<MetricValueForCommit> resultList = new ArrayList<>();
    Map<String, Long> aggregatedMetrics = new LinkedHashMap<>();
    for (MetricTree metricTree : children) {
      for (MetricValueForCommit val : aggregateChildMetrics(metricTree.getChildren())) {
        if (metricTree.getMetrics().stream()
            .noneMatch(metric -> metric.getMetricName().equals(val.getMetricName()))) {
          metricTree
              .getMetrics()
              .add(new MetricValueForCommit(val.getMetricName(), val.getValue()));
        }
      }
      for (MetricValueForCommit value : metricTree.getMetrics()) {
        aggregatedMetrics.putIfAbsent(value.getMetricName(), 0L);
        aggregatedMetrics.put(
            value.getMetricName(), aggregatedMetrics.get(value.getMetricName()) + value.getValue());
      }
    }
    for (Map.Entry<String, Long> metric : aggregatedMetrics.entrySet()) {
      resultList.add(new MetricValueForCommit(metric.getKey(), metric.getValue()));
    }
    return resultList;
  }

  /**
   * Correctly find parent and child modules and constructs a valid MetricTree for them.
   *
   * @param moduleEntities The module entities contained in the project.
   * @param metricTrees The metric trees corresponding to the module entities.
   * @return A complete MetricTree
   */
  private List<MetricTree> findChildModules(
      List<ModuleEntity> moduleEntities, List<MetricTree> metricTrees) {
    List<MetricTree> result = new ArrayList<>();
    for (ModuleEntity moduleEntity : moduleEntities) {
      for (MetricTree metricTree : metricTrees) {
        if (metricTree.getName().equals(moduleEntity.getPath())) {
          metricTree
              .getChildren()
              .addAll(findChildModules(moduleEntity.getChildModules(), metricTrees));
          result.add(metricTree);
        }
      }
    }
    return result;
  }
}
