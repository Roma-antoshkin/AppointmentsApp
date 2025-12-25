package com.appointments.AppointmentsApp.service;

import com.appointments.AppointmentsApp.dto.AppointmentDto;
import com.appointments.AppointmentsApp.entity.Appointment;
import com.appointments.AppointmentsApp.entity.Client;
import com.appointments.AppointmentsApp.entity.ServiceE;
import com.appointments.AppointmentsApp.entity.Specialist;
// Importing required classes
import java.util.List;

// Interface
public interface AppointmentService {
    // Save operation
    Appointment saveAppointment(AppointmentDto appointmentDto);
    // Read operation
    List<Appointment> fetchAppointmentList();
    // Update operation
    Appointment updateAppointment(AppointmentDto appointmentDto, Long appointmentId);

    // Delete operation
    void deleteAppointmentById(Long appointmentId);

    Client saveClient(Client client);

    List<Client> fetchClientList();

    Client fetchClientById(Long clientId);

    Client updateClient(Client client, Long clientId);

    void deleteClientById(Long clientId);

    ServiceE saveService(ServiceE service);

    List<ServiceE> fetchServiceList();

    ServiceE fetchServiceById(Long serviceId);

    ServiceE updateService(ServiceE service, Long serviceId);

    void deleteServiceById(Long serviceId);

    Specialist saveSpecialist(Specialist specialist);

    List<Specialist> fetchSpecialistList();

    Specialist fetchSpecialistById(Long specialistId);

    Specialist updateSpecialist(Specialist specialist, Long specialistId);

    void deleteSpecialistById(Long specialistId);
}
