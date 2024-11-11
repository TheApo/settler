package com.apogames.settler.game.tiwanaku;

import com.apogames.settler.Constants;
import com.apogames.settler.asset.AssetLoader;
import com.apogames.settler.backend.DrawString;
import com.apogames.settler.backend.SequentiallyThinkingScreenModel;
import com.apogames.settler.common.Localization;
import com.apogames.settler.entity.ApoButton;
import com.apogames.settler.game.MainPanel;
import com.apogames.settler.level.LevelCreate;
import com.apogames.settler.level.Level;
import com.apogames.settler.level.NewLevelCreate;
import com.apogames.settler.level.Solve;
import com.apogames.settler.level.helper.Difficulty;
import com.apogames.settler.level.helper.FillHelp;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

public class Tiwanaku extends SequentiallyThinkingScreenModel {

    private final int addX = 10;
    private final int addY = 100;

    private int addXScale = 0;

    public static final String FUNCTION_TIWANAKU_BACK = "TIWANAKU_QUIT";
    public static final String FUNCTION_NEW_LEVEL = "TIWANAKU_NEW_LEVEL";
    public static final String FUNCTION_RESTART = "TIWANAKU_RESTART";
    public static final String FUNCTION_FIX = "TIWANAKU_FIX";
    public static final String FUNCTION_HELP = "TIWANAKU_HELP";

    public static final String FUNCTION_FINISH_BACK = "TIWANAKU_FINISH_QUIT";
    public static final String FUNCTION_FINISH_NEW_LEVEL = "TIWANAKU_FINISH_NEW_LEVEL";
    public static final String FUNCTION_FINISH_RESTART = "TIWANAKU_FINISH_RESTART";

    private final boolean[] keys = new boolean[256];

    private boolean isPressed = false;

    private NewLevelCreate levelCreate;

    private Level level;

    private LevelSize levelSize = LevelSize.SMALL;

    private boolean newLevel = false;

    private Solve solve;

    private GameState gameState = GameState.PLAY;

    private boolean help;

    private Difficulty difficulty;

    private byte[][] error = null;

    public Tiwanaku(final MainPanel game) {
        super(game);
    }

