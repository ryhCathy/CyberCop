//name: Raoyi Huang, andrewID: raoyih
package hw3;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class CyberCop extends Application{

	public static final String DEFAULT_PATH = System.getProperty("user.dir") + "/data"; //folder name where data files are stored
	public static final String DEFAULT_HTML = "/CyberCop.html"; //local HTML
	public static final String APP_TITLE = "Cyber Cop"; //displayed on top of app

	CCView ccView = new CCView();
	CCModel ccModel = new CCModel();

	CaseView caseView; //UI for Add/Modify/Delete menu option

	GridPane cyberCopRoot;
	Stage stage;

	static Case currentCase; //points to the case selected in TableView.


	public static void main(String[] args) {
		launch(args);
	}

	/** start the application and show the opening scene */
	@Override
	public void start(Stage primaryStage) throws Exception {
		stage = primaryStage;
		primaryStage.setTitle(APP_TITLE);
		cyberCopRoot = ccView.setupScreen();  
		setupBindings();
		Scene scene = new Scene(cyberCopRoot, ccView.ccWidth, ccView.ccHeight);
		primaryStage.setScene(scene);
		primaryStage.setMaximized(true);
		ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
		primaryStage.show();
	}

	/** setupBindings() binds all GUI components to their handlers.
	 * It also binds disableProperty of menu items and text-fields 
	 * with ccView.isFileOpen so that they are enabled as needed
	 */
	void setupBindings() {
		//write your code here
		//sets yearComboBox by yearList, caseTableView by caseList
		ccView.yearComboBox.setItems(ccModel.yearList);
		ccView.caseTableView.setItems(ccModel.caseList);
		//let caseTableView listen to the current selection to update currentCase
		ccView.caseTableView.getSelectionModel().selectedItemProperty().addListener(observable -> {
			if(ccView.caseTableView.getSelectionModel().getSelectedIndex() >= 0){
				currentCase = ccView.caseTableView.getSelectionModel().getSelectedItem();
				displayCurrCaseHelper();
			}
		});

		//binds all GUI components (MenuItem/Button) to their handlers
		ccView.openFileMenuItem.setOnAction(new OpenMenuItemHandler());
		ccView.saveFileMenuItem.setOnAction(new SaveMenuItemHandler());
		ccView.closeFileMenuItem.setOnAction(new CloseMenuItemHandler());
		ccView.exitMenuItem.setOnAction(actionEvent -> {Platform.exit();});
		ccView.searchButton.setOnAction(new SearchButtonHandler());
		ccView.clearButton.setOnAction(new ClearButtonHandler());

		ccView.addCaseMenuItem.setOnAction(new CaseMeunuItemHandler());
		ccView.modifyCaseMenuItem.setOnAction(new CaseMeunuItemHandler());
		ccView.deleteCaseMenuItem.setOnAction(new CaseMeunuItemHandler());
		ccView.caseCountChartMenuItem.setOnAction(new CaseCountChartMenuItemHandler());

		//binds disableProperty of menu items and text-fields with ccView.isFileOpen
		ccView.openFileMenuItem.disableProperty().bind(ccView.isFileOpen);
		ccView.closeFileMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.searchButton.disableProperty().bind(ccView.isFileOpen.not());
		ccView.clearButton.disableProperty().bind(ccView.isFileOpen.not());

		ccView.addCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.modifyCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());
		ccView.deleteCaseMenuItem.disableProperty().bind(ccView.isFileOpen.not());

		ccView.titleTextField.disableProperty().bind(ccView.isFileOpen.not());
		ccView.caseTypeTextField.disableProperty().bind(ccView.isFileOpen.not());
		ccView.caseNumberTextField.disableProperty().bind(ccView.isFileOpen.not());
		ccView.yearComboBox.disableProperty().bind(ccView.isFileOpen.not());
		ccView.caseNotesTextArea.disableProperty().bind(ccView.isFileOpen.not());
	}

	private class OpenMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent event) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Select file");
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH)); //local path
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("All Files", "*.*"),
					new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
					new FileChooser.ExtensionFilter("TSV Files", "*.tsv"));
			File file = null;
			if ((file = fileChooser.showOpenDialog(stage)) != null){
				ccModel.readCases(file.getAbsolutePath());
				ccModel.buildYearMapAndList();
				currentCase = ccModel.caseList.get(0);
				displayCurrCaseHelper();
			} //end if

			stage.setTitle("Cyber Cop: " + file.getName());
			ccView.messageLabel.setText(ccModel.caseList.size() + " cases.");
			ccView.isFileOpen.setValue(true);
		} //end handle()
	} //end OpenFileHandler

	private class CloseMenuItemHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent actionEvent) {
			ccView.caseTableView.getItems().clear();
			ccView.yearComboBox.getItems().clear();
			ccView.titleTextField.setText("");
			ccView.caseTypeTextField.setText("");
			ccView.yearComboBox.setValue("");
			ccView.caseNumberTextField.setText("");

			//show default html
			ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
			ccView.caseNotesTextArea.clear();
			ccView.isFileOpen.setValue(false);
			ccModel.caseList.clear(); //clear every time reopen the file
			ccModel.caseMap.clear();
			ccModel.yearMap.clear();
			ccModel.yearList.clear();
			ccModel.yearMapShouldBeUpdated = true;
		}
	}

	private class SearchButtonHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent actionEvent) {
			//if search by all empty fields, set caseTableView by real caseList
			if(ccView.titleTextField.getText().equals("") && ccView.caseTypeTextField.getText().equals("")
					&& ccView.yearComboBox.getValue().equals("") && ccView.caseNumberTextField.getText().equals("")){
				ccView.caseTableView.setItems(ccModel.caseList);
				currentCase = ccModel.caseList.get(0);
				displayCurrCaseHelper();
				ccView.messageLabel.setText(ccModel.caseList.size() + " cases.");
			}
			else {
				//otherwise, set caseTableView by searched results to display
				List<Case> res = ccModel.searchCases(ccView.titleTextField.getText(), ccView.caseTypeTextField.getText(),
						ccView.yearComboBox.getValue(), ccView.caseNumberTextField.getText());

				ccView.caseTableView.setItems((ObservableList<Case>) res);
				currentCase = res.get(0);
				displayCurrCaseHelper();
				ccView.messageLabel.setText(res.size() + " cases.");
			}
		}
	}

	private class ClearButtonHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent actionEvent) {
			ccView.titleTextField.setText("");
			ccView.caseTypeTextField.setText("");
			ccView.yearComboBox.setValue("");
			ccView.caseNumberTextField.setText("");
		}
	}

	private class CaseMeunuItemHandler implements EventHandler<ActionEvent>{

		@Override
		public void handle(ActionEvent actionEvent) {
			MenuItem item = (MenuItem) actionEvent.getSource();
			switch(item.getText()){
				case "Add case":
					caseView = new AddCaseView("Add Case");
					Stage secondaryStage = caseView.buildView();
					secondaryStage.show();
					caseView.updateButton.setOnAction(new AddButtonHandler());
					caseView.closeButton.setOnAction((event) -> secondaryStage.close());
					break;

				case "Modify case":
					caseView = new ModifyCaseView("Modify Case");
					secondaryStage = caseView.buildView();
					secondaryStage.show();
					caseView.updateButton.setOnAction(new ModifyButtonHandler());
					caseView.closeButton.setOnAction((event) -> secondaryStage.close());
					break;

				case "Delete case":
					caseView = new DeleteCaseView("Delete Case");
					secondaryStage = caseView.buildView();
					secondaryStage.show();
					caseView.updateButton.setOnAction(new DeleteButtonHandler());
					caseView.closeButton.setOnAction((event) -> secondaryStage.close());
					break;
			}

			caseView.clearButton.setOnAction((event) -> {
				//set date to current default time when clearing
				caseView.caseDatePicker.setValue(LocalDate.now());
				caseView.titleTextField.setText("");
				caseView.caseTypeTextField.setText("");
				caseView.caseNumberTextField.setText("");
				caseView.caseLinkTextField.setText("");
				caseView.categoryTextField.setText("");
				caseView.caseNotesTextArea.setText("");
			});

		}
	}

	private class AddButtonHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent actionEvent) {
			String caseDate = caseView.caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
			if (caseDate.isBlank() || caseView.titleTextField.getText().isBlank() || caseView.caseTypeTextField.getText().isBlank()
				|| caseView.caseNumberTextField.getText().isBlank()) {
				try{
					throw new DataException("Case must have date, title, type, and number");
				}catch (Exception e) {
					System.out.println("Data exception in add for missing elements occurs");
					return;
				}
			}
			if (ccModel.caseMap.containsKey(caseView.caseNumberTextField.getText())) {
				try{
					throw new DataException("Duplicate case number");
				} catch (Exception e) {
					System.out.println("Data exception in add for duplicate case numbers occurs");
					return;
				}
			}

			Case newCase = new Case(caseDate, caseView.titleTextField.getText(), caseView.caseTypeTextField.getText(),
					caseView.caseNumberTextField.getText(), caseView.caseLinkTextField.getText(),
					caseView.categoryTextField.getText(), caseView.caseNotesTextArea.getText());
			ccModel.caseList.add(newCase);
			ccModel.caseMap.put(caseView.caseNumberTextField.getText(), newCase);
			ccView.messageLabel.setText(ccModel.caseList.size() + " cases.");

			//if a new caseDate year not included in the current ccModel.yearList appears
			if(!ccModel.yearList.contains(caseDate.substring(0,4))){
				//insertion sort to insert to the correct pos in yearList (yearList in ascending order)
				ccModel.yearList.add(caseDate.substring(0,4));
				int j = ccModel.yearList.size() - 1;
				while(j >= 0 && ccModel.yearList.get(j-1).compareTo(caseDate.substring(0,4)) > 0){
					ccModel.yearList.set(j, ccModel.yearList.get(j-1));
					j--;
				}
				if(j != ccModel.yearList.size()-1) {
					ccModel.yearList.set(j, caseDate.substring(0, 4));
				}
			}
			ccView.yearComboBox.setItems(ccModel.yearList);
			ccModel.yearMapShouldBeUpdated = true;
		}
	}

	private class ModifyButtonHandler implements EventHandler<ActionEvent>{
		@Override
		public void handle(ActionEvent actionEvent) {

			for(int i=0; i<ccModel.caseList.size(); i++){
				//since use CaseNumber for equals comparison, assume all cases have case numbers as unique identifiers
				//(including the ones newly added or modified)
				if(ccModel.caseList.get(i).equals(currentCase)){
					Case c = ccModel.caseList.get(i);

					String caseDate = caseView.caseDatePicker.getValue().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
					if (caseDate.isBlank() || caseView.titleTextField.getText().isBlank() || caseView.caseTypeTextField.getText().isBlank()
							|| caseView.caseNumberTextField.getText().isBlank()) {
						try{
							throw new DataException("Case must have date, title, type, and number");
						}catch (Exception e) {
							System.out.println("Data exception in modify for missing elements occurs");
							return;
						}
					}

					if (!c.getCaseNumber().equals(caseView.caseNumberTextField.getText()) && ccModel.caseMap.containsKey(caseView.caseNumberTextField.getText())) {
						try{
							throw new DataException("Duplicate case number");
						} catch (Exception e) {
							System.out.println("Data exception in modify for duplicate case numbers occurs");
							return;
						}
					}

					//update fields of currentCase
					//only if year is modified, yearMap should be updated (latent update, only consider factors
					// that affect the chart), even if title is updated, the chart won't be changed
					if (!c.getCaseDate().substring(0,4).equals(caseDate.substring(0,4))){
						ccModel.yearMapShouldBeUpdated = true;
					}
					c.setCaseDate(caseDate);
					c.setCaseTitle(caseView.titleTextField.getText());
					c.setCaseType(caseView.caseTypeTextField.getText());
					//update caseMap only if caseNumber is updated
					if (!c.getCaseNumber().equals(caseView.caseNumberTextField.getText())){
						ccModel.caseMap.remove(c.getCaseNumber());
						ccModel.caseMap.put(caseView.caseNumberTextField.getText(), c);
					}
					c.setCaseNumber(caseView.caseNumberTextField.getText());
					c.setCaseLink(caseView.caseLinkTextField.getText());
					c.setCaseCategory(caseView.categoryTextField.getText());
					c.setCaseNotes(caseView.caseNotesTextArea.getText());
					displayCurrCaseHelper();

					//if new caseDate year not included in the current ccModel.yearList
					if(!ccModel.yearList.contains(caseDate.substring(0,4))){
						//insertion sort to insert to the correct pos (yearList in ascending order)
						ccModel.yearList.add(caseDate.substring(0,4));
						int j = ccModel.yearList.size()-1;
						while( j>= 0 && ccModel.yearList.get(j-1).compareTo(caseDate.substring(0,4)) > 0){
							ccModel.yearList.set(j, ccModel.yearList.get(j-1));
							j--;
						}
						if(j != ccModel.yearList.size()-1) {
							ccModel.yearList.set(j, caseDate.substring(0, 4));
						}
					}
					ccView.yearComboBox.setItems(ccModel.yearList);
					break;
				}
			}
		}
	}



	private class DeleteButtonHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent actionEvent) {
			for(int i=0; i<ccModel.caseList.size(); i++){
				//since use CaseNumber for comparison, assume all cases have case numbers as unique identifiers
				//(including the ones newly added or modified)
				if(ccModel.caseList.get(i).equals(currentCase)){
					String currentYear = ccModel.caseList.get(i).getCaseDate().substring(0,4);
					ccModel.caseMap.remove(currentCase.getCaseNumber());
					ccModel.caseList.remove(i);

					//if currentCase's year no longer exist in the caseList due to deletion,
					//delete that year from yearList as well
					boolean contain = false;
					for(Case c:ccModel.caseList){
						if(currentYear.equals(c.getCaseDate().substring(0,4))){
							contain = true;
							break;
						}
					}
					if(!contain){
						ccModel.yearList.remove(currentYear);
					}

					if (i >= 1) {
						//when i >= 1 (must exist at least one element after deletion),
						//make current case point to the case before the case to be deleted
						currentCase = ccModel.caseList.get(i - 1);
					}else if(ccModel.caseList.size() == 0){
						//when i == 0 and exist no element after deletion,
						//set to default state
						ccView.titleTextField.setText("");
						ccView.caseTypeTextField.setText("");
						ccView.yearComboBox.setValue("");
						ccView.caseNumberTextField.setText("");

						ccView.webEngine.load(getClass().getResource(DEFAULT_HTML).toExternalForm());
						ccView.caseNotesTextArea.clear();
					}else{
						//when i == 0 and exist at least one element
						//get first element after deletion
						currentCase = ccModel.caseList.get(0);
					}
					displayCurrCaseHelper();
					break;
				}
			}
			ccView.messageLabel.setText(ccModel.caseList.size() + " cases.");
			ccModel.yearMapShouldBeUpdated = true;
		}
	}


	private class SaveMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent actionEvent) {
			FileChooser fileChooser = new FileChooser();
			fileChooser.setTitle("Save file");
			fileChooser.setInitialDirectory(new File(DEFAULT_PATH)); //local path
			fileChooser.getExtensionFilters().addAll(
					new FileChooser.ExtensionFilter("All Files", "*.*"),
					new FileChooser.ExtensionFilter("CSV Files", "*.csv"),
					new FileChooser.ExtensionFilter("TSV Files", "*.tsv"));
			File file = null;
			if ((file = fileChooser.showSaveDialog(stage)) != null){
				boolean t = ccModel.writeCases(file.getAbsolutePath());
				if (t) {
					ccView.messageLabel.setText(file.getName() + " saved.");
				}
			} //end if
		}
	}

	private class CaseCountChartMenuItemHandler implements EventHandler<ActionEvent> {
		@Override
		public void handle(ActionEvent actionEvent) {
			if(ccModel.yearMapShouldBeUpdated){
				ccModel.buildYearMap();
			}
			//only show bar chart of real caseList data, not searched results
			ccView.showChartView(ccModel.yearMap);
			ccModel.yearMapShouldBeUpdated = false;
		}
	}



	/**
	 * Helper method to display currentCase
	 */
	private void displayCurrCaseHelper(){
		ccView.titleTextField.setText(currentCase.getCaseTitle());
		ccView.caseTypeTextField.setText(currentCase.getCaseType());
		ccView.yearComboBox.setValue(currentCase.getCaseDate().substring(0,4));
		ccView.caseNumberTextField.setText(currentCase.getCaseNumber());
		ccView.caseNotesTextArea.setText(currentCase.getCaseNotes());

		if (currentCase.getCaseLink() == null || currentCase.getCaseLink().isBlank()) {  //if no link in data
			URL url = getClass().getResource(DEFAULT_HTML);  //default html
			if (url != null) ccView.webEngine.load(url.toExternalForm());
		} else if (currentCase.getCaseLink().trim().toLowerCase().startsWith("http")){  //if external link
			ccView.webEngine.load(currentCase.getCaseLink().trim());
		} else {
			URL url = getClass().getClassLoader().getResource(currentCase.getCaseLink().trim());  //local link
			if (url != null) ccView.webEngine.load(url.toExternalForm());
		}

	}

}

