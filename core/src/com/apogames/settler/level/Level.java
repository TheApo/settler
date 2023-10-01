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
