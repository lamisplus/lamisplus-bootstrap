package org.lamisplus.modules.bootstrap.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lamisplus.modules.bootstrap.controller.apierror.EntityNotFoundException;
import org.lamisplus.modules.bootstrap.controller.apierror.RecordExistException;
import org.lamisplus.modules.bootstrap.domain.entity.Menu;
import org.lamisplus.modules.bootstrap.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    public static final int UN_ARCHIVED = 0;
    public static final int ARCHIVED = 1;

    public List<Menu> getAllMenu() {
        return menuRepository.findAllByArchivedOrderByIdDesc(UN_ARCHIVED);
    }

    public Menu save(Menu menu) {
        Optional<Menu> menuOptional = menuRepository.findByIdAndArchived(menu.getId(), UN_ARCHIVED);
        if (menuOptional.isPresent()) throw new RecordExistException(Menu.class, "Id", menu.getId() + "");
        menu.setArchived(UN_ARCHIVED);
        return menuRepository.save(menu);
    }

    public Menu getMenuById(Long id) {
        return menuRepository.findByIdAndArchived(id, UN_ARCHIVED).orElseThrow(() ->
            new EntityNotFoundException(Menu.class, "Id", id + ""));
    }

    public Menu update(Long id, Menu menu) {
        Optional<Menu> menuOptional = menuRepository.findByIdAndArchived(menu.getId(), UN_ARCHIVED);
        if (menuOptional.isPresent()) throw new EntityNotFoundException(Menu.class, "Id", id + "");
        menu.setId(id);
        menu.setArchived(UN_ARCHIVED);
        return menuRepository.save(menu);
    }

    public Integer delete(Long id) {
        Optional<Menu> menuOptional = menuRepository.findByIdAndArchived(id, UN_ARCHIVED);
        if (!menuOptional.isPresent()) throw new EntityNotFoundException(Menu.class, "Id", id + "");
        Menu menu = menuOptional.get();
        menu.setArchived(ARCHIVED);
        menuRepository.save(menu);

        return ARCHIVED;
    }
}
