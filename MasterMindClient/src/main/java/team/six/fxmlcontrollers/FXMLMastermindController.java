package team.six.fxmlcontrollers;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
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
    
    private MMPacket lastGuess;
    
    @FXML
    private GridPane row1;
    
    @FXML
    private GridPane row2;

    @FXML
    private GridPane row3;

    @FXML
    private GridPane row4;

    @FXML
    private GridPane row5;

    @FXML
    private GridPane row6;

    @FXML
    private GridPane row7;

    @FXML
    private GridPane row8;

    @FXML
    private GridPane row9;

    @FXML
    private GridPane row10;

    @FXML
    private GridPane row11;
    
    @FXML
    private Button out_guess;
    
    @FXML
    private Label out_host;
    
    @FXML
    private Label out_result;
    
    @FXML
    private Button in_zero;
    
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
        out_guess.setText("Start game! Answer (Random)");
        out_host.setText(conf.getHostProperty().get());
        
        // Hide game result label
        out_result.setVisible(false);
        
        // Hide hint field of row 1
        getRow(1).getChildren().get(4).setVisible(false);
        
        // Create a new connection
        client = new MMClient(new Socket(conf.getHostProperty().get(), 50000));
        
        // Hide rows while setting up answer
        for(int i = 2; i <= 11; i++){
            hideRowContent(getRow(i));
        }
    }

    @FXML
    void onChangeColor(ActionEvent event) {
        Button target = (Button) event.getSource();
        target.setText(this.curCol.getText());
        target.setTextFill(this.curCol.getTextFill());
        
        // If setting the answer
        if(round == 0){
            boolean rand = true;
            for(Button btn: getRowButtons(getRow(1))){
                if(Integer.parseInt(btn.getText()) != 0){
                    rand = false;
                } 
            }
            
            // Set label
            if(rand){
                out_guess.setText("Start game! Answer (Random)"); // Announce random game answer
            }else{
                out_guess.setText("Start game! Answer (Custom)"); // Announce random game answer
            }
        }else{
            // Make sure all fields are set before sending guess on first round (should be set automatically after)
            boolean set = true;            
            for(Button btn: getRowButtons(getRow(1))){
                if(Objects.equals(btn.getText(), "") || Integer.parseInt(btn.getText()) == 0){
                    set = false;
                } 
            }
            
            // Set label
            if(!set){
                out_guess.disableProperty().set(true);
                out_guess.setText("Please assign a number to all fields!"); // Announce random game answer
            }else{
                out_guess.disableProperty().set(false);
                out_guess.setText("Did I get it right?"); // Announce random game answer
            }
        }
    }

    @FXML
    void onGuess(ActionEvent event) throws IOException {
        if(round == 0){
            // Get current components
            MMPacket guess = getRowPacket(getRow(1));
            
            // Send packet and receive hint (discard hint since it is request)
            MMPacket hint = client.sendPacket(guess);
            
            // Reveal hint field
            getRow(1).getChildren().get(4).setVisible(true);
            
            // Change button text to match context
            out_guess.setText("Did I get it right?");
            
            // Hide zero button because we arent supposed to actually use it
            in_zero.setVisible(false);
            
            // Set button text to nothing in order to force client to use correct values           
            for(Button btn: getRowButtons(getRow(1))){
                btn.setText("");
            }
            
            // Prepare to receive first guess
            out_guess.disableProperty().set(true);
            out_guess.setText("Please assign a number to all fields!"); // Announce random game answer
            
            // Increment round
            nextRound();
        }else{
            // Create new guess accoring to row buttons
            MMPacket guess = getRowPacket(getRow(round));
            
            lastGuess = guess; // Set last guess to current to put into next row
            
            // Send packet and receive hint (discard hint since it is request)
            MMPacket hint = client.sendPacket(guess);
            
            // Display server response
            setRowHint(getRow(round), hint);
            
            // Check for win condition
            if(isWin(hint)){
                // Lock current row and display last row with win
                lockRow(getRow(round));
                
                // Reveal last row
                revealRowContent(getRow(11));
                lockRow(getRow(11));
                
                // Set components in row
                setRowGuess(getRow(11), lastGuess); // Set the winning guess on last row
                setRowHint(getRow(11), lastGuess); // Set answer since it 1111
                
                // Show game result label
                gameResult(true);
            }else if(round == 10){
                // Since client lost, request answer from server
                hint = client.sendPacket(new MMPacket((byte) 9, (byte) 9, (byte) 9, (byte) 9));
                
                // Lock current row and display last row with win
                lockRow(getRow(round));
                
                // Reveal last row
                revealRowContent(getRow(11));
                lockRow(getRow(11));
                
                // Set components in row
                setRowGuess(getRow(11), lastGuess); // Set the winning guess on last row
                setRowHint(getRow(11), hint); // Hint should always be 1111 since win condition
                
                // Show game result label
                gameResult(false);
            }else{
                nextRound();
            }
        }
    }
    
    public void gameResult(boolean result){
        out_result.setVisible(true);
        
        if(result){
            // Win
            out_result.setText("W");
            out_result.setTextFill(Color.web("#00ff00"));
        }else{
            // Loss
            out_result.setText("L");
            out_result.setTextFill(Color.web("#ff0000"));
        }
        
        out_guess.disableProperty().set(true);
        out_guess.setText("Game over!");
    }

    @FXML
    void onSelectColor(ActionEvent event) {
        this.curCol = (Button) event.getSource();
    }
    
    public boolean isWin(MMPacket hint){
        for(byte comp: hint.getBytes()){
            if(comp != 1){
                return false;
            }
        }
        return true;
    }
    
    public void hideRowContent(GridPane row){
        for(int i = 0; i < row.getChildren().size(); i++){
            row.getChildren().get(i).setVisible(false);
        }
    }
    
    public void revealRowContent(GridPane row){
        for(int i = 0; i < row.getChildren().size(); i++){
            row.getChildren().get(i).setVisible(true);
        }
    }
    
    public void lockRow(GridPane row){
        for(int i = 0; i < row.getChildren().size() - 1; i++){
            row.getChildren().get(i).disableProperty().set(true);
        }
    }
    
    public MMPacket getRowPacket(GridPane row) throws IOException{
        // Return packet according to row fields
        return new MMPacket((byte) Integer.parseInt(getRowButtons(row)[0].getText()),
                            (byte) Integer.parseInt(getRowButtons(row)[0].getText()),
                            (byte) Integer.parseInt(getRowButtons(row)[0].getText()),
                            (byte) Integer.parseInt(getRowButtons(row)[0].getText()));
    }
    
    public void setRowHint(GridPane row, MMPacket hint){
        // Long fluent syntax because inner grid pane
        Button comp1 = (Button) ((GridPane) row.getChildren().get(4)).getChildren().get(0);
        Button comp2 = (Button) ((GridPane) row.getChildren().get(4)).getChildren().get(1);
        Button comp3 = (Button) ((GridPane) row.getChildren().get(4)).getChildren().get(2);
        Button comp4 = (Button) ((GridPane) row.getChildren().get(4)).getChildren().get(3);

        for(int i = 0; i < 4; i++){
            getRowHintButtons(row)[i].setText(String.valueOf(hint.getBytes()[i]));
        }
        
        /*
        comp1.setText(String.valueOf(hint.getBytes()[0]));
        comp2.setText(String.valueOf(hint.getBytes()[1]));
        comp3.setText(String.valueOf(hint.getBytes()[2]));
        comp4.setText(String.valueOf(hint.getBytes()[3]));*/
    }
    
    public void setRowGuess(GridPane row, MMPacket guess){
        Button comp1 = (Button) row.getChildren().get(0);
        Button comp2 = (Button) row.getChildren().get(1);
        Button comp3 = (Button) row.getChildren().get(2);
        Button comp4 = (Button) row.getChildren().get(3);
        
        for(int i = 0; i < 4; i++){
            getRowButtons(row)[i].setText(String.valueOf(guess.getBytes()[i]));
        }
        
        /*
        comp1.setText(String.valueOf(guess.getBytes()[0]));
        comp2.setText(String.valueOf(guess.getBytes()[1]));
        comp3.setText(String.valueOf(guess.getBytes()[2]));
        comp4.setText(String.valueOf(guess.getBytes()[3]));*/
    }
    
    public GridPane getRow(int id){
        switch(id){
            case 1:
                return row1;
            case 2:
                return row2;
            case 3:
                return row3;
            case 4:
                return row4;
            case 5:
                return row5;
            case 6:
                return row6;
            case 7:
                return row7;
            case 8:
                return row8;
            case 9:
                return row9;
            case 10:
                return row10;
            case 11:
                return row11;
            default:
                return null;
        }
    }
    
    public Button[] getRowButtons(GridPane row){
        // Get buttons in row and add to array
        Button btn1 = (Button) row.getChildren().get(0);
        Button btn2 = (Button) row.getChildren().get(1);
        Button btn3 = (Button) row.getChildren().get(2);
        Button btn4 = (Button) row.getChildren().get(3);
        
        return new Button[]{btn1, btn2, btn3, btn4};
    }
    
    public Button[] getRowHintButtons(GridPane row){
        Button btn1 = (Button) ((GridPane) row.getChildren().get(4)).getChildren().get(0);
        Button btn2 = (Button) ((GridPane) row.getChildren().get(4)).getChildren().get(1);
        Button btn3 = (Button) ((GridPane) row.getChildren().get(4)).getChildren().get(2);
        Button btn4 = (Button) ((GridPane) row.getChildren().get(4)).getChildren().get(3);
        
        return new Button[]{btn1, btn2, btn3, btn4};
    }
    
    public void nextRound(){
        round++;

        /* Prevent user from pressing guess button when game is over
        if(round == 11){
            out_guess.disableProperty().set(true);
            out_guess.setText("Game over!");
        }*/
        
        lockRow(getRow(round - 1));
        revealRowContent(getRow(round));
        
        // Set last guess on current row for easy interaction when playing
        setRowGuess(getRow(round), lastGuess);
    }
    
    @FXML
    void onNewGame(ActionEvent event) throws IOException
    {
        // Send game restart message
        MMPacket restart = client.sendPacket(new MMPacket((byte) 10, (byte) 10, (byte) 10, (byte) 10));
        client.disconnect(); // Unset client
        
        // Prepare scene
        FXMLLoader loader = new FXMLLoader(app.getClass().getResource("/fxml/FXMLMastermind.fxml"));
        loader.setResources(ResourceBundle.getBundle("MessagesBundle"));

        GridPane root = (GridPane) loader.load();

        FXMLMastermindController cont = cont = loader.getController();
        cont.setContext(app, stage, conf);
        loader.setController(cont);

        Scene scene = new Scene(root);

        stage.setScene(scene);
    }
    
    @FXML
    void onEndGame(ActionEvent event) throws IOException
    {
        // Since client lost, request answer from server
        MMPacket hint = client.sendPacket(new MMPacket((byte) 9, (byte) 9, (byte) 9, (byte) 9));
        
        // Set last guess in case this is called on first round and no guess was set
        lastGuess = getRowPacket(getRow(round));
        
        // Lock current row and display last row with win
        lockRow(getRow(round));

        // Reveal last row
        revealRowContent(getRow(11));
        lockRow(getRow(11));

        // Set components in row
        setRowGuess(getRow(11), lastGuess); // Set the winning guess on last row
        setRowHint(getRow(11), hint); // Hint should always be 1111 since win condition

        // Show game result label
        out_result.setVisible(true);
        out_result.setText("L");
        out_result.setTextFill(Color.web("#ff0000"));

        out_guess.disableProperty().set(true);
        out_guess.setText("Game over!");
    }
    
    @FXML
    void onHowTo(ActionEvent event) throws IOException
    {
        Stage howToStage = new Stage();
        
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/FXMLHowTo.fxml"));
        loader.setResources(ResourceBundle.getBundle("MessagesBundle"));
        
        GridPane root = (GridPane) loader.load();
        FXMLHowToController cont = loader.getController();
        cont.setContext(howToStage);
        loader.setController(cont);
        Scene howToScene = new Scene(root);
        
        howToStage.setTitle("How To Play");
        howToStage.resizableProperty().set(false);
        howToStage.setScene(howToScene);
        howToStage.show();
    }
    
    @FXML
    void onAbout(ActionEvent event) throws IOException
    {
        Stage aboutStage = new Stage();
        
        FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/fxml/FXMLAbout.fxml"));
        loader.setResources(ResourceBundle.getBundle("MessagesBundle"));
        
        GridPane root = (GridPane) loader.load();
        FXMLAboutController cont = loader.getController();
        cont.setContext(aboutStage);
        loader.setController(cont);
        Scene aboutScene = new Scene(root);
                
        aboutStage.setTitle("About");
        aboutStage.resizableProperty().set(false);
        aboutStage.setScene(aboutScene);
        aboutStage.show();
    }
}
