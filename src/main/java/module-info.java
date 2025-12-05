module com.group17.oop_project_group17_bongo_meat {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires javafx.graphics;
    requires javafx.base;
    requires com.github.librepdf.openpdf;
    requires org.apache.poi.ooxml;
    requires itextpdf;


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
    // Slaughterhousesupervisior
    opens com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;
    //QA officer
    opens com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

    //logistic manager
    opens com.group17.oop_project_group17_bongo_meat.Zainab.LogisticManager to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.Zainab.LogisticManager;

    //veterinary officer
    opens com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;

}
