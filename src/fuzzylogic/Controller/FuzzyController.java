package fuzzylogic.Controller;

import fuzzylogic.Model.FuzzySet;
import fuzzylogic.Model.FuzzyVariable;
import fuzzylogic.Model.TriangleFuzzySet;
import fuzzylogic.View.FuzzyFrame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class FuzzyController {
    private FuzzyFrame frame;
    private FuzzyVariable GRE;
    private FuzzyVariable GPA;
    private FuzzyVariable decision;
    private double greEvaluate[];
    private double gpaEvaluate[];
    private ArrayList<FuzzySet> fuzzySets;
    private ArrayList<FuzzySet> fuzzySets2;
    
    public static void main(String[] args) {
        FuzzyController t = new FuzzyController();
      
    }

    public FuzzyController() {
        frame = new FuzzyFrame();
        GRE = new FuzzyVariable();
        GPA = new FuzzyVariable();
        decision = new FuzzyVariable();
        initialiazeVariable();
        initializeListener();
        frame.setVisible(true);
    }
    
    
    
    private void initialiazeVariable(){
        fuzzySets = new ArrayList<>();
        
        TriangleFuzzySet greLow = new TriangleFuzzySet(Double.NEGATIVE_INFINITY, 1200, 800, 1);
        TriangleFuzzySet greMed = new TriangleFuzzySet(800, 1800, 1);
        TriangleFuzzySet greHigh= new TriangleFuzzySet(1200, Double.POSITIVE_INFINITY, 1800, 1);
        
        GRE.add(greLow);
        GRE.add(greMed);
        GRE.add(greHigh);
        
        TriangleFuzzySet gpaLow = new TriangleFuzzySet(Double.NEGATIVE_INFINITY, 3.0, 2.2, 1);
        TriangleFuzzySet gpaMed = new TriangleFuzzySet(2.2, 3.8, 1);
        TriangleFuzzySet gpaHigh= new TriangleFuzzySet(3.0, Double.POSITIVE_INFINITY, 3.8, 1);
        
        GPA.add(gpaLow);
        GPA.add(gpaMed);
        GPA.add(gpaHigh);
        
        fuzzySets2 = new ArrayList<>();
        fuzzySets2.add(new FuzzySet("Poor", 0.0,0.6));
        fuzzySets2.add(new FuzzySet("Aferage",0.0 ,0.7));
        fuzzySets2.add(new FuzzySet("Good", 0.0,0.8));
        fuzzySets2.add(new FuzzySet("Very good", 0.0, 0.9));
        fuzzySets2.add(new FuzzySet("Excelent", 0.0,1.0));
        
    }
    
    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }
    
    public void initializeListener(){
        frame.addCentroidButtonListener(new centroidButtonPress());
        frame.addMaxButtonListener(new maxButtonPress());
    }
    
    public void gpaEvaluatePrint(){
        frame.appendCustomText("GPA Low = "+round(gpaEvaluate[0], 2)+"\n");
        frame.appendCustomText("GPA Med = "+round(gpaEvaluate[1], 2)+"\n");
        frame.appendCustomText("GPA High = "+round(gpaEvaluate[2], 2)+"\n");
    }
    
    public void greEvaluatePrint(){
        frame.appendCustomText("GRE Low = "+round(greEvaluate[0], 2)+"\n");
        frame.appendCustomText("GRE Med = "+round(greEvaluate[1], 2)+"\n");
        frame.appendCustomText("GRE High = "+round(greEvaluate[2], 2)+"\n");
    }
    
    public void andOperatorRules(){
        fuzzySets.clear();
        if(gpaEvaluate[2] > 0){
            
            if(greEvaluate[2]>0){
                fuzzySets.add(new FuzzySet("Excelent", Double.min(greEvaluate[2], gpaEvaluate[2]),1.0));
            }
            if(greEvaluate[1] > 0){
                fuzzySets.add(new FuzzySet("Very good", Double.min(greEvaluate[1], gpaEvaluate[2]), 0.9));
            }
            if(greEvaluate[0] > 0){
                fuzzySets.add(new FuzzySet("Aferage", Double.min(greEvaluate[0], gpaEvaluate[2]) ,0.7));
            }
            
        }
        if(gpaEvaluate[1] > 0){
            
            if(greEvaluate[2]>0){
                fuzzySets.add(new FuzzySet("Good", Double.min(greEvaluate[2], gpaEvaluate[1]) ,0.8));
            }
            if(greEvaluate[1] > 0){
                fuzzySets.add(new FuzzySet("Good", Double.min(greEvaluate[1], gpaEvaluate[1]), 0.8));
            }
            if(greEvaluate[0] > 0){
                fuzzySets.add(new FuzzySet("Poor", Double.min(greEvaluate[0], gpaEvaluate[1]) ,0.6));
            }
            
        }
        if(gpaEvaluate[0] > 0){
            
            if(greEvaluate[2]>0){
                fuzzySets.add(new FuzzySet("Aferage", Double.min(greEvaluate[2], gpaEvaluate[0]) ,0.7));
            }
            if(greEvaluate[1] > 0){
                fuzzySets.add(new FuzzySet("Poor", Double.min(greEvaluate[1], gpaEvaluate[0]),0.6));
            }
            if(greEvaluate[0] > 0){
                fuzzySets.add(new FuzzySet("Poor", Double.min(greEvaluate[0], gpaEvaluate[0]), 0.6));
            }
            
        }
    }
    
    public void centroidDefuzz(){
        frame.appendCustomText("\n\nMetode Centroids\n");
        double result1 = 0;
        double result2 = 0;
        for (int i=0 ; i < fuzzySets.size() ; i++) {
            result1 += (fuzzySets.get(i).value * fuzzySets.get(i).index);
            result2 += fuzzySets.get(i).value;
        }
         
        double result = result1/result2;
        frame.appendCustomText("Crisp decision index = " + result+"\n");
        frame.appendCustomText("Fuzzy decision index :\n");
        for (int i=0 ; i < fuzzySets2.size() ; i++) {
            
            boolean found = false;
            
            if(Double.compare(result,fuzzySets2.get(i).index) == 0){
                frame.appendCustomText(fuzzySets2.get(i).name +" = "+ "100%" + "\n");
                found = true;
            }
            
            if(result > fuzzySets2.get(i).index && result <= fuzzySets2.get(i).index+0.1){
                double percentage = Math.abs((result - (fuzzySets2.get(i).index+0.1))/0.1);
                percentage = round(percentage, 2);
                frame.appendCustomText(fuzzySets2.get(i).name +" = "+ percentage*100 + "%\n");
                found = true;
            }
            
            if(result < fuzzySets2.get(i+1).index && result >= (fuzzySets2.get(i+1).index)-0.1){
                double percentage = Math.abs(((fuzzySets2.get(i+1).index-0.1) - result)/0.1);
                percentage = round(percentage, 2);
                frame.appendCustomText(fuzzySets2.get(i+1).name +" = "+ percentage*100 + "%\n");
                found = true;
            }
            
            if(found)
                return;
        }
        
        
    }
    
    public  void maxDefuzz(){
        Double max = 0.0;
        int index =0 ;
        for (int i=0 ; i < fuzzySets.size() ; i++) {
            if(Double.compare(fuzzySets.get(i).value , max) > 0){
                max = fuzzySets.get(i).value;
                index = i;
            }
        }
        frame.appendCustomText("\n\nMetode max\n");
        frame.appendCustomText("Crisp decision index = " + round(fuzzySets.get(index).value, 2) +"\n");
        frame.appendCustomText("Fuzzy decision index = "+ fuzzySets.get(index).name +"\n");
    }
    
    private class centroidButtonPress implements ActionListener{
        @Override
        public void actionPerformed(ActionEvent e) {
            double gpa = frame.getGpaValue();
            double gre = frame.getGreValue();
            
            greEvaluate = GRE.evaluate(gre);
            gpaEvaluate = GPA.evaluate(gpa);
            
            //gpaEvaluatePrint();
            //greEvaluatePrint();
            
            andOperatorRules();
            centroidDefuzz();
        }
        
    }
    
    private class maxButtonPress implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            double gpa = frame.getGpaValue();
            double gre = frame.getGreValue();
            
            greEvaluate = GRE.evaluate(gre);
            gpaEvaluate = GPA.evaluate(gpa);
            
            
            andOperatorRules();
            maxDefuzz();
        }
        
    }
}
