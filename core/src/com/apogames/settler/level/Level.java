package com.apogames.settler.level;

import com.apogames.settler.level.helper.Helper;

public class Level {
    private final byte[][] background;
    private final byte[][] startLevel;
    private final byte[][] numbers;
    private byte[][] fixedNumbers;

    private byte[][] curNumber;
    private byte[][] curBackground;
    private byte[][] region;

    private boolean fixed;

    public Level(byte[][] background, byte[][] numbers, byte[][] startLevel) {
        this.background = background;
        this.numbers = numbers;
        this.startLevel = startLevel;

        this.init();
    }

    public void init() {
        this.curNumber = Helper.cloneArray(startLevel);
        this.fixedNumbers = Helper.cloneArray(startLevel);

        this.curBackground = Helper.cloneArray(startLevel);
        for (int y = 0; y < this.curBackground.length; y++) {
            for (int x = 0; x < this.curBackground[0].length; x++) {
                if (this.curBackground[y][x] > 0) {
                    this.curBackground[y][x] = this.background[y][x];
                }
            }
        }
    }

    public byte[][] getBackground() {
        return background;
    }

    public byte[][] getStartLevel() {
        return startLevel;
    }

    public byte[][] getNumbers() {
        return numbers;
    }

    public byte[][] getCurNumber() {
        return curNumber;
    }

    public byte[][] getCurBackground() {
        return curBackground;
    }

    public byte[][] getRegion() {
        return region;
    }

    public void setRegion(byte[][] region) {
        this.region = region;
    }

    public boolean isSolved() {
        for (int y = 0; y < this.curNumber.length; y++) {
            for (int x = 0; x < this.curNumber[0].length; x++) {
                if (this.curNumber[y][x] != this.numbers[y][x]) {
                    return false;
                }
            }
        }
        return true;
    }

    public byte[][] getError() {
        byte[][] error = new byte[this.curNumber.length][this.curNumber[0].length];
        for (int y = 0; y < this.curNumber.length; y++) {
            for (int x = 0; x < this.curNumber[0].length; x++) {
                if (this.curNumber[y][x] != 0 && hasNeighborSameValue(x, y, error)) {
                    return error;
                }
            }
        }
        return error;
    }

    private boolean hasNeighborSameValue(int startX, int startY, byte[][] error) {
        if (isNeighborSame(startX, startY, -1, -1, error)) {
            return true;
        }
        if (isNeighborSame(startX, startY, -1, 0, error)) {
            return true;
        }
        if (isNeighborSame(startX, startY, -1, +1, error)) {
            return true;
        }
        if (isNeighborSame(startX, startY, 0, -1, error)) {
            return true;
        }
        if (isNeighborSame(startX, startY, 0, +1, error)) {
            return true;
        }
        if (isNeighborSame(startX, startY, +1, -1, error)) {
            return true;
        }
        if (isNeighborSame(startX, startY, +1, 0, error)) {
            return true;
        }
        if (isNeighborSame(startX, startY, +1, +1, error)) {
            return true;
        }

        return false;
    }

    private boolean isNeighborSame(int startX, int startY, int addX, int addY, byte[][] error) {
        if (startX + addX >= 0 && startX + addX < this.curNumber[0].length && startY + addY >= 0 && startY + addY < this.curNumber.length) {
            if (this.curNumber[startY + addY][startX + addX] == this.curNumber[startY][startX]) {
                error[startY][startX] = 1;
                error[startY + addY][startX + addX] = 1;
                return true;
            }
        }
        return false;
    }

    public byte[][] getFixedNumbers() {
        return fixedNumbers;
    }

    public void restart() {
        fixed = false;
        this.curNumber = Helper.cloneArray(this.startLevel);
        this.fixedNumbers = Helper.cloneArray(this.curNumber);
    }

    public void fix() {
        fixed = !fixed;
        if (fixed) {
            this.fixedNumbers = Helper.cloneArray(this.curNumber);
        } else {
            this.fixedNumbers = Helper.cloneArray(this.startLevel);
        }
    }
}
