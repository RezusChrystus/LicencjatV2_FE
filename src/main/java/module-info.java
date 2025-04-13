module org.example.licencjatv2_fe {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.kordamp.bootstrapfx.core;
    requires java.net.http;
    requires jdk.jsobject;
    requires org.json;
    requires static lombok;

    opens org.example.licencjatv2_fe to javafx.fxml;
    exports org.example.licencjatv2_fe.EntryWindow;
    opens org.example.licencjatv2_fe.EntryWindow to javafx.fxml;
    exports org.example.licencjatv2_fe.Api;
    opens org.example.licencjatv2_fe.Api to javafx.fxml;
}