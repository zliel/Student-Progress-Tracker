package com.personal.tracker;

import java.util.Scanner;

/**
 * This class executes the program, handling user input.
 */
public class Main {
  public static void main(String[] args) {
    // Initialize Scanner object for user input
    Scanner input = new Scanner( System.in );

    System.out.println("Hello! What would you like to do today? Enter here:  ");

    String userCmd = input.next();

    // Handle user input
    switch(userCmd) {
      case "help" -> help();
      case "add" -> add(input);
      case "list" -> list(input);
      case "delete" -> delete(input);
    }
    // Close the scanner when we're done
    input.close();
  }

  /** This method handles the "help" command, showing the user what possible commands there are */
  public static void help() {
    // Print out all available commands
    System.out.println("Possible commands: ");
    System.out.println("\tadd: Add a student, chapter, or completed chapter to the tracker.");
    System.out.println("\n\tlist: List all chapters.");
    System.out.println("\n\tdelete: Delete a student, chapter, or completed chapter from the tracker.");
    // main(new String[]{""}); //uncommenting this will make help() restart the program; it'd
    // need to be tested
  }

  /**
   * This method handles the "add" command, letting the user add chapters, students, or completed
   * chapters based on input.
   *
   * @param addScanner The Scanner object passed in by the main() method.
   */
  public static void add(Scanner addScanner) {
    String firstName;
    String lastName;
    String addInput;

    System.out.println("Sick, so what do you wanna add? ");
    addInput = addScanner.next();

    switch (addInput) {
      case "student" -> {
        // Get user input for the student's name
        System.out.println("What is the student's name? First please: ");
        firstName = addScanner.next();
        System.out.println("Last name please: ");
        lastName = addScanner.next();

        Add.addStudent(firstName, lastName);
      }

      case "chapter" -> {
        System.out.println("would you like to add a new chapter or a completed chapter? ");
        String answer = addScanner.next();
        if (answer.equalsIgnoreCase("new")) {
          Long chapterId;
          String chapterTitle;

          System.out.println("What is the chapter number? ");
          chapterId = addScanner.nextLong();

          System.out.println("What is the chapter name? ");
          addScanner.nextLine();
          chapterTitle = addScanner.nextLine();

          Add.addChapter(chapterId, chapterTitle);

        } else if (answer.equals("completed")) {
          // Get the student's first and last name
          System.out.println("What is the student's name? First name please: ");
          firstName = addScanner.next();
          System.out.println("Last name please: ");
          lastName = addScanner.next();

          System.out.println("Chapter number please: ");
          Long chapterId = addScanner.nextLong();
          Add.addCompletedChapter(firstName, lastName, chapterId);
        }
      }
    }
  }

  /**
   * This method handles the "delete" command, letting the user remove chapters from the database.
   *
   * @param deleteScanner The Scanner object passed in by the main() method.
   */
  public static void delete(Scanner deleteScanner) {
    // Get user input for what they want to delete (currently only "chapter" works)
    System.out.println("What would you like to delete? ");
    String deleteInput = deleteScanner.next();

    if (deleteInput.equals("chapter")) {
      // Get input for which chapter should be deleted
      System.out.println("Enter the chapter number of the chapter you'd like to delete: ");
      Long chapterId = deleteScanner.nextLong();

      Delete.deleteChapter(chapterId);
    } else if (deleteInput.equalsIgnoreCase("student")) {

      System.out.println("Which student would you like to delete? Enter their first name: ");
      String studentFirstName = deleteScanner.next();
      System.out.println("Enter their last name: ");
      String studentLastName = deleteScanner.next();
      Long studentId = Delete.getStudentId(studentFirstName, studentLastName);
      if (studentId == null) {
        System.out.println("Sorry, the student wasn't found.");
      } else {
        Delete.deleteStudent(studentId);
      }
    }
  }

  /** This method handles the "list" command, letting the user list either the students or
   * chapters in the database.
   *
   * @param listScanner The Scanner object passed in by the main() method
   */
  public static void list(Scanner listScanner) {
    // Get user input for what they would like to list
    System.out.println("Would you like to list the chapters or students?");
    String answer = listScanner.next();

    // Handle the response accordingly
    if (answer.equalsIgnoreCase("chapters")) {
      Query.listChapters();
    } else if (answer.equalsIgnoreCase("students")) {
      Query.listStudents();
    } else if (answer.equalsIgnoreCase("completed")) {
      System.out.println("Enter the student's first name: ");
      String firstName = listScanner.next();
      System.out.println("Enter the student's last name: ");
      String lastName = listScanner.next();
      Query.getCompletedChapters(firstName, lastName);
    }
  }
}