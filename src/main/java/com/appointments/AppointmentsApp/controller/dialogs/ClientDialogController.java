package com.appointments.AppointmentsApp.controller.dialogs;

import com.appointments.AppointmentsApp.entity.Client;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class ClientDialogController {

    @FXML private TextField nameField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;

    private Client editingClient;

    public void setClient(Client client) {
        this.editingClient = client;

        if (client != null) {
            nameField.setText(client.getName());
            phoneField.setText(client.getPhone());
            emailField.setText(client.getEmail());
        }
    }

    /**
     * Получение объекта Client (вызывается при OK)
     */
    public Client getClient() {

        validate();

        Client client = editingClient != null ? editingClient : new Client();
        client.setName(nameField.getText().trim());
        client.setPhone(phoneField.getText().trim());
        client.setEmail(emailField.getText().trim());

        return client;
    }

    private void validate() {
        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            throw new IllegalStateException("Имя не может быть пустым");
        }

        if (phoneField.getText() == null || phoneField.getText().trim().isEmpty()) {
            throw new IllegalStateException("Телефон не может быть пустым");
        }

        if (emailField.getText() == null || emailField.getText().trim().isEmpty()) {
            throw new IllegalStateException("Email не может быть пустым");
        }

    }
}
