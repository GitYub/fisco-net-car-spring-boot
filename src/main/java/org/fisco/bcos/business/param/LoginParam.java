package org.fisco.bcos.business.param;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LoginParam {

    private String phoneNumber;

    private String password;

}
