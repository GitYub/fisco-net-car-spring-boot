package org.fisco.bcos.business.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author ginyu
 * @date 2019-07-27 0:18
 **/
@Entity
@Table(name = "tb_license")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class LicenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    private String description;

}
