package com.apogames.settler.level;

import com.apogames.settler.level.helper.Difficulty;
import com.badlogic.gdx.math.GridPoint2;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class NewLevelCreate {

    private byte[][] myBackground;

    private boolean found = false;
    private boolean finished = false;
    private Level level;

    private Difficulty difficulty;
    private int[][] regionSize;

    public static void main(String[] args) {
//        NewLevelCreate levelCreate = new NewLevelCreate(Difficulty.HARD);
//        long t = System.nanoTime();
//        for (int i = 0; i < 100; i++) {
//            //levelCreate.createLevel(5, 5, 9, 7);
//            levelCreate.createLevel(7, 5, 15, 15);
//        }
//        System.out.println(System.nanoTime()-t);
//        System.out.println();
//
//        LevelCreate oldLevelCreate = new LevelCreate(Difficulty.HARD);
//        t = System.nanoTime();
//        for (int i = 0; i < 100; i++) {
//            //oldLevelCreate.createLevel(5, 5, 9, 7);
//            oldLevelCreate.createLevel(7, 5, 15, 15);
//        }
//        System.out.println(System.nanoTime()-t);
    }

    public NewLevelCreate(Difficulty difficulty) {
        this.difficulty = difficulty;
    }

    private void init() {
        this.finished = false;
        this.found = false;
    }

    public Level getLevel() {
        return level;
    }

    public void createLevel(int xSize, int ySize, int fiveCount, int fourCount) {
        this.init();
        //long t = System.nanoTime();
        this.setBackground(xSize, ySize, fiveCount, fourCount);
        //System.out.println("Nur Erstellung des Levels: "+(System.nanoTime()-t)+" ns");
        //Helper.printArray(this.myBackground);
        //System.out.println();

        FillAndUniqueCheck fill = new FillAndUniqueCheck(this.difficulty);
        if (!fill.fillLevel(myBackground)) {
            createLevel(xSize, ySize, fiveCount, fourCount);
        } else {
            this.level = fill.getLevel();
            this.finished = true;
            //System.out.println("FOUND");
        }
    }

    private void setBackground(int xSize, int ySize, int fiveCount, int fourCount) {
        int startCount = 1;
        this.found = false;
        byte[][] background = new byte[ySize][xSize];
        boolean[][] visited = new boolean[ySize][xSize];

        this.myBackground = new byte[ySize][xSize];

        int countFive = 0;

        while (countFive < xSize * ySize / 5) {
            countFive += 1;

            if (setInFive(startCount, 5, visited, background)) {
                startCount += 1;
            }
        }

        countFive = 0;
        while (countFive < xSize * ySize / 5) {
            countFive += 1;

            if (setInFive(startCount, 4, visited, background)) {
                startCount += 1;
            }
        }

        for (int y = 0; y < background.length; y++) {
            for (int x = 0; x < background[0].length; x++) {
                myBackground[y][x] = background[y][x];
            }
        }

        int maxNumber = fillMissingNumbers(startCount);

        if (!checkIfPossible(startCount)) {
            //System.out.println("Nicht lösbar");
            setBackground(xSize, ySize, fiveCount, fourCount);
            return;
        }

        this.found = false;
        byte[][] newBackground = new byte[myBackground.length][myBackground[0].length];
        correctNumbersToBiom(newBackground, (int)(Math.random() * maxNumber), maxNumber);

        for (int y = 0; y < newBackground.length; y++) {
            for (int x = 0; x < newBackground[0].length; x++) {
                myBackground[y][x] = newBackground[y][x];
            }
        }
    }

    private boolean checkIfPossible(int startCount) {
        this.regionSize = new int[myBackground.length][myBackground[0].length];
        for (int y = 0; y < myBackground.length; y++) {
            for (int x = 0; x < myBackground[0].length; x++) {
                if (this.regionSize[y][x] == 0) {
                    boolean[][] visited = new boolean[myBackground.length][myBackground[0].length];
                    this.regionSize[y][x] = getRegionSize(x, y, myBackground[y][x], myBackground, visited);
                    if (this.regionSize[y][x] > 5) {
                        return false;
                    }
                    if (this.regionSize[y][x] == 0) {
                        this.regionSize[y][x] = 1;
                    }
                    for (int regionY = 0; regionY < myBackground.length; regionY++) {
                        for (int regionX = 0; regionX < myBackground[0].length; regionX++) {
                            if (visited[regionY][regionX]) {
                                this.regionSize[regionY][regionX] = this.regionSize[y][x];
                            }
                        }
                    }
                }
            }
        }

        for (int y = 0; y < myBackground.length; y++) {
            for (int x = 0; x < myBackground[0].length; x++) {
                boolean[][] visited = new boolean[myBackground.length][myBackground[0].length];
                visited[y][x] = true;
                fillVisitedWithMyValues(myBackground[y][x], visited, x, y);
                Set<Byte> check = new HashSet<>();
                check.add(myBackground[y][x]);
                for (int nextY = -1; nextY <= 1; nextY += 1) {
                    for (int nextX = -1; nextX <= 1; nextX += 1) {
                        if (nextX != 0 || nextY != 0) {
                            if (x + nextX < 0 || x + nextX >= myBackground[0].length || y + nextY < 0 || y + nextY >= myBackground.length) {
                                continue;
                            }
                            if (check.contains(myBackground[y+nextY][x+nextX])) {
                                continue;
                            }
                            check.add(myBackground[y+nextY][x+nextX]);
                            if (fillVisitedWithMyValues(myBackground[y+nextY][x+nextX], visited, x, y) && (regionSize[y][x] == 1 || regionSize[y+nextY][x+nextX] >= regionSize[y][x])) {
                                return false;
                            }
                        }
                    }
                }

            }
        }

        PossibleValueCheck value = new PossibleValueCheck(myBackground, regionSize);
        boolean result = value.isSolveable();

        return result;
    }

    private boolean fillVisitedWithMyValues(byte value, boolean[][] visited, int checkX, int checkY) {
        boolean result = true;
        for (int y = 0; y < myBackground.length; y++) {
            for (int x = 0; x < myBackground[0].length; x++) {
                if (myBackground[y][x] == value) {
                    visited[y][x] = true;
                    if (Math.abs(checkX - x) > 1 || Math.abs(checkY - y) > 1) {
                        result = false;
                    }
                }
            }
        }
        return result;
    }

    private int getRegionSize(int x, int y, int region, byte[][] background, boolean[][] visited) {
        int result = 0;
        if (isOk(x + 1, y, region, background, visited)) {
            visited[y][x+1] = true;
            result += 1 + getRegionSize(x+1, y, region, background, visited);
        }
        if (isOk(x - 1, y, region, background, visited)) {
            visited[y][x-1] = true;
            result += 1 + getRegionSize(x-1, y, region, background, visited);
        }

        if (isOk(x, y - 1, region, background, visited)) {
            visited[y - 1][x] = true;
            result += 1 + getRegionSize(x, y - 1, region, background, visited);
        }

        if (isOk(x, y + 1, region, background, visited)) {
            visited[y + 1][x] = true;
            result += 1 + getRegionSize(x, y + 1, region, background, visited);
        }

        return result;
    }

    private boolean isOk(int x, int y, int region, byte[][] background, boolean[][] visitedRegion) {
        if (x < 0 || x >= background[0].length || y < 0 || y >= background.length) {
            return false;
        }
        return background[y][x] == region && !visitedRegion[y][x];
    }

    private boolean isOnlyAround(int x, int y, int addX, int addY, boolean[][] visited, int value) {
        if (x + addX < 0 || x + addX >= myBackground[0].length || y + addY < 0 || y + addY >= myBackground.length) {
            return false;
        }
        if (visited[y+addY][x+addX]) {
            return true;
        }
        int myValue = value;
        if (myValue < 0) {
            myValue = myBackground[y+addY][x+addX];
        }
        visited[y+addY][x+addX] = true;
        if (myValue != myBackground[y+addY][x+addX]) {
            return true;
        }
        if (Math.abs(addX) > 1 || Math.abs(addY) > 1) {
            return false;
        }

        boolean result = isOnlyAround(x, y, addX - 1, addY, visited, myValue);
        result &= isOnlyAround(x, y, addX + 1, addY, visited, myValue);
        result &= isOnlyAround(x, y, addX, addY - 1, visited, myValue);
        result &= isOnlyAround(x, y, addX, addY + 1, visited, myValue);

        return result;
    }

    private byte getValue(int x, int y, byte[][] background) {
        if (x < 0 || x >= background[0].length || y < 0 || y >= background.length) {
            return -1;
        }
        return background[y][x];
    }

    private void correctNumbersToBiom(byte[][] newBackground, int startValueToReplace, int maxValue) {
        if (found) {
            return;
        }
        if (checkFound(newBackground)) {
            return;
        }

        boolean[][] visitedNow = new boolean[myBackground.length][myBackground[0].length];

        for (int y = 0; y < myBackground.length; y++) {
            for (int x = 0; x < myBackground[0].length; x++) {
                if (myBackground[y][x] == startValueToReplace && !visitedNow[y][x]) {
                    byte value = (byte)(Math.random() * 4 + 1);
                    for (int v = 0; v < 3; v++) {
                        fillWithNumber(x, y, value, visitedNow, newBackground, startValueToReplace);
                        if (checkIfValid(value, visitedNow, newBackground)) {
                            int nextNumber = startValueToReplace + 1;
                            if (nextNumber > maxValue) nextNumber = 1;
                            correctNumbersToBiom(newBackground, nextNumber, maxValue);

                            if (found) {
                                return;
                            }
                            checkFound(newBackground);
                        }

                        if (found) {
                            return;
                        }
                        resetValues(x, y, (byte) 0, newBackground, startValueToReplace);
                        visitedNow = new boolean[myBackground.length][myBackground[0].length];

                        value += 1;
                        if (value > 4) {
                            value = (byte)(1);
                        }
                    }
                    return;
                }
            }
        }
    }

    private boolean checkFound(byte[][] newBackground) {
        boolean found = true;
        this.found = false;
        for (int y = 0; y < newBackground.length; y++) {
            for (int x = 0; x < newBackground[0].length; x++) {
                if (newBackground[y][x] == 0) {
                    found = false;
                    break;
                }
            }
            if (!found) {
                break;
            }
        }
        if (found) {
            this.found = true;
        }
        return this.found;
    }

    private ArrayList<Byte> getPossibleValues(int x, int y, boolean[][] visitedNow, byte[][] background) {
        HashSet<Byte> allValues = new HashSet<>();
        allValues.add((byte) 1);
        allValues.add((byte) 2);
        allValues.add((byte) 3);
        allValues.add((byte) 4);

        allValues.remove(getValue(x - 1, y - 1, background, visitedNow));
        allValues.remove(getValue(x, y - 1, background, visitedNow));
        allValues.remove(getValue(x + 1, y - 1, background, visitedNow));

        allValues.remove(getValue(x - 1, y, background, visitedNow));
        allValues.remove(getValue(x + 1, y, background, visitedNow));

        allValues.remove(getValue(x - 1, y + 1, background, visitedNow));
        allValues.remove(getValue(x, y + 1, background, visitedNow));
        allValues.remove(getValue(x + 1, y + 1, background, visitedNow));

        return new ArrayList<>(allValues);
    }

    private byte getValue(int x, int y, byte[][] background, boolean[][] visitedNow) {
        if (x < 0 || x >= background[0].length || y < 0 || y >= background.length || visitedNow[y][x]) {
            return -1;
        }
        return background[y][x];
    }

    private int fillMissingNumbers(int number) {
        this.finished = false;

        byte myNumber = (byte)number;

        for (int y = 0; y < myBackground.length; y++) {
            for (int x = 0; x < myBackground[0].length; x++) {
                if (myBackground[y][x] == 0) {
                    boolean[][] visitedNow = new boolean[myBackground.length][myBackground[0].length];

                    fillWithNumber(x, y, myNumber, visitedNow, this.myBackground,-1);
                    myNumber += 1;
                }
            }
        }
        this.finished = true;
        return (myNumber - 1);
    }

    private boolean resetValues(int x, int y, byte num, byte[][] background, int checkValue) {
        if (x < 0 || y < 0 || x >= this.myBackground[0].length || y >= this.myBackground.length) {
            return true;
        }
        if (checkValue > 0 && this.myBackground[y][x] != checkValue) {
            return false;
        }
        if (background[y][x] == num) {
            return false;
        }

        background[y][x] = num;

        boolean fill = this.resetValues(x - 1, y, num, background, checkValue);
        fill &= this.resetValues(x + 1, y, num, background, checkValue);
        fill &= this.resetValues(x, y - 1, num, background, checkValue);
        fill &= this.resetValues(x, y + 1, num, background, checkValue);

        return fill;
    }

    private boolean fillWithNumber(int x, int y, byte num, boolean[][] visitedNow, byte[][] background, int checkValue) {
        if (x < 0 || y < 0 || x >= this.myBackground[0].length || y >= this.myBackground.length) {
            return true;
        }
        if (background[y][x] != 0 && (checkValue < 0 || num != 0)) {
            return true;
        }
        if (checkValue > 0 && this.myBackground[y][x] != checkValue) {
            return false;
        }

        background[y][x] = num;
        visitedNow[y][x] = true;

        boolean fill = this.fillWithNumber(x - 1, y, num, visitedNow, background, checkValue);
        fill &= this.fillWithNumber(x + 1, y, num, visitedNow, background, checkValue);
        fill &= this.fillWithNumber(x, y - 1, num, visitedNow, background, checkValue);
        fill &= this.fillWithNumber(x, y + 1, num, visitedNow, background, checkValue);

        return fill;
    }

    private boolean checkIfValid(byte value, boolean[][] visitedNow, byte[][] background) {
        for (int y = 0; y < visitedNow.length; y++) {
            for (int x = 0; x < visitedNow[0].length; x++) {
                if (visitedNow[y][x]) {
                    if (!getPossibleValues(x, y, visitedNow, background).contains(value)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    private boolean setInFive(int value, int size, boolean[][] visited, byte[][] background) {
        int startX = (int)(Math.random() * background[0].length);
        int startY = (int)(Math.random() * background.length);

        int count = 0;
        while ((count < 100) && (background[startY][startX] != 0 || visited[startY][startX])) {
            startX = (int)(Math.random() * background[0].length);
            startY = (int)(Math.random() * background.length);
            count += 1;
        }
        if (count >= 100) {
            //System.out.println("Nichts gefunden");
            found = false;
            return false;
        }

        found = false;
        tryPlaceValue(startX, startY, (byte) value, (int)(Math.random() * 4), size - 1, visited, background);
        if (found) {
            count = 0;
            for (int y = 0; y < background.length; y++) {
                for (int x = 0; x < background[0].length; x++) {
                    if (background[y][x] == value && !visited[y][x]) {
                        visited[y][x] = true;
                        count += 1;
                    }
                }
            }
            if (count > size) {
                return false;
            }
        } else {
            for (int y = 0; y < background.length; y++) {
                for (int x = 0; x < background[0].length; x++) {
                    if (background[y][x] != 0 && !visited[y][x]) {
                        background[y][x] = 0;
                    }
                }
            }
        }
        return this.found;
    }

    private void tryPlaceValue(int x, int y, byte value, int direction, int size, boolean[][] visited, byte[][] background) {
        if (size < 0 || this.found) {
            this.found = true;
            return;
        }
        if (x < 0 || x >= background[0].length || y < 0 || y >= background.length) {
            return;
        }

        if (background[y][x] != 0 || visited[y][x]) {
            return;
        }
        background[y][x] = value;

        int curDirection = direction;
        for (int i = 0; i < 4; i++) {
            GridPoint2 forDirection = getForDirection(curDirection);
            int addX = forDirection.x;
            int addY = forDirection.y;

            tryPlaceValue(x + addX, y + addY, value, (int)(Math.random() * 4), size - 1, visited, background);

            if (this.found) {
                return;
            }

            curDirection += 1;
            if (curDirection > 3) {
                curDirection = 0;
            }
        }

        background[y][x] = 0;
    }

    private GridPoint2 getForDirection(int direction) {
        if (direction == 1) {
            return new GridPoint2(1, 0);
        } else if (direction == 2) {
            return new GridPoint2(0, 1);
        } else if (direction == 3) {
            return new GridPoint2(-1, 0);
        }
        return new GridPoint2(0, -1);
    }
}
