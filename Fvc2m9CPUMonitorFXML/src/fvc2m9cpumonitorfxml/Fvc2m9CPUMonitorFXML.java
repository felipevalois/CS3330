/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fvc2m9cpumonitorfxml;

import java.lang.management.ManagementFactory;
import java.lang.management.OperatingSystemMXBean;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 *
 * @author felip
 */
public class Fvc2m9CPUMonitorFXML extends Application {
    
    private static double cpu = 0;
    
    @Override
    public void start(Stage primaryStage) {
        
        HBox root = new HBox();
        TextArea textArea = new TextArea(); 
        root.getChildren().add(textArea); 
        
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(100), (ActionEvent) -> {
            cpu = Fvc2m9CPUMonitorFXML.getCPUUsage();
            System.out.println("CPU: " + cpu); 
            textArea.appendText(cpu + "\n");
            
        }));
        
        timeline.setCycleCount(100);
        timeline.play();
        
        Scene scene = new Scene(root, 400, 400); 
        
        primaryStage.setTitle("CPU Monitor Starter Code"); 
        primaryStage.setScene(scene); 
        primaryStage.show(); 
    }
    
    private static double getCPUUsage() {
        OperatingSystemMXBean operatingSystemMXBean = ManagementFactory.getOperatingSystemMXBean();
        double value = 0;
        for (Method method : operatingSystemMXBean.getClass().getDeclaredMethods()) {
            method.setAccessible(true);
            if (method.getName().startsWith("getSystemCpuLoad")
                    && Modifier.isPublic(method.getModifiers())) {
                try {
                    value = (double) method.invoke(operatingSystemMXBean);
                } catch (Exception e) {
                    value = 0;
                }
                return value;
            }
        }
        return value;
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
