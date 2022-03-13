package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import frc.robot.subsystems.Sensors;
import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
    private static Sensors sensors = new Sensors();
    private VictorSPX actuatorMotor;

    public static DigitalInput actuatorOut = new DigitalInput(3);
    public static DigitalInput actuatorIn = new DigitalInput(2);
    public static DigitalInput winchLimit1 = new DigitalInput(0);
    public static DigitalInput winchLimit2 = new DigitalInput(1);

    //for climber in a box
    private VictorSPX winchMotor1;
    private VictorSPX winchMotor2;

    public Climber(){
        //placeholder port numbers
        actuatorMotor = new WPI_VictorSPX(4);
        winchMotor1   = new WPI_VictorSPX(2);
        winchMotor2   = new WPI_VictorSPX(3);

    }

    public void driveActuator(float value){
        actuatorMotor.set(ControlMode.PercentOutput, value);
    }

    public void driveWinch1(float value){
        winchMotor1.set(ControlMode.PercentOutput, value);
    }

    public void driveWinch2(float value){
        winchMotor2.set(ControlMode.PercentOutput, value);
    } 

    
}
