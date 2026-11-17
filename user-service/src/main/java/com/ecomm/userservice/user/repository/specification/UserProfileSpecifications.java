package com.ecomm.userservice.user.repository.specification;

import com.ecomm.userservice.user.entity.UserProfile;
import org.springframework.data.jpa.domain.Specification;

public final class UserProfileSpecifications {

    private UserProfileSpecifications() {
    }

    public static Specification<UserProfile> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null || name.isBlank()) {
                return null;
            }

            String pattern = "%" + name.trim().toLowerCase() + "%";

            return criteriaBuilder.or(
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("firstName")),
                            pattern
                    ),
                    criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("lastName")),
                            pattern
                    )
            );
        };
    }
}
