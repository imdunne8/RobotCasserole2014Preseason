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
import edu.wpi.first.wpilibj.Relay;

public class MrRoboto extends IterativeRobot {       
    
    // All constants are placeholders, get real ones from electrical
    
    // CAN Jag Drivetrain IDs
    final int CANID_FRONT_LEFT = 0; 
    final int CANID_FRONT_RIGHT = 0;
    final int CANID_BACK_LEFT = 0;
    final int CANID_BACK_RIGHT = 0;
    
    // Transmission object channels
    final int XMISSION_SOLENOID1_ID = 0;
    final int XMISSION_SOLENOID2_ID = 0;
    final int COMPRESSOR_RELAY_ID = 0;
    final int PRESSURESW_ID = 0;
    
    // Joystick port
    final int JOY_PORT = 0;
    final int OFFJOY_PORT = 0;
    
    // button ID's for used buttons on joystick 
    final int SHIFT_DOWN_BUTTONID = 0;
    final int SHIFT_UP_BUTTONID = 0;
    final int ARCADE_DRIVE_BUTTONID = 0;
    final int TANK_DRIVE_BUTTONID = 0;
    
    // MagicTube object channels
    final int PISTON_SPIKE_ID = 0;
    final int PISTON_SOLENOID_ID = 0;
    
    // CAN Jaguars
    CANJaguar frontLeftCAN;
    CANJaguar frontRightCAN;
    CANJaguar rearLeftCAN;
    CANJaguar rearRightCAN;
    
    // Joysticks
    Joystick mainJoy, offJoy;
    
    // Drivetrain
    RobotDrive driveTrain;
    
    // Transmission
    Solenoid xmissionSolenoid1, xmissionSolenoid2;
    Compressor xmissionCompressor;  
    
    // Booleans
    public boolean isTankDrive; // tank drive or arcade drive? 
    public boolean curGear; // first gear (false) or second (true)
    
    // Piston Spike Relay (Up/Down)
    Relay pistonSpikeRelay;
    
    // Piston Solenoid
    Solenoid pistonSolenoid;
    
    public void robotInit() {     
        // Construct drivetrain motors
        try {
            this.frontLeftCAN = new CANJaguar(CANID_FRONT_LEFT);
            this.frontRightCAN = new CANJaguar(CANID_FRONT_RIGHT);
            this.rearLeftCAN = new CANJaguar(CANID_BACK_LEFT);
            this.rearRightCAN = new CANJaguar(CANID_BACK_RIGHT);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
        
        // Construct transmission objects
        this.xmissionSolenoid1 = new Solenoid(XMISSION_SOLENOID1_ID);
        this.xmissionSolenoid2 = new Solenoid(XMISSION_SOLENOID2_ID);
        this.xmissionCompressor = new Compressor(PRESSURESW_ID,COMPRESSOR_RELAY_ID);
        
        // Construct RobotDrive w/ Jag motors
        this.driveTrain = new RobotDrive(frontLeftCAN,frontRightCAN,rearLeftCAN,rearRightCAN);
        
        // Construct Joystick
        this.mainJoy = new Joystick(JOY_PORT);
        this.offJoy = new Joystick(OFFJOY_PORT);
        
        // Construct MagicTube objects
        this.pistonSpikeRelay = new Relay(PISTON_SPIKE_ID);
 
        // init bools
        isTankDrive = true; //tank drive is default
        curGear = false; // start in 1st gear 
        
        // set solenoids
        xmissionSolenoid1.set(curGear);
        xmissionSolenoid2.set(curGear);
        
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
        if (isTankDrive) {
            driveTrain.tankDrive(mainJoy, offJoy);
        } else {
            driveTrain.arcadeDrive(mainJoy);
        }
            
        // --- End drive train code ---
    }
}