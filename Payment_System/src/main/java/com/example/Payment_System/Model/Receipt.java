package com.example.Payment_System.Model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Receipt {
    private String id;
    private String status;  // OK / ERROR
    private String corrId;  // Связь запроса и квитанции
}