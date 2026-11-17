package com.ecomm.productservice.product.repository;

import com.ecomm.productservice.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ProductRepository extends JpaRepository<Product, UUID>, JpaSpecificationExecutor<Product> {

    //Order-service üçün
    @Modifying
    @Query("update Product p set p.quantity = p.quantity - :quantity where p.productId = :productId and p.quantity >= :quantity")
    int decreaseStock(@Param("productId") UUID productId, @Param("quantity") Integer quantity);

    //Order-service üçün
    @Modifying
    @Query("update Product p set p.quantity = p.quantity + :quantity where p.productId = :productId")
    int increaseStock(@Param("productId") UUID productId, @Param("quantity") Integer quantity);
}
