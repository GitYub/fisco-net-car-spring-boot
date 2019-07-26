package org.fisco.bcos.business.param;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PlatformRegisterParam {

    private String username;

    private String phoneNumber;

    private String password;

}
