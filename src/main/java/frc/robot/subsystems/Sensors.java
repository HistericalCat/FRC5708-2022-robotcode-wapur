package frc.robot.subsystems;

import com.revrobotics.ColorSensorV3;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.util.Color;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Sensors extends SubsystemBase {
    private final ColorSensorV3 colorSensor1 = new ColorSensorV3(I2C.Port.kOnboard);
    private final ColorSensorV3 colorSensor2 = new ColorSensorV3(I2C.Port.kMXP);

    public Sensors(){}

    @Override
    public void periodic(){
        Color color1 = colorSensor1.getColor();
        System.out.println("Color sensor 1 seeing " + color1.red + " " + color1.green + " " + color1.blue);

        Color color2 = colorSensor2.getColor();
        System.out.println("Color sensor 2 seeing " + color2.toString());
    }
}
