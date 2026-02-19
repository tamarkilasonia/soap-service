package ge.tbc.testautomation.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Employee {
    private Long employeeId;
    private String name;
    private String department;
    private String phone;
    private String address;
    private BigDecimal salary;
    private String email;
    private LocalDate birthDate;

    public Employee() {
    }

    public Employee(Long employeeId, String name, String department, String phone,
                    String address, BigDecimal salary, String email, LocalDate birthDate) {
        this.employeeId = employeeId;
        this.name = name;
        this.department = department;
        this.phone = phone;
        this.address = address;
        this.salary = salary;
        this.email = email;
        this.birthDate = birthDate;
    }


    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    @Override
    public String toString() {
        return "Employee{" +
                "employeeId=" + employeeId +
                ", name='" + name + '\'' +
                ", department='" + department + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", salary=" + salary +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                '}';
    }
}