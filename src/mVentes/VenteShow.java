package mVentes;

import java.io.File;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
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

public class VenteShow extends Application{
	public Vente venteSelected = VenteJavaFxVue.codeVenteSelected;	
	public static Text comp = new Text();
	public static TableView<LC> LC = new TableView<LC>();
	public ProduitDaoImpl db = new ProduitDaoImpl();
	public VenteDaoImpl vdb = new VenteDaoImpl();
	public long idVente = venteSelected.getCodev();
	public Text date = new Text();
	public Text client = new Text();
	public ClientDaoImpl dbClient = new ClientDaoImpl();
	public Vente newVente;
	public double total = 0;
	Pane paneFooter = new HBox();
    HBox footerBox = new HBox();
	Text totalText = new Text();
	HBox totalBox = new HBox();
	TextField qte = new TextField();
	ComboBox<String> product = new ComboBox<>();
	Button print = new Button("Imprimer");
	public static Text message = new Text("");


	@Override
	public void start(Stage window) throws Exception {
		window.setWidth(1000);
		window.setHeight(700);
		window.setTitle("Affichage Vente n: "+idVente);
		BorderPane brd = new BorderPane();
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
	
	private Pane createVenteTableLC() {
		LC.getItems().clear();
		LC.setPlaceholder(new Label("Pas de lignes de commande dans cette vente !"));
		LC.setSelectionModel(null);
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
		productCol.setMinWidth(200);
		LC.getColumns().addAll(idCol, productCol, prixCol, qteCol);
//        LC.getColumns().add(totoCol);
		fillTable();
		pane.getChildren().addAll(tableBox);
		return pane;
	}

	private void fillTable() {
		int i=1;
		for(LC lc:vdb.getLCs(venteSelected.getCodev())) {
			lc.setCodelc(i);
			LC.getItems().add(lc);
			total += lc.getQt()*lc.getSoustotal();
			i++;
		}
		totalText.setText("Total : "+total+" DH");
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
		
		
		//##### TOTAL #####//
		totalText.setId("totalVente");
		totalBox.getChildren().add(totalText);
		
		paneFooter.getChildren().addAll(totalBox,footerBox);
		return paneFooter;
	}
	
	private Pane createContentCenter(){
		Pane pane = new VBox();
		pane.setId("VenteCenter");
		((VBox) pane).setSpacing(15);
		
		
		pane.getChildren().addAll(createSectionTitle("Informations de la facture :"),createVenteInfo(),createSectionTitle("Lignes de commande :"),createVenteTableLC(),createVenteFooter());
		
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