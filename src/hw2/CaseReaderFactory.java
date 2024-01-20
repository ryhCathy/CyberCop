//name: Raoyi Huang, andrewID: raoyih
package hw2;

public class CaseReaderFactory {

    CaseReader createReader(String filename){
        //return appropriate caseReader depending on third to the last character
        //since XXX.csv and XXX.tsv differ in it
        if (filename.charAt(filename.length() - 3) == 't') {
            return new TSVCaseReader(filename);
        }
        if (filename.charAt(filename.length() - 3) == 'c') {
            return new CSVCaseReader(filename);
        }
        return null;
    }
}
