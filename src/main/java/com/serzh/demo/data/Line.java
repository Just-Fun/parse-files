package com.serzh.demo.data;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;

/**
 * @author sergii.zagryvyi on 24.06.2017
 */
@Data
@Builder
@Entity
public class Line {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", updatable = false, nullable = false)
    private Long id;

    @Column
    String line;

    @Column
    Integer count;
}
