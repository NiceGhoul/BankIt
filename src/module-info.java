/**
 * 
 */
/**
 * 
 */
module BankIt {
    requires javafx.fxml;
    requires java.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;
    requires java.sql;

    exports main;
    opens controller to javafx.fxml;
    opens main;
}