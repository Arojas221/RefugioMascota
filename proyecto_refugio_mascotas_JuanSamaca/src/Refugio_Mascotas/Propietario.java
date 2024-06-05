package Refugio_Mascotas;

import javafx.beans.property.*;

public class Propietario {

    private final IntegerProperty id;
    private final StringProperty nombre;
    private final StringProperty ciudad;
    private final StringProperty direccion;
    private final StringProperty telefono;
    private final StringProperty correo;

    public Propietario(int id, String nombre, String ciudad, String direccion, String telefono, String correo) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.ciudad = new SimpleStringProperty(ciudad);
        this.direccion = new SimpleStringProperty(direccion);
        this.correo = new SimpleStringProperty(correo);
        this.telefono = new SimpleStringProperty(telefono);

    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public String getNombre() {
        return nombre.get();
    }

    public StringProperty nombreProperty() {
        return nombre;
    }

    public String getCiudad() {
        return ciudad.get();
    }

    public StringProperty CiudadProperty() {
        return ciudad;
    }

    public String getDireccion() {
        return direccion.get();
    }

    public StringProperty direccionProperty() {
        return direccion;
    }

    public String getTelefono() {
        return telefono.get();
    }

    public StringProperty TelefonoProperty() {
        return telefono;
    }

    public String getCorreo() {
        return correo.get();
    }

    public StringProperty CorreoProperty() {
        return correo;
    }

}
