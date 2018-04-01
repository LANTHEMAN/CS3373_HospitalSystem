package com.github.CS3733_D18_Team_F_Project_0.controller.page;


import org.junit.Test;
import javafx.scene.control.Button;
import java.awt.*;
import java.awt.event.InputEvent;

import static org.junit.Assert.*;

public class HomeControllerTest {

    @Test
    public void TestAdminButton() throws java.awt.AWTException{
        Robot R = new Robot();
        R.mouseMove(950,90);
        R.mousePress(InputEvent.BUTTON1_MASK);

        try{
            Thread.sleep(250);
        }catch(InterruptedException e){}
        R.mouseRelease(InputEvent.BUTTON1_MASK);

        try{
            Thread.sleep(10000);
        }catch(InterruptedException e){}
    }
}
