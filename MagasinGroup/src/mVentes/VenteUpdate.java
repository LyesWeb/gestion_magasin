package mVentes;

import java.io.File;
import java.io.FileNotFoundException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
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
import javafx.stage.Stage;
import javafx.util.Callback;
import mClient.Client;
import mClient.ClientDaoImpl;
import mProduit.Produit;
import mProduit.ProduitDaoImpl;
import mTools.AutoCompleteComboBoxListener;
import mTools.Tools;

public class VenteUpdate extends Application{
	public Vente venteSelected = VenteJavaFxVue.codeVenteSelected;	
	public static Text comp = new Text();
	public static TableView<LC> LC = new TableView<LC>();
	public ProduitDaoImpl db = new ProduitDaoImpl();
	public VenteDaoImpl vdb = new VenteDaoImpl();
	public LCDaoImpl lcdb = new LCDaoImpl();
	public long idVente = venteSelected.getCodev();
	public DatePicker date = new DatePicker();
	public ComboBox<String> client = new ComboBox<>();
	public ClientDaoImpl dbClient = new ClientDaoImpl();
	public Vente newVente;
	public double total = 0;
	Pane paneFooter = new HBox();
    HBox footerBox = new HBox();
	Text totalText = new Text("Total : "+total+" DH");
	HBox totalBox = new HBox();
	Text messageText = new Text("");
	HBox messageBox = new HBox();
	TextField qte = new TextField();
	ComboBox<String> product = new ComboBox<>();
	int lastIdLc = lcdb.getLastID();
	
	
	@Override
	public void start(Stage window) throws Exception {
		window.setWidth(1000);
		window.setHeight(700);
		window.setTitle("Modifier Vente n: "+idVente);
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
		Text titre = new Text("Modifier Vente n: "+idVente);
		titre.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		titre.setFill(Color.WHITE);
		pane.getChildren().add(titre);
		return pane;
	}
	
