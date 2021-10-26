package com.sales.manager.application.repository;

import com.sales.manager.application.entity.Seller;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface SellerRepository extends CrudRepository<Seller, Long> {
    @Query("from Seller s where s.name = :name")
    List<Seller> findByName(String name);
}
