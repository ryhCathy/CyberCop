//name: Raoyi Huang, andrewID: raoyih
package hw3;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TSVCaseReader extends CaseReader{

    TSVCaseReader(String filename) { super(filename);}

    @Override
    List<Case> readCases() {
        List<Case> caseList = new ArrayList<>();
        int countRej = 0;
        List<String> test = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = null;
            while ((line = br.readLine()) != null){
                String[] values = line.split("\t");
                if (values[0].isBlank() || values[1].isBlank()  || values[2].isBlank()  || values[3].isBlank() ) {
                    countRej++;
                    continue;
                }
                caseList.add(new Case(values[0], values[1], values[2], values[3], values[4], values[5], values[6]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (countRej != 0) {
            try {
                throw new DataException(countRej +
                        " cases rejected.\nThe file must have cases with\ntab separated date, title, type, and case number!");
            } catch (Exception e) {
                System.out.println("Data exception in TSVCaseReader occurs");
            }
        }
        return caseList;
    }


}
