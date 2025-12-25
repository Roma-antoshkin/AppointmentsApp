package com.appointments.AppointmentsApp.controller.dialogs;

import com.appointments.AppointmentsApp.dto.AppointmentDto;
import com.appointments.AppointmentsApp.entity.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Component
public class AppointmentDialogController {

    @FXML private ComboBox<Client> clientComboBox;
    @FXML private ComboBox<Specialist> specialistComboBox;
    @FXML private ComboBox<ServiceE> serviceComboBox;
    @FXML private ComboBox<Appointment.Status> statusComboBox;

    @FXML private DatePicker datePicker;

    @FXML private Spinner<Integer> startHourSpinner;
    @FXML private Spinner<Integer> startMinuteSpinner;
    @FXML private Spinner<Integer> endHourSpinner;
    @FXML private Spinner<Integer> endMinuteSpinner;

    private Appointment editingAppointment;

    @FXML
    private void initialize() {

        // Заполняем статус
        statusComboBox.setItems(
                FXCollections.observableArrayList(Appointment.Status.values())
        );

        // Часы 0–23
        startHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(9, 18, 9));
        endHourSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(9, 18, 10));

        // Минуты с шагом 15
        startMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 45, 0, 15));
        endMinuteSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 45, 0, 15));

        // Текущая дата по умолчанию
        datePicker.setValue(LocalDate.now());
    }

    /**
     * Наполнение справочников
     */
    public void initData(
            List<Client> clients,
            List<Specialist> specialists,
            List<ServiceE> services
    ) {
        clientComboBox.setItems(FXCollections.observableArrayList(clients));
        specialistComboBox.setItems(FXCollections.observableArrayList(specialists));
        serviceComboBox.setItems(FXCollections.observableArrayList(services));
    }

    /**
     * Режим редактирования
     */
    public void setAppointment(Appointment appointment) {
        this.editingAppointment = appointment;

        clientComboBox.setValue(appointment.getClient());
        specialistComboBox.setValue(appointment.getSpecialist());
        serviceComboBox.setValue(appointment.getService());
        statusComboBox.setValue(appointment.getStatus());

        datePicker.setValue(appointment.getStartTime().toLocalDate());

        // Устанавливаем часы и минуты в спиннерах
        startHourSpinner.getValueFactory().setValue(appointment.getStartTime().getHour());
        startMinuteSpinner.getValueFactory().setValue(appointment.getStartTime().getMinute());

        endHourSpinner.getValueFactory().setValue(appointment.getEndTime().getHour());
        endMinuteSpinner.getValueFactory().setValue(appointment.getEndTime().getMinute());
    }

    public AppointmentDto getDto() {

        validate();

        AppointmentDto dto = new AppointmentDto();
        dto.clientId = clientComboBox.getValue().getClientId();
        dto.specialistId = specialistComboBox.getValue().getSpecialistId();
        dto.serviceId = serviceComboBox.getValue().getServiceId();

        LocalDate date = datePicker.getValue();

        LocalTime startTime = LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue());
        LocalTime endTime   = LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue());

        dto.startTime = LocalDateTime.of(date, startTime);
        dto.endTime   = LocalDateTime.of(date, endTime);

        dto.status = statusComboBox.getValue();

        return dto;
    }

    private void validate() {

        if (clientComboBox.getValue() == null ||
                specialistComboBox.getValue() == null ||
                serviceComboBox.getValue() == null ||
                statusComboBox.getValue() == null ||
                datePicker.getValue() == null ||
                startHourSpinner.getValue() == null ||
                startMinuteSpinner.getValue() == null ||
                endHourSpinner.getValue() == null ||
                endMinuteSpinner.getValue() == null) {

            throw new IllegalStateException("Все поля должны быть заполнены");
        }

        LocalTime startTime = LocalTime.of(startHourSpinner.getValue(), startMinuteSpinner.getValue());
        LocalTime endTime   = LocalTime.of(endHourSpinner.getValue(), endMinuteSpinner.getValue());

        if (!startTime.isBefore(endTime)) {
            throw new IllegalStateException("Время окончания должно быть позже начала");
        }
    }
}
