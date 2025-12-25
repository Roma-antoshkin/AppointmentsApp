package com.appointments.AppointmentsApp.repository;

import com.appointments.AppointmentsApp.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Annotation
@Repository
// Interface
public interface ClientRepository extends JpaRepository<Client, Long> {

}
