package packfx;

import java.io.File;
import java.util.Collection;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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

public class FrmAddClient extends Application{
	private static Text messageSelect = new Text("");
	private static Text messageAction = new Text("");
	private static TextField nom = new TextField();
	private static TextField prenom = new TextField();
	private static TextField tele = new TextField();
	private static TextField email = new TextField();
	private static TextField adresse = new TextField();
	
	Button btnAjouter = new Button("Ajouter");
	Button btnSupprimer = new Button("Supprimer");
	Button btnModifier = new Button("Modifier");
	Button btnAnnuler = new Button("Annuler");
	
	public static ClientDao cdao = new ClientDaoImpl();
	public static TableView<Client> tableC = new TableView<>();
	public static ObservableList<Client> clients = FXCollections.observableArrayList();
	public static Client clientSelected = null;
	public static Text message=new Text("");
	
	public static void remplire(){
        nom.setText(clientSelected.getNom());
        prenom.setText(clientSelected.getPrenom());
        tele.setText(clientSelected.getTele());
        email.setText(clientSelected.getEmail());
        adresse.setText(clientSelected.getAdresse());
//        id.requestFocus();
	}public static void vider(){
        nom.setText("");
        prenom.setText("");
        tele.setText("");
        email.setText("");
        adresse.setText("");
	}
	
	private static void getClients(){
		Collection<Client> clis=cdao.getAll();
		for(Client c:clis){
			clients.add(c);
		}
	}
	public static void addClient(Client c){
		cdao.insert(c);
//		System.out.println("produit add 1..." + pdao.getAll().size());
		clients.clear();
		getClients();
		tableC.setItems(clients);
		tableC.scrollTo(clients.size());
	}
	public static void deleteClient(long code){
		cdao.delete(code);
		clients.clear();
		getClients();
		tableC.setItems(clients);
		clientSelected = null;
		
	}
	public static void updateClient(Client cli){
		cdao.update(cli);
		clients.clear();
		getClients();
		tableC.setItems(clients);
		clientSelected = null;
	}
	@Override
	public void start(Stage window) throws Exception {
//		window.initModality(Modality.APPLICATION_MODAL);
		window.setWidth(550);
		window.setMinHeight(700);
		window.setTitle("Ajouter un Client");
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
	
	private Pane createContentBottom(){
		Pane pane = new HBox();
		((HBox) pane).setSpacing(5);
		pane.getChildren().add(btnAjouter);
		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	if(nom.getText().equals("") || prenom.getText().equals("") || tele.getText().equals("") || email.getText().equals("")) {
            		message.setText("Entrer tout les information");
            		nom.requestFocus();
            	}else {
                	String nomClient = nom.getText();
                	String prenomClient = prenom.getText();
                	String teleClient = tele.getText();
                	String emailClient = email.getText();
                	String adresseClient = adresse.getText();
                	Client nc = new Client(nomClient, prenomClient, teleClient, emailClient, adresseClient);
                    addClient(nc);
                    // vider les champs
                    vider();
                    nom.requestFocus();
            	}
            }
        });
		pane.getChildren().add(btnModifier);
		btnModifier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
					if(clientSelected != null){
	            		Client cli = new Client();
	            		cli.setId(clientSelected.getId());
	            		cli.setNom(nom.getText());
	            		cli.setPrenom(prenom.getText());
	            		cli.setTele(tele.getText());
	            		cli.setEmail(email.getText());
	            		cli.setAdresse(adresse.getText());
	            		updateClient(cli);
	            		vider();
	            		messageSelect.setText("");
	                	messageAction.setFill(Color.LIMEGREEN);
	            		messageAction.setText("Client bien modifier.");
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
		pane.getChildren().add(btnSupprimer);
		btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	if(clientSelected != null){
                	deleteClient(clientSelected.getId());
                	// vider les champs
                    vider();
//                    id.requestFocus();
                }else{
                	message.setText("Veuillez selectionner un client.");
                }
            }
        });
		pane.getChildren().add(btnAnnuler);
		btnAnnuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
            		Stage stage = (Stage) btnAnnuler.getScene().getWindow();
            	    stage.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
            }
        });
		message.setFill(Color.RED);
		pane.getChildren().add(message);
		return pane;
	}

	private Pane createContentCenter(){
		vider();
		Pane pane = new VBox();
		pane.setId("clientCenter");
		((VBox)pane).setSpacing(12);
		pane.setPrefWidth(500);
		Text labelId=new Text("Matricule :");
		Text labelNom=new Text("Nom :");
		Text labelPrenom=new Text("Prenom :");
		Text labelTele=new Text("Tele :");
		Text labelEmail=new Text("Email :");
		Text labelAdress=new Text("Adresse :");
		
		getClients();
		TableColumn<Client, Integer> idCol=new TableColumn<>("Matricule");
		idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
		
		TableColumn<Client, String> nomCol=new TableColumn<>("Nom");
		nomCol.setCellValueFactory(new  PropertyValueFactory<>("Nom"));

		TableColumn<Client, String> prenomCol=new TableColumn<>("Prenom");
		prenomCol.setCellValueFactory(new  PropertyValueFactory<>("Prenom"));
		
		TableColumn<Client, String> teleCol=new TableColumn<>("Tele");
		teleCol.setCellValueFactory(new  PropertyValueFactory<>("Tele"));
		
		TableColumn<Client, String> AdresseCol=new TableColumn<>("Adresse");
		AdresseCol.setCellValueFactory(new  PropertyValueFactory<>("adresse"));
		
		tableC.getColumns().addAll(idCol, nomCol, prenomCol, teleCol, AdresseCol);
		
		
		tableC.setItems(clients);
		
		tableC.setRowFactory(obj->{
			TableRow<Client> row = new TableRow<>();
			row.setOnMouseClicked(event->{
			if (event.getClickCount() == 1 && (!row.isEmpty())) {
				clientSelected = row.getItem();
				message.setText("");
	            remplire();
			}
			});
			return row;
		});
		
		Pane t = new HBox();
		tableC.setMinHeight(200);
		tableC.setMinWidth(500);
		tableC.setId("ClientGride");
		t.getChildren().add(tableC);
		pane.getChildren().add(t);
		HBox NP = new HBox();
		VBox nm = new VBox();
		VBox pr = new VBox();
		nm.getChildren().addAll(labelNom,nom);
		pr.getChildren().addAll(labelPrenom,prenom);
		NP.getChildren().addAll(nm,pr);
		nom.setPrefWidth(150);
		prenom.setPrefWidth(150);
		((HBox)NP).setSpacing(20);
//		pane.getChildren().add(labelNom);pane.getChildren().add(nom);
//		pane.getChildren().add(labelPrenom);pane.getChildren().add(prenom);
		pane.getChildren().add(NP);
		pane.getChildren().add(labelTele);pane.getChildren().add(tele);
		pane.getChildren().add(labelEmail);pane.getChildren().add(email);
		pane.getChildren().add(labelAdress);pane.getChildren().add(adresse);
		

		
		
		pane.getChildren().add(createContentBottom());
		return pane;
	}
	
	private Pane createContentTop(){
		tableC.getColumns().clear();
		tableC.getItems().clear();
		Pane pane = new HBox();
		pane.setId("headTitle");
		pane.setPrefHeight(60);
		((HBox)pane).setAlignment(Pos.CENTER_LEFT);
		Text titre = new Text("Ajouter un client");
		titre.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		titre.setFill(Color.WHITE);
		pane.getChildren().add(titre);
		return pane;
	}


}
