package me.lihq.game;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import me.lihq.game.living.NPC;
import me.lihq.game.models.Clue;
import me.lihq.game.models.Map;
import me.lihq.game.living.Player;
import me.lihq.game.models.Room;
import me.lihq.game.models.Vector2Int;
import me.lihq.game.screen.AbstractScreen;
import me.lihq.game.screen.NavigationScreen;
import me.lihq.game.screen.MainMenuScreen;

import java.util.*;

import static com.badlogic.gdx.maps.tiled.TiledMapTileLayer.*;


/**
 * This is the class responsible for the game as a whole. It manages the current states and entry points of the game
 */
public class GameMain extends Game
{
    //This is a static reference to itself. Comes in REALLY handy when in other classes that don't have a reference to the main game
    public static GameMain me = null;
    //Game wide variables

    /**
     * A list holding NPC objects
     */
    public List<NPC> NPCs = new ArrayList<NPC>();

    /**
     * The game map
     */
    public Map gameMap;
    /**
     * A player object for the player of the game
     */
    public Player player;
    public int ticks = 0;
    public int lastSecond = -1;
    

    /**
     * The main menu screen that shows up when the game is first started
     */
    private MainMenuScreen menuScreen;
    
    /**
     * A screen to be used to display standard gameplay within the game , including the status bar.
     */
    public NavigationScreen navigationScreen;

    /**
     * An FPSLogger, FPSLogger allows us to check the game FPS is good enough
     */
    FPSLogger FPS;

    /**
     * This is called at start up. It initialises the game.
     */
    @Override
    public void create()
    {
        this.me = this;

        Assets.load();// Load in the assets the game needs

        gameMap = new Map(); //instantiate game map

        initialiseAllPeople();

        initialiseClues();

        //set up the screen and display the first room

        //Set up the Menu
        menuScreen = new MainMenuScreen(this);
        this.setScreen(menuScreen);

        navigationScreen = new NavigationScreen(this);
        navigationScreen.updateTiledMapRenderer();

        //Instantiate the FPSLogger to show FPS
        FPS = new FPSLogger();

        gameLoop();
    }

    /**
     * This defines what's rendered on the screen for each frame.
     */
    @Override
    public void render()
    {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        FPS.log();//this is where fps is displayed

        super.render(); // This calls the render method of the screen that is currently set

    }

    @Override
    public void dispose()
    {

    }


    /**
     * Overrides the getScreen method to return our AbstractScreen type.
     * This means that we can access the additional methods like update.
     *
     * @return The current screen of the game.
     */
    @Override
    public AbstractScreen getScreen()
    {
        return (AbstractScreen) super.getScreen();
    }

    public void gameLoop()
    {
        Timer gameTimer = new Timer();
        TimerTask task = new TimerTask()
        {
            @Override
            public void run()
            {
                ticks++;

                Calendar cal = Calendar.getInstance();

                if (cal.get(Calendar.SECOND) != lastSecond) {
                    lastSecond = cal.get(Calendar.SECOND);
                    System.out.println("TPSLogger: tps:      " + ticks);
                    ticks = 0;
                }

                me.getScreen().update();
            }
        };

        gameTimer.schedule(task, 0, 1000 / Settings.TPS);
    }

    /**
     * This method returns the Navigation Screen that the game runs on.
     *
     * @return navigationScreen - The gameplay screen.
     */
    public NavigationScreen getNavigationScreen()
    {
        return navigationScreen;
    }

