package com.appointments.AppointmentsApp.repository;

import com.appointments.AppointmentsApp.entity.ServiceE;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Annotation
@Repository
// Interface
public interface ServiceRepository extends JpaRepository<ServiceE, Long> {

}
