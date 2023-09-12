package edu.step.ui;

import edu.step.db.Department;
import edu.step.db.Employee;
import edu.step.db.EmployeeDB;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

// TODO: implement views
public class MainStageController implements Initializable {

    public TableColumn<Employee, String> nameColumn;
    public TableColumn<Employee, String> surnameColumn;
    public TableColumn<Employee, String> emailColumn;
    public TableColumn<Employee, String> birthdateColumn; // TODO: de inlocuit cu alt tip corespunzator
    public TableColumn<Employee, Department> departmentColumn; // TODO: de inlocuit cu alt tip corespunzator

    public TableView<Employee> employeeTable; // TODO: stergere multipla

    private EmployeeDB employeeDB = new EmployeeDB();


    @Override
    public void initialize(URL location, ResourceBundle resources) {

        List<Employee> employees = employeeDB.selectAll();
        ObservableList<Employee> observableArrayList = FXCollections.observableArrayList(employees);
        employeeTable.setItems(observableArrayList);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        surnameColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));
    }

    public void onAdd() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/add.fxml"));
        AnchorPane pane = fxmlLoader.load();
        Scene scene = new Scene(pane);
        Stage addDialog = new Stage();
        addDialog.setScene(scene);
        addDialog.setTitle("Add employee");
        addDialog.initModality(Modality.APPLICATION_MODAL);
        addDialog.showAndWait();
        AddDialogController controller = fxmlLoader.getController();
        Employee emp = controller.getResult();
        if(emp != null) {
            employeeDB.create(emp); // todo: DE TRATAT ERORILE
            employeeTable.getItems().add(emp);
        }

    }

    public void onDelete() {
        // citim care rand a fost selectat
        int index = employeeTable.getSelectionModel().getSelectedIndex();
        // afisam fereastra de confirmare
        if(index != -1) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setHeaderText("Are you sure you want to delete the selected employee?");
            Employee employee = employeeTable.getItems().get(index);
            alert.setContentText(employee.getId() + " " + employee.getName() + " " + employee.getSurname());
            alert.setTitle("Please confirm");
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent()) {
                if(result.get() == ButtonType.OK) {
                    employeeDB.delete( employeeTable.getItems().get(index).getId());
                    employeeTable.getItems().remove(index);
                }
            }
        }
        // daca yes - stergem
        // daca no - nu facem nimic
    }

    public void onEdit() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/edit.fxml"));
        AnchorPane pane = fxmlLoader.load();
        Scene scene = new Scene(pane);
        Stage editDialog = new Stage();
        editDialog.setScene(scene);
        editDialog.setTitle("Edit employee");
        editDialog.initModality(Modality.APPLICATION_MODAL);
        EditDialogController controller = fxmlLoader.getController();
        int index = employeeTable.getSelectionModel().getSelectedIndex();
        // afisam fereastra de confirmare
        if(index != -1) {
            controller.setEditedEmployee(employeeTable.getItems().get(index));
            editDialog.showAndWait();
            Employee emp = controller.getResult();
            if (emp != null) {
                employeeDB.update(emp); // todo: DE TRATAT ERORILE
                employeeTable.refresh();
            }
        }
    }
}
