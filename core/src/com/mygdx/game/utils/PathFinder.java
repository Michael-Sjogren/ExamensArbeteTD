package com.mygdx.game.utils;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.mygdx.game.managers.LevelManager;

import java.util.ArrayList;
import java.util.Comparator;

/** This is a util class for path finding**/
public abstract class PathFinder {

    /**
     * @param startNode - the tile position of where the path begins
     * @param endNode - the tile position where the path ends
     * @param canGoDiag - if true: path will include calculation of nodes that are diagonal ,
     *                  else will only calculate up , down left , right
     * **/
	
    public static ArrayList<Node> findPath(Vector2 startNode , Vector2 endNode , boolean canGoDiag , boolean isFlying){
        ArrayList<Node> openList = new ArrayList<>();
        ArrayList<Node> closedList = new ArrayList<>();
        Node current = new Node(startNode , null , 0 , startNode.dst(endNode));
        openList.add(current);
        while(openList.size() > 0){
            // sorts the nodes with lowest fCost lowest fCost should be index 0
            openList.sort(nodeSorter);
            current = openList.get(0);
            if(current.getCordinates().equals(endNode)){
                ArrayList<Node> path = new ArrayList<>();
                while(current.getParent() != null){
                    path.add(current);
                    current = current.getParent();
                }
                return path;
            }
            openList.remove(current);
            closedList.add(current);
            // loops through all adjacent tiles
            for (int i = 0; i < 9; i++){
                if(i == 4) continue;
                if(!canGoDiag){
                    if (i == 0) continue;
                    if (i == 2) continue;
                    if (i == 6) continue;
                    if (i == 8) continue;
                }
                int x = MathUtils.floor(current.getCordinates().x);
                int y = MathUtils.floor(current.getCordinates().y);
            
                // at is a valid path
                // to prevent creating a path diagonally we have to check
                // that there arent any walls between the diagonal path

                // when topleft(diagonally) is being considerd, check if it has any walls below it or to its right
                // if it does, continue
                if(canGoDiag){
                    final Tile top = LevelManager.getTile(x  , y  - 1);
                    final Tile bottom = LevelManager.getTile(x , y + 1);
                    final Tile right = LevelManager.getTile(x  + 1 , y );
                    final Tile left = LevelManager.getTile(x  - 1, y);
                    
                    if(i == 0){
                        if(top == null || left == null) continue;
                        if(top.getType() == TileType.WALL && left.getType() == TileType.WALL) continue;
                    }
                    if(i == 2){
                        if(top == null || right == null) continue;
                        if(top.getType() == TileType.WALL && right.getType() == TileType.WALL) continue;
                    }
                    if(i == 6){
                        if(bottom == null || left == null) continue;
                        if(bottom.getType() == TileType.WALL && left.getType() == TileType.WALL) continue;
                    }
                    if(i == 8){
                        if(bottom == null || right == null) continue;
                        if(bottom.getType() == TileType.WALL && right.getType() == TileType.WALL) continue;
                    }
                }
                
                int xDir = (i % 3) - 1;
                int yDir = (i / 3) - 1;
                Tile at = LevelManager.getTile(x + xDir , y + yDir);
                if (at == null) continue;
                if(!isFlying){
                	if(at.getType() == TileType.WALL) continue;
                }

                Vector2 a = new Vector2(x + xDir , y + yDir);
                double gCost = current.getgCost() + current.getCordinates().dst(a);
                double hCost = current.getCordinates().dst(endNode);
                Node node = new Node(a , current ,gCost , hCost);
                if (vectInList(closedList, a) && gCost >= current.getgCost()) continue;
                if (!vectInList(openList, a) || gCost < current.getgCost()) openList.add(node);
            }
        }
//        System.out.println("found no path, returning null :: START :: " + startNode + " END :: "  + endNode);
        return null;
    }
    
    
    public static void drawPath(ArrayList<Node> path , ShapeRenderer sr , OrthographicCamera camera){
    	sr.setAutoShapeType(true);
    	sr.setProjectionMatrix(camera.combined);
    	sr.begin();
    	for(Node node : path){
    		Vector2 start = node.getParent() != null ? node.getParent().getCordinates().cpy().scl(32) : LevelManager.tileSpawn.getTileCenter();
    		sr.setColor(Color.WHITE);
    		sr.set(ShapeType.Line);
    		sr.line(start , node.getCordinates().cpy().scl(32));
    	}
    	sr.end();
    }


    private static Comparator<Node> nodeSorter = new Comparator<Node>() {
        @Override
        public int compare(Node n0, Node n1) {
            if(n1.getfCost() < n0.getfCost()) return 1;
            if(n1.getfCost() > n0.getfCost()) return -1;
            return 0;
        }
    };

    private static boolean vectInList(ArrayList<Node> list , Vector2 vector2){
        for (Node n : list) {
            if(n.getCordinates().equals(vector2)) return true;
        }
        return false;
    }


}
