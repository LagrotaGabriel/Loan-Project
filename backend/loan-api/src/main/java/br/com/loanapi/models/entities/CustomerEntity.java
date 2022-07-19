package br.com.loanapi.models.entities;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/** Class that contains all attributes of the object of type CustomerEntity
 ** @author Gabriel Lagrota
 ** @version 1.0.0
 ** @since 30/06/2022
 ** @email gabriellagrota23@gmail.com
 ** @github https://github.com/LagrotaGabriel/Loan-Project/blob/master/backend/loan-api/src/main/java/br/com/loanapi/models/entities/CustomerEntity.java */
@Entity
@Table(name = "TB_CUSTOMER")
@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class CustomerEntity {

    @Id
    @Column(name = "customer_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "customer_name", length = 65)
    private String name;

    @Column(name = "customer_lastNname", length = 65)
    private String lastName;

    @Column(name = "customer_birthDate")
    private String birthDate;

    @Column(name = "customer_signUpDate")
    private String signUpDate;

    @Column(name = "customer_rg", length = 12, unique = true)
    private String rg;

    @Column(name = "customer_cpf", length = 14, unique = true)
    private String cpf;

    @Column(name = "customer_email", length = 65, unique = true)
    private String email;

    @ManyToOne
//    @JoinColumn(name = "address_id")
    private AddressEntity address;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "score_id", referencedColumnName = "score_id")
    private ScoreEntity score;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<PhoneEntity> phones = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<LoanEntity> loans = new ArrayList<>();

}
