package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.Utilities.InputType;
import com.teamtwo.aerolites.Utilities.LevelConfig;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;

import java.util.ArrayList;

public class StarMap extends State {

    private class Node {
        private int phase;
        private LevelConfig.Difficulty difficulty;
        private CircleShape display;

        private Node(LevelConfig.Difficulty difficulty, CircleShape shape, int phase) {
            this.difficulty = difficulty;
            display = shape;
            this.phase = phase;
        }
    }

    private Node current;
    private int lastPhase = -1;
    private boolean prevPress;

    private ArrayList<Node> prevNodes;

    private boolean generated;
    private ArrayList<Node> stars;
    private float starSize;
    private boolean r;

    private Font font;

    private LevelConfig config;

    public StarMap(GameStateManager gsm) {
        super(gsm);

        font = ContentManager.instance.loadFont("Ubuntu", "Ubuntu.ttf");

        stars = new ArrayList<>();
        prevNodes = new ArrayList<>();
        generated = false;
        generate();
    }

    private void generate() {
        //if(generated) return;
      //  generated = true;

        int hardCount = 2;

        starSize = 16;

        stars.clear();

        // Start shape
        CircleShape shape = new CircleShape(starSize, 4);
        shape.setPosition(40, State.WORLD_SIZE.y / 2f);

        LevelConfig.Difficulty difficulty = LevelConfig.Difficulty.Easy;

        stars.add(new Node(difficulty, shape, 0));

        // Phase One
        int starCount;
        float y;
        float x = (State.WORLD_SIZE.x / 4f);
        for(int i = 0; i < 3; i++) {
            starCount = MathUtil.randomInt(1, 8);
            starCount = ((starCount + 1) % 3) + 1;
            y = State.WORLD_SIZE.y / (float) (starCount + 1);
            for(int j = 0; j < starCount; j++) {
                shape = new CircleShape(starSize, 4);
                shape.setPosition(x * (i + 1), ((starCount - j) * y) + MathUtil.randomFloat(-y / 4f, y / 4f));
                difficulty = LevelConfig.Difficulty.values()[MathUtil.randomInt(0, 3)];
                if(hardCount == 0 && difficulty == LevelConfig.Difficulty.Hard) {
                    difficulty = LevelConfig.Difficulty.values()[MathUtil.randomInt(0, 2)];
                }
                else if(difficulty == LevelConfig.Difficulty.Hard) {
                    hardCount--;
                }

                stars.add(new Node(difficulty, shape, i + 1));
            }
        }

        shape = new CircleShape(starSize, 4);
        shape.setPosition(1880 - (2 * starSize), State.WORLD_SIZE.y / 2f);
        difficulty = LevelConfig.Difficulty.Hard;

        stars.add(new Node(difficulty, shape, 5));

        current = stars.get(0);
        prevNodes.clear();

        System.out.println("Star Count: " + stars.size());
    }

    public void update(float dt) {
        if(Keyboard.isKeyPressed(Keyboard.Key.R) && !r) {
            generate();
        }

        mouse = Mouse.getPosition(window);
        boolean pressed = Mouse.isButtonPressed(Mouse.Button.LEFT);
        Vector2f mousePos = window.mapPixelToCoords(mouse);


        for(Node node : stars) {
            Vector2f pos = node.display.getPosition();
            if(mousePos.x > pos.x && mousePos.x < pos.x + (starSize * 2)) {
                if(mousePos.y > pos.y && mousePos.y < pos.y + (starSize * 2)) {
                    if(pressed && !prevPress && node != current) {
                        if(node.phase == current.phase + 1) {
                            prevNodes.add(current);
                            current = node;
                        }
                    }
                    else if(pressed && !prevPress && node == current) {
                        prevPress = true;
                        reset();
                        gsm.addState(new PlayState(gsm, config));
                    }
                }
            }
        }

        r = Keyboard.isKeyPressed(Keyboard.Key.R);
        prevPress = Mouse.isButtonPressed(Mouse.Button.LEFT);
    }

    public void render() {
        for(int i = 0; i < prevNodes.size(); i++) {
            Vertex[] line = new Vertex[2];
            line[0] = new Vertex(Vector2f.add(prevNodes.get(i).display.getPosition(),
                    new Vector2f(starSize, starSize)), Color.GREEN);

            if(i + 1 < prevNodes.size()) {
                line[1] = new Vertex(Vector2f.add(prevNodes.get(i + 1).display.getPosition(),
                        new Vector2f(starSize, starSize)), Color.GREEN);
            }
            else {
                line[1] = new Vertex(Vector2f.add(current.display.getPosition(),
                        new Vector2f(starSize, starSize)), Color.GREEN);
            }

            window.draw(line, PrimitiveType.LINES);
        }

        for(Node node : stars) {
            if(node == current) {
                int index = stars.indexOf(node) + 1;

                if (index < stars.size()) {
                    Node next = stars.get(index);
                    while (next.phase <= node.phase + 1) {

                        if (next.phase == node.phase) {
                            index++;
                            next = stars.get(index);
                            continue;
                        }

                        Vertex[] line = new Vertex[]{
                                new Vertex(Vector2f.add(node.display.getPosition(), new Vector2f(starSize, starSize))),
                                new Vertex(Vector2f.add(next.display.getPosition(), new Vector2f(starSize, starSize)))
                        };

                        window.draw(line, PrimitiveType.LINES);


                        index++;
                        if (index == stars.size()) break;
                        next = stars.get(index);
                    }
                }
            }

            switch (node.difficulty) {
                case Easy:
                    node.display.setFillColor(new Color(93, 249, 72));
                    break;
                case Medium:
                    node.display.setFillColor(new Color(244, 131, 66));
                    break;
                case Hard:
                    node.display.setFillColor(new Color(244, 75, 66));
                    break;
            }

            window.draw(node.display);
        }

        Text t = new Text("Mouse: [" + mouse.x + ":" + mouse.y + "]", font, 20);
        t.setPosition(100, 100);
        window.draw(t);
    }

    private void reset() {
        config = new LevelConfig();

        config.asteroidBaseRate = 2.4f;
        config.swarmerBaseRate = 16.0f;
        config.aiBaseRate = 14.0f;
        config.textured = false;

        config.bossBaseLives = 10000;
        config.bossSpawnTime = 0;

        config.players[0] = InputType.Controller;
        // config.players[1] = InputType.Controller;

        config.textured = false;
    }

    public void dispose() {}
}
