package com.schoolworld.feesPayment.controller;

import com.schoolworld.feesPayment.dto.ResponseDTO;
import com.schoolworld.feesPayment.dto.ScholarShipRequestDTO;
import com.schoolworld.feesPayment.entity.ScholarShip;
import com.schoolworld.feesPayment.service.ScholarShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scholar-ship")
public class ScholarShipController {

    @Autowired
    private ScholarShipService scholarShipService;

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody final ScholarShipRequestDTO scholarShipRequestDTO) {
        return this.scholarShipService.create(scholarShipRequestDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO update(@PathVariable final String id, @RequestBody final ScholarShipRequestDTO scholarShipRequestDTO) {
        return this.scholarShipService.update(id, scholarShipRequestDTO);
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieve(@PathVariable final String id) {
        return this.scholarShipService.retrieve(id);
    }

    @GetMapping("/retrieve-all")
    public ResponseDTO retrieveAll() {
        return this.scholarShipService.retrieveAll();
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO remove(@PathVariable final String id) {
        return this.scholarShipService.remove(id);
    }

    @GetMapping("/retrieve-student/{studentId}")
    public ResponseDTO retrieveStudent(@PathVariable final String studentId) {
        return this.scholarShipService.retrieveStudent(studentId);
    }

    @GetMapping("/student-scholarships")
    public Page<ScholarShip> getUsers(@RequestParam(required = true) final int page,
                                      @RequestParam(required = true) final int size,
                                      @RequestParam(required = true) final String sortBy,
                                      @RequestParam(required = true) final String sortDirection) {

        return scholarShipService.getUsers(page,size,sortBy,sortDirection);
    }
}
