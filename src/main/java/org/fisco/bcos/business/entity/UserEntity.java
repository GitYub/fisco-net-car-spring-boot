package org.fisco.bcos.business.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tb_user")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "private_key")
    private String privateKey;

    @Column
    private String username;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column
    private String password;

    @Column
    private int role;

    @Column
    private String address;

}
