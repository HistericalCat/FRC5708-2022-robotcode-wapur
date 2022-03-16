package frc.robot.commands;

import frc.robot.Control;
import frc.robot.subsystems.Drivetrain;
import frc.robot.subsystems.Sensors;
import frc.robot.subsystems.Climber;

import frc.robot.ControlScheme;

import javax.swing.Action;

import edu.wpi.first.wpilibj2.command.CommandBase;

public class DriveWithJoystick {
    public static ControlScheme scheme = new ControlScheme();

    public static class DoDrivetrain extends CommandBase {
        private final Drivetrain drivetrain;
        private static Boolean switchesEnabled = true;
        
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
	        power = scheme.getDriveForward() - scheme.getDriveBackward();
            
	        turn = inputTransform(turn, 0, 0.1);
	        power = inputTransform(power, 0.15, 0.03);
            
            if(scheme.getCreepButton()){ //If we're in creep mode
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
        //private Sensors sensor = new Sensors();
        public DoClimber(Climber c){
            climber = c;
            addRequirements(climber);
            
        }

        enum Direction {
            up,
            down,
            none
        }
        
        private Direction winch1HitGoing = Direction.up;
        private Direction winch2HitGoing = Direction.up;

        @Override
        public void execute(){
            float actPower = 0.0f;
            if(switchedEnabled){
                if(climber.actuatorOut.get()){
                    System.out.println("Maximum extension");
                } else if(climber.actuatorIn.get()){
                    System.out.println("Minimum extension");
                }
            } 
            actPower = (float) scheme.getActPower();
            //System.out.println(power);
            //reduce actuator power to 20%
            actPower *= 0.70;
            climber.driveActuator(actPower);
            
            float winch1Power = (float)scheme.getWinch1Power();
            if(switchedEnabled){
                if(climber.winchLimit1.get()){
                    if(winchHitGoing1 == none) {
                        if(winch1Power > 0) winch1HitGoing = up;
                        if(winch1Power < 0) winch1HitGoing = down;
                    }
                } else {
                    winchHitGoing1 = none;
                }

                if(winch1HitGoing == up && winch1Power > 0){
                    winch1Power = 0;
                } else if(winch1HitGoing == down && winch1Power < 0){
                    winch1Power = 0;
                }
            }
            //reduce winch power to 50%
            winch1Power *= 0.80;
            climber.driveWinch1(winch1Power);
            
            
            float winch2Power = (float)scheme.getWinch2Power();
            if(switchedEnabled){
                if(climber.winchLimit2.get()){
                    if(winch2HitGoing == Direction.none) {
                        if(winch2Power > 0) winch2HitGoing = Direction.up;
                        if(winch2Power < 0) winch2HitGoing = Direction.down;
                    }
                } else {
                    winch2HitGoing= Direction.none;
                }

                if(winch2HitGoing == Direction.up && winch2Power > 0){
                    winch2Power = 0;
                } else if(winch2HitGoing == Direction.down && winch2Power < 0){
                    winch2Power = 0;
                }
            }
            winch2Power *=0.8;
            climber.driveWinch2(winch2Power);
            
            

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
