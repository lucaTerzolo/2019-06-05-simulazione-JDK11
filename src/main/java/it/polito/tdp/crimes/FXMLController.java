/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.crimes;

import java.net.URL;
import java.util.ResourceBundle;

import it.polito.tdp.crimes.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="boxAnno"
    private ComboBox<Integer> boxAnno; // Value injected by FXMLLoader

    @FXML // fx:id="boxMese"
    private ComboBox<Integer> boxMese; // Value injected by FXMLLoader

    @FXML // fx:id="boxGiorno"
    private ComboBox<Integer> boxGiorno; // Value injected by FXMLLoader

    @FXML // fx:id="btnCreaReteCittadina"
    private Button btnCreaReteCittadina; // Value injected by FXMLLoader

    @FXML // fx:id="btnSimula"
    private Button btnSimula; // Value injected by FXMLLoader

    @FXML // fx:id="txtN"
    private TextField txtN; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCreaReteCittadina(ActionEvent event) {
    	Integer anno=this.boxAnno.getValue();
    	if(anno==null)
    		return;
    	String msg=this.model.creaGrafo(anno);
    	this.txtResult.setText(msg);
    	msg=this.model.stampaVicini();
    	this.txtResult.appendText(msg);
    	this.btnSimula.setDisable(false);
    	this.boxGiorno.setDisable(false);
    	this.boxMese.setDisable(false);
    }

    @FXML
    void doSimula(ActionEvent event) {
    	int n=0;
    	Integer giorno=0;
    	Integer mese=0;
    	Integer anno=0;
    	
    	try {
    		n=Integer.parseInt(this.txtN.getText());
    	}catch(NumberFormatException e) {
    		e.printStackTrace();
    	}
    	if(n<=0&&n>10) {
    		return;
    	}
    	giorno=this.boxGiorno.getValue();
    	mese=this.boxMese.getValue();
    	anno=this.boxAnno.getValue();
    	if(giorno==null||mese==null||anno==null)
    		return;
    	int malGestiti=this.model.simulatore(n, giorno, mese, anno);
    	this.txtResult.setText("Simulazione della situazione con: "+n+" agenti\n"
    			+ "Data simulata: "+giorno+"-"+mese+"-"+anno+"\nEventi mal gestiti: "+malGestiti);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxMese != null : "fx:id=\"boxMese\" was not injected: check your FXML file 'Scene.fxml'.";
        assert boxGiorno != null : "fx:id=\"boxGiorno\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCreaReteCittadina != null : "fx:id=\"btnCreaReteCittadina\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnSimula != null : "fx:id=\"btnSimula\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtN != null : "fx:id=\"txtN\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	this.boxAnno.getItems().addAll(this.model.getAnni());
    	this.btnSimula.setDisable(true);
    	for(int i=1;i<=31;i++) {
    		this.boxGiorno.getItems().add(i);
    	}
    	for(int i=1;i<=12;i++) {
    		this.boxMese.getItems().add(i);
    	}
    	this.boxGiorno.setDisable(true);
    	this.boxMese.setDisable(true);
    }
}
