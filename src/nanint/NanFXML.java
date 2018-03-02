/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nanint;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;

/**
 * This is the method for the whole GUI creation
 * @author Grdan Andreas
 */
public class NanFXML extends Application implements Initializable {
    
    private static final Logger logger = ApplicationLogger.getInstance();
    
    // Version Number
    String ProgrammVersion = "Version 1.10 with Lot Data";
    
    // SQL select query's
    String sqlShipmentSelectTSMC = "SELECT tsmc_to_nan.* FROM tsmc_to_nan LEFT JOIN wip_nan ON tsmc_to_nan.Lot = wip_nan.Lot WHERE wip_nan.Lot ISNULL;";
    String sqlShipmentSelectGF = "SELECT gf_to_nan.* FROM gf_to_nan LEFT JOIN wip_nan ON gf_to_nan.CustomerLotId = wip_nan.Lot WHERE wip_nan.Lot ISNULL;";
    String sqlLotProdSelect = "SELECT * FROM wip_nan WHERE Lot LIKE '%%' AND Owner NOT LIKE 'PROD' GROUP BY Lot;";
    String sqlShipmentSelectUMCI = "SELECT umci_to_nan.* FROM umci_to_nan LEFT JOIN wip_nan ON umci_to_nan.CUSTOMER_LOT = wip_nan.Lot WHERE wip_nan.Lot ISNULL;";
    String sqlLotSelect = "SELECT * FROM wip_nan;";
    String sqlLotDataSelect = "SELECT * FROM lot_data;";
    String sqlAllLotsWIP = "SELECT DISTINCT Lot FROM wip_nan ORDER BY Lot;";
    String sqlWIPActual = "SELECT * FROM wip_nan WHERE ReportDate LIKE '" + dateToString(0) + "';";
    String sqlWIPChartActual = "SELECT * FROM wip_nan WHERE ReportDate LIKE '" + dateToString(0) + "';";
    String sqlWIPLineChartActual = "SELECT * FROM wip_nan WHERE Lot LIKE '';";
    String sqlNewestDateWipNanSelect = "SELECT MAX(ReportDate) AS ReportDate FROM wip_nan;";
    
    /**
     * Define all FXML GUI items
     */
    // Menu Bar
    @FXML
    private MenuBar MenuBar;
    @FXML
    private Label ProgrammVersionLabel;
    @FXML
    private void menuImportData(ActionEvent event) {
        System.out.println("Menu Button for data import pressed -----------------------------");
        // we call the update or import method
        // we run it in the background
        backroundUpdateTask();
    }
    @FXML
    private void menuClearDatabase(ActionEvent event) {
        System.out.println("Menu Button for database clean data pressed -----------------------------");
        // show alert dialog
        Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to delete all data from Database?", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.showAndWait();
        
        // we check the result of the alert dialog
        if (alert.getResult() == ButtonType.YES) {
            // create instance of database
            DataBaseSQLite db = new DataBaseSQLite();
            // run the database cleanup
            // here we cleanup the whole database and create new clean tables
            db.runCreateNewDatabase();
            
            // show confirmation dialog
            Alert alert2 = new Alert(AlertType.INFORMATION, "Database cleaned!", ButtonType.OK);
            alert2.setHeaderText(null);
            alert2.showAndWait();

            // update the table views
            buildTableLotData(sqlLotDataSelect);
            buildTableWipNan(sqlLotSelect);
            buildTableGFtoNan(sqlShipmentSelectGF);
            buildTableTSMCtoNan(sqlShipmentSelectTSMC);
        }
    }
    @FXML
    private void menuCheckDatabase(ActionEvent event) {
        System.out.println("Menu Button for database structure check pressed -----------------------------");
        // show alert dialog
        Alert alert = new Alert(AlertType.CONFIRMATION, "Do you want to check and update the structure of the Database? No Data will be deleted.", ButtonType.YES, ButtonType.NO);
        alert.setHeaderText(null);
        alert.showAndWait();
        
        // we check the result of the alert dialog
        if (alert.getResult() == ButtonType.YES) {
            // create instance of database
            DataBaseSQLite db = new DataBaseSQLite();
            // run the database check
            // here we check the tables from the database and add columns if new needed
            // no data are deleted, only the table strucutre is checked
            db.runCheckDatabaseTables();
            
            // show confirmation dialog
            Alert alert2 = new Alert(AlertType.INFORMATION, "Database checked and updated!", ButtonType.OK);
            alert2.setHeaderText(null);
            alert2.showAndWait();
        }
    }
    
