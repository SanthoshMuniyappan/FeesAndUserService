package com.schoolworld.user.controller;

import com.schoolworld.user.dto.ResponseDTO;
import com.schoolworld.user.dto.RoleRequestDTO;
import com.schoolworld.user.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/role")
public class RoleController {

    @Autowired
    private RoleService roleService;

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody final RoleRequestDTO roleRequestDTO) {
        return this.roleService.create(roleRequestDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO update(@PathVariable final String id, @RequestBody final RoleRequestDTO roleRequestDTO) {
        return this.roleService.update(id, roleRequestDTO);
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieve(@PathVariable final String id) {
        return this.roleService.retrieve(id);
    }

    @GetMapping("/retrieve")
    public ResponseDTO retrieveAll() {
        return this.roleService.retrieveAll();
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO remove(@PathVariable final String id) {
        return this.roleService.remove(id);
    }
}
