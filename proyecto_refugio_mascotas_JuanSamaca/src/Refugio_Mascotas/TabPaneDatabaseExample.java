package Refugio_Mascotas;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;    //importe de librerías
import java.sql.Statement;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.scene.Scene;

import javafx.stage.Stage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javax.swing.JOptionPane;

public class TabPaneDatabaseExample extends Application { //para cuando se ejecute el proyecto

    private TableView<Propietario> propietarioTable; //lista para almacenar datos de propietario
    private TableView<Mascota> mascotaTable; // lista para almacenar datos de mascota
    private TabPane tabPane; // panel de inicio
    private Tab propietarioTab; // panel propietario
    private Tab newPropietarioTab; // panel de crear propietario
    private Tab mascotaTab;// panel de mostrar mascota
    private Tab newMascotaTab; //panel de crear mascota
    private Tab editarPropietarioTab; // panel de editar/eliminar propietario
    private Tab editarMascotaTab; //panel para editar/eliminar mascota
    private ComboBox<Integer> propietarioIdComboBox; // comobobox que únicamente almacena el ID de propietario
    private ComboBox<Integer> mascotaIdComboBox; // combobox que hereda el ID asociado al propietario de una mascota

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Refugio de Mascotas"); // título del proyecto cuando ejecute
        tabPane = new TabPane(); // nuevo panel de inicio

        //  tabPane.setStyle("-fx-background-color: #6EC9FA"); // color definido para el panel inicio
        Image imagebanner = new Image("build_classes_APP_EMPLEADOS_CRUD_SQL/banner.png");
        ImageView bannerImageView = new ImageView(imagebanner);
        bannerImageView.setFitWidth(900);//1530, 792
        bannerImageView.setFitHeight(492);
        bannerImageView.setLayoutX(350);
        bannerImageView.setLayoutY(130);
        // Pesta?a de Propietarios
        propietarioTab = new Tab("Propietarios");
        propietarioTable = new TableView<>();
        propietarioTab.setContent(propietarioTable);

        // Pesta?a de Mascotas
        mascotaTab = new Tab("Mascotas");
        mascotaTable = new TableView<>();
        mascotaTab.setContent(mascotaTable);

        // Pesta?a de Editar Propietarios
        editarPropietarioTab = new Tab("Editar/Eliminar Propietarios");
        editarPropietarioTab.setContent(createEditPropietarioPane());

        // Pesta?a de Editar Mascotas
        editarMascotaTab = new Tab("Editar/Eliminar Mascotas");
        editarMascotaTab.setContent(createEditMascotaPane());

        newPropietarioTab = new Tab("Crear Propietario");//-----nuevo botón para crear propietario
        newPropietarioTab.setContent(createPropietarioPane());

        newMascotaTab = new Tab("Crear Mascota");//----nuevo botón para crear mascota
        newMascotaTab.setContent(CreateMascotaPane());

