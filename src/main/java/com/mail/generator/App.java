package com.mail.generator;

/**
 * App
 *
 */
public class App {

    HtmlUtils htmlUtils = new HtmlUtils();

    public static void main(String[] args) {
        HtmlUtils.writeMailToFile(System.getProperty("user.dir") + "/src/main/resources/Status.html", "",
                System.getProperty("user.dir") + "/src/main/resources/mail-temp.html",
                System.getProperty("user.dir") + "/src/main/resources/mail.html");
    }
}
