package com.apogames.settler.client;

import com.apogames.settler.Constants;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.apogames.settler.Settler;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                Constants.IS_HTML = true;
                // Resizable application, uses available space in browser
                return new GwtApplicationConfiguration(true);
                // Fixed size application:
                //return new GwtApplicationConfiguration(Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new Settler();
        }
}