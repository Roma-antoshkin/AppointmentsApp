package com.appointments.AppointmentsApp.service;

import com.appointments.AppointmentsApp.dto.AppointmentDto;
import com.appointments.AppointmentsApp.entity.*;
import com.appointments.AppointmentsApp.mapper.AppointmentMapper;
import com.appointments.AppointmentsApp.repository.AppointmentRepository;
import com.appointments.AppointmentsApp.repository.ClientRepository;
import com.appointments.AppointmentsApp.repository.ServiceRepository;
import com.appointments.AppointmentsApp.repository.SpecialistRepository;
// Importing required classes
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// Annotation
@Service
// Class
public class AppointmentServiceImpl implements AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    ClientRepository clientRepository;
    @Autowired
    ServiceRepository serviceRepository;
    @Autowired
    SpecialistRepository specialistRepository;
    @Autowired
    AppointmentMapper appointmentMapper;

    Appointment getAppointmentByDto(AppointmentDto appointmentDto) {
//        return new Appointment(
//                null,
//                clientRepository.getReferenceById(appointmentDto.clientId),
//                specialistRepository.getReferenceById(appointmentDto.specialistId),
//                serviceRepository.getReferenceById(appointmentDto.serviceId),
//                appointmentDto.startTime,
//                appointmentDto.endTime,
//                appointmentDto.status
//        );
        return appointmentMapper.toEntity(appointmentDto);
    }

    // Save operation
    @Override
    public Appointment saveAppointment(AppointmentDto appointmentDto) {
        return appointmentRepository.save(getAppointmentByDto(appointmentDto));
    }

    // Read operation
    @Override
    public List<Appointment> fetchAppointmentList() {
        return (List<Appointment>) appointmentRepository.findAll();
    }

    // Update operation
    @Override
    public Appointment updateAppointment(AppointmentDto appointmentDto, Long appointmentId) {
        Appointment appDB = getAppointmentByDto(appointmentDto);
        appDB.setAppointmentId(appointmentId);

        return appointmentRepository.save(appDB);
    }

    // Delete operation
    @Override
    public void deleteAppointmentById(Long appointmentId) {
        appointmentRepository.deleteById(appointmentId);
    }

    // Create client
    @Override
    public Client saveClient(Client client) {
        return clientRepository.save(client);
    }

    // Read clients
    public List<Client> fetchClientList() {
        return clientRepository.findAll();
    }

    // Read client
    @Override
    public Client fetchClientById(Long clientId) {
        return clientRepository.getReferenceById(clientId);
    }

    // Update client
    @Override
    public Client updateClient(Client client, Long clientId) {
        Client clientDB = clientRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Клиент не найден"));

        clientDB.setName(client.getName());
        clientDB.setPhone(client.getPhone());
        clientDB.setEmail(client.getEmail());

        return clientRepository.save(clientDB);
    }

    // Delete client
    @Override
    public void deleteClientById(Long clientId) {
        clientRepository.deleteById(clientId);
    }

    // CREATE
    @Override
    public ServiceE saveService(ServiceE service) {
        return serviceRepository.save(service);
    }

    // READ ALL
    @Override
    public List<ServiceE> fetchServiceList() {
        return serviceRepository.findAll();
    }

    // READ ONE
    @Override
    public ServiceE fetchServiceById(Long serviceId) {
        return serviceRepository.getReferenceById(serviceId);
    }

    // UPDATE
    @Override
    public ServiceE updateService(ServiceE service, Long serviceId) {
        ServiceE serviceDB = serviceRepository.findById(serviceId)
                .orElseThrow(() -> new RuntimeException("Сервис не найден"));

        serviceDB.setName(service.getName());
        serviceDB.setDuration(service.getDuration());
        serviceDB.setPrice(service.getPrice());

        return serviceRepository.save(serviceDB);
    }

    // DELETE
    @Override
    public void deleteServiceById(Long serviceId) {
        serviceRepository.deleteById(serviceId);
    }

    // CREATE
    public Specialist saveSpecialist(Specialist specialist) {
        return specialistRepository.save(specialist);
    }

    // READ ALL
    public List<Specialist> fetchSpecialistList() {
        return specialistRepository.findAll();
    }

    // READ ONE
    public Specialist fetchSpecialistById(Long specialistId) {
        return specialistRepository.getReferenceById(specialistId);
    }

    // UPDATE
    public Specialist updateSpecialist(Specialist specialist, Long specialistId) {
        Specialist specialistDB = specialistRepository.findById(specialistId)
                .orElseThrow(() -> new RuntimeException("Специалист не найден"));

        specialistDB.setName(specialist.getName());
        specialistDB.setSpecialization(specialist.getSpecialization());
        specialistDB.setRating(specialist.getRating());

        return specialistRepository.save(specialistDB);
    }

    // DELETE
    public void deleteSpecialistById(Long specialistId) {
        specialistRepository.deleteById(specialistId);
    }
}