    public void setNeededButtonsVisible() {
        getMainPanel().getButtonByFunction(FUNCTION_TIWANAKU_BACK).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_RESTART).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_FIX).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_HELP).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_BACK).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_RESTART).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_NEW_LEVEL).setVisible(false);
    }

    public void setValues(LevelSize levelSize, boolean newLevel, Difficulty difficulty) {
        this.levelSize = levelSize;
        this.newLevel = newLevel;
        this.difficulty = difficulty;
    }

    @Override
    public void init() {
        if (getGameProperties() == null) {
            setGameProperties(new TiwanakuPreferences(this));
            loadProperties();
        }
        if (this.levelCreate == null || this.newLevel) {
            this.createNewLevel();
        }

        this.gameState = GameState.PLAY;
        this.getMainPanel().resetSize(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        this.setNeededButtonsVisible();
    }

    @Override
    public void keyPressed(int keyCode, char character) {
        super.keyPressed(keyCode, character);

        keys[keyCode] = true;
    }

    private void createNewLevel() {
        //this.levelCreate = new NewLevelCreate(this.difficulty);
        this.levelCreate = new NewLevelCreate(this.difficulty);

        this.levelCreate.createLevel(this.levelSize.getX(), this.levelSize.getY(), this.levelSize.getFiveCount(), this.levelSize.getFourCount());
        this.level = this.levelCreate.getLevel();
        this.setSolve();
        this.gameState = GameState.PLAY;

        this.getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(true);

        this.addXScale = (int)((9 - this.level.getBackground()[0].length) * AssetLoader.backgroundTextureRegion[4].getRegionWidth() / 2f);
        if (this.level.getBackground().length > 5) {
            int tileSize = 640 / this.level.getCurNumber().length;
            this.addXScale = Constants.GAME_WIDTH/2 - AssetLoader.hudRightTextureRegion.getRegionWidth()/2 - (tileSize * this.level.getCurNumber().length)/2;
        }

        setNeededButtonsVisible();
    }

    private void setSolve() {
        this.solve = new Solve(this.level.getBackground());
        this.solve.setPossibleValues(this.level.getCurNumber());
    }

    @Override
    public void keyButtonReleased(int keyCode, char character) {
        super.keyButtonReleased(keyCode, character);

        if (keyCode == Input.Keys.N) {
            this.createNewLevel();
        }

        keys[keyCode] = false;
    }

    public void mouseMoved(int mouseX, int mouseY) {
    }

    public void mouseButtonReleased(int mouseX, int mouseY, boolean isRightButton) {
        this.isPressed = false;

        if (this.gameState == GameState.SOLVED) {
            return;
        }

        int tileSize = AssetLoader.backgroundTextureRegion[4].getRegionWidth();
        if (this.level.getCurNumber().length > 5) {
            tileSize = 640 / this.level.getCurNumber().length;
        }
        if (this.addX + addXScale < mouseX && this.addX + addXScale + tileSize * this.level.getBackground()[0].length > mouseX &&
            this.addY < mouseY && this.addY + tileSize * this.level.getBackground().length > mouseY) {
            int x = (mouseX - this.addX - addXScale) / tileSize;
            int y = (mouseY - this.addY) / tileSize;
            if (this.error != null) {
                this.error = null;
            }
            if (this.level.getFixedNumbers()[y][x] == 0) {
                byte add = (byte) (isRightButton ? -1 : 1);
                this.level.getCurNumber()[y][x] = (byte) (this.level.getCurNumber()[y][x] + add);
                if (this.level.getCurNumber()[y][x] > this.level.getRegion()[y][x]) {
                    this.level.getCurNumber()[y][x] = 0;
                }
                if (this.level.getCurNumber()[y][x] < 0) {
                    this.level.getCurNumber()[y][x] = this.level.getRegion()[y][x];
                }
                this.setSolve();
                if (this.level.isSolved()) {
                    this.gameSolved();
                }
            }
        }
    }

    private void gameSolved() {
        this.gameState = GameState.SOLVED;
        this.setButtonsVisibility();
//        this.getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(true);
//        this.getMainPanel().getButtonByFunction(FUNCTION_RESTART).setVisible(true);
//        this.getMainPanel().getButtonByFunction(FUNCTION_FIX).setVisible(true);
    }

    public void mousePressed(int x, int y, boolean isRightButton) {
        if (isRightButton && !this.isPressed) {
            this.isPressed = true;
        }
    }

    public void mouseDragged(int x, int y, boolean isRightButton) {
        if (isRightButton) {
            if (!this.isPressed) {
                this.mousePressed(x, y, isRightButton);
            }
        }
    }

    @Override
    public void mouseButtonFunction(String function) {
        super.mouseButtonFunction(function);
        switch (function) {
            case Tiwanaku.FUNCTION_TIWANAKU_BACK:
            case Tiwanaku.FUNCTION_FINISH_BACK:
                quit();
                break;
            case Tiwanaku.FUNCTION_NEW_LEVEL:
            case Tiwanaku.FUNCTION_FINISH_NEW_LEVEL:
                createNewLevel();
                break;
            case Tiwanaku.FUNCTION_RESTART:
            case Tiwanaku.FUNCTION_FINISH_RESTART:
                restartLevel();
                break;
            case Tiwanaku.FUNCTION_FIX:
                saveCurrentFixLevel();
                break;
            case Tiwanaku.FUNCTION_HELP:
                this.help = !this.help;
                //checkLevel();
                break;
        }
    }

    private void checkLevel() {
        this.error = this.level.getError();
    }

    private void saveCurrentFixLevel() {
        this.level.fix();
    }

    private void restartLevel() {
        this.level.restart();
        this.gameState = GameState.PLAY;
        this.help = false;
        this.setNeededButtonsVisible();
    }

    private void setButtonsVisibility() {
        getMainPanel().getButtonByFunction(FUNCTION_TIWANAKU_BACK).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_RESTART).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_FIX).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_NEW_LEVEL).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_HELP).setVisible(false);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_BACK).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_RESTART).setVisible(true);
        getMainPanel().getButtonByFunction(FUNCTION_FINISH_NEW_LEVEL).setVisible(true);
    }

    public void mouseWheelChanged(int changed) {
    }

    @Override
    protected void quit() {
        getMainPanel().changeToMenu();
    }

    @Override
    public void doThink(float delta) {

    }

    @Override
    public void render() {
        getMainPanel().getRenderer().begin(ShapeRenderer.ShapeType.Filled);

        getMainPanel().getRenderer().setColor(Constants.COLOR_PURPLE_MENU[0], Constants.COLOR_PURPLE_MENU[1], Constants.COLOR_PURPLE_MENU[2], 1f);

        getMainPanel().getRenderer().end();

        getMainPanel().spriteBatch.begin();

        float hudStartX = Constants.GAME_WIDTH - 5 - AssetLoader.hudRightTextureRegion.getRegionWidth();
        getMainPanel().spriteBatch.draw(AssetLoader.hudRightTextureRegion, hudStartX, 5);

        int startY = 130;
        getMainPanel().drawString("Level:", hudStartX + 30, startY, Constants.COLOR_WHITE, AssetLoader.font20, DrawString.BEGIN, false, false);
        getMainPanel().drawString(this.levelSize.getX()+"x"+this.levelSize.getY(), hudStartX + 60, startY + 30, Constants.COLOR_WHITE, AssetLoader.font20, DrawString.BEGIN, false, false);

        getMainPanel().drawString(Localization.getInstance().getCommon().get("menu_difficulty")+":", hudStartX + 30, startY + 80, Constants.COLOR_WHITE, AssetLoader.font15, DrawString.BEGIN, false, false);
        getMainPanel().drawString(this.difficulty.toString(), hudStartX + 60, startY + 110, Constants.COLOR_WHITE, AssetLoader.font20, DrawString.BEGIN, false, false);

        getMainPanel().drawString(Localization.getInstance().getCommon().get("hud_rules")+":", hudStartX + 30, startY + 180, Constants.COLOR_WHITE, AssetLoader.font20, DrawString.BEGIN, false, false);
        String[] rules = Localization.getInstance().getCommon().get("hud_rules_text").split(";");
        for (int i = 0; i < rules.length; i++) {
            getMainPanel().drawString(rules[i], hudStartX + 30, startY + 210 + i * 20, Constants.COLOR_WHITE, AssetLoader.font15, DrawString.BEGIN, false, false);
        }


        getMainPanel().spriteBatch.draw(AssetLoader.titleTextureRegion, (Constants.GAME_WIDTH - AssetLoader.hudRightTextureRegion.getRegionWidth() - AssetLoader.titleTextureRegion.getRegionWidth())/2f, 5);
        getMainPanel().drawString(Constants.PROPERTY_NAME, (Constants.GAME_WIDTH - AssetLoader.hudRightTextureRegion.getRegionWidth())/2f, 44, Constants.COLOR_WHITE, AssetLoader.font30, DrawString.MIDDLE, true, false);

        int tileSizeWidth = AssetLoader.backgroundTextureRegion[4].getRegionWidth();
        int tileSizeHeight = AssetLoader.backgroundTextureRegion[4].getRegionHeight();
        float scale = 1.0f;
        if (this.level.getCurNumber().length > 5) {
            tileSizeWidth = 640 / this.level.getCurNumber().length;
            tileSizeHeight = (int)(tileSizeWidth * 1.5f);
            scale = (float)tileSizeWidth / AssetLoader.backgroundTextureRegion[4].getRegionWidth();
        }

        for (int y = 0; y < this.level.getCurBackground().length; y++) {
            for (int x = 0; x < this.level.getCurBackground()[0].length; x++) {
                //if (this.level.getCurBackground()[y][x] > 0) {
                if (this.level.getBackground()[y][x] > 0) {
                    getMainPanel().spriteBatch.draw(AssetLoader.backgroundTextureRegion[this.level.getBackground()[y][x] - 1], x * tileSizeWidth + addX + addXScale, addY + y * tileSizeWidth - (tileSizeHeight - tileSizeWidth), tileSizeWidth, tileSizeHeight);
                } else {
                    getMainPanel().spriteBatch.draw(AssetLoader.backgroundTextureRegion[4], x * tileSizeWidth + addX + addXScale, addY + y * tileSizeWidth - (tileSizeHeight - tileSizeWidth), tileSizeWidth, tileSizeHeight);
                }
                if (this.level.getCurNumber()[y][x] > 0) {
                    getMainPanel().spriteBatch.draw(AssetLoader.circlesTextureRegion[this.level.getCurNumber()[y][x] - 1], x * tileSizeWidth + addX + addXScale + tileSizeWidth/2f - AssetLoader.circlesTextureRegion[this.level.getCurNumber()[y][x] - 1].getRegionWidth()*scale/2f, addY + y * tileSizeWidth + tileSizeWidth/2f - AssetLoader.circlesTextureRegion[this.level.getCurNumber()[y][x] - 1].getRegionHeight()*scale/2f, AssetLoader.circlesTextureRegion[this.level.getCurNumber()[y][x] - 1].getRegionWidth()*scale, AssetLoader.circlesTextureRegion[this.level.getCurNumber()[y][x] - 1].getRegionHeight()*scale);
                    if (this.level.getFixedNumbers()[y][x] > 0) {
                        getMainPanel().spriteBatch.draw(AssetLoader.circlesTextureRegion[5], x * tileSizeWidth + addX + addXScale + tileSizeWidth / 2f - AssetLoader.circlesTextureRegion[5].getRegionWidth() * scale / 2f, addY + y * tileSizeWidth + tileSizeWidth / 2f - AssetLoader.circlesTextureRegion[5].getRegionHeight() * scale / 2f, AssetLoader.circlesTextureRegion[5].getRegionWidth() * scale, AssetLoader.circlesTextureRegion[5].getRegionHeight() * scale);
                    }
                    BitmapFont font40 = AssetLoader.font40;
                    if (scale < 1.0f) {
                        font40 = AssetLoader.font30;
                    }
                    //getMainPanel().drawString(String.valueOf(this.level.getCurNumber()[y][x]), x * tileSizeWidth + addX + tileSizeWidth/2f, addY + y * tileSizeWidth + tileSizeWidth/2f, Constants.COLOR_WHITE, AssetLoader.font40, DrawString.MIDDLE, true, false);

                    float[] color = Constants.COLOR_BLACK;
                    if (this.error != null && this.error[y][x] > 0) {
                        color = Constants.COLOR_RED;
                    }
                    getMainPanel().drawString(String.valueOf(this.level.getCurNumber()[y][x]), x * tileSizeWidth + addX + addXScale + tileSizeWidth/2f, addY + y * tileSizeWidth + tileSizeWidth/2f + 4, color, font40, DrawString.MIDDLE, true, false);
                }

                if (this.help && this.level.getCurNumber()[y][x] == 0) {
                    ArrayList<Byte> possibleValues = this.solve.getPossibleValues()[y][x].getPossibleValues();
                    for (int value : possibleValues) {
                        int addValueX = (value - 1) * tileSizeWidth/4 + 10;
                        if (value > 3) {
                            addValueX = (value - 4) * tileSizeWidth/4 + 10;
                        }
                        int addValueY = -15;
                        if (value > 3) {
                            addValueY += tileSizeWidth/4;
                        }
                        getMainPanel().spriteBatch.draw(AssetLoader.circlesTextureRegion[value-1], x * tileSizeWidth + addX + addXScale + addValueX, addY + addValueY + y * tileSizeWidth + tileSizeWidth/2f - AssetLoader.circlesTextureRegion[value].getRegionHeight()/2f, AssetLoader.circlesTextureRegion[value].getRegionWidth()/2f, AssetLoader.circlesTextureRegion[value].getRegionHeight()/2f);
                    }
                }

//                getMainPanel().spriteBatch.draw(AssetLoader.backgroundTextureRegion[this.level.getBackground()[y][x] - 1], x * tileSizeWidth * smaller + addX + addSmall, addY + y * tileSizeWidth * smaller - (tileSizeHeight - tileSizeWidth) * smaller, tileSizeWidth * smaller, tileSizeHeight * smaller);
//                getMainPanel().drawString(String.valueOf(this.level.getNumbers()[y][x]), x * tileSizeWidth * smaller + addX + addSmall + tileSizeWidth * smaller/2f + 1, addY + y * tileSizeWidth * smaller + tileSizeWidth * smaller/2f + 1, Constants.COLOR_BLACK, AssetLoader.font30, DrawString.MIDDLE, true, false);
//
//                int addSolveY = 350;
//                getMainPanel().spriteBatch.draw(AssetLoader.backgroundTextureRegion[this.level.getBackground()[y][x] - 1], x * tileSizeWidth * smaller + addX + addSmall, addY + addSolveY + y * tileSizeWidth * smaller - (tileSizeHeight - tileSizeWidth) * smaller, tileSizeWidth * smaller, tileSizeHeight * smaller);
//                getMainPanel().drawString(String.valueOf(this.level.getSolvedNumbers()[y][x]), x * tileSizeWidth * smaller + addX + addSmall + tileSizeWidth * smaller/2f + 1, addY + addSolveY + y * tileSizeWidth * smaller + tileSizeWidth * smaller/2f + 1, Constants.COLOR_BLACK, AssetLoader.font30, DrawString.MIDDLE, true, false);
            }
        }

        if (this.gameState == GameState.SOLVED) {
            float wonX = (Constants.GAME_WIDTH - AssetLoader.hudRightTextureRegion.getRegionWidth())/2f;
            float wonY = Constants.GAME_HEIGHT/2f - AssetLoader.wonTextureRegion.getRegionHeight()/2f;
            getMainPanel().spriteBatch.draw(AssetLoader.wonTextureRegion, wonX - AssetLoader.wonTextureRegion.getRegionWidth()/2f, wonY);
            getMainPanel().drawString(Localization.getInstance().getCommon().get("won"), wonX, wonY + 40, Constants.COLOR_BLACK, AssetLoader.font30, DrawString.MIDDLE, false, false);

            getMainPanel().drawString(Localization.getInstance().getCommon().get("won_text"), wonX, wonY + 125, Constants.COLOR_WHITE, AssetLoader.font25, DrawString.MIDDLE, false, false);

            getMainPanel().drawString(Localization.getInstance().getCommon().get("won"), hudStartX + AssetLoader.hudRightTextureRegion.getRegionWidth()/2f, 70, Constants.COLOR_WHITE, AssetLoader.font20, DrawString.MIDDLE, false, false);
        }

        getMainPanel().spriteBatch.end();

        for (ApoButton button : this.getMainPanel().getButtons()) {
            button.render(this.getMainPanel());
        }
    }

//	        Gdx.graphics.getGL20().glEnable(GL20.GL_BLEND);
//			Gdx.graphics.getGL20().glDisable(GL20.GL_BLEND);
//
//			getMainPanel().getRenderer().begin(ShapeType.Line);
//			getMainPanel().getRenderer().setColor(Constants.COLOR_WHITE[0], Constants.COLOR_WHITE[1], Constants.COLOR_WHITE[2], 1f);
//			getMainPanel().getRenderer().roundedRectLine((WIDTH - width)/2f, startY, width, height, 5);
//			getMainPanel().getRenderer().end();


    public void drawOverlay() {
    }

    @Override
    public void dispose() {
    }
}
