import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * The type Main. This is the main class of the program.
 * It reads the input file and creates a map of points.
 * Then it asks for 10 moves from the user and updates the map.
 * Then it finds all the pools and calculates their volumes.
 * Finally, it prints the map and the pools.
 *
 */
public class Main {
    /**
     * The entry point of application. This is the main method of the program.
     *
     * @param args the input arguments
     * @throws FileNotFoundException the file not found exception
     */
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<String> lowerCaseAlphabet = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h",
                "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));
        Scanner in = new Scanner(new File(args[0]));
        int row = in.nextInt();
        int column = in.nextInt();
        ArrayList<ArrayList<Integer>> valueMap = new ArrayList<>();
        for (int i = 0; i < column; i++) {                      //read the input file and store it in a 2D arraylist
            valueMap.add(new ArrayList<>());
            for (int j = 0; j < row; j++) {
                valueMap.get(i).add(in.nextInt());
            }
        }
        in.close();
        ArrayList<ArrayList<Point>> allPoints = setPoints(valueMap, lowerCaseAlphabet); //create a map of points
        printMap(lowerCaseAlphabet, allPoints,true);
        Scanner input = new Scanner(System.in);
        for (int i = 1; i < 11; i++) {
            System.out.print("Add stone " + i + " / 10 to coordinate:");
            String move = input.next();
            Point movedPoint = findPoint(allPoints, move);   //find the point that corresponds to the user input
            if (movedPoint != null) {                        //if the user input is valid, update the map
                movedPoint.value++;
                printMap(lowerCaseAlphabet, allPoints,false);
                System.out.println("---------------");
            } else {                                        //if the user input is invalid, ask for another input
                System.out.println("Not a valid step!");
                i--;
            }
        }
        input.close();
        setAdj(allPoints);                               //set the adjacent points of each point
        ArrayList<Pool> allPools = new ArrayList<>();
        for (int i = 1; i < allPoints.size(); i++) {
            for (int j = 1; j < allPoints.get(0).size(); j++) {
                Point currentPoint = allPoints.get(i).get(j);           //for each point, find the pool that it belongs to
                Pool pool = new Pool();
                if (dfsIterative(currentPoint, currentPoint.value, new int[allPoints.size()][allPoints.get(0).size()], pool)) {
                    //if the pool is valid, add it to the list of pools
                    allPools.add(pool);
                }
            }
        }
        removeDuplicate(allPools);                   //remove duplicate pools
        Collections.reverse(allPools);           //reverse the list of pools
        removeDuplicate(allPools);                   //remove duplicate pools
        Collections.sort(allPools);              //sort the list of pools in the order of their top most left points
        double ans = 0;
        for (int i = 0; i < allPools.size(); i++) {
            allPools.get(i).setName(i);
            allPools.get(i).setSurroundingPoints();
            ans += allPools.get(i).calculateVolume();     //calculate the volume of each pool and add it to the final score
        }
        printMap(lowerCaseAlphabet, allPoints,false);         //print the map with the calculated pools
        System.out.printf("Final score: " + "%.2f", ans);               //print the final score
    }

    /**
     * Converts a value map into a list of points, using a set of lowercase alphabets to
     * generate labels for the points.
     *
     * @param valueMap          the value map to convert
     * @param lowerCaseAlphabet the set of lowercase alphabets to use for generating labels
     * @return a list of points corresponding to the values in the value map
     */
    public static ArrayList<ArrayList<Point>> setPoints(ArrayList<ArrayList<Integer>> valueMap, ArrayList<String> lowerCaseAlphabet) {
        ArrayList<ArrayList<Point>> allPoints = new ArrayList<>();
        for (int i = 0; i < valueMap.size(); i++) {                              // Iterate over each row in the value map
            ArrayList<Point> rowPoints = new ArrayList<>();
            for (int j = 0; j < valueMap.get(i).size(); j++) {                   // Iterate over each element in the row
                if (j == lowerCaseAlphabet.size()) {                             // If we've run out of alphabets, we stop adding points
                    break;
                }
                                                                                 // Create a point using the current alphabet as the label
                Point point = new Point(i, j, lowerCaseAlphabet.get(j), valueMap.get(i).get(j));
                rowPoints.add(point);
            }
                                                      // If the row has more elements than alphabets, we generate labels using two alphabets
            if (valueMap.get(i).size() > lowerCaseAlphabet.size()) {
                int numExtraElements = valueMap.get(i).size() - lowerCaseAlphabet.size();
                int currentElementIndex = lowerCaseAlphabet.size();
                for (int k = 0; k < numExtraElements; k++) {
                    String label = lowerCaseAlphabet.get(k / lowerCaseAlphabet.size()) + lowerCaseAlphabet.get(k % lowerCaseAlphabet.size());
                    Point point = new Point(i, currentElementIndex, label, valueMap.get(i).get(currentElementIndex));
                    rowPoints.add(point);
                    currentElementIndex++;
                }
            }
            allPoints.add(rowPoints);
        }
        return allPoints;
    }

    /**
     * Remove duplicate. This method removes subset pools from the list of all pools.
     * It iterates over each pool, and checks if the current pool contains all the points
     * in the other pool. If it does, it removes the other pool. It then repeats this
     * process for each pool after the current pool.
     *
     * @param allPools the all pools to remove duplicate from
     */
    public static void removeDuplicate(ArrayList<Pool> allPools) {
        for (int i = 0; i < allPools.size(); i++) {                                   // Iterate over each pool
            for (int j = i + 1; j < allPools.size(); j++) {                           // Iterate over each pool after the current pool
                if (allPools.get(i).points.containsAll(allPools.get(j).points)) {  // If the current pool contains all the points in the other pool
                    allPools.remove(allPools.get(j));                              // Remove the other pool
                    j--;                                                              // Decrement j to account for the removed pool
                }
            }
        }
    }

    /**
     * Dfs iterative boolean. This method performs a depth-first search on a point, and adds all
     * the points in the pool to the pool object. It returns true if the pool is valid, and false
     * if the pool is invalid. A pool is invalid if it touches the edge of the map. This method
     * uses an iterative approach, and is faster than the recursive approach.
     *
     * @param p        the point to start the search from
     * @param maxValue the max value of the pool
     * @param visited  the visited array
     * @param pool     the pool object to add points to
     * @return the boolean
     */
    public static boolean dfsIterative(Point p, int maxValue, int[][] visited, Pool pool) {
        Stack<Point> stack = new Stack<>();                                 // Create a stack to store points
        stack.push(p);                                                 // Push the starting point onto the stack
        while (!stack.empty()) {                                            // While the stack is not empty
            Point point = stack.pop();                                      // Pop a point from the stack
            pool.addPoint(point);                                           // Add the point to the pool
            if (visited[point.row][point.column] == 1) continue;            // If the point has already been visited, continue
                                                                            // If the point is on the edge of the map, return false
            if (point.row == 0 || point.column == 0 || point.row == visited.length - 1 || point.column == visited[0].length - 1)
                return false;
            visited[point.row][point.column] = 1;                            // Mark the point as visited
            for (Point pAdj : point.adj) {                                   // Iterate over each adjacent point
                if (visited[pAdj.row][pAdj.column] == 0 && pAdj.value <= maxValue) {
                    stack.push(pAdj);
                }
            }
        }
        return true;
    }

    /**
     * Print map. This method prints the map, with the points in each pool labelled with the
     * pool name. If the pool name is null, then the point is not in any pool.
     * This method is called twice, once before the algorithm is run, and once after the algorithm
     * is run. This is because the first time the method is called, the points are labelled with
     * the values, and the second time the method is called, the points are labelled with the
     * pool names.
     *
     * @param lowerCaseAlphabet the lower case alphabet
     * @param allPoints         the all points
     * @param isFirstTime       the is first line
     */
    public static void printMap(ArrayList<String> lowerCaseAlphabet, ArrayList<ArrayList<Point>> allPoints, boolean isFirstTime) {
        int nameCount = 0;
        for (int j = 0; j < allPoints.size(); j++) { // Iterate over each row
            if (j < 10) { // Print the row number
                if (allPoints.get(j).get(0).value < 10) // If the value is less than 10, we add an extra space
                    System.out.print("  " + j + "  ");
                else
                    System.out.print("  " + j + " ");
            }
            else if (j < 100) { // Print the row number
                if (allPoints.get(j).get(0).value < 10) // If the value is less than 10, we add an extra space
                    System.out.print(" " + j + "  ");
                else
                    System.out.print(" " + j + " ");
            }
            else { // Print the row number
                if (allPoints.get(j).get(0).value < 10)  // If the value is less than 10, we add an extra space
                    System.out.print(j + "  ");
                else
                    System.out.print(j + " ");
            }
            for (int k = 0; k < allPoints.get(j).size(); k++) {     // Iterate over each point in the row
                if (k == allPoints.get(j).size() - 1) {             // If we are at the last point in the row
                    if(isFirstTime) {                               // If this is the first time we are printing the map
                        System.out.println(allPoints.get(j).get(k).value);
                        isFirstTime = false;
                    }
                    else
                        System.out.println(allPoints.get(j).get(k).value + " ");
                } else { // If we are not at the last point in the row
                    if (allPoints.get(j).get(k).poolName == null) { // If the point is not in a pool
                        if (nameCount > 25 && allPoints.get(j).get(k + 1).poolName != null) {
                            System.out.print(allPoints.get(j).get(k).value + " ");
                        }
                        else { // If the pool name is less than Z, we add an extra space
                            if (allPoints.get(j).get(k + 1).value < 10) // If the value is less than 10, we add an extra space
                                System.out.print(allPoints.get(j).get(k).value + "  ");
                            else                                        //
                                System.out.print(allPoints.get(j).get(k).value + " ");
                        }
                    }
                    else { // If the point is in a pool
                        if (allPoints.get(j).get(k + 1).value < 10) // If the value is less than 10, we add an extra space
                            System.out.print(allPoints.get(j).get(k).poolName + "  ");
                        else
                            System.out.print(allPoints.get(j).get(k).poolName + " ");
                        nameCount++;
                    }
                }
            }
        }
        System.out.print("     ");
        if (allPoints.get(0).size() > lowerCaseAlphabet.size()) { // If the number of columns is greater than 26
            int count = 0;
            for (String s : lowerCaseAlphabet) { // Print the first 26 letters
                if (s.equals("z")) {
                    System.out.print("z ");
                } else {
                    System.out.print(s + "  ");
                }
                count++;
            }
            boolean flag = false;
            for (String s : lowerCaseAlphabet) { // Print the remaining letters
                for (String j : lowerCaseAlphabet) {
                    if (count == allPoints.get(0).size() - 1) {
                        System.out.println(s + j + " ");
                        flag = true;
                        break;
                    } else {
                        System.out.print(s + j + " ");
                    }
                    count++;
                }
                if (flag) break;
            }
        } else { // If the number of columns is less than 26
            for (int i = 0; i < allPoints.get(0).size(); i++) { // Print the first column letters
                if (i == allPoints.get(0).size() - 1) System.out.println(lowerCaseAlphabet.get(i) + " ");
                else {
                    System.out.print(lowerCaseAlphabet.get(i) + "  ");
                }
            }
        }
    }

    /**
     * Finds a point with the given name in a list of points. If the point is not found, null is
     * returned. This method is used to check that the user inputted points are valid.
     *
     * @param allPoints the list of points to search in
     * @param move      the name of the point to find
     * @return the first point with the given name, or null if not found
     */
    public static Point findPoint(ArrayList<ArrayList<Point>> allPoints, String move) {
        for (ArrayList<Point> rowPoints : allPoints) {      // Iterate over each point in the list
            for (Point point : rowPoints) {
                if (point.name.equals(move)) {              // If the point is found
                    return point;                           // Return the point
                }
            }
        }
        return null;                      // If the point is not found, return null
    }

    /**
     * Sets the adjacent points for each point in a list of points. This method is used to set the
     * adjacent points for each point in the map.
     *
     * @param allPoints the list of points to set the adjacent points for
     */
    public static void setAdj(ArrayList<ArrayList<Point>> allPoints) {
        int numRows = allPoints.size();
        int numCols = allPoints.get(0).size();
        for (int row = 0; row < numRows; row++) {                  // Iterate over each point in the list
            for (int col = 0; col < numCols; col++) {
                Point p = allPoints.get(row).get(col);             // Get the current point
                for (int dr = -1; dr <= 1; dr++) {                 // Iterate over each adjacent point
                    for (int dc = -1; dc <= 1; dc++) {
                        int nr = row + dr;                         // Get the row and column of the adjacent point
                        int nc = col + dc;
                        if (nr >= 0 && nr < numRows && nc >= 0 && nc < numCols && (dr != 0 || dc != 0)) {
                                                                    // If the adjacent point is in the map and is not the current point
                            p.adj.add(allPoints.get(nr).get(nc));   // Add the adjacent point to the current point's list of adjacent points
                        }
                    }
                }
            }
        }
    }
}