import javafx.geometry.Point3D;
import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;
import java.util.stream.Collector;
// TODO: to run/compile this project in an IDE:

/**
 * This project was built using the processing 3 lib.
 * https://processing.org/download/
 * Open this project with your IDE,
 * import the core.jar lib from the files you downloaded into your
 * project structure.
 * /


/**
 * Dijkstra's Algorithm
 * Created by Nicolas Dutly
 * mNr.16207557
 * on 16/01/2017, 14:55.
 */

public class DijkstrasAlgorithm extends PApplet {

    final String ANSI_GREEN = "\u001B[32m";
    final String ANSI_RED = "\u001B[31m";
    final String ANSI_RESET = "\u001B[0m";
    int NUM_OF_POINTS = 10;
    Point[] cities;
    float[] distanceToStart;
    Point STARTNODE;
    int startIndex = 0;
    ArrayList<String> tentative = new ArrayList<>();
    ArrayList<String> confirmed = new ArrayList<>();
    float distances[][];
    boolean existentPath[][] = new boolean[NUM_OF_POINTS][NUM_OF_POINTS];
    boolean DEBUG = true;


    public static void main(String args[]) {
        PApplet.main("DijkstrasAlgorithm");
    }

    @Override
    public void settings() {
        size(1500, 900);
//       fullScreen();
    }

    public void initializeCities() {
        cities = new Point[NUM_OF_POINTS];
        distanceToStart = new float[NUM_OF_POINTS];
        existentPath = new boolean[NUM_OF_POINTS][NUM_OF_POINTS];
        STARTNODE = cities[0];
        distances = new float[NUM_OF_POINTS][NUM_OF_POINTS];
        for (int rows = 0; rows < existentPath.length; rows++) {
            for (int col = 0; col < existentPath.length; col++) {
                int rnd = (int) (Math.random() * 100) % 2;
                if (rnd == 1) {
                    existentPath[rows][col] = true;
                    existentPath[col][rows] = true;
                } else {
                    existentPath[rows][col] = false;
                    existentPath[col][rows] = false;
                }
            }
        }

        for (int col = 0; col < existentPath.length; col++) {
            int rnd = (int) (Math.random() * 100) % 2;
            if (rnd == 1) {
                existentPath[0][col] = true;
                existentPath[col][0] = true;
            } else {
                existentPath[0][col] = false;
                existentPath[col][0] = false;
            }
        }

        if (DEBUG) System.out.println("Allowed paths generated...");

        for (int i = 0; i < cities.length; i++) {
            int rndWidth = (int) random(50, width - 50);
            int rndHeight = (int) random(50, height - 50);
            cities[i] = new Point(rndWidth, rndHeight);
        }

        if (DEBUG) System.out.println("Initialized points...");
        STARTNODE = cities[startIndex];
        if (DEBUG) System.out.println("Generating distance Matrix...\n");
        for (int row = 0; row < distances.length; row++) {
            for (int col = 0; col < distances.length; col++) {
                //Fill Distance matrix
                if ((row == 1)) {
                }
                distances[row][col] = dist(((float) cities[row].getX()), (float) cities[row].getY(), (float) cities[col].getX(), (float) cities[col].getY());
                if (existentPath[row][col]) {
                    if (DEBUG) System.out.printf(ANSI_GREEN + "%8.2f" + ANSI_GREEN, distances[row][col]);
                } else {
                    if (DEBUG) System.out.printf(ANSI_RED + "%8.2f" + ANSI_RED, distances[row][col]);
                }
            }
            if (DEBUG) System.out.println();
        }
        System.out.println(ANSI_RESET);
    }


