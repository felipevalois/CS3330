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
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.util.Duration;

/**
 *
 * @author felip
 */
public class FXMLDocumentController implements Initializable {
    
    private int count = 1;
    private static double cpu;
    boolean running = false;
    Timeline timeline;

    private static int rotate = 0;
    private static int cpuCount = 0;
    private static int cpuUsage = 0;
    
    @FXML
    private ImageView hand;
    @FXML
    private Button start;
    @FXML
    private Button record;
    @FXML
    private Label display;
    @FXML
    private Label record1;
    @FXML
    private Label record2;
    @FXML
    private Label record3;

    public void setupCPU(){
        timeline = new Timeline(new KeyFrame(Duration.millis(100), (ActionEvent) -> {
            cpu = 100*getCPUUsage();
            if(Double.isNaN(cpu)){
                cpu = 0;
            }
            
            System.out.println("CPU = " + cpu); 
            cpuCount++;
            cpuUsage += cpu;
            
            if(cpu>0){
                double usage = cpu;
                double rotate = usage * 3.055;
                display.setText("CPU = " + new DecimalFormat("00.00").format(usage)+"%");
                hand.setRotate(rotate+225);
            }
            else{
                display.setText("CPU = 00.00%");
                hand.setRotate(225);
            }
        }));

        timeline.setCycleCount(Animation.INDEFINITE);
    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setupCPU();
        hand.setRotate(225);
        
        start.setOnAction((ActionEvent event) ->{
            if(!running){
                startBtn();
            }
            else if(running){
                stopBtn();
            }
        });
        
        record.setOnAction((ActionEvent event) ->{
            if(running){
                recordBtn();
            }
            else if(!running){
                resetBtn();
            }            
        });
    }
    
    public void startBtn(){
        running = true;
       record.setText("Record");
        start.setText("Stop");
        timeline.play();
    }
    
    public void stopBtn(){
        running = false;
        record.setText("Reset");
        start.setText("Start");
        timeline.pause();
    }
    
    public void recordBtn(){
        running = true;
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm:ss a");
        String cpuRecord = new DecimalFormat("00.00").format(cpu);
            
        if(count%3==1){
            if(cpu>50)
                record1.setTextFill(Color.RED);
            else
                record1.setTextFill(Color.GREEN);
            record1.setText("Record" + count + ": " + cpuRecord + "\n at " + dateFormat.format(now));
            ++count;
        }
        else if(count%3==2){
            if(cpu>50)
                record2.setTextFill(Color.RED);
            else
                record2.setTextFill(Color.GREEN);
            record2.setText("Record" + count + ": " + cpuRecord + "\n at " + dateFormat.format(now));
            ++count;
        }
        else{
            if(cpu>50)
                record3.setTextFill(Color.RED);
            else
                record3.setTextFill(Color.GREEN);
            record3.setText("Record" + count + ": " + cpuRecord + "\n at " + dateFormat.format(now));
            ++count;
        }
    }
    
    public void resetBtn(){        
        running = false;
        record.setText("Record");
        start.setText("Start");
        hand.setRotate(225);
        count = 1;

        display.setText("CPU = --.--%");
        record1.setText("--.--%");
        record2.setText("--.--%");
        record3.setText("--.--%");

        record1.setTextFill(Color.BLACK);
        record2.setTextFill(Color.BLACK);
        record3.setTextFill(Color.BLACK);    
    }
    
    private static double getSystemCpuLoad(){
        return cpuUsage/cpuCount*100;    
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
}
