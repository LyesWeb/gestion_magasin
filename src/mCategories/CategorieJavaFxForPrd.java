package mCategories;

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
import javafx.stage.Stage;
import mProduit.FrmAjouterProduit;
import mProgramme.ApplicationJavaFx;

public class CategorieJavaFxForPrd extends Application{

	Button btnAnnuler = new Button("Annuler");
	private static Text messageSelect = new Text("Double clique sur une categorie pour la sélectionne !");
	public static Text message = new Text("");
	public static CategorieDao cdao = new CategorieDaoImpl();
	public static Categorie categorieSelected = null;
	public static TableView<Categorie> table = new TableView<>();
	public static ObservableList<Categorie> categories = FXCollections.observableArrayList();

	private static void getCategories(){
		categories.clear();
		Collection<Categorie> cats = cdao.getAll();
		for(Categorie c:cats){
			categories.add(c);
		}
	}
	
	@Override
	public void start(Stage window) throws Exception {
//		window.initModality(Modality.APPLICATION_MODAL);
		window.setWidth(500);
		window.setHeight(415);
		window.setTitle("Gestion des categories");
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
		pane.setPrefWidth(500);
		messageSelect.setFill(Color.RED);
		pane.getChildren().add(messageSelect);
		pane.getChildren().add(createContentBottom());
		return pane;
	}

	private Pane createContentBottom(){
		Pane pane = new HBox();
		pane.setId("updateCenter");
		pane.getChildren().add(btnAnnuler);
		btnAnnuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
            		messageSelect.setText("");
            		Stage stage = (Stage) btnAnnuler.getScene().getWindow();
            	    stage.close();
				} catch (Exception e) {
					messageSelect.setFill(Color.RED);
					messageSelect.setText("Erreur !");
				}
            }
        });
		return pane;
	}
	
	private  Pane createContentTop(){
		Pane pane = new VBox();
		getCategories();
		table.getColumns().clear();
		TableColumn<Categorie, Long> codecatCol = new TableColumn<>("Code");
		codecatCol.setCellValueFactory(new PropertyValueFactory<>("codecat"));
		TableColumn<Categorie, String> intituleCol = new TableColumn<>("Intitule");
		intituleCol.setCellValueFactory(new  PropertyValueFactory<>("intitule"));
		table.getColumns().addAll(codecatCol, intituleCol);
		table.setItems(categories);
		table.setRowFactory(obj->{
			TableRow<Categorie> row = new TableRow<>();
			row.setOnMouseClicked(event->{
				try {
					if (event.getClickCount() == 2 && (!row.isEmpty())) {
						categorieSelected = row.getItem();
						FrmAjouterProduit.txtCategorie.setText(categorieSelected.getIntitule()+"");
						ApplicationJavaFx.contentCategorie.setText(categorieSelected.getIntitule()+"");
						messageSelect.setFill(Color.ORANGE);
						messageSelect.setText("Categorie bien selectionne.");
						Stage stage = (Stage)table.getScene().getWindow();
		        	    stage.close();
					}
				} catch (Exception e) {
					messageSelect.setFill(Color.RED);
					messageSelect.setText("Erreur !");
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
		pane.setId("headTitle");
		pane.setMinHeight(50);
		((HBox)pane).setAlignment(Pos.CENTER_LEFT);
		Text titre = new Text(" Liste des categories :");
		titre.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		titre.setFill(Color.WHITE);
		pane.getChildren().add(titre);
		return pane;
	}
	
	/* public static void main(String[] args) {
		Application.launch(args);
	} */
	
}
