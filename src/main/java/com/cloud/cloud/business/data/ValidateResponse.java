package com.cloud.cloud.business.data;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ValidateResponse {
    String message;
    String email;
}
