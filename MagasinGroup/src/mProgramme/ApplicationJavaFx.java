package mProgramme;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Collection;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import mCategories.CategorieJavaFxVue;
import mClient.FrmAddClient;
import mDonnees.Config;
import mProduit.FrmAjouterProduit;
import mProduit.FrmModifierProduit;
import mProduit.Produit;
import mProduit.ProduitDao;
import mProduit.ProduitDaoImpl;
import mTools.Tools;
import mVentes.VenteAdd;
import mVentes.VenteJavaFxVue;

public class ApplicationJavaFx extends Application{
	
	public static boolean bVente = false;
	public static boolean bCat = false;
	public static boolean bClient = false;
	public static boolean bProduct = false;
	public static boolean bUpdate = false;
	
	public String appTitle = "Gestion De Magasin";
	public static ProduitDao pdao = new ProduitDaoImpl();
	public static TextField txtDesignation = new TextField(); 
	public static Text contentCode = new Text();
	public static Text contentDesignation = new Text();
	public static Text contentCategorie = new Text();
	public static Text contentPrixAchat = new Text();
	public static Text contentPrixVente = new Text();
	public static GridPane paneCenter = new GridPane();
	public static ImageView imgview;
	public HBox welcome = new HBox();
	public static ImageView welcomeImg;
	public static Produit produitSelected = null;
	public static TableView<Produit> table = new TableView<>();
	public static Text msg=new Text("");
	public static ObservableList<Produit> produits = FXCollections.observableArrayList();

	VBox hc = new VBox();
	EventHandler<KeyEvent> eventText=new EventHandler<KeyEvent>() {
		@Override
		public void handle(KeyEvent event) {
			getProduits(txtDesignation.getText());
			table.setItems(produits);
		}
	};
		
	private static void getProduits(){
		Collection<Produit> prods=pdao.getAll();
		for(Produit p:prods){
			produits.add(p);
		}
	}

	public static void addPrd(Produit p){
		pdao.insert(p);
		System.out.println("produit add 1..." + pdao.getAll().size());
		produits.clear();
		getProduits();
		table.setItems(produits);
	}

	public static void updatePrd(Produit p){
		pdao.update(p);
		produits.clear();
		getProduits();
		table.setItems(produits);
		displayContentProduit();
	}
	
	public static void deletePrd(long code){
		pdao.delete(code);
		produits.clear();
		produitSelected = null;
		getProduits();
		table.setItems(produits);
		displayContentProduit();
		displayContentPrdDeleted();
	}
	
	private void getProduits(String s){
		Collection<Produit> prods=pdao.getAll();
		produits.clear();
		for(Produit p:prods){
		if(p.getDesignation().contains(s))
			produits.add(p);
		}
	}

	public static int getCountProduits(){
		return produits.size();
	}
	
	@Override
	public void start(Stage window) throws Exception {
//		System.out.println(ProduitDaoImpl.getCount());
		try {
//			window.setWidth(1200);
//			window.setHeight(700);
			window.setMaximized(true);
			window.setTitle(Config.appName);
			BorderPane brd = new BorderPane();
			brd.setTop(createContentTop());
			brd.setLeft(createContentLeft());
			brd.setBottom(createContentBottom());
			brd.setRight(createContentRight());
			createContentCenter();
			paneCenter.setMinHeight(490);
			HBox hm = new HBox();
			hm.setId("msgBox");
			hm.getChildren().add(msg);
			hm.setAlignment(Pos.BOTTOM_LEFT);
			hc.getChildren().addAll(welcome, hm);
			brd.setCenter(hc);
			Scene scene = new Scene(brd, 200, 300, Color.WHITE);
			File f = new File("css/style.css");
			scene.getStylesheets().clear();
			scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
			window.setScene(scene);
			window.show();
		} catch (Exception e) {
			//System.out.println("Erreur BD !");
		    JFrame frame = new JFrame("Erreur");
		    JOptionPane.showMessageDialog(frame, "Probleme de connexion !", "Erreur de connexion", JOptionPane.ERROR_MESSAGE);
		}
		
	}
	
	private static void displayContentPrdDeleted() {
		contentCode.setText("");
		contentDesignation.setText("");
		contentCategorie.setText("");
		contentPrixAchat.setText("");
		contentPrixVente.setText("");
		//paneCenter.setVisible(false);
		
		//paneCenter.getChildren().remove(imgview);
		
		paneCenter.getChildren().remove(imgview);
	}

