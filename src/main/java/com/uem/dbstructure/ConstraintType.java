package com.uem.dbstructure;

/**
 * Provides all the constraint types which are necessary for the application
 *
 * @author zessin
 */
public enum ConstraintType {
    PRIMARY_KEY("PRIMARY KEY"),
    FOREIGN_KEY("FOREIGN KEY"),
    UNIQUE_KEY("UNIQUE");

    private String name;

    /**
     * Associates a ConstraintType object with a String, which is its name
     * @param name The name of the ConstraintType
     */
    ConstraintType(String name) {
        this.name= name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Tells if the ConstraintType is a foreign key
     * @return true when the ConstraintType is a foreign key, false otherwise
     */
    public boolean isForeignKey() {
        return this.equals(ConstraintType.FOREIGN_KEY);
    }

    /**
     * Provides a ConstraintType based on its name
     * @param name The name of the ConstraintType
     * @return The correct ConstraintType for the name (null if not found)
     */
    public static ConstraintType getConstraintTypeByName(String name) {
        switch (name) {
            case "PRIMARY_KEY":
                return PRIMARY_KEY;
            case "FOREIGN_KEY":
                return FOREIGN_KEY;
            case "UNIQUE_KEY":
                return UNIQUE_KEY;
            default:
                return null;
        }
    }
}
