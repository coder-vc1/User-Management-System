package com.example.usermanagement.dto;

public class UserResponseDto {
    private Long id;
    private String firstName;
    private String lastName;
    private String ssn;
    private String email;
    private Integer age;
    private String role;
    private String phone;
    private String username;
    private String birthDate;
    private String gender;

    public UserResponseDto() {}

    public UserResponseDto(Long id, String firstName, String lastName, String ssn, String email, 
                          Integer age, String role, String phone, String username, String birthDate, String gender) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.ssn = ssn;
        this.email = email;
        this.age = age;
        this.role = role;
        this.phone = phone;
        this.username = username;
        this.birthDate = birthDate;
        this.gender = gender;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getSsn() { return ssn; }
    public void setSsn(String ssn) { this.ssn = ssn; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getAge() { return age; }
    public void setAge(Integer age) { this.age = age; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getBirthDate() { return birthDate; }
    public void setBirthDate(String birthDate) { this.birthDate = birthDate; }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = gender; }
}