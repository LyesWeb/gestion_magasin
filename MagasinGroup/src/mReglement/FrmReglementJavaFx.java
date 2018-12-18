package mReglement;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
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
import mClient.ClientDaoImpl;
import mProduit.ProduitDaoImpl;
import mVentes.Vente;
import mVentes.VenteDaoImpl;
import mVentes.VenteJavaFxVue;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;


public class FrmReglementJavaFx extends Application{
	public Vente venteSelected = VenteJavaFxVue.codeVenteSelected;	
	public static Text comp = new Text();
	public ProduitDaoImpl db = new ProduitDaoImpl();
	public VenteDaoImpl vdb = new VenteDaoImpl();
	public long idVente = venteSelected.getCodev();
	public Text date = new Text();
	public Text client = new Text();
	public ClientDaoImpl dbClient = new ClientDaoImpl();
	Pane paneFooter = new HBox();
    HBox footerBox = new HBox();
	Button print = new Button("Imprimer");
	public static Text message = new Text("");
	public static TableView traits = new TableView();
	public EspeceDaoImpl espDao = new EspeceDaoImpl();
	public Espece venteRegEsp = espDao.getOne(venteSelected.getCodev());
	public BorderPane brd = new BorderPane();
	@Override
	public void start(Stage window) throws Exception {
		window.setWidth(1000);
		window.setHeight(700);
		window.setTitle("Reglement Vente n: "+idVente);
		
		brd.setTop(createContentTopTitle());
		brd.setCenter(createContentCenter());
		Scene scene = new Scene(brd, 200, 300, Color.WHITE);
		File f = new File("css/style.css");
		scene.getStylesheets().clear();
		scene.getStylesheets().add("file:///" + f.getAbsolutePath().replace("\\", "/"));
		
		print.setOnAction(new EventHandler<ActionEvent>() {
	        @Override
	        public void handle(ActionEvent event) {
	            
	        }
	    });
		window.setScene(scene);
		window.show();
	}
	
	private Pane createContentTopTitle(){
		Pane pane = new HBox();
		pane.setId("headTitle");
		pane.setMinHeight(80);
		((HBox)pane).setAlignment(Pos.CENTER_LEFT);
		Text titre = new Text(" Vente n: "+idVente);
		titre.setFont(Font.font("Calibri", FontWeight.BOLD, 20));
		titre.setFill(Color.WHITE);
		pane.getChildren().add(titre);
		return pane;
	}
	
	private Pane createVenteInfo() {
		Pane pane = new HBox();
		pane.setStyle("-fx-padding: 0 15 0 15");
		//##### Date #####//
		date.setText(venteSelected.getDatev().toString());
		VBox dateBox = new VBox();
		((VBox) dateBox).setSpacing(10);
		dateBox.setMinWidth(333);
		Text dateTxt = new Text("Date de Vente :");
		date.setFont(Font.font("open sans", FontWeight.BOLD, 17));
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
		client.setText(venteSelected.getClientv().getNomPrenom());
		client.setFont(Font.font("open sans", FontWeight.BOLD, 17));
		clientBox.getChildren().addAll(clientTxt,client);
		//##### Client Vente #####//
		
		pane.getChildren().addAll(dateBox,idVenteBox,clientBox);
		return pane;
	}
	
