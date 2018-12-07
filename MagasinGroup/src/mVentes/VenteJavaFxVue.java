package mVentes;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;

import javax.tools.Tool;

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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
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
import mCategories.CategorieJavaFxForPrd;
import packfx.Tools;
import sun.applet.Main;

public class VenteJavaFxVue extends Application{
	public static boolean bAdd = false;
	public static boolean bUpdate = false;
	public static boolean bAddLC = false;
	public static boolean bDeleteLC = false;
	Button btnAjouter = new Button("Ajouter");
	Button add = new Button("add");
	Button btnModifier = new Button("Modifier");
	ImageView btnicon = new ImageView();
	Button btnAjouterLC = new Button("Ajouter des lignes de commande");
	Button btnSupprimerLC = new Button("Lignes des commandes");
	Button btnAnnuler = new Button("Annuler");
	public static Text message = new Text("");
	public static VenteDao vdao = new VenteDaoImpl();
	public static Vente venteSelected = null;
	public static TableView<Vente> table = new TableView<>();
	public static ObservableList<Vente> ventes = FXCollections.observableArrayList();
	
	private static void getVentes(){
		ventes.clear();
		Collection<Vente> vts = vdao.getAll();
		for(Vente c:vts){
			ventes.add(c);
		}
	}
	
	public static void deleteVente(long codev){
		vdao.delete(codev);
		ventes.clear();
		getVentes();
		table.setItems(ventes);
		venteSelected = null;
	}
	
	public static void charger(){
		ventes.clear();
		getVentes();
		table.setItems(ventes);
		venteSelected = null;
	}
	
	@Override
	public void start(Stage window) throws Exception {
//		window.initModality(Modality.APPLICATION_MODAL);
		window.setWidth(1000);
		window.setHeight(460);
		window.setTitle("Gestion des ventes");
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
		pane.setId("VenteCenter");
		pane.setPrefWidth(500);
		message.setFill(Color.RED);
		pane.getChildren().add(message);
		Text labelActions = new Text(" Actions :");
		labelActions.setFont(Font.font("open sans",FontWeight.BOLD,16));
		pane.getChildren().add(labelActions);
		pane.getChildren().add(createContentBottom());
		return pane;
	}

