package com.teamtwo.aerolites.States;

import com.teamtwo.engine.Input.Controllers.Controller;
import com.teamtwo.engine.Input.Controllers.Controllers;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.MathUtil;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

public class ControllerTests extends State {

    private Sprite controller;

    private Vector2f leftPos;
    private Vector2f rightPos;

    private CircleShape leftStick;
    private CircleShape rightStick;
    private CircleShape[] buttons;
    private ConvexShape[] dpad;
    private RectangleShape[] triggers;

    private Font font;

    public ControllerTests(GameStateManager gsm) {
        super(gsm);

        Texture texture = ContentManager.instance.loadTexture("Controller", "Controller.png");
        font = ContentManager.instance.loadFont("Ubuntu", "Ubuntu.ttf");

        controller = new Sprite(texture);
        controller.setPosition(381, 150);

        buttons = new CircleShape[9];
        buttons[0] = new CircleShape(11);
        buttons[0].setPosition(controller.getPosition().x + 441, 150 + 86);

        buttons[1] = new CircleShape(11);
        buttons[1].setPosition(controller.getPosition().x + 401, controller.getPosition().y + 127);

        buttons[2] = new CircleShape(11);
        buttons[2].setPosition(controller.getPosition().x + 359, controller.getPosition().y + 86);

        buttons[3] = new CircleShape(11);
        buttons[3].setPosition(controller.getPosition().x + 401, controller.getPosition().y + 46);

        buttons[4] = new CircleShape(20);
        buttons[4].setOutlineThickness(4f);
        buttons[4].setOutlineColor(new Color(55, 55, 55));
        buttons[4].setPosition(controller.getPosition().x + 240, controller.getPosition().y + 80);

        buttons[5] = new CircleShape(11);
        buttons[5].setPosition(controller.getPosition().x + 192, controller.getPosition().y + 90);

        buttons[6] = new CircleShape(11);
        buttons[6].setPosition(controller.getPosition().x + 304, controller.getPosition().y + 90);

        buttons[7] = new CircleShape(11);
        buttons[7].setPosition(controller.getPosition().x + 140, controller.getPosition().y);

        buttons[8] = new CircleShape(11);
        buttons[8].setPosition(controller.getPosition().x + 367, controller.getPosition().y);

        leftPos = new Vector2f(381 + 100, 150 + 95);
        rightPos = new Vector2f(381 + 331, 150 + 190);

        leftStick = new CircleShape(20);
        leftStick.setOrigin(20, 20);
        leftStick.setFillColor(new Color(140, 140, 140));
        leftStick.setOutlineColor(new Color(55, 55, 55));
        leftStick.setOutlineThickness(4);
        leftStick.setPosition(leftPos);

        rightStick = new CircleShape(20);
        rightStick.setOrigin(20, 20);
        rightStick.setFillColor(new Color(140, 140, 140));
        rightStick.setOutlineColor(new Color(55, 55, 55));
        rightStick.setOutlineThickness(4);
        rightStick.setPosition(rightPos);


        // Left
        Vector2f[] vertices = new Vector2f[5];
        vertices[0] = new Vector2f(10, 10);
        vertices[1] = new Vector2f(25, 10);
        vertices[2] = new Vector2f(32.5f, 17.5f);
        vertices[3] = new Vector2f(25, 25);
        vertices[4] = new Vector2f(10, 25);

        dpad = new ConvexShape[4];
        dpad[0] = new ConvexShape(vertices);
        dpad[0].setPosition(200 + 324, 200 + 120);

        // Up
        vertices[0] = new Vector2f(10, 10);
        vertices[1] = new Vector2f(25, 10);
        vertices[2] = new Vector2f(25, 25);
        vertices[3] = new Vector2f(17.5f, 32.5f);
        vertices[4] = new Vector2f(10, 25);
        dpad[1] = new ConvexShape(vertices);
        dpad[1].setPosition(220 + 324, 180 + 120);

        // Down
        vertices[0] = new Vector2f(10, 10);
        vertices[1] = new Vector2f(17.5f, 2.5f);
        vertices[2] = new Vector2f(25, 10);
        vertices[3] = new Vector2f(25, 25);
        vertices[4] = new Vector2f(10, 25);
        dpad[2] = new ConvexShape(vertices);
        dpad[2].setPosition(220 + 324, 220 + 120);

        // Right
        vertices[0] = new Vector2f(2.5f, 17.5f);
        vertices[1] = new Vector2f(10, 10);
        vertices[2] = new Vector2f(25, 10);
        vertices[3] = new Vector2f(25, 25);
        vertices[4] = new Vector2f(10, 25);
        dpad[3] = new ConvexShape(vertices);
        dpad[3].setPosition(240 + 324, 200 + 120);

        triggers = new RectangleShape[2];
        triggers[0] = new RectangleShape(new Vector2f(200, 40));
        triggers[0].setPosition(50, 150);

        triggers[1] = new RectangleShape(new Vector2f(200, 40));
        triggers[1].setPosition(50, 250);

    }

