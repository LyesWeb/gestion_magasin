package mTools;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class Tools {

	public static ImageView createImageView(String path) throws FileNotFoundException{
		 Image img = new Image(new FileInputStream(path));
	     // simple displays ImageView the image as is
	     ImageView imgview = new ImageView();
	     imgview.setImage(img);
	  return imgview;
	}
	public static Text createText(String texte, Color color){
		Text txt=new Text(texte);
		txt.setFont(Font.font("Verdana", FontWeight.NORMAL, 18));
		txt.setFill(color);
		//txt.setStroke(Color.web("#7080A0"));
		return txt;
	}
	public static Text createText(String texte, Color color,int taille){
		Text txt=new Text(texte);
		txt.setFont(Font.font("Verdana", FontWeight.NORMAL, taille));
		txt.setFill(color);
		//txt.setStroke(Color.web("#7080A0"));
		return txt;
	}
	public static void createText(Text text,String texte, Color color,int taille, FontWeight f){
		text.setText(texte);
		text.setFont(Font.font("Verdana", f, taille));
		text.setFill(color);
		//txt.setStroke(Color.web("#7080A0"));
	}
	public static final LocalDate NOW_LOCAL_DATE (){
        String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        LocalDate localDate = LocalDate.parse(date , formatter);
        return localDate;
    }
	public static Date fromLocalDate(LocalDate date) {
	    Instant instant = date.atStartOfDay().atZone(ZoneId.systemDefault())
	        .toInstant();
	    return Date.from(instant);
	}
	public static LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
		return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
	}
	public static HBox labelleProduit(String labelle) {
		HBox l = new HBox();
		l.setAlignment(Pos.CENTER_LEFT);
		Text labelCode = new Text(labelle);
		labelCode.setId("produitLabelle");
		try {
			l.getChildren().addAll(createImageView("photosStock/arrow_right.png"),labelCode);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return l;
	}
}
