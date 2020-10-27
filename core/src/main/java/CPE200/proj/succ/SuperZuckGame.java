package CPE200.proj.succ;

import CPE200.proj.succ.model.GameObject;
import CPE200.proj.succ.model.GameObjectType;
import CPE200.proj.succ.model.GameState;
import CPE200.proj.succ.model.TurnState;
import CPE200.proj.succ.model.board.GameBoard;
import CPE200.proj.succ.model.movable.Bomb;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import org.mini2Dx.core.game.BasicGame;
import org.mini2Dx.core.graphics.Graphics;
import org.mini2Dx.core.graphics.Sprite;


public class SuperZuckGame extends BasicGame {
	public static final String GAME_IDENTIFIER = "CPE200.proj.succ";
	private static final float volume = 0.12f;
    private int gridSize;
    private boolean stageChange = false;
    private int boardOffsetX, boardOffsetY;
	private Sound music;
	private Sound sfx ;
	private boolean soundOn = true;
	private Sprite sound_sprite_button;
	private Sprite play_sprite_button;
	private Sprite restartStage;
	private Sprite mainMenu_btn;
	private long checkdelay = 0;

    private GameBoard gameBoard; // กระดานของตัวเกม

    private GameState currentState = GameState.MainMenu; // stage ปัจจุบันของตัวเกม
    private TurnState currentPhase = TurnState.Player_Move;// turn phase ปัจจุบัน



    @Override
    public void initialise() {
        gridSize = 50;
        boardOffsetX = 50;
        boardOffsetY = 50;
        music = Gdx.audio.newSound(Gdx.files.internal("sound/song.mp3"));
        music.play(volume);
        checkdelay = System.nanoTime()/1000000000;
        //------------------------------Sprite-----------------------------------------------------
        play_sprite_button = new Sprite(new Texture("btn/start.png"));
        play_sprite_button.setPosition(width/2-125,height/2-(float)37.5+100);
        //------------------------------Sprite_sound------------------------------------------------
        sound_sprite_button = new Sprite(new Texture("btn/sound_up.png"));
        sound_sprite_button.setPosition(width-180,height-115);
        //------------------------------restartstate------------------------------------------------
        restartStage = new Sprite(new Texture("btn/restart.png"));
        restartStage.setPosition(width-180,height-250);
        //---------------------------------mainMenu-------------------------------------------------
        mainMenu_btn = new Sprite(new Texture("btn/main menu.png"));
        mainMenu_btn.setPosition(width-210,height-300);

        gameBoard = new GameBoard(11,18);
        gameBoard = gameBoard.newBoard(GameState.Stage1);
    }
    
    private boolean isSpriteTouch(Sprite button){
        if (Gdx.input.justTouched()) {
            if (Gdx.input.getX() > button.getX() && Gdx.input.getX() < button.getX() + button.getWidth()) {
                return Gdx.input.getY() > button.getY() && Gdx.input.getY() < button.getY() + button.getHeight(); 
            }
        }
        return false;
    }
    
    @Override
    public void update(float delta) {
	    if(stageChange)playMusic();

        if (isSpriteTouch(sound_sprite_button)) {
            if (soundOn){
                soundOn = false;
                music.pause();
                sound_sprite_button.setTexture(new Texture("btn/mute.png"));
            }
            else {
                soundOn = true;
                music.resume();
                sound_sprite_button.setTexture(new Texture("btn/sound_up.png"));
            }
        }

        switch (currentState) {
            case MainMenu:
                        if (isSpriteTouch(play_sprite_button)) {
                            setCurrentState(GameState.Stage1);
                            restartStage();
                            playMusic();
                        }break;
            case Stage1: case Stage2:case Stage3:case Stage4:case Stage5:
                switch (currentPhase) {
                    case Player_Move:
                        if (Gdx.input.isKeyJustPressed(Input.Keys.RIGHT)) {
                            moveRight();
                        }
                        if (Gdx.input.isKeyJustPressed(Input.Keys.LEFT))  {
                            moveLeft();
                        }
                        if (Gdx.input.isKeyJustPressed(Input.Keys.UP)) {
                            moveUp();
                        }
                        if (Gdx.input.isKeyJustPressed(Input.Keys.DOWN)) {
                            moveDown();
                        }
                        checkdelay = System.nanoTime()/1000000000;
                        break;
                    case Police_Check:
                        gameBoard.checkPolice(this , soundOn);
                    case Object_Cool_down:
                        gameBoard.checkBomb(this,soundOn);
                        nextPhase();
                }
                if (isSpriteTouch(restartStage)) {
                    playMusic();
                    restartStage();
                }
                if (isSpriteTouch(mainMenu_btn)){
                    setCurrentState(GameState.MainMenu);
                    playMusic();
                }
                break;
            case GameOver:
            case GameClear:
                if (Gdx.input.justTouched() && System.nanoTime()/1000000000 - checkdelay > 5){
                    setCurrentState(GameState.MainMenu);
                    playMusic();
                    if(soundOn) music.play(volume);
                }break;
        }
    }
    