    public void update(float dt) {

        if(Controllers.isButtonPressed(Controller.Player.One, Controller.Button.A)) {
            buttons[1].setFillColor(Color.GREEN);
        }
        else {
            buttons[1].setFillColor(Color.WHITE);
        }

        if(Controllers.isButtonPressed(Controller.Player.One, Controller.Button.B)) {
            buttons[0].setFillColor(Color.RED);
        }
        else {
            buttons[0].setFillColor(Color.WHITE);
        }

        if(Controllers.isButtonPressed(Controller.Player.One, Controller.Button.X)) {
            buttons[2].setFillColor(Color.BLUE);
        }
        else {
            buttons[2].setFillColor(Color.WHITE);
        }

        if(Controllers.isButtonPressed(Controller.Player.One, Controller.Button.Y)) {
            buttons[3].setFillColor(Color.YELLOW);
        }
        else {
            buttons[3].setFillColor(Color.WHITE);
        }


        if(Controllers.isButtonPressed(Controller.Player.One, Controller.Button.DPad_Up)) {
            dpad[1].setFillColor(Color.CYAN);
        }
        else {
            dpad[1].setFillColor(Color.WHITE);
        }

        if(Controllers.isButtonPressed(Controller.Player.One, Controller.Button.DPad_Down)) {
            dpad[2].setFillColor(Color.CYAN);
        }
        else {
            dpad[2].setFillColor(Color.WHITE);
        }

        if(Controllers.isButtonPressed(Controller.Player.One, Controller.Button.DPad_Left)) {
            dpad[0].setFillColor(Color.CYAN);
        }
        else {
            dpad[0].setFillColor(Color.WHITE);
        }

        if(Controllers.isButtonPressed(Controller.Player.One, Controller.Button.DPad_Right)) {
            dpad[3].setFillColor(Color.CYAN);
        }
        else {
            dpad[3].setFillColor(Color.WHITE);
        }

        if(Controllers.isButtonPressed(Controller.Player.One, Controller.Button.Xbox)) {
            buttons[4].setFillColor(new Color(0, 255, 67));
        }
        else {
            buttons[4].setFillColor(Color.WHITE);
        }

        if(Controllers.isButtonPressed(Controller.Player.One, Controller.Button.Back)) {
            buttons[5].setFillColor(new Color(255, 221, 86));
        }
        else {
            buttons[5].setFillColor(Color.WHITE);
        }

        if(Controllers.isButtonPressed(Controller.Player.One, Controller.Button.Start)) {
            buttons[6].setFillColor(new Color(255, 221, 86));
        }
        else {
            buttons[6].setFillColor(Color.WHITE);
        }

        if(Controllers.isButtonPressed(Controller.Player.One, Controller.Button.R3)) {
            rightStick.setFillColor(Color.WHITE);
        }
        else {
            rightStick.setFillColor(new Color(140, 140, 140));
        }

        if(Controllers.isButtonPressed(Controller.Player.One, Controller.Button.L3)) {
            leftStick.setFillColor(Color.WHITE);
        }
        else {
            leftStick.setFillColor(new Color(140, 140, 140));
        }

        if(Controllers.isButtonPressed(Controller.Player.One, Controller.Button.LB)) {
            buttons[7].setFillColor(new Color(66, 244, 161));
        }
        else {
            buttons[7].setFillColor(Color.WHITE);
        }

        if(Controllers.isButtonPressed(Controller.Player.One, Controller.Button.RB)) {
            buttons[8].setFillColor(new Color(66, 244, 161));
        }
        else {
            buttons[8].setFillColor(Color.WHITE);
        }


        // Thumbsticks
        Vector2f left = Controllers.getThumbstickValues(Controller.Player.One, Controller.Thumbstick.Left);
        Vector2f right = Controllers.getThumbstickValues(Controller.Player.One, Controller.Thumbstick.Right);

        float valX = (left.x + 100) / 200f;
        float valY = (left.y + 100) / 200f;

        valX = MathUtil.lerp(leftPos.x - 20, leftPos.x + 20, valX);
        valY = MathUtil.lerp(leftPos.y - 20, leftPos.y + 20, valY);

        leftStick.setPosition(valX, valY);

        valX = (right.x + 100) / 200f;
        valY = (right.y + 100) / 200f;

        valX = MathUtil.lerp(rightPos.x - 20, rightPos.x + 20, valX);
        valY = MathUtil.lerp(rightPos.y - 20, rightPos.y + 20, valY);

        rightStick.setPosition(valX, valY);

        int r, g, b;

        // Triggers
        float val = ((Controllers.getTriggerValue(Controller.Player.One, Controller.Trigger.LT) + 100) / 200f);

        r = (int)MathUtil.lerp(Color.BLUE.r, Color.RED.r, val);
        g = (int)MathUtil.lerp(Color.BLUE.g, Color.RED.g, val);
        b = (int)MathUtil.lerp(Color.BLUE.b, Color.RED.b, val);

        val = MathUtil.lerp(0, 200, val);

        triggers[0].setFillColor(new Color(r, g, b));
        triggers[0].setSize(new Vector2f(val, 40));

        val = ((Controllers.getTriggerValue(Controller.Player.One, Controller.Trigger.RT) + 100) / 200f);

        r = (int)MathUtil.lerp(Color.BLUE.r, Color.RED.r, val);
        g = (int)MathUtil.lerp(Color.BLUE.g, Color.RED.g, val);
        b = (int)MathUtil.lerp(Color.BLUE.b, Color.RED.b, val);

        val = MathUtil.lerp(0, 200, val);

        triggers[1].setFillColor(new Color(r, g, b));
        triggers[1].setSize(new Vector2f(val, 40));
    }

    public void render() {
        window.clear(new Color(104, 149, 237));

        window.draw(controller);
        for(int i = 0; i < 9; i++) {
            window.draw(buttons[i]);
        }

        window.draw(leftStick);
        window.draw(rightStick);

        for(int i = 0; i < 4; i++) {
            window.draw(dpad[i]);
        }


        Text t = new Text("Left Trigger", font, 20);
        t.setPosition(50, 120);

        window.draw(t);
        window.draw(triggers[0]);

        t.setString("Right Trigger");
        t.setPosition(50, 220);

        window.draw(t);
        window.draw(triggers[1]);
    }

    public void dispose() {}
}
