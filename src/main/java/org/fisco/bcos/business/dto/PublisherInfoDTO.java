package org.fisco.bcos.business.dto;

import lombok.*;

/**
 * @author ginyu
 * @date 2019-07-26 22:31
 **/
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class PublisherInfoDTO {

    private String contractAddress;

    private String address;

    private String privateKey;

    private String publicKey;

}