	private static void displayContentProduit() {
		contentCode.setText(produitSelected.getCode()+"");
		contentDesignation.setText(produitSelected.getDesignation());
		contentDesignation.setWrappingWidth(200);
		contentCategorie.setText(produitSelected.getCat().getIntitule().toString());
		contentPrixAchat.setText(produitSelected.getPrixAchat()+"");
		contentPrixVente.setText(produitSelected.getPrixVente()+"");
		imgview = null;
		try {
			imgview = Tools.createImageView("photosStock/" + produitSelected.getCode() + ".jpg");
//			imgview = Tools.createImageView("photosStock/1.jpg");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		imgview.setFitWidth(300);
		imgview.setFitHeight(300);
		//paneCenter.add(imgview, 1, 6, 2, 1);
		paneCenter.add(imgview, 1, 6);
	}

	private  Pane createContentRight(){
		Pane pane=new VBox();

		((VBox)pane).setSpacing(5);
		
		getProduits();
		
		TableColumn<Produit, Long> codeCol=new TableColumn<>("code");
		codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
		
		TableColumn<Produit, String> designationCol=new TableColumn<>("D�signation");
		designationCol.setCellValueFactory(new  PropertyValueFactory<>("designation"));

		TableColumn<Produit, Double> prixAchatCol=new TableColumn<>("Prix Achat");
		prixAchatCol.setCellValueFactory(new  PropertyValueFactory<>("prixAchat"));
		
		TableColumn<Produit, Double> prixVenteCol=new TableColumn<>("Prix vente");
		prixVenteCol.setCellValueFactory(new  PropertyValueFactory<>("prixVente"));
		
		table.getColumns().addAll(codeCol, designationCol, prixAchatCol, prixVenteCol);
		
		table.setItems(produits);
		
		table.setRowFactory(obj->{
			TableRow<Produit> row = new TableRow<>();
			row.setOnMouseClicked(event->{
			if (event.getClickCount() == 1 && (!row.isEmpty())) {
				produitSelected = row.getItem();
//				hc.getChildren().remove(welcome);
				hc.getChildren().set(0, paneCenter);
				displayContentProduit();
				msg.setText("");
			}
			});
			return row;
		});

		pane.setId("rightPane");
		pane.setPrefWidth(300);
		Text titre = new Text("Liste des produits");
		titre.setFont(Font.font("Calibri", FontWeight.BOLD, 18));
		titre.setFill(Color.BLACK);
		txtDesignation.setOnKeyReleased(eventText);
		pane.getChildren().addAll(titre,txtDesignation,table);
		return pane;
	}

	private HBox createContentUSer(){
		HBox box=new HBox();
		box.setSpacing(10);
		box.setAlignment(Pos.CENTER_RIGHT);
		HBox.setHgrow(box, Priority.ALWAYS);
		Text txtuser=new Text("Administrateur");
		txtuser.setFill(Color.WHITE);
		txtuser.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		ImageView imgview=new ImageView();
		FileInputStream file=null;
		try {
			file = new FileInputStream("photosStock/user.png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		Image img =new Image(file);
		imgview.setImage(img);
		imgview.setFitHeight(60);
		imgview.setFitWidth(60);
		box.getChildren().addAll(txtuser,imgview);
		return box;
	}
	private Pane createContentTop(){
		Pane pane=new HBox();
		pane.setId("topPane");
		pane.setPrefHeight(100);
		((HBox)pane).setAlignment(Pos.CENTER_LEFT);
		Text titre=new Text("Gestion des magasin");
		titre.setFont(Font.font("Calibri", FontWeight.BOLD, 30));
		titre.setFill(Color.WHITE);

		HBox boxUser=createContentUSer();
		pane.getChildren().add(titre);
		pane.getChildren().add(boxUser);

		return pane;
	}

	private Pane createContentBottom(){
		Pane hbox=new HBox();
		Text text=new Text("All Right Reserved 2018 Developped By GLSID Enset Mohammedia");
		text.setFill(Color.WHITE);
		hbox.getChildren().add(text);
		hbox.setId("bottomPane");
		hbox.setPrefHeight(40);
		return hbox;
	}
	
	private Pane createContentLeft(){
		Pane pane=new VBox();
		pane.setId("leftPane");
		pane.setPrefWidth(200);
		HBox pt=new HBox();
		pt.setId("pt");
		pt.setAlignment(Pos.CENTER);
		Text titre=new Text("MENUE");
		titre.setFont(Font.font("open sans", FontWeight.BOLD, 20));
		titre.setId("menue");
		pt.getChildren().add(titre);
		Hyperlink link1=new Hyperlink("Les Ventes");
		Hyperlink link4=new Hyperlink("Nouveau Produit");
		
		VBox sub1 = new VBox();
		Hyperlink link11=new Hyperlink(" -  Add vente");
		link11.setId("l11");

		Hyperlink link12=new Hyperlink(" -  Liste des ventes");
		link12.setId("l11");

		link11.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	VenteAdd frmVentadd = new VenteAdd();
                Stage w = new Stage();
                try {
					frmVentadd.start(w);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
		link12.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	VenteJavaFxVue frmVenteVue = new VenteJavaFxVue();
                Stage w = new Stage();
                try {
                	frmVenteVue.start(w);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
		
		link1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	if(sub1.getChildren().size()>0) {
            		sub1.getChildren().removeAll(link11,link12);
            	}else {
            		sub1.getChildren().addAll(link11,link12);
            	}
            	
//            	VenteJavaFxVue frmVent = new VenteJavaFxVue();
//                Stage w = new Stage();
//                Stage stage; 
//                try {
//                	if(bVente == true){
//                		if(VenteJavaFxVue.message != null){
//			                stage = (Stage)VenteJavaFxVue.message.getScene().getWindow();
//			                if(!stage.isShowing()){
//			                	frmVent.start(w);}
//			        }}
//                } catch (Exception e) { System.out.println("Erreur !"); }
//                try {
//                	if(bVente == false){
//                		frmVent.start(w); bVente = true;
//                	}
//                } catch (Exception e) { e.printStackTrace(); }
            	
            }
        });
		link4.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	FrmAjouterProduit frmJPrd = new FrmAjouterProduit();
                Stage w = new Stage();
                Stage stage; 
                try {
                	if(bProduct == true){
                		if(FrmAjouterProduit.message != null){
			                stage = (Stage)FrmAjouterProduit.message.getScene().getWindow();
			                if(!stage.isShowing()){
			                	frmJPrd.start(w);}
			        }}
                } catch (Exception e) { System.out.println("Erreur !"); }
                try {
                	if(bProduct == false){
                		frmJPrd.start(w); bProduct = true;
                	}
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
		
		Hyperlink link5=new Hyperlink("Modifier");
		link5.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	if(produitSelected != null){
            		FrmModifierProduit frmMPrd = new FrmModifierProduit();
	                Stage w = new Stage();
	                Stage stage; 
	                try {
	                	if(bUpdate == true){
	                		if(FrmModifierProduit.message != null){
				                stage = (Stage)FrmModifierProduit.message.getScene().getWindow();
				                if(!stage.isShowing()){
				                	frmMPrd.start(w);}
				        }}
	                } catch (Exception e) { System.out.println("Erreur !"); }
	                try {
	                	if(bUpdate == false){
	                		frmMPrd.start(w); bUpdate = true;
	                	}
	                } catch (Exception e) { e.printStackTrace(); }
            	}else{
                	msg.setText("Veuillez selectionner un produit.");
            	}
            }
        });
		
		Hyperlink link6=new Hyperlink("Suprimer");
		link6.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                if(produitSelected != null){
                	deletePrd(produitSelected.getCode());
                }else{
                	msg.setText("Veuillez selectionner un produit.");
                }
            }
        });
		Hyperlink link7=new Hyperlink("Les Clients");
		link7.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	FrmAddClient frmAC = new FrmAddClient();
                Stage w = new Stage();
                Stage stage; 
                try {
                	if(bClient == true){
                		if(FrmAddClient.message != null){
			                stage = (Stage)FrmAddClient.message.getScene().getWindow();
			                if(!stage.isShowing()){
			                	frmAC.start(w);}
			        }}
                } catch (Exception e) { System.out.println("Erreur !"); }
                try {
                	if(bClient == false){
                		frmAC.start(w); bClient = true;
                	}
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
		Hyperlink link8=new Hyperlink("Categories");
		link8.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	CategorieJavaFxVue frmCat = new CategorieJavaFxVue();
                Stage w = new Stage();
                Stage stage; 
                try {
                	if(bCat == true){
                		if(CategorieJavaFxVue.message != null){
			                stage = (Stage)CategorieJavaFxVue.message.getScene().getWindow();
			                if(!stage.isShowing()){
			                	frmCat.start(w);}
			        }}
                } catch (Exception e) { System.out.println("Erreur !"); }
                try {
                	if(bCat == false){
                		frmCat.start(w); bCat = true;
                	}
                } catch (Exception e) { e.printStackTrace(); }
            }
        });
		
		link1.setId("l1");link4.setId("l4");
		link5.setId("l5");link6.setId("l6");link7.setId("l7");
		link8.setId("l8");
		pane.getChildren().addAll(pt,link1,sub1,link4,link5,link6,link7,link8);
		return pane;
	}

	private void createContentCenter(){
		try {
			welcomeImg = Tools.createImageView("photosStock/welcome.png");
			welcomeImg.setFitHeight(250);
			welcomeImg.setFitWidth(550);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		};
		welcome.setPrefHeight(500);
		welcome.setAlignment(Pos.CENTER);
		welcome.getChildren().add(welcomeImg);
		
		Text labelCode=new Text("Code:");
		Text labelDesignation=new Text("D�signation:");
		Text labelCategorie=new Text("Cat�gorie:");
		Text labelPrixAchat=new Text("Prix achat:");
		Text labelPrixVente=new Text("Prix Vente:");
		msg.setId("msg");
		paneCenter.add(labelCode, 1, 1);
		paneCenter.add(labelDesignation, 1, 2);
		paneCenter.add(labelCategorie, 1, 3);
		paneCenter.add(labelPrixAchat, 1, 4);
		paneCenter.add(labelPrixVente, 1, 5);
		paneCenter.add(contentCode, 2, 1);
		paneCenter.add(contentDesignation, 2, 2);
		paneCenter.add(contentCategorie, 2, 3);
		paneCenter.add(contentPrixAchat, 2, 4);
		paneCenter.add(contentPrixVente, 2, 5);
		
		paneCenter.setAlignment(Pos.TOP_LEFT);
		paneCenter.setVgap(20);
		paneCenter.setHgap(20);
		paneCenter.setPrefWidth(200);
	}

	public static void main(String[] args) {
		Application.launch(args);
	}
	
	
}
