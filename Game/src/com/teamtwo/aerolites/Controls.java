package com.teamtwo.aerolites;

import com.teamtwo.engine.Input.InputProcessor;
import org.jsfml.system.Vector2f;
import org.jsfml.system.Vector2i;
import org.jsfml.window.Joystick;
import org.jsfml.window.Keyboard;
import org.jsfml.window.Mouse;

public class Controls implements InputProcessor {

    private Vector2f position = new Vector2f(0, 0);
    private boolean mousePressed = false;
    private boolean ePressed, qPressed;
    private float radius = 15;

    @Override
    public void keyPressed(Keyboard.Key key) {
        System.out.println("Key " + key.toString() + " Pressed!");
        switch (key) {
            case E:
                ePressed = true;
                break;
            case Q:
                qPressed = true;
                break;
        }
    }

    @Override
    public void keyReleased(Keyboard.Key key) {
        System.out.println("Key " + key.toString() + " Released!");
        switch (key) {
            case E:
                ePressed = false;
                break;
            case Q:
                qPressed = false;
                break;
        }
    }

    @Override
    public void mouseButtonPressed(Mouse.Button button, Vector2i position) {
        mousePressed = true;
    }

    @Override
    public void mouseButtonReleased(Mouse.Button button, Vector2i position) {
        mousePressed = false;
    }

    @Override
    public void mouseWheelMoved(int amount) {}

    @Override
    public void mouseMoved(Vector2i position) {
        this.position = new Vector2f(position.x, position.y);
    }

    @Override
    public void controllerButtonPressed(int controllerID, int button) {
        System.out.println("Controller " + controllerID + " Pressed Button " + button);
    }

    @Override
    public void controllerButtonReleased(int controllerID, int button) {
        //System.out.println("Controller " + controllerID + " Released Button " + button);
    }

    @Override
    public void controllerAxisMoved(int controllerID, Joystick.Axis axis, float position) {
        //  System.out.println("Controller " + controllerID + " Moved Axis " + axis.toString() + " to Position " + position);
    }

    @Override
    public void controllerConnected(int controllerID) {
        System.out.println("Controller " + controllerID + " was Connected");
    }

    @Override
    public void controllerDisconnected(int controllerID) {
        System.out.println("Controller " + controllerID + " was Disconnected");
    }

    public Vector2f getPosition() {
        return position;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public boolean isqPressed() { return qPressed; }

    public boolean isePressed() { return ePressed; }

    public float getRadius() { return radius; }
}
