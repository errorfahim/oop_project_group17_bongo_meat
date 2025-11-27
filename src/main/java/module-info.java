module com.group17.oop_project_group17_bongo_meat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.base;

    // Main package
    opens com.group17.oop_project_group17_bongo_meat to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat;

    // Farm Manager
    opens com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

    // Admin
    opens com.group17.oop_project_group17_bongo_meat.fahim.Admin to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.fahim.Admin;

    // Customer
    opens com.group17.oop_project_group17_bongo_meat.shaika.Customer to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.shaika.Customer;

    // Delivery Staff
    opens com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff;
}
