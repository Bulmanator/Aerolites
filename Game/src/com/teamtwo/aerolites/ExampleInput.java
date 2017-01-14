package com.teamtwo.aerolites;

import com.teamtwo.engine.Input.InputProcessor;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Joystick;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;

public class ExampleInput implements InputProcessor {

    public Vector2f position;
    public Keyboard.Key lastKeyPressed;
    public Keyboard.Key lastKeyReleased;

    public boolean mouseDown;

    public ExampleInput() {
        position = new Vector2f(0, 0);
        lastKeyPressed = null;
        lastKeyReleased = null;
        mouseDown = false;
    }

    public void keyPressed(Keyboard.Key key) { lastKeyPressed = key; }

    public void keyReleased(Keyboard.Key key) { lastKeyReleased = key; }

    public void mouseButtonPressed(Mouse.Button button, Vector2i position) { mouseDown = true; }

    public void mouseButtonReleased(Mouse.Button button, Vector2i position) { mouseDown = false; }

    public void mouseWheelMoved(int amount) {}

    public void mouseMoved(Vector2i position) { this.position = new Vector2f(position); }

    public void controllerConnected(int id) {}

    public void controllerDisconnected(int id) {}

    public void controllerButtonPressed(int id, int button) {}

    public void controllerButtonReleased(int id, int button) {}

    public void controllerAxisMoved(int id, Joystick.Axis axis, float position) {}
}
