package com.dto;

import lombok.Data;

@Data
public class PaymentDTO {
    private String orderNumber;
    private Integer payMethod;
}
