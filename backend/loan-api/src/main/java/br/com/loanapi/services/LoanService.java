package br.com.loanapi.services;

import br.com.loanapi.config.ModelMapperConfig;
import br.com.loanapi.exceptions.ConnectionFailedException;
import br.com.loanapi.exceptions.InvalidRequestException;
import br.com.loanapi.exceptions.ObjectNotFoundException;
import br.com.loanapi.models.dto.InstallmentDTO;
import br.com.loanapi.models.dto.LoanDTO;
import br.com.loanapi.models.entities.CustomerEntity;
import br.com.loanapi.models.entities.LoanEntity;
import br.com.loanapi.proxys.InstallmentServiceProxy;
import br.com.loanapi.repositories.CustomerRepository;
import br.com.loanapi.repositories.LoanRepository;
import br.com.loanapi.validations.LoanValidation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static br.com.loanapi.utils.StringConstants.*;

@Slf4j
@Service
public class LoanService {

    @Autowired
    LoanRepository repository;

    @Autowired
    CustomerRepository customerRepository;

    @Autowired
    InstallmentServiceProxy proxy;

    @Autowired
    ModelMapperConfig modelMapper;

    String LOAN_NOT_FOUND = "Loan not found";

    LoanValidation validation = new LoanValidation();

    public LoanDTO create(Long customerId, LoanDTO loan){

        log.info(LOG_BAR);
        log.info("[STARTING] Starting create method");

        log.info("[PROGRESS] Searching for a customer with the customerId received in JSON...");
        Optional<CustomerEntity> optionalCustomer = customerRepository.findById(customerId);

        if (validation.validateRequest(loan) && optionalCustomer.isPresent()) {

            CustomerEntity customer = optionalCustomer.get();
            log.warn("[INFO] Customer found: {} {}", customer.getName(), customer.getLastName());

            log.info("[PROGRESS] Trying to connect into installment calculation micro service...");
            ResponseEntity<List<InstallmentDTO>> installmentMicroServiceRequest = proxy.calculateInstallments(loan);

            if (installmentMicroServiceRequest.getStatusCode() == HttpStatus.OK) {
                log.warn("[INFO] Microservice connection successfull");
                List<InstallmentDTO> loanInstallments = installmentMicroServiceRequest.getBody();

                log.info("[PROGRESS] Setting the calculated installments into the loan...");
                loan.setInstallments(loanInstallments);

                log.info("[PROGRESS] Setting the debit balance equals than the original loan value");
                loan.setDebitBalance(loan.getOriginalValue());

                log.info("[PROGRESS] Saving the loan into the customer with id {}", customerId);
                customer.addLoan(modelMapper.mapper().map(loan, LoanEntity.class));

                log.info("[PROGRESS] Updating the customer in the database...");
                customerRepository.save(customer);
            }
            else {
                log.error(MICROSERVICE_CONNECTION_FAILED_LOG);
                throw new ConnectionFailedException("Installment Microservice connection failed");
            }

            return loan;
        }
        else {
            log.warn(CUSTOMER_NOT_FOUND_LOG);
            throw new InvalidRequestException(CUSTOMER_NOT_FOUND);
        }

    }

    public List<LoanDTO> findAll() {
        log.info(LOG_BAR);
        log.info("[STARTING] Starting findAll method...");
        log.info("[PROGRESS] Verifying if there is loans saved in the database...");
        if(!repository.findAll().isEmpty()) {
            log.info(REQUEST_SUCCESSFULL);
            return repository.findAll().stream().map(x -> modelMapper.mapper().map(x, LoanDTO.class))
                    .collect(Collectors.toList());
        }
        log.error("[FAILURE]  There is no loans saved in the database");
        throw new ObjectNotFoundException("There is no loans saved in the database");
    }

    public LoanDTO findById(Long id) {

        log.info(LOG_BAR);
        log.info("[STARTING] Starting findById method...");

        log.info("[PROGRESS] Searching for a loan by id {}...", id);
        Optional<LoanEntity> loan = repository.findById(id);

        loan.ifPresent(loanEntity -> log.info(REQUEST_SUCCESSFULL));
        if(loan.isEmpty()) log.error("[FAILURE]  Loan with id {} not found", id);

        return modelMapper.mapper().map(
                loan.orElseThrow(() -> new ObjectNotFoundException(LOAN_NOT_FOUND)), LoanDTO.class);
    }

    public Boolean delete(Long id){

        log.info(LOG_BAR);
        log.info("[STARTING] Starting deleteById method...");

        log.info("[PROGRESS] Searching for a loan by id {}...", id);
        Optional<LoanEntity> loan = repository.findById(id);

        if (loan.isPresent()) {
            log.warn("[PROGRESS] Loan finded. Removing...");
            repository.deleteById(id);

            log.warn(REQUEST_SUCCESSFULL);
            return true;
        }
        log.error("[FAILURE]  Loan with id {} not found", id);
        throw new ObjectNotFoundException(LOAN_NOT_FOUND);
    }

}
