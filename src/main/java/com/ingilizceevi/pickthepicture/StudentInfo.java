package com.ingilizceevi.pickthepicture;

public class StudentInfo {
    String studentName;
    Integer studentID;
    Integer studentTime;
    Integer studentClicks;
    Integer studentChapter;
    StudentInfo(Integer sID){
        this.studentID = sID;
    }
    StudentInfo(String sN, Integer sID){
        this.studentID = sID;
        this.studentName = sN;
    }


}
