package com.utd;

import java.io.*;
import java.util.Scanner;

/**
 * Created by darshanubuntu on 1/22/15
 * This class contains main method which will be the starting point of the program
 * 2 file paths should be passed as arguments while running this program , test file and then train file
 * this class deals with reading the input files to form a 2 dimensional array and construct a Decision Tree object from it.
 * and then print the decision tree as well as read the test data and see how the decision tree evaluate the test data adn print the accuracy
 *
 *
 * .
 */

public class id3 {
    public static void main(String[] args) {

        int numberOfAttributes = 0;
        int[] attributesPossibleNoOfvalues = null;
        String[] attributesNames = null;

        if(args.length<2){
            System.out.println("Please provide training and test file names");
            return;
        }
        try {
            File file = new File(args[0]);
            //Reading the input train data
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String str = null;
            str = reader.readLine();
            String[] header = str.split("\\W+");
            numberOfAttributes = header.length/2;
            attributesPossibleNoOfvalues = new int[numberOfAttributes];
            attributesNames = new String[numberOfAttributes];
            //reading and storing the attribute names and possible values
            for( int i =0; i<header.length;i++){
                attributesNames[i/2] = header[i];
                i++;
                attributesPossibleNoOfvalues[i/2]= Integer.parseInt(header[i]);
            }
            int[][] trainingData = new int[numberOfAttributes+1][];

            String[] trainingDataString = new String[numberOfAttributes+1];
            //Constructing a two dimensional array with all the columns from the given input raw data
            while((str=reader.readLine())!=null && str.length()!=0) {
                int column = 0;
                Scanner in = new Scanner(str);
                while (in.hasNext()) {
                    if(trainingDataString[column]!=null && trainingDataString[column].length()>0){
                trainingDataString[column]+=" "+in.next();}
                    else{
                        trainingDataString[column]=in.next();
                    }
                column++;
                }
            }
            //constructing the 2 dimensional array of integers from strings
            for(int k=0;k<numberOfAttributes+1;k++){
                String[] raw = trainingDataString[k].split(" ");
                int l=0;
                trainingData[k] = new int[raw.length];
                while(l<raw.length){
                    trainingData[k][l]=Integer.parseInt(raw[l]);
                    l++;
                }
            }
            //Constructing the decision tree for the given input train data, attribute name s and possible values
            DecisionTree decisionTree = new DecisionTree(trainingData,attributesNames,attributesPossibleNoOfvalues);
            //testing the accuracy of train data with decision tree and printing the accuracy
            System.out.println("Accuracy on training set ("+trainingData[trainingData.length-1].length +" instances) :"+decisionTree.test(trainingData,attributesNames));

            //Reading the test data and building a 2 dimensional array to test decision tree against the test data
            File testFile = new File(args[1]);
            reader = new BufferedReader(new FileReader(testFile));

            str = reader.readLine();
            header = str.split("\\W+");
            numberOfAttributes = header.length/2;
            attributesPossibleNoOfvalues = new int[numberOfAttributes];
            attributesNames = new String[numberOfAttributes];
            for( int i =0; i<header.length;i++){
                attributesNames[i/2] = header[i];
                i++;
                attributesPossibleNoOfvalues[i/2]= Integer.parseInt(header[i]);
            }
            int[][] testData = new int[numberOfAttributes+1][];

            String[] testDataString = new String[numberOfAttributes+1];

            while((str=reader.readLine())!=null && str.length()!=0) {
                int column = 0;
                Scanner in = new Scanner(str);
                while (in.hasNext()) {
                    if(testDataString[column]!=null && testDataString[column].length()>0){
                        testDataString[column]+=" "+in.next();}
                    else{
                        testDataString[column]=in.next();
                    }
                    column++;
                }
            }
            for(int k=0;k<numberOfAttributes+1;k++){
                String[] raw = testDataString[k].split(" ");
                int l=0;
                testData[k] = new int[raw.length];
                while(l<raw.length){
                    testData[k][l]=Integer.parseInt(raw[l]);
                    l++;
                }
            }
            //Printing the accuracy for test data
            System.out.println("Accuracy on test set ("+testData[testData.length-1].length +" instances) :"+decisionTree.test(testData,attributesNames));

        } catch (FileNotFoundException e) {
            System.out.println("File name incorrect");
        } catch (IOException e) {
            System.out.println("Error reading from file");
        }


    }

}
