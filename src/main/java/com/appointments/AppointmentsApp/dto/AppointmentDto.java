package com.appointments.AppointmentsApp.dto;

import com.appointments.AppointmentsApp.entity.Appointment;

import java.time.LocalDateTime;

public class AppointmentDto {
    public Long clientId;
    public Long specialistId;
    public Long serviceId;
    public LocalDateTime startTime;
    public LocalDateTime endTime;
    public Appointment.Status status;
}
