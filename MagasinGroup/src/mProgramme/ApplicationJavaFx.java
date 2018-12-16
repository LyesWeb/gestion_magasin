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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
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
import mCategories.Categorie;
import mCategories.CategorieJavaFxForPrd;
import mCategories.CategorieJavaFxVue;
import mClient.FrmAddClient;
import mDonnees.Config;
import mProduit.FrmAjouterProduit;
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
	public static ScrollPane scrollPane = new ScrollPane(paneCenter);
	public static ImageView imgview;
	public HBox welcome = new HBox();
	public static ImageView welcomeImg;
	public static Produit produitSelected = null;
	public static TableView<Produit> table = new TableView<>();
	public static Text msg=new Text("");
	public static ObservableList<Produit> produits = FXCollections.observableArrayList();
	public static Hyperlink btnSup = new Hyperlink("Suprimer");
	public static Hyperlink btnUp = new Hyperlink("Modifier");
    Boolean isEdit = false;
    public static HBox centerFooter = new HBox();
    public static TextField designationUpdate = new TextField();
    public static TextField prixAchatUpdate = new TextField();
    public static TextField prixVenteUpdate = new TextField();
    public static BorderPane brd = new BorderPane();
    public static VBox hc = new VBox();
	public static VBox centerVBOX = new VBox();
	HBox cateditHbox = new HBox();
	public static Boolean isFirst = true;
	ImageView imageEditCat = new ImageView();
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
		// delete image
		try {
			File f = new File("photosStock/thumbnail/"+produitSelected.getCode()+".jpg");
	        if(f.delete()){
	            System.out.println("yess");
	        }else System.out.println("nooo");
		}catch (Exception e){
	         e.printStackTrace();
		}
		// end delete image
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
		try {
//			window.setWidth(1200);
//			window.setHeight(700);
			window.setMaximized(true);
			window.setTitle(Config.appName);
			
			brd.setTop(createContentTop());
			brd.setLeft(createContentLeft());
			brd.setBottom(createContentBottom());
			brd.setRight(createContentRight());
			createContentCenter();
			paneCenter.setMinHeight(490);
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
		contentCode.setId("codeProduitContent");
		contentDesignation.setId("produitInfos");
		contentCategorie.setId("produitInfos");
		contentPrixAchat.setId("produitInfos");
		contentPrixVente.setId("produitInfos");
		
		contentCode.setText(produitSelected.getCode()+"");
		contentDesignation.setText(produitSelected.getDesignation());
		contentDesignation.setWrappingWidth(500);
		contentCategorie.setText(produitSelected.getCat().getIntitule().toString());
		contentPrixAchat.setText(produitSelected.getPrixAchat()+" DH");
		contentPrixVente.setText(produitSelected.getPrixVente()+" DH");
		imgview = null;
		try {
			imgview = Tools.createImageView("photosStock/thumbnail/" + produitSelected.getCode() + ".jpg");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		imgview.setFitWidth(300);
		imgview.setFitHeight(300);
		//paneCenter.add(imgview, 1, 6, 2, 1);
		paneCenter.add(imgview, 2, 6);
		designationUpdate.setText(produitSelected.getDesignation());
		prixAchatUpdate.setText(Double.toString(produitSelected.getPrixAchat()));
		prixVenteUpdate.setText(Double.toString(produitSelected.getPrixVente()));
		
		if(!isFirst) {
			hc.getChildren().clear();
			hc.getChildren().add(centerVBOX);
		}
		
	}

	private  Pane createContentRight(){
		Pane pane=new VBox();
		((VBox)pane).setSpacing(5);
		getProduits();
		TableColumn<Produit, Long> codeCol=new TableColumn<>("code");
		codeCol.setCellValueFactory(new PropertyValueFactory<>("code"));
		TableColumn<Produit, String> designationCol=new TableColumn<>("Désignation");
		designationCol.setCellValueFactory(new  PropertyValueFactory<>("designation"));
		TableColumn<Produit, Double> prixAchatCol=new TableColumn<>("Prix Achat");
		prixAchatCol.setCellValueFactory(new  PropertyValueFactory<>("prixAchat"));
		TableColumn<Produit, Double> prixVenteCol=new TableColumn<>("Prix vente");
		prixVenteCol.setCellValueFactory(new  PropertyValueFactory<>("prixVente"));
		table.setMinHeight(500);
		table.getColumns().addAll(codeCol, designationCol, prixAchatCol, prixVenteCol);
		table.setItems(produits);
		table.setRowFactory(obj->{
			TableRow<Produit> row = new TableRow<>();
			row.setOnMouseClicked(event->{
			if (event.getClickCount() == 1 && (!row.isEmpty())) {
				isFirst = false;
				produitSelected = row.getItem();
				displayContentProduit();
				msg.setText("");
			}
			});
			return row;
		});
		pane.setId("rightPane");
		pane.setPrefWidth(350);
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
		
		btnUp.setId("btn");
		btnUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	if(produitSelected != null){
            		/*FrmModifierProduit frmMPrd = new FrmModifierProduit();
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
	                } catch (Exception e) { e.printStackTrace(); }*/
            		isEdit = !isEdit;
            		if(isEdit) {
            			btnUp.setText("Mise a jour");
            			paneCenter.getChildren().remove(contentDesignation);
                		paneCenter.getChildren().remove(contentPrixAchat);
                		paneCenter.getChildren().remove(contentPrixVente);
                		paneCenter.getChildren().remove(contentCategorie);
                		paneCenter.add(designationUpdate, 2, 2);
                		try {
							cateditHbox.getChildren().addAll(contentCategorie, imageEditCat);
						} catch (Exception e) {
							e.printStackTrace();
						}
                		paneCenter.add(cateditHbox, 2, 3);
                		paneCenter.add(prixAchatUpdate, 2, 4);
                		paneCenter.add(prixVenteUpdate, 2, 5);
            		}else {
            			updateProduit();
            			btnUp.setText("Modifier");
            			paneCenter.getChildren().remove(designationUpdate);
                		paneCenter.getChildren().remove(prixAchatUpdate);
                		paneCenter.getChildren().remove(prixVenteUpdate);
                		paneCenter.getChildren().remove(cateditHbox);
                		paneCenter.add(contentDesignation, 2, 2);
                		paneCenter.add(contentCategorie, 2, 3);
                		paneCenter.add(contentPrixAchat, 2, 4);
                		paneCenter.add(contentPrixVente, 2, 5);
            		}
            	}else{
                	msg.setText("Veuillez selectionner un produit.");
            	}
            }
        });
		btnSup.setId("btn");
		btnSup.setOnAction(new EventHandler<ActionEvent>() {
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
		Hyperlink link8=new Hyperlink("Les categories");
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
		link7.setId("l7");
		link8.setId("l8");
		pane.getChildren().addAll(pt,link1,sub1,link4,link7,link8);
		return pane;
	}

	private void createContentCenter(){
		((HBox)cateditHbox).setSpacing(12);
		try {
			imageEditCat = Tools.createImageView("photosStock/edit.png");
			imageEditCat.setFitHeight(16);
			imageEditCat.setFitWidth(16);
			imageEditCat.setId("imageEditCat");
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		imageEditCat.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
		     @Override
		     public void handle(MouseEvent event) {
		    	 CategorieJavaFxForPrd frmCAT = new CategorieJavaFxForPrd();
	                Stage w = new Stage();
	                try {
	                	frmCAT.start(w); 
	                } catch (Exception e) {
//	                	message.setFill(Color.RED);
//	                	message.setText("Erreur !"); 
                	}
		     }
		});
		
		HBox labelCode = Tools.labelleProduit("Code");
		HBox labelDesignation = Tools.labelleProduit("Désignation");
		HBox labelCategorie = Tools.labelleProduit("Catégorie");
		HBox labelPrixAchat = Tools.labelleProduit("Prix achat");
		HBox labelPrixVente = Tools.labelleProduit("Prix Vente");
		HBox labelVignette = Tools.labelleProduit("Vignette");
		msg.setId("msg");
		paneCenter.add(labelCode, 1, 1);
		paneCenter.add(labelDesignation, 1, 2);
		paneCenter.add(labelCategorie, 1, 3);
		paneCenter.add(labelPrixAchat, 1, 4);
		paneCenter.add(labelPrixVente, 1, 5);
		paneCenter.add(labelVignette, 1, 6);
		
		HBox codeBox = new HBox();
		codeBox.setId("codeBoxProduct");
		codeBox.setMaxWidth(35);
		codeBox.setMinHeight(35);
		codeBox.setAlignment(Pos.CENTER);
		codeBox.getChildren().add(contentCode);
		paneCenter.add(codeBox, 2, 1);
		paneCenter.add(contentDesignation, 2, 2);
		paneCenter.add(contentCategorie, 2, 3);
		paneCenter.add(contentPrixAchat, 2, 4);
		paneCenter.add(contentPrixVente, 2, 5);
		
		paneCenter.setAlignment(Pos.TOP_LEFT);
		paneCenter.setVgap(20);
		paneCenter.setHgap(20);
		paneCenter.setPrefWidth(200);
		
	    centerFooter.setId("panePadding");
	    ((HBox)centerFooter).setSpacing(12);
	    centerFooter.getChildren().addAll(btnSup, btnUp);
	    centerVBOX.getChildren().addAll(scrollPane,centerFooter);
		
		try {
			welcomeImg = Tools.createImageView("photosStock/welcome.png");
			welcomeImg.setFitHeight(250);
			welcomeImg.setFitWidth(550);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		};
		welcome.setPrefHeight(560);
		welcome.setAlignment(Pos.CENTER);
		welcome.getChildren().add(welcomeImg);
		HBox hm = new HBox();
		hm.setId("msgBox");
		hm.getChildren().add(msg);
		hm.setAlignment(Pos.BOTTOM_LEFT);
		hc.getChildren().addAll(welcome, hm);
		brd.setCenter(hc);
	}
	public void updateProduit() {
		try {
    		long code = produitSelected.getCode();
        	String designation = designationUpdate.getText();
        	double prixAchat = Double.parseDouble(prixAchatUpdate.getText());
        	double prixVente = Double.parseDouble(prixVenteUpdate.getText());
        	Categorie objCat;
        	if(CategorieJavaFxForPrd.categorieSelected != null){
        		objCat = CategorieJavaFxForPrd.categorieSelected;
        	}else{
        		objCat = new Categorie(produitSelected.getCat().getCodecat(), produitSelected.getCat().getIntitule());
        	}
        	Produit np = new Produit(code, designation, prixAchat, prixVente, objCat);
            produitSelected.setCode(code);
            produitSelected.setDesignation(designation);
            produitSelected.setPrixAchat(prixAchat);
            produitSelected.setPrixVente(prixVente);
            produitSelected.setCat(objCat);
            updatePrd(np);
//            message.setFill(Color.LIMEGREEN);
//			message.setText("Produit modifier avec succes.");
		} catch (Exception e) {
//			message.setFill(Color.RED);
//			message.setText("Erreur !");
		}
	}
	public static void main(String[] args) {
		Application.launch(args);
	}
	
	
}
