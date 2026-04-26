package com.hmi.alarm.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AcknowledgeRequestDTO {

    @NotBlank(message = "Acknowledged by (operator name) is required")
    private String acknowledgedBy;

    private String remarks;
}
