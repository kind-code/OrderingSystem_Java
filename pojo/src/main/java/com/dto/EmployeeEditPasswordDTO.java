package com.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeEditPasswordDTO {
    private long empId;
    private String newPassword;
    private String oldPassword;
}
