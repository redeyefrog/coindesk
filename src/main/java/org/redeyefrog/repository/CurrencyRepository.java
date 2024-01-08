package org.redeyefrog.repository;

import org.redeyefrog.entity.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CurrencyRepository extends JpaRepository<CurrencyEntity, String> {

    @Modifying
    @Query("DELETE FROM CurrencyEntity WHERE currency = ?1 ")
    void deleteById(String id);

}
