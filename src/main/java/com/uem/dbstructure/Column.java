package com.uem.dbstructure;

/**
 * Represents a column from a table in the relational database
 *
 * @author zessin
 */
public class Column {
    private Table table;
    private String name;

    /**
     * Initializes the Column with a name, associating it with a Table
     * @param table The Table which the Column belongs to
     * @param name The name of the Column
     */
    public Column(Table table, String name) {
        super();
        this.table = table;
        this.name = name;
    }

    public Table getTable() {
        return table;
    }

    public void setTable(Table table) {
        this.table = table;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object other){
        if (other == null) {
            return false;
        }
        if (other == this) {
            return true;
        }
        if (!(other instanceof Column)) {
            return false;
        }

        final Column otherColumn = (Column) other;
        return this.getName().equals(otherColumn.getName()) &&
               this.getTable().equals(otherColumn.getTable());
    }

    @Override
    public String toString() {
        return String.format("%s.%s", table, name);
    }
}
