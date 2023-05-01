import java.util.ArrayList;

public class Point {
    public int row;
    public int column;
    public String columnName;
    public String name;
    public String poolName;
    public int value;
    public ArrayList<Point> adj;
    Point(int row, int column, String columnName, int value){
        this.row = row;
        this.column = column;
        this.columnName = columnName;
        name = columnName + row;
        this.value = value;
        adj = new ArrayList<>();
    }
}
