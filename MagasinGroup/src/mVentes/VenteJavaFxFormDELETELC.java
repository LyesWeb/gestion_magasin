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

public class VenteJavaFxFormDELETELC extends Application {

	Button btnSupprimer = new Button("Supprimer la ligne de commande");
	Button btnAnnuler = new Button("Annuler");
	public static Text message = new Text("Veuillez selectionné une ligne de commande !");
	public static VenteDao vdao = new VenteDaoImpl();
	public static LCDao lcdao = new LCDaoImpl();
	public static LCDao pdao = new LCDaoImpl();
	public static LC lcSelected = null;
	public static Vente venteS = null;
	public static TableView<LC> table = new TableView<>();
	public static ObservableList<LC> lignescommandes = FXCollections.observableArrayList();

	private static void getLCs(Vente v){
		lignescommandes.clear();
		Collection<LC> lcs = lcdao.getAll(v);
		for(LC c:lcs){
			System.out.println(c);
			lignescommandes.add(c);
		}
	}

	public static void deleteLC(long codelc){
		lcdao.delete(codelc);
		vdao.updateTotalVente(lcSelected.getCodevente());
		lignescommandes.clear();
		getLCs(venteS);
		table.setItems(lignescommandes);
		lcSelected = null;
		VenteJavaFxVue.charger();
	}
	
	@Override
	public void start(Stage window) throws Exception {
		System.err.println("test sup lc");
		venteS = VenteJavaFxVue.venteSelected;
//		window.initModality(Modality.APPLICATION_MODAL);
		window.setWidth(990);
		window.setHeight(450);
		window.setTitle("Supprimer des lignes de commande d'une vente");
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
		pane.setId("deleteLCCenter");
		((VBox)pane).setSpacing(12);
		pane.setPrefWidth(500);
		message.setFill(Color.RED);
		pane.getChildren().add(message);
		pane.getChildren().add(createContentBottom());
		return pane;
	}

	private Pane createContentBottom(){
		Pane pane = new HBox();
		((HBox)pane).setSpacing(20);
		pane.getChildren().add(btnSupprimer);
		btnSupprimer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
					if(lcSelected != null && venteS != null){
	            		deleteLC(lcSelected.getCodelc());
	            		message.setFill(Color.LIMEGREEN);
	            		message.setText(" Ligne de commande est supprimer avec succes.");
	            	}else{
	            		message.setFill(Color.RED);
	            		message.setText(" Ligne de commande non selectionné !");
	            	}
				} catch (Exception e) {
					message.setFill(Color.RED);
					message.setText(" Erreur ! ");
				}
            }
        });
		pane.getChildren().add(btnAnnuler);
		btnAnnuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
					message.setText("");
            		Stage stage = (Stage) btnAnnuler.getScene().getWindow();
            	    stage.close();
				} catch (Exception e) {
					message.setFill(Color.RED);
					message.setText(" Erreur ! ");
				}
            }
        });
		return pane;
	}
	
	private  Pane createContentTop(){
		Pane pane = new VBox();
		getLCs(venteS);
		table.getColumns().clear();
		TableColumn<LC, Long> codelcCol = new TableColumn<>("Code LC");
		codelcCol.setCellValueFactory(new PropertyValueFactory<>("codelc"));
		TableColumn<LC, Integer> qtCol = new TableColumn<>("Quantité");
		qtCol.setCellValueFactory(new  PropertyValueFactory<>("qt"));
		TableColumn<LC, Double> soustotalCol = new TableColumn<>("Sous Total");
		soustotalCol.setCellValueFactory(new  PropertyValueFactory<>("soustotal"));
		TableColumn<LC, Long> codeprdCol = new TableColumn<>("Produit");
		codeprdCol.setCellValueFactory(new  PropertyValueFactory<>("produitlc"));
		TableColumn<LC, Long> codeventeCol = new TableColumn<>("Code Vente");
		codeventeCol.setCellValueFactory(new  PropertyValueFactory<>("codevente"));
		table.getColumns().addAll(codelcCol, qtCol, soustotalCol, codeprdCol, codeventeCol);
		table.setItems(lignescommandes);
		table.setRowFactory(obj->{
			TableRow<LC> row = new TableRow<>();
			row.setOnMouseClicked(event->{
				try {
					if (event.getClickCount() == 1 && (!row.isEmpty())) {
						lcSelected = row.getItem();
						message.setFill(Color.ORANGE);
						message.setText(" LC bien selectionné. ");
					}else{
						lcSelected = null;
						message.setFill(Color.RED);
						message.setText(" LC n'est pas selectionné ! ");
					}
				} catch (Exception e) {
					message.setFill(Color.RED);
					message.setText(" Erreu ! ");
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
		Text titre = new Text(" Supprimer des lignes de commandes pour la vente selectionné ");
		titre.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		titre.setFill(Color.WHITE);
		pane.getChildren().add(titre);
		return pane;
	}
	
}
