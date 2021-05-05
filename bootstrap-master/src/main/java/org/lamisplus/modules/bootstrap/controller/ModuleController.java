package org.lamisplus.modules.bootstrap.controller;

import com.foreach.across.core.annotations.Exposed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lamisplus.modules.bootstrap.domain.dto.ModuleDependencyDTO;
import org.lamisplus.modules.bootstrap.domain.entity.Module;
import org.lamisplus.modules.bootstrap.service.ModuleService;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
@Slf4j
@RequiredArgsConstructor
@Exposed
public class ModuleController {
    private final ModuleService moduleService;

    @GetMapping("/{id}")
    public Module getModule(@PathVariable("id") String id) {
        LOG.debug("Getting module: {}", id);

        return moduleService.getModule(id).orElseThrow(() ->new RuntimeException("Module not found"));
    }

    @GetMapping
    public List<Module> getWebModules() {
        LOG.debug("Getting all modules");
        return moduleService.getModules();
    }

    @GetMapping("/core")
    public ResponseEntity<List> getAllCoreModulesByModuleStatus() {
        return ResponseEntity.ok(moduleService.getAllCoreModulesByModuleType());
    }

    @PostMapping("/core")
    public Module save(@RequestBody Module module) {
        return moduleService.save(module);
    }

    @PostMapping("/{id}/activate")
    public Module activateModule(@PathVariable String id) {
        return moduleService.activate(id);
    }

    @PostMapping("/{id}/deactivate")
    public Module deactivateModule(@PathVariable String id) {
        return moduleService.deactivate(id);
    }

    @GetMapping("/{id}/uninstall")
    @CacheEvict({"modules"})
    public void uninstallModule(@PathVariable String id) {
        moduleService.uninstall(id);
    }

    @PostMapping("/update")
    public Module updateModule(@RequestBody Module module) {
        return moduleService.installOrUpdate(module);
    }

    @PostMapping("/upload")
    public Module uploadModuleData(@RequestParam("file") MultipartFile file) {
        return moduleService.uploadModuleData(file);
    }

    @PostMapping("/install")
    public Module installModule(final @RequestBody Module module) {
        return moduleService.installOrUpdate(module);
    }

    @GetMapping("/{id}/dependencies")
    public List<ModuleDependencyDTO> getDependencies(@PathVariable String id) {
        return moduleService.getDependencies(id);
    }
}
