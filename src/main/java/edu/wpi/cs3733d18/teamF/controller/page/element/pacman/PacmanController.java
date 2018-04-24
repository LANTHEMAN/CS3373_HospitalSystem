package edu.wpi.cs3733d18.teamF.controller.page.element.pacman;


public class PacmanController{

    PacmanDrawable pacmanDrawable;
    public PacmanController(){
        pacmanDrawable = new PacmanDrawable();
    }
    public PacmanController(double x, double y, Direction d){
        pacmanDrawable = new PacmanDrawable(x, y, d);
    }

}
