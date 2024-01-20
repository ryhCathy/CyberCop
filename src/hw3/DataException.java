//name: Raoyi Huang, andrewID: raoyih
package hw3;

import javafx.scene.control.Alert;

public class DataException extends RuntimeException{

    DataException(String msg){
        super(msg);
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Data Error");
        alert.setContentText(msg);
        alert.showAndWait();
    }

}
