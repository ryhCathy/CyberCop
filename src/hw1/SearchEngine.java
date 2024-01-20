//name: Raoyi Huang, Andrew ID: raoyih
package hw1;

public class SearchEngine {
	
	/**searchTitle() takes a searchString and array of cases,
	 * searches for cases with searchString in their title,
	 * and if found, returns them in another array of cases.
	 * If no match is found, it returns null.
	 * Search is case-insensitive
	 * @param searchString
	 * @param cases
	 * @return
	 */
	Case[] searchTitle(String searchString, Case[] cases) {
		//write your code here
		int count=0;
		//count the number of cases whose titles contain the 'searchString' for array size initialization
		for(Case c:cases){
			if(c.caseTitle.toLowerCase().contains(searchString.toLowerCase()))
				count++;
		}

		if(count==0)
			return null;

		//declare and initialize the 'results' array
		Case[] results = new Case[count];
		//assign the cases whose titles contain the 'searchString' to 'results' array
		int j=0;
		for(int i=0; i<cases.length; i++){
			if(cases[i].caseTitle.toLowerCase().contains(searchString.toLowerCase())){
				results[j]=cases[i];
				j++;
			}
		}
		return results;
	}
	
	/**searchYear() takes year in YYYY format as search string,
	 * searches for cases that have the same year in their date,
	 * and returns them in another array of cases.
	 * If not found, it returns null.
	 * @param year
	 * @param cases
	 * @return
	 */
	Case[] searchYear(String year, Case[] cases) {
		//write your code here
		int count=0;
		//count the number of cases whose year is the 'searchString' for array size initialization
		for(Case c:cases){
			if(c.caseDate!="" && c.caseDate.substring(0,4).equals(year))
				count++;
		}

		if(count==0)
			return null;

		//declare and initialize the 'results' array
		Case[] results = new Case[count];
		int j=0;
		//assign the cases whose year is the 'searchString' to 'results' array
		for(int i=0; i<cases.length; i++){
			if(cases[i].caseDate!="" && cases[i].caseDate.substring(0,4).equals(year)){
				results[j]=cases[i];
				j++;
			}
		}
		return results;
	}
	
	/**searchCaseNumber() takes a caseNumber,
	 * searches for those cases that contain that caseNumber, 
	 * and returns an array of cases that match the search.
	 * If not found, it returns null.
	 * Search is case-insensitive.
	 * @param caseNumber
	 * @param cases
	 * @return
	 */
	Case[] searchCaseNumber(String caseNumber, Case[] cases) {
		//write your code here
		int count=0;
		//count the number of cases whose caseNumber contains the 'searchString' for array size initialization
		for(Case c:cases){
			if(c.caseNumber.contains(caseNumber))
				count++;
		}

		if(count==0)
			return null;

		//declare and initialize the 'results' array
		Case[] results = new Case[count];
		//assign the cases whose caseNumber contains the 'searchString' to 'results' array
		int j=0;
		for(int i=0; i<cases.length; i++){
			if(cases[i].caseNumber.contains(caseNumber)){
				results[j]=cases[i];
				j++;
			}
		}
		return results;
	}
}
