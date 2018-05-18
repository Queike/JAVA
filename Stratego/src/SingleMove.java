public class SingleMove {
    int row;
    int column;

    SingleMove(){}

    SingleMove(int row, int column){
        this.row = row;
        this.column = column;
    }

    public int getRow() {
        return row;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public String toString(){
        return "Row: " + row + ", Column: " + column;
    }


    public boolean equals(SingleMove move){
        return this.getRow() == move.getRow() && this.getColumn() == move.getColumn();
    }
}
