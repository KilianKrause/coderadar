package io.reflectoring.coderadar.core.projectadministration.port.driven.module;

import io.reflectoring.coderadar.core.projectadministration.domain.Module;

public interface CreateModulePort {
  Module createModule(Module module);
}