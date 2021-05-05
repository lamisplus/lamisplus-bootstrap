package org.lamisplus.modules.bootstrap.service;

import com.github.zafarkhaja.semver.Version;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.lamisplus.modules.bootstrap.controller.apierror.EntityNotFoundException;
import org.lamisplus.modules.bootstrap.controller.apierror.IllegalTypeException;
import org.lamisplus.modules.bootstrap.controller.apierror.RecordExistException;
import org.lamisplus.modules.bootstrap.domain.dto.ModuleDependencyDTO;
import org.lamisplus.modules.bootstrap.domain.entity.Menu;
import org.lamisplus.modules.bootstrap.domain.entity.Module;
import org.lamisplus.modules.bootstrap.module.ModuleFileStorageService;
import org.lamisplus.modules.bootstrap.module.ModuleUtils;
import org.lamisplus.modules.bootstrap.repository.MenuRepository;
import org.lamisplus.modules.bootstrap.repository.ModuleRepository;
import org.lamisplus.modules.bootstrap.yml.ModuleConfig;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.jar.Attributes;
import java.util.jar.Manifest;

@Service
@RequiredArgsConstructor
@Slf4j
public class ModuleService {
    private final ModuleRepository moduleRepository;
    private final MenuRepository menuRepository;
    private final ModuleFileStorageService storageService;
    private final int UN_ARCHIVED = 0;

    public Optional<Module> getModule(String id) {
        return moduleRepository.findById(id);
    }

    public Module activate(String id) {
        Module module = moduleRepository.findByIdAndStatus(id, 3).orElseThrow(() ->
            new EntityNotFoundException(Module.class, id+"", "Not Found"));            LOG.debug("Activating module {} ...", module);
        module.setActive(true);
        module.setStatus(2);
        return moduleRepository.save(module);
    }

    public Module deactivate(String id) {
        Module module = moduleRepository.findByIdAndStatus(id, 2).orElseThrow(() ->
            new EntityNotFoundException(Module.class, id+"", "Not Found"));
        LOG.debug("Deactivating module {} ...", module);
        module.setActive(false);
        Optional<Menu> menuOptional = menuRepository.findByModuleId(module.getId());
        menuOptional.ifPresent(menu -> {
            menu.setArchived(1);
            menuRepository.save(menu);
        });
        module.setStatus(3);
        module.setActive(false);
        return moduleRepository.save(module);
    }

    public List<Module> getModules() {
        return moduleRepository.findAllByStatusNot(1);
    }

    @SneakyThrows
    @Transactional
    public Module installOrUpdate(Module updateModule) {
        Module module = moduleRepository.findByName(updateModule.getName()).orElse(updateModule);
        module.setVersion(updateModule.getVersion());
        module.setDescription(updateModule.getDescription());
        module.setBasePackage(updateModule.getBasePackage());
        module.setBuildTime(updateModule.getBuildTime());
        module.setArtifact(updateModule.getArtifact());
        module.setActive(true);
        module.setArchived(UN_ARCHIVED);
        module.setProcessConfig(true);
        module = moduleRepository.save(module);

        saveModuleData(module);
        module.setStatus(2);
        return module;
    }

    public void uninstall(String id) {
        moduleRepository.findById(id).ifPresent(module -> {
            module.setUninstall(true);
            moduleRepository.delete(module);
            Optional<Menu> menuOptional = menuRepository.findByModuleId(module.getId());
            menuOptional.ifPresent(menu -> {
                menuRepository.delete(menu);
            });
            moduleRepository.save(module);
        });
    }

    @SneakyThrows
    public Module uploadModuleData(MultipartFile file) {
        Module module = new Module();
        ModuleConfig config = ModuleUtils.loadModuleConfig(file.getInputStream(), "module.yml");
        String fileName = storageService.store(config.getName(), file);
        URLClassLoader classLoader = new URLClassLoader(new URL[]{storageService.getURL(fileName)});
        URL url = classLoader.findResource("META-INF/MANIFEST.MF");
        Manifest manifest = new Manifest(url.openStream());
        Attributes attributes = manifest.getMainAttributes();
        module.setVersion(attributes.getValue("Implementation-Version"));
        module.setDescription(attributes.getValue("Implementation-Title"));
        if (StringUtils.isNotBlank(config.getSummary())) {
            module.setDescription(config.getSummary());
        }
        try {
            Date date = DateUtils.parseDate(attributes.getValue("Build-Time"), "yyyyMMdd-HHmm",
                "yyyy-MM-dd'T'HH:mm:ss'Z'");
            module.setBuildTime(date);
        } catch (Exception ignored) {
        }
        module.setArtifact(StringUtils.replace(fileName, "\\", "/"));
        module.setName(config.getName());
        module.setBasePackage(config.getBasePackage());
        module.setStatus(1);
        module.setArchived(1);
        return module;
    }

    @SneakyThrows
    @Transactional
    public List<ModuleDependencyDTO> getDependencies(String id) {
        List<ModuleDependencyDTO> dependencies = new ArrayList<>();
        moduleRepository.findById(id).ifPresent(module ->  {
            try {
                InputStream inputStream;
                byte[] data = module.getData();
                if (data != null) {
                    inputStream = new ByteArrayInputStream(data);
                } else {
                    inputStream = storageService.readFile(module.getArtifact());
                }
                ModuleConfig config = ModuleUtils.loadModuleConfig(inputStream, "module.yml");
                if (config != null) {
                    config.getDependencies()
                        .forEach(dependency -> {
                            String name = dependency.getName();
                            String version = dependency.getVersion();

                            ModuleDependencyDTO dto = new ModuleDependencyDTO();
                            dto.setName(name);
                            dto.setRequiredVersion(version);
                            moduleRepository.findByName(name).ifPresent(m -> {
                                dto.setId(m.getId());
                                dto.setActive(m.getActive());
                                dto.setInstalledVersion(m.getVersion());
                                Version installed = Version.valueOf(m.getVersion());
                                dto.setVersionSatisfied(installed.satisfies(version));
                            });
                            dependencies.add(dto);
                        });
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        return dependencies;
    }

    private void saveModuleData(Module module) {
        try {
            InputStream stream = storageService.readFile(module.getArtifact());
            byte[] data = IOUtils.toByteArray(stream);
            ModuleConfig config = ModuleUtils.loadModuleConfig(new ByteArrayInputStream(data), "module.yml");
            if (config != null && config.isStore()) {
                LOG.info("config {}", config);
                module.setData(data);
                Module externalModule = moduleRepository.save(module);

                if(config.getMenuUrl() != null && !config.getMenuUrl().isEmpty()) {
                    Menu menu = new Menu();
                    menu = menuRepository.findByName(module.getName()).orElse(menu);
                    menu.setName(module.getName());
                    menu.setUrl(config.getMenuUrl());
                    menu.setName(config.getMenuName());
                    menu.setArchived(UN_ARCHIVED);
                    menu.setModuleId(externalModule.getId());
                    LOG.info("Module Id: ", externalModule.getId());
                    menuRepository.save(menu);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Module> getAllCoreModulesByModuleType() {
        return moduleRepository.findAllByModuleType(0);
    }

    public Module save(Module module) {
        if(module.getModuleType() != 0) throw new IllegalTypeException(Module.class, module.getName() +"", "Module type not valid");
        Optional<Module> optionalModule = this.moduleRepository.findByName(module.getName());
        if(optionalModule.isPresent()) throw new RecordExistException(Module.class, module.getName() +"", "Exist");
        module.setArchived(UN_ARCHIVED);
        return moduleRepository.save(module);
    }
}
