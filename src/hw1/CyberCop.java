//name: Raoyi Huang, Andrew ID: raoyih
package hw1;

import java.util.Arrays;
import java.util.Collections;
import java.util.Scanner;

public class CyberCop {

	public static final String DATAFILE = "data/FTC-cases-TSV.txt";
	CCModel ccModel = new CCModel();
	SearchEngine searchEngine = new SearchEngine();

	Scanner input = new Scanner(System.in);

	/**main() instantiates CyberCop and then invokes dataManager's loadData
	 * and loadCases() methods
	 * It then invokes showMenu to get user input
	 * @param args
	 */
	//Do not change this method
	public static void main(String[] args) {
		CyberCop cyberCop = new CyberCop();
		cyberCop.ccModel.loadData(DATAFILE);
		cyberCop.ccModel.loadCases();
		cyberCop.showMenu();
	}

	/**showMenu() shows the menu. 
	 * Based on the user choice, it invokes one of the methods:
	 * printSearchResults(), printCaseTypeSummary(), or printYearwiseSummary()
	 * The program exits when user selects Exit option. 
	 * See the hand-out for the expected layout of menu-UI
	 */
	void showMenu() {
		//write your code here
		int choice = 0;
		Scanner input = new Scanner(System.in);
		do {
			System.out.println("*** Welcome to CyberCop! ***");
			System.out.println("1. Search cases for a company");
			System.out.println("2. Search cases in a year");
			System.out.println("3. Search case number");
			System.out.println("4. Print Case-type summary");
			System.out.println("5. Print Year-wise summary");
			System.out.println("6. Exit");

			choice = input.nextInt();
			input.nextLine(); //clear the buffer
			System.out.println("----------------------------------------------------------------------------------");
			switch (choice) {
				case 1: {
					System.out.println("Enter search string");
					String searchString = input.nextLine();
					System.out.println("----------------------------------------------------------------------------------");
					Case[] results = searchEngine.searchTitle(searchString, ccModel.cases);
					printSearchResults(searchString, results);
					break;
				}
				case 2: {
					System.out.println("Enter search year as YYYY");
					System.out.println("----------------------------------------------------------------------------------");
					String searchString = input.nextLine();
					Case[] results = searchEngine.searchYear(searchString, ccModel.cases);
					printSearchResults(searchString, results);
					break;
				}
				case 3: {
					System.out.println("Enter case number");
					System.out.println("----------------------------------------------------------------------------------");
					String searchString = input.nextLine();
					Case[] results = searchEngine.searchCaseNumber(searchString, ccModel.cases);
					printSearchResults(searchString, results);
					break;
				}
				case 4: {
					printCaseTypeSummary();
					break;
				}
				case 5: {
					printYearwiseSummary();
					break;
				}

			}
		} while (choice != 6);
		input.close();
	}

	/**printSearcjResults() takes the searchString and array of cases as input
	 * and prints them out as per the format provided in the handout
	 * @param searchString
	 * @param cases
	 */
	void printSearchResults(String searchString, Case[] cases) {
		//write your code here
		if (cases != null) {
			System.out.println(cases.length + " case(s) found for " + searchString);
			System.out.println("----------------------------------------------------------------------------------");
			System.out.printf(" %s. %-90s %-20s %-20s\n", "#", "Last update Case Title", "Case Type", "Case/File number");
			System.out.println("----------------------------------------------------------------------------------");
			int count = 0;
			for (Case c : cases ) {
				System.out.printf(" %d. %s  %-78s %-20s %-20s\n",++count, c.caseDate, c.caseTitle, c.caseType, c.caseNumber);
			}
		} else System.out.println("Sorry, no search results found for " + searchString);

		System.out.println("----------------------------------------------------------------------------------");

	}

	/**printCaseTypeSummary() prints a summary of
	 * number of cases of different types as per the 
	 * format given in the handout.
	 */
	void printCaseTypeSummary() {
		//write your code here
		//since current data only have Federal and Administrative as two case types
		String[] types = {"Administrative", "Federal"};

		//initialize counts for 'types' array, with the last count for unknown cases
		int[] typeCounts = new int[types.length+1];
		for(int i=0; i<types.length+1; i++){
			typeCounts[i]=0;
		}

		//count for each case type including the missing and the unknown ones for all cases
		for(Case c: ccModel.cases){
			//cases with missing case types
			if(c.caseType.equals("")){
				typeCounts[typeCounts.length-1]++;
				continue;
			}
			boolean flag = false;
			for(int i=0; i<typeCounts.length-1; i++){
				if(c.caseType.equalsIgnoreCase(types[i])){
					typeCounts[i]++;
					flag = true;
					break;
				}
			}
			//cases with unknown case types
			if(!flag){
				typeCounts[typeCounts.length-1]++;
			}
		}

		System.out.println("*** Case Type Summary Report ***");
		for(int i=0; i<typeCounts.length-1; i++){
			System.out.println("No. of " + types[i] + " cases: " + typeCounts[i]);
		}
		System.out.println("No. of unknown case types: " + typeCounts[typeCounts.length-1]);
		System.out.println("----------------------------------------------------------------------------------");
	}
	
	/**printYearWiseSummary() prints number of cases in each year
	 * as per the format given in the handout
	 */
	void printYearwiseSummary() {
		//write your code here
		StringBuilder uniqueYears = new StringBuilder();
		//get a String array of unique years
		for(Case c: ccModel.cases){
			if(!uniqueYears.toString().contains(c.caseDate.substring(0,4))){
				uniqueYears.append(c.caseDate.substring(0,4)+",");
			}
		}
		String[] years = uniqueYears.toString().split(",");
		//sort years by String (ASCII code comparison = number comparison in terms of results)
		Arrays.sort(years);

		//initialize counts for 'years' array
		int[] yearCounts = new int[years.length];
		for(int i=0; i<years.length; i++){
			yearCounts[i]=0;
		}

		//count for each year for all cases
		for(Case c: ccModel.cases){
			if(c.caseDate.equals("")){
				continue;
			}
			for(int i=0; i<years.length; i++){
				if(c.caseDate.substring(0,4).equals(years[i])){
					yearCounts[i]++;
					break;
				}
			}
		}

		//print from the most recent years to the least recent years
		System.out.printf("%55s", "*** Year-wise Summary Report ***\n");
		System.out.printf("%60s", "*** Number of FTC cases per year ***\n\n");
		for(int i=years.length-1; i>=0; i-=5){
			System.out.printf("%10s: %2d  %10s: %2d  %10s: %2d  %10s: %2d  %10s: %2d\n", years[i], yearCounts[i], years[i-1], yearCounts[i-1], years[i-2], yearCounts[i-2], years[i-3], yearCounts[i-3], years[i-4], yearCounts[i-4]);
		}
		System.out.println("----------------------------------------------------------------------------------");

	}
	
	
}
