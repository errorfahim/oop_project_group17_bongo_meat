module com.group17.oop_project_group17_bongo_meat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.group17.oop_project_group17_bongo_meat to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat;

    opens com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

    opens com.group17.oop_project_group17_bongo_meat.fahim.Admin to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.fahim.Admin;

    opens com.group17.oop_project_group17_bongo_meat.shaika.Customer to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.shaika.Customer;

    opens com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff;


}