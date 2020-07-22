package com.ukrainianboyz.nearly.db.entity;

import lombok.*;

import java.io.Serializable;

//TODO: MAKE DB FOLDER
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class UserRelationshipPK implements Serializable{
    private String requesterId;
    private String responderId;
}
