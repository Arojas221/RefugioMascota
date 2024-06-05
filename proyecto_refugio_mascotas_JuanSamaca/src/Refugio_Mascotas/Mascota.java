package Refugio_Mascotas;

import javafx.beans.property.*;

public class Mascota {

    private final IntegerProperty id;
    private final StringProperty nombre;
    private final StringProperty tipo;
    private final IntegerProperty propietarioId;
    private final StringProperty raza;
    private final StringProperty fechaNacimiento;

    public Mascota(int id, String nombre, String tipo, String raza, String fechaNacimiento, int propietarioId) {
        this.id = new SimpleIntegerProperty(id);
        this.nombre = new SimpleStringProperty(nombre);
        this.tipo = new SimpleStringProperty(tipo);
        this.raza = new SimpleStringProperty(raza);
        this.fechaNacimiento = new SimpleStringProperty(fechaNacimiento);
        this.propietarioId = new SimpleIntegerProperty(propietarioId);
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

    public String getTipo() {
        return tipo.get();
    }

    public StringProperty tipoProperty() {
        return tipo;
    }

    public String getRaza() {
        return raza.get();
    }

    public StringProperty RazaProperty() {
        return raza;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento.get();
    }

    public StringProperty FechaNacimientoProperty() {
        return fechaNacimiento;
    }

    public int getPropietarioId() {
        return propietarioId.get();
    }

    public IntegerProperty propietarioIdProperty() {
        return propietarioId;
    }

}