    public void drawBaseVectorsAndPoints() {
        if (DEBUG) System.out.println("Drawing base vectors...");
        for (int row = 0; row < distances.length; row++) {
            for (int col = 0; col < distances.length; col++) {
                if (existentPath[row][col]) {
                    stroke(50);
                    line((float) cities[row].getX(), (float) cities[row].getY(), (float) cities[col].getX(), (float) cities[col].getY());
                    Point city = cities[row];
                    Point city2 = cities[col];
                    if (city2 == STARTNODE) {
                        stroke(255, 0, 0);
                        fill(255, 0, 0);
                        ellipse(city2.x, city2.y, 40, 40);
                        text("START", city2.x + 50, city2.y);
                        stroke(255);
                        fill(255);
                    } else if (city == STARTNODE) {
                        stroke(255, 0, 0);
                        fill(255, 0, 0);
                        ellipse(city.x, city.y, 40, 40);
                        text("START", city.x + 50, city.y);
                        stroke(255);
                        fill(255);
                    } else {
                        stroke(255, 255, 255);
                        fill(255, 255, 255);
                        text(row, city.x + 50, city.y);
                        ellipse(city2.x, city2.y, 20, 20);
                        text(col, city2.x + 50, city2.y);
                    }
                }
            }
        }
    }

    public boolean exitCondition() {
        for (int i = 0; i < confirmed.size(); i++) {
            if (tentative.size() == 0) return true;
        }
        return false;
    }

    private boolean isInConfirmed(int i) {
        for (int n = 0; n < confirmed.size(); n++) {
            if (confirmed.get(n).startsWith(String.valueOf(i))) return true;
        }
        return false;
    }

    private int getNextMinDistance(int currentIndex) {
        float minVal = 0;
        int minIndex = 0;
        for (int i = 0; i < distanceToStart.length; i++) {
            if (distanceToStart[i] != 0 && !isInConfirmed(i) && i != currentIndex) {
                minVal = distanceToStart[i];
                minIndex = i;
                break;
            }
        }
        for (int i = minIndex; i < distanceToStart.length; i++) {
            if (distanceToStart[i] > 0 && distanceToStart[i] < minVal && !isInConfirmed(i) && i != currentIndex) {
                minVal = distanceToStart[i];
                minIndex = i;
            }
        }
        moveToConfirmed(minIndex);
        getNewTentative(minIndex);
        return minIndex;
    }

    private void getNewTentative(int index) {
        for (int i = 0; i < distances.length; i++) {
            if (existentPath[i][index] && !(isInConfirmed(i) || isInTentative(i))) {
                tentative.add(i + "|" + (distanceToStart[index] + distances[index][i]) + "|" + index);
                if (DEBUG) {
                    System.out.printf(ANSI_RESET);
                    System.out.printf("Added city " + ANSI_RED + i + ANSI_RESET);
                    System.out.printf(" passing by" + ANSI_GREEN + " %d" + ANSI_RESET + ":%n", index);
                }
                distanceToStart[i] = distanceToStart[index] + distances[index][i];
            }
        }
    }

    private void moveToConfirmed(int index) {
        for (int i = 0; i < tentative.size(); i++) {
            if (tentative.get(i).startsWith(String.valueOf(index))) {
                String tmp = tentative.get(i);
                String[] tmp2 = tentative.get(i).split("[\\|]");
                flashConfirmed(index, Integer.valueOf(tmp2[2]));
                tentative.remove(i);
                confirmed.add(tmp);
            }
        }
    }

    private int checkAllDistances(int currentIndex) {
        int tmp23 = currentIndex;
        if (DEBUG) System.out.println("checking distance from " + ANSI_GREEN + currentIndex + ANSI_RESET + ":");
        for (int col = 0; col < distances.length; col++) {
            if (existentPath[currentIndex][col] && distances[currentIndex][col] > 0) {
                if (!isInConfirmed(col)) {
                    distanceToStart[col] = distanceToStart[currentIndex] + distances[currentIndex][col];
                }
                flashNode(col);
                if (isInTentative(col)) {
                    for (int i = 0; i < tentative.size(); i++) {
                        if (tentative.get(i).startsWith(String.valueOf(col))) {
                            String tmp[];
                            tmp = tentative.get(i).split("[\\|]");
                            if (Float.valueOf(tmp[1]) > distanceToStart[col]) {
                                if (DEBUG) {
                                    System.out.println("New distance found:\n");
                                    System.out.printf("city " + ANSI_RED + col + ANSI_RESET);
                                    System.out.printf(" passing by" + ANSI_GREEN + " %d" + ANSI_RESET + ":%n" +
                                            "is shorter then passing by: %s%n", currentIndex, tmp[2]);
                                    System.out.println("\nupdating record...");
                                }
                                tentative.set(i, tmp[0] + "|" + distanceToStart[col] + "|" + currentIndex);
                            } else break;
                        }
                    }
                }
                if (!isInTentative(col) && !isInConfirmed(col)) {
                    String str;
                    str = col + "|" + distanceToStart[col] + "|" + currentIndex;
                    tentative.add(str);
                    flashTentative(col, currentIndex);
                    if (DEBUG) {
                        System.out.printf(ANSI_RESET);
                        System.out.printf("Added city " + ANSI_RED + col + ANSI_RESET);
                        System.out.printf(" passing by" + ANSI_GREEN + " %d" + ANSI_RESET + ":%n", currentIndex);
                        System.out.println(str);
                    }
                }
            }
        }
        return getNextMinDistance(currentIndex);
    }