    @Override
    public void interpolate(float alpha) {
    }
    
    @Override
    public void render(Graphics g) {
        switch (currentState) {
            case MainMenu:{
                renderMainmenu(g);
                renderBtn(g,sound_sprite_button);
                break;
            }
            case Stage1: case Stage2: case Stage3: case Stage4: case Stage5:{
                g.setBackgroundColor(Color.GRAY);
                renderBoard(g);
                renderBtn(g,sound_sprite_button);
                renderBtn(g,restartStage);
                renderBtn(g,mainMenu_btn);
                break;
            }
            case GameOver: {
                renderEndscreen(g);
                break;
            }
            case GameClear:{
                renderClear(g);
                break;
            }
        }

    }

    private void renderBoard(Graphics g){
        g.setLineHeight(5);
        g.setColor(Color.BLACK);
        for (int x = 0; x < gameBoard.getRow(); x++) {
            for (int y = 0; y < gameBoard.getColumn(); y++) {
                int renderX = boardOffsetX + (y * gridSize);
                int renderY = boardOffsetY + (x * gridSize);

                GameObject current = gameBoard.board(x,y);
                if(current.getType() != GameObjectType.NULL) g.drawTexture(current.getTexture(),renderX,renderY);
            }
        }
    }
    private void renderMainmenu (Graphics g) {
        g.drawTexture(new Texture("bg/bg.jpg"), 0, 0);
        g.drawTexture(new Texture("bg/Full_Flour_Alchemist.png"), (width / 3), height / 7 - 100);
        g.drawSprite(play_sprite_button);
    }
    private void renderBtn(Graphics g , Sprite btn){
        g.drawSprite(btn);
    }
    private void renderEndscreen(Graphics g){
	    g.setBackgroundColor(Color.GRAY);
	    renderBoard(g);
	    music.pause();
        if (System.nanoTime()/1000000000 - checkdelay > 5){
            g.drawTexture(new Texture("bg/end bg.jpg"),0,0);
        }
    }
    private void renderClear (Graphics g){
	    g.drawTexture(new Texture("bg/complete_bg.jpg"),0,0);
	    g.drawTexture(new Texture("bg/complete.png"),width/2-300,height/6);
    }
    public void playMusic(){
        music.dispose();
	   switch (currentState){
           case MainMenu:
               music = Gdx.audio.newSound(Gdx.files.internal("sound/song.mp3"));break;
           case Stage1:
               music = Gdx.audio.newSound(Gdx.files.internal("sound/1.mp3"));break;
           case Stage2:
               music = Gdx.audio.newSound(Gdx.files.internal("sound/2.mp3"));break;
           case Stage3:
               music = Gdx.audio.newSound(Gdx.files.internal("sound/3.mp3"));break;
           case Stage4:
               music = Gdx.audio.newSound(Gdx.files.internal("sound/4.mp3"));break;
           case Stage5:
               music = Gdx.audio.newSound(Gdx.files.internal("sound/5.mp3"));break;
           case GameClear:
               music = Gdx.audio.newSound(Gdx.files.internal("sound/game clear.mp3"));break;
       }
        music.play(soundOn ? volume:0);
	   stageChange = false;
    }

