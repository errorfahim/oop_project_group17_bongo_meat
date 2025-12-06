module com.group17.oop_project_group17_bongo_meat {

    // JavaFX
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.base;

    // Desktop + File Chooser
    requires java.desktop;

    // Apache POI (Excel)
    requires org.apache.poi.ooxml;
    requires org.apache.poi.poi;

    // Log4j (REQUIRED by Apache POI)
   // requires org.apache.logging.log4j;
    //requires org.apache.logging.log4j.core;

    // PDF libraries
    requires itextpdf;
    requires com.github.librepdf.openpdf;

    // MAIN PACKAGE
    opens com.group17.oop_project_group17_bongo_meat to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat;

    // FARM MANAGER
    opens com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.fahim.Farm_Manager;

    // ADMIN
    opens com.group17.oop_project_group17_bongo_meat.fahim.Admin to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.fahim.Admin;

    // CUSTOMER
    opens com.group17.oop_project_group17_bongo_meat.shaika.Customer to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.shaika.Customer;

    // DELIVERY STAFF
    opens com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.shaika.DeliveryStaff;

    // SLAUGHTERHOUSE SUPERVISOR
    opens com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.Abdullah.SlaughterHouseSupervisior;

    // QA OFFICER
    opens com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.Abdullah.QAOfficer;

    // LOGISTIC MANAGER
    opens com.group17.oop_project_group17_bongo_meat.Zainab.LogisticManager to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.Zainab.LogisticManager;

    // VETERINARY OFFICER
    opens com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer to javafx.fxml;
    exports com.group17.oop_project_group17_bongo_meat.Zainab.VeterinaryOfficer;
}
