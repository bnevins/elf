module com.elf.jfx {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.elf.jfx to javafx.fxml;
    exports com.elf.jfx;
}