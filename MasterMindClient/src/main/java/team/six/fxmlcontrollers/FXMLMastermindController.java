package team.six.fxmlcontrollers;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import team.six.mastermind.client.MMClient;
import team.six.mastermind.client.MMClientApp;
import team.six.mastermind.common.MMConfig;
import team.six.mastermind.common.MMPacket;

/**
 * FXML Controller class
 *
 * @author 1437203
 */
public class FXMLMastermindController implements Initializable {
    private MMConfig conf;
    private Stage stage;
    private MMClientApp app;
    
    private MMClient client;
    private int round = 0;
    
    private Button curCol;
    
    @FXML
    private GridPane row1;
    
    @FXML
    private Label out_answer;
    
    @FXML
    private Button out_guess;
    
    @FXML
    private Label out_host;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Loaded
    }
   
    public FXMLMastermindController(){
        this.conf = new MMConfig();
    }
    
    public void setContext(MMClientApp app, Stage stage, MMConfig conf) throws IOException{
        this.app = app;
        this.stage = stage;
        this.conf = conf;
        
        // Setup here because method runs AFTER initialize and would be binded to old object
        out_guess.setText("Start game!");
        out_answer.setText("Answer (Random)");
        out_host.setText(conf.getHostProperty().get());
        
        // Create a new connection
        client = new MMClient(new Socket(conf.getHostProperty().get(), 50000));
    }

    @FXML
    void onChangeColor(ActionEvent event) {
        Button target = (Button) event.getSource();
        target.setText(this.curCol.getText());
        
        // If setting the answer
        if(round == 0){
            // Get current components
            Button comp1 = (Button) row1.getChildren().get(0);
            Button comp2 = (Button) row1.getChildren().get(1);
            Button comp3 = (Button) row1.getChildren().get(2);
            Button comp4 = (Button) row1.getChildren().get(3);
            
            if(Integer.parseInt(comp1.getText()) == 0 &&
               Integer.parseInt(comp2.getText()) == 0 &&
               Integer.parseInt(comp3.getText()) == 0 &&
               Integer.parseInt(comp4.getText()) == 0){
                out_answer.setText("Answer (Random)");
            }else{
                out_answer.setText("Answer (Custom)");
            }
        }
    }

    @FXML
    void onGuess(ActionEvent event) throws IOException {
        if(round == 0){
            // Get current components
            Button comp1 = (Button) row1.getChildren().get(0);
            Button comp2 = (Button) row1.getChildren().get(1);
            Button comp3 = (Button) row1.getChildren().get(2);
            Button comp4 = (Button) row1.getChildren().get(3);
            
            client.sendPacket(new MMPacket((byte) Integer.parseInt(comp1.getText()), (byte) Integer.parseInt(comp2.getText()), (byte) Integer.parseInt(comp3.getText()), (byte) Integer.parseInt(comp4.getText())));
        }
    }

    @FXML
    void onSelectColor(ActionEvent event) {
        this.curCol = (Button) event.getSource();
    }
    
    public void nextRound(){
        round++;
        
        if(round == 1){
            
        }
    }
}
