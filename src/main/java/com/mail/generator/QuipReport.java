package com.mail.generator;

/*
 * QuipReport
 */
public class QuipReport {
    
    private String title;
    private String date;
    private String tableHead;
    private String tableContent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTableHead() {
        return tableHead;
    }

    public void setTableHead(String tableHead) {
        this.tableHead = tableHead;
    }

    public String getTableContent() {
        return tableContent;
    }

    public void setTableContent(String tableContent) {
        this.tableContent = tableContent;
    }
}
