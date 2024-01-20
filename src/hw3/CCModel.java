//name: Raoyi Huang, andrewID: raoyih
package hw3;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class CCModel {
	ObservableList<Case> caseList = FXCollections.observableArrayList(); 			//a list of case objects
	ObservableMap<String, Case> caseMap = FXCollections.observableHashMap();		//map with caseNumber as key and Case as value
	ObservableMap<String, List<Case>> yearMap = FXCollections.observableHashMap();	//map with each year as a key and a list of all cases dated in that year as value. 
	ObservableList<String> yearList = FXCollections.observableArrayList();			//list of years to populate the yearComboBox in ccView

	boolean yearMapShouldBeUpdated = false;  //check whether caseList has been updated (if updated, update yearMap)

	/** readCases() performs the following functions:
	 * It creates an instance of CaseReaderFactory, 
	 * invokes its createReader() method by passing the filename to it, 
	 * and invokes the caseReader's readCases() method. 
	 * The caseList returned by readCases() is sorted 
	 * in the order of caseDate for initial display in caseTableView. 
	 * Finally, it loads caseMap with cases in caseList. 
	 * This caseMap will be used to make sure that no duplicate cases are added to data
	 * @param filename
	 */
	void readCases(String filename) {
		//write your code here
		//convert windows path to mac path
		if(filename.contains("\\")){
			filename = filename.replace("\\", "/");
		}

		CaseReaderFactory factory = new CaseReaderFactory();
		CaseReader reader = factory.createReader(filename);
		List<Case> list = reader.readCases();
		Collections.sort(list);

		//sort cases by date from the most recent to the least recent
		for(Case c:list){
			caseList.add(c);
		}

		for(Case c:list){
			//if duplicate caseNumber, replace old case with new case value
			caseMap.put(c.getCaseNumber(), c);
		}

	}

	/** buildYearMapAndList() performs the following functions:
	 * 1. It builds yearMap that will be used for analysis purposes in Cyber Cop 3.0
	 * 2. It creates yearList which will be used to populate yearComboBox in ccView
	 * Note that yearList can be created simply by using the keySet of yearMap.
	 */
	void buildYearMapAndList() {
		//write your code here
		for(Case c:caseList){
			String year = c.getCaseDate().substring(0,4);
			//list is the List<Case> corresponding to key "year", which is null when no
			//such a key is found
			List<Case> list = yearMap.get(year);
			if(list == null){
				//when year not exist as a key in yearMap
				list = new ArrayList<>();
				list.add(c);
				yearMap.put(year, list);
			}else{
				//when year exists as a key in yearMap
				list.add(c);
			}
		}

		//add all keys to yearList
		yearList.addAll(yearMap.keySet());
		//reverse its order to rank yearList from the least recent year to the most recent year
		//(for order of the years in yearComboBox same as the video)
		Collections.reverse(yearList);
	}

	void buildYearMap(){
		//write your code here
		for(Case c:caseList){
			String year = c.getCaseDate().substring(0,4);
			//list is the List<Case> corresponding to key "year", which is null when no
			//such a key is found
			List<Case> list = yearMap.get(year);
			if(list == null){
				//when year not exist as a key in yearMap
				list = new ArrayList<>();
				list.add(c);
				yearMap.put(year, list);
			}else{
				//when year exists as a key in yearMap
				list.add(c);
			}
		}
	}

	/**searchCases() takes search criteria and 
	 * iterates through the caseList to find the matching cases. 
	 * It returns a list of matching cases.
	 */
	List<Case> searchCases(String title, String caseType, String year, String caseNumber) {
		//write your code here
		ObservableList<Case> res = FXCollections.observableArrayList();
		for (Case c : caseList) {
			//short-circuit, if any one field is null just don't consider it.
			//otherwise, check whether it's empty or contains title without case sensitivity
			if ((title == null || title.isEmpty() || c.getCaseTitle().toLowerCase().contains(title))
					&& (caseType == null || caseType.isEmpty() || c.getCaseType().toLowerCase().contains(caseType))
					&& (year == null || year.isEmpty() || c.getCaseDate().substring(0, 4).equals(year))
					&& (caseNumber == null || caseNumber.isEmpty() || c.getCaseNumber().contains(caseNumber))) {
				res.add(c);
			}
		}
		return res;
	}

	public boolean writeCases(String filename) {
		try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
			for (Case c: caseList) {
				bw.write(String.format("%s\t%s\t%s\t%s\t%s\t%s\t%s\t \t \t \n", c.getCaseDate(),
						c.getCaseTitle(), c.getCaseType(), c.getCaseNumber(), c.getCaseLink(),
						c.getCaseCategory(), c.getCaseNotes()));
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}

	//TODO: test
	public static void main(String[] args) {
		CCModel t = new CCModel();
		t.readCases("data/CyberCop-TSVData-Perfect.tsv");
		t.buildYearMapAndList();

	}


}
