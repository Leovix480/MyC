module com.mycompany.myc {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.base;
    requires java.sql;
    requires java.desktop;

    opens com.mycompany.myc to javafx.fxml;
    exports com.mycompany.myc;
    //exports com.mycompany.myc.modelos;
    //requires jasperreports;
}