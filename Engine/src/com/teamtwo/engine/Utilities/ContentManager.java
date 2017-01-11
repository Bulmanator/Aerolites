package com.teamtwo.engine.Utilities;

import org.jsfml.audio.Music;
import org.jsfml.audio.Sound;
import org.jsfml.audio.SoundBuffer;
import org.jsfml.graphics.Font;
import org.jsfml.graphics.Texture;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;

/**
 * A Well-defined location for all content to be loaded into, this prevents extra memory from being used when using content
 * @author James Bulman
 */
public class ContentManager {

    /**
     * Singleton instance which allows the user to load content
     */
    public static final ContentManager instance;
    static { instance = new ContentManager(); }

    // Maps a String name onto a Texture
    private HashMap<String, Texture> textures;
    // Maps a String name onto a font
    private HashMap<String, Font> fonts;
    // Maps a String name onto a sound
    private HashMap<String, Sound> sounds;
    // Maps a String name onto a piece of music
    private HashMap<String, Music> music;


    /**
     * Private constructor to prevent other instances from being created
     */
    private ContentManager() {
        textures = new HashMap<>();
        fonts = new HashMap<>();
        sounds = new HashMap<>();
        music = new HashMap<>();
    }

    /**
     * Retrieve a loaded Texture from the Content Manager
     * @param name The name associated to the Texture to retrieve
     * @return The Texture if it is loaded, otherwise throws an {@link IllegalArgumentException}
     */
    public Texture getTexture(String name) {
        // Checks to make sure the name has been loaded
        if(!textures.containsKey(name)) {
            throw new IllegalArgumentException("Error: Unknown Texture: " + name);
        }

        // Returns the Texture associated
        return textures.get(name);
    }

    /**
     * Retrieves a loaded Font from the Content Manager
     * @param name The name associated to the Font to retrieve
     * @return The Font if it has been loaded, otherwise throws an {@link IllegalArgumentException}
     */
    public Font getFont(String name) {
        // Checks to make sure the name has been loaded
        if(!fonts.containsKey(name)) {
            throw new IllegalArgumentException("Error: Unknown Font: " + name);
        }

        // Returns the Font associated
        return fonts.get(name);
    }

    /**
     * Retrieves a loaded Sound from the Content Manager
     * @param name The name associated with the Sound to retrieve
     * @return The Sound if it has been loaded, otherwise throws an {@link IllegalArgumentException}
     */
    public Sound getSound(String name) {
        // Checks to make sure the name has been loaded
        if(!sounds.containsKey(name)) {
            throw new IllegalArgumentException("Error: Unknown Sound: " + name);
        }

        // Returns the Sound associated
        return sounds.get(name);
    }

    /**
     * Retrieves a loaded piece of Music from the Content Manager
     * @param name The name associated with the Music to retrieve
     * @return The Music if it has been loaded, otherwise throws an {@link IllegalArgumentException}
     */
    public Music getMusic(String name) {
        // Checks to make sure the name has been loaded
        if(!music.containsKey(name)) {
            throw new IllegalArgumentException("Error: Unknown Music: " + name);
        }

        // Returns the Music associated
        return music.get(name);
    }

    /**
     * Loads a Texture from the given path into the Content Manager
     * @param name The name to associate to the Texture for retrieving it
     * @param path The path to the Texture
     */
    public void loadTexture(String name, String path) {
        // Makes sure a duplicate name has not been user
        // Stops loading if it has
        if(textures.containsKey(name)) {
            System.err.println("Warning: A Texture of name \"" + name + "\" has already been loaded");
            return;
        }

        // Creates a new Texture
        Texture t = new Texture();
        try {
            // Tries to load if from the path
            t.loadFromFile(Paths.get(System.getProperty("user.dir") + File.separator + path));
        }
        catch (IOException ex) {
            // If it fails the exit
            System.err.println("Error: Failed to load Texture: " + path);
            ex.printStackTrace();
            System.exit(-1);
        }

        // If it succeeds then put it into the hash map
        textures.put(name, t);
    }

    /**
     * Loads a Font from the given path into the Content Manager
     * @param name The name to associate to the Font for retrieving it
     * @param path The path to the Font
     */
    public void loadFont(String name, String path) {
        // Makes sure a duplicate name has not been used
        // Stops loading if it has
        if(fonts.containsKey(name)) {
            System.err.println("Warning: A Font of name \"" + name + "\" has already been loaded");
            return;
        }

        // Creates a new font
        Font f = new Font();
        try {
            // Tries to load it from the path
            f.loadFromFile(Paths.get(System.getProperty("user.dir") + File.separator + path));
        }
        catch (IOException ex) {
            // If it fails the exit
            System.err.println("Error: Failed to load Font: " + path);
            ex.printStackTrace();
            System.exit(-1);
        }

        // If it succeeds then put it into the hash map
        fonts.put(name, f);
    }

    /**
     * Loads a Sound from the given path into the Content Manager
     * @param name The name to associate with the Sound for retrieving
     * @param path The path to the Sound
     */
    public void loadSound(String name, String path) {
        // Makes sure a duplicate name has not been used
        // Stops loading if it has
        if(sounds.containsKey(name)) {
            System.err.println("Warning: A Sound of name \"" + name + "\" has already been loaded");
            return;
        }

        // Creates a new Sound
        Sound s = new Sound();
        try {
            // Loads a SoundBuffer from from the path
            SoundBuffer soundBuffer = new SoundBuffer();
            soundBuffer.loadFromFile(Paths.get(System.getProperty("user.dir") + File.separator + path));
            s.setBuffer(soundBuffer);
        }
        catch (IOException ex) {
            // If it fails then exit
            System.err.println("Error: Failed to load Sound: " + path);
            ex.printStackTrace();
            System.exit(-1);
        }

        // If it succeeds then put it into the hash map
        sounds.put(name, s);
    }

    /**
     * Loads Music from the given path into the Content Manager
     * @param name The name to associate with the Music for retrieving
     * @param path The path to the Music
     */
    public void loadMusic(String name, String path) {
        // Makes sure a duplicate name has not been used
        // Stops loading if it has
        if(music.containsKey(name)) {
            System.err.println("Warning: Music of the name \"" + name + "\" has already been loaded");
            return;
        }

        // Creates new Music
        Music m = new Music();
        try {
            // Tries to load from the path
            m.openFromFile(Paths.get(System.getProperty("user.dir") + File.separator + path));
        }
        catch (IOException ex) {
            // If it fails then exit
            System.err.println("Error: Failed to load Music: " + path);
            ex.printStackTrace();
            System.exit(-1);
        }

        // If it succeeds then put it into the hash map
        music.put(name, m);
    }
}
