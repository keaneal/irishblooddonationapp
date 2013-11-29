package com.hotmail.keanser.irishblooddonationapp.findclinic;

public class Clinics {

	private String clinicDetails;
	private String clinicLink;

	// Constructor for the clinics class
	public Clinics(String clinicDetails, String clinicLink) {
		super();
		this.clinicDetails = clinicDetails;
		this.clinicLink = clinicLink;

	}

	// Getter and setter methods for all the fields.
	public String getClinicDetails() {
		return clinicDetails;
	}

	public void setBloodType(String clinicDetails) {
		this.clinicDetails = clinicDetails;
	}

	public String getClinicLink() {
		return clinicLink;
	}

	public void setBloodLevel(String clinicLink) {
		this.clinicLink = clinicLink;
	}

}
