package com.uem.dbstructure;

/**
 * Represents a constraint related to columns in the relational database
 *
 * @author zessin
 */
public class Constraint {
    private String name;
    private Table table;
    private Column column;
    private Table referencedTable;
    private Column referencedColumn;
    private ConstraintType type;

    /**
     * Initializes the Constraint with the needed information
     * @param name The name of the constraint
     * @param table The Table which the Constraint belongs to
     * @param column The Column which the Constraint belongs to
     * @param referencedTable The referenced Table, when the Constraint is a FOREIGN_KEY
     * @param referencedColumn The referenced Column, when the Constraint is a FOREIGN_KEY
     * @param type The type of the Constraint
     */
    public Constraint(String name, Table table, Column column, Table referencedTable, Column referencedColumn, ConstraintType type) {
        super();
        this.name = name;
        this.table = table;
        this.column = column;
        this.referencedTable = referencedTable;
        this.referencedColumn = referencedColumn;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public Column getColumn() {
        return column;
    }

    public void setColumn(Column column) {
        this.column = column;
    }

    public Table getReferencedTable() {
        return referencedTable;
    }

    public void setReferencedTable(Table referencedTable) {
        this.referencedTable = referencedTable;
    }

    public Column getReferencedColumn() {
        return referencedColumn;
    }

    public void setReferencedColumn(Column referencedColumn) {
        this.referencedColumn = referencedColumn;
    }

    public ConstraintType getType() {
        return type;
    }

    public void setType(ConstraintType type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Constraint)) {
            return false;
        }

        final Constraint otherConstraint = (Constraint) other;
        return this.getTable().equals(otherConstraint.getTable()) &&
               this.getName().equals(otherConstraint.getName()) &&
               this.getType().equals(otherConstraint.getType());
    }

    @Override
    public String toString() {
        return String.format("Name: %s; Type: %s; Column: %s; Referenced Column: %s", name, type, column, referencedColumn);
    }
}
