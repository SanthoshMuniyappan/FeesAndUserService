package com.schoolworld.feesPayment.controller;

import com.schoolworld.feesPayment.dto.FeesRequestDTO;
import com.schoolworld.feesPayment.dto.ResponseDTO;
import com.schoolworld.feesPayment.service.FeesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fees")
public class FeesController {

    @Autowired
    private FeesService feesService;

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody final FeesRequestDTO feesRequestDTO) {
        return this.feesService.create(feesRequestDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO update(@PathVariable final String id, @RequestBody final FeesRequestDTO feesRequestDTO) {
        return this.feesService.update(id, feesRequestDTO);
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieve(@PathVariable final String id) {
        return this.feesService.retrieve(id);
    }

    @GetMapping("/retrieve-all")
    public ResponseDTO retrieveAll() {
        return this.feesService.retrieveAll();
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO remove(@PathVariable final String id) {
        return this.feesService.remove(id);
    }

    @GetMapping("/student-id-fees-type")
    public ResponseDTO studentFees(@RequestParam final String studentId, @RequestParam final String feesCategory) {
        return this.feesService.studentFees(studentId, feesCategory);
    }

    @GetMapping("/standard-all-fees-types")
    public ResponseDTO standardAllFeesTypes(@RequestParam final String standardId) {
        return this.feesService.standardAllFeesTypes(standardId);
    }
}
