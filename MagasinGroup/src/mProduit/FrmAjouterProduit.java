package mProduit;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import mCategories.CategorieJavaFxForPrd;
import mProgramme.ApplicationJavaFx;
import mTools.Tools;

public class FrmAjouterProduit extends Application{

	public static Text message = new Text("");
	TextField txtCode = new TextField();
	TextField txtDesignation = new TextField();
	public static TextField txtCategorie = new TextField();
	ImageView catPlus = new ImageView();
	TextField txtPrixAchat = new TextField();
	TextField txtPrixVente = new TextField();
	Button photoChooser = new Button("Sélectionner une photo");
	Button btnAjouter = new Button("Ajouter");
	Button btnAnnuler = new Button("Annuler");
	ImageView imageproduit;
    File exist =new File("photosStock/thumbnail/"+ProduitDaoImpl.getCount()+".jpg");

	
	@Override
	public void start(Stage window) throws Exception {
		imageproduit = Tools.createImageView("photosStock/default.png");
		imageproduit.setId("imgPrev");
		imageproduit.setFitHeight(130);
        imageproduit.setFitWidth(250);
//        imageproduit.setLayoutX(18.0);
//        imageproduit.setLayoutY(128.0);
        imageproduit.setPickOnBounds(true);
		imageproduit.setOnMouseClicked(event -> {
            if (event.getClickCount() == 1) {
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Open Resource File");
                // Set extension filter
                FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Image files (*.jpg)", "*.jpg");
                fileChooser.getExtensionFilters().add(extFilter);
                File file = fileChooser.showOpenDialog(window);
                if(file.exists()){
                    InputStream is = null;
                    OutputStream os = null;
                    try {
                        is = new FileInputStream(file);
                        os = new FileOutputStream(exist);
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = is.read(buffer)) > 0) {
                            os.write(buffer, 0, length);   
                        }
                        imageproduit.setImage(new Image("file:///"+exist.getAbsolutePath()));
                    } catch (Exception ex) {
                    } finally {
                          try {
                              is.close();
                          } catch (IOException ex) {
//                              Logger.getLogger(ModifierProduit.class.getName()).log(Level.SEVERE, null, ex);
                          }
                          try {
                              os.close();
                          } catch (IOException ex) {
//                              Logger.getLogger(ModifierProduit.class.getName()).log(Level.SEVERE, null, ex);
                          }
                    }
                }
            }
        });
		
//		window.initModality(Modality.APPLICATION_MODAL);
		window.setWidth(540);
		window.setHeight(650);
		window.setTitle("Ajouter un produit");
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
		pane.getChildren().add(btnAjouter);
		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
            		String designation = txtDesignation.getText();
                	double prixAchat = Double.parseDouble(txtPrixAchat.getText());
                	double prixVente = Double.parseDouble(txtPrixVente.getText());
                    System.out.println("Produit ajoute avec succes");
                    Produit np = new Produit(designation, prixAchat, prixVente, CategorieJavaFxForPrd.categorieSelected);
                    ApplicationJavaFx.addPrd(np);
                    txtCode.setText("");
                    txtDesignation.setText("");
                    txtCategorie.setText("");
                    txtPrixAchat.setText("");
                    txtPrixVente.setText("");
                    imageproduit = Tools.createImageView("photosStock/default.png");
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
		pane.setId("CenterPane");
		((VBox)pane).setSpacing(12);
		pane.setPrefWidth(500);
		Text labelCode=new Text("Code:");
		Text labelDesignation=new Text("Désignation:");
		Text labelCategorie=new Text("Catégorie:");
		Text labelPrixAchat=new Text("Prix achat:");
		Text labelPrixVente=new Text("Prix Vente:");
		Text prev =new Text("La vignette:");
		pane.getChildren().add(labelDesignation);
		pane.getChildren().add(txtDesignation);
		pane.getChildren().add(labelCategorie);
		try {
			catPlus = Tools.createImageView("photosStock/Plus.png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		catPlus.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		     @Override
		     public void handle(MouseEvent event) {
		    	 CategorieJavaFxForPrd frmCAT = new CategorieJavaFxForPrd();
		            Stage w = new Stage();
		            try { frmCAT.start(w); } catch (Exception e) { e.printStackTrace(); }
		     }
		});
		
		HBox Hcat = new HBox();
		Hcat.getChildren().addAll(txtCategorie,catPlus);
		
		txtCategorie.setDisable(true);
//		pane.getChildren().add(txtCategorie);
//		pane.getChildren().add(catPlus);
		pane.getChildren().add(Hcat);
		pane.getChildren().add(labelPrixAchat);
		pane.getChildren().add(txtPrixAchat);
		pane.getChildren().add(labelPrixVente);
		pane.getChildren().add(txtPrixVente);
		pane.getChildren().add(prev);
		pane.getChildren().add(imageproduit);
		message.setFill(Color.RED);
		pane.getChildren().add(message);
		pane.getChildren().add(createContentBottom());
		return pane;
	}
	
	private Pane createContentTop(){
		Pane pane = new HBox();
		pane.setId("headTitle");
		pane.setPrefHeight(60);
		((HBox)pane).setAlignment(Pos.CENTER_LEFT);
		Text titre = new Text("Ajouter un produit");
		titre.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		titre.setFill(Color.WHITE);
		pane.getChildren().add(titre);
		return pane;
	}
	
}