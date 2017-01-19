package com.teamtwo.engine.Physics;

import com.teamtwo.engine.Physics.Collisions.AABB;
import com.teamtwo.engine.Physics.ShapeUtil;
import com.teamtwo.engine.Physics.RigidBody;
import com.teamtwo.engine.Utilities.Interfaces.Initialisable;
import com.teamtwo.engine.Utilities.MathUtil;
import org.jsfml.system.Vector2f;

public class Polygon implements Initialisable<RigidBody> {

    /**
     * The maximum number of vertices a polygon can have for performance reasons
     */
    private static final int MAX_VERTICES = 16;

    /** The vertices which make up the polygon */
    private Vector2f[] vertices;
    /** The body associated with the polygon */
    private RigidBody body;

    /** Whether or not the polygon has been initialised */
    private boolean initialised;

    /**
     * Creates a randomly generated polygon
     */
    public Polygon() {
        // Pick a random radius between 10 and 100 and generate
        this(MathUtil.randomFloat(10, 100));
    }

    /**
     * Creates a randomly generated polygon where all of the vertices are within the radius specified
     * @param radius The radius to generate the vertices in
     */
    public Polygon(float radius) {
        // Temporary vertex array with MAX_VERTICES
        Vector2f[] tmp = new Vector2f[MAX_VERTICES];

        // Generate all of the vertices needed
        int vertexCount = 0;
        for(float angle = 0; angle < MathUtil.PI2;) {

            tmp[vertexCount] = new Vector2f(MathUtil.cos(angle) * radius, MathUtil.sin(angle) * radius);

            vertexCount++;
            angle += MathUtil.randomFloat(20, 80) * MathUtil.DEG_TO_RAD;
        }

        // Check vertex count
        if(vertexCount >= MAX_VERTICES) {
            throw new IllegalStateException("Error: A Polygon cannot have more than " + MAX_VERTICES + " vertices");
        }

        // Resize the vertex array so it's not taking up more space
        vertices = new Vector2f[vertexCount];
        System.arraycopy(tmp, 0, vertices, 0, vertexCount);

        // Set to be uninitialised
        initialised = false;
        body = null;
    }

    /**
     * Creates a new polygon from the vertices given
     * @param vertices The vertices to make up the polygon
     */
    public Polygon(Vector2f[] vertices) {
        // Check vertex count
        if(vertices.length >= MAX_VERTICES) {
            throw new IllegalStateException("Error: A Polygon cannot have more than " + MAX_VERTICES + " vertices");
        }

        // Find the centroid to make sure it is (0, 0)
        Vector2f centroid = ShapeUtil.findCentroid(vertices);

        // If the centroid is not at (0, 0) then translate all vertices so the centroid is
        if(!MathUtil.isZero(centroid.x) || !MathUtil.isZero(centroid.y)) {
            this.vertices = new Vector2f[vertices.length];
            for(int i = 0; i < vertices.length; i++) {
                this.vertices[i] = Vector2f.sub(vertices[i], centroid);
            }
        }
        else {
            // Otherwise just store vertices given
            this.vertices = vertices;
        }

        // Set to be uninitialised
        initialised = false;
        body = null;
    }

    public void initialise(RigidBody body) {
        this.body = body;

        // Calculate the area and set the mass
        float area = ShapeUtil.findArea(vertices);
        body.setMass(body.getDensity() * area);

        System.out.println("Mass: " + body.getDensity() * area);

        // Use the moment of inertia of a rectangle
        // This is because it is way too complicated to work out the inertia of the actual shape
        AABB aabb = new AABB(vertices);
        float inertia = ((4 * aabb.getHalfSize().x * aabb.getHalfSize().y)) / 12f;
        inertia *= (MathUtil.squared(aabb.getHalfSize().x * 2) + MathUtil.squared(aabb.getHalfSize().y * 2));

        body.setInertia(Math.abs(inertia));
        System.out.println("Inertia: " + Math.abs(inertia));

        initialised = true;
    }

    /**
     * Gets the untransformed vertices which make up the shape
     * @return The untransformed vertices
     */
    public Vector2f[] getVertices() { return vertices; }

    /**
     * Gets the transformed vertices relative to the body that this shape is attached to
     * @return The transformed vertices
     */
    public Vector2f[] getTransformed() {
        Vector2f[] transformed = new Vector2f[vertices.length];
        float sin = MathUtil.sin(body.getTransform().getAngle());
        float cos = MathUtil.cos(body.getTransform().getAngle());

        for(int i = 0; i < vertices.length; i++) {
            Vector2f vertex = vertices[i];
            float x = (vertex.x * cos) - (vertex.y * sin);
            float y = (vertex.x * sin) + (vertex.y * cos);

            transformed[i] = Vector2f.add(new Vector2f(x, y), body.getTransform().getPosition());
        }

        return transformed;
    }
}
