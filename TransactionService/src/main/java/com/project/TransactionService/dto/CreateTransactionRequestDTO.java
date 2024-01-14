package com.project.TransactionService.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateTransactionRequestDTO {

    @NotBlank
    private String receiver;

    private String purpose;

    @NotBlank
    private String amount;
}
