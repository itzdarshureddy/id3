package com.utd;

/**
 * Created by darshanubuntu on 1/23/15.
 * This class takes the training data as two dimensional matrix and the available attribute names and possible number of values for these attributes
 * and constructs the Object which will hold all information about a particular NOde. like entropy,no of examples of each class that belong to this node from training data
 * what is the class of this node in case if its leaf, if not leaf it will try to generate all its children recursively
 *
 * While constructing the child nodes it passes the copy of the training data with already used attribute removed and training data that is not applicable to this node
 * marked with class -1
 *
 */
public class Node {
    int branchValue;
    Node[] children;
    int noOfZeroClass;
    int noOfOneClass;
    double entropyH;
    String attributeName;
    int[] attributesPossibleNoOfValues;
    int label=-1;
    int globalLabel=-1;
    int[][] trainingData;
    String[] remainingAttributes;

    public Node(int[][] trainingData, int branchValue, String attributeName,String[] remainingAttributes, int[] attributesPossibleNoOfValues,int globalLabel) {
        this.branchValue = branchValue;
        //this.children = children;
        int initialOnes=0,initialZeros=0;
        for(int i=0;i<trainingData[(trainingData.length-1)].length;i++)
        {
            if(trainingData[trainingData.length-1][i]==0){
                initialZeros++;
            }else if(trainingData[trainingData.length-1][i]==1){
                initialOnes++;
            }
        }
        this.noOfZeroClass = initialZeros;
        this.noOfOneClass = initialOnes;
        double zeroPart =0.0;
        double onePart =0.0;
        if(noOfOneClass>0){
            double oneP= noOfOneClass;
            oneP /= (noOfZeroClass+noOfOneClass);
             onePart = -oneP*(Math.log(oneP)/Math.log(2.0));
        }
        if(noOfZeroClass>0){
            double zeroP= noOfZeroClass;
            zeroP /= (noOfZeroClass+noOfOneClass);
            zeroPart = -zeroP*(Math.log(zeroP)/Math.log(2.0));
        }
        //Assign the global label for the first time based on which class is found more in training data
        if(globalLabel<0){
            globalLabel=noOfOneClass>noOfZeroClass?1:0;
        }
        this.remainingAttributes = remainingAttributes;
        this.trainingData=trainingData;
        this.globalLabel=globalLabel;
        this.entropyH = zeroPart + onePart;
        this.attributeName = attributeName;
        this.attributesPossibleNoOfValues = attributesPossibleNoOfValues;
        //once entropy is calculated assign label for the node if its leaf or if there are no more attributes to split this current node, and generate child nodes for this node if its not leaf
        if(getEntropyH()==0){
            label=noOfOneClass>0?1:0;
        }else if(remainingAttributes==null || remainingAttributes.length==0){
            if(this.noOfZeroClass!=this.noOfOneClass){
                label=this.noOfOneClass>noOfZeroClass?1:0;
            }else{
                label=globalLabel;
            }
        }else{
            generateChildNodes();
        }

    }
    //This method after every non leaf node is created will be called to create child nodes for that node based on the possible Information Gains for different attributes
    private void generateChildNodes() {
        //create an array to store all the possible IG for all attributes available
            double[] informationGain=new double[remainingAttributes.length];
        // Calculate the information gains for each available attribute
        for(int i=0;i<remainingAttributes.length;i++){
            AttributeValue[] attributeValues = new AttributeValue[attributesPossibleNoOfValues[i]];

            for(int j=0;j<trainingData[i].length;j++){
                for (int k = 0; k <attributesPossibleNoOfValues[i]; k++) {
                    if(attributeValues[k]==null){
                        attributeValues[k]= new AttributeValue();
                    }
                    if(attributeValues[k].getValue()==-1 || attributeValues[k].getValue()==trainingData[i][j]){
                        if(attributeValues[k].getValue()==-1) {
                            attributeValues[k].setValue(trainingData[i][j]);
                        }
                        if(trainingData[trainingData.length-1][j]==0){
                            attributeValues[k].noOfZeros++;
                        }else if(trainingData[trainingData.length-1][j]==1){
                            attributeValues[k].noOfOnes++;
                        }
                        break;
                    }
                }
            }
            double entropy = 0.0;
            for (int j = 0; j < attributeValues.length; j++) {
                if(attributeValues[j]==null){
                    break;
                }
                double p=attributeValues[j].getCount();
                p /= (noOfOneClass+noOfZeroClass);
                entropy += p*attributeValues[j].getEntropy();
            }
            informationGain[i] = entropyH-entropy;
        }
        //find out for which attribute IG is highest
        double maxIG=0.0;
        int maxIgAttr=0;
        for (int i = 0; i < informationGain.length; i++) {
            if(informationGain[i]>maxIG){
                maxIG=informationGain[i];
                maxIgAttr=i;
            }
        }
        //Assign the name of the attribute with highest IG to this node
        attributeName=remainingAttributes[maxIgAttr];
        String visitedValues=" ";
        Node[] nodes =new Node[attributesPossibleNoOfValues[maxIgAttr]];
        for (int i = 0; i < attributesPossibleNoOfValues[maxIgAttr]; i++) {
            //Create copy of all the data that need to be passed to the child node of this node, by eliminating the already used attribute
            String[] newRemainingAttributes = new String[remainingAttributes.length-1];
            int[] newAttributesPossibleNoOfValues= new int[remainingAttributes.length-1];
            int[][] newTrainingData = new int[trainingData.length-1][];
            for (int j = 0; j < remainingAttributes.length; j++) {
                if(j==maxIgAttr) continue;
                if(j<maxIgAttr) {
                    newRemainingAttributes[j] = remainingAttributes[j];
                    newAttributesPossibleNoOfValues[j] = attributesPossibleNoOfValues[j];
                    newTrainingData[j] = trainingData[j].clone();
                }else{
                    newRemainingAttributes[j-1] = remainingAttributes[j];
                    newAttributesPossibleNoOfValues[j-1] = attributesPossibleNoOfValues[j];
                    newTrainingData[j-1] = trainingData[j].clone();
                }

            }
            newTrainingData[newTrainingData.length-1]=trainingData[trainingData.length-1].clone();
            int currentValue=-1;
            for (int j = 0; j < trainingData[maxIgAttr].length; j++) {
               if(currentValue==-1&& !visitedValues.contains(String.valueOf(trainingData[maxIgAttr][j]))){
                   currentValue = trainingData[maxIgAttr][j];
                   visitedValues+=trainingData[maxIgAttr][j];
               }
               if(currentValue != trainingData[maxIgAttr][j]){
                   newTrainingData[newTrainingData.length-1][j]=-1;
               }

            }
            //For each possible value of attribute create a new Node with the copy of the data created
            nodes[i]=new Node(newTrainingData,currentValue,null,newRemainingAttributes,newAttributesPossibleNoOfValues,globalLabel);
        }
        //assign this list of nodes as children of this current node
        children = nodes;

    }

