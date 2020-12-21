package com.techshard.batch;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IVoltageRepository extends JpaRepository<StudentDTO, Long> {

}
