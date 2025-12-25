package com.appointments.AppointmentsApp.entity;

import com.appointments.AppointmentsApp.searchable.Searchable;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.Duration;

// Annotations
@Getter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Class
public class ServiceE implements Searchable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long serviceId;
    private String name;
    private Duration duration;
    private BigDecimal price;
}
