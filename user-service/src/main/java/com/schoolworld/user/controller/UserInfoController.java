package com.schoolworld.user.controller;

import com.schoolworld.user.dto.ResponseDTO;
import com.schoolworld.user.dto.UserInfoRequestDTO;
import com.schoolworld.user.entity.User;
import com.schoolworld.user.entity.UserInfo;
import com.schoolworld.user.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user-info")
public class UserInfoController {

    @Autowired
    private UserInfoService userInfoService;

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody final UserInfoRequestDTO userInfoRequestDTO) {
        return this.userInfoService.create(userInfoRequestDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO update(@PathVariable final String id, @RequestBody final UserInfoRequestDTO userInfoRequestDTO) {
        return this.userInfoService.update(id, userInfoRequestDTO);
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieve(@PathVariable final String id) {
        return this.userInfoService.retrieve(id);
    }

    @GetMapping("/retrieve")
    public ResponseDTO retrieveAll() {
        return this.userInfoService.retrieveAll();
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO remove(@PathVariable final String id) {
        return this.userInfoService.remove(id);
    }

    @GetMapping("/users-info")
    public Page<UserInfo> getUsers(@RequestParam(required = true) final int page,
                                   @RequestParam(required = true) final int size,
                                   @RequestParam(required = true) final String sortBy,
                                   @RequestParam(required = true) final String sortDirection) {

        return userInfoService.getUsers(page,size,sortBy,sortDirection);
    }
}
