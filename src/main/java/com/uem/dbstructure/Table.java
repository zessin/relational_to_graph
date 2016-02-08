package com.uem.dbstructure;

/**
 * Represents a table in the relational database
 *
 * @author zessin
 */
public class Table {
    private String name;
    private Boolean relationshipTable;

    /**
     * Initializes the Table with a name
     * @param name The name of the Table
     */
    public Table(String name) {
        super();
        this.name = name;
        this.relationshipTable = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * Tells whether a Table is a many to many relationship table or not
     * @return True if the Table is a many to many relationship table, false otherwise
     */
    public Boolean isRelationshipTable() {
        return relationshipTable;
    }

    /**
     * Sets whether a Table is a many to many relationship table or not
     * @param relationshipTable The value to be set
     */
    public void setRelationshipTable(Boolean relationshipTable) {
        this.relationshipTable = relationshipTable;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Table)) {
            return false;
        }

        final Table otherTable = (Table) other;
        return this.getName().equals(otherTable.getName());
    }

    @Override
    public String toString() {
        return String.format("%s", name);
    }
}