	private Pane createVenteInfo() {
		Pane pane = new HBox();
		pane.setStyle("-fx-padding: 0 15 0 15");
		//##### Date #####//
		date.setValue(Tools.convertToLocalDateViaInstant(venteSelected.getDatev()));
		VBox dateBox = new VBox();
		((VBox) dateBox).setSpacing(10);
		dateBox.setMinWidth(333);
		Text dateTxt = new Text("Date de Vente :");
		dateBox.getChildren().addAll(dateTxt,date);
		//##### Date #####//

		//##### ID Vente #####//
		VBox idVenteBox = new VBox();
		((VBox) idVenteBox).setSpacing(10);
		idVenteBox.setMinWidth(333);
		idVenteBox.setAlignment(Pos.BASELINE_LEFT);
		// get last id from table
		Text idVenteTxt = new Text("Vente n : "+String.format("%03d", idVente));
		idVenteTxt.setId("idVente");
		idVenteBox.getChildren().addAll(idVenteTxt);
		//##### ID Vente #####//
		
		//##### Client Vente #####//
		VBox clientBox = new VBox();
		((VBox) clientBox).setSpacing(10);
		clientBox.setMinWidth(300);
		Text clientTxt = new Text("Client :");
		// fill the comboBox from db
		int i = 0;
		int j = 0;
		for(Client c:dbClient.getAll()) {
			if(venteSelected.getClientv().getNom().equals(c.getNom())) {
				j=i;
			}
			client.getItems().add(c.getNom());
			
			i++;
		}
        new AutoCompleteComboBoxListener<>(client);
        client.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue ov, String t, String t1) {
            }    
        });
        client.getSelectionModel().select(j);
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
		// fill the comboBox from db
		for(Produit p:db.getAll()) {			
			product.getItems().add(p.getDesignation());
		}
        new AutoCompleteComboBoxListener<>(product);
        product.valueProperty().addListener(new ChangeListener<String>() {
            @Override public void changed(ObservableValue ov, String t, String t1) {
            }    
        });
        productBox.getChildren().addAll(productTxt,product);
		//##### Product Vente LC #####//
        
		//##### Product Qte LC #####//
        VBox qteBox = new VBox();
		((VBox) qteBox).setSpacing(10);
		qteBox.setMaxWidth(50);
		Text qteTxt = new Text("Qte :");
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
            	if(LC.getItems().size()>0) {
            		int i = 0;
            		boolean isExist = false;
            		for(LC ll:LC.getItems()) {
                		if(ll.getProduitlc().getDesignation().equals(product.getValue())) {
                			LC.getItems().get(i).setQt(LC.getItems().get(i).getQt()+Integer.parseInt(qte.getText()));
                			LC.refresh();
                	    	qte.setText("");
                	    	product.getSelectionModel().clearSelection();
                	    	product.requestFocus();
                			isExist = true;
                			break;
                		}
                		i++;
                	}
            		if(!isExist) {
            			Produit p1 = db.getOne(product.getValue());
                    	LC lc = new LC(lastIdLc, Integer.parseInt(qte.getText()), p1.getPrixVente(), p1, idVente);
                		addLCinTable(lc);
                		lastIdLc++;
            		}
            	}else {
                	Produit p2 = db.getOne(product.getValue());
                	LC lc2 = new LC(lastIdLc, Integer.parseInt(qte.getText()), p2.getPrixVente(), p2, idVente);
                	addLCinTable(lc2);
                	lastIdLc++;
            	}
            }
        });
		
		return pane;
	}
	
	private void addLCinTable(LC lc) {
    	LC.getItems().add(lc);
    	total += lc.getQt()*lc.getSoustotal();
    	updateTotalText();
    	qte.setText("");
    	product.getSelectionModel().clearSelection();
    	product.requestFocus();
	}
	private void fillTable() {
		for(LC lc:vdb.getLCs(venteSelected.getCodev())) {
			LC.getItems().add(lc);
			total += lc.getQt()*lc.getSoustotal();
		}
		totalText.setText("Total : "+total+" DH");
	}
	private void addButtonToTable() {
        TableColumn<LC, Void> colBtn = new TableColumn("Opérations");
        Callback<TableColumn<LC, Void>, TableCell<LC, Void>> cellFactory = new Callback<TableColumn<LC, Void>, TableCell<LC, Void>>() {
            @Override
            public TableCell<LC, Void> call(final TableColumn<LC, Void> param) {
                final TableCell<LC, Void> cell = new TableCell<LC, Void>() {
                    private final Button btn1 = new Button("");
                    {
                        btn1.setOnAction((ActionEvent event) -> {
                        	LC currentLC = (LC) LC.getItems().get(getIndex());
                        	total -= currentLC.getSoustotal()*currentLC.getQt();
                        	updateTotalText();
                        	LC.getItems().remove(getIndex());
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
								e.printStackTrace();
							}
                        	imgview.setFitHeight(15);
                        	imgview.setFitWidth(15);
                        	btn1.setGraphic(imgview);
                        	btn1.setPrefHeight(10);
                        	HBox pane = new HBox(btn1);
                        	setGraphic(pane);   
                        }
                    }
                };
                return cell;
            }
        };
        colBtn.setCellFactory(cellFactory);
        LC.getColumns().add(colBtn);
    }
	
	private Pane createVenteTableLC() {
		LC.getItems().clear();
		LC.setPlaceholder(new Label("Pas de lignes de commande dans le tableau, Ajoutez les !"));
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
		LC.getColumns().clear();
		TableColumn idCol = new TableColumn("ID");
		idCol.setCellValueFactory(new PropertyValueFactory<>("codelc"));

		TableColumn productCol = new TableColumn("Produit");
		productCol.setCellValueFactory(new PropertyValueFactory<>("produitlc"));
		productCol.setPrefWidth(500);
		TableColumn prixCol = new TableColumn("Prix");
		prixCol.setCellValueFactory(new PropertyValueFactory<>("soustotal"));

		TableColumn qteCol = new TableColumn("Qte");
		qteCol.setCellValueFactory(new PropertyValueFactory<>("qt"));

//		TableColumn<Integer, Number> totoCol = new TableColumn<>("toto");
//		totoCol.setCellValueFactory(cellData -> {
//            return new ReadOnlyIntegerWrapper(22);
//        });
		
//		TableColumn actionsCol = new TableColumn("Actions");
		productCol.setMinWidth(200);
		LC.getColumns().addAll(idCol, productCol, prixCol, qteCol);
//        LC.getColumns().add(totoCol);
		fillTable();
		addButtonToTable();
		pane.getChildren().addAll(tableBox);
		return pane;
	}

	
	private Pane createVenteFooter() {
		messageBox.getChildren().add(messageText);
		((HBox) messageBox).setAlignment(Pos.CENTER);
		paneFooter.getChildren().add(messageBox);
		paneFooter.setStyle("-fx-padding: 0 15 0 15");
		((HBox) paneFooter).setSpacing(10);
		((HBox) paneFooter).setAlignment(Pos.BASELINE_RIGHT);
		paneFooter.setMaxWidth(940);
		//##### footer buttons #####//
		((HBox) footerBox).setSpacing(10);
		Button update = new Button("Mise a jour");
		Button cancel = new Button("Annuler");
		footerBox.getChildren().addAll(update,cancel);
		//##### footer buttons #####//
	
		update.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
//            	if(client.getValue()!=null && date.getValue()!=null && LC.getItems().size()>0) {
            		java.sql.Date datev = java.sql.Date.valueOf(date.getValue());
                	newVente = new Vente(idVente, datev,dbClient.getOne(client.getValue()));
                	newVente.setTotalv(total);
                	ArrayList<Integer> ids = new ArrayList<>();
                	for(LC lc:LC.getItems()) {
                		newVente.addLC(lc);
                		int idlc = (int) lc.getCodelc();
                		ids.add(idlc);
                	}
                	lcdb.updateOrInsert(newVente,ids);
                	VenteJavaFxVue.charger();
                	try {
                		Stage stage = (Stage) cancel.getScene().getWindow();
                	    stage.close();
    				} catch (Exception e) {
    					System.out.println(e);
    				}
//            	}else {
//            		messageText.setText("Entrer tout les informations");
//            	}
            }
        });
		cancel.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
            		Stage stage = (Stage) cancel.getScene().getWindow();
            	    stage.close();
				} catch (Exception e) {
				}
            }
        });
		
		//##### TOTAL #####//
		totalText.setId("totalVente");
		totalBox.getChildren().add(totalText);
		
		paneFooter.getChildren().addAll(totalBox,footerBox);
		return paneFooter;
	}
	
	public void updateTotalText() {
		paneFooter.getChildren().clear();
		DecimalFormat df = new DecimalFormat("#.###");

		totalText.setText("Total : "+df.format(total)+" DH");
		paneFooter.getChildren().addAll(totalBox,footerBox);
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