    /**
     * Generates all the NPC's, Players
     */
    public void initialiseAllPeople()
    {
        //Add ALL NPCs to the list
        //This is how you initialise an NPC
        player = new Player("Test name", "player.png", 3, 6);
        player.setRoom(gameMap.getRoom(0));

        //TODO: Add NPC assets
        NPC npc = new NPC("Colin", "colin.png", 15, 17, gameMap.getRoom(0), true);
        NPCs.add(npc);

        NPC npc2 = new NPC("Diana", "diana.png",4,4, gameMap.getRoom(1), true);
        NPCs.add(npc2);

        NPC npc3 = new NPC("Lily", "lily.png", 0, 0, gameMap.getRoom(0), true);
        NPCs.add(npc3);

        NPC npc4 = new NPC("Mary", "mary.png", 0, 0, gameMap.getRoom(0), true);
        NPCs.add(npc4);

        NPC npc5 = new NPC("Mike", "mike.png", 0, 0, gameMap.getRoom(0), true);
        NPCs.add(npc5);

        NPC npc6 = new NPC("Will", "will.png", 0, 0, gameMap.getRoom(0), true);
        NPCs.add(npc6);

        int amountOfRooms = gameMap.getAmountOfRooms();

        List<Integer> roomsLeft = new ArrayList<Integer>();

        for (int i = 0; i < amountOfRooms; i ++)
        {
            roomsLeft.add(i);
        }

        for (NPC loopNpc : NPCs)
        {
            /*
            Refill the rooms left list if there are more NPCs than Rooms. This will put AT LEAST one NPC per room if so.
             */
            if (roomsLeft.isEmpty())
            {
                for (int i = 0; i < amountOfRooms; i ++)
                {
                    roomsLeft.add(i);
                }
            }

            /*
            Pick a random room and put that NPC in it
             */
            int toTake = new Random().nextInt(roomsLeft.size() - 1);
            int selectedRoom = roomsLeft.get(toTake);
            roomsLeft.remove(toTake);

            loopNpc.setRoom(gameMap.getRoom(selectedRoom));
            Vector2Int position = loopNpc.getRoom().getRandomLocation();
            loopNpc.setTileCoordinates(position.x, position.y);

            System.out.println(loopNpc.getName() + " has been placed in room " + selectedRoom + " at " + position);
        }

        /*
        Generate who the Killer and Victim are
         */
        NPC killer = NPCs.get(new Random().nextInt(NPCs.size() - 1));

        while(!killer.setKiller())
        {
            killer = NPCs.get(new Random().nextInt(NPCs.size() - 1));
        }

        NPC victim = NPCs.get(new Random().nextInt(NPCs.size() - 1));;

        while (!victim.setVictim())
        {
            victim = NPCs.get(new Random().nextInt(NPCs.size() - 1));
    }

    }

    public List<? extends Sprite> getNPCS(Room room)
    {
        List<NPC> npcsInRoom = new ArrayList<>();
        for (NPC n : this.NPCs) {
            if (n.getRoom() == room) {
                npcsInRoom.add(n);
            }
        }

        return npcsInRoom;
    }

    private void initialiseClues()
    {
        //This is a temporary list of clues
        List<Clue> tempClues = new ArrayList<Clue>();

        tempClues.add(new Clue("Clue 1", "test Desc", new TextureRegion(Assets.CLUE_SHEET, 0, 0, 32, 32)));
        tempClues.add(new Clue("Clue 2", "test Desc", new TextureRegion(Assets.CLUE_SHEET, 0, 0, 32, 32)));
        tempClues.add(new Clue("Clue 3", "test Desc", new TextureRegion(Assets.CLUE_SHEET, 0, 0, 32, 32)));
        tempClues.add(new Clue("Clue 4", "test Desc", new TextureRegion(Assets.CLUE_SHEET, 0, 0, 32, 32)));
        tempClues.add(new Clue("Clue 5", "test Desc", new TextureRegion(Assets.CLUE_SHEET, 0, 0, 32, 32)));
        tempClues.add(new Clue("Clue 6", "test Desc", new TextureRegion(Assets.CLUE_SHEET, 0, 0, 32, 32)));
        tempClues.add(new Clue("Clue 7", "test Desc", new TextureRegion(Assets.CLUE_SHEET, 0, 0, 32, 32)));
        tempClues.add(new Clue("Clue 8", "test Desc", new TextureRegion(Assets.CLUE_SHEET, 0, 0, 32, 32)));
        tempClues.add(new Clue("Clue 9", "test Desc", new TextureRegion(Assets.CLUE_SHEET, 0, 0, 32, 32)));
        tempClues.add(new Clue("Clue 10", "test Desc", new TextureRegion(Assets.CLUE_SHEET, 0, 0, 32, 32)));

        Collections.shuffle(tempClues);

        for (Room room : gameMap.getRooms())
        {
            if (tempClues.isEmpty()) return;


            Vector2Int randHidingSpot = room.getRandHidingSpot();

            if (randHidingSpot != null) {
                room.addClue(tempClues.get(0).setTileCoordinates(randHidingSpot));
                tempClues.remove(0);
            }

        }

    }
}
