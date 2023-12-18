package com.example.db.domain;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Entity
@Getter
@Setter
@Table(name = "balance")
public class Balance {

    @Id
    @Column(nullable = false, updatable = false)
    @SequenceGenerator(
            name = "primary_sequence",
            sequenceName = "primary_sequence",
            allocationSize = 1,
            initialValue = 10000
    )
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "primary_sequence"
    )
    private Integer id;

    @Column
    private LocalDate createDate;

    @Column(precision = 10, scale = 2)
    private BigDecimal debit;

    @Column(precision = 10, scale = 2)
    private BigDecimal credit;

    @Column(precision = 10, scale = 2)
    private BigDecimal amount;

}
