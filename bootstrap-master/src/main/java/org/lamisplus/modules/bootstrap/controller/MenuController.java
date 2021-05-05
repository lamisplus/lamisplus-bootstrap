package org.lamisplus.modules.bootstrap.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lamisplus.modules.bootstrap.domain.entity.Menu;
import org.lamisplus.modules.bootstrap.service.MenuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
@Slf4j
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    @GetMapping
    public ResponseEntity<List<Menu>> getAllStandardCodesetSource() {
        return ResponseEntity.ok(this.menuService.getAllMenu());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Menu> getMenuById(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.getMenuById(id));
    }

    @PostMapping
    public ResponseEntity<Menu> save(@RequestBody Menu menu) {
        return ResponseEntity.ok(menuService.save(menu));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Menu> update(@PathVariable Long id, @RequestBody Menu menu) {
        return ResponseEntity.ok(this.menuService.update(id, menu));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Integer> delete(@PathVariable Long id) {
        return ResponseEntity.ok(this.menuService.delete(id));
    }
}
