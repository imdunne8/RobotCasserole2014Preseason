/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.wpi.first.wpilibj.templates;

import edu.wpi.first.wpilibj.Relay;
import edu.wpi.first.wpilibj.Solenoid;

/**
 *
 * @author mec
 */
public class MagicTube {
    // Piston Spike Relay (Up/Down)
    Relay pistonSpikeRelay;
    
    // Piston Solenoid
    Solenoid pistonSolenoid;
    
    public MagicTube(int PISTON_SPIKE_ID, int PISTON_SOLENOID_ID) {
        this.pistonSpikeRelay = new Relay(PISTON_SPIKE_ID);
        this.pistonSolenoid = new Solenoid(PISTON_SOLENOID_ID);
    }
}
