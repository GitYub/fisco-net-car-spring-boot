package org.fisco.bcos.business.param;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class RegisterParam {

    private String username;

    private String idCard;

    private String phoneNumber;

    private String password;

    private int platform;

}
