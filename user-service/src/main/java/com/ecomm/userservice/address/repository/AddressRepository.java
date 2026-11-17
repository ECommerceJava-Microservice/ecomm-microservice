package com.ecomm.userservice.address.repository;

import com.ecomm.userservice.address.entity.Address;
import com.ecomm.userservice.user.entity.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {

    List<Address> findByUserProfile(UserProfile userProfile);

    Optional<Address> findByUserProfileAndIsDefaultTrue(UserProfile userProfile);

    boolean existsByUserProfileAndIsDefaultTrue(UserProfile userProfile);
}
