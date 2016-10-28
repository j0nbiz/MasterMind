package team.six.mastermind.common;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * This class provides the configuration for the connection.
 * 
 * @author Jonathan Bizier
 */
public class MMConfig {
    private StringProperty host;

    public StringProperty getHostProperty() {
        return host;
    }
    
    public MMConfig(){
        this.host = new SimpleStringProperty("");
    }
}
