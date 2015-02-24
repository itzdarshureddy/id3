package com.utd;

/**
 * Created by darshanubuntu on 1/22/15.
 * This class constructs the decision tree by taking in the training data and possible no of values for each attribute and their names.
 * Also this uses recursive nature of Node class to construct the entire tree by just creating the root node.
 * This class also provides method to test the accuracy of any test or training data against decision tree
 */
public class DecisionTree {

    Node root;

    public DecisionTree(int[][] trainingData, String[] attributes, int[] attributesPossibleNoOfValues) {


        //uses recursive nature of Node class to construct the entire tree by just creating the root node.
        root = new Node(trainingData,-1,null,attributes,attributesPossibleNoOfValues,-1);



        root.print("");



    }
    //method to test the accuracy of any test or training data against decision tree,returns the accuracy as double
    public double test(int[][] trainingData, String[] attributesNames) {
        int correctResult=0;
        int incorrectResult=0;
        for (int i = 0; i < trainingData[trainingData.length - 1].length; i++) {
            Node node = root;
            while (node.children!=null){
                int currentAttribute=0;
                for (int j = 0; j < attributesNames.length; j++) {
                    if(attributesNames[j].equalsIgnoreCase(node.attributeName)){
                        currentAttribute=j;
                        break;
                    }
                }
                for (int j = 0; j < node.children.length; j++) {
                    if(node.children[j].branchValue==trainingData[currentAttribute][i]){
                        node=node.children[j];
                        break;
                    }
                }
            }
            if(node.label==trainingData[trainingData.length-1][i]){
                correctResult++;
            }else{
                incorrectResult++;
            }
        }
        double percentage=correctResult;
        percentage /= (correctResult+incorrectResult);
        return percentage*100;

    }
}
