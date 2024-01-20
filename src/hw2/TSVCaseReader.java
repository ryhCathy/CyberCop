//name: Raoyi Huang, andrewID: raoyih
package hw2;

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
        try(BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line = null;
            while ((line = br.readLine()) != null){
                String[] values = line.split("\t");
                caseList.add(new Case(values[0], values[1], values[2], values[3], values[4], values[5], values[6]));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return caseList;
    }

}
