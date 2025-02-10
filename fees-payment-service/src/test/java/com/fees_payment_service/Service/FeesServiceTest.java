package com.fees_payment_service.Service;

import com.schoolworld.feesPayment.dto.FeesRequestDTO;
import com.schoolworld.feesPayment.dto.ResponseDTO;
import com.schoolworld.feesPayment.entity.Fees;
import com.schoolworld.feesPayment.entity.Standard;
import com.schoolworld.feesPayment.exception.BadServiceAlertException;
import com.schoolworld.feesPayment.repository.FeesRepository;
import com.schoolworld.feesPayment.repository.StandardRepository;
import com.schoolworld.feesPayment.service.FeesService;
import com.schoolworld.feesPayment.util.AuthenticationService;
import com.schoolworld.feesPayment.util.Constants;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static junit.framework.TestCase.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FeesServiceTest {

    @Mock
    private StandardRepository standardRepository;

    @Mock
    private FeesRepository feesRepository;

    @Mock
    private AuthenticationService authenticationService;

    @InjectMocks
    private FeesService feesService;

    @Test
    public void createTest() {
        FeesRequestDTO feesRequestDTO = new FeesRequestDTO();
        feesRequestDTO.setFeesAmount(10000);
        feesRequestDTO.setFeesCategory("TutionFees");
        feesRequestDTO.setStandardId(Constants.ID);

        Fees fees = new Fees();
        Standard standard = new Standard();
        fees.setFeesAmount(feesRequestDTO.getFeesAmount());
        fees.setFeesCategory(feesRequestDTO.getFeesCategory());
        fees.setStandard(standard);

        when(authenticationService.getUserName()).thenReturn("santhosh");

        fees.setCreatedBy(authenticationService.getUserName());
        fees.setUpdatedBy(authenticationService.getUserName());

        when(this.standardRepository.findById(feesRequestDTO.getStandardId())).thenReturn(Optional.of(standard));
        when(this.feesRepository.save(any(Fees.class))).thenReturn(fees);

        ResponseDTO response = this.feesService.create(feesRequestDTO);

        assertNotNull(response);
        assertEquals(Constants.CREATED, response.getMessage());
        assertEquals(fees, response.getData());
        verify(feesRepository, times(1)).save(any(Fees.class));
    }

    @Test
    public void updateTest() {
        FeesRequestDTO feesRequestDTO = new FeesRequestDTO();
        feesRequestDTO.setFeesAmount(10000);

        Fees fees = new Fees();
        fees.setFeesAmount(feesRequestDTO.getFeesAmount());

        when(this.feesRepository.findById(Constants.ID)).thenReturn(Optional.of(fees));
        when(this.feesRepository.save(any(Fees.class))).thenReturn(fees);

        ResponseDTO response = this.feesService.update(Constants.ID, feesRequestDTO);

        assertNotNull(response);
        assertEquals(Constants.UPDATED, response.getMessage());
        assertEquals(fees, response.getData());
        verify(feesRepository, times(1)).save(any(Fees.class));
    }


    @Test
    public void retrieveTest() {
        Fees fees = new Fees();

        when(this.feesRepository.findById(Constants.ID)).thenReturn(Optional.of(fees));

        ResponseDTO response = this.feesService.retrieve(Constants.ID);

        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(fees, response.getData());
    }

    @Test
    public void retrieveAllTest() {
        List<Fees> fees = new ArrayList<>();
        when(this.feesRepository.retrieveAll()).thenReturn(fees);

        ResponseDTO response = this.feesService.retrieveAll();

        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(fees, response.getData());
    }

    @Test
    public void removeTest() {

        Fees fees = new Fees();
        when(this.feesRepository.findById(Constants.ID)).thenReturn(Optional.of(fees));
        when(this.feesRepository.save(fees)).thenReturn(fees);

        ResponseDTO response = this.feesService.remove(Constants.ID);

        assertNotNull(response);
        TestCase.assertEquals(Constants.DELETED, response.getMessage());
        TestCase.assertEquals(fees, response.getData());
    }

    @Test(expected = BadServiceAlertException.class)
    public void createNegativeTest() {
        FeesRequestDTO feesRequestDTO = new FeesRequestDTO();
        feesRequestDTO.setStandardId(Constants.ID);

        Standard standard = new Standard();
        standard.setDelete(true);

        when(this.standardRepository.findById(feesRequestDTO.getStandardId())).thenReturn(Optional.of(standard));
        this.feesService.create(feesRequestDTO);
    }

    @Test(expected = BadServiceAlertException.class)
    public void updateNegativeTest() {
        FeesRequestDTO feesRequestDTO = new FeesRequestDTO();
        feesRequestDTO.setFeesAmount(10000);

        Fees fees = new Fees();
        fees.setDelete(true);
        when(this.feesRepository.findById(Constants.ID)).thenReturn(Optional.of(fees));
        this.feesService.update(Constants.ID, feesRequestDTO);
    }

    @Test(expected = BadServiceAlertException.class)
    public void retrieveNegativeTest() {
        Fees fees = new Fees();
        fees.setDelete(true);
        when(this.feesRepository.findById(Constants.ID)).thenReturn(Optional.of(fees));
        this.feesService.retrieve(Constants.ID);
    }

    @Test
    public void retrieveAllNegativeTest() {
        when(this.feesRepository.retrieveAll()).thenReturn(null);
        ResponseDTO response = this.feesService.retrieveAll();
        assertNotNull(response);
        TestCase.assertEquals(Constants.RETRIEVED, response.getMessage());
        TestCase.assertEquals(null, response.getData());
    }

    @Test(expected = BadServiceAlertException.class)
    public void removeNegativeTest() {
        Fees fees = new Fees();
        fees.setDelete(true);
        when(this.feesRepository.findById(Constants.ID)).thenReturn(Optional.of(fees));
        this.feesService.remove(Constants.ID);
    }
}
