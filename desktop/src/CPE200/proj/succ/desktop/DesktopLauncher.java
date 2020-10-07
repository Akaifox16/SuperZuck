package CPE200.proj.succ.desktop;

import org.mini2Dx.desktop.DesktopMini2DxConfig;

import com.badlogic.gdx.backends.lwjgl.DesktopMini2DxGame;

import CPE200.proj.succ.SuperZuckGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		DesktopMini2DxConfig config = new DesktopMini2DxConfig(SuperZuckGame.GAME_IDENTIFIER);
		config.height = 768;
		config.width = 1366;
		//config.resizable = false;
//		config.fullscreen = true;
		new DesktopMini2DxGame(new SuperZuckGame(), config);
	}
}