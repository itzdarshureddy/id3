package com.utd;

/**
 * Created by darshanubuntu on 1/25/15.
 * this class is to hold the child node temporarily while deciding  on the Information gain
 */
public class AttributeValue {
    int value=-1;
    int noOfOnes;
    int noOfZeros;

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getNoOfOnes() {
        return noOfOnes;
    }

    public void setNoOfOnes(int noOfOnes) {
        this.noOfOnes = noOfOnes;
    }

    public int getNoOfZeros() {
        return noOfZeros;
    }

    public void setNoOfZeros(int noOfZeros) {
        this.noOfZeros = noOfZeros;
    }
    //This method is used to calculate the entropy of the temporary node
    public double getEntropy(){
        double zeroPart =0.0;
        double onePart =0.0;
        if(noOfOnes>0){
            double oneP= noOfOnes;
            oneP /= (noOfZeros+noOfOnes);
            onePart = -oneP*(Math.log(oneP)/Math.log(2.0));
        }
        if(noOfZeros>0){
            double zeroP= noOfZeros;
            zeroP /= (noOfOnes+noOfZeros);
            zeroPart = -zeroP*(Math.log(zeroP)/Math.log(2.0));
        }
        return onePart+zeroPart;
    }
    public int getCount(){
        return  noOfOnes+noOfZeros;
    }
}
