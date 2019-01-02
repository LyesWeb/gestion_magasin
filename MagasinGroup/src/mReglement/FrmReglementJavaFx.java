package mReglement;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
import mClient.ClientDaoImpl;
import mProduit.ProduitDaoImpl;
import mTools.Tools;
import mVentes.Vente;
import mVentes.VenteDaoImpl;
import mVentes.VenteJavaFxVue;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;

public class FrmReglementJavaFx extends Application{
	
	///////////
	private InputStream input=null;
	private OutputStream output=null;
	private static final int port=3333;
	private Socket socketend2=null;
	///////////
	
	public Vente venteSelected = VenteJavaFxVue.codeVenteSelected;	
	public static Text comp = new Text();
	public ProduitDaoImpl db = new ProduitDaoImpl();
	public VenteDaoImpl vdb = new VenteDaoImpl();
	public long idVente = venteSelected.getCodev();
	public DatePicker date = new DatePicker();
	public Text client = new Text();
	public ClientDaoImpl dbClient = new ClientDaoImpl();
	Pane paneFooter = new HBox();
    HBox footerBox = new HBox();
	Button print = new Button("Imprimer");
	public static Text message = new Text("");
	public static Text messageEspese = new Text("");
	public static Text messageCheque = new Text("");
	public static Text messageRetrait = new Text("");
	public static TableView<Reglement> traits = new TableView<Reglement>();
	public ReglementDao regDao = new ReglementDaoImpl();
	double montantPayer = regDao.getMontantPayerTrait(venteSelected.getCodev());
	double montantRest = venteSelected.getTotalv() - montantPayer;
	public BorderPane brd = new BorderPane();
	public static String type_espece = "espese";
	public static String type_cheque = "cheque";
	public static String type_enligne = "enligne";
	public static String type_traite = "traite";
	public static Reglement reg;
	public Text montantRestText;
	Button btnPayer = new Button("Payer");
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
		date.setValue(Tools.NOW_LOCAL_DATE());
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
	VBox RegEnligne = new VBox();
	VBox RegTrait = new VBox();
	
