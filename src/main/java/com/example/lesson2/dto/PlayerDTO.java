package com.example.lesson2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@NoArgsConstructor
@Data // @Getter, @Setter, @RequiredArgsConstructor, etc.
@Builder
public class PlayerDTO {

    private long id;
    private String nickname;
    private String description;
}
