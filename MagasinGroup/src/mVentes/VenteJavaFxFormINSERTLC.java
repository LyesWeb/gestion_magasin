package mVentes;

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
import packfx.Produit;
import packfx.ProduitDao;
import packfx.ProduitDaoImpl;

public class VenteJavaFxFormINSERTLC extends Application {

	TextField txtQte = new TextField();
	Button btnAjouter = new Button("Ajouter");
	Button btnAnnuler = new Button("Annuler");
	public static Text messageSelect = new Text("Veuillez selectionné un produit !");
	private static Text messageAction = new Text("");
	public static LCDao lcdao = new LCDaoImpl();
	public static VenteDao vdao = new VenteDaoImpl();
	public static ProduitDao pdao = new ProduitDaoImpl();
	public static Produit produitSelected = null;
	public static Vente venteS = null;
	public static TableView<Produit> table = new TableView<>();
	public static ObservableList<Produit> produits = FXCollections.observableArrayList();

	private static void getProduits(){
		produits.clear();
		Collection<Produit> prds = pdao.getAll();
		for(Produit c:prds){
			produits.add(c);
		}
	}

	public static void addLC(LC lc){
		lcdao.insert(lc);
		vdao.updateTotalVente(lc.getCodevente());
		produitSelected = null;
		getProduits();
		VenteJavaFxVue.charger();
	}
	
	public static void updateLC(LC lc){
		lcdao.update(lc);
		vdao.updateTotalVente(lc.getCodevente());
		produitSelected = null;
		getProduits();
		VenteJavaFxVue.charger();
	}
	
	@Override
	public void start(Stage window) throws Exception {
//		window.initModality(Modality.APPLICATION_MODAL);
		venteS = VenteJavaFxVue.venteSelected;
		window.setWidth(730);
		window.setHeight(515);
		window.setTitle("Ajout les Lignes de commandes");
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
		pane.setId("AddLCVenteCenter");
		pane.setPrefWidth(500);
		messageSelect.setFill(Color.RED);
		pane.getChildren().add(messageSelect);
		Text labelQt = new Text("Quantite commandé :");
		labelQt.setFont(Font.font("Verdana",FontWeight.NORMAL,16));
		pane.getChildren().add(labelQt);
		pane.getChildren().add(txtQte);
		messageAction.setFill(Color.RED);
		pane.getChildren().add(messageAction);
		pane.getChildren().add(createContentBottom());
		return pane;
	}

	private Pane createContentBottom(){
		Pane pane = new HBox();
		pane.getChildren().add(btnAjouter);
		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
					if(produitSelected != null && venteS != null){
	            		LC lc = new LC();
	            		lc.setQt(Integer.parseInt(txtQte.getText()));
	            		lc.setSoustotal(Integer.parseInt(txtQte.getText()) * produitSelected.getPrixVente());
	            		lc.setProduitlc(produitSelected);
	            		lc.setCodevente(venteS.getCodev());
	            		LC lc2 = lcdao.getLc(produitSelected.getCode(), venteS.getCodev());
	            		if(lc2 != null){
	            			lc.setCodelc(lc2.getCodelc());
	            			lc.setQt(lc.getQt() + lc2.getQt());
	            			lc.setSoustotal(lc.getSoustotal() + lc2.getSoustotal());
	            			updateLC(lc);
	            		}else{
	            			addLC(lc);
	            		}
	            		txtQte.setText("");
						messageSelect.setText("");
		                messageAction.setFill(Color.LIMEGREEN);
		                messageAction.setText("Ligne de commande ajouter avec succes.");
	            	}else{
	            		messageAction.setFill(Color.RED);
	            		messageAction.setText("Veuillez selectionné un produit !");
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
		getProduits();
		table.getColumns().clear();
		TableColumn<Produit, Long> codeCol=new TableColumn<>("code");
		codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
		TableColumn<Produit, String> designationCol=new TableColumn<>("Désignation");
		designationCol.setCellValueFactory(new  PropertyValueFactory<>("designation"));
		TableColumn<Produit, Double> prixAchatCol=new TableColumn<>("Prix Achat");
		prixAchatCol.setCellValueFactory(new  PropertyValueFactory<>("prixAchat"));
		TableColumn<Produit, Double> prixVenteCol=new TableColumn<>("Prix vente");
		prixVenteCol.setCellValueFactory(new  PropertyValueFactory<>("prixVente"));
		TableColumn<Produit, String> catCol=new TableColumn<>("Categorie");
		catCol.setCellValueFactory(new  PropertyValueFactory<>("cat"));
		table.getColumns().addAll(codeCol, designationCol, prixAchatCol, prixVenteCol, catCol);
		table.setItems(produits);
		table.setRowFactory(obj->{
			TableRow<Produit> row = new TableRow<>();
			row.setOnMouseClicked(event->{
				try {
					if (event.getClickCount() == 1 && (!row.isEmpty())) {
						produitSelected = row.getItem();
						messageAction.setText("");
						messageSelect.setFill(Color.ORANGE);
						messageSelect.setText("Produit bien selectionné.");
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
		return pane;
	}
	
	private Pane createContentTopTitle(){
		Pane pane = new HBox();
		pane.setMinHeight(60);
		pane.setId("headTitle");
		((HBox)pane).setAlignment(Pos.CENTER_LEFT);
		Text titre = new Text(" Ajouter des lignes de commandes au vente selectionné - Liste des produits :");
		titre.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		titre.setFill(Color.WHITE);
		pane.getChildren().add(titre);
		return pane;
	}
	
}
