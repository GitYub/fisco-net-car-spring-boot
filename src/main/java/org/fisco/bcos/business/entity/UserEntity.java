package org.fisco.bcos.business.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "tm_tutor_user")
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

    @Column
    private String username;

    @Column
    private int role;

    @Column
    private String publicKey;

    @Column
    private String privateKey;

}