    public boolean isLeaf(){
        return  children == null;
    }

    public boolean isPure(){
        return noOfOneClass==0||noOfZeroClass==0;
    }
    public int getBranchValue() {
        return branchValue;
    }

    public double getEntropyH() {
        return entropyH;
    }

    public void setEntropyH(double entropyH) {
        this.entropyH = entropyH;
    }

    public void setBranchValue(int branchValue) {
        this.branchValue = branchValue;
    }

    public Node[] getChildren() {
        return children;
    }

    public void setChildren(Node[] children) {
        this.children = children;
    }

    public int getNoOfZeroClass() {
        return noOfZeroClass;
    }

    public int getLabel() {
        return label;
    }

    public void setLabel(int label) {
        this.label = label;
    }

    public void setNoOfZeroClass(int noOfZeroClass) {
        this.noOfZeroClass = noOfZeroClass;
    }

    public int getNoOfOneClass() {
        return noOfOneClass;
    }

    public void setNoOfOneClass(int noOfOneClass) {
        this.noOfOneClass = noOfOneClass;
    }
    //This method will print the tree from this node, including all its children
    public void print(String str) {



        if(children!=null) {
            for (int i = 0; i < children.length; i++) {
                if(children[i].label>=0){
                    System.out.println(str + attributeName + " = " + children[i].branchValue + " : "+children[i].label);

                }else {
                    System.out.println(str + attributeName + " = " + children[i].branchValue + " :");

                children[i].print(str+"| ");}
            }
        }

    }
}
