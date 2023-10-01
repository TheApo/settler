/*
 * Copyright (c) 2005-2020 Dirk Aporius <dirk.aporius@gmail.com>
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.apogames.settler.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * The type Asset loader.
 */
public class AssetLoader {

	public static final String FONT_CHARACTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|\\/?-+=()*&.;,{}\"´`'<>";

	private static Texture backgroundTexture;
	public static TextureRegion[] backgroundTextureRegion;

	private static Texture backgroundMainTexture;
	public static TextureRegion backgroundMainTextureRegion;

	private static Texture boardMainTexture;
	public static TextureRegion boardMainTextureRegion;

	private static Texture wonTexture;
	public static TextureRegion wonTextureRegion;
	private static Texture titleTexture;
	public static TextureRegion titleTextureRegion;
	private static Texture hudMenuTexture;
	public static TextureRegion hudMenuTextureRegion;

	private static Texture buttonXTexture;
	public static TextureRegion[] buttonXTextureRegion;
	private static Texture buttonHelpTexture;
	public static TextureRegion[] buttonHelpTextureRegion;
	private static Texture buttonFixTexture;
	public static TextureRegion[] buttonFixTextureRegion;
	private static Texture buttonRestartTexture;
	public static TextureRegion[] buttonRestartTextureRegion;
	private static Texture buttonRightTexture;
	public static TextureRegion[] buttonRightTextureRegion;
	private static Texture buttonLeftTexture;
	public static TextureRegion[] buttonLeftTextureRegion;
	private static Texture buttonBlancoTexture;
	public static TextureRegion[] buttonBlancoTextureRegion;
	private static Texture hudRightTexture;
	public static TextureRegion hudRightTextureRegion;
	private static Texture circlesTexture;
	public static TextureRegion[] circlesTextureRegion;
	public static BitmapFont font40;
	public static BitmapFont font20;
	public static BitmapFont font15;
	public static BitmapFont font25;
	public static BitmapFont font30;



