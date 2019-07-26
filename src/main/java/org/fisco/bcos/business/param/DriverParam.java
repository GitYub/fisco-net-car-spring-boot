package org.fisco.bcos.business.param;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DriverParam {

    long driverId;

    int miles;

    int isSafeDrive;

}
