package com.appointments.AppointmentsApp.controller;

import com.appointments.AppointmentsApp.controller.dialogs.*;
import com.appointments.AppointmentsApp.entity.*;
import com.appointments.AppointmentsApp.dto.*;
import com.appointments.AppointmentsApp.mapper.AppointmentMapper;
import com.appointments.AppointmentsApp.service.AppointmentService;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXMLLoader;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Predicate;

@Component
public class MainController {

    private final AppointmentService service;
    private final ApplicationContext context;
    private final AppointmentMapper appointmentMapper;

    public MainController(AppointmentService service, ApplicationContext context, AppointmentMapper appointmentMapper) {
        this.service = service;
        this.context = context;
        this.appointmentMapper = appointmentMapper;
    }

    @FXML private TableView<Appointment> appointmentsTable;
    @FXML private TableView<Client> clientsTable;
    @FXML private TableView<ServiceE> servicesTable;
    @FXML private TableView<Specialist> specialistsTable;

    @FXML private TableColumn<Appointment, Long> appointmentIdCol;
    @FXML private TableColumn<Appointment, String> appointmentStartTimeCol;
    @FXML private TableColumn<Appointment, String> appointmentEndTimeCol;
    @FXML private TableColumn<Appointment, String> appointmentClientCol;
    @FXML private TableColumn<Appointment, String> appointmentServiceCol;
    @FXML private TableColumn<Appointment, String> appointmentSpecialistCol;

    @FXML private TableColumn<Appointment, Appointment.Status> appointmentStatusCol;

    @FXML private ToggleGroup filterStatusGroup;
    ObjectProperty<Predicate<Appointment>> statusAppointmentsFilter;
    ObjectProperty<Predicate<Appointment>> searchAppointmentsFilter;
    ObservableList<Appointment> appointmentObservableList;
    FilteredList<Appointment> appointmentFilteredList;

    @FXML private TableColumn<Client, Long> clientIdCol;
    @FXML private TableColumn<Client, String> clientNameCol;
    @FXML private TableColumn<Client, String> clientPhoneCol;
    @FXML private TableColumn<Client, String> clientEmailCol;

    ObjectProperty<Predicate<Client>> searchClientsFilter;
    ObservableList<Client> clientObservableList;
    FilteredList<Client> clientFilteredList;

    @FXML private TableColumn<ServiceE, Long> serviceIdCol;
    @FXML private TableColumn<ServiceE, String> serviceNameCol;
    @FXML private TableColumn<ServiceE, Double> servicePriceCol;
    @FXML private TableColumn<ServiceE, Integer> serviceDurationCol;

    ObjectProperty<Predicate<ServiceE>> searchServicesFilter;
    ObservableList<ServiceE> serviceObservableList;
    FilteredList<ServiceE> serviceFilteredList;

    @FXML private TableColumn<Specialist, Long> specialistIdCol;
    @FXML private TableColumn<Specialist, String> specialistNameCol;
    @FXML private TableColumn<Specialist, String> specialistSpecCol;
    @FXML private TableColumn<Specialist, Double> specialistRatingCol;

    ObjectProperty<Predicate<Specialist>> searchSpecialistsFilter;
    ObservableList<Specialist> specialistObservableList;
    FilteredList<Specialist> specialistFilteredList;

    @FXML private TextField matchesAppointmentsField;
    @FXML private TextField matchesClientsField;
    @FXML private TextField matchesServicesField;
    @FXML private TextField matchesSpecialistsField;

    @FXML
    public void initialize() {
        initColumns();
        loadAllData();
    }

    private void initColumns() {

        appointmentsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        clientsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        servicesTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        specialistsTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);

        // Appointments
        appointmentIdCol.setCellValueFactory(new PropertyValueFactory<>("appointmentId"));
        appointmentStartTimeCol.setCellValueFactory(
                c -> new SimpleStringProperty(
                        c.getValue().getStartTime().format(
                                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                        )
                )
        );
        appointmentEndTimeCol.setCellValueFactory(
                c -> new SimpleStringProperty(
                        c.getValue().getEndTime().format(
                                DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm")
                        )
                )
        );
        appointmentClientCol.setCellValueFactory(
                c -> javafx.beans.binding.Bindings.createStringBinding(
                        () -> c.getValue().getClient().getName()
                )
        );
        appointmentServiceCol.setCellValueFactory(
                c -> javafx.beans.binding.Bindings.createStringBinding(
                        () -> c.getValue().getService().getName()
                )
        );
        appointmentSpecialistCol.setCellValueFactory(
                c -> javafx.beans.binding.Bindings.createStringBinding(
                        () -> c.getValue().getSpecialist().getName()
                )
        );
        appointmentStatusCol.setCellValueFactory(new PropertyValueFactory<>("status"));

