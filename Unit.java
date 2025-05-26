package models;

public class Unit {
    private int unitId;
    private String unitName;
    private String abbreviation;

    public Unit(int unitId, String unitName, String abbreviation) {
        this.unitId = unitId;
        this.unitName = unitName;
        this.abbreviation = abbreviation;
    }

    public Unit(String unitName, String abbreviation) { // for insert
        this.unitName = unitName;
        this.abbreviation = abbreviation;
    }

    public int getUnitId() {
        return unitId;
    }

    public String getName() {
        return unitName;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setUnitId(int unitId) {
        this.unitId = unitId;
    }
}
