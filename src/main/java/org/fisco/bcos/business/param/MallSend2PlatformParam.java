package org.fisco.bcos.business.param;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class MallSend2PlatformParam {

    private long userId;
    private int point;

}
