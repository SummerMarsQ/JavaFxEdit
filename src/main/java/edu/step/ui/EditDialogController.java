package edu.step.ui;

import edu.step.db.Department;
import edu.step.db.DepartmentDB;
import edu.step.db.Employee;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
public class EditDialogController implements Initializable {

    public TextField nameTextField;

    public TextField surnameTextField;

    public TextField emailTextField;

    public DatePicker birthdateDatePicker;

    public ChoiceBox<Department> departmentChoiceBox;

    private Employee editedEmployee;

    private Employee result;

    public Employee getResult() {
        return result;
    }

    private ObservableList<Department> departments;

    public EditDialogController() {
        DepartmentDB db= new DepartmentDB();
        List<Department> selectAll = db.selectAll();
        departments = FXCollections.observableArrayList(selectAll);
    }

    public void setEditedEmployee(Employee editedEmployee) {
        this.editedEmployee = editedEmployee;
        this.nameTextField.setText(editedEmployee.getName());
        this.surnameTextField.setText(editedEmployee.getSurname());
        System.out.println("Departamentul din angajat: "+editedEmployee.getDepartment());
        System.out.println("Departamentele incarcate: " + departments.size());
         departmentChoiceBox.setValue(editedEmployee.getDepartment());
//        for(Department d: departments){
//            if(d.getId() == editedEmployee.getDepartment().getId()){
//                departmentChoiceBox.setValue(d);
//            }
//        }
    }

    public void onSave(ActionEvent event) {
        // De verificat daca edited employee nu s-a schimbat in baza de date?
        // Daca e diferit - de afisat o alerta de confirmare
            // Daca ok - suprascriem datele
            // Daca cancel - incarcam datele noi si afisam in campuri
        // Daca ii acelasi - salvam informatia in baza de date


        if(!nameTextField.getText().isEmpty() || !surnameTextField.getText().isEmpty()) {
            // TODO: de adaugat birthdate aici
            result = new Employee(nameTextField.getText(), surnameTextField.getText(), emailTextField.getText(), departmentChoiceBox.getValue());
        }
        Node button = (Node)event.getSource();
        Stage stage = (Stage) button.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        departmentChoiceBox.setItems(departments);
    }
}
