package com.serzh.demo.data;

import lombok.Builder;
import lombok.Data;

/**
 * @author sergii.zagryvyi on 24.06.2017
 */
@Data
@Builder
public class Line {

    String line;
    Integer count;
}
