/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import java.math.BigDecimal;
import java.sql.Array;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toMap;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.effect.Glow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Font;

/**
 * This is the method for the Chart creation
 * @author Grdan Andreas
 */
public class ChartData {

    /**
     * Create Line Chart
     * @param lineChartData
     * @param wipNanData 
     */
    public void chartDataLineChart(LineChart<Number, Number> lineChartData, ObservableList<WipNan> wipNanData) {
        // clear the old chart data
        lineChartData.getData().clear();
        lineChartData.layout();
        lineChartData.setAnimated(false);
        
        // create new XY chart series
        XYChart.Series<Number, Number> series = new XYChart.Series();
        
        // set range date first to 0
        int rds = 0;
        
        // define hash set of the final report date
        TreeSet<Integer> reportDateSet = new TreeSet<Integer>();
        
        try{
            // loop through the observable list to get the range of dates
            for(WipNan wns : wipNanData){
                // add the report date to the hash set
                reportDateSet.add(Integer.parseInt(wns.getReportDate()));
                // set name of basic type
                series.setName(wns.getLot());
            }
 
            // check if something is in the tree set
            if (!reportDateSet.isEmpty()){
                // get first date of range dates
                rds = reportDateSet.first();
            }
        
            // loop through the observable list
            for(WipNan wn : wipNanData){
                // add the report date to the hash set
                reportDateSet.add(Integer.parseInt(wn.getReportDate()));
                //Insertion in corresponding row  
                int yValue = Integer.parseInt(wn.getSEQ());
                int xValue = Integer.parseInt(wn.getReportDate());
                System.out.println("rd:" + wn.getReportDate() + ", owner:" + wn.getOwner());
                // set the data, we start with the first date by 0
                // we calculate the days between the first day and the other days
                // we use the formatter to get the right date format yyyyMMdd
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                // we get the first day
                LocalDate firstDate = LocalDate.parse(Integer.toString(rds), formatter);
                // we get the report date
                LocalDate reportDate = LocalDate.parse(wn.getReportDate(), formatter);
                // we calculate the days between
                int daysBetween = Period.between(firstDate, reportDate).getDays();
                // add data to series
                series.getData().add(new XYChart.Data(daysBetween, yValue));
            }
            lineChartData.getData().add(series);
            
            // loop through all data points and add tooltip value
            for (final Data<Number, Number> data : series.getData()) {
                // create new tooltip on hover
                Tooltip tooltip = new Tooltip();
                // set tooltip text to number formated with delimiters
                tooltip.setText(NumberFormat.getIntegerInstance().format(data.getYValue()));
                // add the tooltip to the node
                Tooltip.install(data.getNode(), tooltip);
            }
                        
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error in creating the data for line chart wip nan.");             
        }
    }
    
