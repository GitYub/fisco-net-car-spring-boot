package org.fisco.bcos.business.entity;

import lombok.*;

import javax.persistence.*;

/**
 * @author ginyu
 * @date 2019-07-25 23:38
 **/
@Entity
@Table(name = "tb_item")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "item_name")
    private String itemName;

    @Column
    private long point;

}
