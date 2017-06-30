package com.serzh.demo.data;

import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * @author sergii.zagryvyi on 29.06.2017
 */

@Data
@Entity
@Builder
public class Result extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ElementCollection
    @MapKeyColumn(name = "resultMapKey")
    @Column(name = "resultMapValue")
    private Map<String, Integer> resultMap = new HashMap<>();

}
