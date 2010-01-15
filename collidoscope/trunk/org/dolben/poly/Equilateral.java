/**
 *  Copyright (c) 2005-2010 Hank Dolben
 *  Licensed under the Open Software License version 2.1
 *  http://opensource.org/licenses/osl-2.1.php
 */
package org.dolben.poly;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import org.dolben.iiid.R3;
import org.dolben.iiid.Rn;

/**
 *  A polyhedron that has edges that are all the same length.
 *
 *  create() only sets the vertices. Constructor sets the faces.
 */
public abstract class Equilateral extends Polyhedron {
    
    private double edgeLength;
    
    public Equilateral( ) {
        create();
        setEdgeLength();
        findFaces();
    }
    
    /**
     *  Sets the length of an edge. All edges must be the same length.
     *
     *  Finds the minimum distance between vertices.
     */
    protected void setEdgeLength( ) {
        edgeLength = Rn.magnitude(Rn.subtract(vertex[0],vertex[1]));
        for ( int i = 0; i < vertex.length-1; ++i ) {
            for ( int j = i+1; j < vertex.length; ++j ) {
                double distance =
                    Rn.magnitude(Rn.subtract(vertex[i],vertex[j]));
                if ( distance < edgeLength ) {
                    edgeLength = distance;
                }
            }
        }
    }
    
    /**
     *  Finds and sets all of the faces for Polyhedron.
     */
    private void findFaces( ) {
        /*
         *  For each vertex find all of the edges and sort them
         *  by angle around the radius.
         *
         *  Then, for each vertex walk around the faces starting with
         *  each untraversed edge leading from the vertex.
         *  At the next vertex, take the edge that comes after the
         *  one just followed, i.e., exit the node to the left of
         *  where it was entered.
         *
         *  Set the faces array from the list of faces.
         */
        setFaces(walkGraph(createGraph()));
    }
    
    /**
     *  Creates a graph (nodes and edges) of the polyhedron
     *  and returns a list of the nodes.
     */
    private List createGraph( ) {
        List nodes = new LinkedList();
        for ( int i = 0; i < vertex.length; ++i ) {
            nodes.add(new Node(i));
        }
        return nodes;
    }
    
    /**
     *  Walks around each face of the graph and returns a list of the faces.
     */
    private List walkGraph( List nodes ) {
        /*
         *  There are a couple of key points to this algorithm.
         *
         *  First, the list of edges at a node have been ordered by angle
         *  around the vertex, so that if you're looking along the radius
         *  of a vertex towards the center of the polyhedron, the edges are
         *  listed in clockwise order. Then, following the edges around a
         *  face in the counter-clockwise direction (viewed from outside
         *  the polyhedron), upon arriving at a node the next edge will be
         *  to the left of (following, on the list) the edge on which you
         *  arrived.
         *
         *  Second, each edge is listed at two nodes and is on two faces,
         *  and so is traversed twice, once in each direction; each time
         *  for the face on the left.
         */
        List faces = new LinkedList();
        for ( int index = 0; index < vertex.length; ++index ) {
            while ( true ) {
                Node node = (Node)nodes.get(index);
                int next = node.getNodeOnUntraversedEdge();
                if ( next == index ) {
                    break;
                }
                List face = new LinkedList();
                faces.add(face);
                face.add(new Integer(index));
                int last = index;
                while ( next != index ) {
                    face.add(new Integer(next));
                    node = (Node)nodes.get(next);
                    int current = next;
                    next = node.getNextNode(last);
                    last = current;
                }
            }
        }
        return faces;
    }
    
    /**
     *  Sets the array of faces as required by Polyhedron.
     */
    private void setFaces( List faces ) {
        face = new int[faces.size()][];
        for ( int i = 0; i < faces.size(); ++i ) {
            List f = (List)faces.get(i);
            int[] index = new int[f.size()];
            face[i] = index;
            for ( int j = 0; j < f.size(); ++j ) {
                index[j] = ((Integer)f.get(j)).intValue();
            }
        }
    }
    
