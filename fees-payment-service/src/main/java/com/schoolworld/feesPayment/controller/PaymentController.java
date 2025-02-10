package com.schoolworld.feesPayment.controller;

import com.schoolworld.feesPayment.dto.PaymentRequestDTO;
import com.schoolworld.feesPayment.dto.ResponseDTO;
import com.schoolworld.feesPayment.entity.Payment;
import com.schoolworld.feesPayment.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody final PaymentRequestDTO paymentRequestDTO) {
        return this.paymentService.create(paymentRequestDTO);
    }

    @GetMapping("/retrieve-paid-amount/{studentId}")
    public ResponseDTO retrievePaidAmount(@PathVariable final String studentId) {
        return this.paymentService.retrievePayAmount(studentId);
    }

    @GetMapping("/retrieve-unpaid-amount/{studentId}")
    public ResponseDTO retrieveUnPaidAmount(@PathVariable final String studentId) {
        return this.paymentService.retrieveUnPaidAmount(studentId);
    }

    @GetMapping("/retrieve-student-unpaid-fees-types")
    public ResponseDTO retrieveUnPaidFeesTypeStatus(@RequestParam final String studentId,@RequestParam final String feesType){
        return this.paymentService.retrieveUnPaidFeesTypeStatus(studentId,feesType);
    }

    @GetMapping("/retrieve-student-paid-fees-types")
    public ResponseDTO retrievePaidFeesTypeStatus(@RequestParam final String studentId,@RequestParam final String feesType){
        return this.paymentService.retrievePaidFeesTypeStatus(studentId,feesType);
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieve(@PathVariable final String id) {
        return this.paymentService.retrieve(id);
    }

    @GetMapping("/retrieve-all")
    public ResponseDTO retrieveAll() {
        return this.paymentService.retrieveAll();
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO remove(final String id) {
        return this.paymentService.remove(id);
    }

    @GetMapping("/student-payments")
    public Page<Payment> getUsers(@RequestParam(required = true) final int page,
                                  @RequestParam(required = true) final int size,
                                  @RequestParam(required = true) final String sortBy,
                                  @RequestParam(required = true) final String sortDirection) {

        return paymentService.getUsers(page,size,sortBy,sortDirection);
    }
}
