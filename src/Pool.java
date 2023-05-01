import java.util.ArrayList;
import java.util.Arrays;

public class Pool implements Comparable<Pool>{
    public String name;
    public ArrayList<Point> points = new ArrayList<>();
    public ArrayList<Point> surroundingPoints = new ArrayList<>();
    Pool(){}
    public void addPoint(Point point){
        points.add(point);
        setSurroundingPoints(point);
    }
    public void setSurroundingPoints(Point p){
        for(Point ps : p.adj){
            if(!surroundingPoints.contains(ps)) surroundingPoints.add(ps);
        }
        for(Point pj : points){
            surroundingPoints.remove(pj);
        }
    }
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
    public Point findTopMostLeftMostPoint(){
        Point topMostLeftMostPoint = points.get(0);
        for(Point p : points){
            if(p.row < topMostLeftMostPoint.row) topMostLeftMostPoint = p;
            else if(p.row == topMostLeftMostPoint.row && p.column < topMostLeftMostPoint.column) topMostLeftMostPoint = p;
        }
        return topMostLeftMostPoint;
    }

    @Override
    public int compareTo(Pool o) {
        if(findTopMostLeftMostPoint() == o.findTopMostLeftMostPoint()) return 0;
        else if(findTopMostLeftMostPoint().row < o.findTopMostLeftMostPoint().row) return -1;
        else if(findTopMostLeftMostPoint().row == o.findTopMostLeftMostPoint().row && findTopMostLeftMostPoint().column < o.findTopMostLeftMostPoint().column) return -1;
        else return 1;
    }
}

