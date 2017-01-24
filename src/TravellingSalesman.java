import processing.core.PApplet;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class TravellingSalesman extends PApplet {
    private static final int NB_OF_NODES = 17;
    ArrayList<Point> nodes = new ArrayList<>();
    ArrayList<Point> currentMinDist = new ArrayList<>();
    float distance = 0;
    float currentMinimalDist = Float.MAX_VALUE;
    float time = 0;
    float percentage = 0.0f;
    int count = 0;
    long possibilites;
    float finish;

    public static void main(String[] args) {
        PApplet.main("TravellingSalesman");
    }

    @Override
    public void settings() {
        size(1200, 800);
    }

    @Override
    public void setup() {
        stroke(255);
        frameRate(180);
        for (int i = 0; i < NB_OF_NODES; i++) {
            int rndX = ((int) random(50, width - 50));
            int rndY = ((int) random(100, height - 50));
            nodes.add(new Point(rndX, rndY));
        }
        for (int i = 0; i < NB_OF_NODES; i++) {
            currentMinDist.add(new Point());
            currentMinDist.set(i, (Point) nodes.get(i).clone());
        }
        background(0);
    }

    public long factorial(long num) {
        if (num == 0) {
            return 1;
        }
        return num * factorial(--num);
    }

    @Override
    public void draw() {
        count++;
        possibilites = factorial(NB_OF_NODES);
        if (count == 19) {
            finish = (100f / percentage) * (((time / 1000) / 60 / 60 / 24 / 365));
        }
        percentage = (100f / possibilites) * count;
        clear();
        textSize(25);
        text("Current minimal distance: " + currentMinimalDist, 50, 50);
        text("TRAVELLING SALESMAN", width / 2 - 100, 50);
        text("Permutation Nr: " + count, 5, height - 50);
//        text(String.format("Remaining time (estimated): %dyears",(int)finish),800,height-50);
        text(String.format("%.10f" + "%%", percentage), width - 300, 50);
        text(String.format("Total number of possible permutations: %d ", possibilites), width - 800, height - 50);
        if (count == possibilites) {
            frameRate(0);
        }
        //initialGrid
        for (Point point : nodes) {
            stroke(255);
            ellipse(point.x, point.y, 20, 20);
        }
        //draw current min distance
        for (int i = 0; i < currentMinDist.size() - 1; i++) {
            stroke(0, 255, 0);
            strokeWeight(3);
            line(currentMinDist.get(i).x, currentMinDist.get(i).y, currentMinDist.get(i + 1).x, currentMinDist.get(i + 1).y);
        }
        distance = 0;
        //lazy. // TODO: 24.01.2017 implement heap's algorithm
        Collections.shuffle(nodes);

        for (int i = 0; i < nodes.size() - 1; i++) {
            strokeWeight(1);
            stroke(100);
            line(nodes.get(i).x, nodes.get(i).y, nodes.get(i + 1).x, nodes.get(i + 1).y);
            distance += dist(nodes.get(i).x, nodes.get(i).y, nodes.get(i + 1).x, nodes.get(i + 1).y);
        }

        if (distance < currentMinimalDist) {
            currentMinimalDist = distance;
            for (int i = 0; i < currentMinDist.size(); i++) {
                currentMinDist.set(i, (Point) nodes.get(i).clone());
            }
        }
        if (count == 20) {
            time = millis();
        }
    }
}
