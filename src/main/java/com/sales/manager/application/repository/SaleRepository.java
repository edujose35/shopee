package com.sales.manager.application.repository;

import com.sales.manager.application.entity.Sale;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SaleRepository extends CrudRepository<Sale, Long> {
    @Query("from Sale s order by s.value desc")
    List<Sale> getAllSales();
}
