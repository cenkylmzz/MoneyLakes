import java.util.ArrayList;

/**
 * The type Point. This class is used to store the information of each point in the matrix.
 * Each point has a row, a column, a name, a value, and a list of adjacent points.
 * The adjacent points are the points are in any of the eight directions of the current point.
 * The adjacent points are stored in an ArrayList.
 */
public class Point {
    /**
     * The Row. The row number of the point in the matrix.
     */
    public int row;
    /**
     * The Column. The column number of the point in the matrix.
     */
    public int column;
    /**
     * The Name. The name of the point. The name is the column name plus the row number.
     */
    public String name;
    /**
     * The Pool name. The name of the pool that the point belongs to. The default value is null.
     */
    public String poolName;
    /**
     * The Value. The value of the point.
     */
    public int value;
    /**
     * The Adj. The list of adjacent points. The adjacent points are the points are in any of the eight directions of the current point.
     */
    public ArrayList<Point> adj;

    /**
     * Instantiates a new Point. The constructor of the Point class.
     *
     * @param row        the row number of the point in the matrix
     * @param column     the column number of the point in the matrix
     * @param columnName the column name of the point in the matrix
     * @param value      the value of the point
     */
    Point(int row, int column, String columnName, int value){
        this.row = row;
        this.column = column;
        name = columnName + row;
        this.value = value;
        adj = new ArrayList<>();
    }
}