    private void flashConfirmed(int col, int currentIndex) {
        stroke(0, 200, 0);
        fill(0, 200, 0);
        line((float) cities[col].getX(), (float) cities[col].getY(), (float) cities[currentIndex].getX(), (float) cities[currentIndex].getY());

    }

    private void flashTentative(int col, int currentIndex) {
        stroke(0, 200, 0);
        fill(0, 200, 0);
        line((float) cities[col].getX(), (float) cities[col].getY(), (float) cities[currentIndex].getX(), (float) cities[currentIndex].getY());
    }

    private void flashNode(int col) {
        stroke(0, 200, 0);
        fill(0, 200, 0);
        ellipse((float) cities[col].getX(), (float) cities[col].getY(), 20, 20);
        if (cities[col] == STARTNODE) {
            ellipse((float) cities[col].getX(), (float) cities[col].getY(), 40, 40);
        }
    }


    public boolean isInTentative(int endIndex) {
        for (int i = 0; i < tentative.size(); i++) {
            if (tentative.get(i).startsWith(String.valueOf(endIndex))) return true;
        }
        return false;
    }

    public void reset() {
        tentative.clear();
        confirmed.clear();
        textSize(15);
        clear();
        frameRate(30);
        stroke(255);
        strokeWeight(1.2f);
        fill(255);
        clear();
        initializeCities();
        if (DEBUG) System.out.println("Drawing ellipses...");
        drawBaseVectorsAndPoints();
        if (DEBUG) System.out.println("Running Dijkstra...\n");
    }

    public boolean dijkstra() {
        reset();
        text("Click on window to gain focus and press any key to generate new topology", 10, height - 10);
        text("Number of nodes: " + NUM_OF_POINTS + "\n(Press UP-DOWN to change. May cause failure at > 13)", 1060, height - 40);
        if (DEBUG) System.out.println("Drawing ellipses...");
        drawBaseVectorsAndPoints();
        if (DEBUG) System.out.println("Running Dijkstra...\n");
        int currentIndex = startIndex;
        confirmed.add("0|0|0");
        int pass = 0;
        do {
            currentIndex = checkAllDistances(currentIndex);
            pass++;
            if (pass > 200) {
                System.out.println("something went wrong... exiting.");
                System.exit(-1);
            }
        } while (!exitCondition());
        System.out.println(ANSI_GREEN + "\n===============================");
        System.out.println(ANSI_GREEN + "shortest paths marked in GREEN");
        return true;
    }

    @Override
    public void setup() {
        dijkstra();
    }

    public void keyPressed() {
        if (key == CODED) {
            if (keyCode == UP) {
                if (DEBUG) System.out.println("Number of points increased by 1");
                NUM_OF_POINTS++;
            }
            if (keyCode == DOWN) {
                if (DEBUG) System.out.println("Number of points decreased by 1");
                NUM_OF_POINTS--;
            }
        }
        reset();
        dijkstra();
    }

    @Override
    public void draw() {
        if (focused) {
            stroke(0);
            fill(0);
            rect(width - 200, 0, 900, 40);
            stroke(255, 0, 0);
            fill(255, 0, 0);
            fill(0, 255, 0);
            stroke(0, 255, 0);
            text("focus active", width - 200, 30);
        } else {
            stroke(0);
            fill(0);
            rect(width - 200, 0, 900, 40);
            stroke(255, 0, 0);
            fill(255, 0, 0);
            stroke(255, 0, 0);
            text("focus lost", width - 200, 30);
        }

    }
}