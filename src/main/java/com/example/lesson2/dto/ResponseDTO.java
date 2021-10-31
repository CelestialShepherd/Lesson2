package com.example.lesson2.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

// FilterDTO
@AllArgsConstructor
@NoArgsConstructor
@Data // @Getter, @Setter, @RequiredArgsConstructor, etc.
@Builder
public class ResponseDTO {

    private List<PlayerDTO> result;
}
