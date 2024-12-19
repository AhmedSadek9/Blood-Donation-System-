
package com.project.ds;
public class Person {
    protected int id;
    protected String name;
    protected String bloodGroup;
    protected String gender;
    protected int age;

    public Person(int id, String name, String bloodGroup, String gender, int age) {
        this.id = id;
        this.name = name;
        this.bloodGroup = bloodGroup;
        this.gender = gender;
        this.age = age;
    }

    public int getId() { return id; }
    public String getName() { return name; }
    public String getBloodGroup() { return bloodGroup; }
    public String getGender() { return gender; }
    public int getAge() { return age; }
    
    public void setId(int id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setBloodGroup(String bloodGroup) { this.bloodGroup = bloodGroup; }
    public void setGender(String gender) { this.gender = gender; }
    public void setAge(int age) { this.age = age; }

    @Override
    public String toString() {
        return "Person{" +
               "id=" + id +
               ", name='" + name + '\'' +
               ", bloodGroup='" + bloodGroup + '\'' +
               ", gender='" + gender + '\'' +
               ", age=" + age +
               '}';
    }
     public String getDescription() {
        return "Person [ID=" + id + ", Name=" + name + ", Age=" + age + ", Gender=" + gender + ", Blood Group=" + bloodGroup + "]";
    }
}
