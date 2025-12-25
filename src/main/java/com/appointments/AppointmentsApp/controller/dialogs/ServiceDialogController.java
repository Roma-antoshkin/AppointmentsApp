package com.appointments.AppointmentsApp.controller.dialogs;

import com.appointments.AppointmentsApp.entity.ServiceE;
import javafx.fxml.FXML;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Duration;

@Component
public class ServiceDialogController {

    @FXML private TextField nameField;
    @FXML private Spinner<Integer> durationSpinner;
    @FXML private TextField priceField;

    private ServiceE editingService;

    @FXML
    private void initialize() {
        durationSpinner.setValueFactory(
                new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 600, 60)
        );
    }

    public void setService(ServiceE service) {
        this.editingService = service;

        if (service != null) {
            nameField.setText(service.getName());
            durationSpinner.getValueFactory().setValue((int) service.getDuration().toMinutes());
            priceField.setText(service.getPrice().toString());
        }
    }

    public ServiceE getService() {
        validate();

        ServiceE service = editingService != null ? editingService : new ServiceE();
        service.setName(nameField.getText().trim());
        service.setDuration(Duration.ofMinutes(durationSpinner.getValue()));
        service.setPrice(new BigDecimal(priceField.getText().trim()));

        return service;
    }

    private void validate() {
        if (nameField.getText().trim().isEmpty()) {
            throw new IllegalStateException("Название не может быть пустым");
        }
        if (priceField.getText().trim().isEmpty()) {
            throw new IllegalStateException("Цена не может быть пустой");
        }
    }
}
