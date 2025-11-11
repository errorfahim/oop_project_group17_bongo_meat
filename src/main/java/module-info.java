module com.group17.oop_project_group17_bongo_meat {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.group17.oop_project_group17_bongo_meat to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat;
}