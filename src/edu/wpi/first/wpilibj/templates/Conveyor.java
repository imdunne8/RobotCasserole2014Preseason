/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.CANJaguar;
import edu.wpi.first.wpilibj.Relay.Value;
import edu.wpi.first.wpilibj.can.CANTimeoutException;

/**
 *
 * @author FIRSTMentor
 */
public class Conveyor {
    //Single spike motor -- Controls conveyor
    Relay conveyorSpike;
    
    //Single normal motor -- Controls drop spindle
    CANJaguar spindleJag;
    
    /**
     * Initializes spike and jaguar
     * @param spikeID ID for spike motor that controls conveyor
     * @param spindleID ID for jaguar that controls spindle for drop(?)
     */
    public Conveyor(int spikeID, int spindleID)
    {   
        conveyorSpike = new Relay(spikeID);

        try {
            spindleJag = new CANJaguar(spindleID);
        } catch (CANTimeoutException ex) {
            ex.printStackTrace();
        }
    }
    
    public void setConveyor(int value) 
    {
        if(value == 1){
            conveyorSpike.set(Value.kForward);
        }
        else if(value == -1) {
            conveyorSpike.set(Value.kReverse);
        }
        else{
            conveyorSpike.set(Value.kOff);
        }
    }
}
