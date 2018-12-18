package mCategories;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collection;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
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
import javafx.util.Callback;
import mProduit.Produit;
import mProduit.ProduitDaoImpl;
import mVentes.LC;
import mVentes.Vente;
import mVentes.VenteJavaFxVue;

public class Frmafficherproduits extends Application{
	
	
	public static ProduitDaoImpl pdao = new ProduitDaoImpl();
	public static Categorie categorieS = null;
	public static TableView<Produit> table = new TableView<>();
	public static ObservableList<Produit> produits = FXCollections.observableArrayList();
	
	
	private static void getProduits(Categorie c){
		
		produits.clear();
		Collection<Produit> prods=pdao.getproduits(c.getCodecat());
		for(Produit p:prods){
			System.out.println(p);
		
			produits.add(p);
		}
		}
	
	
	
	
	
	@Override
	public void start(Stage window) throws Exception {
		categorieS = CategorieJavaFxVue.categorieSelected;
//		window.initModality(Modality.APPLICATION_MODAL);
		window.setWidth(500);
		window.setHeight(512);
		window.setTitle("Afficher les produits");
		BorderPane brd = new BorderPane();
		brd.setTop(createContentTop());
		
		Scene scene = new Scene(brd, 200, 300, Color.WHITE);
		File f = new File("css/style.css");
		scene.getStylesheets().clear();
		scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
		window.setScene(scene);
		window.show();
	}



	
	private  Pane createContentTop(){
		Pane pane=new VBox();
		getProduits(categorieS);
		((VBox)pane).setSpacing(5);
		
		
		
		TableColumn<Produit, Long> codeCol=new TableColumn<>("code");
		codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
		
		TableColumn<Produit, String> designationCol=new TableColumn<>("Désignation");
		designationCol.setCellValueFactory(new  PropertyValueFactory<>("designation"));

		TableColumn<Produit, Double> prixAchatCol=new TableColumn<>("Prix Achat");
		prixAchatCol.setCellValueFactory(new  PropertyValueFactory<>("prixAchat"));
		
		TableColumn<Produit, Double> prixVenteCol=new TableColumn<>("Prix vente");
		prixVenteCol.setCellValueFactory(new  PropertyValueFactory<>("prixVente"));
		
		table.getColumns().addAll(codeCol, designationCol, prixAchatCol, prixVenteCol);
		
		table.setItems(produits);
		
		pane.setPrefHeight(300);
		pane.getChildren().addAll(createContentTopTitle(), table);
		return pane;
	}
	
	private Pane createContentTopTitle(){
		Pane pane = new HBox();
		pane.setId("headTitle");
		pane.setMinHeight(50);
		((HBox)pane).setAlignment(Pos.CENTER_LEFT);
		Text titre = new Text(" Liste des produits :");
		titre.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		titre.setFill(Color.WHITE);
		pane.getChildren().add(titre);
		return pane;
	}

}

