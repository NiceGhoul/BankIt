/**
 * 
 */
/**
 * 
 */
module BankIt {
    requires javafx.fxml;
    requires java.base;
    // requires org.apache.commons.lang3;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires java.sql;

    exports main;
    exports controller;
    opens controller to javafx.fxml;
    opens main;
}