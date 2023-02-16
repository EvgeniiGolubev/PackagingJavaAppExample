module com.example.packagingjavaappexample {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.example.packagingjavaappexample to javafx.fxml;
    exports com.example.packagingjavaappexample;
}