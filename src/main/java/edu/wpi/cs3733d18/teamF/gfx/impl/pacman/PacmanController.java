package edu.wpi.cs3733d18.teamF.gfx.impl.pacman;


public class PacmanController{

    GameMapDrawer gameMapDrawer;
    public PacmanController(){
        gameMapDrawer = new GameMapDrawer();
    }
    public PacmanController(double x, double y, Direction d){
        gameMapDrawer = new GameMapDrawer(x, y, d);
    }

}
