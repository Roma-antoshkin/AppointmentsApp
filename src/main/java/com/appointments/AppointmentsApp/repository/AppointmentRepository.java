package com.appointments.AppointmentsApp.repository;

import com.appointments.AppointmentsApp.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Annotation
@Repository
// Interface
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {

}
