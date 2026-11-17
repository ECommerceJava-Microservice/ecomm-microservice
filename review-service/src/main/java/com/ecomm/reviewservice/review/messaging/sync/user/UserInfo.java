package com.ecomm.reviewservice.review.messaging.sync.user;

public record UserInfo(
        String firstName,
        String lastName
) {

    public String fullName() {
        return firstName + " " + lastName;
    }
}
