package com.serzh.demo.repositories;

import com.serzh.demo.data.Result;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author sergii.zagryvyi on 29.06.2017
 */
public interface ResultRepository extends JpaRepository<Result, Long>{
}
