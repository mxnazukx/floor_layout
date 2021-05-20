package com.rudniev.nerdysofttest.others;


import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.rudniev.nerdysofttest.entities.Point;

public class RoomValidator {
    private static List<Point> pointList = new ArrayList<>();
    private static List<Point> fullPointList = new ArrayList<>();

    public static String validateRoom(List<Point> list) {
        pointList = list;
        String message = enoughWalls() + areWallsDiagonal() + wallsDoNotIntersect() + pointsGoClockwise();

        return message;
    }

    private static String areWallsDiagonal() {
        boolean wallsAreNotDiagonal = true;

        for (int i = 0; i < pointList.size() - 1; i++) {
            Point point = pointList.get(i);
            Point nextPoint = pointList.get(i + 1);
            if (point.getX() != nextPoint.getX() && point.getY() != nextPoint.getY()) wallsAreNotDiagonal = false;
        }
        if (pointList.get(0).getX() != pointList.get(pointList.size() - 1).getX() && pointList.get(0).getY() != pointList.get(pointList.size() - 1).getY())
            wallsAreNotDiagonal = false;

        if (wallsAreNotDiagonal) return "";
        return "Illegal, as it would result in the wall going diagonally. ";
    }

    private static String enoughWalls() {
        boolean enoughWalls = pointList.size() > 3;
        if (enoughWalls) return "";

        return "Illegal, not enough corners. ";
    }

    private static String wallsDoNotIntersect() {
        fillPointList();
        Set<Point> pointSet = new HashSet<>(fullPointList);
        if (pointSet.size() == fullPointList.size()) return "";

        return "Illegal, walls intersect";
    }

    private static void fillPointList() {
        for (int i = 1; i < pointList.size(); i++) {
            Point point = pointList.get(i - 1);
            Point nextPoint = pointList.get(i);
            fillPointsBetween(point, nextPoint);
        }
        fillPointsBetween(pointList.get(pointList.size() - 1), pointList.get(0));
    }

    private static void fillPointsBetween(Point point, Point nextPoint) {
        fullPointList.add(point);
        if (point.getY() != nextPoint.getY()) {
            if (point.getY() - nextPoint.getY() < 0) {
                for (int k = point.getY() + 1; k < nextPoint.getY(); k++) {
                    fullPointList.add(new Point(point.getX(), k));
                }
            } else {
                for (int k = point.getY() - 1; k > nextPoint.getY(); k--) {
                    fullPointList.add(new Point(point.getX(), k));
                }
            }
        } else if (point.getX() != nextPoint.getX()) {
            if (point.getX() - nextPoint.getX() < 0) {
                for (int k = point.getX() + 1; k < nextPoint.getX(); k++) {
                    fullPointList.add(new Point(k, point.getY()));
                }
            } else {
                for (int k = point.getX() - 1; k > nextPoint.getX(); k--) {
                    fullPointList.add(new Point(k, point.getY()));
                }
            }

        }
    }

    private static String pointsGoClockwise() {
        double sum = 0;

        for(int i = 0; i < pointList.size(); i++){
            int xi = pointList.get(i).getX();
            int yp1 = i==pointList.size()-1 ? pointList.get(0).getY() : pointList.get(i+1).getY();
            int ym1 = i == 0 ? pointList.get(pointList.size()-1).getY() : pointList.get(i-1).getY();
            sum += xi*(yp1-ym1);
        }
        sum *= 0.5;
        if(sum > 0) return "Invalid, infinite room";
        return "";
    }

}



