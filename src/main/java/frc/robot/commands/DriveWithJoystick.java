package frc.robot.commands;

import frc.robot.Control;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Climber;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveWithJoystick {

    public static class DoDrivetrain extends CommandBase {
        private final Drivetrain drivetrain;
    
        public DoDrivetrain(Drivetrain d){
            System.out.println("Do Drivetrain Constructed...");
            drivetrain = d;
            addRequirements(drivetrain);
        }
        
        @Override
        public void execute() {
            
            double turn = 0;
	        double power = 0;
            double creepRate = 0.3;

	        turn = -Control.getXboxCtrl().getLeftX();
	        power = Control.getXboxCtrl().getRightTriggerAxis() - Control.getXboxCtrl().getLeftTriggerAxis();
            
	        turn = inputTransform(turn, 0, 0.1);
	        power = inputTransform(power, 0.15, 0.03);
            
            if(Control.getXboxCtrl().getYButton()){ //If we're in creep mode
                turn *= creepRate;
                power *= creepRate;
            }
            if(Control.getPOV()== Control.POV.IntakePOV){
                power = -power; //Switch forwards and backwards.
            }
            power *= .3; //Intentionally limit ourselves.
            turn *= .3; // also limits turn power

            drivetrain.DrivePolar(power, turn);
            
        }

        @Override
        public void end(boolean interupted){
            drivetrain.Drive(0, 0);
        }

    }

    public static class DoClimber extends CommandBase {
        private final Climber climber;

        public DoClimber(Climber c){
            climber = c;
            addRequirements(climber);
        }

        @Override
        public void execute(){
            float power = 0.0f;
            int POV = Control.getXboxCtrl().getPOV();
            if (POV>=0){
                //if up on d-pad linear actuator power = 1
                if(POV>90 && POV<270){
                    power = -1.0f;
                } 
                //if down on the d-pad linear actuator power = -1
                else if(POV>270 || POV<90){
                    power = 1.0f;
                }
            }
            System.out.println(power);
            //reduce power to 15%
            power *= 0.15;
            climber.driveActuator(power);
        }
    }

    public static double inputTransform(
        double input,
        double minPowerOutput,
        double inputDeadZone,
        double inputChangePosition,
        double outputChangePosition
    ) {
        double output = 0;
        double correctedInput = (Math.abs(input) - inputDeadZone) / (1 - inputDeadZone);
        
        if (correctedInput <= 0){
            return 0;
        } else if (correctedInput <= inputChangePosition) {
            output = (correctedInput / inputChangePosition * (outputChangePosition - minPowerOutput)) + minPowerOutput;
        } else {
            output = (correctedInput - inputChangePosition)
                    / (1 - inputChangePosition)
                    * (1 - outputChangePosition)
                    + outputChangePosition;
        }

        return (input < 0) ? (output * -1.0) : output;
    }

    public static double inputTransform(
        double input,
        double minPowerOutput,
        double inputDeadZone
    ) {
        return DriveWithJoystick.inputTransform(
            input, 
            minPowerOutput, 
            inputDeadZone, 
            .75, 
            .5
        );
    }

    public static double powerRampup(
        double input,
        double outputInit
    ) {
        
        if ((Math.abs(input) < Math.abs(outputInit)) && ((input < 0 && outputInit < 0 ) || (input > 0 && outputInit > 0))){
            return input;
        } 

        return outputInit + (0.1 * ((input > 0) ? 1.0 : -1.0));

        //int sign = (input > 0) ? 1 : -1;
        //*outputVar += 0.1*sign;
    }

}
