package com.teamtwo.aerolites.States;

import com.teamtwo.aerolites.ExampleInput;
import com.teamtwo.engine.Graphics.Animation;
import com.teamtwo.engine.Graphics.Particles.Particle;
import com.teamtwo.engine.Graphics.Particles.ParticleEmitter;
import com.teamtwo.engine.Utilities.ContentManager;
import com.teamtwo.engine.Utilities.State.GameStateManager;
import com.teamtwo.engine.Utilities.State.State;
import org.jsfml.graphics.*;
import org.jsfml.system.Vector2f;

//
// This is an example game state
// Game states can be used to implement separate parts of the game
// Such as levels, menus etc.
// Just extend the State class to make a new State and you can add/ remove states from the Game State Manager
//

public class ExampleState extends State {

    private ParticleEmitter emitter;
    private Animation animation;

    private ExampleInput input;
    private Font font;

    /**
     * Called once at the beginning of execution
     * @param gsm The Game state manager which this state belongs to
     */
    public ExampleState(GameStateManager gsm) {
        super(gsm);

        // This method is like the initialise method from the ExampleGame class

        // Changing the Input handler
        input = new ExampleInput();
        game.getEngine().setInputHandler(input);

        // Particle Emitter example
        Particle.Configuration config = new Particle.Configuration();

        config.position = new Vector2f(50, window.getSize().y / 2);

        config.minAngle = 85;
        config.maxAngle = 95;
        config.speed = 100;
        config.rotationalSpeed = 2;

        config.pointCount = 6;

        // 7 colours used, 8 are supported however
        config.colours[0] = Color.RED;
        config.colours[1] = Color.YELLOW;
        config.colours[2] = Color.MAGENTA;
        config.colours[3] = Color.GREEN;
        config.colours[4] = new Color(255, 144, 0); // Orange
        config.colours[5] = new Color(255, 153, 255); // Pink
        config.colours[6] = Color.BLUE;

        config.fadeOut = true;

        config.startSize = 12;
        config.endSize = 8;

        config.minLifetime = 2.5f;
        config.maxLifetime = 3;

        emitter = new ParticleEmitter(config, 30, 300);

        // Animation Example

        // Loading texture into the content manager
        Texture texture = ContentManager.instance.loadTexture("PlayerWalk", "PlayerMove.png");

        // Feel free to mess around with these values
        animation = new Animation(texture, 1, 5, 0.1f);
        animation.setPosition(170, (window.getSize().y / 2) - animation.width);

        ContentManager.instance.loadFont("Ubuntu", "Ubuntu.ttf");
        // Using get instead of loading directly
        font = ContentManager.instance.getFont("Ubuntu");
    }

    public void update(float dt) {
        // Update the particle system
        emitter.update(dt);
        // Update the animation
        animation.update(dt);
    }

    public void render() {
        // Render the particle system
        emitter.render(window);
        // Draw the animation
        animation.render(window);

        // Drawing text
        Text text = new Text("Particle Emitter", font, 12);
        text.setPosition(10, (window.getSize().y / 2) + 16);
        window.draw(text);

        text = new Text("Animation", font, 12);
        text.setPosition(140, (window.getSize().y / 2) + 16);
        window.draw(text);

        // Shows if the mouse is clicked
        CircleShape circle = new CircleShape(20);
        if(input.mouseDown) {
            circle.setFillColor(Color.RED);
        }

        circle.setPosition(270, (window.getSize().y / 2) - 20);
        window.draw(circle);

        // Mouse Position
        text = new Text("Mouse Position: [" + input.position.x + ":" + input.position.y + "]", font, 15);
        text.setPosition(330, (window.getSize().y / 2) - 30);
        window.draw(text);

        // Last key pressed and released
        text = new Text("Last Key Pressed: " +
                (input.lastKeyPressed == null ? "None" : input.lastKeyPressed.toString()), font, 15);
        text.setPosition(330, (window.getSize().y / 2) - 10);
        window.draw(text);

        text = new Text("Last Key Released: " +
                (input.lastKeyReleased == null ? "None" : input.lastKeyReleased.toString()), font, 15);
        text.setPosition(330, (window.getSize().y / 2) + 10);
        window.draw(text);

    }

    public void dispose() {}
}
