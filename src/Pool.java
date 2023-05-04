import java.util.ArrayList;
import java.util.Arrays;

/**
 * The type Pool. This class is used to store the information of each pool.
 * Each pool has a name, a list of points, and a list of surrounding points.
 * The surrounding points are the points that are adjacent to any of the points in the pool but not in the pool.
 * The surrounding points are stored in an ArrayList. The points in the pool are stored in an ArrayList.
 *
 */
public class Pool implements Comparable<Pool>{
    /**
     * The Name. The name of the pool. The name is a capital letter.
     */
    public String name;
    /**
     * The Points. The list of points in the pool.
     */
    public ArrayList<Point> points = new ArrayList<>();
    /**
     * The Surrounding points. The list of surrounding points of the pool.
     */
    public ArrayList<Point> surroundingPoints = new ArrayList<>();

    /**
     * Instantiates a new Pool. The constructor of the Pool class. The default constructor.
     */
    Pool(){}

    /**
     * Add point. Add a point to the pool. The point is added to the list of points in the pool.
     * The surrounding points of the pool are updated.
     *
     * @param point the point
     */
    public void addPoint(Point point){
        points.add(point);
    }

    /**
     * Set surrounding points. The surrounding points of the pool are updated.
     * The surrounding points are the points that are adjacent to any of the points in the pool but not in the pool.
     * The surrounding points are stored in an ArrayList.
     */
    public void setSurroundingPoints(){
        for(Point point : points){
            for(Point adjPoint : point.adj){
                if(!points.contains(adjPoint) && !surroundingPoints.contains(adjPoint)){
                    surroundingPoints.add(adjPoint);
                }
            }
        }
    }

    /**
     * Calculate volume double. The volume of the pool is calculated. The volume is calculated by the formula:
     * volume = sqrt(sum of (minimum max height - height of each point))
     * The minimum max height is the minimum value of the maximum height of the surrounding points.
     * The height of each point is the value of the point. The volume is returned.
     *
     * @return the double volume
     */
    public double calculateVolume(){
        double volume = 0;
        int minimumMaxHeight  = Integer.MAX_VALUE;
        for(Point sp: surroundingPoints){
            if(sp.value < minimumMaxHeight) minimumMaxHeight = sp.value;
        }
        for(Point pj : points){
            volume += minimumMaxHeight - pj.value;
        }
        return Math.sqrt(volume);
    }

    /**
     * Set name. The name of the pool is set. The name is a capital letter.
     * The name is set by the formula: A, B, C, ..., Z, AA, AB, AC, ..., AZ, BA, BB, BC, ..., ZZ.
     * The name is set by the count of the pool. The count is the index of the pool in the list of pools.
     * The count is used to calculate the name of the pool.
     *
     * @param count the count
     */
    public void setName(int count){
        ArrayList<String> upperCaseAlphabet = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H",
                "I", "J", "K", "L","M", "N", "O", "P", "Q", "R", "S", "T", "U","V","W","X","Y","Z"));
        if (count < upperCaseAlphabet.size()){
            name = upperCaseAlphabet.get(count);
        }
        else{
            int firstLetter = count / upperCaseAlphabet.size() - 1;
            int secondLetter = count % upperCaseAlphabet.size();
            name = upperCaseAlphabet.get(firstLetter) + upperCaseAlphabet.get(secondLetter);
        }
        for(Point p : points){
            p.poolName = name;
        }
    }

    /**
     * Find top most left most point. The top most left most point of the pool is found.
     * The top most left most point is the point with the smallest row and column.
     * The top most left most point is returned.
     *
     * @return the point top most left most point
     */
    public Point findTopMostLeftMostPoint(){
        Point topMostLeftMostPoint = points.get(0);
        for(Point p : points){
            if(p.row < topMostLeftMostPoint.row) topMostLeftMostPoint = p;
            else if(p.row == topMostLeftMostPoint.row && p.column < topMostLeftMostPoint.column) topMostLeftMostPoint = p;
        }
        return topMostLeftMostPoint;
    }
    /**
     * Compare to int. The comparison of two pools is done.
     * The comparison is done by comparing the top most left most point of the two pools.
     * If the top most left most point of the pool is smaller than the top most left most point of the pool to be compared with, -1 is returned.
     * If the top most left most point of the pool is equal to the top most left most point of the pool to be compared with, 0 is returned.
     * If the top most left most point of the pool is larger than the top most left most point of the pool to be compared with, 1 is returned.
     *
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     * @param o the o. The pool to be compared with.
     * @return the int. The result of the comparison.
     *
     */
    @Override
    public int compareTo(Pool o) {
        if(findTopMostLeftMostPoint() == o.findTopMostLeftMostPoint()) return 0;
        else if(findTopMostLeftMostPoint().row < o.findTopMostLeftMostPoint().row) return -1;
        else if(findTopMostLeftMostPoint().row == o.findTopMostLeftMostPoint().row && findTopMostLeftMostPoint().column < o.findTopMostLeftMostPoint().column) return -1;
        else return 1;
    }
}

