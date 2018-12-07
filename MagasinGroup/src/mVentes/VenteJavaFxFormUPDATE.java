package mVentes;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import packfx.Client;
import packfx.ClientDao;
import packfx.ClientDaoImpl;

public class VenteJavaFxFormUPDATE extends Application {

	public static Text messageSelect = new Text("Client bien selecionné.");
	private static Text messageAction = new Text("");
	DatePicker datep = new DatePicker();		
	Button btnModifier = new Button("Modifier");
	Button btnAnnuler = new Button("Annuler");
	public static VenteDao vdao = new VenteDaoImpl();
	public static ClientDao cdao = new ClientDaoImpl();
	public static Client clientSelected = null;
	public static Vente venteS = null;
	public static TableView<Client> table = new TableView<>();
	public static ObservableList<Client> clients = FXCollections.observableArrayList();

	private static void getClients(){
		clients.clear();
		Collection<Client> clis = cdao.getAll();
		for(Client c:clis){
			clients.add(c);
		}
	}

	public static void updateVente(Vente v){
		vdao.update(v);
		getClients();
		clientSelected = null;
	}
	
	@Override
	public void start(Stage window) throws Exception {
		venteS = VenteJavaFxVue.venteSelected;
//		window.initModality(Modality.APPLICATION_MODAL);
		window.setWidth(630);
		window.setHeight(480);
		window.setTitle("Modifier la vente");
		BorderPane brd = new BorderPane();
		brd.setTop(createContentTop());
		brd.setCenter(createContentCenter());
		Scene scene = new Scene(brd, 200, 300, Color.WHITE);
		File f = new File("css/style.css");
		scene.getStylesheets().clear();
		scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
		window.setScene(scene);
		window.show();
	}

	private Pane createContentCenter(){
		Pane pane = new VBox();
		((VBox)pane).setSpacing(12);
		pane.setId("updateVCenter");
		pane.setPrefWidth(500);
		Text labelDatev = new Text("Date de vente :");
		labelDatev.setFont(Font.font("Verdana",FontWeight.NORMAL,16));
		pane.getChildren().add(labelDatev);
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-d");
		String date = VenteJavaFxVue.venteSelected.getDatev().toString();
		LocalDate localDate = LocalDate.parse(date, formatter);
		datep.setValue(LocalDate.of(localDate.getYear(), localDate.getMonthValue(), localDate.getDayOfMonth()));
		pane.getChildren().add(datep);
		messageAction.setFill(Color.RED);
		pane.getChildren().add(messageAction);
		pane.getChildren().add(createContentBottom());
		return pane;
	}

	private Pane createContentBottom(){
		clientSelected = VenteJavaFxVue.venteSelected.getClientv();
		Pane pane = new HBox();
		((HBox) pane).setSpacing(5);
		pane.getChildren().add(btnModifier);
		btnModifier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
					if(clientSelected != null){
	            		java.sql.Date datev = java.sql.Date.valueOf(datep.getValue());
	            		Vente nv = new Vente(venteS.getCodev(), datev, clientSelected);
		                updateVente(nv);
		                VenteJavaFxVue.charger();
		                messageSelect.setText("");
	                	messageAction.setFill(Color.LIMEGREEN);
	            		messageAction.setText("Vente modifier avec succes.");
	            	}else{
	            		messageSelect.setFill(Color.RED);
	                	messageSelect.setText("Veuillez selectionner un client !");
	                	messageAction.setFill(Color.RED);
	            		messageAction.setText("Veuillez selectionner un client !");
	            	}
				} catch (Exception e) {
					messageSelect.setText("");
                	messageAction.setFill(Color.RED);
            		messageAction.setText("Erreur !");
				}            	
            }
        });
		pane.getChildren().add(btnAnnuler);
		btnAnnuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
            		messageSelect.setText("");
            		messageAction.setText("");
            		Stage stage = (Stage) btnAnnuler.getScene().getWindow();
            	    stage.close();
				} catch (Exception e) {
					messageSelect.setText("");
					messageAction.setFill(Color.RED);
            		messageAction.setText("Erreur !");
				}
            }
        });
		return pane;
	}
	
	private  Pane createContentTop(){
		Pane pane = new VBox();
		((VBox)pane).setSpacing(10);
		getClients();
		TableColumn<Client, Integer> idCol=new TableColumn<>("Id");
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		TableColumn<Client, String> nomCol=new TableColumn<>("Nom");
		nomCol.setCellValueFactory(new  PropertyValueFactory<>("nom"));
		TableColumn<Client, String> prenomCol=new TableColumn<>("Prenom");
		prenomCol.setCellValueFactory(new  PropertyValueFactory<>("prenom"));
		TableColumn<Client, String> teleCol=new TableColumn<>("Téléphone");
		teleCol.setCellValueFactory(new  PropertyValueFactory<>("tele"));
		TableColumn<Client, String> emailCol=new TableColumn<>("Email");
		emailCol.setCellValueFactory(new  PropertyValueFactory<>("email"));
		TableColumn<Client, String> adresseCol=new TableColumn<>("Adresse");
		adresseCol.setCellValueFactory(new  PropertyValueFactory<>("adresse"));
		table.getColumns().addAll(idCol, nomCol, prenomCol, teleCol, emailCol, adresseCol);
		table.setItems(clients);
		table.setRowFactory(obj->{
			TableRow<Client> row = new TableRow<>();
			row.setOnMouseClicked(event->{
				try {
					if (event.getClickCount() == 1 && (!row.isEmpty())) {
						clientSelected = row.getItem();
						messageAction.setText("");
						messageSelect.setFill(Color.ORANGE);
						messageSelect.setText("Client bien selectionné.");
					}
				} catch (Exception e) {
					messageSelect.setText("");
                	messageAction.setFill(Color.RED);
            		messageAction.setText("Erreur !");
				}
				
			});
			return row;
		});
		pane.setPrefHeight(300);
		pane.getChildren().addAll(createContentTopTitle(), table);
		messageSelect.setFill(Color.LIMEGREEN);
		pane.getChildren().add(messageSelect);
		return pane;
	}
	
	private Pane createContentTopTitle(){
		Pane pane = new HBox();
		pane.setId("headTitle");
		pane.setPrefHeight(60);
		((HBox)pane).setAlignment(Pos.CENTER_LEFT);
		Text titre = new Text(" Modification du vente - Liste des clients :");
		titre.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		titre.setFill(Color.WHITE);
		pane.getChildren().add(titre);
		return pane;
	}
	
}
