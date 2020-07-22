package com.ukrainianboyz.nearly.model.requestdata;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor  //for testing
public class FirebaseCommandRequest {

    private String receiverId;
    private String senderId;
    private String command;
    private Boolean isUrgent;

}
