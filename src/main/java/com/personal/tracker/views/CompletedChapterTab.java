package com.personal.tracker.views;

import com.personal.tracker.controller.Add;
import com.personal.tracker.controller.Delete;
import com.personal.tracker.controller.Query;
import com.personal.tracker.models.CompletedChapter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import java.sql.Date;
import java.time.LocalDate;

public class CompletedChapterTab {
  public static Tab createCompletedChaptersTab(TableView<CompletedChapter> completedChapters) {
    //Make input fields for each necessary piece of information for the database
    TextField studentFirstNameField = new TextField();
    studentFirstNameField.setPromptText("Student First Name");

    TextField studentLastNameField = new TextField();
    studentLastNameField.setPromptText("Student Last Name");

    TextField bookTitleField = new TextField();
    bookTitleField.setPromptText("Book Title");

    Spinner<Integer> chapterNumberSpinner = new Spinner<>(1, 99, 1);
    chapterNumberSpinner.setTooltip(new Tooltip("Chapter Number"));
    chapterNumberSpinner.setEditable(true);

    // Labels for testing action handlers on buttons
//    Label firstNameLabel = new Label();
//    Label lastNameLabel = new Label();
//    Label bookTitleLabel = new Label();
//    Label chapterNumberLabel = new Label();

    // Creating our submit button and giving it a style rule
    Button submitButton = new Button("Add Row");
    submitButton.getStyleClass().add("submit-button");

    // Handle when the button is clicked
    submitButton.setOnAction(new EventHandler<>() {
      @Override
      public void handle(ActionEvent event) {
        // Here we get the information from the input form fields
        LocalDate date = LocalDate.now();
        String studentFirstName = studentFirstNameField.getText();
        String studentLastName = studentLastNameField.getText();
        String bookTitle = bookTitleField.getText();
        long chapterNum = chapterNumberSpinner.getValue();

        // We get the chapter title from the Chapter table
        String chapterTitle = Query.getChapterTitle(chapterNum, bookTitle);

        // We get the student ID using the student's first and last name to search the database
        Long studentId = Delete.getStudentId(studentFirstName, studentLastName);

        // Labels to be removed later
//        firstNameLabel.setText("First Name: " + studentFirstNameField.getText());
//        lastNameLabel.setText("Last Name: " + studentLastNameField.getText());
//        chapterNumberLabel.setText("Chapter Number: " + chapterNumberSpinner.getValue());

        // Add the new chapter to our database
        Add.addCompletedChapter(studentFirstName, studentLastName, chapterNum, bookTitle);

        // Add the new chapter to the TableView (to the underlying ObservableList)
        completedChapters.getItems().add(new CompletedChapter(studentId, chapterNum, Date.valueOf(date), bookTitle, chapterTitle));
      }
    });

    // Creating a button to let the user delete things from the database
    Button deleteButton = new Button("Delete");
    deleteButton.getStyleClass().add("delete-button");

    // Adding delete functionality to our deleteButton
    deleteButton.setOnAction(e -> {
      // Get the selected row from the TableView
      CompletedChapter selectedCompletedChapter = completedChapters.getSelectionModel().getSelectedItem();

      // Remove the selected row from the TableView
      completedChapters.getItems().remove(selectedCompletedChapter);

      // Remove the completed chapter from the database
      Delete.deleteCompletedChapter(selectedCompletedChapter.getStudentId(), selectedCompletedChapter.getBookTitle(), selectedCompletedChapter.getChapterNumber());
    });

    // Create a VBox to store all of our elements in
    final VBox vbox = new VBox();
    vbox.setSpacing(5);
    vbox.setPadding(new Insets(10, 10, 0, 10));

    // Make a new GridPane for our user input form
    GridPane completedChapterInputForm = new GridPane();

    // Set the padding of our GridPane
    completedChapterInputForm.setPadding(new Insets(10, 10, 10, 10));

    // Set the Max and Min height of our GridPane
    completedChapterInputForm.setMaxSize(3840, 2160);
    completedChapterInputForm.setMinSize(800, 400);

    // Set the spacing between elements of the GridPane
    completedChapterInputForm.setHgap(5);
    completedChapterInputForm.setVgap(5);

    // Add contents to our GridPane
    completedChapterInputForm.add(studentFirstNameField, 0, 0);
    //completedChapterInputForm.add(firstNameLabel, 0, 1);
    completedChapterInputForm.add(studentLastNameField, 1, 0);
    //completedChapterInputForm.add(lastNameLabel, 1, 1);
    completedChapterInputForm.add(bookTitleField, 2, 0);
    //completedChapterInputForm.add(bookTitleLabel, 2, 1);
    completedChapterInputForm.add(chapterNumberSpinner, 3, 0);
    //completedChapterInputForm.add(chapterNumberLabel, 3, 1);
    completedChapterInputForm.add(submitButton, 4, 0);
    completedChapterInputForm.add(deleteButton, 5, 0);

    // Add the GridPane to our Vbox
    vbox.getChildren().addAll(completedChapters, completedChapterInputForm);

    // Create our new Tab
    Tab completedChaptersTab = new Tab("Completed Chapters");
    // Set the content of our Tab to what's in the VBox
    completedChaptersTab.setContent(vbox);

    // Return our newly created Tab
    return completedChaptersTab;
  }
}