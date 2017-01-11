package com.teamtwo.engine.Physics.Shapes;

import org.jsfml.graphics.RenderWindow;
import org.jsfml.system.Vector2f;

public class Polygon extends Shape {

    // TODO Complete Implementation

    private Vector2f[] vertices;
    private Vector2f[] normals;

    public Polygon() { setAsBox(25, 25); }

    public Polygon(float x, float y) {}

    public Polygon(float x, float y, float radius) {}

    public Polygon(Vector2f[] vertices) {}

    public void set(Vector2f[] vertices) {}

    public void setAsBox(float hw, float hh) {}

    public void rotate(float degrees) {}

    public void render(RenderWindow renderer) {}

    public Vector2f[] getVertices() { return vertices; }
    public Vector2f getVertex(int index) { return vertices[index]; }
    public Vector2f getNormal(int index) { return normals[index]; }


}
