//name: Raoyi Huang, andrewID: raoyih
package hw2;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;

public class AddCaseView extends CaseView{
    AddCaseView(String header) {
        super(header);
    }

    @Override
    Stage buildView() {
        //set default date the current date
        caseDatePicker.setValue(LocalDate.now());
        updateButton.setText("Add Case");
        Scene scene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);
        stage.setScene(scene);
        return stage;
    }
}
