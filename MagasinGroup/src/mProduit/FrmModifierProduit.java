package mProduit;

import java.io.File;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
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
import mCategories.Categorie;
import mCategories.CategorieJavaFxForPrd;
import mCategories.CategorieJavaFxVue;
import mProgramme.ApplicationJavaFx;

public class FrmModifierProduit extends Application{

	public static Text message = new Text("");
	TextField txtCode = new TextField();
	TextField txtDesignation = new TextField();
	public static TextField txtCategorie = new TextField();
	Hyperlink linkCat=new Hyperlink(" >>> ");
	TextField txtPrixAchat = new TextField();
	TextField txtPrixVente = new TextField();
	Button btnModifier = new Button("Modifier");
	Button btnAnnuler = new Button("Annuler");
	
	@Override
	public void start(Stage window) throws Exception {
//		window.initModality(Modality.APPLICATION_MODAL);
		window.setWidth(500);
		window.setHeight(520);
		window.setTitle("Modification du produit");		
		txtCode.setText(ApplicationJavaFx.produitSelected.getCode()+"");
		txtDesignation.setText(ApplicationJavaFx.produitSelected.getDesignation()+"");
		txtCategorie.setText(ApplicationJavaFx.produitSelected.getCat().getIntitule()+"");
		txtPrixAchat.setText(ApplicationJavaFx.produitSelected.getPrixAchat()+"");
		txtPrixVente.setText(ApplicationJavaFx.produitSelected.getPrixVente()+"");
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
		((HBox)pane).setSpacing(12);
		pane.getChildren().add(btnModifier);
		btnModifier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
            		long code = Long.parseLong(txtCode.getText());
                	String designation = txtDesignation.getText();
                	double prixAchat = Double.parseDouble(txtPrixAchat.getText());
                	double prixVente = Double.parseDouble(txtPrixVente.getText());
                	Categorie objCat;
                	if(CategorieJavaFxForPrd.categorieSelected != null){
                		objCat = CategorieJavaFxForPrd.categorieSelected;
                	}else{
                		objCat = new Categorie(ApplicationJavaFx.produitSelected.getCat().getCodecat(), ApplicationJavaFx.produitSelected.getCat().getIntitule());
                	}
                	Produit np = new Produit(code, designation, prixAchat, prixVente, objCat);
                    ApplicationJavaFx.produitSelected.setCode(code);
                    ApplicationJavaFx.produitSelected.setDesignation(designation);
                    ApplicationJavaFx.produitSelected.setPrixAchat(prixAchat);
                    ApplicationJavaFx.produitSelected.setPrixVente(prixVente);
                    ApplicationJavaFx.produitSelected.setCat(objCat);
                    ApplicationJavaFx.updatePrd(np);
                    message.setFill(Color.LIMEGREEN);
					message.setText("Produit modifier avec succes.");
				} catch (Exception e) {
					message.setFill(Color.RED);
					message.setText("Erreur !");
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
					message.setFill(Color.RED);
					message.setText("Erreur !");
				}
            }
        });
		return pane;
	}
	
	private Pane createContentCenter(){
		Pane pane = new VBox();
		pane.setId("updateCenter");
		((VBox)pane).setSpacing(12);
		pane.setPrefWidth(500);
		Text labelCode=new Text("Code:");
		Text labelDesignation=new Text("Désignation:");
		Text labelCategorie=new Text("Catégorie:");
		Text labelPrixAchat=new Text("Prix achat:");
		Text labelPrixVente=new Text("Prix Vente:");
		txtCode.setDisable(true);
		pane.getChildren().add(labelCode);
		pane.getChildren().add(txtCode);
		pane.getChildren().add(labelDesignation);
		pane.getChildren().add(txtDesignation);
		pane.getChildren().add(labelCategorie);
		linkCat.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	message.setFill(Color.ORANGE);
				message.setText("Categorie bien selectionné.");
				CategorieJavaFxForPrd frmCAT = new CategorieJavaFxForPrd();
                Stage w = new Stage();
                try { frmCAT.start(w); } 
                catch (Exception e) { message.setFill(Color.RED); message.setText("Erreur !"); }
        }});
		txtCategorie.setDisable(true);
		pane.getChildren().add(txtCategorie);
		pane.getChildren().add(linkCat);
		pane.getChildren().add(labelPrixAchat);
		pane.getChildren().add(txtPrixAchat);
		pane.getChildren().add(labelPrixVente);
		pane.getChildren().add(txtPrixVente);
		message.setFill(Color.RED);
		pane.getChildren().add(message);
		pane.getChildren().add(createContentBottom());
		return pane;
	}
	
	private Pane createContentTop(){
		Pane pane = new HBox();
		pane.setId("headTitle");
		pane.setPrefHeight(30);
		((HBox)pane).setAlignment(Pos.CENTER_LEFT);
		Text titre = new Text("Modifier un produit");
		titre.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		titre.setFill(Color.WHITE);
		pane.getChildren().add(titre);
		return pane;
	}
	
}