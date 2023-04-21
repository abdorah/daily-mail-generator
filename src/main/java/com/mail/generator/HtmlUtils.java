package com.mail.generator;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class HtmlUtils {

    private static List<String> parseTablecolumn(String htmlFile, int count) {

        Document html = null;

        try {
            html = Jsoup.parse(new File(htmlFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<String> content = new ArrayList<>();

        Optional.ofNullable(html).orElseThrow(IllegalArgumentException::new)
                .body().getElementsByTag("table").stream().findFirst().get()
                .getElementsByTag("tbody").stream().findFirst().get()
                .getElementsByTag("tr").stream()
                .map(elt -> elt.getElementsByTag("td").stream()
                        .skip(count).findFirst().get())
                .forEach(td -> {
                    if (td.getAllElements().stream().anyMatch(node -> node.tag().getName().equals("cli"))) {
                        content.add(td.getElementsByTag("cli").stream()
                                .map(cli -> cli.text())
                                .collect(Collectors.joining("\\n")));
                    } else {
                        content.add(td.text());
                    }
                });

        return content;
    }

    private static int parseDate(String htmlFile, String checkDate) {

        List<String> dates = parseTablecolumn(htmlFile, 1);

        int count = 0;
        for (int i = 0; i < dates.size(); i++) {
            if (dates.get(i).equals(checkDate)) {
                count = i;
                break;
            }
        }

        return count;
    }

    private static List<String> parseTasksTitle(String htmlFile, String checkDate) {

        List<String> titles = parseTablecolumn(htmlFile, 1).stream().skip(parseDate(htmlFile, checkDate))
                .map(elt -> keepBulletPoints(elt))
                .collect(Collectors.toList());
        return titles;
    }

    private static List<String> parseUsers(String htmlFile, String checkDate) {

        List<String> users = parseTablecolumn(htmlFile, 2).stream().skip(parseDate(htmlFile, checkDate))
                .map(elt -> keepBulletPoints(elt))
                .collect(Collectors.toList());
        return users;
    }

    private static List<String> parseTasksDescription(String htmlFile, String checkDate) {

        List<String> descriptions = parseTablecolumn(htmlFile, 3).stream().skip(parseDate(htmlFile, checkDate))
                .map(elt -> keepBulletPoints(elt))
                .collect(Collectors.toList());
        return descriptions;
    }

    private static List<String> parseTodo(String htmlFile, String checkDate) {

        List<String> todos = parseTablecolumn(htmlFile, 4).stream().skip(parseDate(htmlFile, checkDate))
                .map(elt -> keepBulletPoints(elt))
                .collect(Collectors.toList());
        return todos;
    }

    private static List<String> parseBlocking(String htmlFile, String checkDate) {

        List<String> done = parseTablecolumn(htmlFile, 5).stream().skip(parseDate(htmlFile, checkDate))
                .map(elt -> keepBulletPoints(elt))
                .collect(Collectors.toList());
        return done;
    }

    private static List<String> parseStatus(String htmlFile, String checkDate) {

        List<String> status = parseTablecolumn(htmlFile, 6).stream().skip(parseDate(htmlFile, checkDate))
                .map(elt -> keepBulletPoints(elt))
                .collect(Collectors.toList());
        return status;
    }

    private static String buildTable(String htmlFile, String checkDate) {

        Document document = Jsoup.parse("");
        Element table = document.createElement("table");
        Element thead = document.createElement("thead");
        table.appendChild(thead);
        Element row = document.createElement("tr");
        thead.appendChild(row);
        Element cell = document.createElement("th");
        String[] titles = { "Team members", "Tasks", "Completed", "Remaining", "Blocking", "Status" };
        for (String title : titles) {
            cell = document.createElement("th");
            cell.append(title);
            row.appendChild(cell);
        }
        thead.appendChild(row);
        Element tbody = document.createElement("tbody");
        List<String> tasks = parseTasksTitle(htmlFile, checkDate);
        List<String> users = parseUsers(htmlFile, checkDate);
        List<String> descriptions = parseTasksDescription(htmlFile, checkDate);
        List<String> blocking = parseBlocking(htmlFile, checkDate);
        List<String> todo = parseTodo(htmlFile, checkDate);
        List<String> status = parseStatus(htmlFile, checkDate);
        for (int i = 0; i < users.size(); i++) {
            if (i != 0 && users.get(i).isEmpty())
                break;
            row = document.createElement("tr");
            tbody.appendChild(row);
            cell = document.createElement("td");
            cell.append(users.get(i));
            row.appendChild(cell);
            cell = document.createElement("td");
            cell.append(tasks.get(i));
            row.appendChild(cell);
            cell = document.createElement("td");
            cell.append(descriptions.get(i));
            row.appendChild(cell);
            cell = document.createElement("td");
            cell.append(todo.get(i));
            row.appendChild(cell);
            cell = document.createElement("td");
            cell.append(blocking.get(i));
            row.appendChild(cell);
            cell = document.createElement("td");
            cell.append(status.get(i));
            row.appendChild(cell);
        }
        table.appendChild(thead);
        table.appendChild(tbody);
        document.appendChild(table);
        return document.outerHtml();
    }

    private static String keepBulletPoints(String content) {
        if (content.split("\\\\n").length == 1)
            return content;
        Document document = Jsoup.parse("");
        Element unorderdList = document.createElement("ul");
        for (String element : content.split("\\\\n")) {
            if (element.isEmpty())
            continue;
            unorderdList.appendElement("li").text(element);
        }
        return unorderdList.outerHtml();
    }

    private static String parseReport(QuipReport quipReport, String tempFile, String mailFile) {

        Document document = null;
        try {
            document = Jsoup.parse(new File(tempFile));
            document.body().getElementById("title").text(quipReport.getTitle());
            document.body().getElementById("introduction")
                    .text("This is the status of the refactoring for " + quipReport.getDate() + ":");
            document.body().getElementById("table").html(quipReport.getTableContent());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return document.outerHtml();
    }

    private static String buildMail(String htmlFile, String checkDate, String tempFile, String mailFile) {

        QuipReport quipReport = new QuipReport();

        if (checkDate.isEmpty()) {
            Date actualDate = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            checkDate = sdf.format(actualDate);
        }

        quipReport.setTitle("Status Update for " + checkDate);
        quipReport.setDate(checkDate);
        quipReport.setTableHead("Tasks");
        quipReport.setTableContent(buildTable(htmlFile, checkDate));

        return parseReport(quipReport, tempFile, mailFile);
    }

    public static void writeMailToFile(String htmlFile, String checkDate, String tempFile, String mailFile) {

        try (FileWriter writer = new FileWriter(new File(mailFile))) {
            writer.write(buildMail(htmlFile, checkDate, tempFile, mailFile));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
