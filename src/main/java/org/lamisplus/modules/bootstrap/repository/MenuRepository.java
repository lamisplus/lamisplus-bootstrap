package org.lamisplus.modules.bootstrap.repository;


import org.lamisplus.modules.bootstrap.domain.entity.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>, JpaSpecificationExecutor {

    Optional<Menu> findByIdAndArchived(Long id, int archive);

    List<Menu> findAllByArchivedOrderByIdDesc(int archived);

    Optional<Menu> findByName(String name);

    Optional<Menu> findByModuleId(String moduleId);
}
