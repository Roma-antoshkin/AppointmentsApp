package com.appointments.AppointmentsApp.repository;

import com.appointments.AppointmentsApp.entity.Specialist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Annotation
@Repository
// Interface
public interface SpecialistRepository extends JpaRepository<Specialist, Long> {

}
