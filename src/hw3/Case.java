//name: Raoyi Huang, andrewID: raoyih
package hw3;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Case implements Comparable<Case>{
    private StringProperty caseDate = new SimpleStringProperty();
    private StringProperty caseTitle = new SimpleStringProperty();
    private StringProperty caseType = new SimpleStringProperty();
    private StringProperty caseNumber = new SimpleStringProperty();
    private StringProperty caseLink = new SimpleStringProperty();
    private StringProperty caseCategory = new SimpleStringProperty();
    private StringProperty caseNotes = new SimpleStringProperty();

    Case(String caseDate, String caseTitle, String caseType, String caseNumber,
         String caseLink, String caseCategory, String caseNotes){
        this.caseDate.set(caseDate);
        this.caseTitle.set(caseTitle);
        this.caseType.set(caseType);
        this.caseNumber.set(caseNumber);
        this.caseLink.set(caseLink);
        this.caseCategory.set(caseCategory);
        this.caseNotes.set(caseNotes);
    }

    public String getCaseDate() {
        return caseDate.get();
    }
    public void setCaseDate(String caseDate) {
        this.caseDate.set(caseDate);
    }
    public StringProperty caseDateProperty() {
        return caseDate;
    }


    public String getCaseTitle() {
        return caseTitle.get();
    }
    public void setCaseTitle(String caseTitle) {
        this.caseTitle.set(caseTitle);
    }
    public StringProperty caseTitleProperty() {
        return caseTitle;
    }


    public String getCaseType() {
        return caseType.get();
    }
    public void setCaseType(String caseType) {
        this.caseType.set(caseType);
    }
    public StringProperty caseTypeProperty() {
        return caseType;
    }


    public String getCaseNumber() {
        return caseNumber.get();
    }
    public void setCaseNumber(String caseNumber) {
        this.caseNumber.set(caseNumber);
    }
    public StringProperty caseNumberProperty() {
        return caseNumber;
    }



    public String getCaseLink() {
        return caseLink.get();
    }
    public void setCaseLink(String caseLink) {
        this.caseLink.set(caseLink);
    }
    public StringProperty caseLinkProperty() {
        return caseLink;
    }



    public String getCaseCategory() {
        return caseCategory.get();
    }
    public void setCaseCategory(String caseCategory) {
        this.caseCategory.set(caseCategory);
    }
    public StringProperty caseCategoryProperty() {
        return caseCategory;
    }



    public String getCaseNotes() {
        return caseNotes.get();
    }
    public void setCaseNotes(String caseNotes) {
        this.caseNotes.set(caseNotes);
    }
    public StringProperty caseNotesProperty() {
        return caseNotes;
    }


    @Override
    public int compareTo(Case o) {
        return -caseDate.get().compareTo(o.caseDate.get());
    }

    @Override
    public String toString() {
        return caseNumber.get();
    }

    /*
     * compare two cases by caseNumber
     * @param obj
     * @return true/false
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false;
        if (this == obj) return true;
        if (this.getClass() != obj.getClass()) return false;
        Case c = (Case) obj;
        return this.caseNumber.equals(c.caseNumber);
    }
}
