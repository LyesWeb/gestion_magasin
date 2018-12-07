package mVentes;

import java.io.File;
import java.time.LocalDate;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mCategories.Categorie;
import packfx.Client;
import packfx.ClientDaoImpl;
import packfx.Produit;
import packfx.ProduitDaoImpl;

public class VenteAdd extends Application{
	
	public static Text comp = new Text();
	public static TableView<LC> LC = new TableView<LC>();
	public ProduitDaoImpl db = new ProduitDaoImpl();
	
	@Override
	public void start(Stage window) throws Exception {

		window.setWidth(1000);
		window.setHeight(700);
		window.setTitle("Ajouter une nouvelle vente");
		BorderPane brd = new BorderPane();
		brd.setTop(createContentTopTitle());
		brd.setCenter(createContentCenter());
		Scene scene = new Scene(brd, 200, 300, Color.WHITE);
		File f = new File("css/style.css");
		scene.getStylesheets().clear();
		scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
		window.setScene(scene);
		window.show();
		
	}
	
	private Pane createContentTopTitle(){
		Pane pane = new HBox();
		pane.setId("headTitle");
		pane.setMinHeight(80);
		((HBox)pane).setAlignment(Pos.CENTER_LEFT);
		Text titre = new Text(" Liste des ventes :");
		titre.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		titre.setFill(Color.WHITE);
		pane.getChildren().add(titre);
		return pane;
	}
	