	private Pane createContentBottom(){
		Pane pane = new HBox();
		((HBox)pane).setSpacing(15);
		pane.getChildren().add(btnAjouter);
		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                VenteJavaFxFormINSERT frmAddV = new VenteJavaFxFormINSERT();
            	Stage w = new Stage();
            	Stage stage;
            	message.setText("");
            	try {
            		if(bAdd == true){
            			if(VenteJavaFxFormINSERT.messageSelect != null){
            				stage = (Stage)VenteJavaFxFormINSERT.messageSelect.getScene().getWindow();
            				if(!stage.isShowing()){
            					frmAddV.start(w); }
            		}}
	            } catch (Exception e) { System.out.println("Erreur !"); }
            	try {
            		if(bAdd == false){
            			frmAddV.start(w); bAdd = true;
            		}
            	} catch (Exception e) { message.setFill(Color.RED); message.setText("Erreur !"); }
            }
        });
		add.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
                VenteAdd frmAdd = new VenteAdd();
            	Stage w = new Stage();
            	try {
					frmAdd.start(w);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        });
		pane.getChildren().add(add);
		pane.getChildren().add(btnModifier);
		
		btnModifier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	if(venteSelected != null){
            	VenteJavaFxFormUPDATE frmU = new VenteJavaFxFormUPDATE();
            	Stage w = new Stage();
            	Stage stage;
            	try {
	            		if(bUpdate == true){
	            			if(VenteJavaFxFormUPDATE.messageSelect != null){
	            				stage = (Stage)VenteJavaFxFormUPDATE.messageSelect.getScene().getWindow();
	            				if(!stage.isShowing()){
	            					frmU.start(w); }
	            		}}
	            	} catch (Exception e) { System.out.println("Erreur !"); }
	            	try {
	            		if(bUpdate == false){
	            			frmU.start(w); bUpdate = true;
	            		}
	            	} catch (Exception e) { message.setFill(Color.RED); message.setText("Erreur !"); }
	            }else{
		    		message.setFill(Color.RED);
		    		message.setText("Veuillez selectionner une vente !");
		    	}
            }
        });
		
		
		
		pane.getChildren().add(btnAjouterLC);
		btnAjouterLC.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {            	
            	if(venteSelected != null){
            		VenteJavaFxFormINSERTLC frmAddLC = new VenteJavaFxFormINSERTLC();
                	Stage w = new Stage();
                	Stage stage;
                	try {
    	            		if(bAddLC == true){
    	            			if(VenteJavaFxFormINSERTLC.messageSelect != null){
    	            				stage = (Stage)VenteJavaFxFormINSERTLC.messageSelect.getScene().getWindow();
    	            				if(!stage.isShowing()){
    	            					frmAddLC.start(w); }
    	            		}}
    	            	} catch (Exception e) { System.out.println("Erreur !"); }
    	            	try {
    	            		if(bAddLC == false){
    	            			frmAddLC.start(w); bAddLC = true;
    	            		}
	            	} catch (Exception e) { message.setFill(Color.RED); message.setText("Erreur !"); }
	            }else{
		    		message.setFill(Color.RED);
		    		message.setText("Veuillez selectionner une vente !");
		    	}
            }
        });
		pane.getChildren().add(btnSupprimerLC);
		btnSupprimerLC.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	if(venteSelected != null){
	               VenteJavaFxFormDELETELC frmDLC = new VenteJavaFxFormDELETELC();
                	Stage w = new Stage();
                	Stage stage;
                	try {
    	            		if(bDeleteLC == true){
    	            			if(VenteJavaFxFormDELETELC.message != null){
    	            				stage = (Stage)VenteJavaFxFormDELETELC.message.getScene().getWindow();
    	            				if(!stage.isShowing()){
    	            					frmDLC.start(w); }
    	            		}}
    	            	} catch (Exception e) { System.out.println("Erreur !"); }
    	            	try {
    	            		if(bDeleteLC == false){
    	            			frmDLC.start(w); bDeleteLC = true;
    	            		}
	            	} catch (Exception e) { message.setFill(Color.RED); message.setText("Erreur !"); }
            	}else{
            		message.setFill(Color.RED);
            		message.setText("Veuillez selectionner une vente !");
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
					message.setText("Erreur !");
				}
            }
        });
		return pane;
	}

	private void addButtonToTable() {
        TableColumn<Vente, Void> colBtn = new TableColumn("Opérations");
       
        
        
        Callback<TableColumn<Vente, Void>, TableCell<Vente, Void>> cellFactory = new Callback<TableColumn<Vente, Void>, TableCell<Vente, Void>>() {
            @Override
            public TableCell<Vente, Void> call(final TableColumn<Vente, Void> param) {
                final TableCell<Vente, Void> cell = new TableCell<Vente, Void>() {
                	
                	
                	
                    private final Button btn1 = new Button("");

                    {
                    	
                        btn1.setOnAction((ActionEvent event) -> {
                            Vente Vente = getTableView().getItems().get(getIndex());
                            System.out.println("selectedVente: " + Vente.getCodev());
                        });
                        
                    }
                  
                    private final Button btn2 = new Button("");

                    {
                        btn2.setOnAction((ActionEvent event) -> {
                        	Vente Vente = getTableView().getItems().get(getIndex());
                        
                        	try {
            					
            	            		deleteVente(Vente.getCodev());
            	            		message.setFill(Color.LIMEGREEN);
            	            		message.setText("Vente supprimer avec succes.");
            	            	
            				} catch (Exception e) {
            					message.setText("Erreur !");
            				}
                        });
                    }

                    
                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                        	ImageView imgview = new ImageView();
                        	try {
								imgview=Tools.createImageView("photosStock/Delete.png");
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        	imgview.setFitHeight(15);
                        	imgview.setFitWidth(15);
                        	btn2.setGraphic(imgview);
                        	btn2.setPrefHeight(10);
                        	ImageView imgview2 = new ImageView();
                        	try {
								imgview2=Tools.createImageView("photosStock/Affiche.png");
							} catch (FileNotFoundException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
                        	imgview2.setFitHeight(15);
                        	imgview2.setFitWidth(15);
                        	btn1.setGraphic(imgview2);
                        	btn1.setPrefHeight(10);
                        	HBox pane = new HBox(btn2, btn1);
                        	setGraphic(pane);
                            
                        }
                    }
                };
                return cell;
            }
        };
        
        colBtn.setCellFactory(cellFactory);
        

        table.getColumns().add(colBtn);

    }
	
	private  Pane createContentTop(){
		Pane pane = new VBox();
		((VBox)pane).setSpacing(10);
		getVentes();
		table.getColumns().clear();
		TableColumn<Vente, Long> codevCol = new TableColumn<>("Code du vente");
		codevCol.setCellValueFactory(new PropertyValueFactory<>("codev"));
		TableColumn<Vente, String> datevCol = new TableColumn<>("Date du vente");
		datevCol.setCellValueFactory(new  PropertyValueFactory<>("datev"));
		TableColumn<Vente, String> totalvCol = new TableColumn<>("Total du vente");
		totalvCol.setCellValueFactory(new  PropertyValueFactory<>("totalv"));
		TableColumn<Vente, String> clientCol = new TableColumn<>("Client");
		clientCol.setCellValueFactory(new  PropertyValueFactory<>("clientv"));
		
		table.getColumns().addAll(codevCol, datevCol, totalvCol, clientCol);
		clientCol.setPrefWidth(600);
		codevCol.setPrefWidth(30);
		table.setItems(ventes);
		addButtonToTable();
		table.setRowFactory(obj->{
				TableRow<Vente> row = new TableRow<>();
				row.setOnMouseClicked(event->{
					try {
						if (event.getClickCount() == 1 && (!row.isEmpty())) {
							venteSelected = row.getItem();
							message.setFill(Color.LIMEGREEN);
							message.setText("Vente bien selectionné.");
						}
					} catch (Exception e) {
						message.setFill(Color.RED);
						message.setText("Erreur !");
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
		pane.setMinHeight(60);
		((HBox)pane).setAlignment(Pos.CENTER_LEFT);
		Text titre = new Text(" Liste des ventes :");
		titre.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		titre.setFill(Color.WHITE);
		pane.getChildren().add(titre);
		return pane;
	}
	
}