    /**
     * Create Bar Chart
     * @param barChartData
     * @param wipNanData 
     */
    public void chartDataBarChart(StackedBarChart<String, Number> barChartData, ObservableList<WipNan> wipNanData) {
        // clear the old chart data
        barChartData.getData().clear();
        barChartData.layout();
        barChartData.setAnimated(false);
        
        // set range date first to 0
        int rds = 0;
        String lotID = "";
        
        // define hash set of the final report date
        TreeSet<Integer> reportDateSet = new TreeSet<Integer>();
        
        // define hash set of owner
        Set<String> ownerSet = new TreeSet<String>();
        
        // loop through the observable list
        for(WipNan wn : wipNanData){
            // add the basic type to the hash set
            ownerSet.add(wn.getOwner());
            // add the report date to the hash set
            reportDateSet.add(Integer.parseInt(wn.getReportDate()));
            // get lot number
            lotID = wn.getLot();
        }
        
        // check if something is in the tree set
        if (!reportDateSet.isEmpty()){
            // get first date of range dates
            rds = reportDateSet.first();
        }
        
        // loop through the hash set of owner
        for (String ow : ownerSet){
            // create new XY chart series
            XYChart.Series<String, Number> series = new XYChart.Series();
            // set series name to basictype
            series.setName(lotID + "," + ow);
            
            // new filtered wip nan list based on owner
            // loop through the full range of report date hash set
            for (Integer repdat : reportDateSet){
            
                // new filtered wip nan list based on owner      
                List<WipNan> wipnanOwner = wipNanData.stream()
                        .filter(p -> p.getOwner().equals(ow))
                        .filter(p -> p.getReportDate().equals(Integer.toString(repdat)))
                        .collect(Collectors.<WipNan>toList());
        
                // create yValue and Step
                int yValue = 0;
                String step = "";
                // loop through the new filtered observable list
                for(WipNan wn : wipnanOwner){
                    //get the seq 
                    yValue = Integer.parseInt(wn.getSEQ());
                    step = wn.getStep();
                }

                // set the data, we start with the first date by 0
                // we calculate the days between the first day and the other days
                // we use the formatter to get the right date format yyyyMMdd
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                // we get the first day
                Date firstDate = stringToDate(Integer.toString(rds));
                LocalDate firstLocalDate = LocalDate.parse(Integer.toString(rds), formatter);
                // we get the report date
                Date reportDate = stringToDate(Integer.toString(repdat));
                LocalDate reportLocalDate = LocalDate.parse(Integer.toString(repdat), formatter);
                // we calculate the days between
                // get the time in milliseconds between
                long milliSecondsBetween = reportDate.getTime() - firstDate.getTime();
                long calcDaysBetween = ChronoUnit.DAYS.between(firstLocalDate, reportLocalDate);
                // calculate to days
                long daysBetween = TimeUnit.DAYS.convert(milliSecondsBetween, TimeUnit.MILLISECONDS);

                // check if we have a value to also draw a label
                if(yValue>0){
                    // add data to series
                    // call method to create XYChart data with label value
                    series.getData().add(createXYChartData(Long.toString(calcDaysBetween), yValue, step));
                }else{
                    // add data to series
                    series.getData().add(new XYChart.Data(Long.toString(calcDaysBetween), yValue));
                }
            }
            barChartData.getData().add(series);

            // loop through all data points and add tooltip value
            for (final Data<String, Number> data : series.getData()) {
                // create new tooltip on hover
                Tooltip tooltip = new Tooltip();
                // set tooltip text to number formated with delimiters
                tooltip.setText(NumberFormat.getIntegerInstance().format(data.getYValue()));
                // add the tooltip to the node
                Tooltip.install(data.getNode(), tooltip);
            }
        }
    }

    /**
     * method to return the predicate used in the list filter which equals the given basic type
     * @param basicType
     * @return Predicate
     */
    public static Predicate<WipNan> isBasicType(String basicType) {
        return p -> p.getBasicType().equals(basicType);
    }
    
    /**
     * method to return the predicate used in the list filter which equals the given seq
     * @param seq
     * @return Predicate
     */ 
    public static Predicate<WipNan> isSEQ(String seq) {
        return p -> p.getSEQ().equals(seq);
    }
    
    /**
     * method to return the predicate used in the list filter which equals the given percentage
     * @param percentage
     * @return Predicate
     */ 
    public static Predicate<WipNan> isPercentage(String percentage) {
        return p -> p.getPercentage().equals(percentage);
    }

    /**
     * method to return all wip nan objects out of the list which equals the given predicate
     * @param wipnan
     * @param predicate
     * @return List of WipNan
     */
    public static List<WipNan> filterWipNan (List<WipNan> wipnan, Predicate<WipNan> predicate) {
        return wipnan.stream().filter( predicate ).collect(Collectors.<WipNan>toList());
    }