    // Lot Data tab with table view
    // Filter Area
    @FXML
    private ComboBox<String> ComboBoxValueLotData;  
    @FXML
    private TextField TextFieldValueLotData;
    @FXML
    private ComboBox<String> ComboBox2ValueLotData;  
    @FXML
    private TextField TextField2ValueLotData;
    @FXML
    private ComboBox<String> ComboBox3ValueLotData;  
    @FXML
    private TextField TextField3ValueLotData;
    @FXML
    protected void filterButtonLotData() {
        // define the sylQuery based on the selected values
        System.out.println("Filter Button for data filter LOT DATA pressed -----------------------------");
        // get the sql query from the combo boxes etc
        String sqlQuery = getLotDatafilterButtonQuery();
        // call the filter method
        filterLotData(sqlQuery);
    }
    @FXML
    protected void LotDataExcelExportButton() {
        System.out.println("Excel Export Button for LOT DATA pressed -----------------------------");
        // file chooser dialog
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Save Excel File");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            ExcelExport xlsExport = new ExcelExport();
            // export the observalbe list to excel
            xlsExport.reportLotDataExcelExport(lotData, file);
        }
    }
    // Lot Data Table
    @FXML
    private TableView<LotData> lotDataTable;
    @FXML
    private TableColumn<LotData, String> LotColLotData;
    @FXML
    private TableColumn<LotData, String> ProductColLotData;
    @FXML
    private TableColumn<LotData, String> BasicTypeColLotData;
    @FXML
    private TableColumn<LotData, String> CreationDateColLotData;
    @FXML
    private TableColumn<LotData, String> FirstStepDateColLotData;
    @FXML
    private TableColumn<LotData, String> LastStepDateColLotData;
    @FXML
    private TableColumn<LotData, String> LastDateColLotData;
    
    // Wip Nan tab with table view
    // Filter Area
    @FXML
    private ComboBox<String> ComboBoxValue;  
    @FXML
    private TextField TextFieldValue;
    @FXML
    private ComboBox<String> ComboBox2Value;  
    @FXML
    private TextField TextField2Value;
    @FXML
    private ComboBox<String> ComboBox3Value;  
    @FXML
    private TextField TextField3Value;
    @FXML
    protected void filterButton() {
        // define the sylQuery based on the selected values
        System.out.println("Filter Button for data filter WIP NAN pressed -----------------------------");
        // get the sql query from the combo boxes etc
        String sqlQuery = getWipNanfilterButtonQuery();
        // call the filter method
        filterWipNanData(sqlQuery);
    }
    @FXML
    private CheckBox CheckBoxNotProd;
    @FXML
    protected void xlsExportButton() {
        System.out.println("Excel Export Button for WIP NAN pressed -----------------------------");
        // file chooser dialog
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Save Excel File");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            ExcelExport xlsExport = new ExcelExport();
            // export the observalbe list to excel
            xlsExport.reportWipNanExcelExport(wipNanData, file);
        }
    }

    // Wip Nan Table
    @FXML
    private TableView<WipNan> wipNanTable;
    @FXML
    private TableColumn<WipNan, String> ReportDateCol;
    @FXML
    private TableColumn<WipNan, String> LotCol;
    @FXML
    private TableColumn<WipNan, String> OwnerCol;
    @FXML
    private TableColumn<WipNan, String> ProductCol;
    @FXML
    private TableColumn<WipNan, String> HoldFlagCol;
    @FXML
    private TableColumn<WipNan, String> WIP1Col;
    @FXML
    private TableColumn<WipNan, String> WIP2Col;
    @FXML
    private TableColumn<WipNan, String> ShrinkCol;
    @FXML
    private TableColumn<WipNan, String> FE_SITECol;
    @FXML
    private TableColumn<WipNan, String> BasicTypeCol;
    @FXML
    private TableColumn<WipNan, String> PackageCol;
    @FXML
    private TableColumn<WipNan, String> StepCol;
    @FXML
    private TableColumn<WipNan, String> WorkCenterCol;
    @FXML
    private TableColumn<WipNan, String> SEQCol;
    @FXML
    private TableColumn<WipNan, String> PercentageCol;
    @FXML
    private TableColumn<WipNan, String> ReportFileNameCol;
    
    // GF to Nan Filter Area
    @FXML
    private ComboBox<String> ComboBoxGFtoNanValue;  
    @FXML
    private TextField TextFieldGFtoNanValue;
    @FXML
    private ComboBox<String> ComboBoxGFtoNan2Value;  
    @FXML
    private TextField TextFieldGFtoNan2Value;
    @FXML
    private ComboBox<String> ComboBoxGFtoNan3Value;  
    @FXML
    private TextField TextFieldGFtoNan3Value;
    @FXML
    private CheckBox CheckBoxGFtoNanOpenShipment;
    @FXML
    protected void filterGFtoNanButton() {
        // define the sylQuery based on the selected values
        System.out.println("Filter Button for data filter GF to NAN pressed -----------------------------"); 
        // get the sql query from the combo boxes etc
        String sqlQuery = getGFtoNanfilterButtonQuery();
        // call the filter method
        buildTableGFtoNan(sqlQuery);
    }
    @FXML
    protected void GFtoNanxlsExportButton() {
        System.out.println("Excel Export Button for GF to NAN pressed -----------------------------");
        // file chooser dialog
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Save Excel File");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            ExcelExport xlsExport = new ExcelExport();
            // export the observalbe list to excel
            xlsExport.reportGFtoNanExcelExport(GFtoNanData, file);
        }
    }
    
    // GF to Nan Table
    @FXML
    private TableView<GFtoNan> GFtoNanTable;
    @FXML
    private TableColumn<GFtoNan, String> GFReportDateCol;
    @FXML
    private TableColumn<GFtoNan, String> GFCustomerNameCol;
    @FXML
    private TableColumn<GFtoNan, String> GFShipDateCol;
    @FXML
    private TableColumn<GFtoNan, String> GFInvoiceNoCol;
    @FXML
    private TableColumn<GFtoNan, String> GFInvoiceDateCol;
    @FXML
    private TableColumn<GFtoNan, String> GFPOCol;
    @FXML
    private TableColumn<GFtoNan, String> GFSOCol;
    @FXML
    private TableColumn<GFtoNan, String> GFOrderDateCol;
    @FXML
    private TableColumn<GFtoNan, String> GFProcessNameCol;
    @FXML
    private TableColumn<GFtoNan, String> GFCustomerPartnameCol;
    @FXML
    private TableColumn<GFtoNan, String> GFInternalPartNameCol;
    @FXML
    private TableColumn<GFtoNan, String> GFLotIdCol;
    @FXML
    private TableColumn<GFtoNan, String> GFCustomerLotIdCol;
    @FXML
    private TableColumn<GFtoNan, String> GFLotTypeCol;
    @FXML
    private TableColumn<GFtoNan, String> GFLotPriorityCol;
    @FXML
    private TableColumn<GFtoNan, String> GFAgreedGDPWCol;
    @FXML
    private TableColumn<GFtoNan, String> GFCalculatedGDPLCol;
    @FXML
    private TableColumn<GFtoNan, String> GFCycleTimeCol;
    @FXML
    private TableColumn<GFtoNan, String> GFBillQtyWfrCol;
    @FXML
    private TableColumn<GFtoNan, String> GFWfrIdsCol;
    @FXML
    private TableColumn<GFtoNan, String> GFvBillDieCol;
    @FXML
    private TableColumn<GFtoNan, String> GFShipToLocationCol;
    @FXML
    private TableColumn<GFtoNan, String> GFBillToLocationCol;
    @FXML
    private TableColumn<GFtoNan, String> GFETACol;
    @FXML
    private TableColumn<GFtoNan, String> GFETDCol;
    @FXML
    private TableColumn<GFtoNan, String> GFForwarderCol;
    @FXML
    private TableColumn<GFtoNan, String> GFHAWBCol;
    @FXML
    private TableColumn<GFtoNan, String> GFMAWBCol;
    @FXML
    private TableColumn<GFtoNan, String> GFFlightNoCol;
    @FXML
    private TableColumn<GFtoNan, String> GFConnectingFlightNoCol;
    @FXML
    private TableColumn<GFtoNan, String> GFReportFileNameCol;
    
    // TSMC to Nan Filter Area
    @FXML
    private ComboBox<String> ComboBoxTSMCtoNanValue;  
    @FXML
    private TextField TextFieldTSMCtoNanValue;
    @FXML
    private ComboBox<String> ComboBoxTSMCtoNan2Value;  
    @FXML
    private TextField TextFieldTSMCtoNan2Value;
    @FXML
    private ComboBox<String> ComboBoxTSMCtoNan3Value;  
    @FXML
    private TextField TextFieldTSMCtoNan3Value;
    @FXML
    private CheckBox CheckBoxTSMCtoNanOpenShipment;
    @FXML
    protected void filterTSMCtoNanButton() {
        // define the sylQuery based on the selected values
        System.out.println("Filter Button for data filter TSMC to NAN pressed -----------------------------"); 
        // get the sql query from the combo boxes etc
        String sqlQuery = getTSMCtoNanfilterButtonQuery();
        // call the filter method
        buildTableTSMCtoNan(sqlQuery);
    }
    @FXML
    protected void TSMCtoNanxlsExportButton() {
        System.out.println("Excel Export Button for TSMC to NAN pressed -----------------------------");
        // file chooser dialog
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Save Excel File");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            ExcelExport xlsExport = new ExcelExport();
            // export the observalbe list to excel
            xlsExport.reportTSMCtoNanExcelExport(TSMCtoNanData, file);
        }
    }
    
    // TSMC to Nan Table
    @FXML
    private TableView<TSMCtoNan> TSMCtoNanTable;
    @FXML
    private TableColumn<TSMCtoNan, String> TSMCLotCol;
    @FXML
    private TableColumn<TSMCtoNan, String> TSMCWaferPcsCol;
    @FXML
    private TableColumn<TSMCtoNan, String> TSMCInvoiceNoCol;
    @FXML
    private TableColumn<TSMCtoNan, String> TSMCInvoiceDateCol;
    @FXML
    private TableColumn<TSMCtoNan, String> TSMCForwarderCol;
    @FXML
    private TableColumn<TSMCtoNan, String> TSMCMAWBCol;
    @FXML
    private TableColumn<TSMCtoNan, String> TSMCHAWBCol;
    @FXML
    private TableColumn<TSMCtoNan, String> TSMCReportFileNameCol;
    @FXML
    private TableColumn<TSMCtoNan, String> TSMCTechnologyCol;
    @FXML
    private TableColumn<TSMCtoNan, String> TSMCProductCol;
    @FXML
    private TableColumn<TSMCtoNan, String> TSMCProduct2000Col;
    @FXML
    private TableColumn<TSMCtoNan, String> TSMCETACol;
    
    // UMCI to Nan Filter Area
    @FXML
    private ComboBox<String> ComboBoxUMCItoNanValue;  
    @FXML
    private TextField TextFieldUMCItoNanValue;
    @FXML
    private ComboBox<String> ComboBoxUMCItoNan2Value;  
    @FXML
    private TextField TextFieldUMCItoNan2Value;
    @FXML
    private ComboBox<String> ComboBoxUMCItoNan3Value;  
    @FXML
    private TextField TextFieldUMCItoNan3Value;
    @FXML
    private CheckBox CheckBoxUMCItoNanOpenShipment;
    @FXML
    protected void filterUMCItoNanButton() {
        // define the sylQuery based on the selected values
        System.out.println("Filter Button for data filter UMCI to NAN pressed -----------------------------"); 
        // get the sql query from the combo boxes etc
        String sqlQuery = getUMCItoNanfilterButtonQuery();
        // call the filter method
        buildTableUMCItoNan(sqlQuery);
    }
    @FXML
    protected void UMCItoNanxlsExportButton() {
        System.out.println("Excel Export Button for UMCI to NAN pressed -----------------------------");
        // file chooser dialog
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Excel files (*.xls)", "*.xls");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Save Excel File");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            ExcelExport xlsExport = new ExcelExport();
            // export the observalbe list to excel
            xlsExport.reportUMCItoNanExcelExport(UMCItoNanData, file);
        }
    }
    
    // UMCI to Nan Table
    @FXML
    private TableView<UMCItoNan> UMCItoNanTable;
    @FXML
    private TableColumn<UMCItoNan, String> UMCIPART_DIVCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCIINV_NOCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCISHPTO_IDCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCIINV_DATECol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCIMAWB_NOCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCIHAWB_NOCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCIFLT_NOCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCIFLT_DATECol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCIFLT_DESTCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCICARTON_NOCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCIPO_NOCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCIPRD_NOCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCILOT_TYPECol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCILOT_NOCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCISHIP_W_QTYCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCISHIP_D_QTYCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCISHP_PRD_NOCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCICTM_DEVICECol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCICUSTOMER_LOTCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCIUMC_INV_NOCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCIREMARKCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCIWAFER_NOCol;
    @FXML
    private TableColumn<UMCItoNan, String> UMCIReportFileNameCol;

    // Wip Nan Line Chart Filter Area
    @FXML
    private TextField TextFieldwipLineChartValue;
    @FXML
    protected void filterwipLineChartButton() {
        // define the sylQuery based on the selected values
        System.out.println("Filter Button for wip line chart data filter pressed -----------------------------"); 
        // get the sql query from the combo boxes etc
        String sqlQuery = getwipLineChartfilterButtonQuery();
        // call the filter method
        buildDataForLineChartWipNan(sqlQuery);
        ChartData chartWipNanData = new ChartData();
        chartWipNanData.chartDataBarChart(lineChartWipNan, wipNanLineChartData);
        //chartWipNanData.chartDataLineChart(lineChartWipNan, wipNanLineChartData); // line chart
    }
    
    @FXML
    public void wipLineChartSaveAsPngButton() {
        System.out.println("PNG Button for wip line chart pressed -----------------------------");
        WritableImage image = lineChartWipNan.snapshot(new SnapshotParameters(), null);
        
        // file chooser dialog
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Save PNG File");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                // TODO: handle exception here
            }
        }
    }
    
    // Wip Nan Line Chart
    @FXML
    private CategoryAxis lineChartWipNanXAxis; // bar chart
    //private NumberAxis lineChartWipNanXAxis; // line chart
    @FXML
    private NumberAxis lineChartWipNanYAxis;
    @FXML
    private StackedBarChart<String, Number> lineChartWipNan; // bar chart
    //private LineChart<Number, Number> lineChartWipNan; // line chart
    
    // Wip Nan Bar Chart Filter Area
    @FXML
    private ComboBox<String> ComboBoxwipBarChartValue;  
    @FXML
    private TextField TextFieldwipBarChartValue;
    @FXML
    private ComboBox<String> ComboBoxwipBarChart2Value;  
    @FXML
    private TextField TextFieldwipBarChart2Value;
    @FXML
    private CheckBox CheckBoxwipBarChartNotDieBank;
    @FXML
    private CheckBox CheckBoxwipBarChartWIP2;
    @FXML
    private CheckBox CheckBoxwipBarChartWIPvalue;
    @FXML
    protected void filterwipBarChartButton() {
        // define the sylQuery based on the selected values
        System.out.println("Filter Button for wip bar chart data filter pressed -----------------------------");
        // check if checkbox is selected
        boolean barChartWIPpcs = CheckBoxwipBarChartWIP2.isSelected();
        boolean barChartWIPvalue = CheckBoxwipBarChartWIPvalue.isSelected();
        if(barChartWIPpcs){
            // set title of bar chart
            barChartWipNan.setTitle("WIP overview in Pcs.");
        }else{
            // set title of bar chart
            barChartWipNan.setTitle("WIP overview in Wafer Qty.");
        }
        // get the sql query from the combo boxes etc
        String sqlQuery = getwipBarChartfilterButtonQuery();
        // call the filter method
        buildDataForBarChartWipNan(sqlQuery);
        ChartData chartWipNanData = new ChartData();
        chartWipNanData.chartDataStackedBarChart(barChartWipNan, wipNanBarChartData, barChartWIPpcs, barChartWIPvalue);
    }
    
    @FXML
    public void wipBarChartSaveAsPngButton() {
        System.out.println("PNG Button for wip bar chart pressed -----------------------------");
        WritableImage image = barChartWipNan.snapshot(new SnapshotParameters(), null);
        
        // file chooser dialog
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.png");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.setTitle("Save PNG File");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            try {
                // we write the node to the image
                ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            } catch (IOException e) {
                // TODO: handle exception here
            }
        }
    }
    
    // Wip Nan Bar Chart                                        
    @FXML
    private CategoryAxis barChartWipNanXAxis;
    @FXML
    private NumberAxis barChartWipNanYAxis;
    @FXML
    private StackedBarChart<String, Number> barChartWipNan;

    /**
     * Initialize FXML
     * @param url
     * @param rb 
     */
    @FXML
    public void initialize(URL url, ResourceBundle rb) {
        // logger message
        logger.info("Initialize!");      
        
        // set the version number
        ProgrammVersionLabel.setText(ProgrammVersion);
        
        // define the combo box values for lot data
        ComboBoxLotDataList.addAll("Lot","Product","BasicType","CreationDate","FirstStepDate","LastStepDate","LastDate");
        // put the values in the 3 combo boxes for lot data
        ComboBoxValueLotData.setItems(ComboBoxLotDataList);
        ComboBox2ValueLotData.setItems(ComboBoxLotDataList);
        ComboBox3ValueLotData.setItems(ComboBoxLotDataList);
        // select the first value in combo boxes for lot data
        ComboBoxValueLotData.getSelectionModel().select(0);
        ComboBox2ValueLotData.getSelectionModel().select(1);
        ComboBox3ValueLotData.getSelectionModel().select(2);
                
        // define the combo box values for wip nan
        ComboBoxWipNanList.addAll("ReportDate","Lot","Owner","Product","HoldFlag","WIP1","WIP2","Shrink","FE_SITE",
                "BasicType","Package","Step","WorkCenter","SEQ","Percentage","ReportFileName");
        // put the values in the 3 combo boxes for wip nan
        ComboBoxValue.setItems(ComboBoxWipNanList);
        ComboBox2Value.setItems(ComboBoxWipNanList);
        ComboBox3Value.setItems(ComboBoxWipNanList);
        // select the first value in combo boxes for wip nan
        ComboBoxValue.getSelectionModel().select(0);
        ComboBox2Value.getSelectionModel().select(7);
        ComboBox3Value.getSelectionModel().select(2);
        // set the report date to newest day found in database
        String getLastDate = getLatestDateWipNan(sqlNewestDateWipNanSelect);
        TextFieldValue.setText(getLastDate);
        
        // define the combo box values for gf to nan
        ComboBoxGFtoNanList.addAll("ReportDate","CustomerName","ShipDate","InvoiceNo","InvoiceDate","PO","SO","OrderDate","ProcessName",
                "CustomerPartname","InternalPartName","LotId","CustomerLotId","LotType","LotPriority","AgreedGDPW","CalculatedGDPL","CycleTime",
                "BillQtyWfr","WfrIds","vBillDie","ShipToLocation","BillToLocation","ETA","ETD","Forwarder","HAWB","MAWB","FlightNo","ConnectingFlightNo","ReportFileName");
        ComboBoxTSMCtoNanList.addAll("Lot","WaferPcs","InvoiceNo","InvoiceDate","Forwarder","MAWB","HAWB","ReportFileName");
        // put the values in the 3 combo boxes for wip nan
        ComboBoxGFtoNanValue.setItems(ComboBoxGFtoNanList);
        ComboBoxGFtoNan2Value.setItems(ComboBoxGFtoNanList);
        ComboBoxGFtoNan3Value.setItems(ComboBoxGFtoNanList);
        // select the first value in combo boxes for wip nan
        ComboBoxGFtoNanValue.getSelectionModel().select(0);
        ComboBoxGFtoNan2Value.getSelectionModel().select(1);
        ComboBoxGFtoNan3Value.getSelectionModel().select(2);
        // select the shipment open checkbox
        CheckBoxGFtoNanOpenShipment.setSelected(true);
        
        // define the combo box values for tsmc to nan
        ComboBoxTSMCtoNanList.addAll("Lot","WaferPcs","InvoiceNo","InvoiceDate","Forwarder","MAWB","HAWB","ReportFileName","Technology",
                "Product","Product2000","ETA");
        // put the values in the 3 combo boxes for tsmc to nan
        ComboBoxTSMCtoNanValue.setItems(ComboBoxTSMCtoNanList);
        ComboBoxTSMCtoNan2Value.setItems(ComboBoxTSMCtoNanList);
        ComboBoxTSMCtoNan3Value.setItems(ComboBoxTSMCtoNanList);
        // select the first value in combo boxes for tsmc to nan
        ComboBoxTSMCtoNanValue.getSelectionModel().select(0);
        ComboBoxTSMCtoNan2Value.getSelectionModel().select(1);
        ComboBoxTSMCtoNan3Value.getSelectionModel().select(2);
        // select the shipment open checkbox
        CheckBoxTSMCtoNanOpenShipment.setSelected(true);
        
        // define the combo box values for umci to nan
        ComboBoxUMCItoNanList.addAll("PART_DIV","INV_NO","SHPTO_ID","INV_DATE","MAWB_NO","HAWB_NO","FLT_NO","FLT_DATE","FLT_DEST",
                "CARTON_NO","PO_NO","PRD_NO","LOT_TYPE","LOT_NO","SHIP_W_QTY","SHIP_D_QTY","SHP_PRD_NO","CTM_DEVICE",
                "CUSTOMER_LOT","UMC_INV_NO","REMARK","WAFER_NO","ReportFileName");
        // put the values in the 3 combo boxes for umci to nan
        ComboBoxUMCItoNanValue.setItems(ComboBoxUMCItoNanList);
        ComboBoxUMCItoNan2Value.setItems(ComboBoxUMCItoNanList);
        ComboBoxUMCItoNan3Value.setItems(ComboBoxUMCItoNanList);
        // select the first value in combo boxes for tsmc to nan
        ComboBoxUMCItoNanValue.getSelectionModel().select(0);
        ComboBoxUMCItoNan2Value.getSelectionModel().select(1);
        ComboBoxUMCItoNan3Value.getSelectionModel().select(2);
        // select the shipment open checkbox
        CheckBoxUMCItoNanOpenShipment.setSelected(true);
        
        // define the combo box values for bar chart wip nan
        ComboBoxWipNanBarChartList.addAll("ReportDate","Lot","Owner","Product","HoldFlag","WIP1","WIP2","Shrink","FE_SITE",
                "BasicType","Package","Step","WorkCenter","SEQ","Percentage","ReportFileName");
        // put the values in the 3 combo boxes for tsmc to nan
        ComboBoxwipBarChartValue.setItems(ComboBoxWipNanBarChartList);
        ComboBoxwipBarChart2Value.setItems(ComboBoxWipNanBarChartList);
        // select the first value in combo boxes for tsmc to nan
        ComboBoxwipBarChartValue.getSelectionModel().select(0);
        ComboBoxwipBarChart2Value.getSelectionModel().select(9);
        // set the report date to today
        TextFieldwipBarChartValue.setText(dateToString(-1));
        
        // build the lot data table data
        buildTableLotData(sqlLotDataSelect);
        // Setup the lot data table data
        LotColLotData.setCellValueFactory(new PropertyValueFactory<LotData,String>("Lot"));
        ProductColLotData.setCellValueFactory(new PropertyValueFactory<LotData,String>("Product"));
        BasicTypeColLotData.setCellValueFactory(new PropertyValueFactory<LotData,String>("BasicType"));
        CreationDateColLotData.setCellValueFactory(new PropertyValueFactory<LotData,String>("CreationDate"));
        FirstStepDateColLotData.setCellValueFactory(new PropertyValueFactory<LotData,String>("FirstStepDate"));
        LastStepDateColLotData.setCellValueFactory(new PropertyValueFactory<LotData,String>("LastStepDate"));
        LastDateColLotData.setCellValueFactory(new PropertyValueFactory<LotData,String>("LastDate"));
        // set the items to the table
        lotDataTable.setItems(lotData);
        
        // build the wip nan table data
        buildTableWipNan(sqlLotSelect);
        // Setup the wip nan table data
        ReportDateCol.setCellValueFactory(new PropertyValueFactory<WipNan,String>("ReportDate"));
        LotCol.setCellValueFactory(new PropertyValueFactory<WipNan,String>("Lot"));
        OwnerCol.setCellValueFactory(new PropertyValueFactory<WipNan,String>("Owner"));
        ProductCol.setCellValueFactory(new PropertyValueFactory<WipNan,String>("Product"));
        HoldFlagCol.setCellValueFactory(new PropertyValueFactory<WipNan,String>("HoldFlag"));
        WIP1Col.setCellValueFactory(new PropertyValueFactory<WipNan,String>("WIP1"));
        WIP2Col.setCellValueFactory(new PropertyValueFactory<WipNan,String>("WIP2"));
        ShrinkCol.setCellValueFactory(new PropertyValueFactory<WipNan,String>("Shrink"));
        FE_SITECol.setCellValueFactory(new PropertyValueFactory<WipNan,String>("FE_SITE"));
        BasicTypeCol.setCellValueFactory(new PropertyValueFactory<WipNan,String>("BasicType"));
        PackageCol.setCellValueFactory(new PropertyValueFactory<WipNan,String>("Package"));
        StepCol.setCellValueFactory(new PropertyValueFactory<WipNan,String>("Step"));
        WorkCenterCol.setCellValueFactory(new PropertyValueFactory<WipNan,String>("WorkCenter"));
        SEQCol.setCellValueFactory(new PropertyValueFactory<WipNan,String>("SEQ"));
        PercentageCol.setCellValueFactory(new PropertyValueFactory<WipNan,String>("Percentage"));
        ReportFileNameCol.setCellValueFactory(new PropertyValueFactory<WipNan,String>("ReportFileName"));
        // set the items to the table
        wipNanTable.setItems(wipNanData);
        
        // build the gf to nan table data
        buildTableGFtoNan(sqlShipmentSelectGF);
        // setup the ggf to nan table data
        GFReportDateCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("ReportDate"));
        GFCustomerNameCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("CustomerName"));
        GFShipDateCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("ShipDate"));
        GFInvoiceNoCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("InvoiceNo"));
        GFInvoiceDateCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("InvoiceDate"));
        GFPOCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("PO"));
        GFSOCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("SO"));
        GFOrderDateCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("OrderDate"));
        GFProcessNameCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("ProcessName"));
        GFCustomerPartnameCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("CustomerPartname"));
        GFInternalPartNameCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("InternalPartName"));
        GFLotIdCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("LotId"));
        GFCustomerLotIdCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("CustomerLotId"));
        GFLotTypeCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("LotType"));
        GFLotPriorityCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("LotPriority"));
        GFAgreedGDPWCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("AgreedGDPW"));
        GFCalculatedGDPLCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("CalculatedGDPL"));
        GFCycleTimeCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("CycleTime"));
        GFBillQtyWfrCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("BillQtyWfr"));
        GFWfrIdsCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("WfrIds"));
        GFvBillDieCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("vBillDie"));
        GFShipToLocationCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("ShipToLocation"));
        GFBillToLocationCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("BillToLocation"));
        GFETACol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("ETA"));
        GFETDCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("ETD"));
        GFForwarderCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("Forwarder"));
        GFHAWBCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("HAWB"));
        GFMAWBCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("MAWB"));
        GFFlightNoCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("FlightNo"));
        GFConnectingFlightNoCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("ConnectingFlightNo"));
        GFReportFileNameCol.setCellValueFactory(new PropertyValueFactory<GFtoNan,String>("ReportFileName"));
        // set the items to the table
        GFtoNanTable.setItems(GFtoNanData);
        
        // build the tsmc to nan table data
        buildTableTSMCtoNan(sqlShipmentSelectTSMC);
        // Setup the tsmc to nan table data
        TSMCLotCol.setCellValueFactory(new PropertyValueFactory<TSMCtoNan,String>("Lot"));
        TSMCWaferPcsCol.setCellValueFactory(new PropertyValueFactory<TSMCtoNan,String>("WaferPcs"));
        TSMCInvoiceNoCol.setCellValueFactory(new PropertyValueFactory<TSMCtoNan,String>("InvoiceNo"));
        TSMCInvoiceDateCol.setCellValueFactory(new PropertyValueFactory<TSMCtoNan,String>("InvoiceDate"));
        TSMCForwarderCol.setCellValueFactory(new PropertyValueFactory<TSMCtoNan,String>("Forwarder"));
        TSMCMAWBCol.setCellValueFactory(new PropertyValueFactory<TSMCtoNan,String>("MAWB"));
        TSMCHAWBCol.setCellValueFactory(new PropertyValueFactory<TSMCtoNan,String>("HAWB"));
        TSMCReportFileNameCol.setCellValueFactory(new PropertyValueFactory<TSMCtoNan,String>("ReportFileName"));
        TSMCTechnologyCol.setCellValueFactory(new PropertyValueFactory<TSMCtoNan,String>("Technology"));
        TSMCProductCol.setCellValueFactory(new PropertyValueFactory<TSMCtoNan,String>("Product"));
        TSMCProduct2000Col.setCellValueFactory(new PropertyValueFactory<TSMCtoNan,String>("Product2000"));
        TSMCETACol.setCellValueFactory(new PropertyValueFactory<TSMCtoNan,String>("ETA"));
        // set the items to the table
        TSMCtoNanTable.setItems(TSMCtoNanData);
        
        // build the umci to nan table data
        buildTableUMCItoNan(sqlShipmentSelectUMCI);
        // Setup the umci to nan table data
        UMCIPART_DIVCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("PART_DIV"));
        UMCIINV_NOCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("INV_NO"));
        UMCISHPTO_IDCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("SHPTO_ID"));
        UMCIINV_DATECol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("INV_DATE"));
        UMCIMAWB_NOCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("MAWB_NO"));
        UMCIHAWB_NOCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("HAWB_NO"));
        UMCIFLT_NOCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("FLT_NO"));
        UMCIFLT_DATECol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("FLT_DATE"));
        UMCIFLT_DESTCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("FLT_DEST"));
        UMCICARTON_NOCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("CARTON_NO"));
        UMCIPO_NOCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("PO_NO"));
        UMCIPRD_NOCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("PRD_NO"));
        UMCILOT_TYPECol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("LOT_TYPE"));
        UMCILOT_NOCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("LOT_NO"));
        UMCISHIP_W_QTYCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("SHIP_W_QTY"));
        UMCISHIP_D_QTYCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("SHIP_D_QTY"));
        UMCISHP_PRD_NOCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("SHP_PRD_NO"));
        UMCICTM_DEVICECol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("CTM_DEVICE"));
        UMCICUSTOMER_LOTCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("CUSTOMER_LOT"));
        UMCIUMC_INV_NOCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("UMC_INV_NO"));
        UMCIREMARKCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("REMARK"));
        UMCIWAFER_NOCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("WAFER_NO"));
        UMCIReportFileNameCol.setCellValueFactory(new PropertyValueFactory<UMCItoNan,String>("ReportFileName"));
        // set the items to the table
        UMCItoNanTable.setItems(UMCItoNanData);
        
        // build the wip nan data for line chart
        buildDataForLineChartWipNan(sqlWIPLineChartActual);
        // build the wip nan data for bar chart
        buildDataForBarChartWipNan(sqlWIPChartActual);
        // create new instance of chart data
        ChartData chartWipNanData = new ChartData();
        // create the line chart data
        chartWipNanData.chartDataBarChart(lineChartWipNan, wipNanLineChartData);
        //chartWipNanData.chartDataLineChart(lineChartWipNan, wipNanLineChartData); // line chart
        // get the value of  WIP pcs. and show value checkboxes
        boolean barChartWIPpcs = CheckBoxwipBarChartWIP2.isSelected();
        boolean barChartWIPvalue = CheckBoxwipBarChartWIPvalue.isSelected();
        
        // create the bar chart data
        chartWipNanData.chartDataStackedBarChart(barChartWipNan, wipNanBarChartData,barChartWIPpcs,barChartWIPvalue);
    }    
    
    // The data as an observable list of wip nan
    private ObservableList<WipNan> wipNanData  = FXCollections.observableArrayList();
    // The data as an observable list of lot data
    private ObservableList<LotData> lotData  = FXCollections.observableArrayList();
    // The data as an observable list of gf to Nan 
    private ObservableList<GFtoNan> GFtoNanData  = FXCollections.observableArrayList();
    // The data as an observable list of tsmc to Nan 
    private ObservableList<TSMCtoNan> TSMCtoNanData  = FXCollections.observableArrayList();
    // The data as an observable list of umci to Nan 
    private ObservableList<UMCItoNan> UMCItoNanData  = FXCollections.observableArrayList();
    // The data as an observable list of Strings for the combo box for lot data
    private ObservableList<String> ComboBoxLotDataList = FXCollections.observableArrayList();
    // The data as an observable list of Strings for the combo box for wip nan
    private ObservableList<String> ComboBoxWipNanList  = FXCollections.observableArrayList();
    // The data as an observable list of Strings for the combo box for gf to nan
    private ObservableList<String> ComboBoxGFtoNanList  = FXCollections.observableArrayList();
    // The data as an observable list of Strings for the combo box for tsmc to nan
    private ObservableList<String> ComboBoxTSMCtoNanList  = FXCollections.observableArrayList();
    // The data as an observable list of Strings for the combo box for umci to nan
    private ObservableList<String> ComboBoxUMCItoNanList  = FXCollections.observableArrayList();
    // The data as an observable list of Strings for the combo box for bar chart wip nan
    private ObservableList<String> ComboBoxWipNanBarChartList  = FXCollections.observableArrayList();
    // The bar chart data as an observable list of wip nan
    private ObservableList<WipNan> wipNanBarChartData  = FXCollections.observableArrayList();
    // The line chart data as an observable list of wip nan
    private ObservableList<WipNan> wipNanLineChartData  = FXCollections.observableArrayList();
    
    /**
     * The start method for the UI
     * @param primaryStage
     * @throws Exception 
     */
    @Override
    public void start(Stage primaryStage) throws Exception { 
        
        // set the title
        primaryStage.setTitle("Wip Nan");
        
        // load the FXML file
        Pane primaryPane = (Pane)FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        // set a stylesheet
        primaryPane.getStylesheets().add(getClass().getResource("wipnan.css").toString());
        // create the scene
        Scene scene = new Scene(primaryPane);
        
        // set the scene to the stage
        primaryStage.setScene(scene);

        // show the stage
        primaryStage.show();
    }
    
    /**
     * Build filtered sql query for lot data
     * we check the filters like comboboxes, text fields and checkboxes
     * out of the data we create the sql query string
     * @return sql query string
     */
    public String getLotDatafilterButtonQuery() {
        // get the combo box value
        String ComboBoxStringLotData = ComboBoxValueLotData.getSelectionModel().getSelectedItem();
        // get the text field value
        String TextFieldStringLotData = TextFieldValueLotData.getText();
        // check if string value is empty
        if (TextFieldStringLotData.isEmpty()){
            // set the text field value to wildcard "&"
            TextFieldStringLotData = "%";
        }
        
        // get the combo box 2 value
        String ComboBox2StringLotData = ComboBox2ValueLotData.getSelectionModel().getSelectedItem();
        // get the text field 2 value
        String TextField2StringLotData = TextField2ValueLotData.getText();
        // check if string value is empty
        if (TextField2StringLotData.isEmpty()){
            // set the text field value to wildcard "&"
            TextField2StringLotData = "%";
        }
        // get the combo box 3 value
        String ComboBox3StringLotData = ComboBox3ValueLotData.getSelectionModel().getSelectedItem();
        // get the text field 3 value
        String TextField3StringLotData = TextField3ValueLotData.getText();
        // check if string value is empty
        if (TextField3StringLotData.isEmpty()){
            // set the text field value to wildcard "&"
            TextField3StringLotData = "%";
        }
        // create the combined SQL query with all 3 comboboxes and text field values
        String filterLotDataDataQuery = ("SELECT * FROM lot_data WHERE " + ComboBoxStringLotData + " LIKE '" + TextFieldStringLotData + "' "
                + "AND " + ComboBox2StringLotData + " LIKE '" + TextField2StringLotData + "' "
                + "AND " + ComboBox3StringLotData + " LIKE '" + TextField3StringLotData + "';");
        
        logger.info("Build filtered sql query for lot data.");
        
        // we return the sql query string
        return filterLotDataDataQuery;
    }

    /**
     * Build filtered sql query for wip nan
     * we check the filters like comboboxes, text fields and checkboxes
     * out of the data we create the sql query string
     * @return sql query string
     */
    public String getWipNanfilterButtonQuery() {
        // check if checkbox is selected
        String CheckBoxSelected = "";
        if (CheckBoxNotProd.isSelected()){
            // insert the filter not PROD
            CheckBoxSelected = " AND Owner NOT LIKE 'PROD'";
        }
        // get the combo box value
        String ComboBoxString = ComboBoxValue.getSelectionModel().getSelectedItem();
        // get the text field value
        String TextFieldString = TextFieldValue.getText();
        // check if string value is empty
        if (TextFieldString.isEmpty()){
            // set the text field value to wildcard "&"
            TextFieldString = "%";
        }
        
        // get the combo box 2 value
        String ComboBox2String = ComboBox2Value.getSelectionModel().getSelectedItem();
        // get the text field 2 value
        String TextField2String = TextField2Value.getText();
        // check if string value is empty
        if (TextField2String.isEmpty()){
            // set the text field value to wildcard "&"
            TextField2String = "%";
        }
        // get the combo box 3 value
        String ComboBox3String = ComboBox3Value.getSelectionModel().getSelectedItem();
        // get the text field 3 value
        String TextField3String = TextField3Value.getText();
        // check if string value is empty
        if (TextField3String.isEmpty()){
            // set the text field value to wildcard "&"
            TextField3String = "%";
        }
        // create the combined SQL query with all 3 comboboxes and text field values
        String filterWipNanDataQuery = ("SELECT * FROM wip_nan WHERE " + ComboBoxString + " LIKE '" + TextFieldString + "' "
                + "AND " + ComboBox2String + " LIKE '" + TextField2String + "' "
                + "AND " + ComboBox3String + " LIKE '" + TextField3String + "'"+ CheckBoxSelected + ";");
        
        logger.info("Build filtered sql query for wip nan.");
        
        // we return the sql query string
        return filterWipNanDataQuery;
    }
    
    /**
     * Build filtered sql query for gf to nan
     * we check the filters like comboboxes, text fields and checkboxes
     * out of the data we create the sql query string
     * @return sql query string
     */
    public String getGFtoNanfilterButtonQuery() {
        // check if checkbox is selected
        String CheckBoxSelected = "";
        if (CheckBoxGFtoNanOpenShipment.isSelected()){
            // insert the filter not PROD
            CheckBoxSelected = " AND wip_nan.Lot ISNULL";
        }
        // get the combo box value
        String ComboBoxGFtoNanString = ComboBoxGFtoNanValue.getSelectionModel().getSelectedItem();
        // get the text field value
        String TextFieldGFtoNanString = TextFieldGFtoNanValue.getText();
        // check if string value is empty
        if (TextFieldGFtoNanString.isEmpty()){
            // set the text field value to wildcard "&"
            TextFieldGFtoNanString = "%";
        }
        
        // get the combo box 2 value
        String ComboBoxGFtoNan2String = ComboBoxGFtoNan2Value.getSelectionModel().getSelectedItem();
        // get the text field 2 value
        String TextFieldGFtoNan2String = TextFieldGFtoNan2Value.getText();
        // check if string value is empty
        if (TextFieldGFtoNan2String.isEmpty()){
            // set the text field value to wildcard "&"
            TextFieldGFtoNan2String = "%";
        }
        // get the combo box 3 value
        String ComboBoxGFtoNan3String = ComboBoxGFtoNan2Value.getSelectionModel().getSelectedItem();
        // get the text field 3 value
        String TextFieldGFtoNan3String = TextFieldGFtoNan3Value.getText();
        // check if string value is empty
        if (TextFieldGFtoNan3String.isEmpty()){
            // set the text field value to wildcard "&"
            TextFieldGFtoNan3String = "%";
        }
        // create the combined SQL query with all 3 comboboxes and text field values
        String filterGFtoNanDataQuery = ("SELECT gf_to_nan.* FROM gf_to_nan LEFT JOIN wip_nan ON gf_to_nan.CustomerLotId = wip_nan.Lot"
                + " WHERE gf_to_nan." + ComboBoxGFtoNanString + " LIKE '" + TextFieldGFtoNanString + "'"
                + " AND gf_to_nan." + ComboBoxGFtoNan2String + " LIKE '" + TextFieldGFtoNan2String + "'"
                + " AND gf_to_nan." + ComboBoxGFtoNan3String + " LIKE '" + TextFieldGFtoNan3String + "'"
                + CheckBoxSelected + ";");
        
        logger.info("Build filtered sql query for gf to nan.");

        // we return the sql query string
        return filterGFtoNanDataQuery;
    }
    
    /**
     * Build filtered sql query for tsmc to nan
     * we check the filters like comboboxes, text fields and checkboxes
     * out of the data we create the sql query string
     * @return sql query string
     */
    public String getTSMCtoNanfilterButtonQuery() {
        // check if checkbox is selected
        String CheckBoxSelected = "";
        if (CheckBoxTSMCtoNanOpenShipment.isSelected()){
            // insert the filter not PROD
            CheckBoxSelected = " AND wip_nan.Lot ISNULL";
        }
        // get the combo box value
        String ComboBoxTSMCtoNanString = ComboBoxTSMCtoNanValue.getSelectionModel().getSelectedItem();
        // get the text field value
        String TextFieldTSMCtoNanString = TextFieldTSMCtoNanValue.getText();
        // check if string value is empty
        if (TextFieldTSMCtoNanString.isEmpty()){
            // set the text field value to wildcard "&"
            TextFieldTSMCtoNanString = "%";
        }
        
        // get the combo box 2 value
        String ComboBoxTSMCtoNan2String = ComboBoxTSMCtoNan2Value.getSelectionModel().getSelectedItem();
        // get the text field 2 value
        String TextFieldTSMCtoNan2String = TextFieldTSMCtoNan2Value.getText();
        // check if string value is empty
        if (TextFieldTSMCtoNan2String.isEmpty()){
            // set the text field value to wildcard "&"
            TextFieldTSMCtoNan2String = "%";
        }
        // get the combo box 3 value
        String ComboBoxTSMCtoNan3String = ComboBoxTSMCtoNan3Value.getSelectionModel().getSelectedItem();
        // get the text field 3 value
        String TextFieldTSMCtoNan3String = TextFieldTSMCtoNan3Value.getText();
        // check if string value is empty
        if (TextFieldTSMCtoNan3String.isEmpty()){
            // set the text field value to wildcard "&"
            TextFieldTSMCtoNan3String = "%";
        }
        // create the combined SQL query with all 3 comboboxes and text field values
        String filterTSMCtoNanDataQuery = ("SELECT tsmc_to_nan.* FROM tsmc_to_nan LEFT JOIN wip_nan ON tsmc_to_nan.Lot = wip_nan.Lot"
                + " WHERE tsmc_to_nan." + ComboBoxTSMCtoNanString + " LIKE '" + TextFieldTSMCtoNanString + "'"
                + " AND tsmc_to_nan." + ComboBoxTSMCtoNan2String + " LIKE '" + TextFieldTSMCtoNan2String + "'"
                + " AND tsmc_to_nan." + ComboBoxTSMCtoNan3String + " LIKE '" + TextFieldTSMCtoNan3String + "'"
                + CheckBoxSelected + ";");
        
        logger.info("Build filtered sql query for tsmc to nan.");
        
        // we return the sql query string
        return filterTSMCtoNanDataQuery;
    }
    
    /**
     * Build filtered sql query for umci to nan
     * we check the filters like comboboxes, text fields and checkboxes
     * out of the data we create the sql query string
     * @return sql query string
     */
    public String getUMCItoNanfilterButtonQuery() {
        // check if checkbox is selected
        String CheckBoxSelected = "";
        if (CheckBoxUMCItoNanOpenShipment.isSelected()){
            // insert the filter not PROD
            CheckBoxSelected = " AND wip_nan.Lot ISNULL";
        }
        // get the combo box value
        String ComboBoxUMCItoNanString = ComboBoxUMCItoNanValue.getSelectionModel().getSelectedItem();
        // get the text field value
        String TextFieldUMCItoNanString = TextFieldUMCItoNanValue.getText();
        // check if string value is empty
        if (TextFieldUMCItoNanString.isEmpty()){
            // set the text field value to wildcard "&"
            TextFieldUMCItoNanString = "%";
        }
        
        // get the combo box 2 value
        String ComboBoxUMCItoNan2String = ComboBoxUMCItoNan2Value.getSelectionModel().getSelectedItem();
        // get the text field 2 value
        String TextFieldUMCItoNan2String = TextFieldUMCItoNan2Value.getText();
        // check if string value is empty
        if (TextFieldUMCItoNan2String.isEmpty()){
            // set the text field value to wildcard "&"
            TextFieldUMCItoNan2String = "%";
        }
        // get the combo box 3 value
        String ComboBoxUMCItoNan3String = ComboBoxUMCItoNan3Value.getSelectionModel().getSelectedItem();
        // get the text field 3 value
        String TextFieldUMCItoNan3String = TextFieldUMCItoNan3Value.getText();
        // check if string value is empty
        if (TextFieldUMCItoNan3String.isEmpty()){
            // set the text field value to wildcard "&"
            TextFieldUMCItoNan3String = "%";
        }
        // create the combined SQL query with all 3 comboboxes and text field values
        // "SELECT umci_to_nan.* FROM umci_to_nan LEFT JOIN wip_nan ON umci_to_nan.CUSTOMER_LOT = wip_nan.Lot WHERE wip_nan.Lot ISNULL;";
        String filterUMCItoNanDataQuery = ("SELECT umci_to_nan.* FROM umci_to_nan LEFT JOIN wip_nan ON umci_to_nan.CUSTOMER_LOT = wip_nan.Lot"
                + " WHERE umci_to_nan." + ComboBoxUMCItoNanString + " LIKE '" + TextFieldUMCItoNanString + "'"
                + " AND umci_to_nan." + ComboBoxUMCItoNan2String + " LIKE '" + TextFieldUMCItoNan2String + "'"
                + " AND umci_to_nan." + ComboBoxUMCItoNan3String + " LIKE '" + TextFieldUMCItoNan3String + "'"
                + CheckBoxSelected + ";");
        
        logger.info("Build filtered sql query for umci to nan.");
        
        // we return the sql query string
        return filterUMCItoNanDataQuery;
    }
    
    /**
     * Build filtered sql query for wip nan line chart
     * we check the text field for the lot number
     * out of the data we create the sql query string
     * @return sql query string
     */
    public String getwipLineChartfilterButtonQuery() {
        // get the text field value
        String TextFieldwipLineChartString = TextFieldwipLineChartValue.getText();
        // check if string value is empty
        if (TextFieldwipLineChartString.isEmpty()){
            // set the text field value to wildcard "&"
            TextFieldwipLineChartString = "%";
        }
        // create the combined SQL query with all 3 comboboxes and text field values
        String filterwipLineChartDataQuery = ("SELECT * FROM wip_nan WHERE Lot LIKE '" + TextFieldwipLineChartString + "';");
        
        logger.info("Build filtered sql query for wip nan line chart.");
        
        // return the sql query string
        return filterwipLineChartDataQuery;
    }
    
    /**
     * Build filtered sql query for wip nan bar chart
     * we check the filters like comboboxes, text fields and checkboxes
     * out of the data we create the sql query string
     * @return sql query string
     */
    public String getwipBarChartfilterButtonQuery() {
        // check if checkbox is selected
        String CheckBoxSelected = "";
        if (CheckBoxwipBarChartNotDieBank.isSelected()){
            // insert the filter for SEQ not with DieBank
            CheckBoxSelected = " AND SEQ NOT LIKE '0'";
        }
        // get the combo box value
        String ComboBoxwipBarChartString = ComboBoxwipBarChartValue.getSelectionModel().getSelectedItem();
        // get the text field value
        String TextFieldwipBarChartString = TextFieldwipBarChartValue.getText();
        // check if string value is empty
        if (TextFieldwipBarChartString.isEmpty()){
            // set the text field value to wildcard "&"
            TextFieldwipBarChartString = "%";
        }
        
        // get the combo box 2 value
        String ComboBoxwipBarChart2String = ComboBoxwipBarChart2Value.getSelectionModel().getSelectedItem();
        // get the text field 2 value
        String TextFieldwipBarChart2String = TextFieldwipBarChart2Value.getText();
        // check if string value is empty
        if (TextFieldwipBarChart2String.isEmpty()){
            // set the text field value to wildcard "&"
            TextFieldwipBarChart2String = "%";
        }
        // create the combined SQL query with all 3 comboboxes and text field values
        String filterwipBarChartDataQuery = ("SELECT * FROM wip_nan WHERE " + ComboBoxwipBarChartString + " LIKE '" + TextFieldwipBarChartString + "' "
                + "AND " + ComboBoxwipBarChart2String + " LIKE '" + TextFieldwipBarChart2String + "' "
                + CheckBoxSelected + ";");
        
        logger.info("Build filtered sql query for wip nan bar chart.");
        
        // return the sql query string
        return filterwipBarChartDataQuery;
    }
    
    /**
     * get newest date in table nan wip from sql query
     * we connect to the database, run the sql query and get the result set
     * @param sqlQuery 
     */
    public String getLatestDateWipNan(String sqlQuery){
        String lastDateWipNan = "";
        try{
            // connect to database
            DataBaseSQLite db = new DataBaseSQLite();
            // get ResultSet as ArrayList of ArrayList of Strings
            ResultSet rs = db.selectTableResultSet(sqlQuery);
            // loop through the result set
            while(rs.next()){
                lastDateWipNan = rs.getString("ReportDate");
            }
            logger.info("Got latest date from wip nan.");
            System.out.println("Got latest date from wip nan"); 
        }catch(Exception e){
            e.printStackTrace();
            logger.error("Error on getting latest date from wip nan.");
            System.out.println("Error on getting latest date from wip nan");
        }
        return lastDateWipNan;
    }
    
    /**
     * Build table view data for lot data from sql query
     * we connect to the database, run the sql query and get the result set
     * we fill the data into the table view observable list
     * @param sqlQuery 
     */
    public void buildTableLotData(String sqlQuery){
        try{
            // connect to database
            DataBaseSQLite db = new DataBaseSQLite();
            // get ResultSet as ArrayList of ArrayList of Strings
            ResultSet rs = db.selectTableResultSet(sqlQuery);
            lotData.clear();
            // loop through the result set
            while(rs.next()){
                //add data from result set into lot data
                LotData lotDataItem = new LotData();
                lotDataItem.setLot(rs.getString("Lot"));
                lotDataItem.setProduct(rs.getString("Product"));
                lotDataItem.setBasicType(rs.getString("BasicType"));
                lotDataItem.setCreationDate(rs.getString("CreationDate"));
                lotDataItem.setFirstStepDate(rs.getString("FirstStepDate"));
                lotDataItem.setLastStepDate(rs.getString("LastStepDate"));
                lotDataItem.setLastDate(rs.getString("LastDate"));

                lotData.add(lotDataItem);
            }
            logger.info("Table build done for lot data.");
            System.out.println("Table build done for lot data");  
            // enable multi-selection
            lotDataTable.getSelectionModel().setCellSelectionEnabled(true);
            lotDataTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            // enable copy/paste
            TableUtils.installCopyPasteHandler(lotDataTable);
        }catch(Exception e){
            e.printStackTrace();
            logger.error("Error on Building Data for lot data.");
            System.out.println("Error on Building Data for lot data");             
        }
    }

    /**
     * Build table view data for nan wip from sql query
     * we connect to the database, run the sql query and get the result set
     * we fill the data into the table view observable list
     * @param sqlQuery 
     */
    public void buildTableWipNan(String sqlQuery){
        try{
            // connect to database
            DataBaseSQLite db = new DataBaseSQLite();
            // get ResultSet as ArrayList of ArrayList of Strings
            ResultSet rs = db.selectTableResultSet(sqlQuery);
            wipNanData.clear();
            // loop through the result set
            while(rs.next()){
                //add data from result set into WipNan data
                WipNan wipNanItem = new WipNan();
                wipNanItem.setReportDate(rs.getString("ReportDate"));
                wipNanItem.setLot(rs.getString("Lot"));
                wipNanItem.setOwner(rs.getString("Owner"));
                wipNanItem.setProduct(rs.getString("Product"));
                wipNanItem.setHoldFlag(rs.getString("HoldFlag"));
                wipNanItem.setWIP1(rs.getString("WIP1"));
                wipNanItem.setWIP2(rs.getString("WIP2"));
                wipNanItem.setShrink(rs.getString("Shrink"));
                wipNanItem.setFE_SITE(rs.getString("FE_SITE"));
                wipNanItem.setBasicType(rs.getString("BasicType"));
                wipNanItem.setPackage(rs.getString("Package"));
                wipNanItem.setStep(rs.getString("Step"));
                wipNanItem.setWorkCenter(rs.getString("WorkCenter"));
                wipNanItem.setSEQ(rs.getString("SEQ"));
                wipNanItem.setPercentage(rs.getString("Percentage"));
                wipNanItem.setReportFileName(rs.getString("ReportFileName"));
                wipNanData.add(wipNanItem);
            }
            logger.info("Table build done for wip nan.");
            System.out.println("Table build done for wip nan");  
            // enable multi-selection
            wipNanTable.getSelectionModel().setCellSelectionEnabled(true);
            wipNanTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            // enable copy/paste
            TableUtils.installCopyPasteHandler(wipNanTable);
        }catch(Exception e){
            e.printStackTrace();
            logger.error("Error on Building Data for wip nan.");
            System.out.println("Error on Building Data for wip nan");             
        }
    }
    
    /**
     * Build data for bar chart for nan wip from sql query
     * we connect to the database, run the sql query and get the result set
     * we fill the data into the table view observable list
     * @param sqlQuery 
     */
    public void buildDataForBarChartWipNan(String sqlQuery){
        try{
            // connect to database
            DataBaseSQLite db = new DataBaseSQLite();
            // get ResultSet as ArrayList of ArrayList of Strings
            ResultSet rs = db.selectTableResultSet(sqlQuery);
            wipNanBarChartData.clear();
            // loop through the result set
            while(rs.next()){
                //add data from result set into WipNan data
                WipNan wipNanItem = new WipNan();
                wipNanItem.setReportDate(rs.getString("ReportDate"));
                wipNanItem.setLot(rs.getString("Lot"));
                wipNanItem.setOwner(rs.getString("Owner"));
                wipNanItem.setProduct(rs.getString("Product"));
                wipNanItem.setHoldFlag(rs.getString("HoldFlag"));
                wipNanItem.setWIP1(rs.getString("WIP1"));
                wipNanItem.setWIP2(rs.getString("WIP2"));
                wipNanItem.setShrink(rs.getString("Shrink"));
                wipNanItem.setFE_SITE(rs.getString("FE_SITE"));
                wipNanItem.setBasicType(rs.getString("BasicType"));
                wipNanItem.setPackage(rs.getString("Package"));
                wipNanItem.setStep(rs.getString("Step"));
                wipNanItem.setWorkCenter(rs.getString("WorkCenter"));
                wipNanItem.setSEQ(rs.getString("SEQ"));
                wipNanItem.setPercentage(rs.getString("Percentage"));
                wipNanItem.setReportFileName(rs.getString("ReportFileName"));
                wipNanBarChartData.add(wipNanItem);
            }
            logger.info("Data build for bar chart done for wip nan.");
            System.out.println("Data build for bar chart done for wip nan");  
        }catch(Exception e){
            e.printStackTrace();
            logger.error("Error on Building Data for bar chart wip nan.");
            System.out.println("Error on Building Data for bar chart wip nan.");             
        }
    }
    
    /**
     * Build data for line chart for nan wip from sql query
     * we connect to the database, run the sql query and get the result set
     * we fill the data into the table view observable list
     * @param sqlQuery 
     */
    public void buildDataForLineChartWipNan(String sqlQuery){
        try{
            // connect to database
            DataBaseSQLite db = new DataBaseSQLite();
            // get ResultSet as ArrayList of ArrayList of Strings
            ResultSet rs = db.selectTableResultSet(sqlQuery);
            wipNanLineChartData.clear();
            // loop through the result set
            while(rs.next()){
                //add data from result set into WipNan data
                WipNan wipNanItem = new WipNan();
                wipNanItem.setReportDate(rs.getString("ReportDate"));
                wipNanItem.setLot(rs.getString("Lot"));
                wipNanItem.setOwner(rs.getString("Owner"));
                wipNanItem.setProduct(rs.getString("Product"));
                wipNanItem.setHoldFlag(rs.getString("HoldFlag"));
                wipNanItem.setWIP1(rs.getString("WIP1"));
                wipNanItem.setWIP2(rs.getString("WIP2"));
                wipNanItem.setShrink(rs.getString("Shrink"));
                wipNanItem.setFE_SITE(rs.getString("FE_SITE"));
                wipNanItem.setBasicType(rs.getString("BasicType"));
                wipNanItem.setPackage(rs.getString("Package"));
                wipNanItem.setStep(rs.getString("Step"));
                wipNanItem.setWorkCenter(rs.getString("WorkCenter"));
                wipNanItem.setSEQ(rs.getString("SEQ"));
                wipNanItem.setPercentage(rs.getString("Percentage"));
                wipNanItem.setReportFileName(rs.getString("ReportFileName"));
                wipNanLineChartData.add(wipNanItem);
            }
            logger.info("Data build for line chart done for wip nan.");
            System.out.println("Data build for line chart done for wip nan");  
        }catch(Exception e){
            e.printStackTrace();
            logger.error("Error on Building Data for line chart wip nan.");
            System.out.println("Error on Building Data for line chart wip nan.");             
        }
    }
    
    /**
     * Build table view data for gf to nan from sql query
     * we connect to the database, run the sql query and get the result set
     * we fill the data into the table view observable list
     * @param sqlQuery 
     */
    public void buildTableGFtoNan(String sqlQuery){
        try{
            // connect to database
            DataBaseSQLite db = new DataBaseSQLite();
            // get ResultSet as ArrayList of ArrayList of Strings
            ResultSet rs = db.selectTableResultSet(sqlQuery);
            GFtoNanData.clear();
            // loop through the result set
            while(rs.next()){
                //add data from result set into WipNan data
                GFtoNan GFtoNanItem = new GFtoNan();
                GFtoNanItem.setReportDate(rs.getString("ReportDate"));
                GFtoNanItem.setCustomerName(rs.getString("CustomerName"));
                GFtoNanItem.setShipDate(rs.getString("ShipDate"));
                GFtoNanItem.setInvoiceNo(rs.getString("InvoiceNo"));
                GFtoNanItem.setInvoiceDate(rs.getString("InvoiceDate"));
                GFtoNanItem.setPO(rs.getString("PO"));
                GFtoNanItem.setSO(rs.getString("SO"));
                GFtoNanItem.setOrderDate(rs.getString("OrderDate"));
                GFtoNanItem.setProcessName(rs.getString("ProcessName"));
                GFtoNanItem.setCustomerPartname(rs.getString("CustomerPartname"));
                GFtoNanItem.setInternalPartName(rs.getString("InternalPartName"));
                GFtoNanItem.setLotId(rs.getString("LotId"));
                GFtoNanItem.setCustomerLotId(rs.getString("CustomerLotId"));
                GFtoNanItem.setLotType(rs.getString("LotType"));
                GFtoNanItem.setLotPriority(rs.getString("LotPriority"));
                GFtoNanItem.setAgreedGDPW(rs.getString("AgreedGDPW"));
                GFtoNanItem.setCalculatedGDPL(rs.getString("CalculatedGDPL"));
                GFtoNanItem.setCycleTime(rs.getString("CycleTime"));
                GFtoNanItem.setBillQtyWfr(rs.getString("BillQtyWfr"));
                GFtoNanItem.setWfrIds(rs.getString("WfrIds"));
                GFtoNanItem.setvBillDie(rs.getString("vBillDie"));
                GFtoNanItem.setShipToLocation(rs.getString("ShipToLocation"));
                GFtoNanItem.setBillToLocation(rs.getString("BillToLocation"));
                GFtoNanItem.setETA(rs.getString("ETA"));
                GFtoNanItem.setETD(rs.getString("ETD"));
                GFtoNanItem.setForwarder(rs.getString("Forwarder"));
                GFtoNanItem.setHAWB(rs.getString("HAWB"));
                GFtoNanItem.setMAWB(rs.getString("MAWB"));
                GFtoNanItem.setFlightNo(rs.getString("FlightNo"));
                GFtoNanItem.setConnectingFlightNo(rs.getString("ConnectingFlightNo"));
                GFtoNanItem.setReportFileName(rs.getString("ReportFileName"));                   
                GFtoNanData.add(GFtoNanItem);
            }
            logger.info("Table build done for gf to nan.");
            System.out.println("Table build done for gf to nan"); 
            // enable multi-selection
            GFtoNanTable.getSelectionModel().setCellSelectionEnabled(true);
            GFtoNanTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            // enable copy/paste
            TableUtils.installCopyPasteHandler(GFtoNanTable);
        }catch(Exception e){
            e.printStackTrace();
            logger.error("Error on Building Data for gf to nan.");
            System.out.println("Error on Building Data for gf to nan");             
        }
    }
    
    /**
     * Build table view data for tsmc to nan from sql query
     * we connect to the database, run the sql query and get the result set
     * we fill the data into the table view observable list
     * @param sqlQuery 
     */
    public void buildTableTSMCtoNan(String sqlQuery){
        try{
            // connect to database
            DataBaseSQLite db = new DataBaseSQLite();
            // get ResultSet as ArrayList of ArrayList of Strings
            ResultSet rs = db.selectTableResultSet(sqlQuery);
            TSMCtoNanData.clear();
            // loop through the result set
            while(rs.next()){
                //add data from result set into WipNan data
                TSMCtoNan TSMCtoNanItem = new TSMCtoNan();
                TSMCtoNanItem.setLot(rs.getString("Lot"));
                TSMCtoNanItem.setWaferPcs(rs.getString("WaferPcs"));
                TSMCtoNanItem.setInvoiceNo(rs.getString("InvoiceNo"));
                TSMCtoNanItem.setInvoiceDate(rs.getString("InvoiceDate"));
                TSMCtoNanItem.setForwarder(rs.getString("Forwarder"));
                TSMCtoNanItem.setMAWB(rs.getString("MAWB"));
                TSMCtoNanItem.setHAWB(rs.getString("HAWB"));
                TSMCtoNanItem.setReportFileName(rs.getString("ReportFileName"));
                TSMCtoNanItem.setTechnology(rs.getString("Technology"));
                TSMCtoNanItem.setProduct(rs.getString("Product"));
                TSMCtoNanItem.setProduct2000(rs.getString("Product2000"));
                TSMCtoNanItem.setETA(rs.getString("ETA"));
                TSMCtoNanData.add(TSMCtoNanItem);
            }
            System.out.println("Table build done for tsmc to nan"); 
            // enable multi-selection
            TSMCtoNanTable.getSelectionModel().setCellSelectionEnabled(true);
            TSMCtoNanTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            // enable copy/paste
            TableUtils.installCopyPasteHandler(TSMCtoNanTable);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data for tsmc to nan");             
        }
    }
    
    /**
     * Build table view data for umci to nan from sql query
     * we connect to the database, run the sql query and get the result set
     * we fill the data into the table view observable list
     * @param sqlQuery 
     */
    public void buildTableUMCItoNan(String sqlQuery){
        try{
            // connect to database
            DataBaseSQLite db = new DataBaseSQLite();
            // get ResultSet as ArrayList of ArrayList of Strings
            ResultSet rs = db.selectTableResultSet(sqlQuery);
            UMCItoNanData.clear();
            // loop through the result set
            while(rs.next()){
                //add data from result set into WipNan data
                UMCItoNan UMCItoNanItem = new UMCItoNan();
                UMCItoNanItem.setPART_DIV(rs.getString("PART_DIV"));
                UMCItoNanItem.setINV_NO(rs.getString("INV_NO"));
                UMCItoNanItem.setSHPTO_ID(rs.getString("SHPTO_ID"));
                UMCItoNanItem.setINV_DATE(rs.getString("INV_DATE"));
                UMCItoNanItem.setMAWB_NO(rs.getString("MAWB_NO"));
                UMCItoNanItem.setHAWB_NO(rs.getString("HAWB_NO"));
                UMCItoNanItem.setFLT_NO(rs.getString("FLT_NO"));
                UMCItoNanItem.setFLT_DATE(rs.getString("FLT_DATE"));
                UMCItoNanItem.setFLT_DEST(rs.getString("FLT_DEST"));
                UMCItoNanItem.setCARTON_NO(rs.getString("CARTON_NO"));
                UMCItoNanItem.setPO_NO(rs.getString("PO_NO"));
                UMCItoNanItem.setPRD_NO(rs.getString("PRD_NO"));
                UMCItoNanItem.setLOT_TYPE(rs.getString("LOT_TYPE"));
                UMCItoNanItem.setLOT_NO(rs.getString("LOT_NO"));
                UMCItoNanItem.setSHIP_W_QTY(rs.getString("SHIP_W_QTY"));
                UMCItoNanItem.setSHIP_D_QTY(rs.getString("SHIP_D_QTY"));
                UMCItoNanItem.setSHP_PRD_NO(rs.getString("SHP_PRD_NO"));
                UMCItoNanItem.setCTM_DEVICE(rs.getString("CTM_DEVICE"));
                UMCItoNanItem.setCUSTOMER_LOT(rs.getString("CUSTOMER_LOT"));
                UMCItoNanItem.setUMC_INV_NO(rs.getString("UMC_INV_NO"));
                UMCItoNanItem.setREMARK(rs.getString("REMARK"));
                UMCItoNanItem.setWAFER_NO(rs.getString("WAFER_NO"));
                UMCItoNanItem.setReportFileName(rs.getString("ReportFileName"));
                UMCItoNanData.add(UMCItoNanItem);
            }
            System.out.println("Table build done for umci to nan"); 
            // enable multi-selection
            UMCItoNanTable.getSelectionModel().setCellSelectionEnabled(true);
            UMCItoNanTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            // enable copy/paste
            TableUtils.installCopyPasteHandler(UMCItoNanTable);
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("Error on Building Data for umci to nan");             
        }
    }
    
    /**
     * Filter data based on combobox and field values
     * @param sqlQuery 
     */
    public void filterLotData(String sqlQuery) {
        System.out.println("Start filter query for lot data");
        buildTableLotData(sqlQuery);
    }
    
    /**
     * Filter data based on combobox and field values
     * @param sqlQuery 
     */
    public void filterWipNanData(String sqlQuery) {
        System.out.println("Start filter query for wip nan");
        buildTableWipNan(sqlQuery);
    }
    
    /**
     * Filter data based on combobox and field values
     * @param sqlQuery 
     */
    public void filterGFtoNanData(String sqlQuery) {
        System.out.println("Start filter query for gf to nan");
        buildTableGFtoNan(sqlQuery);
    }
    
    /**
     * Filter data based on combobox and field values
     * @param sqlQuery 
     */
    public void filterTSMCtoNanData(String sqlQuery) {
        System.out.println("Start filter query for tsmc to nan");
        buildTableTSMCtoNan(sqlQuery);
    }
    
    /**
     * Filter data based on combobox and field values
     * @param sqlQuery 
     */
    public void filterUMCItoNanData(String sqlQuery) {
        System.out.println("Start filter query for umci to nan");
        buildTableUMCItoNan(sqlQuery);
    }
    
    /**
     * Constructor for a new runnable to run task in background
     */
    public void backroundUpdateTask() {
        System.out.println("Start Background Update Task"); 
        // Create a Runnable
        Runnable task = new Runnable()
        {
            public void run()
            {
                // we call the update/import task    
                runUpdateTask();  
            }
        };

        // Run the task in a background thread
        Thread backgroundThread = new Thread(task);
        // Terminate the running thread if the application exits
        backgroundThread.setDaemon(true);
        // Start the thread
        backgroundThread.start();
    }
    
    /**
     * Run data import task in background to update database
     * we check all folders, extract the files from emails and read the files
     * all results are imported in the database
     */
    public void runUpdateTask() {
        try {
            System.out.println("Run Background Task for data update"); 
            // disable the button
            MenuBar.setDisable(true);

            // Update the Label on the JavaFx Application Thread		
            Platform.runLater(new Runnable() {
                @Override 
                public void run() {
                    //updateLabel.setText("Processing import.");
                }
            });
            
            // run the report import
            ReportImport repimp = new ReportImport();
            repimp.runReportImport();
            // run the lot data update
            LotDataUpdate lotup = new LotDataUpdate();
            lotup.lotDataUpdate();

            // Update the Label on the JavaFx Application Thread		
            Platform.runLater(new Runnable() {
                @Override 
                public void run() {
                    //updateLabel.setText("Finished import.");
                }
            });
            // enable the button again
            MenuBar.setDisable(false);
            System.out.println("Finished Background Task for data update"); 
        } catch (Exception e){
            //
        }
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
     * class for redirecting the system out to the text area console
     */
    public class Console extends OutputStream {
        private TextArea console;
        
        public Console(TextArea console){
            this.console = console;
        }

        public void appendText(String valueOf){
            Platform.runLater(() -> console.appendText(valueOf));
        }
        
        public void write(int b) throws IOException {
            appendText(String.valueOf((char)b));
        }
    }
}
