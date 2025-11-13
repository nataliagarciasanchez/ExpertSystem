package domain;

public class Symptom {
    private SymptomType type;
    private Double numericValue;
    private boolean present;
    /**
     * @param type    the specific symptom type of the SymtomType enum
     * @param present true if the symptom is present in the patient
     */
    public Symptom(SymptomType type, boolean present) {
        this.type = type;
        this.present = present;
    }
    public void setNumericValue(Double v) {
        this.numericValue = v;
    }
    public Double getNumericValue() {
        return numericValue;
    }
    public SymptomType getType() {
        return type;
    }

    public void setType(SymptomType type) {
        this.type = type;
    }

    public boolean isPresent() {
        return present;
    }

    public void setPresent(boolean present) {
        this.present = present;
    }

    @Override
    public String toString() {
        return type + " (" + (present ? "sí" : "no") + ")";
    }
}