	private Pane createVenteInfo() {
		Pane pane = new HBox();
		pane.setStyle("-fx-padding: 0 15 0 15");
		//##### Date #####//
		VBox dateBox = new VBox();
		((VBox) dateBox).setSpacing(10);
		dateBox.setMinWidth(333);
		Text dateTxt = new Text("Date de Vente :");
		DatePicker date = new DatePicker();
		date.setOnAction(arg0 -> {
			LocalDate d = date.getValue();
		    System.err.println("Selected date: " + d);
		});
		dateBox.getChildren().addAll(dateTxt,date);
		//##### Date #####//
		
		//##### ID Vente #####//
		VBox idVenteBox = new VBox();
		((VBox) idVenteBox).setSpacing(10);
		idVenteBox.setMinWidth(333);
		idVenteBox.setAlignment(Pos.BASELINE_LEFT);
		// get last id from table
		VenteDaoImpl vdb = new VenteDaoImpl();
		Text idVenteTxt = new Text("Vente n : "+String.format("%03d", vdb.getLastId()));
		idVenteTxt.setId("idVente");
		idVenteBox.getChildren().addAll(idVenteTxt);
		//##### ID Vente #####//
		
		//##### Client Vente #####//
		VBox clientBox = new VBox();
		((VBox) clientBox).setSpacing(10);
		clientBox.setMinWidth(300);
		Text clientTxt = new Text("Client :");
		ComboBox<String> client = new ComboBox<>();
		// fill the comboBox from db
		ClientDaoImpl db = new ClientDaoImpl();
		for(Client c:db.getAll()) {			
			client.getItems().add(c.getNom());
		}
        new AutoCompleteComboBoxListener<>(client);
        client.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
              System.out.println(ov);
                System.out.println(t);
                System.out.println(t1);
            }    
        });
		clientBox.getChildren().addAll(clientTxt,client);
		//##### Client Vente #####//
		
		pane.getChildren().addAll(dateBox,idVenteBox,clientBox);
		return pane;
	}

	private Pane createVenteLC() {
		Pane pane = new HBox();
		pane.setStyle("-fx-padding: 0 15 0 15");
		((HBox) pane).setSpacing(10);
		//##### Product Vente LC #####//
		VBox productBox = new VBox();
		((VBox) productBox).setSpacing(10);
		productBox.setMaxWidth(400);
		Text productTxt = new Text("Produit :");
		ComboBox<String> product = new ComboBox<>();
		// fill the comboBox from db
		for(Produit p:db.getAll()) {			
			product.getItems().add(p.getDesignation());
		}
        new AutoCompleteComboBoxListener<>(product);
        product.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
//              System.out.println(ov);
//                System.out.println(t);
//                System.out.println(t1);
            }    
        });
        productBox.getChildren().addAll(productTxt,product);
		//##### Product Vente LC #####//
        
		//##### Product Qte LC #####//
        VBox qteBox = new VBox();
		((VBox) qteBox).setSpacing(10);
		qteBox.setMaxWidth(50);
		Text qteTxt = new Text("Qte :");
		TextField qte = new TextField();
		qteBox.getChildren().addAll(qteTxt,qte);
		//##### Product Qte LC #####//
		
		//##### Button add #####//
		VBox btnAddBox = new VBox();
		((VBox) btnAddBox).setSpacing(10);
		Text btnTxt = new Text("");
		Button add = new Button("Ajouter");
		btnAddBox.getChildren().addAll(btnTxt,add);
		pane.getChildren().addAll(productBox,qteBox,btnAddBox);
		
		add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	Categorie c = new Categorie(22,"sdfvsd");
            	Produit p1 = db.getOne(product.getValue());
            	LC lc = new LC(LC.getItems().size()+1, Integer.parseInt(qte.getText()), p1.getPrixVente(), p1, 2);
            	LC.getItems().add(lc);
            	qte.setText("");
            	product.requestFocus();
            }
        });
		
		return pane;
	}
	
	private Pane createVenteTableLC() {
		Pane pane = new HBox();
		pane.setStyle("-fx-padding: 0 15 0 15");
		((HBox) pane).setSpacing(10);
		//##### LC table #####//
        VBox tableBox = new VBox();
		((VBox) tableBox).setSpacing(10);
		tableBox.setMinWidth(920);
		tableBox.setMaxHeight(280);
		tableBox.getChildren().addAll(LC);
		//##### LC table #####//
		TableColumn idCol = new TableColumn("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("codelc"));

		TableColumn productCol = new TableColumn("Produit");
		productCol.setCellValueFactory(new PropertyValueFactory<>("produitlc"));

		TableColumn prixCol = new TableColumn("Prix");
		prixCol.setCellValueFactory(new PropertyValueFactory<>("soustotal"));

		TableColumn qteCol = new TableColumn("Qte");
		qteCol.setCellValueFactory(new PropertyValueFactory<>("qt"));

//		TableColumn totoCol = new TableColumn("Toto");
//		totoCol.setCellValueFactory(new PropertyValueFactory<>("Total"));
		
//		TableColumn actionsCol = new TableColumn("Actions");
		productCol.setMinWidth(200);
		LC.getColumns().addAll(idCol, productCol, prixCol, qteCol);
	
		pane.getChildren().addAll(tableBox);
		return pane;
	}
	
	private Pane createVenteFooter() {
		Pane pane = new HBox();
		pane.setStyle("-fx-padding: 0 15 0 15");
		((HBox) pane).setSpacing(10);
		((HBox)pane).setAlignment(Pos.BASELINE_RIGHT);
		pane.setMaxWidth(940);
		//##### Product Qte LC #####//
        VBox footerBox = new VBox();
		((VBox) footerBox).setSpacing(10);
		Button add = new Button("Ajouter");
		footerBox.getChildren().addAll(add);
		//##### Product Qte LC #####//
	
		pane.getChildren().addAll(footerBox);
		return pane;
	}
	
	private Pane createContentCenter(){
		Pane pane = new VBox();
		pane.setId("VenteCenter");
		((VBox) pane).setSpacing(15);
		
		
		pane.getChildren().addAll(createSectionTitle("Informations de la facture :"),createVenteInfo(),createSectionTitle("Lignes de commande :"),createVenteLC(),createVenteTableLC(),createVenteFooter());
		
		return pane;
	}
	
	private Pane createSectionTitle(String stitle){
		Pane sectionTitle = new HBox();
		sectionTitle.setMaxWidth(950);
		sectionTitle.setId("VenteTitle");
		Text title = new Text(stitle);
		sectionTitle.getChildren().add(title);
		return sectionTitle;
	}

}
