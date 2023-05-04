import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<String> lowerCaseAlphabet = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h",
                "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));
        Scanner in = new Scanner(new File("src\\input.txt"));
        int row = in.nextInt();
        int column = in.nextInt();
        ArrayList<ArrayList<Integer>> valueMap = new ArrayList<>();
        for (int i = 0; i < column; i++) {
            valueMap.add(new ArrayList<>());
            for (int j = 0; j < row; j++) {
                valueMap.get(i).add(in.nextInt());
            }
        }
        in.close();
        ArrayList<ArrayList<Point>> allPoints = setPoints(valueMap, lowerCaseAlphabet);
        printMap(lowerCaseAlphabet, allPoints);
        Scanner input = new Scanner(System.in);
        for (int i = 1; i < 11; i++) {
            System.out.print("Add stone " + i + " / 10 to coordinate:");
            String move = input.next();
            Point movedPoint = findPoint(allPoints, move);
            if (movedPoint != null) {
                movedPoint.value++;
                printMap(lowerCaseAlphabet, allPoints);
                System.out.println("---------------");
            } else {
                System.out.println("Not a valid step!");
                i--;
            }
        }
        input.close();
        setAdj(allPoints);
        ArrayList<Pool> allPools = new ArrayList<>();
        for (int i = 1; i < allPoints.size(); i++) {
            for (int j = 1; j < allPoints.get(0).size(); j++) {
                Point currentPoint = allPoints.get(i).get(j);
                Pool pool = new Pool();
                if (dfsIterative(currentPoint, currentPoint.value, new int[allPoints.size()][allPoints.get(0).size()], pool)) {
                    allPools.add(pool);
                }
            }
        }
        removeDuplicate(allPools);
        Collections.reverse(allPools);
        removeDuplicate(allPools);
        Collections.sort(allPools);
        double ans = 0;
        for (int i = 0; i < allPools.size(); i++) {
            allPools.get(i).setName(i);
            ans += allPools.get(i).calculateVolume();
        }
        printMap(lowerCaseAlphabet, allPoints);
        System.out.printf("Final score: " + "%.2f", ans);
    }

    public static ArrayList<ArrayList<Point>> setPoints(ArrayList<ArrayList<Integer>> valueMap,
                                                        ArrayList<String> lowerCaseAlphabet) {
        ArrayList<ArrayList<Point>> allPoints = new ArrayList<>();
        for (int i = 0; i < valueMap.size(); i++) {
            allPoints.add(new ArrayList<>());
            int count = 0;
            for (int k = 0; k < valueMap.get(i).size(); k++) {
                if (k == lowerCaseAlphabet.size()) break;
                allPoints.get(i).add(new Point(i, k, lowerCaseAlphabet.get(k), valueMap.get(i).get(k)));
                count++;
            }
            if (valueMap.get(i).size() > lowerCaseAlphabet.size()) {
                boolean flag = false;
                for (int k = count; k < valueMap.get(i).size(); k++) {
                    for (String s : lowerCaseAlphabet) {
                        for (String j : lowerCaseAlphabet) {
                            if (count == valueMap.get(i).size()) {
                                flag = true;
                                break;
                            }
                            allPoints.get(i).add(new Point(i, count, s + j, valueMap.get(i).get(count)));
                            count++;
                        }
                        if (flag) break;
                    }
                }
            }
        }
        return allPoints;
    }

    public static void removeDuplicate(ArrayList<Pool> allPools) {
        for (int i = 0; i < allPools.size(); i++) {
            for (int j = i + 1; j < allPools.size(); j++) {
                if (allPools.get(i).points.containsAll(allPools.get(j).points)) {
                    allPools.remove(allPools.get(j));
                    j--;
                }
            }
        }
    }

    public static boolean dfsIterative(Point p, int maxValue, int[][] visited, Pool pool) {
        Stack<Point> stack = new Stack<>();
        stack.push(p);
        while (!stack.empty()) {
            Point point = stack.pop();
            pool.addPoint(point);
            if (visited[point.row][point.column] == 1) continue;
            if (point.row == 0 || point.column == 0 || point.row == visited.length - 1 || point.column == visited[0].length - 1)
                return false;
            visited[point.row][point.column] = 1;
            for (Point pAdj : point.adj) {
                if (visited[pAdj.row][pAdj.column] == 0 && pAdj.value <= maxValue) {
                    stack.push(pAdj);
                }
            }
        }
        return true;
    }

    public static void printMap(ArrayList<String> lowerCaseAlphabet, ArrayList<ArrayList<Point>> allPoints) {
        int nameCount = 0;
        for (int j = 0; j < allPoints.size(); j++) {
            if (j < 10) {
                if (allPoints.get(j).get(0).value < 10)
                    System.out.print("  " + j + "  ");
                else
                    System.out.print("  " + j + " ");
            }
            if (j >= 10 && j < 100) {
                if (allPoints.get(j).get(0).value < 10)
                    System.out.print(" " + j + "  ");
                else
                    System.out.print(" " + j + " ");
            }
            if (j >= 100) {
                if (allPoints.get(j).get(0).value < 10)
                    System.out.print(j + "  ");
                else
                    System.out.print(j + " ");
            }
            for (int k = 0; k < allPoints.get(j).size(); k++) {
                if (k == allPoints.get(j).size() - 1) {
                    System.out.println(allPoints.get(j).get(k).value);
                } else {
                    if (allPoints.get(j).get(k).poolName == null) {
                        if (nameCount > 25 && allPoints.get(j).get(k + 1).poolName != null) {
                            System.out.print(allPoints.get(j).get(k).value + " ");
                        }
                        else {
                            if (allPoints.get(j).get(k + 1).value < 10)
                                System.out.print(allPoints.get(j).get(k).value + "  ");
                            else
                                System.out.print(allPoints.get(j).get(k).value + " ");
                        }
                    }
                    else {
                        if (allPoints.get(j).get(k + 1).value < 10)
                            System.out.print(allPoints.get(j).get(k).poolName + "  ");
                        else
                            System.out.print(allPoints.get(j).get(k).poolName + " ");
                        nameCount++;
                    }
                }
            }
        }
        System.out.print("     ");
        if (allPoints.get(0).size() > lowerCaseAlphabet.size()) {
            int count = 0;
            for (String s : lowerCaseAlphabet) {
                if (s.equals("z")) {
                    System.out.print("z ");
                } else {
                    System.out.print(s + "  ");
                }
                count++;
            }
            boolean flag = false;
            for (String s : lowerCaseAlphabet) {
                for (String j : lowerCaseAlphabet) {
                    if (count == allPoints.get(0).size() - 1) {
                        System.out.println(s + j);
                        flag = true;
                        break;
                    } else {
                        System.out.print(s + j + " ");
                    }
                    count++;
                }
                if (flag) break;
            }
        } else {
            for (int i = 0; i < allPoints.get(0).size(); i++) {
                if (i == allPoints.get(0).size() - 1) System.out.println(lowerCaseAlphabet.get(i));
                else {
                    System.out.print(lowerCaseAlphabet.get(i) + "  ");
                }
            }
        }
    }

    public static Point findPoint(ArrayList<ArrayList<Point>> allPoints, String move) {
        Point changedPoint = null;
        boolean found = false;
        for (ArrayList<Point> rowPoints : allPoints) {
            for (Point p : rowPoints) {
                if (p.name.equals(move)) {
                    changedPoint = p;
                    found = true;
                    break;
                }
            }
            if (found) break;
        }
        return changedPoint;
    }

    public static void setAdj(ArrayList<ArrayList<Point>> allPoints) {
        for (ArrayList<Point> ap : allPoints) {
            for (Point p : ap) {
                try {
                    p.adj.add(allPoints.get(p.row).get(p.column - 1));
                    p.adj.add(allPoints.get(p.row).get(p.column + 1));
                    p.adj.add(allPoints.get(p.row + 1).get(p.column - 1));
                    p.adj.add(allPoints.get(p.row + 1).get(p.column));
                    p.adj.add(allPoints.get(p.row + 1).get(p.column + 1));
                    p.adj.add(allPoints.get(p.row - 1).get(p.column - 1));
                    p.adj.add(allPoints.get(p.row - 1).get(p.column));
                    p.adj.add(allPoints.get(p.row - 1).get(p.column + 1));
                }
                catch (IndexOutOfBoundsException ignored){
                }
            }
        }
    }
}