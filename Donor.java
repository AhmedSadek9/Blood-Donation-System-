package com.project.ds;

import java.util.Objects;

public class Donor extends Person {
    private String donationReason;

    public Donor(int id, String name, String gender, int age, String bloodGroup, String donationReason) {
        super(id, name, bloodGroup, gender, age);
        this.donationReason = donationReason;
    }

    public String getDonationReason() {
        return donationReason;
    }

    public void setDonationReason(String donationReason) {
        this.donationReason = donationReason;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) return false;
        if (getClass() != obj.getClass()) return false;
        Donor donor = (Donor) obj;
        return Objects.equals(donationReason, donor.donationReason);
    }

    @Override
    public String toString() {
        return "Donor{" +
                "id=" + getId() +
                ", name='" + getName() + '\'' +
                ", gender='" + getGender() + '\'' +
                ", age=" + getAge() +
                ", bloodGroup='" + getBloodGroup() + '\'' +
                ", donationReason='" + donationReason + '\'' +
                '}';
    }

    @Override
    public String getDescription() {
        return super.getDescription() + ", Donation Reason=" + donationReason;
    }
}