    /**
     *  Node in the graph of a polyhedron has edges to adjacent nodes.
     *
     *  Edges are ordered where right-hand rule produces the direction
     *  from the node's point to the center.
     */
    private class Node {
        
        private List edges;         // edges at this node
        private int index;          // identity of this node
        private double[] reference; // edges sorted by angle relative to this
        
        /**
         *  Creates a node with the given identity, finding its edges.
         */
        public Node( int i ) {
            edges = null;
            index = i;
            reference = null;
            findEdges();
        }
        
        /**
         *  Finds all of the edges of this node
         *  assuming that they are all the same length.
         */
        private void findEdges( ) {
            final double TOLERANCE = 1e-2;
            double[] v = vertex[index];
            double[] u = unit(v);
            for ( int other = 0; other < vertex.length; ++other ) {
                if ( other != index ) {
                    double[] disp = Rn.subtract(vertex[other],v);
                    double distance = Rn.magnitude(disp);
                    if ( Math.abs(1-(distance/edgeLength)) < TOLERANCE ) {
                        double r = Rn.dot(disp,u)/distance;
                        double[] direction = Rn.subtract(disp,Rn.multiply(r,u));
                        addEdge(other,unit(direction));
                    }
                }
            }
            Collections.sort(edges);
        }
        
        /**
         *  Adds an edge that goes to the given other node in some direction
         *  (perpendicular to the radius vector to this node's vertex).
         */
        public void addEdge( int other, double[] direction ) {
            double angle = 0;
            if ( edges == null ) {
                reference = direction;
                edges = new LinkedList();
            } else {
                angle = Math.acos(Rn.dot(direction,reference));
                if ( Rn.dot(direction,R3.cross(reference,vertex[index])) < 0 ) {
                    angle = -angle;
                }
            }
            edges.add(new Edge(other,angle));
        }
        
        /**
         *  Returns the unit vector in the direction of the one given.
         */
        private double[] unit( double[] v ) {
            return Rn.multiply(1/Rn.magnitude(v),v);
        }
        
        /**
         *  Returns the index of the next node in a face
         *  given the index of the last node.
         */
        public int getNextNode( int last ) {
            Iterator it = edges.iterator();
            while ( it.hasNext() ) {
                Edge edge = (Edge)it.next();
                if ( edge.getOther() == last ) {
                    break;
                }
            }
            Edge edge = (Edge)( it.hasNext() ? it.next() : edges.get(0) );
            return edge.traverse();
        }
        
        /**
         *  Returns the index of the node that is on
         *  a previously untraversed edge from this node.
         */
        public int getNodeOnUntraversedEdge( ) {
            Iterator it = edges.iterator();
            while ( it.hasNext() ) {
                Edge edge = (Edge)it.next();
                if ( !edge.getTraversed() ) {
                    return edge.traverse();
                }
            }
            return index;
        }
    
    }
    
    // An edge in a graph
    private class Edge implements Comparable {
        
        private int other;          // index of node at the other end of edge
        private boolean traversed;  // edge has been traversed
        private double angle;       // angle of edge around radial
        
        // makes a new edge
        public Edge( int othr, double angl ) {
            other = othr;
            angle = angl;
            traversed = false;
        }
        
        // traverses the edge returning the index of the node there
        public int traverse( ) {
            traversed = true;
            return other;
        }
        
        // returns node
        public int getOther( ) {
            return other;
        }
        
        // returns traversed
        public boolean getTraversed( ) {
            return traversed;
        }
        
        // Order Edges by increasing angle.
        public int compareTo( Object obj ) {
            Edge o = (Edge)obj;
            if ( angle < o.angle ) {
                return -1;
            } else if ( angle > o.angle ) {
                return 1;
            } else {
                return 0;
            }
        }
    
    }

}