    private void move(GameObject current , GameObject next , boolean canNext){
        if(current.getType() == GameObjectType.NULL){
            GameObject toSpace = gameBoard.getThumnaz();
            gameBoard.toNull(toSpace);
            gameBoard.toThumnaZ(current);
            gameBoard.setThumnaz(current.row(),current.column());
            nextPhase();
        }else if(canNext){
            manageNextObject(current,next);
            nextPhase();
        }
    }

    public void moveRight(){
        GameObject rightTile = gameBoard.rightObject(gameBoard.getThumnaz());
        move(rightTile,gameBoard.rightObject(rightTile),gameBoard.canRight(rightTile));
    }
    public void moveLeft(){
        GameObject leftTile = gameBoard.leftObject(gameBoard.getThumnaz());
        move(leftTile,gameBoard.leftObject(leftTile),gameBoard.canLeft(leftTile));
    }
    public void moveUp(){
        GameObject upTile = gameBoard.upperObject(gameBoard.getThumnaz());
        move(upTile,gameBoard.upperObject(upTile), gameBoard.canUp(upTile));
    }
    public void moveDown(){
        GameObject downTile = gameBoard.lowerObject(gameBoard.getThumnaz());
        move(downTile, gameBoard.lowerObject(downTile), gameBoard.canDown(downTile));
    }

    public void toGameOver(GameObject obj){
        switch (obj.getType()){
            case Police:
                sfx = Gdx.audio.newSound(Gdx.files.internal("sound/wasted.mp3"));break;
            case Bomb:
                sfx = Gdx.audio.newSound(Gdx.files.internal("sound/btoom.mp3"));break;
            case Coke:
                sfx = Gdx.audio.newSound(Gdx.files.internal("sound/coke fools.mp3"));break;
        }
        if (soundOn) sfx.play();
        setCurrentState(GameState.GameOver);
    }

    public void setCurrentState(GameState currentState) { this.currentState = currentState;}
    public void nextPhase(){
        switch (currentPhase){
            case Player_Move:
                this.currentPhase = TurnState.Police_Check;break;
            case Police_Check:
                this.currentPhase = TurnState.Object_Cool_down;break;
            case Object_Cool_down:
                this.currentPhase = TurnState.Player_Move;break;
        }
    }
    private void moveThumnaz(GameObject obj){
        gameBoard.toNull(gameBoard.getThumnaz());
        gameBoard.toThumnaZ(obj);
        gameBoard.setThumnaz(obj.row(),obj.column());
    }
    public void restartStage(){
        gameBoard = gameBoard.newBoard(currentState);
    }

    public void nextStage(){
        switch (currentState) {
            case Stage1:
                this.currentState = GameState.Stage2;
                break;
            case Stage2:
                this.currentState = GameState.Stage3;
                break;
            case Stage3:
                this.currentState = GameState.Stage4;
                break;
            case Stage4:
                this.currentState = GameState.Stage5;
                break;
            case Stage5:
                this.currentState = GameState.GameClear;
                break;
        }
        stageChange = true;
    }
    public void manageNextObject(GameObject obj , GameObject nextObj){
        switch (obj.getType()){
            case Bribe:
                if(nextObj.getType() == GameObjectType.NULL) {
                    gameBoard.toBribe(nextObj);
                    moveThumnaz(obj);
                }
                break;
            case Bomb:
                if(nextObj.getType() == GameObjectType.NULL) {
                    gameBoard.toBomb(nextObj,(Bomb)obj);
                    moveThumnaz(obj);
                }
                break;
            case Coke:
                moveThumnaz(obj);
                toGameOver(obj);
                //setCurrentState(GameState.GameOver);break;
            case Flour:
                gameBoard.getFlours().remove(obj);
                moveThumnaz(obj);
                break;
            case Key:
                obj.play(soundOn);
                gameBoard.getInventory().add(obj);
                moveThumnaz(obj);
                break;
            case StageDoor:
                if(gameBoard.checkFlours()){
                    obj.play(soundOn);
                    nextStage();
                    restartStage();
                }
                break;
            case Door:
                if(gameBoard.haveKey()){
                    obj.play(soundOn);
                    moveThumnaz(obj);
                }
                break;
            case Converter:
                gameBoard.convertFlours();
                moveThumnaz(obj);
                break;
        }
    }

}
