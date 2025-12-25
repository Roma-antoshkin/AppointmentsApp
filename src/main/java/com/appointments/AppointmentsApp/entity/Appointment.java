package com.appointments.AppointmentsApp.entity;

import com.appointments.AppointmentsApp.searchable.Searchable;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Objects;

// Annotations
@Getter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Class
public class Appointment implements Searchable {
    public enum Status {
        SCHEDULED("Планируется"),
        COMPLETED("Завершено"),
        CANCELLED("Отменено"),
        NO_SHOW("Неявка");

        private final String displayName;

        Status(String displayName) {
            this.displayName = displayName;
        }

        @Override
        public String toString() {
            return displayName;
        }
    }

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long appointmentId;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "clientId")
    private Client client;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "specialistId")
    private Specialist specialist;

    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    @JoinColumn(name = "serviceId")
    private ServiceE service;

    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
    protected Status status;

    @Override
    protected Object clone() throws CloneNotSupportedException {
        Appointment app = (Appointment) super.clone();
        app.appointmentId = appointmentId;
        app.client = client;
        app.specialist = specialist;
        app.service = service;
        app.startTime = startTime;
        app.endTime = endTime;
        return app;
    }
    @Override
    public boolean equals(Object obj) {
        return obj instanceof Appointment app
                && app.appointmentId.equals(this.appointmentId)
                && app.client.equals(this.client)
                && app.specialist.equals(this.specialist)
                && app.service.equals(this.service)
                && app.startTime.equals(this.startTime)
                && app.endTime.equals(this.endTime);
    }
    @Override
    public String toString() {
        return "appointment:\n Id - " + appointmentId
                + "\nClient:\n" + client.toString()
                + "\nSpecialist:\n" + specialist.toString()
                + "\nDate&Time - " + startTime + " - " + endTime
                + "\nStatus - " + status;
    }
    @Override
    public int hashCode() {
        return Objects.hash(appointmentId, client, specialist, startTime, endTime, status);
    }
}
