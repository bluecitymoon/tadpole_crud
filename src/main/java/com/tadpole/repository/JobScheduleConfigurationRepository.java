package com.tadpole.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tadpole.entity.JobScheduleConfiguration;

public interface JobScheduleConfigurationRepository extends JpaRepository<JobScheduleConfiguration, Integer> , JpaSpecificationExecutor<JobScheduleConfiguration>{
}