	public static void load() {
		backgroundTexture = new Texture(Gdx.files.internal("images/background.png"));
		backgroundTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		backgroundTextureRegion = new TextureRegion[5];
		for (int i = 0; i < backgroundTextureRegion.length; i++) {
			backgroundTextureRegion[i] = new TextureRegion(backgroundTexture, 128 * i, 0, 128, 192);
			backgroundTextureRegion[i].flip(false, true);
		}

		backgroundMainTexture = new Texture(Gdx.files.internal("images/background_main.png"));
		backgroundMainTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		backgroundMainTextureRegion = new TextureRegion(backgroundMainTexture, 0, 0, 1422, 800);
		backgroundMainTextureRegion.flip(false, true);

		boardMainTexture = new Texture(Gdx.files.internal("images/board_main.png"));
		boardMainTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		boardMainTextureRegion = new TextureRegion(boardMainTexture, 0, 0, 1200, 715);
		boardMainTextureRegion.flip(false, true);

		wonTexture = new Texture(Gdx.files.internal("images/won.png"));
		wonTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		wonTextureRegion = new TextureRegion(wonTexture, 0, 0, 500, 300);
		wonTextureRegion.flip(false, true);

		titleTexture = new Texture(Gdx.files.internal("images/title.png"));
		titleTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		titleTextureRegion = new TextureRegion(titleTexture, 0, 0, 400, 71);
		titleTextureRegion.flip(false, true);

		hudRightTexture = new Texture(Gdx.files.internal("images/hud_right.png"));
		hudRightTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		hudRightTextureRegion = new TextureRegion(hudRightTexture, 0, 0, 230, 700);
		hudRightTextureRegion.flip(false, true);

		hudMenuTexture = new Texture(Gdx.files.internal("images/hud_menu.png"));
		hudMenuTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		hudMenuTextureRegion = new TextureRegion(hudMenuTexture, 0, 0, 800, 700);
		hudMenuTextureRegion.flip(false, true);

		buttonXTexture = new Texture(Gdx.files.internal("images/buttons_x.png"));
		buttonXTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		buttonXTextureRegion = new TextureRegion[3];
		for (int i = 0; i < buttonXTextureRegion.length; i++) {
			buttonXTextureRegion[i] = new TextureRegion(buttonXTexture, 70 * i, 0, 70, 70);
			buttonXTextureRegion[i].flip(false, true);
		}

		buttonHelpTexture = new Texture(Gdx.files.internal("images/buttons_help.png"));
		buttonHelpTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		buttonHelpTextureRegion = new TextureRegion[3];
		for (int i = 0; i < buttonHelpTextureRegion.length; i++) {
			buttonHelpTextureRegion[i] = new TextureRegion(buttonHelpTexture, 70 * i, 0, 70, 70);
			buttonHelpTextureRegion[i].flip(false, true);
		}

		buttonFixTexture = new Texture(Gdx.files.internal("images/buttons_fix.png"));
		buttonFixTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		buttonFixTextureRegion = new TextureRegion[3];
		for (int i = 0; i < buttonFixTextureRegion.length; i++) {
			buttonFixTextureRegion[i] = new TextureRegion(buttonFixTexture, 70 * i, 0, 70, 70);
			buttonFixTextureRegion[i].flip(false, true);
		}

		buttonRestartTexture = new Texture(Gdx.files.internal("images/buttons_restart.png"));
		buttonRestartTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		buttonRestartTextureRegion = new TextureRegion[3];
		for (int i = 0; i < buttonRestartTextureRegion.length; i++) {
			buttonRestartTextureRegion[i] = new TextureRegion(buttonRestartTexture, 70 * i, 0, 70, 70);
			buttonRestartTextureRegion[i].flip(false, true);
		}

		buttonRightTexture = new Texture(Gdx.files.internal("images/buttons_right.png"));
		buttonRightTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		buttonRightTextureRegion = new TextureRegion[3];
		for (int i = 0; i < buttonRightTextureRegion.length; i++) {
			buttonRightTextureRegion[i] = new TextureRegion(buttonRightTexture, 70 * i, 0, 70, 70);
			buttonRightTextureRegion[i].flip(false, true);
		}

		buttonLeftTexture = new Texture(Gdx.files.internal("images/buttons_left.png"));
		buttonLeftTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		buttonLeftTextureRegion = new TextureRegion[3];
		for (int i = 0; i < buttonLeftTextureRegion.length; i++) {
			buttonLeftTextureRegion[i] = new TextureRegion(buttonLeftTexture, 70 * i, 0, 70, 70);
			buttonLeftTextureRegion[i].flip(false, true);
		}

		buttonBlancoTexture = new Texture(Gdx.files.internal("images/buttons_blanco.png"));
		buttonBlancoTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		buttonBlancoTextureRegion = new TextureRegion[3];
		for (int i = 0; i < buttonBlancoTextureRegion.length; i++) {
			buttonBlancoTextureRegion[i] = new TextureRegion(buttonBlancoTexture, 300 * i, 0, 300, 70);
			buttonBlancoTextureRegion[i].flip(false, true);
		}

		circlesTexture = new Texture(Gdx.files.internal("images/circles.png"));
		circlesTexture.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

		circlesTextureRegion = new TextureRegion[6];
		for (int i = 0; i < circlesTextureRegion.length; i++) {
			circlesTextureRegion[i] = new TextureRegion(circlesTexture, 64 * i, 0, 64, 64);
			circlesTextureRegion[i].flip(false, true);
		}

		font40 = new BitmapFont(Gdx.files.internal("fonts/pirate40.fnt"), Gdx.files.internal("fonts/pirate40.png"), true);
		font20 = new BitmapFont(Gdx.files.internal("fonts/pirate20.fnt"), Gdx.files.internal("fonts/pirate20.png"), true);
		font15 = new BitmapFont(Gdx.files.internal("fonts/pirate15.fnt"), Gdx.files.internal("fonts/pirate15.png"), true);
		font25 = new BitmapFont(Gdx.files.internal("fonts/pirate25.fnt"), Gdx.files.internal("fonts/pirate25.png"), true);
		font30 = new BitmapFont(Gdx.files.internal("fonts/pirate30.fnt"), Gdx.files.internal("fonts/pirate30.png"), true);
	}

	public static void dispose() {
		backgroundTexture.dispose();
		circlesTexture.dispose();
		backgroundMainTexture.dispose();
		boardMainTexture.dispose();
		hudRightTexture.dispose();
		buttonXTexture.dispose();
		buttonFixTexture.dispose();
		buttonHelpTexture.dispose();
		buttonRestartTexture.dispose();
		buttonRightTexture.dispose();
		buttonLeftTexture.dispose();
		buttonBlancoTexture.dispose();
		wonTexture.dispose();
		titleTexture.dispose();
		font40.dispose();
		font30.dispose();
		font25.dispose();
		font20.dispose();
		font15.dispose();
//        click.dispose();
	}

}

