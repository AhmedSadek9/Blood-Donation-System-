
package com.project.ds;


import java.util.Date;

public class BloodDonation {
    private int id;
    private int donorId;
    private int recipientId;
    private Date donationDate;

    public BloodDonation(int id, int donorId, int recipientId, Date donationDate) {
        this.id = id;
        this.donorId = donorId;
        this.recipientId = recipientId;
        this.donationDate = donationDate;
    }


    public int getId() {
        return id;
    }

    public int getDonorId() {
        return donorId;
    }

    public int getRecipientId() {
        return recipientId;
    }

    public Date getDonationDate() {
        return donationDate;
    }


    public void setId(int id) {
        this.id = id;
    }

    public void setDonorId(int donorId) {
        this.donorId = donorId;
    }

    public void setRecipientId(int recipientId) {
        this.recipientId = recipientId;
    }

    public void setDonationDate(Date donationDate) {
        this.donationDate = donationDate;
    }
      @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        BloodDonation that = (BloodDonation) obj;
        return id == that.id && donorId == that.donorId && 
               recipientId == that.recipientId && donationDate.equals(that.donationDate);
    }

    @Override
    public String toString() {
        return "BloodDonation{" +
               "id=" + id +
               ", donorId=" + donorId +
               ", recipientId=" + recipientId +
               ", donationDate=" + donationDate +
               '}';
    }
}

