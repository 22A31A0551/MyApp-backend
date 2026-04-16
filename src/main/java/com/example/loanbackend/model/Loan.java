package com.example.loanbackend.model;

import jakarta.persistence.*;

@Entity
@Table(name = "loans")
public class Loan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String phone;
    private String email;
    private String address;
    private String item;
    private String weight;
    private String amount;
    private String interest;
    private String date;

    // ✅ Always default to Active (VERY IMPORTANT)
    private String status;

    private String repaymentDate;
    private String amountPaid;

    // ✅ DEFAULT CONSTRUCTOR (IMPORTANT)
    public Loan() {
        this.status = "Active";
    }

    // 🔧 GETTERS & SETTERS

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // ✅ STATUS (SAFE HANDLING)
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        // 🔥 Prevent null override
        if (status == null || status.isEmpty()) {
            this.status = "Active";
        } else {
            this.status = status;
        }
    }

    // ✅ REPAYMENT
    public String getRepaymentDate() {
        return repaymentDate;
    }

    public void setRepaymentDate(String repaymentDate) {
        this.repaymentDate = repaymentDate;
    }

    public String getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(String amountPaid) {
        this.amountPaid = amountPaid;
    }
}