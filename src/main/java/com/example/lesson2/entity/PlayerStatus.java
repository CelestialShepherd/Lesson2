package com.example.lesson2.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum PlayerStatus {

    ACTIVE(false),
    INACTIVE(true);

    private boolean val;
}
