package com.appointments.AppointmentsApp.entity;

import com.appointments.AppointmentsApp.searchable.Searchable;
import lombok.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;


// Annotations
@Getter
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Class
public class Specialist implements Searchable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long specialistId;
    private String name;
    private String specialization;
    private double rating;
}
