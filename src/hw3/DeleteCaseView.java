//name: Raoyi Huang, andrewID: raoyih
package hw3;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.time.LocalDate;

public class DeleteCaseView extends CaseView{

    DeleteCaseView(String header) {
        super(header);
    }

    @Override
    Stage buildView() {
        updateButton.setText("Delete Case");
        //show currentCase in the CaseView panel
        caseDatePicker.setValue(LocalDate.parse(CyberCop.currentCase.getCaseDate()));
        titleTextField.setText(CyberCop.currentCase.getCaseTitle());
        caseTypeTextField.setText(CyberCop.currentCase.getCaseType());
        caseNumberTextField.setText(CyberCop.currentCase.getCaseNumber());
        caseLinkTextField.setText(CyberCop.currentCase.getCaseLink());
        categoryTextField.setText(CyberCop.currentCase.getCaseCategory());
        caseNotesTextArea.setText(CyberCop.currentCase.getCaseNotes());

        Scene scene = new Scene(updateCaseGridPane, CASE_WIDTH, CASE_HEIGHT);
        stage.setScene(scene);
        return stage;
    }
}