	private Pane createVenteReg() {
		ImageView imgOk = null;
		try {
			imgOk = Tools.createImageView("photosStock/ok.png");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		imgOk.setFitWidth(100);
		imgOk.setFitHeight(100);
		if(venteSelected.getStat().equals("termine") && regDao.getOneRegOfType(venteSelected.getCodev(), type_espece) != null){
			VBox resPane = new VBox();
			HBox statueBox = new HBox();
			Text statueLabel = new Text("Statue : ");
			Text statue = new Text(" Paye par espece");
			statueBox.getChildren().addAll(statueLabel, statue);
			HBox montantBox = new HBox();
			Text montantLabel = new Text("Montant : ");
			Text montant = new Text(" "+venteSelected.getTotalv()+" DH");
			montant.setStyle("-fx-fill: green;-fx-font-weight: bold;");
			montantBox.getChildren().addAll(montantLabel, montant);
			statueBox.setAlignment(Pos.CENTER);
			montantBox.setAlignment(Pos.CENTER);
			resPane.getChildren().addAll(imgOk, statueBox, montantBox);
			resPane.setStyle("-fx-padding: 10; -fx-font-size: 20px;");
			resPane.setAlignment(Pos.CENTER);
			((VBox)resPane).setSpacing(12);
			return resPane;
		}else if(venteSelected.getStat().equals("termine") && regDao.getOneRegOfType(venteSelected.getCodev(), type_cheque) != null ) {
			VBox resPane = new VBox();
			HBox statueBox = new HBox();
			Text statueLabel = new Text("Statue : ");
			Text statue = new Text(" Paye par cheque");
			statueBox.getChildren().addAll(statueLabel, statue);
			HBox montantBox = new HBox();
			Text montantLabel = new Text("Montant : ");
			Text montant = new Text(" "+venteSelected.getTotalv()+" DH");
			montant.setStyle("-fx-fill: green;-fx-font-weight: bold;");
			montantBox.getChildren().addAll(montantLabel, montant);
			statueBox.setAlignment(Pos.CENTER);
			montantBox.setAlignment(Pos.CENTER);
			resPane.getChildren().addAll(imgOk, statueBox, montantBox);
			resPane.setStyle("-fx-padding: 10; -fx-font-size: 20px;");
			resPane.setAlignment(Pos.CENTER);
			((VBox)resPane).setSpacing(12);
			return resPane;
		}else if(venteSelected.getStat().equals("termine") && regDao.getOneRegOfType(venteSelected.getCodev(), type_enligne) != null ) {
			VBox resPane = new VBox();
			HBox statueBox = new HBox();
			Text statueLabel = new Text("Statue : ");
			Text statue = new Text(" Paye en ligne");
			statueBox.getChildren().addAll(statueLabel, statue);
			HBox montantBox = new HBox();
			Text montantLabel = new Text("Montant : ");
			Text montant = new Text(" "+venteSelected.getTotalv()+" DH");
			montant.setStyle("-fx-fill: green;-fx-font-weight: bold;");
			montantBox.getChildren().addAll(montantLabel, montant);
			statueBox.setAlignment(Pos.CENTER);
			montantBox.setAlignment(Pos.CENTER);
			resPane.getChildren().addAll(imgOk, statueBox, montantBox);
			resPane.setStyle("-fx-padding: 10; -fx-font-size: 20px;");
			resPane.setAlignment(Pos.CENTER);
			((VBox)resPane).setSpacing(12);
			return resPane;
		}else if(regDao.getOneRegOfType(venteSelected.getCodev(), type_traite) != null && venteSelected.getStat().equals("termine")) {
			VBox resPane = new VBox();
			HBox statueBox = new HBox();
			Text statueLabel = new Text("Statue : ");
			Text statue = new Text(" Paye par traites.");
			statueBox.getChildren().addAll(statueLabel, statue);
			HBox montantBox = new HBox();
			Text montantLabel = new Text("Montant : ");
			Text montant = new Text(" "+venteSelected.getTotalv()+" DH");
			montant.setStyle("-fx-fill: green;-fx-font-weight: bold;");
			montantBox.getChildren().addAll(montantLabel, montant);
			statueBox.setAlignment(Pos.CENTER);
			montantBox.setAlignment(Pos.CENTER);
			resPane.getChildren().addAll(imgOk, statueBox, montantBox);
			resPane.setStyle("-fx-padding: 10; -fx-font-size: 20px;");
			resPane.setAlignment(Pos.CENTER);
			((VBox)resPane).setSpacing(12);
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
			RegChoix.getItems().add("Enligne");
			RegChoix.getItems().add("Traite");
			RegEspece();
			RegCheque();
			RegEnligne();
			RegTrait();
			RegChoix.valueProperty().addListener(new ChangeListener<String>() {
		        @Override public void changed(ObservableValue ov, String t, String t1) {
		            if(t1.equals("Espece")) {
//		            	btnPayer.setVisible(true);
		            	System.out.println("non p espese !");
		            	//System.out.println(venteSelected.getCodev()+"");
		        		//System.out.println(regDao.getOne(venteSelected.getCodev(), type_espece).getId()+"");
		            	paneReg.getChildren().removeAll(RegCheque, RegEnligne, RegTrait);
		            	paneReg.getChildren().add(RegEspece);
		            }else if(t1.equals("Cheque")) {
//		            	btnPayer.setVisible(true);
		            	paneReg.getChildren().removeAll(RegEspece, RegEnligne, RegTrait);
		            	paneReg.getChildren().add(RegCheque);
		            }else if(t1.equals("Enligne")) {
//		            	btnPayer.setVisible(true);
		            	paneReg.getChildren().removeAll(RegEspece, RegCheque, RegTrait);
		            	paneReg.getChildren().add(RegEnligne);
		            }else {
//		            	btnPayer.setVisible(false);
		            	paneReg.getChildren().removeAll(RegCheque, RegEnligne, RegEspece);
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
		messageEspese.setText("");
		((VBox)RegEspece).setSpacing(10);
		RegEspece.setStyle("-fx-padding: 0 0 0 10;");
		HBox montantBox = new HBox();
		Text labelMontant = new Text(" - Montant :   ");
		labelMontant.setId("regLabel");
		Text montantText = new Text(""+venteSelected.getTotalv()+" DH");
		montantText.setFont(Font.font("open snas", FontWeight.BOLD, 18));
		montantText.setFill(Color.GREEN);
		montantBox.getChildren().addAll(labelMontant, montantText);
		btnPayer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
            public void handle(ActionEvent t) {
				btnPayer.setVisible(true);
				Reglement reg = new Reglement();
				reg.setMontant(venteSelected.getTotalv());
				reg.setType("espese");
				reg.setVente(venteSelected);
				regDao.insert(reg);
				venteSelected.setStat("termine");
				vdb.update(venteSelected);
				VenteJavaFxVue.charger();
				messageEspese.setFill(Color.GREEN);
				messageEspese.setText("Paiement en espese bien effectue.");
				btnPayer.setVisible(false);
			}
		});
		RegEspece.getChildren().addAll(montantBox, btnPayer, messageEspese);
	}
	
	private void RegCheque() {
		messageCheque.setText("");
		((VBox)RegCheque).setSpacing(10);
		RegCheque.setStyle("-fx-padding: 0 0 0 10;");
		HBox montantBox = new HBox();
		Text labelMontant = new Text(" - Montant : ");
		labelMontant.setId("regLabel");
		Text montantText = new Text(""+venteSelected.getTotalv()+" DH");
		montantText.setFont(Font.font("open snas", FontWeight.BOLD, 18));
		montantText.setFill(Color.GREEN);
		montantBox.getChildren().addAll(labelMontant, montantText);
		////
		HBox TitulaireBox = new HBox();
		Text labelTitulaire = new Text(" - Titulaire de cheque : ");
		labelTitulaire.setId("regLabel");
		TextField TitulaireText = new TextField();
		TitulaireBox.getChildren().addAll(labelTitulaire, TitulaireText);
		////
		HBox dateChequeBox = new HBox();
		Text labelDateCheque = new Text(" - Date de cheque : ");
		labelDateCheque.setId("regLabel");
		DatePicker dateCheque = new DatePicker();
		dateChequeBox.getChildren().addAll(labelDateCheque, dateCheque);
		////
		Button btnPayer = new Button("Payer");
		btnPayer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
            public void handle(ActionEvent t) {
				btnPayer.setVisible(true);
				java.sql.Date dcheq = java.sql.Date.valueOf(dateCheque.getValue());
				Reglement reg = new Reglement();
				reg.setMontant(venteSelected.getTotalv());
				reg.setType("cheque");
				reg.setVente(venteSelected);
				reg.setTitulaire(TitulaireText.getText());
				reg.setDatecheque((Date)dcheq);
				reg.setType("cheque");
				regDao.insert(reg);
				venteSelected.setStat("termine");
				vdb.update(venteSelected);
				VenteJavaFxVue.charger();
				messageCheque.setFill(Color.GREEN);
				messageCheque.setText("Paiement par cheque bien effectue.");
				btnPayer.setVisible(false);
			}
		});
		RegCheque.getChildren().addAll(montantBox, TitulaireBox, dateChequeBox, btnPayer, messageCheque);
	}
	
	////////////
	public void sendInfoCompte(Compte c){
    	try {
//    		PrintWriter pw=new PrintWriter(output);
//    		pw.write("ca march");
//    		pw.flush();
//    		pw.close();
    		
    		ObjectOutputStream outputo=new ObjectOutputStream(output);
    		outputo.writeObject(c);
    		outputo.flush();
    		outputo.close();
		} catch (Exception exp) {
			System.out.println(exp);
		}
    
    }

	public void getInfoCompte(){
		try {
	//		BufferedReader bfread=new BufferedReader(new InputStreamReader(input));
	//		System.out.println(bfread.readLine());
			ObjectInputStream inputo=new ObjectInputStream(input);  
			Compte compte=(Compte)inputo.readObject();
		System.out.println(compte.getNumber()+"");
		} catch (Exception exp) {
			System.out.println(exp);
		}
		
		
	}
	////////////
	
private void RegEnligne() {
		////
		((VBox)RegEnligne).setSpacing(10);
		RegEnligne.setStyle("-fx-padding: 0 0 0 10;");
		HBox montantBox = new HBox();
		Text labelMontant = new Text(" -  Le montant                  :   ");
		labelMontant.setId("regLabel");
		Text montantText = new Text(""+venteSelected.getTotalv()+" DH");
		montantText.setFont(Font.font("open snas", FontWeight.BOLD, 18));
		montantText.setFill(Color.GREEN);
		montantBox.getChildren().addAll(labelMontant, montantText);
		////
		HBox NumberCard = new HBox();
		Text labelNumberCard = new Text(" -  Number Card    :   ");
		labelNumberCard.setId("regLabel");
		TextField NumberText = new TextField();
		NumberCard.getChildren().addAll(labelNumberCard, NumberText);
		////
		HBox SortCode = new HBox();
		Text labelSortCode = new Text(" -  Sort Code   :   ");
		labelSortCode.setId("regLabel");
		TextField SortCodeText = new TextField();
		SortCodeText.setPrefWidth(50);
		SortCode.getChildren().addAll(labelSortCode, SortCodeText);
		////
		HBox dateEnLigneBox = new HBox();
		Text labelDateEnLigne = new Text(" -  Date de payment          :   ");
		labelDateEnLigne.setId("regLabel");
		DatePicker datePaiment = new DatePicker();
		dateEnLigneBox.getChildren().addAll(labelDateEnLigne, datePaiment);
		////
		
		Button btnPayer = new Button("Payer");
		btnPayer.setOnAction(new EventHandler<ActionEvent>() {
			@Override
            public void handle(ActionEvent t) {
				try {
					System.out.println("demande de connexion au server");
					socketend2=new Socket("127.0.0.1", port);
					System.out.println("connexion établie avec le serveur");
					input=socketend2.getInputStream();
					output=socketend2.getOutputStream();
				} catch (Exception exp) {
				System.out.println(exp);
				}
				try {
					Date dd = new Date();
					Compte C= new Compte(Integer.parseInt(NumberText.getText()), Integer.parseInt(SortCodeText.getText()),dd);
					sendInfoCompte(C);
					getInfoCompte();
				} catch (Exception e) {
					System.out.println(e);
				}
				
        		
			}
			
		});
		RegEnligne.getChildren().addAll(montantBox, NumberCard ,SortCode, dateEnLigneBox , btnPayer);
	}
	
	static boolean bMt = false;
	private void RegTrait() {
		message.setText("");
		messageRetrait.setFont(Font.font("open snas", FontWeight.NORMAL, 12));
		messageRetrait.setFill(Color.RED);
		
		((VBox)RegTrait).setSpacing(10);
		RegTrait.setStyle("-fx-padding: 0 0 0 10;");
		HBox montantBox = new HBox();
		Text labelMontant = new Text(" - Montant : ");
		labelMontant.setId("regLabel");
		TextField montantText = new TextField("00");
		montantText.textProperty().addListener((observable, oldValue, newValue) -> {
		    try {
		    	if(Double.parseDouble(newValue) > venteSelected.getTotalv()){
		    		bMt = false;
		    		messageRetrait.setText("NOT OK");
		    		messageRetrait.setFill(Color.RED);
			    }else{
			    	bMt = true;
			    	messageRetrait.setText("OK");
			    	messageRetrait.setFill(Color.GREEN);
			    }
			} catch (Exception e) {
				messageRetrait.setText("Erreur !");
				messageRetrait.setFill(Color.RED);
			}
		});

		montantBox.getChildren().addAll(labelMontant, montantText);
		
		////
		HBox ModeBox = new HBox();
		Text labelMode = new Text(" - Mode : ");
		labelMode.setId("regLabel");
		RadioButton m1 = new RadioButton(" Espece ");
		RadioButton m2 = new RadioButton(" Cheque ");
		VBox ModeBoxRight = new VBox();
		((VBox)ModeBoxRight).setSpacing(8);
		ModeBoxRight.getChildren().addAll(m1, m2);
		ModeBox.getChildren().addAll(labelMode, ModeBoxRight);
		///
		HBox montantRestBox = new HBox();
		Text labelMontantRest = new Text(" - Montant Resté : ");
		labelMontantRest.setId("regLabel");
		montantRestText = new Text(montantRest+" DH");
		montantRestText.setFont(Font.font("open snas", FontWeight.BOLD, 18));
		montantRestText.setFill(Color.GREEN);
		montantRestBox.getChildren().addAll(labelMontantRest, montantRestText);
		///
		HBox tableTraitsBox = new HBox();
		tableTraitsBox.getChildren().add(traits);
		tableTraitsBox.setMinHeight(80);
		tableTraitsBox.setMinWidth(500);
		tableTraitsBox.setMaxWidth(500);
		tableTraitsBox.setPrefWidth(500);
		traits.getColumns().clear();
		TableColumn mantantCol = new TableColumn("Mantant");
		mantantCol.setCellValueFactory(new PropertyValueFactory<>("montant"));
		TableColumn dateCol = new TableColumn("Date");
		dateCol.setCellValueFactory(new PropertyValueFactory<>("datepp"));
		TableColumn modeCol = new TableColumn("Mode");
		modeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
		traits.getColumns().addAll(mantantCol, dateCol, modeCol);
		fillTable();
		///
		HBox dateChequeBox = new HBox();
		Text labelDateCheque = new Text(" - Date de cheque : ");
		labelDateCheque.setId("regLabel");
		DatePicker dateCheque = new DatePicker();
		dateChequeBox.getChildren().addAll(labelDateCheque, dateCheque);
		////
		HBox ppBox = new HBox(); Text ppLBL = new Text(" - Date Paiement Prevue : ");
		ppLBL.setId("regLabel"); ppBox.getChildren().addAll(ppLBL);
		HBox datePPBox = new HBox(); DatePicker datePP = new DatePicker(); 
		datePP.setValue(Tools.NOW_LOCAL_DATE());
		datePPBox.getChildren().addAll(datePP);
		////
		HBox peBox = new HBox(); Text peLBL = new Text(" - Date Paiement Effective : ");
		peLBL.setId("regLabel"); peBox.getChildren().addAll(peLBL);
		HBox datePEBox = new HBox(); DatePicker datePE = new DatePicker(); 
		datePE.setValue(Tools.NOW_LOCAL_DATE());
		datePEBox.getChildren().addAll(datePE);
		////
		Button btnPayerRetrait = new Button("Payer");
		Button btnAddTrait = new Button("Ajouter");
		btnAddTrait.setOnAction(new EventHandler<ActionEvent>() {
			@Override
            public void handle(ActionEvent t) {
				Reglement reg = new Reglement();
				reg.setMontant(Double.parseDouble(montantText.getText()));
				if(m1.isSelected()) {
					reg.setType("espese");
				}else if(m2.isSelected()){
					reg.setType("cheque");
				}
				reg.setVente(venteSelected);
				java.sql.Date d = java.sql.Date.valueOf(datePP.getValue());
				reg.setDatepp(d);
				reg.setIsTrait(1);
				traits.getItems().add(reg);
				regDao.insert(reg);
				montantRestText.setText((venteSelected.getTotalv()-regDao.getMontantPayerTrait(venteSelected.getCodev()))+" DH");
				messageEspese.setFill(Color.GREEN);
				messageEspese.setText("Trait added");
			}
		});
		HBox regTraitHbox = new HBox();
		VBox regTraitVbox = new VBox();
		((HBox)regTraitHbox).setSpacing(20);
		((VBox)regTraitVbox).setSpacing(20);
		regTraitVbox.getChildren().addAll(montantBox, ppBox, datePP, peBox, datePE, ModeBox, montantRestBox);
		regTraitHbox.getChildren().addAll(regTraitVbox,tableTraitsBox);
		RegTrait.getChildren().addAll(regTraitHbox, btnAddTrait, messageRetrait, btnPayerRetrait);
	}
	private void fillTable() {
		traits.getItems().clear();
		for(Reglement r:regDao.getAllofType(venteSelected.getCodev(),1)) {
			traits.getItems().add(r);
		}
	}
	private Pane createContentCenter(){
		Pane pane = new VBox();
		pane.setId("VenteCenter");
		((VBox) pane).setSpacing(15);
		
		pane.getChildren().addAll(createSectionTitle("Reglement de la facture :"), createVenteInfo(), createSectionTitle("Detail de reglement de la facture :"), createVenteReg(), createVenteFooter());
		
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