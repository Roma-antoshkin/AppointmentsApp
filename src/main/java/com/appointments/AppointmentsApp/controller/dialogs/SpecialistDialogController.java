package com.appointments.AppointmentsApp.controller.dialogs;

import com.appointments.AppointmentsApp.entity.Specialist;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.stereotype.Component;

@Component
public class SpecialistDialogController {

    @FXML private TextField nameField;
    @FXML private TextField specializationField;
    @FXML private TextField ratingField;

    private Specialist editingSpecialist;

    /**
     * Инициализация диалога редактирования
     */
    public void setSpecialist(Specialist specialist) {
        this.editingSpecialist = specialist;

        if (specialist != null) {
            nameField.setText(specialist.getName());
            specializationField.setText(specialist.getSpecialization());
            ratingField.setText(String.valueOf(specialist.getRating()));
        }
    }

    /**
     * Получение объекта Specialist (вызывается при OK)
     */
    public Specialist getSpecialist() {
        validate();

        Specialist specialist = editingSpecialist != null ? editingSpecialist : new Specialist();
        specialist.setName(nameField.getText().trim());
        specialist.setSpecialization(specializationField.getText().trim());
        specialist.setRating(Double.parseDouble(ratingField.getText().trim()));

        return specialist;
    }

    /**
     * Проверка корректности введённых данных
     */
    private void validate() {
        if (nameField.getText() == null || nameField.getText().trim().isEmpty()) {
            throw new IllegalStateException("Имя не может быть пустым");
        }

        if (specializationField.getText() == null || specializationField.getText().trim().isEmpty()) {
            throw new IllegalStateException("Специализация не может быть пустой");
        }

        if (ratingField.getText() == null || ratingField.getText().trim().isEmpty()) {
            throw new IllegalStateException("Рейтинг не может быть пустым");
        }

        try {
            double rating = Double.parseDouble(ratingField.getText().trim());
            if (rating < 0 || rating > 5) {
                throw new IllegalStateException("Рейтинг должен быть от 0 до 5");
            }
        } catch (NumberFormatException e) {
            throw new IllegalStateException("Рейтинг должен быть числом");
        }
    }
}
