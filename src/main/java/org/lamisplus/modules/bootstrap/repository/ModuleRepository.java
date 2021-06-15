package org.lamisplus.modules.bootstrap.repository;


import org.lamisplus.modules.bootstrap.domain.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ModuleRepository extends JpaRepository<Module, String> {

    Optional<Module> findByName(String name);

    List<Module> findByProcessConfigIsTrue();

    List<Module> findByUninstallIsTrue();

    List<Module> findByActiveIsTrue();

    List<Module> findByActiveIsTrueAndStartedIsTrue();

    List<Module> findAllByStatusNot(int status);

    Optional<Module> findByIdAndStatus(String id, int status);

    List<Module> findAllByModuleType(int moduleType);

    Optional<Module> findByNameAndModuleType(String name, int moduleType);
}
