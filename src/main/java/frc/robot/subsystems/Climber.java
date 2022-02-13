package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj2.command.SubsystemBase;

public class Climber extends SubsystemBase {
    private VictorSPX actuatorMotor;

    //for climber in a box
    private VictorSPX winchMotor1;
    private VictorSPX winchMotor2;

    public Climber(){
        //placeholder port numbers
        actuatorMotor = new WPI_VictorSPX(4);
        /*
        winchMotor1   = new WPI_VictorSPX(2);
        winchMotor2   = new WPI_VictorSPX(3);
        */
    }

    public void driveActuator(float value){
        actuatorMotor.set(ControlMode.PercentOutput, value);
    }
}