    /**
     * Create bar chart
     * @param barChartData
     * @param wipNanData
     * @param wip2 
     */
    public void chartDataStackedBarChart(StackedBarChart<String, Number> barChartData, ObservableList<WipNan> wipNanData, Boolean wip2, Boolean wipValue) {
        // clear the old bar chart data
        barChartData.getData().clear();
        barChartData.layout();
        barChartData.setAnimated(false);
        
        // define hash set of basic types
        Set<String> basicTypeSet = new TreeSet<String>();
        
        // define hash set of the final seq
        Set<Integer> seqFullSet = new TreeSet<Integer>();
        
        // loop through the observable list
        for(WipNan wn : wipNanData){
            // add the basic type to the hash set
            basicTypeSet.add(wn.getBasicType());
            // add the seq to the hash set
            seqFullSet.add(Integer.parseInt(wn.getSEQ()));
        }
        
        // loop through the hash set of basic type
        for (String bt : basicTypeSet){            
            // create new XY chart series
            XYChart.Series<String, Number> series = new XYChart.Series();
            
            // new filtered wip nan list based on basic type
            // loop through the full range of seq from 0 to 161
            //for (int seq=0; seq<161; seq++){
            // loop through the hash set of seq
            for (Integer seq : seqFullSet){
                // new filtered wip nan list based on basic type and seq
                List<WipNan> wipnanBasicTypeSEQ = wipNanData.stream()
                        .filter(p -> p.getBasicType().equals(bt))
                        .filter(p -> p.getSEQ().equals(Integer.toString(seq)))
                        .collect(Collectors.<WipNan>toList());
                
                // create wip number int
                int wip = 0;
                // loop through the new filtered wip nan list
                for(WipNan wnbtseq : wipnanBasicTypeSEQ){
                    // check if checkbox for pcs is selected - wip2
                    if(wip2){
                        // cum all pcs
                        wip = wip + Integer.parseInt(wnbtseq.getWIP1());
                    }else{
                        // cum all wafers
                        wip = wip + Integer.parseInt(wnbtseq.getWIP2());
                    }
                }
                
                // set series name to basictype
                series.setName(bt);
                // add the datapoint as XYChart.Data
                // check first if there is some volume
                if(wip>0 && wipValue){
                    // call method to create XYChart data with label value
                    series.getData().add(createXYChartData(Integer.toString(seq), wip, bt));
                }else{
                    // create XYChart data
                    series.getData().add(new XYChart.Data(Integer.toString(seq), wip));
                }
            }
            barChartData.getData().add(series);
            
            // loop through all data points and add tooltip value
            for (final Data<String, Number> data : series.getData()) {
                // create new tooltip on hover
                Tooltip tooltip = new Tooltip();
                // set tooltip text to basic type, wip number formated with delimiters
                tooltip.setText(bt + ": " + NumberFormat.getIntegerInstance().format(data.getYValue()));
                // add the tooltip to the node
                Tooltip.install(data.getNode(), tooltip);
            }
        }
    }
    
    /**
     * Create XY Chart with label
     * @param country
     * @param value
     * @return XYChart.Data
     */
    private XYChart.Data createXYChartData(String seq, Number wip, String bt) {
        XYChart.Data data =  new XYChart.Data(seq, wip);
        
        // set label value to basic type, wip number formated with delimiters
        String text = bt + ": " + NumberFormat.getIntegerInstance().format(wip);
 
        StackPane node = new StackPane();
        Label label = new Label(text);
        label.setRotate(-90);
        Group group = new Group(label);
        StackPane.setAlignment(group, Pos.BOTTOM_CENTER);
        StackPane.setMargin(group, new Insets(0, 0, 5, 0));
        node.getChildren().add(group);
        data.setNode(node);
 
        return data;
    }
    
    /**
     * Convert date to string
     * we get the actual date and convert it to the used date as string
     * @return actual date as formated string
     */
    public String dateToString(int dateAdd) {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat("yyyyMMdd");

        // get the instance of calender
        Calendar cal = Calendar.getInstance();
        // get the date today and add the calculation
        cal.add(Calendar.DATE, dateAdd);

        // put the calculated date into a date object 
        Date calcDate = cal.getTime();
        
        // Using DateFormat format method we can create a string 
        // representation of a date with the defined format.
        String reportDate = df.format(calcDate);
        return reportDate;
    }
    
    /**
     * Convert string to date
     * we get the date as string and convert it to the used date format
     * @return formated date from string
     */
    public Date stringToDate(String dateString) {
        // Create an instance of SimpleDateFormat used for formatting 
        // the string representation of date (month/day/year)
        DateFormat df = new SimpleDateFormat("yyyyMMdd");

        try{
            // parse the string to date format
            Date date = df.parse(dateString);
            return date;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
}
