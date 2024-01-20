//name: Raoyi Huang, Andrew ID: raoyih
package hw1;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;

public class CCModel {
	Case[] cases;
	String[] fileData;

	/**loadData() takes filename as a parameter,
	 * reads the file and loads all 
	 * data as a String for each row in 
	 * fileData[] array
	 * @param filename
	 */
	void loadData(String filename) {
		//write your code here
		Scanner input = null;
		try {
			input = new Scanner(new File(filename));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		StringBuilder stringData = new StringBuilder();
		while (input.hasNextLine()) {
			stringData.append(input.nextLine() + "\n");
		}
		fileData = stringData.toString().split("\n");
		input.close();
	}

	/**loadCases() uses the data stored in fileData array
	 * and creates Case objects for each row.
	 * These cases are loaded into the cases array.
	 * Note that you may have to traverse the fileData array twice
	 * to be able to initialize the cases array's size.
	 */
	void loadCases() {
		//write your code here
		//suppose all cases are distinct
		cases = new Case[fileData.length];
		for(int i=0; i<fileData.length; i++){
			String[] record = fileData[i].split("\t");
			//if caseNumber is missing
			if(record.length<3){
				// if caseType is missing
				if(record[1].trim().charAt(record[1].trim().length()-1) != ')'){
					cases[i] = new Case(record[0].trim(), record[1].trim(), "", "");
				}else{
					int lastLeftParenthese = record[1].trim().lastIndexOf('(');
					cases[i] = new Case(record[0].trim(), record[1].trim().substring(0,lastLeftParenthese), record[1].trim().substring(lastLeftParenthese+1, record[1].trim().length()-1), "");
				}
				continue;
			}

			//if caseNumber isn't missing
			String caseDate = record[0].trim();
			String titleType = record[1].trim();
			String caseNumber = record[2].trim();
			// if caseType is missing
			if(titleType.charAt(titleType.length()-1) != ')'){
				cases[i] = new Case(caseDate, titleType, "", caseNumber);
			}else{
				int lastLeftParenthese = titleType.lastIndexOf('(');
				cases[i] = new Case(caseDate, titleType.substring(0,lastLeftParenthese), titleType.substring(lastLeftParenthese+1, titleType.length()-1), caseNumber);
			}

		}

	}


}
