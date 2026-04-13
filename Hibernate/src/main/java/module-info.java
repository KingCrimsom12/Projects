module com.example.hibernate {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.persistence;
    requires org.hibernate.orm.core;
    requires java.sql;
    requires java.naming;

    opens com.example.hibernate to javafx.fxml;
    exports com.example.hibernate;

    exports com.example.hibernate.Controllers;
    opens com.example.hibernate.Controllers to javafx.fxml;

    exports com.example.hibernate.Database;
    opens com.example.hibernate.Database to org.hibernate.orm.core, javafx.fxml;

    exports com.example.hibernate.HibernateUtils;
    opens com.example.hibernate.HibernateUtils to javafx.fxml;

    exports com.example.hibernate.Service;
    opens com.example.hibernate.Service to javafx.fxml;
    exports com.example.hibernate.Controllers.Botones;
    opens com.example.hibernate.Controllers.Botones to javafx.fxml;
}