        appointmentObservableList = FXCollections.observableArrayList();
        appointmentFilteredList = new FilteredList<>(appointmentObservableList);

        searchAppointmentsFilter = new SimpleObjectProperty<>(item -> true);
        statusAppointmentsFilter = new SimpleObjectProperty<>(item -> true);

        appointmentFilteredList.predicateProperty().bind(
                Bindings.createObjectBinding(
                        () -> statusAppointmentsFilter.get().and(searchAppointmentsFilter.get()),
                        statusAppointmentsFilter, searchAppointmentsFilter
                )
        );

        matchesAppointmentsField.textProperty().addListener((obs, oldText, newText) -> {
            searchAppointmentsFilter.setValue(appointment -> appointment.matches(newText));
        });

        filterStatusGroup.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle != null) {
                String userData = (String) newToggle.getUserData();
                switch (userData) {
                    case "ALL" -> searchAppointmentsFilter.setValue(appointment -> true);
                    case "SCHEDULED" -> searchAppointmentsFilter.setValue(appointment -> appointment.getStatus() == Appointment.Status.SCHEDULED);
                    case "COMPLETED" -> searchAppointmentsFilter.setValue(appointment -> appointment.getStatus() == Appointment.Status.COMPLETED);
                    case "CANCELLED" -> searchAppointmentsFilter.setValue(appointment -> appointment.getStatus() == Appointment.Status.CANCELLED);
                    case "NO_SHOW" -> searchAppointmentsFilter.setValue(appointment -> appointment.getStatus() == Appointment.Status.NO_SHOW);
                }
            }
        });

        // Clients
        clientIdCol.setCellValueFactory(new PropertyValueFactory<>("clientId"));
        clientNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        clientPhoneCol.setCellValueFactory(new PropertyValueFactory<>("phone"));
        clientEmailCol.setCellValueFactory(new PropertyValueFactory<>("email"));

        clientObservableList = FXCollections.observableArrayList();
        clientFilteredList = new FilteredList<>(clientObservableList);

        searchClientsFilter = new SimpleObjectProperty<>(item -> true);

        clientFilteredList.predicateProperty().bind(
                Bindings.createObjectBinding(
                        () -> searchClientsFilter.get(),
                        searchClientsFilter
                )
        );

        matchesClientsField.textProperty().addListener((obs, oldText, newText) -> {
            searchClientsFilter.setValue(client -> client.matches(newText));
        });

        // Services
        serviceIdCol.setCellValueFactory(new PropertyValueFactory<>("serviceId"));
        serviceNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        servicePriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        serviceDurationCol.setCellValueFactory(new PropertyValueFactory<>("duration"));

        serviceObservableList = FXCollections.observableArrayList();
        serviceFilteredList = new FilteredList<>(serviceObservableList);

        searchServicesFilter = new SimpleObjectProperty<>(item -> true);

        serviceFilteredList.predicateProperty().bind(
                Bindings.createObjectBinding(
                        () -> searchServicesFilter.get(),
                        searchServicesFilter
                )
        );

        matchesServicesField.textProperty().addListener((obs, oldText, newText) -> {
            searchServicesFilter.setValue(service -> service.matches(newText));
        });

        // Specialists
        specialistIdCol.setCellValueFactory(new PropertyValueFactory<>("specialistId"));
        specialistNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        specialistSpecCol.setCellValueFactory(new PropertyValueFactory<>("specialization"));
        specialistRatingCol.setCellValueFactory(new PropertyValueFactory<>("rating"));

        specialistObservableList = FXCollections.observableArrayList();
        specialistFilteredList = new FilteredList<>(specialistObservableList);

        searchSpecialistsFilter = new SimpleObjectProperty<>(item -> true);

        specialistFilteredList.predicateProperty().bind(
                Bindings.createObjectBinding(
                        () -> searchSpecialistsFilter.get(),
                        searchSpecialistsFilter
                )
        );

        matchesSpecialistsField.textProperty().addListener((obs, oldText, newText) -> {
            searchSpecialistsFilter.setValue(specialist -> specialist.matches(newText));
        });
    }

    /* ================= LOAD DATA ================= */

    private void loadAllData() {
        loadAppointments();
        loadClients();
        loadServices();
        loadSpecialists();
    }

    private void loadAppointments() {
        runAsync(
                service::fetchAppointmentList,
                list -> {
                    appointmentObservableList.clear();
                    appointmentObservableList.setAll(list);
                }
        );
        appointmentsTable.setItems(appointmentFilteredList);
    }

    private void loadClients() {
        runAsync(
                service::fetchClientList,
                list -> {
                    clientObservableList.clear();
                    clientObservableList.setAll(list);
                }
        );
        clientsTable.setItems(clientFilteredList);
    }

    private void loadServices() {
        runAsync(
                service::fetchServiceList,
                list -> {
                    serviceObservableList.clear();
                    serviceObservableList.setAll(list);
                }
        );
        servicesTable.setItems(serviceFilteredList);
    }

    private void loadSpecialists() {
        runAsync(
                service::fetchSpecialistList,
                list -> {
                    specialistObservableList.clear();
                    specialistObservableList.setAll(list);
                }
        );
        specialistsTable.setItems(specialistFilteredList);
    }

    /* ================= CRUD BUTTONS ================= */

    // -------- Clients --------

    @FXML
    private void onAddClient() {
        openClientDialog(null);
    }

    @FXML
    private void onEditClient() {
        Client client = getSelected(clientsTable);
        if (client != null) {
            openClientDialog(client);
        }
    }

    @FXML
    private void onDeleteClient() {
        Client client = getSelected(clientsTable);
        if (client != null && confirmDelete()) {
            service.deleteClientById(client.getClientId());
            loadClients();
        }
    }

    // -------- Services --------

    @FXML
    private void onAddService() {
        openServiceDialog(null);
    }

    @FXML
    private void onEditService() {
        ServiceE selected = getSelected(servicesTable);
        if (selected != null) {
            openServiceDialog(selected);
        }
    }

    @FXML
    private void onDeleteService() {
        ServiceE selected = getSelected(servicesTable);
        if (selected != null && confirmDelete()) {
            service.deleteServiceById(selected.getServiceId());
            loadServices();
        }
    }

    // -------- Specialists --------

    @FXML
    private void onAddSpecialist() {
        openSpecialistDialog(null);
    }

    @FXML
    private void onEditSpecialist() {
        Specialist specialist = getSelected(specialistsTable);
        if (specialist != null) {
            openSpecialistDialog(specialist);
        }
    }

    @FXML
    private void onDeleteSpecialist() {
        Specialist specialist = getSelected(specialistsTable);
        if (specialist != null && confirmDelete()) {
            service.deleteSpecialistById(specialist.getSpecialistId());
            loadSpecialists();
        }
    }

    // -------- Appointments --------

    @FXML
    private void onAddAppointment() {
        openAppointmentDialog(null);
    }

    @FXML
    private void onEditAppointment() {
        Appointment appointment = getSelected(appointmentsTable);
        if (appointment != null) {
            openAppointmentDialog(appointment);
        }
    }

    @FXML
    private void onDeleteAppointment() {
        Appointment appointment = getSelected(appointmentsTable);
        if (appointment != null && confirmDelete()) {
            service.deleteAppointmentById(appointment.getAppointmentId());
            loadAppointments();
        }
    }

    /* ================= DOUBLE CLICK ================= */

    @FXML
    private void onClientDoubleClick(MouseEvent e) {
        if (e.getClickCount() == 2) {
            Client client = getSelected(clientsTable);
            if (client != null) {
                showInfo(client);
            }
        }
    }

    @FXML
    private void onServiceDoubleClick(MouseEvent e) {
        if (e.getClickCount() == 2) {
            ServiceE serviceE = getSelected(servicesTable);
            if (serviceE != null) {
                showInfo(serviceE);
            }
        }
    }

    @FXML
    private void onSpecialistDoubleClick(MouseEvent e) {
        if (e.getClickCount() == 2) {
            Specialist specialist = getSelected(specialistsTable);
            if (specialist != null) {
                showInfo(specialist);
            }
        }
    }

    @FXML
    private void onAppointmentDoubleClick(MouseEvent e) {
        if (e.getClickCount() == 2) {
            Appointment appointment = getSelected(appointmentsTable);
            if (appointment != null) {
                showInfo(appointment);
            }
        }
    }

    // Вспомогательные функции

    private <T> T getSelected(TableView<T> table) {
        return table.getSelectionModel().getSelectedItem();
    }

    private boolean confirmDelete() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                "Удалить выбранную запись?",
                ButtonType.YES, ButtonType.NO);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    private void showInfo(Object obj) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Детали");
        alert.setHeaderText(null);
        alert.setContentText(obj.toString());
        alert.showAndWait();
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Ошибка");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private <T> void runAsync(TaskSupplier<List<T>> supplier,
                              java.util.function.Consumer<List<T>> onSuccess) {

        Task<List<T>> task = new Task<>() {
            @Override
            protected List<T> call() {
                return supplier.get();
            }
        };

        task.setOnSucceeded(e -> onSuccess.accept(task.getValue()));
        new Thread(task).start();
    }

    @FunctionalInterface
    private interface TaskSupplier<T> {
        T get();
    }

    // Открытие диалоговых окон
    private void openAppointmentDialog(Appointment appointment) {

        try {
            Dialog<AppointmentDto> dialog = new Dialog<>();

            URL url = getClass().getResource(
                    "/com/appointments/AppointmentsApp/view/dialogs/AppointmentDialog.fxml"
            );
            System.out.println("FXML URL = " + url);

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/appointments/AppointmentsApp/view/dialogs/AppointmentDialog.fxml")
            );
            loader.setControllerFactory(context::getBean);

            dialog.setDialogPane(loader.load());
            dialog.setTitle(
                    appointment == null ? "Новая запись" : "Редактирование записи"
            );

            AppointmentDialogController controller = loader.getController();

            // наполняем справочники
            controller.initData(
                    service.fetchClientList(),
                    service.fetchSpecialistList(),
                    service.fetchServiceList()
            );

            // если редактирование
            if (appointment != null) {
                controller.setAppointment(appointment);
            }

            dialog.setResultConverter(button -> {
                if (button.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    return controller.getDto();
                }
                return null;
            });

            dialog.showAndWait().ifPresent(dto -> {
                if (appointment == null) {
                    service.saveAppointment(dto);
                } else {
                    service.updateAppointment(
                            dto, appointment.getAppointmentId()
                    );
                }
                loadAppointments();
            });

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void openClientDialog(Client client) {
        try {
            Dialog<Client> dialog = new Dialog<>();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/appointments/AppointmentsApp/view/dialogs/ClientDialog.fxml")
            );
            loader.setControllerFactory(context::getBean);

            dialog.setDialogPane(loader.load());
            dialog.setTitle(client == null ? "Новый клиент" : "Редактирование клиента");

            ClientDialogController controller = loader.getController();

            // если редактирование
            if (client != null) {
                controller.setClient(client);
            }

            dialog.setResultConverter(button -> {
                if (button.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    return controller.getClient();
                }
                return null;
            });

            dialog.showAndWait().ifPresent(c -> {
                if (client == null) {
                    service.saveClient(c);
                } else {
                    service.updateClient(c, client.getClientId());
                }
                loadClients();
            });

        } catch (Exception e) {
            showError(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private void openSpecialistDialog(Specialist specialist) {
        try {
            Dialog<Specialist> dialog = new Dialog<>();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/appointments/AppointmentsApp/view/dialogs/SpecialistDialog.fxml")
            );
            loader.setControllerFactory(context::getBean);

            dialog.setDialogPane(loader.load());
            dialog.setTitle(specialist == null ? "Новый специалист" : "Редактирование специалиста");

            SpecialistDialogController controller = loader.getController();

            if (specialist != null) {
                controller.setSpecialist(specialist);
            }

            dialog.setResultConverter(button -> {
                if (button.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    return controller.getSpecialist();
                }
                return null;
            });

            dialog.showAndWait().ifPresent(s -> {
                if (specialist == null) {
                    service.saveSpecialist(s);
                } else {
                    service.updateSpecialist(s, specialist.getSpecialistId());
                }
                loadSpecialists();
            });

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void openServiceDialog(ServiceE serviceEntity) {
        try {
            Dialog<ServiceE> dialog = new Dialog<>();

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/appointments/AppointmentsApp/view/dialogs/ServiceDialog.fxml")
            );
            loader.setControllerFactory(context::getBean);

            dialog.setDialogPane(loader.load());
            dialog.setTitle(serviceEntity == null ? "Новая услуга" : "Редактирование услуги");

            ServiceDialogController controller = loader.getController();
            controller.setService(serviceEntity); // заполняем поля

            dialog.setResultConverter(button -> {
                if (button.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    return controller.getService(); // получаем изменённую сущность
                }
                return null;
            });

            dialog.showAndWait().ifPresent(entity -> {
                if (serviceEntity == null) {
                    service.saveService(entity);
                } else {
                    service.updateService(entity, serviceEntity.getServiceId());
                }
                loadServices(); // обновляем таблицу
            });

        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
}
