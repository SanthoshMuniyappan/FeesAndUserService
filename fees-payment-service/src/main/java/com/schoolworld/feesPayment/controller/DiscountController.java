package com.schoolworld.feesPayment.controller;

import com.schoolworld.feesPayment.dto.DiscountRequestDTO;
import com.schoolworld.feesPayment.dto.ResponseDTO;
import com.schoolworld.feesPayment.entity.Discount;
import com.schoolworld.feesPayment.service.DiscountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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
@RequestMapping("/discount")
public class DiscountController {

    @Autowired
    private DiscountService discountService;

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody final DiscountRequestDTO discountRequestDTO) {
        return this.discountService.create(discountRequestDTO);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO update(@PathVariable final String id, @RequestBody final DiscountRequestDTO discountRequestDTO) {
        return this.discountService.update(id, discountRequestDTO);
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieve(@PathVariable final String id) {
        return this.discountService.retrieve(id);
    }

    @GetMapping("/retrieve-all")
    public ResponseDTO retrieveAll() {
        return this.discountService.retrieveAll();
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO remove(@PathVariable final String id) {
        return this.discountService.remove(id);
    }

    @GetMapping("/retrieve-student-discount/{studentId}")
    public ResponseDTO retrieveStudentDiscountAmount(@PathVariable final String studentId) {
        return this.discountService.studentDiscountAmount(studentId);
    }

    @GetMapping("/student-discounts")
    public Page<Discount> getUsers(@RequestParam(required = true) final int page,
                                   @RequestParam(required = true) final int size,
                                   @RequestParam(required = true) final String sortBy,
                                   @RequestParam(required = true) final String sortDirection) {

        return discountService.getUsers(page, size, sortBy, sortDirection);
    }
}
