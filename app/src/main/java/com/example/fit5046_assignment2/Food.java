package com.example.fit5046_assignment2;


import java.math.BigDecimal;

public class Food {

    private Integer foodid;
    private String foodname;
    private String foodcategory;
    private int foodcalamt;
    private String foodservunit;
    private BigDecimal foodservamt;
    private int footfat;


    public Food(Integer foodid, String foodname, String foodcategory, int foodcalamt, String foodservunit, BigDecimal foodservamt, int footfat) {
        this.foodid = foodid;
        this.foodname = foodname;
        this.foodcategory = foodcategory;
        this.foodcalamt = foodcalamt;
        this.foodservunit = foodservunit;
        this.foodservamt = foodservamt;
        this.footfat = footfat;
    }

    public Food()
    {

    }

    public Integer getFoodid() {
        return foodid;
    }

    public void setFoodid(Integer foodid) {
        this.foodid = foodid;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodcategory() {
        return foodcategory;
    }

    public void setFoodcategory(String foodcategory) {
        this.foodcategory = foodcategory;
    }

    public int getFoodcalamt() {
        return foodcalamt;
    }

    public void setFoodcalamt(int foodcalamt) {
        this.foodcalamt = foodcalamt;
    }

    public String getFoodservunit() {
        return foodservunit;
    }

    public void setFoodservunit(String foodservunit) {
        this.foodservunit = foodservunit;
    }

    public BigDecimal getFoodservamt() {
        return foodservamt;
    }

    public void setFoodservamt(BigDecimal foodservamt) {
        this.foodservamt = foodservamt;
    }

    public int getFootfat() {
        return footfat;
    }

    public void setFootfat(int footfat) {
        this.footfat = footfat;
    }

}
