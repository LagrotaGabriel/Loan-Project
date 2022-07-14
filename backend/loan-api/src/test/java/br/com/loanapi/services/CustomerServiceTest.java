package br.com.loanapi.services;

import br.com.loanapi.config.ModelMapperConfig;
import br.com.loanapi.exceptions.InvalidRequestException;
import br.com.loanapi.exceptions.ObjectNotFoundException;
import br.com.loanapi.mocks.dto.CustomerDTODataBuilder;
import br.com.loanapi.mocks.entity.CustomerEntityDataBuilder;
import br.com.loanapi.models.entities.CustomerEntity;
import br.com.loanapi.repositories.CustomerRepository;
import br.com.loanapi.validations.CustomerValidation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@DisplayName("Service: Customer")
@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {

    @InjectMocks
    CustomerService service;

    @Mock
    CustomerValidation validation;

    @Mock
    CustomerRepository repository;

    @Mock
    ModelMapperConfig modelMapper;

    @Test
    @DisplayName("Should test create method with success")
    void shouldTestCreateMethodWithSuccess() throws ParseException {

        Mockito.when(modelMapper.mapper()).thenReturn(new ModelMapper());
        Mockito.when(validation.validateRequest(Mockito.any())).thenReturn(true);
        Mockito.when(repository.save(Mockito.any())).thenReturn(CustomerEntityDataBuilder.builder().build());

        Assertions.assertEquals("CustomerDTO(id=1, name=João, lastName=da Silva, " +
                        "birthDate=Fri Nov 11 00:00:00 BRST 2011, signUpDate=Thu Nov 11 00:00:00 BRT 2021, " +
                        "rg=55.626.926-4, cpf=391.534.277-44, email=joao@email.com, address=AddressDTO(id=1, " +
                        "street=Rua 9, neighborhood=Lauzane Paulista, number=583, postalCode=02442-090, " +
                        "city=CityDTO(id=1, city=São Paulo, state=SAO_PAULO, addresses=null), customers=null), " +
                        "score=ScoreDTO(id=1, pontuation=50.0, customer=null), phones=null, loans=null)",
                service.create(CustomerDTODataBuilder.builder().build()).toString());

    }

    @Test
    @DisplayName("Should test create method with exception")
    void shouldTestCreateMethodWithException(){

        Mockito.when(validation.validateRequest(Mockito.any())).thenReturn(false);

        try {
            service.create(CustomerDTODataBuilder.builder().build());
            Assertions.fail();
        }
        catch(InvalidRequestException | ParseException exception) {
            Assertions.assertEquals("Customer validation failed", exception.getMessage());
        }

    }

    @Test
    @DisplayName("Should test findAll method with success")
    void shouldTestFindAllMethodWithSuccess() throws ParseException {

        List<CustomerEntity> customers = new ArrayList<>();
        customers.add(CustomerEntityDataBuilder.builder().build());

        Mockito.when(modelMapper.mapper()).thenReturn(new ModelMapper());
        Mockito.when(repository.findAll()).thenReturn(customers);

        Assertions.assertEquals("[CustomerDTO(id=1, name=João, lastName=da Silva, " +
                        "birthDate=Fri Nov 11 00:00:00 BRST 2011, signUpDate=Thu Nov 11 00:00:00 BRT 2021, " +
                        "rg=55.626.926-4, cpf=391.534.277-44, email=joao@email.com, address=AddressDTO(id=1, " +
                        "street=Rua 9, neighborhood=Lauzane Paulista, number=583, postalCode=02442-090, " +
                        "city=CityDTO(id=1, city=São Paulo, state=SAO_PAULO, addresses=null), customers=null), " +
                        "score=ScoreDTO(id=1, pontuation=50.0, customer=null), phones=null, loans=null)]",
                service.findAll().toString());

    }


    @Test
    @DisplayName("Should test findAll method with exception")
    void shouldTestFindAllMethodWithException(){

        List<CustomerEntity> customers = new ArrayList<>();
        Mockito.when(repository.findAll()).thenReturn(customers);

        try {
            service.findAll();
            Assertions.fail();
        }
        catch(ObjectNotFoundException exception) {
            Assertions.assertEquals("There is no consumers saved in the database", exception.getMessage());
        }

    }

    @Test
    @DisplayName("Should test findById method with success")
    void shouldTestFindByIdMethodWithSuccess() throws ParseException {

        Mockito.when(modelMapper.mapper()).thenReturn(new ModelMapper());
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(CustomerEntityDataBuilder.builder().build()));

        Assertions.assertEquals("CustomerDTO(id=1, name=João, lastName=da Silva, " +
                        "birthDate=Fri Nov 11 00:00:00 BRST 2011, signUpDate=Thu Nov 11 00:00:00 BRT 2021, " +
                        "rg=55.626.926-4, cpf=391.534.277-44, email=joao@email.com, address=AddressDTO(id=1, " +
                        "street=Rua 9, neighborhood=Lauzane Paulista, number=583, postalCode=02442-090, " +
                        "city=CityDTO(id=1, city=São Paulo, state=SAO_PAULO, addresses=null), customers=null), " +
                        "score=ScoreDTO(id=1, pontuation=50.0, customer=null), phones=null, loans=null)",
                service.findById(1L).toString());

    }

    @Test
    @DisplayName("Should test findById method with exception")
    void shouldTestFindByIdMethodWithException() {

        Mockito.when(modelMapper.mapper()).thenReturn(new ModelMapper());
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.empty());

        try {
            service.findById(1L);
        }
        catch (ObjectNotFoundException exception){
            Assertions.assertEquals("Customer not found", exception.getMessage());
        }

    }

    @Test
    @DisplayName("Should test update method with success")
    void shouldTestUpdateMethodWithSuccess() throws ParseException {

        Mockito.when(modelMapper.mapper()).thenReturn(new ModelMapper());
        Mockito.when(validation.validateRequest(Mockito.any())).thenReturn(true);
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(CustomerEntityDataBuilder.builder().build()));
        Mockito.when(repository.save(Mockito.any())).thenReturn(CustomerEntityDataBuilder.builder().build());

        Assertions.assertEquals("CustomerDTO(id=1, name=João, lastName=da Silva, " +
                        "birthDate=Fri Nov 11 00:00:00 BRST 2011, signUpDate=Thu Nov 11 00:00:00 BRT 2021, " +
                        "rg=55.626.926-4, cpf=391.534.277-44, email=joao@email.com, address=AddressDTO(id=1, " +
                        "street=Rua 9, neighborhood=Lauzane Paulista, number=583, postalCode=02442-090, " +
                        "city=CityDTO(id=1, city=São Paulo, state=SAO_PAULO, addresses=null), customers=null), " +
                        "score=ScoreDTO(id=1, pontuation=50.0, customer=null), phones=null, loans=null)",
                service.update(1L, CustomerDTODataBuilder.builder().build()).toString());

    }

    @Test
    @DisplayName("Should test update method with exception")
    void shouldTestUpdateMethodWithException() {

        Mockito.when(validation.validateRequest(Mockito.any())).thenReturn(false);

        try{
            service.update(1L, CustomerDTODataBuilder.builder().build());
            Assertions.fail();
        }
        catch(InvalidRequestException | ParseException exception){
            Assertions.assertEquals("Customer validation failed", exception.getMessage());
        }
    }

    @Test
    @DisplayName("Should test delete method with success")
    void shouldTestDeleteMethodWithSuccess() throws ParseException {
        Mockito.when(modelMapper.mapper()).thenReturn(new ModelMapper());
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.of(CustomerEntityDataBuilder.builder().build()));
        Assertions.assertTrue(service.deleteById(1L));
    }

    @Test
    @DisplayName("Should test delete method with exception")
    void shouldTestDeleteMethodWithException() {
        Mockito.when(repository.findById(Mockito.any())).thenReturn(Optional.empty());

        try{
            service.deleteById(1L);
            Assertions.fail();
        }
        catch (ObjectNotFoundException exception) {
            Assertions.assertEquals("Customer not found", exception.getMessage());
        }

    }


}