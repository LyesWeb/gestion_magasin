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
import javafx.stage.Stage;
import javafx.util.Callback;
import mTools.Tools;

public class CategorieJavaFxVue extends Application{
	public static boolean bAff = false;
	public static int prdF = 0;
	private static TextField txtintitu = new TextField();
	Button btnAjouter = new Button("Ajouter");
	Button btnModifier = new Button("Modifier");
	Button btnAnnuler = new Button("Annuler");
	public static Text message = new Text("");
	private static Text messageSelect = new Text("");
	private static Text messageAction = new Text("");
	public static CategorieDao cdao = new CategorieDaoImpl();
	public static Categorie categorieSelected = null;
	public static TableView<Categorie> table = new TableView<>();
	public static ObservableList<Categorie> categories = FXCollections.observableArrayList();

	public static void remplire(){
		txtintitu.setText(categorieSelected.getIntitule());
	}
	
	public static void vider(){
		txtintitu.setText("");
		categorieSelected = null;
	}
	
	private static void getCategories(){
		categories.clear();
		Collection<Categorie> cats = cdao.getAll();
		for(Categorie c:cats){
			categories.add(c);
		}
	}

	public static void addCategorie(Categorie c){
		cdao.insert(c);
		categories.clear();
		getCategories();
		table.setItems(categories);
		categorieSelected = null;
	}
	
	public static void updateCategorie(Categorie c){
		cdao.update(c);
		categories.clear();
		getCategories();
		table.setItems(categories);
		categorieSelected = null;
	}
	
	public static void deleteCategorie(long codecateg){
		cdao.delete(codecateg);
		categories.clear();
		getCategories();
		table.setItems(categories);
		categorieSelected = null;
	}
	
	@Override
	public void start(Stage window) throws Exception {
//		window.initModality(Modality.APPLICATION_MODAL);
		window.setWidth(500);
		window.setHeight(512);
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
		pane.setId("CatCenter");
		pane.setPrefWidth(500);
		messageSelect.setFill(Color.RED);
		pane.getChildren().add(messageSelect);
		Text labelCodeCat=new Text("Code:");
		Text labelIntitule=new Text("Intitule:");
		pane.getChildren().add(labelIntitule);
		pane.getChildren().add(txtintitu);
		messageAction.setFill(Color.RED);
		pane.getChildren().add(messageAction);
		pane.getChildren().add(createContentBottom());
		return pane;
	}

