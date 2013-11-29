package com.hotmail.keanser.irishblooddonationapp.bloodlevels;

public class BloodLevels {
	
    private String bloodType;
    private String bloodLevel;

   
    // Constructor for the BloodLevels class
    public BloodLevels(String bloodType, String bloodLevel) {
            super();
            this.bloodType = bloodType;
            this.bloodLevel = bloodLevel;

    }
   
    // Getter and setter methods for all the fields.
    public String getBloodType() {
            return bloodType;
    }
    public void setBloodType(String bloodType) {
            this.bloodType = bloodType;
    }
    public String getBloodLevel() {
            return bloodLevel;
    }
    public void setBloodLevel(String bloodLevel) {
            this.bloodLevel = bloodLevel;
    }
	
}
