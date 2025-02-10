package com.schoolworld.feesPayment.service;

import com.schoolworld.feesPayment.dto.ResponseDTO;
import com.schoolworld.feesPayment.dto.ScholarShipRequestDTO;
import com.schoolworld.feesPayment.entity.ScholarShip;
import com.schoolworld.feesPayment.entity.Student;
import com.schoolworld.feesPayment.exception.BadServiceAlertException;
import com.schoolworld.feesPayment.exception.StudentNotFoundException;
import com.schoolworld.feesPayment.repository.ScholarShipRepository;
import com.schoolworld.feesPayment.repository.StudentRepository;
import com.schoolworld.feesPayment.util.AuthenticationService;
import com.schoolworld.feesPayment.util.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ScholarShipService {

    @Autowired
    private ScholarShipRepository scholarShipRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @Transactional
    public ResponseDTO create(final ScholarShipRequestDTO scholarShipRequestDTO) {
        final ScholarShip scholarShip = new ScholarShip();
        final Student student = this.studentRepository.findById(scholarShipRequestDTO.getStudentId()).orElseThrow(() -> new StudentNotFoundException(Constants.STUDENT_ID));
        if (!student.isDelete()) {
            scholarShip.setStudent(student);
            scholarShip.setScholarShip(scholarShipRequestDTO.isScholarShip());
            scholarShip.setScholarShipAmount(scholarShipRequestDTO.getScholarShipAmount());
            scholarShip.setCreatedBy(authenticationService.getUserName());
            scholarShip.setUpdatedBy(authenticationService.getUserName());
            return new ResponseDTO(Constants.CREATED, this.scholarShipRepository.save(scholarShip), HttpStatus.CREATED.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    @Transactional
    public ResponseDTO update(final String id, final ScholarShipRequestDTO scholarShipRequestDTO) {
        final ScholarShip scholarShip = this.scholarShipRepository.findById(id).orElseThrow(() -> new BadServiceAlertException(Constants.SCHOLARSHIP_ID));
        if (!scholarShip.isDelete()) {
            if (scholarShipRequestDTO.getScholarShipAmount() != 0) {
                scholarShip.setScholarShipAmount(scholarShipRequestDTO.getScholarShipAmount());
            }
            if (authenticationService.getUserName() != null) {
                scholarShip.setUpdatedBy(authenticationService.getUserName());
            }
            return new ResponseDTO(Constants.UPDATED, this.scholarShipRepository.save(scholarShip), HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    @Transactional
    public ResponseDTO remove(final String id) {
        final ScholarShip scholarShip = this.scholarShipRepository.findById(id).orElseThrow(() -> new BadServiceAlertException(Constants.SCHOLARSHIP_ID));
        if (!scholarShip.isDelete()) {
            scholarShip.setDelete(true);
            return new ResponseDTO(Constants.DELETED, this.scholarShipRepository.save(scholarShip), HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public ResponseDTO retrieve(final String id) {
        final ScholarShip scholarShip = this.scholarShipRepository.findById(id).orElseThrow(() -> new BadServiceAlertException(Constants.SCHOLARSHIP_ID));
        if (!scholarShip.isDelete()) {
            return new ResponseDTO(Constants.RETRIEVED, scholarShip, HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_NOT_AVAILABLE);
        }
    }

    public ResponseDTO retrieveAll() {
        return new ResponseDTO(Constants.RETRIEVED, this.scholarShipRepository.retrieveAll(), HttpStatus.OK.getReasonPhrase());
    }

    public ResponseDTO retrieveStudent(final String studentId) {
        final Student student = this.studentRepository.findById(studentId).orElseThrow(() -> new StudentNotFoundException(Constants.STUDENT_ID));
        if (!student.isDelete()) {
            int scholarShipAmount = this.scholarShipRepository.findScholarShipAmountByStudentId(studentId);
            return new ResponseDTO(Constants.RETRIEVED, scholarShipAmount, HttpStatus.OK.getReasonPhrase());
        } else {
            throw new BadServiceAlertException(Constants.ID_DELETED_IN_TABLE);
        }
    }

    public Page<ScholarShip> getUsers(int page, int size, String sortBy, String sortDirection) {

        Sort sort = Sort.by(Sort.Order.by(sortBy));
        if ("desc".equalsIgnoreCase(sortDirection)) {
            sort = sort.descending();
        } else {
            sort = sort.ascending();
        }
        Pageable pageable = PageRequest.of(page, size, sort);
        return scholarShipRepository.findByIsDeleteFalse(pageable);
    }
}
