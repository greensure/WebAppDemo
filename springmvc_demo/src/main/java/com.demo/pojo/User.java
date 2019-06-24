package com.demo.pojo;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class User {
    private String name;
    private Integer age;
    // 多层级对象绑定
    private ContactInfo contactInfo;

    public ContactInfo getContactInfo() {
        return contactInfo;
    }

    public void setContactInfo(ContactInfo contactInfo) {
        this.contactInfo = contactInfo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public static void main(String[] args) {
        Set<User> u = new HashSet<User>();
        User u1 = new User();
        u1.setName("aa");
        User u2 = new User();
        u2.setName("aa");
        u.add(u1);
        u.add(u2);
        System.out.println("size:"+u.size());
        System.out.println(u);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(age, user.age);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }

    //    @Override
//    public String toString() {
//        return "User{" +
//                "name='" + name + '\'' +
//                ", age=" + age +
//                '}'+this.hashCode();
//    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", contactInfo=" + contactInfo +
                '}';
    }
}
