package frc.robot.subsystems;

import frc.robot.lib.TCS34725ColorSensor;
import frc.robot.lib.TCS34725ColorSensor.TCSColor;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Sensors extends SubsystemBase {
    private TCS34725ColorSensor colorSensor = new TCS34725ColorSensor();
    private TCSColor color; 

    public Sensors(){
        if(colorSensor.init() != 0){
            System.out.println("Failed to initialize sensor!");
        }
    }

    @Override
    public void periodic(){
        color = colorSensor.readColors();
        System.out.println("Seeing " + color.toString());
    }
}
