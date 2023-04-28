import java.util.ArrayList;

public class Point {
    public int row;
    public int column;
    public String columnName;
    public String name;
    public int value;
    public String poolName;
    public boolean isPool;
    public ArrayList<Point> adj;
    public boolean isVisited;
    Point(int row, int column, String columnName, int value){
        this.row = row;
        this.column = column;
        this.columnName = columnName;
        name = columnName + row;
        this.value = value;
        isPool = false;
        adj = new ArrayList<>();
        isVisited = false;
    }
    public void setPool(){
        int count = 0;
        for(Point p : adj){
            if(value > p.value){
                count++;
            }
        }
        if(count == adj.size()){
            isPool = true;
        }
    }
}