	private Pane createContentBottom(){
		Pane pane = new HBox();
		((HBox) pane).setSpacing(5);
		pane.getChildren().add(btnAjouter);
		btnAjouter.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
					String intitule = txtintitu.getText();
	                System.out.println("Categorie ajoute avec succes");
	                Categorie nc = new Categorie(intitule);
	                addCategorie(nc);
	                vider();
	                messageSelect.setText("");
                	messageAction.setFill(Color.LIMEGREEN);
            		messageAction.setText("Categorie bien ajouter.");
				} catch (Exception e) {
					messageSelect.setText("");
					messageAction.setFill(Color.RED);
            		messageAction.setText("Erreur !");
				}
            }
        });
		
		pane.getChildren().add(btnModifier);
		btnModifier.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
					if(categorieSelected != null){
	                	Categorie c = new Categorie();
	                	c.setCodecat(categorieSelected.getCodecat());
	                	c.setIntitule(txtintitu.getText());
	                	updateCategorie(c);
	                	vider();
	                	messageSelect.setText("");
	                	messageAction.setFill(Color.LIMEGREEN);
	            		messageAction.setText("Categorie bien modifier.");
	            	}else{
	            		messageSelect.setFill(Color.RED);
	            		messageSelect.setText("Categorie n'est pas selectionné !");
	            		messageAction.setFill(Color.RED);
	            		messageAction.setText("Veuillez selectionner une categorie !");
	            	}
				} catch (Exception e) {
					messageSelect.setText("");
					messageAction.setFill(Color.RED);
            		messageAction.setText("Erreur !");
				}
            	
            }
        });
		
		
		pane.getChildren().add(btnAnnuler);
		btnAnnuler.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent t) {
            	try {
            		messageSelect.setText("");
            		messageAction.setText("");
            		Stage stage = (Stage) btnAnnuler.getScene().getWindow();
            	    stage.close();
				} catch (Exception e) {
					messageSelect.setText("");
					messageAction.setFill(Color.RED);
            		messageAction.setText("Erreur !");
				}
            }
        });
		return pane;
	}
	
	private void addButtonToTable() {
        TableColumn<Categorie, Void> colBtn = new TableColumn("Opérations");
       
        
        
        Callback<TableColumn<Categorie, Void>, TableCell<Categorie, Void>> cellFactory = new Callback<TableColumn<Categorie, Void>, TableCell<Categorie, Void>>() {
            @Override
            public TableCell<Categorie, Void> call(final TableColumn<Categorie, Void> param) {
                final TableCell<Categorie, Void> cell = new TableCell<Categorie, Void>() {
                    private final Button btn1 = new Button("");

                    {
                        btn1.setOnAction((ActionEvent event) -> {
                        	
                        	Categorie categorieSelected = getTableView().getItems().get(getIndex());
                        	Frmafficherproduits frmprods = new Frmafficherproduits();
                            Stage w = new Stage();
                            Stage stage; 
                            try {
                            	frmprods.start(w);
                            } catch (Exception e) { System.out.println("Erreur !"); }
                        });
                    }
                    private final Button btn2 = new Button("");
                    {
                        btn2.setOnAction((ActionEvent event) -> {
                        	Categorie categorieSelected = getTableView().getItems().get(getIndex());
                        	try {
            					if(categorieSelected != null){
            	        			deleteCategorie(categorieSelected.getCodecat());
            	        			vider();
            	                	messageSelect.setText("");
            	                	messageAction.setFill(Color.LIMEGREEN);
            	            		messageAction.setText("Categorie bien supprimer.");
            	            	}else{
            	            		messageSelect.setFill(Color.RED);
            	            		messageSelect.setText("Categorie n'est pas selectionné !");
            	            		messageAction.setFill(Color.RED);
            	            		messageAction.setText("Veuillez selectionner une categorie !");
            	            	}
            				} catch (Exception e) {
            					messageSelect.setText("");
            					messageAction.setFill(Color.RED);
                        		messageAction.setText("Erreur !");
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
		getCategories();
		table.getColumns().clear();
		TableColumn<Categorie, Long> codecatCol = new TableColumn<>("Code");
		codecatCol.setCellValueFactory(new PropertyValueFactory<>("codecat"));
		TableColumn<Categorie, String> intituleCol = new TableColumn<>("Intitule");
		intituleCol.setCellValueFactory(new  PropertyValueFactory<>("intitule"));
		table.getColumns().addAll(codecatCol, intituleCol);
		table.setItems(categories);
		addButtonToTable();
		if(prdF == 1){
			table.setRowFactory(obj->{
				TableRow<Categorie> row = new TableRow<>();
				row.setOnMouseClicked(event->{
					try {
						if (event.getClickCount() == 2 && (!row.isEmpty())) {
							categorieSelected = row.getItem();
							remplire();
							messageAction.setText("");
							messageSelect.setFill(Color.ORANGE);
							messageSelect.setText("Categorie bien selectionne pour ajouter le nouveau produit.");
							Stage stage = (Stage)table.getScene().getWindow();
			        	    stage.close();
						}
					} catch (Exception e) {
						messageSelect.setText("");
						messageAction.setFill(Color.RED);
	            		messageAction.setText("Erreur !");
					}
				});
				return row;
			});
		}else{
			table.setRowFactory(obj->{
				TableRow<Categorie> row = new TableRow<>();
				row.setOnMouseClicked(event->{
					try {
						if (event.getClickCount() == 1 && (!row.isEmpty())) {
							categorieSelected = row.getItem();
							remplire();
							messageAction.setText("");
							messageSelect.setFill(Color.ORANGE);
		            		messageSelect.setText("Categorie bien selectionné. ");
						}
					} catch (Exception e) {
						messageSelect.setText("");
						messageAction.setFill(Color.RED);
	            		messageAction.setText("Erreur !");
					}
				});
				return row;
			});
		}
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
	
	/*public static void main(String[] args) {
		Application.launch(args);
	}*/
	
}
