package org.redeyefrog.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "CURRENCY")
public class CurrencyEntity {

    @Id
    @Column(name = "CURRENCY")
    private String currency;

    @Column(name = "CURRENCY_NAME")
    private String currencyName;

}