        // Configurar las columnas para Propietarios
        TableColumn<Propietario, Integer> propietarioIdColumn = new TableColumn<>("ID");
        propietarioIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Propietario, String> propietarioNombreColumn = new TableColumn<>("Nombre");
        propietarioNombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());

        TableColumn<Propietario, String> propietarioCiudadColum = new TableColumn<>("Ciudad");
        propietarioCiudadColum.setCellValueFactory(cellData -> cellData.getValue().CiudadProperty());

        TableColumn<Propietario, String> propietarioDireccionColumn = new TableColumn<>("Direcci?n");
        propietarioDireccionColumn.setCellValueFactory(cellData -> cellData.getValue().direccionProperty());

        TableColumn<Propietario, String> propietarioTelefonoColumn = new TableColumn<>("Teléfono");
        propietarioTelefonoColumn.setCellValueFactory(cellData -> cellData.getValue().TelefonoProperty());

        TableColumn<Propietario, String> propietarioCorreoColumn = new TableColumn<>("Correo");
        propietarioCorreoColumn.setCellValueFactory(cellData -> cellData.getValue().CorreoProperty());

        propietarioTable.getColumns().addAll(propietarioIdColumn, propietarioNombreColumn, propietarioCiudadColum, propietarioDireccionColumn, propietarioTelefonoColumn, propietarioCorreoColumn);

        // Configurar las columnas para Mascotas
        TableColumn<Mascota, Integer> mascotaIdColumn = new TableColumn<>("ID");
        mascotaIdColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());

        TableColumn<Mascota, String> mascotaNombreColumn = new TableColumn<>("Nombre");
        mascotaNombreColumn.setCellValueFactory(cellData -> cellData.getValue().nombreProperty());

        TableColumn<Mascota, String> mascotaTipoColumn = new TableColumn<>("Tipo");
        mascotaTipoColumn.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());

        TableColumn<Mascota, Integer> mascotaPropietarioIdColumn = new TableColumn<>("Propietario ID");
        mascotaPropietarioIdColumn.setCellValueFactory(cellData -> cellData.getValue().propietarioIdProperty().asObject());

        TableColumn<Mascota, String> mascotaRazaColumn = new TableColumn<>("Raza");
        mascotaRazaColumn.setCellValueFactory(cellData -> cellData.getValue().RazaProperty());

        TableColumn<Mascota, String> mascotaFechaNacimientoColumn = new TableColumn<>("Fecha de nacimiento");
        mascotaFechaNacimientoColumn.setCellValueFactory(cellData -> cellData.getValue().FechaNacimientoProperty());

        mascotaTable.getColumns().addAll(mascotaIdColumn, mascotaNombreColumn, mascotaTipoColumn, mascotaRazaColumn, mascotaFechaNacimientoColumn, mascotaPropietarioIdColumn);

        // Conectar a la base de datos y cargar datos
        loadData();

        // Crear el menú
        MenuBar menuBar = new MenuBar(); // barra de opciones para el menú
        menuBar.setStyle("-fx-background-color: #F2F2F2"); //colores para la barra del menú
        Menu menu = new Menu("Opciones"); //botón desplegable para opciones

        MenuItem createPropietario = new MenuItem("Registrar Propietario");//--------nueva opcion crear propietario
        createPropietario.setOnAction(e -> openTab(newPropietarioTab)); //agrega método para abrir la pestaña

        MenuItem openPropietarioTab = new MenuItem("Mostrar Propietarios");// mostrar lista con usuarios registrados
        openPropietarioTab.setOnAction(e -> openTab(propietarioTab));

        MenuItem createMascota = new MenuItem("Registrar Mascota");//--------------Nueva opcion crear mascota
        createMascota.setOnAction(e -> openTab(newMascotaTab));

        MenuItem openMascotaTab = new MenuItem("Mostrar  Mascotas"); // mostrar lista de mascotas registradas
        openMascotaTab.setOnAction(e -> openTab(mascotaTab)); //agrega método para abrir la pestaña

        MenuItem openEditarPropietarioTab = new MenuItem("Editar/Eliminar Propietarios");
        openEditarPropietarioTab.setOnAction(e -> openTab(editarPropietarioTab)); //agrega método para abrir la pestaña
        // menú para cargar dados de usuario/mascota, para modificar o eliminar
        MenuItem openEditarMascotaTab = new MenuItem("Editar/Eliminar Mascotas");
        openEditarMascotaTab.setOnAction(e -> openTab(editarMascotaTab)); //agrega método para abrir la pestaña

        menu.getItems().addAll(createMascota, createPropietario, openPropietarioTab, openMascotaTab, openEditarPropietarioTab, openEditarMascotaTab);
        menuBar.getMenus().add(menu); //cargar las opciones desde el menú de inicio en un orden establecido

        // Layout principal
        BorderPane root = new BorderPane();
        root.getChildren().addAll(bannerImageView);
        root.setTop(menuBar); //cargar la barra de opciones
        root.setCenter(tabPane); //cargar la pestaña de inicio en el centro

        Scene scene = new Scene(root, 1530, 792); // dimensiones del proyecto cuando se ejecute
        primaryStage.setScene(scene); //cargar la pestaña
        primaryStage.show(); //mostrar la pestaña cuando ejecute el proyecto

    }

    private void openTab(Tab tab) { //método para abrir pestañas para otras opciones
        if (!tabPane.getTabs().contains(tab)) {
            tabPane.getTabs().add(tab);
        }
        tabPane.getSelectionModel().select(tab);
    }

    private GridPane createEditPropietarioPane() {
        GridPane pane = new GridPane();
        pane.setStyle("-fx-background-color: #14D9C5");
        pane.setHgap(10);
        pane.setVgap(10);

        Label idLabel = new Label("ID:");
        idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        propietarioIdComboBox = new ComboBox<>();
        propietarioIdComboBox.setOnAction(e -> loadPropietarioData());

        Label nombreLabel = new Label("Nombre:");
        nombreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField nombreField = new TextField();

        Label ciudadLabel = new Label("Ciudad:");
        ciudadLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField ciudadField = new TextField();

        Label direccionLabel = new Label("Dirección:");
        direccionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField direccionField = new TextField();

        Label telefonoLabel = new Label("Teléfono:");
        telefonoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField telefonoField = new TextField();

        Label correoLabel = new Label("Correo:");
        correoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField correoField = new TextField();

        Button updateButton = new Button("Actualizar");
        updateButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Button deleteButton = new Button("Eliminar");
        deleteButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        updateButton.setOnAction(e -> {
            Integer id = propietarioIdComboBox.getValue();
            String nombre = nombreField.getText();
            String ciudad = ciudadField.getText();
            String direccion = direccionField.getText();
            String telefono = telefonoField.getText();
            String correo = correoField.getText();
            updatePropietario(id, nombre, ciudad, direccion, telefono, correo);
            loadData();

        });

        deleteButton.setOnAction(e -> {
            Integer id = propietarioIdComboBox.getValue();
            if (id != null) {
                deletePropietario(id);
                loadData();
            } else {
                System.out.println("Refugio_Mascotas.TabPaneDatabaseExample.createEditPropietarioPane()");
                // Manejar la falta de selección de ID
            }
        });

        pane.add(idLabel, 60, 0);
        pane.add(propietarioIdComboBox, 61, 0);
        pane.add(nombreLabel, 60, 7);
        pane.add(nombreField, 61, 7);
        pane.add(ciudadLabel, 60, 13);
        pane.add(ciudadField, 61, 13);
        pane.add(direccionLabel, 60, 19);
        pane.add(direccionField, 61, 19);
        pane.add(telefonoLabel, 60, 25);
        pane.add(telefonoField, 61, 25);
        pane.add(correoLabel, 60, 31);
        pane.add(correoField, 61, 31);
        pane.add(updateButton, 60, 37);
        pane.add(deleteButton, 61, 37);

        return pane;
    }

    private GridPane createPropietarioPane() {
        GridPane pane = new GridPane();
        pane.setStyle("-fx-background-color: #14D9C5"); // color definido para el panel inicio
        pane.setHgap(10); //tamaño horizontal
        pane.setVgap(10); //tamaño vertical

        Label nombreLabel = new Label("Nombre:"); //label y textfield para los datos de nombre
        nombreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField nombreField = new TextField();

        Label ciudadLabel = new Label("Ciudad:"); //label y textfield para los datos de ciudad
        ciudadLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField ciudadField = new TextField();

        Label direccionLabel = new Label("Dirección:"); //label y textfield para los datos de dirección
        direccionLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField direccionField = new TextField();

        Label telefonoLabel = new Label("Teléfono:"); //label y textfield para los datos de telefono
        telefonoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField telefonoField = new TextField();

        Label correoLabel = new Label("Correo:"); //label y textfield para los datos de  correo
        correoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField correoField = new TextField();

        Button createButton = new Button("Crear"); // crea botón para crear propietario
        createButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
createButton.setOnAction(e -> {
    try {
        String nombre = nombreField.getText();
        String ciudad = ciudadField.getText();
        String direccion = direccionField.getText();
        String telefono = telefonoField.getText();
        String correo = correoField.getText();
        if (nombre.isEmpty() ||ciudad.isEmpty()||direccion.isEmpty()||telefono.isEmpty() ||correo.isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor llenar todos los espacios antes de presionar el botón ", "Error", JOptionPane.ERROR_MESSAGE);
        
        }
        createPropietario(nombre, ciudad, direccion, telefono, correo);
        loadData();
    } catch (Exception ex) {
        JOptionPane.showMessageDialog(null, "Error al crear el propietario: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
    }
});;
        //carga según la posición los labels y textfield
        pane.add(nombreLabel, 60, 7);
        pane.add(nombreField, 61, 7);
        pane.add(ciudadLabel, 60, 13);
        pane.add(ciudadField, 61, 13);
        pane.add(direccionLabel, 60, 19);
        pane.add(direccionField, 61, 19);
        pane.add(telefonoLabel, 60, 25);
        pane.add(telefonoField, 61, 25);
        pane.add(correoLabel, 60, 31);
        pane.add(correoField, 61, 31);
        pane.add(createButton, 60, 37);

        return pane;
    }

    private GridPane CreateMascotaPane() {

        GridPane pane = new GridPane(); //crea el panel
        pane.setStyle("-fx-background-color: #14D9C5"); // color definido para el panel inicio
        pane.setHgap(10); //tamaño horizontal
        pane.setVgap(10); // tamaño vertical

        Label nombreLabel = new Label("Nombre:"); //label y textfield para los datos de nombre
        nombreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField nombreField = new TextField();

        Label tipoLabel = new Label("Tipo:"); //label y textfield para los datos de tipo
        tipoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField tipoField = new TextField();

        Label razaLabel = new Label("Raza:"); //label y textfield para los datos de raza
        razaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField razaField = new TextField();

        Label fechaNacimientoLabel = new Label("Fecha Nacimiento:"); //label y textfield para los datos de fecha de nacimiento
        fechaNacimientoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField fechaNacimientoField = new TextField();

        Label propietarioIdLabel = new Label("Propietario ID:");  //label y textfield para los datos heredados del id propietario
        propietarioIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        propietarioIdComboBox = new ComboBox<>();
        propietarioIdComboBox.setOnAction(e -> loadIdPropietarioData());

        Button createButton = new Button("Crear"); // botón para crear mascota
        createButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        createButton.setOnAction(e -> {
            try {
                String nombre = nombreField.getText();
                String tipo = tipoField.getText();
                int propietarioId = propietarioIdComboBox.getValue(); // Puede lanzar NullPointerException si no hay valor seleccionado
                String raza = razaField.getText();
                String fechaNacimiento = fechaNacimientoField.getText();

                createMascota(nombre, tipo, propietarioId, raza, fechaNacimiento);
                loadData();
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "Error: Debes seleccionar un valor de ID de propietario.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: El ID del propietario debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(null, "Error al crear la mascota: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        //carga según la posición de los labels y textfield
        //    pane.add(idLabel, 60, 1);
        //   pane.add(mascotaIdComboBox, 61, 1);
        pane.add(nombreLabel, 60, 7);
        pane.add(nombreField, 61, 7);
        pane.add(tipoLabel, 60, 13);
        pane.add(tipoField, 61, 13);
        pane.add(propietarioIdLabel, 60, 19);
        pane.add(propietarioIdComboBox, 61, 19);
        pane.add(razaLabel, 60, 25);
        pane.add(razaField, 61, 25);
        pane.add(fechaNacimientoLabel, 60, 31);
        pane.add(fechaNacimientoField, 61, 31);
        pane.add(createButton, 60, 37);

        return pane;
    }

    private GridPane createEditMascotaPane() {
        GridPane pane = new GridPane(); //crea el panel
        pane.setStyle("-fx-background-color: #14D9C5"); // color definido para el panel inicio
        pane.setHgap(10); //tamaño horizontal
        pane.setVgap(10); //tamaño vertical

        Label idLabel = new Label("ID:"); //label definido para id
        idLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        mascotaIdComboBox = new ComboBox<>();
        mascotaIdComboBox.setOnAction(e -> loadMascotaData());

        Label nombreLabel = new Label("Nombre:"); //label y textfield para los datos de nombre
        nombreLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField nombreField = new TextField();

        Label tipoLabel = new Label("Tipo:"); //label y textfield para los datos de tipo
        tipoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField tipoField = new TextField();

        Label razaLabel = new Label("Raza:"); //label y textfield para los datos de raza
        razaLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField razaField = new TextField();

        Label fechaNacimientoLabel = new Label("Fecha Nacimiento:"); //label y textfield para los datos de fecha de nacimiento
        fechaNacimientoLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField fechaNacimientoField = new TextField();

        Label propietarioIdLabel = new Label("Propietario ID:"); //label y textfield para los datos id propietario
        propietarioIdLabel.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        TextField propietarioIdField = new TextField();
        Button updateButton = new Button("Actualizar"); //botones para actualizar o eliminar
        updateButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));
        Button deleteButton = new Button("Eliminar");
        deleteButton.setFont(Font.font("Arial", FontWeight.BOLD, 14));

        updateButton.setOnAction(e -> {
            // L?gica para actualizar la mascota
            try {
                int id = mascotaIdComboBox.getValue();
                String nombre = nombreField.getText();
                String tipo = tipoField.getText();
                int propietarioId = Integer.parseInt(propietarioIdField.getText()); // registra los datos ingresados de la pestaña
                String raza = razaField.getText();
                String fechaNacimiento = fechaNacimientoField.getText();
                updateMascota(id, nombre, tipo, raza, fechaNacimiento, propietarioId);
                loadData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: El ID del propietario debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "Error: Debes seleccionar un valor de ID de mascota.", "Error", JOptionPane.ERROR_MESSAGE);
            }

        });

        deleteButton.setOnAction(e -> {
            try {
                int id = mascotaIdComboBox.getValue(); // un atributo int del comboBox de id propietarioque Puede lanzar NullPointerException si no hay valor seleccionado
                deleteMascota(id); // borra la mascota de acuerdo al id asociado
                loadData();
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "Error: El ID del propietario debe ser un número válido.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NullPointerException ex) {
                JOptionPane.showMessageDialog(null, "Error: Debes seleccionar un valor de ID de mascota.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        //      carga según la posición de los labels y textfield
        pane.add(idLabel, 60, 1);
        pane.add(mascotaIdComboBox, 61, 1);
        pane.add(nombreLabel, 60, 7);
        pane.add(nombreField, 61, 7);
        pane.add(tipoLabel, 60, 13);
        pane.add(tipoField, 61, 13);
        pane.add(propietarioIdLabel, 60, 19);
        pane.add(propietarioIdField, 61, 19);
        pane.add(razaLabel, 60, 25);
        pane.add(razaField, 61, 25);
        pane.add(fechaNacimientoLabel, 60, 31);
        pane.add(fechaNacimientoField, 61, 31);
        pane.add(updateButton, 60, 37);
        pane.add(deleteButton, 61, 37);

        return pane;
    }

    private void createPropietario(String nombre, String ciudad, String direccion, String telefono, String correo) {
        try { //try y catch dentro del método de crear
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", ""); // llama a la base de datos
            Statement statement = connection.createStatement();
            String query = "INSERT INTO Propietario (nombre, ciudad, direccion, telefono, correo) VALUES ( '" // variable String que aplique un método para insertar datos desde el db
                    + nombre + "', '" + ciudad + "', '" + direccion + "', '" + telefono + "', '" + correo + "')";
            statement.executeUpdate(query); // actualice el db
            connection.close(); // detenga la conexión cuando termine de actualizar los datos
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Se produjo un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updatePropietario(int id, String nombre, String ciudad, String direccion, String telefono, String correo) {
        try { //try y catch dentro del método de actualizar
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");// llama a la base de datos
            Statement statement = connection.createStatement();
            // variable String que aplique un método para actualizar a los propietarios desde el db de acuerdo a su sintaxis
            String query = "UPDATE Propietario SET nombre = '" + nombre + "', ciudad = '" + ciudad + "', direccion = '" + direccion + "', telefono = '" + telefono + "', correo = '" + correo + "' WHERE id = " + id;

            statement.executeUpdate(query); //actualice el db
            connection.close();  // detenga la conexión cuando termine de actualizar los datos
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Se produjo un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deletePropietario(int id) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");// llama a la base de datos
            Statement statement = connection.createStatement();
            String query = "DELETE FROM Propietario WHERE id = " + id; // crea una variable string, donde aplique un método para eliminar propietario de acuardo al id
            statement.executeUpdate(query);
            connection.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Se produjo un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void createMascota(String nombre, String tipo, int propietario_id, String raza, String fechaNacimiento) {

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
            Statement statement = connection.createStatement();
            String query = "INSERT INTO Mascota (nombre, tipo, propietario_id, raza, FechaNacimiento) VALUES ('" + nombre + "', '" + tipo + "', '" + propietario_id + "', '" + raza + "', '" + fechaNacimiento + "')";
            statement.executeUpdate(query);
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Se produjo un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void updateMascota(int id, String nombre, String tipo, String raza, String fechaNacimiento, int propietarioId) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
            Statement statement = connection.createStatement();
            String query = "UPDATE Mascota SET nombre = '" + nombre + "', tipo = '" + tipo + "', raza = '" + raza + "', FechaNacimiento = '" + fechaNacimiento + "', propietario_id = " + propietarioId + " WHERE id = " + id;
            statement.executeUpdate(query);
            connection.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Se produjo un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void deleteMascota(int id) {
        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
            Statement statement = connection.createStatement();
            String query = "DELETE FROM Mascota WHERE id = " + id;
            statement.executeUpdate(query);
            connection.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Se produjo un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void loadData() {
        ObservableList<Propietario> propietarioData = FXCollections.observableArrayList();
        ObservableList<Mascota> mascotaData = FXCollections.observableArrayList();
        ObservableList<Integer> propietarioIds = FXCollections.observableArrayList();
        ObservableList<Integer> mascotaIds = FXCollections.observableArrayList();

        try {
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
            Statement statement = connection.createStatement();

            // Cargar datos de propietarios
            ResultSet propietarioResultSet = statement.executeQuery("SELECT * FROM Propietario");
            while (propietarioResultSet.next()) {
                int id = propietarioResultSet.getInt("id");
                String nombre = propietarioResultSet.getString("nombre");
                String direccion = propietarioResultSet.getString("direccion");
                String telefono = propietarioResultSet.getString("telefono");
                String correo = propietarioResultSet.getString("correo");
                String ciudad = propietarioResultSet.getString("ciudad");
                propietarioData.add(new Propietario(id, nombre, ciudad, direccion, telefono, correo));
                propietarioIds.add(id);
            }

            // Cargar datos de mascotas
            ResultSet mascotaResultSet = statement.executeQuery("SELECT * FROM Mascota");
            while (mascotaResultSet.next()) {
                int id = mascotaResultSet.getInt("id");
                String nombre = mascotaResultSet.getString("nombre");
                String tipo = mascotaResultSet.getString("tipo");
                String raza = mascotaResultSet.getString("raza");
                String fechaNacimiento = mascotaResultSet.getString("fechaNacimiento");
                int propietarioId = mascotaResultSet.getInt("propietario_id");
                mascotaData.add(new Mascota(id, nombre, tipo, raza, fechaNacimiento, propietarioId));
                mascotaIds.add(id);
            }

            connection.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Se produjo un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

        propietarioTable.setItems(propietarioData);
        mascotaTable.setItems(mascotaData);
        propietarioIdComboBox.setItems(propietarioIds);
        mascotaIdComboBox.setItems(mascotaIds);
    }

    private void loadPropietarioData() {
        Integer id = propietarioIdComboBox.getValue();
        if (id != null) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Propietario WHERE id = " + id);
                if (resultSet.next()) {
                    String nombre = resultSet.getString("nombre");
                    String Ciudad = resultSet.getString("ciudad");
                    String direccion = resultSet.getString("direccion");
                    String telefono = resultSet.getString("telefono");
                    String correo = resultSet.getString("correo");
                    ((TextField) ((GridPane) editarPropietarioTab.getContent()).getChildren().get(3)).setText(nombre);
                    ((TextField) ((GridPane) editarPropietarioTab.getContent()).getChildren().get(5)).setText(Ciudad);
                    ((TextField) ((GridPane) editarPropietarioTab.getContent()).getChildren().get(7)).setText(direccion);
                    ((TextField) ((GridPane) editarPropietarioTab.getContent()).getChildren().get(9)).setText(telefono);
                    ((TextField) ((GridPane) editarPropietarioTab.getContent()).getChildren().get(11)).setText(correo);
                }
                connection.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Se produjo un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void loadMascotaData() {
        Integer id = mascotaIdComboBox.getValue();
        if (id != null) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Mascota WHERE id = " + id);
                if (resultSet.next()) {

                    String nombre = resultSet.getString("nombre");
                    String tipo = resultSet.getString("tipo");
                    int propietarioId = resultSet.getInt("propietario_id");
                    String raza = resultSet.getString("raza");
                    String fechaNacimiento = resultSet.getString("FechaNacimiento");
                    ((TextField) ((GridPane) editarMascotaTab.getContent()).getChildren().get(3)).setText(nombre);
                    ((TextField) ((GridPane) editarMascotaTab.getContent()).getChildren().get(5)).setText(tipo);
                    ((TextField) ((GridPane) editarMascotaTab.getContent()).getChildren().get(7)).setText(String.valueOf(propietarioId));
                    ((TextField) ((GridPane) editarMascotaTab.getContent()).getChildren().get(9)).setText(raza);
                    ((TextField) ((GridPane) editarMascotaTab.getContent()).getChildren().get(11)).setText(fechaNacimiento);
                }
                connection.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Se produjo un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
// nuevo método

    private void loadIdPropietarioData() {
        Integer id = propietarioIdComboBox.getValue();
        if (id != null) {
            try {
                Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/PetsDatabase", "root", "");
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT id FROM Propietario WHERE id = " + id); // Solo selecciona el ID
                if (resultSet.next()) {
                    int propietarioId = resultSet.getInt("id");
                    ((TextField) ((GridPane) editarPropietarioTab.getContent()).getChildren().get(3)).setText(String.valueOf(propietarioId)); // Asigna el ID al campo correspondiente
                }
                connection.close();
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "Se produjo un error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