	private Pane createVenteFooter() {
		paneFooter.setStyle("-fx-padding: 0 15 0 15");
		((HBox) paneFooter).setSpacing(10);
		((HBox) paneFooter).setAlignment(Pos.BASELINE_RIGHT);
		paneFooter.setMaxWidth(940);
		//##### footer buttons #####//
		((HBox) footerBox).setSpacing(10);
		footerBox.getChildren().addAll(print);
		//##### footer buttons #####//
		
		paneFooter.getChildren().addAll(footerBox);
		return paneFooter;
	}
	ComboBox<String> RegChoix = new ComboBox();
	VBox RegCheque = new VBox();
	VBox RegEspece = new VBox();
	VBox RegTrait = new VBox();
	private Pane createVenteReg() {
		venteRegEsp = espDao.getOne(venteSelected.getCodev());
		if(venteRegEsp != null) {
			VBox resPane = new VBox();
			///
			HBox statueBox = new HBox();
			Text statueLabel = new Text("Statue : ");
			Text statue = new Text(" Paye par espece");
			statueBox.getChildren().addAll(statueLabel, statue);
			///
			HBox montantBox = new HBox();
			Text montantLabel = new Text("Montant : ");
			Text montant = new Text(" "+venteSelected.getTotalv()+" DH");
			montantBox.getChildren().addAll(montantLabel, montant);
			///
//			HBox dateBox = new HBox();
//			Text dateLabel = new Text("Date de paiment : ");
//			String dateString = venteRegEsp.getDateEspece().toString();
//			SimpleDateFormat sdfr = new SimpleDateFormat("yyyy/MMM/mm");
//			try{
//				dateString = venteRegEsp.getDateEspece().toString();
//			   }catch (Exception ex ){
//				System.out.println(ex);
//			   }
//			Text dt = new Text();
//			dateBox.getChildren().addAll(dateLabel, dt);
			///
			resPane.getChildren().addAll(statueBox, montantBox);
			return resPane;
		}else {
		VBox paneReg = new VBox();
		paneReg.setStyle("-fx-padding: 0 15 0 15");
		HBox choixBox = new HBox();
		choixBox.setStyle("-fx-padding: 0 0 20 0;");
		Text labelRegChoix = new Text("Type de Reglement   :   ");
		labelRegChoix.setFont(Font.font("open snas", FontWeight.BOLD, 14));
		labelRegChoix.setFill(Color.DARKCYAN);
		RegChoix.getItems().add("Espece");
		RegChoix.getItems().add("Cheque");
		RegChoix.getItems().add("Trait");
		RegEspece();RegCheque();RegTrait();
		RegChoix.valueProperty().addListener(new ChangeListener<String>() {
	        @Override public void changed(ObservableValue ov, String t, String t1) {
	            if(t1.equals("Espece")) {
	            	paneReg.getChildren().removeAll(RegCheque,RegTrait);
	            	paneReg.getChildren().add(RegEspece);
	            }else if(t1.equals("Cheque")) {
	            	paneReg.getChildren().removeAll(RegEspece,RegTrait);
	            	paneReg.getChildren().add(RegCheque);
	            }else {
	            	paneReg.getChildren().removeAll(RegCheque,RegEspece);
	            	paneReg.getChildren().add(RegTrait);
	            }
	        }    
	    });
		choixBox.getChildren().addAll(labelRegChoix, RegChoix);
		paneReg.getChildren().addAll(choixBox);
		
		return paneReg;
		}
	}
	private void RegEspece() {
		////
		((VBox)RegEspece).setSpacing(10);
		RegEspece.setStyle("-fx-padding: 0 0 0 10;");
		HBox montantBox = new HBox();
		Text labelMontant = new Text(" -  Le montant                  :   ");
		labelMontant.setId("regLabel");
		Text montantText = new Text(""+venteSelected.getTotalv()+" DH");
		montantText.setFont(Font.font("open snas", FontWeight.BOLD, 18));
		montantText.setFill(Color.GREEN);
		montantBox.getChildren().addAll(labelMontant, montantText);
		////
		Button btnPayer = new Button("Payer");
		btnPayer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
            public void handle(ActionEvent t) {
				Espece esp = new Espece(venteSelected, new Date(), venteSelected.getTotalv());
				espDao.insert(esp);
				venteSelected.setStat(1);
				vdb.update(venteSelected);
				VenteJavaFxVue.charger();
			}
		});
		RegEspece.getChildren().addAll(montantBox, btnPayer);
		////
	}
	private void RegCheque() {
		////
		((VBox)RegCheque).setSpacing(10);
		RegCheque.setStyle("-fx-padding: 0 0 0 10;");
		HBox montantBox = new HBox();
		Text labelMontant = new Text(" -  Le montant                  :   ");
		labelMontant.setId("regLabel");
		Text montantText = new Text(""+venteSelected.getTotalv()+" DH");
		montantText.setFont(Font.font("open snas", FontWeight.BOLD, 18));
		montantText.setFill(Color.GREEN);
		montantBox.getChildren().addAll(labelMontant, montantText);
		////
		HBox TitulaireBox = new HBox();
		Text labelTitulaire = new Text(" -  Titulaire de cheque    :   ");
		labelTitulaire.setId("regLabel");
		TextField TitulaireText = new TextField();
		TitulaireBox.getChildren().addAll(labelTitulaire, TitulaireText);
		////
		HBox dateChequeBox = new HBox();
		Text labelDateCheque = new Text(" -  Date de cheque          :   ");
		labelDateCheque.setId("regLabel");
		DatePicker dateCheque = new DatePicker();
		dateChequeBox.getChildren().addAll(labelDateCheque, dateCheque);
		////
		Button btnPayer = new Button("Payer");
		RegCheque.getChildren().addAll(montantBox, TitulaireBox, dateChequeBox, btnPayer);
	}
	private void RegTrait() {
		((VBox)RegTrait).setSpacing(10);
		RegTrait.setStyle("-fx-padding: 0 0 0 10;");
		HBox montantBox = new HBox();
		Text labelMontant = new Text(" -  Le montant             :   ");
		labelMontant.setId("regLabel");
		TextField montantText = new TextField(""+venteSelected.getTotalv());
		montantBox.getChildren().addAll(labelMontant, montantText);
		////
		HBox ModeBox = new HBox();
		Text labelMode = new Text(" -  Mode                       :   ");
		labelMode.setId("regLabel");
		RadioButton m1 = new RadioButton("   Espese");
		RadioButton m2 = new RadioButton("   Cheque");
		VBox ModeBoxRight = new VBox();
		((VBox)ModeBoxRight).setSpacing(8);
		ModeBoxRight.getChildren().addAll(m1, m2);
		ModeBox.getChildren().addAll(labelMode, ModeBoxRight);
		///
		HBox montantRestBox = new HBox();
		Text labelMontantRest = new Text(" -  Montant Rester      :   ");
		labelMontantRest.setId("regLabel");
		Text montantRestText = new Text(""+venteSelected.getTotalv()+" DH");
		montantRestText.setFont(Font.font("open snas", FontWeight.BOLD, 18));
		montantRestText.setFill(Color.GREEN);
		montantRestBox.getChildren().addAll(labelMontantRest, montantRestText);
		///
		HBox tableTraitsBox = new HBox();
		tableTraitsBox.getChildren().add(traits);
		traits.getColumns().clear();
		TableColumn idTraitCol = new TableColumn("ID");
		idTraitCol.setPrefWidth(20);
//		idTraitCol.setCellValueFactory(new PropertyValueFactory<>("codelc"));
		TableColumn mantantCol = new TableColumn("Mantant");
//		mantantCol.setCellValueFactory(new PropertyValueFactory<>("produitlc"));
		TableColumn dateCol = new TableColumn("Date");
//		dateCol.setCellValueFactory(new PropertyValueFactory<>("soustotal"));
		TableColumn modeCol = new TableColumn("Mode");
//		modeCol.setCellValueFactory(new PropertyValueFactory<>("qt"));
		traits.getColumns().addAll(idTraitCol, mantantCol, dateCol, modeCol);
		///
		Button btnAdd = new Button("Ajouter");
		RegTrait.getChildren().addAll(montantBox, ModeBox, montantRestBox, btnAdd, tableTraitsBox);
	}
	private Pane createContentCenter(){
		Pane pane = new VBox();
		pane.setId("VenteCenter");
		((VBox) pane).setSpacing(15);
		
		pane.getChildren().addAll(createSectionTitle("Reglement de la facture :"),createVenteInfo(), createSectionTitle("Detail de reglement de la facture :"), createVenteReg(),createVenteFooter());
		
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