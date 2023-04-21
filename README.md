# Mail Gen

Mail Gen is a utility that generates emails based on an HTML table. This can be helpful for tasks such as sending out status report emails.

## Features

* Generate emails based on an HTML table
* Customize the email template
* Send emails to multiple recipients
* Track email opens and clicks

## Instructions

1. To generate an email, first create an HTML table with the following columns:
    * Team member
    * Task
    * Completed
    * Remaining
    * Blocking
    * Status

2. Save the table as a .html file.

3. In the Mail Gen directory update the template according to your needs.

4. In my case, as input I use an html file exported from a quip spreadsheet. Put the template file, as well as the input file.

5. Update the paths in the main method.

6. Mail Gen will generate an email containing the information of the html table provided.

## Screenshots

- **Screenshot of a generated email:**

![Screenshot of a generated email](https://github.com/abdorah/daily-mail-generator/blob/c60cfafa020dc46dd8e700705235de16db5b7ebd/src/main/resources/assets/result.png)

## Contributing

We welcome contributions to Mail Gen. If you have any ideas for improvements, please open an issue or submit a pull request.
