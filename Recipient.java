package com.project.ds;

public class Recipient extends Person {
    private String recipientCase; 

    public Recipient(int id, String name, String bloodGroup, String gender, int age, String recipientCase) {
        super(id, name, bloodGroup, gender, age);
        this.recipientCase = recipientCase;
    }


    public String getRecipientCase() {
        return recipientCase;
    }

    public void setRecipientCase(String recipientCase) {
        this.recipientCase = recipientCase;
    }
    
@Override
public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null || getClass() != obj.getClass()) return false;
    if (!super.equals(obj)) return false; 

    Recipient recipient = (Recipient) obj;
    return recipientCase.equals(recipient.recipientCase); 
}

    @Override
    public String toString() {
        return "Recipient{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", bloodGroup='" + bloodGroup + '\'' +
               ", gender='" + gender + '\'' +
               ", age=" + age +
               ", recipientCase='" + recipientCase + '\'' +
               '}';
    }
       @Override
    public String getDescription() {
        return super.getDescription() + ", Recipient Case=" + recipientCase;
    }
}
