package frc.robot.subsystems;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.robot.commands.DriveWithJoystick;
import frc.robot.Globals;
import frc.robot.Pair;

//Motor Controller imports
import com.ctre.phoenix.motorcontrol.can.*;
import com.ctre.phoenix.motorcontrol.can.TalonFX;
import com.ctre.phoenix.motorcontrol.ControlMode;

public class Drivetrain  extends SubsystemBase {
    /*
    private PWMVictorSPX FLMotor;
    private PWMVictorSPX FRMotor;
    private PWMVictorSPX BLMotor;
    private PWMVictorSPX BRMotor;
    */

    private TalonFX FLMotor;
    private TalonFX FRMotor;
    private TalonFX BLMotor;
    private TalonFX BRMotor;

    Encoder leftEncoder, rightEncoder;
    Gyro gyro;

    private boolean leftEncoderGood = false, rightEncoderGood = false;


    public Drivetrain(){    
        FLMotor = new WPI_TalonFX(10);
        FRMotor = new WPI_TalonFX(12);
        BLMotor = new WPI_TalonFX(9);
        BRMotor = new WPI_TalonFX(11);
/*
        System.out.println(FLMotor.getDescription());
        System.out.println(FRMotor.getDescription());
        System.out.println(BLMotor.getDescription());
        System.out.println(BRMotor.getDescription());
*/
        
        //System.out.println("FLMotor: " + FLMotor.isAlive());

        //setDefaultCommand(new DriveWithJoystick.DoDrivetrain(this));
    }

    @Override
    public void periodic(){
    }

    public void SetMotors(double left,double right){
        FLMotor.set(ControlMode.PercentOutput, left);
        FRMotor.set(ControlMode.PercentOutput, right);
        BLMotor.set(ControlMode.PercentOutput, left);
        BRMotor.set(ControlMode.PercentOutput, right);
        System.out.println("SetMotors " + left + "  "  + right);

        //System.out.println("FL: " + FLMotor.get() + " FR: " + FRMotor.get() + " BL: " + BLMotor.get() + " BR: " + BRMotor.get());
    }
    
    
    public double boundValue(double value, double bound) {
        if(value < (-1.0 * bound)) return -1.0 * bound;
        if(value > bound) return bound;
        return value;
    }

    public void Drive(double left, double right) {
        double bounded_left=boundValue(left,1.0);
	    double bounded_right=boundValue(right,1.0);
        SetMotors(bounded_left, -bounded_right);
	    
    }

    public void DrivePolar(double power, double turn) {
        double bounded_power = boundValue(power, 1.0);
        double bounded_turn = boundValue(turn, 1.0);
        double v = (1-Math.abs(bounded_turn)) * (bounded_power) + bounded_power;
        double w = (1-Math.abs(bounded_power)) * (bounded_turn) + bounded_turn;
        double rightMotorOutput = (v+w)/2;
        double leftMotorOutput = (v-w)/2;
    
        Drive(leftMotorOutput,rightMotorOutput);
    }

    public void checkEncoders() {
        if (!leftEncoderGood) leftEncoderGood = Math.abs(leftEncoder.getDistance()) > 0.1;
        if (!rightEncoderGood) rightEncoderGood = Math.abs(rightEncoder.getDistance()) > 0.1;    
    }

    public double GetGyroAngle() {
        return -1.0 * this.gyro.getAngle();
    }

    public double radian_t(double x) {
        return x / 180.0 * Math.PI;
    }

    public double degree_t(double x) {
        return x / Math.PI * 180.0;
    }

    // returns 2 element array
    public double[] GetEncoderDistance() {
        this.checkEncoders();
        Double leftDistance = leftEncoder.getDistance();
        Double rightDistance = rightEncoder.getDistance();
        
	    if (!leftEncoderGood && rightEncoderGood) {
		// emulate with gyro
		    return new double[]{ rightDistance - radian_t(degree_t(GetGyroAngle())) * Globals.ROBOT_WIDTH, rightDistance };
	    }
	    else if (!rightEncoderGood && leftEncoderGood) {
		    return new double[]{ leftDistance, leftDistance + radian_t(degree_t(GetGyroAngle())) * Globals.ROBOT_WIDTH };
	    }
	    else {
		    // both encoders, yay!
		    // or maybe no encoders
		    return new double[]{leftDistance, rightDistance};
	    }
    }

    // Returns a vector with the current motor powers of drivetrain in the following order: Front-Left, Front-Right, Back-Left, Back-Right
    /*public double[] getMotorPowers() {
        return new double[] { 
            this.FLMotor.get(),
            this.FRMotor.get(),
            this.BLMotor.get(),
            this.BRMotor.get()
        };
    }*/
}
