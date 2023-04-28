import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws FileNotFoundException {
        ArrayList<String> lowerCaseAlphabet = new ArrayList<>(Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h",
                "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"));
//        ArrayList<String> upperCaseAlphabet = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H",
//                "I", "J", "K", "L","M", "N", "O", "P", "Q", "R", "S", "T", "U","V","W","X","Y","Z"));
        Scanner in = new Scanner(new File("src\\input3.txt"));
        int row = in.nextInt();
        int column = in.nextInt();
        ArrayList<ArrayList<Integer>> valueMap = new ArrayList<>();
        for(int i = 0; i <column; i++){
            valueMap.add(new ArrayList<>());
            for (int j = 0; j <row; j++){
                valueMap.get(i).add(in.nextInt());
            }
        }
        ArrayList<ArrayList<Point>> allPoints = setPoints(valueMap, lowerCaseAlphabet);
        for(int i = 0; i< 11; i++){
            String move = in.next();
            Point movedPoint = null;
            if(i == 0) continue;
            if(i == 1){
                printMap(lowerCaseAlphabet, allPoints);
                System.out.println("Add stone "+i+" / 10 to coordinate:");
                movedPoint = findPoint(allPoints, move);
            }
            if(i != 1){
                printMap(lowerCaseAlphabet, allPoints);
                System.out.println("---------------");
                System.out.println("Add stone "+i+" / 10 to coordinate:");
                movedPoint = findPoint(allPoints, move);
            }

            if(movedPoint != null){
                movedPoint.value++;
            }
            else {
                System.out.println("Not a valid step!");
            }
        }
        printMap(lowerCaseAlphabet, allPoints);
        System.out.println("---------------");
        valueMap = new ArrayList<>();
        for(int i = 0; i < allPoints.size(); i++){
            valueMap.add(new ArrayList<>());
            for(Point p : allPoints.get(i)){
                valueMap.get(i).add(p.value);
            }
        }
        setAdj(allPoints);
        for(ArrayList<Point> p : allPoints){
            for(Point point : p){
                point.setPool();
            }
        }
        //TODO  dfs();
    }
    public static void printMap(ArrayList<String> lowerCaseAlphabet, ArrayList<ArrayList<Point>> allPoints){
        for(int j = 0; j< allPoints.size(); j++){
            if(j < 10) System.out.print("  " + j + "  ");
            if (j >= 10 && j < 100) System.out.print(" " + j + "  ");
            if (j >= 100) System.out.print(j + "  ");
            for (int k = 0; k < allPoints.get(j).size(); k++){
                if (k == allPoints.get(j).size() -1){
                    System.out.println(allPoints.get(j).get(k).value);
                }
                else {
                    System.out.print(allPoints.get(j).get(k).value + "  ");
                }
            }
        }
        System.out.print("     ");
        if(allPoints.get(0).size() > lowerCaseAlphabet.size()){
            int count = 0;
            for (String s : lowerCaseAlphabet) {
                if(s.equals("z")) {
                    System.out.print("z ");
                }
                else {
                    System.out.print(s + "  ");
                }
                count++;
            }
            boolean flag = false;
            for(String s : lowerCaseAlphabet){
                for (String j : lowerCaseAlphabet){
                    if(count == allPoints.get(0).size()-1){
                        System.out.println(s+j);
                        flag = true;
                        break;
                    }
                    else{
                        System.out.print(s+j + " ");
                    }
                    count++;
                }
                if(flag) break;
            }
        }
        else {
            for(int i = 0; i < allPoints.get(0).size(); i++){
                if(i == allPoints.get(0).size()-1) System.out.println(lowerCaseAlphabet.get(i));
                else{
                    System.out.print(lowerCaseAlphabet.get(i) + "  ");
                }
            }
        }
    }
    public static Point findPoint(ArrayList<ArrayList<Point>> allPoints, String move){
        Point changedPoint = null;
        boolean found = false;
        for(ArrayList<Point> rowPoints : allPoints){
            for(Point p : rowPoints){
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
    public static ArrayList<ArrayList<Point>> setPoints(ArrayList<ArrayList<Integer>> valueMap,
                                                         ArrayList<String> lowerCaseAlphabet){
        ArrayList<ArrayList<Point>> allPoints = new ArrayList<>();
        for (int i = 0; i < valueMap.size(); i++){
            allPoints.add(new ArrayList<>());
            int count = 0;
            for(int k = 0; k < valueMap.get(i).size(); k++){
                if(k == lowerCaseAlphabet.size()) break;
                allPoints.get(i).add(new Point(i, k ,lowerCaseAlphabet.get(k), valueMap.get(i).get(k)));
                count++;
            }
            if (valueMap.get(i).size() > lowerCaseAlphabet.size()){
                boolean flag = false;
                for (int k = count; k < valueMap.get(i).size(); k++){
                    for(String s : lowerCaseAlphabet){
                        for(String j : lowerCaseAlphabet){
                            if(count == valueMap.get(i).size()){
                                flag = true;
                                break;
                            }
                            allPoints.get(i).add(new Point(i, count,s+j, valueMap.get(i).get(count)));
                            count++;
                        }
                        if (flag) break;
                    }
                }
            }
        }
        return allPoints;
    }
    public static void setAdj(ArrayList<ArrayList<Point>> allPoints){
        for(ArrayList<Point> ap: allPoints){
            for(Point p: ap){
                if(p.row == 0){
                    if(p.column == 0){
                        p.adj.add(allPoints.get(p.row).get(p.column+1));
                        p.adj.add(allPoints.get(p.row+1).get(p.column));
                        p.adj.add(allPoints.get(p.row+1).get(p.column+1));
                    }
                    else if(p.column == ap.size() -1){
                        p.adj.add(allPoints.get(p.row).get(p.column-1));
                        p.adj.add(allPoints.get(p.row+1).get(p.column));
                        p.adj.add(allPoints.get(p.row+1).get(p.column-1));
                    }
                    else{
                        p.adj.add(allPoints.get(p.row).get(p.column-1));
                        p.adj.add(allPoints.get(p.row).get(p.column+1));
                        p.adj.add(allPoints.get(p.row+1).get(p.column-1));
                        p.adj.add(allPoints.get(p.row+1).get(p.column));
                        p.adj.add(allPoints.get(p.row+1).get(p.column+1));
                    }
                }
                else if(p.row == allPoints.size()-1){
                    if(p.column ==0){
                        p.adj.add(allPoints.get(p.row).get(p.column+1));
                        p.adj.add(allPoints.get(p.row-1).get(p.column));
                        p.adj.add(allPoints.get(p.row-1).get(p.column+1));
                    }
                    else if(p.column == ap.size() -1){
                        p.adj.add(allPoints.get(p.row).get(p.column-1));
                        p.adj.add(allPoints.get(p.row-1).get(p.column));
                        p.adj.add(allPoints.get(p.row-1).get(p.column-1));
                    }
                    else{
                        p.adj.add(allPoints.get(p.row).get(p.column-1));
                        p.adj.add(allPoints.get(p.row).get(p.column+1));
                        p.adj.add(allPoints.get(p.row-1).get(p.column-1));
                        p.adj.add(allPoints.get(p.row-1).get(p.column));
                        p.adj.add(allPoints.get(p.row-1).get(p.column+1));
                    }
                }
                else{
                    if(p.column == 0){
                        p.adj.add(allPoints.get(p.row).get(p.column+1));
                        p.adj.add(allPoints.get(p.row+1).get(p.column));
                        p.adj.add(allPoints.get(p.row+1).get(p.column+1));
                        p.adj.add(allPoints.get(p.row-1).get(p.column));
                        p.adj.add(allPoints.get(p.row-1).get(p.column+1));
                    }
                    else if(p.column == ap.size() -1){
                        p.adj.add(allPoints.get(p.row).get(p.column-1));
                        p.adj.add(allPoints.get(p.row+1).get(p.column));
                        p.adj.add(allPoints.get(p.row+1).get(p.column-1));
                        p.adj.add(allPoints.get(p.row-1).get(p.column));
                        p.adj.add(allPoints.get(p.row-1).get(p.column-1));
                    }
                    else{
                        p.adj.add(allPoints.get(p.row).get(p.column-1));
                        p.adj.add(allPoints.get(p.row).get(p.column+1));
                        p.adj.add(allPoints.get(p.row+1).get(p.column-1));
                        p.adj.add(allPoints.get(p.row+1).get(p.column));
                        p.adj.add(allPoints.get(p.row+1).get(p.column+1));
                        p.adj.add(allPoints.get(p.row-1).get(p.column-1));
                        p.adj.add(allPoints.get(p.row-1).get(p.column));
                        p.adj.add(allPoints.get(p.row-1).get(p.column+1));
                    }
                }
            }
        }
    }

}