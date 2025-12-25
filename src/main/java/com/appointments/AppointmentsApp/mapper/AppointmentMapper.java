package com.appointments.AppointmentsApp.mapper;

import com.appointments.AppointmentsApp.dto.AppointmentDto;
import com.appointments.AppointmentsApp.entity.Appointment;
import com.appointments.AppointmentsApp.repository.ClientRepository;
import com.appointments.AppointmentsApp.repository.ServiceRepository;
import com.appointments.AppointmentsApp.repository.SpecialistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    @Autowired
    ClientRepository clientRepository;
    @Autowired
    SpecialistRepository specialistRepository;
    @Autowired
    ServiceRepository serviceRepository;

//    public AppointmentMapper(
//            ClientRepository clientRepository,
//            SpecialistRepository specialistRepository,
//            ServiceRepository serviceRepository
//    ) {
//        this.clientRepository = clientRepository;
//        this.specialistRepository = specialistRepository;
//        this.serviceRepository = serviceRepository;
//    }

    public Appointment toEntity(AppointmentDto dto) {
        Appointment appointment = new Appointment();
        map(dto, appointment);
        return appointment;
    }

    public void map(AppointmentDto dto, Appointment appointment) {

        appointment.setClient(
                clientRepository.findById(dto.clientId)
                        .orElseThrow(() -> new IllegalArgumentException("Client not found"))
        );

        appointment.setSpecialist(
                specialistRepository.findById(dto.specialistId)
                        .orElseThrow(() -> new IllegalArgumentException("Specialist not found"))
        );

        appointment.setService(
                serviceRepository.findById(dto.serviceId)
                        .orElseThrow(() -> new IllegalArgumentException("Service not found"))
        );

        appointment.setStartTime(dto.startTime);
        appointment.setEndTime(dto.endTime);
        appointment.setStatus(dto.status);
    }
}
