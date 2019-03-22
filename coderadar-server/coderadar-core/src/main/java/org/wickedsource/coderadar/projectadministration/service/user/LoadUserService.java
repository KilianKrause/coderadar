package org.wickedsource.coderadar.projectadministration.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.wickedsource.coderadar.projectadministration.port.driven.user.LoadUserPort;
import org.wickedsource.coderadar.projectadministration.port.driver.user.LoadUserUseCase;

@Service
public class LoadUserService implements LoadUserUseCase {

    private final LoadUserPort port;

    @Autowired
    public LoadUserService(LoadUserPort port) {
        this.port = port;
    }
}