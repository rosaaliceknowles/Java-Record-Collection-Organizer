package com.rosamusic;

// needed for a constructor im writing that converts from LocalDate to Date 
// i didn't realized java had an inbuilt date class until i had already written this entire class
// my fault
import java.time.LocalDate;

// Class that stores a date in MM DD YYYY format
// used in the recordRelease class to store the release date of a release
public class Date implements Comparable<Date>
{
    enum Months
    {
        JAN (1),
        FEB (2),
        MAR (3),
        APR (4),
        MAY (5),
        JUN (6),
        JUL (7),
        AUG (8),
        SEP (9),
        OCT (10),
        NOV (11),
        DEC (12);

        public final int val; // ensures that each constant stored here stores the correct value
        // constructor
        Months(int x)
        {
            val = x;
        }
    }

    //---PRIVATE ACCESS---//
    private byte month, day;
    private int year;

    // checks if a year is a leap year
    // code is translated directly from an old C++ class I wrote, I have no idea what each line does ijbol
    private boolean checkYear(int x)
    {
        if (x % 100 == 0 && x % 400 == 0) return true;
        else if (x % 4 == 0) return true;
        else return false;
    }

    //---PUBLIC ACCESS---//

    public byte getMonth()
    {
        return month;
    }

    public byte getDay()
    {
        return day;
    }

    public int getYear()
    {
        return year;
    }

    // constructor w/ parameters
    public Date(byte paramMonth, byte paramDay, int paramYear)
    {
        if (paramYear > 0) year = paramYear;
        // checks to see if paramMonth is a valid value
        month = ((int)(paramMonth) <= Months.DEC.val && paramMonth > 0) ?
                paramMonth :
                0; // error! 

        // the comment above this section of code in the original c++ script said "I'm going to f-ing kill myself"
        // so idk anymore
        // this code is literal slop that i would feed to a pig
        if (paramDay > 0)
        {
            if (paramDay <= 28) day = paramDay;
            else if (checkYear(paramYear) && paramDay <= 29) day = paramDay;
            else if (paramDay <= 30 && paramMonth != Months.FEB.val) day = paramDay;
            else if (paramDay == 31 && ( (paramMonth % 2 == 1 && paramMonth < Months.AUG.val) || (paramMonth % 2 == 0 && paramMonth > Months.JUL.val) ) ) day = paramDay;
            else day = 0;
        }
        else day = 0;
    }

    // default constructor
    public Date()
    {
        month = 0;
        day = 0;
        year = 0;
    }

    // constructor with instance of LocalDate
    // basically, i didn't realize LocalDate was a thing
    // so I have to create a constructor that converts a LocalDate to an instance of the Date class 
    // in order to use the datePicker widget
    public Date(LocalDate ld)
    {
        // this type conversion is very sketch but whatever! 
        day = (byte)ld.getDayOfMonth();
        year = ld.getYear();

        switch (ld.getMonth())
        {
            case JANUARY:
                month = 1;
                break;
            case FEBRUARY:
                month = 2;
                break;
            case MARCH:
                month = 3;
                break;
            case APRIL:
                month = 4;
                break;
            case MAY:
                month = 5;
                break;
            case JUNE:
                month = 6;
                break;
            case JULY:
                month = 7;
                break;
            case AUGUST:
                month = 8;
                break;
            case SEPTEMBER:
                month = 9;
                break;
            case OCTOBER:
                month = 10;
                break;
            case NOVEMBER:
                month = 11;
                break;
            case DECEMBER:
                month = 12;
                break;
        }
    }

    // used for printing this object 
    // MM - DD - YYYY
    @Override 
    public String toString()
    {
        String temp = ""; 

        if (month == 0 && day == 0 && year == 0)
            temp = "Unknown Date";
        else if (month == 0 && day == 0)
            temp = Integer.toString(year);
        else if (day == 0)
            temp = Byte.toString(month) + ", " + Integer.toString(year);
        else
            temp = Byte.toString(month) + ", " + Byte.toString(day) + ", " + Integer.toString(year);
        

        return temp;
    }

    // returns -1 if less than
    // returns 0 if equal
    // returns 1 if greater than
    @Override 
    public int compareTo(Date otherDate)
    {
        // compare years
        if (this.year < otherDate.year) return -1;
        else if (this.year > otherDate.year) return 1;
        // same year, compare months
        else if (this.month < otherDate.month) return -1;
        else if (this.month > otherDate.month) return 1;
        // same month, compare years
        else if (this.day < otherDate.day) return -1;
        else if (this.day > otherDate.day) return 1;
        // everything is equal 
        return 0;
    }
}