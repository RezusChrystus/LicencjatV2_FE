module org.example.licencjatv2_fe {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;

    opens org.example.licencjatv2_fe to javafx.fxml;
    exports org.example.licencjatv2_fe;
}