package com.ukrainianboyz.nearly.model.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@AllArgsConstructor
@EqualsAndHashCode
@Getter
public class SecureUser {

    String userId;

    String userName;

    String userBio;

    String imageUrl;

    Integer statusIndicator;
}
