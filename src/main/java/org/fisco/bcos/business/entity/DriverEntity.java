package org.fisco.bcos.business.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author ginyu
 * @date 2019-07-25 23:07
 **/
@Entity
@Table(name = "tb_driver")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class DriverEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id")
    private long userId;

    @Column
    private int platform;

    @Column
    private int star;

    @Column
    private int miles;

}
