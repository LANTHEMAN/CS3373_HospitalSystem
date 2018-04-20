package edu.wpi.cs3733d18.teamF.face;

import java.awt.image.BufferedImage;
import java.io.File;

import javax.swing.JFrame;

import com.googlecode.javacv.CanvasFrame;
import com.googlecode.javacv.OpenCVFrameGrabber;
import com.googlecode.javacv.OpenCVFrameRecorder;
import com.googlecode.javacv.cpp.opencv_core.IplImage;

public class CaptureImage implements Runnable{

    IplImage image;
    static CanvasFrame frame = new CanvasFrame("Web Cam");
    public static boolean running = false;

    public Webcam() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    @Override
    public void run() {
        try {
            grabber.setImageWidth(800);
            grabber.setImageHeight(600);
            grabber.start();
            while (running) {
                IplImage cvimg = grabber.grab();
                BufferedImage image;
                if (cvimg != null) {
                    // opencv_core.cvFlip(cvimg, cvimg, 1); // mirror
                    // show image on window
                    image = cvimg.getBufferedImage();
                    frame.showImage(image);
                }
            }
            grabber.stop();
            frame.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String... args) {
        Webcam webcam = new Webcam();
        webcam.start();
    }

    public void start() {
        new Thread(this).start();
        running = true;
    }

    public void stop() {
        running = false;
    }
}
