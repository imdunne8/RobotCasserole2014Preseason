/*----------------------------------------------------------------------------*/
/* Copyright (c) FIRST 2008. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package edu.wpi.first.wpilibj.templates;


import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.RobotDrive;
import edu.wpi.first.wpilibj.can.CANTimeoutException;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class MrRoboto extends IterativeRobot {       
    
    // All constants are placeholders, get real ones from electrical
    
    // CAN Jag Drivetrain IDs
    final int CANID_FRONT_LEFT = 13; 
    final int CANID_FRONT_RIGHT = 14;
    final int CANID_BACK_LEFT = 4;
    final int CANID_BACK_RIGHT = 2;
    
    // Transmission object channels
    final int XMISSION_SOLENOID1_ID = 1;
    final int XMISSION_SOLENOID2_ID = 2;
    final int COMPRESSOR_RELAY_ID = 8;
    final int PRESSURESW_ID = 14;
    
    // Joystick port
    final int JOY_PORT = 1;
    final int OFFJOY_PORT = 2;
    
    // button ID's for used buttons on joystick 
    final int SHIFT_DOWN_BUTTONID = 5; //left bumper
    final int SHIFT_UP_BUTTONID = 6; //right bumper
    final int ARCADE_DRIVE_BUTTONID = 4;
    final int TANK_DRIVE_BUTTONID = 1;
    
    // MagicTube object channels
    final int MAGIC_SPIKE_ID = 8;
    final int MAGIC_SOLENOID_ID = 0;
    
    //Conveyor channels
    final int CONVEYOR_SPIKE_ID = 0;
    final int SPINDLE_JAG_ID = 0;
    
    // CAN Jaguars
    CANJaguar frontLeftCAN;
    CANJaguar frontRightCAN;
    CANJaguar rearLeftCAN;
    CANJaguar rearRightCAN;
    
    // Joysticks
    Joystick mainJoy, offJoy;
    
    // Drivetrain
    RobotDrive driveTrain;
    
    // MagicTube
    MagicTube magicTube;
    
    //Conveyor
    Conveyor conveyor;
    
    // Transmission
    Solenoid xmissionSolenoid1, xmissionSolenoid2;
    Compressor xmissionCompressor;
    
    //Encoder
    Encoder encoder;
    
    // Booleans
    public boolean isTankDrive; // tank drive or arcade drive? 
    public boolean curGear; // first gear (false) or second (true)
    public boolean enableMagicTube = true;
    public boolean enableConveyor = false;
    
    
    public void robotInit() {     
         // Construct drivetrain motors
        try {
            this.frontLeftCAN = new CANJaguar(CANID_FRONT_LEFT);
            //Jaguar encoder left -- verify 360 parameter.
            this.frontLeftCAN.configEncoderCodesPerRev(360);
            this.frontRightCAN = new CANJaguar(CANID_FRONT_RIGHT);
            //Jaguar encoder right -- verify 360 parameter.
            this.frontRightCAN.configEncoderCodesPerRev(360);
            this.rearLeftCAN = new CANJaguar(CANID_BACK_LEFT);
            this.rearRightCAN = new CANJaguar(CANID_BACK_RIGHT);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        
        // Construct transmission objects
        this.xmissionSolenoid1 = new Solenoid(XMISSION_SOLENOID1_ID);
        this.xmissionSolenoid2 = new Solenoid(XMISSION_SOLENOID2_ID);
        this.xmissionCompressor = new Compressor(PRESSURESW_ID,COMPRESSOR_RELAY_ID);
        this.xmissionCompressor.start();
        
        // Construct RobotDrive w/ Jag motors
        this.driveTrain = new RobotDrive(frontLeftCAN,rearLeftCAN,frontRightCAN,rearRightCAN);
        
        // Construct Encoder
        //this.encoder = new Encoder(0,0);
        
        // Construct Joystick
        this.mainJoy = new Joystick(JOY_PORT);
        this.offJoy = new Joystick(OFFJOY_PORT);
        
        // Construct MagicTube for magical tube-ness if enabled!
        if(enableMagicTube == true) 
        {
            //this.magicTube = new MagicTube(MAGIC_SPIKE_ID, MAGIC_SOLENOID_ID);
        }
        
        // Construct Conveyor if enabled
        if(enableConveyor == true) {
            this.conveyor = new Conveyor(CONVEYOR_SPIKE_ID, SPINDLE_JAG_ID);
        }
        
        // init bools
        isTankDrive = true; //tank drive is default
        curGear = false; // start in 1st gear 
        
        // set solenoids
        xmissionSolenoid1.set(curGear);
        xmissionSolenoid2.set(curGear);
        
        driveTrain.setSafetyEnabled(false);
        driveTrain.setInvertedMotor(RobotDrive.MotorType.kFrontLeft, false);
        driveTrain.setInvertedMotor(RobotDrive.MotorType.kRearLeft, false);
        driveTrain.setInvertedMotor(RobotDrive.MotorType.kFrontRight, false);
        driveTrain.setInvertedMotor(RobotDrive.MotorType.kRearRight, false);
        
    }
    public void autonomousPeriodic() {
        
    }
    public void teleopPeriodic() { 
        
        // --- Begin Drive Train Code ---
        // Check if a gearshift button was pressed       
        // if get shift down button and currently in second gear
        if (mainJoy.getRawButton(SHIFT_DOWN_BUTTONID) && curGear == true) {   
            curGear = false; // change variable accordingly
            xmissionSolenoid1.set(curGear); // switch gears
            xmissionSolenoid2.set(curGear);
        }
        // if get shift up button and and currently in first gear
        if (mainJoy.getRawButton(SHIFT_UP_BUTTONID) && curGear == false) {
            curGear = true; //change var accordingly
            xmissionSolenoid1.set(curGear);
            xmissionSolenoid2.set(curGear);
        }                  
        // Check if arcade drive button was pressed
        if (mainJoy.getRawButton(ARCADE_DRIVE_BUTTONID) && isTankDrive) {                      
            isTankDrive = false; // switch to arcade
        }
        // Check if tank drive button was pressed
        if (mainJoy.getRawButton(TANK_DRIVE_BUTTONID) && !isTankDrive) {
            isTankDrive = true; // switch to tank
        }     
        // Drive it!
//        if (isTankDrive) {
//            driveTrain.tankDrive(mainJoy.getRawAxis(1), mainJoy.getRawAxis(3));
//        } else {
//            driveTrain.arcadeDrive(mainJoy);
//        }
        driveTrain.arcadeDrive(mainJoy);
        try {
            SmartDashboard.putNumber("Front Left Command", frontLeftCAN.getOutputVoltage());
            SmartDashboard.putNumber("Front Right Command", frontRightCAN.getOutputVoltage());
            SmartDashboard.putNumber("Rear Left Command", rearLeftCAN.getOutputVoltage());
            SmartDashboard.putNumber("Rear Right Command", rearRightCAN.getOutputVoltage());
            SmartDashboard.putBoolean("Pressure Switch", xmissionCompressor.getPressureSwitchValue());
                
            // --- End drive train code ---
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
}