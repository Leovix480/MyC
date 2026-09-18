module com.mycompany.myc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires java.desktop;
    requires java.prefs;

    opens com.mycompany.myc to javafx.fxml;
    opens com.mycompany.modelos to javafx.base;
    exports com.mycompany.myc;
    //exports com.mycompany.myc.modelos;
    requires jasperreports;
 
}