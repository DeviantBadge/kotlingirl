package ru.atom.gameservice.tick;

import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.slf4j.LoggerFactory;

public class Field {
    private static final org.slf4j.Logger log = LoggerFactory.getLogger(Field.class);
    /*

    |-----------------> X
    |       width
    |
    | he
    | ig
    | ht
    |
    V Y

     */
    private int idGenerator;
    public final int height; // Y
    public final int width;  // X
    public static final int tile = 32;   // количество пикселей

    private Set<GameObject> replicaObjects;
    public Set<Bomb> bombs;

    private GameObject[][] gameObjects;

    public Field(int height, int width, List<String> players) {
        this.height = height;
        this.width = width;
        replicaObjects = new HashSet<>();
        bombs = new HashSet<>();
        gameObjects = new GameObject[height][];
        for (int i = 0; i < height; i++) {
            gameObjects[i] = new GameObject[width];
        }

        //Добавлю Стены WALL
        // walls();
        //Добавлю Игроков
        //boxes();
        wallsAndBoxes();
        int[] X = {1, 25, 1, 25};
        int[] Y = {1, 1, 15, 15};

        for (int i = 0; i < 2; i++) {
            Player player = new Player(X[i], Y[i], this, players.get(i));
            gameObjects[X[i]][Y[i]] = player;
            replicaObjects.add(player);
        }
    }

    private void boxes() {
        for (int i = 1; i < height; i += 2)
            for (int j = 1; j < width; j += 2) {
                Box box = new Box(i, j, this);
                gameObjects[i][j] = box;
                replicaObjects.add(box);
            }

    }

    private void walls() {
        for (int i = 1; i < height; i += 2)
            for (int j = 1; j < width; j += 2) {
                Wall wall = new Wall(i, j, this);
                gameObjects[i][j] = wall;
                replicaObjects.add(wall);
            }

    }

    private void wallsAndBoxes() {
        for (int i = 3; i < height-3; i += 2) {
            for (int j = 3; j < width-3; j += 2) {
                Box box = new Box(i, j, this);
                gameObjects[i][j] = box;
                replicaObjects.add(box);
            }
        }

        for(int i =0 ;i<height;i++ ){
            Wall wall = new Wall(i, 0, this);
            gameObjects[i][0] = wall;
            replicaObjects.add(wall);
        }
        for(int i =0 ;i<height;i++ ){
            Wall wall = new Wall(i, width-1, this);
            gameObjects[i][width-1] = wall;
            replicaObjects.add(wall);
        }
        for(int i =0 ;i<width;i++ ){
            Wall wall = new Wall(height-1, i, this);
            gameObjects[height-1][i] = wall;
            replicaObjects.add(wall);
        }
        for(int i =0 ;i<width;i++ ){
            Wall wall = new Wall(0, i, this);
            gameObjects[0][i] = wall;
            replicaObjects.add(wall);
        }


    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public GameObject getAt(int x, int y) {
        return (x < 0 || y < 0 || x >= width || y >= height) ? null : gameObjects[x][y];
    }

    public void plantBomb(Player player) {
        if (player.tryToPlantBomb()) {
//            replicaObjects.add(gameObjects[player.x][player.y]);
//            gameObjects[player.x][player.y] = new Bomb(this, player);
            Bomb b = new Bomb(this, player);
            bombs.add(b);

        }
    }

    public void gameLogic(long elapsed) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                GameObject gameObject = gameObjects[i][j];
                if (gameObject instanceof Tickable) {
                    ((Tickable) gameObject).tick(elapsed);
                }
            }
        }
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                GameObject gameObject = gameObjects[i][j];
                if (gameObject instanceof Player || gameObject instanceof Bomb || gameObject instanceof Fire) {
                    replicaObjects.add(gameObject);
                }
                if (gameObject != null && !gameObject.isAlive()) {
                    replicaObjects.add(gameObject);
                    gameObjects[i][j] = (gameObject instanceof Box) ? PowerUp.generateNewPowerUp(i, j, this) : null;
                    if (gameObjects[i][j] != null) replicaObjects.add(gameObjects[i][j]);
                }
            }
        }
        if (!bombs.isEmpty()) {
            for (Bomb b : bombs) {
                b.tick(elapsed);
            }
            replicaObjects.addAll(bombs);
        }
        //walls();

    }

    public Player getPlayerByName(String name) {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                GameObject gameObject = gameObjects[i][j];
                if (gameObject instanceof Player) {
                    if (((Player) gameObject).getName().equals(name)) return (Player) gameObject;
                }
            }
        }
        return null;
    }


    public void updatePlayerPosition(Player player, int x, int y) {

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                GameObject gameObject = gameObjects[i][j];
                if (gameObject instanceof Player) {
                    if (((Player) gameObject).getName().equals(player.getName())) {
                        gameObjects[i][j] = null;
                        gameObjects[x][y] = gameObject;
                        return;
                    }
                }
            }
        }

    }

    public static boolean checkCollision(int centerX, int centerY, GameObject collideObj) {
        if (collideObj == null) return false;
        int t = 5;
        int down1 = collideObj.y * tile;
        int up1 = down1 + tile;
        int left1 = collideObj.x * tile;
        int right1 = left1 + tile;
        int up2 = centerY + Field.tile / 2 - t;
        int down2 = centerY - Field.tile / 2 + t;
        int left2 = centerX - Field.tile / 2 + t;
        int right2 = centerX + Field.tile / 2 - t;
        return (down1 <= up2)
                && (up1 >= down2)
                && (left1 <= right2)
                && (right1 >= left2);
    }

    public int getNextId() {
        return idGenerator++;
    }

    public String getReplica() {
        StringBuilder stringBuilder = new StringBuilder("{   \"topic\": \"REPLICA\",  \"data\":[");
        String collect = replicaObjects.stream().map(GameObject::toJson).collect(Collectors.joining(","));
        replicaObjects.clear();
        stringBuilder.append(collect);
        stringBuilder.append("]}");
        return stringBuilder.toString();
    }

    public void createFire(int x, int y) {
        Fire f = new Fire(x, y, this);
        gameObjects[x][y] = f;
    }
}
