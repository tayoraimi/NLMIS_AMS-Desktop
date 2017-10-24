package com.chai.inv;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import com.chai.inv.logger.MyLogger;
import com.chai.inv.model.CCECapRiBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.service.AssetDashboardService;
import com.chai.inv.service.UserService;
import java.sql.SQLException;
import java.util.Date;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class CCECapRiDashboardController {
        
        
        @FXML
        private TableView<CCECapRiBean> cceCapRiTable;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiTotalVolumeColumn;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiAt2cTo8cColumn;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiSupplyIntervalForSafeInjectionEquiptColumn;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiContigencyFactorColumn;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiOthersInterventionsColumn;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiAt25cTo15c;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiScheduleVaccinesColumn;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiSupplyChainLevelColumn;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiAnnualBirthRateColumn;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiMaximumStockColumn;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiAt25cTo15c5Column;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiAt2cTo8c4Column;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiIfMoreThan3MonthsColumn;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiAt2cTo8c2Column;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiSafetyStockColumn;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiAt25cTo15c3Column;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiFacilityNameColumn;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiStateColumn;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiWardColumn;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiLocationColumn;
        @FXML
        private TableColumn<CCECapRiBean, String> cceCapRiLgaColumn;
        @FXML
        private ComboBox<String> x_CCE_CAP_RI_LEVEL;
        @FXML
        private ComboBox<String> x_CCE_CAP_RI_VACCINE;
	@FXML
	private VBox x_VBOX;
	@FXML
	private ToolBar x_FILTER_TOOLBAR;
        

	@FXML
	private Label x_ROW_COUNT;
	@FXML
	private ToolBar x_TOOLBAR;
        @FXML
        private CheckBox x_CCE_CAP_RI_HIDE_COLUMNS;
        
        
	private MainApp mainApp;
	private AssetDashboardService cceCapRiService;
	private RootLayoutController rootLayoutController;
	private CCECapRiBean cceCapRiBean;
	private Stage primaryStage;
	private ObservableList<CCECapRiBean> list;
	private String actionBtnString;
	private UserBean userBean;
	private LabelValueBean warehouseLbv;
	private LabelValueBean role;
	private HomePageController homePageController;
        private String[] splittedValue;

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setRootLayoutController(
			RootLayoutController rootLayoutController) {
		this.rootLayoutController = rootLayoutController;
		rootLayoutController.getX_ROOT_COMMON_LABEL().setText("CCE Antigens Capacity Dashboard");
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		cceCapRiService = new AssetDashboardService();
		//cceCapRiTable.setItems(cceCapRiService.getCCECapRiList());
                x_CCE_CAP_RI_VACCINE.getItems().addAll("Cap_RI","Men_A","Cap_Rota","Cap_MR","Cap_HPV");
                x_CCE_CAP_RI_VACCINE.setValue("Cap_RI");
		x_ROW_COUNT.setText("Row Count : "+cceCapRiTable.getItems().size());
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = new UserBean();
		this.userBean = userBean;
	}
	public void setCCECapRiListData() throws SQLException {

		cceCapRiService = new AssetDashboardService();

		list = cceCapRiService.getCCECapRiList(x_CCE_CAP_RI_VACCINE.getValue(), x_CCE_CAP_RI_LEVEL.getValue());
		cceCapRiTable.setItems(list);
		x_ROW_COUNT.setText("Row Count : "+cceCapRiTable.getItems().size());
	}
//	public void setTableVert() throws SQLException {
//            TableCellRenderer headerRenderer = new VerticalTableHeaderCellRenderer();
//            Enumeration columns = table.getColumnModel().getColumns();
//            while (columns.hasMoreElements()) {
//                columns.nextElement().
//                setHeaderRenderer(headerRenderer);
//            }
//        }


	@FXML
	private void initialize() {
            
            
                cceCapRiSupplyChainLevelColumn.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_SUPPLY_CHAIN_LEVEL"));
                cceCapRiStateColumn.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_STATE"));
                cceCapRiLgaColumn.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_LGA"));
                cceCapRiWardColumn.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_WARD"));
                cceCapRiFacilityNameColumn.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_FACILITY_NAME"));
                cceCapRiAnnualBirthRateColumn.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_ANNUAL_BIRTH"));
                cceCapRiSafetyStockColumn.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_SAFETY_STOCK"));
                cceCapRiSupplyIntervalForSafeInjectionEquiptColumn.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_SUPPLY_INTERVAL_FOR_SAFE_INJECTION_EQUIPT"));
                cceCapRiMaximumStockColumn.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_MAXIMUM_STOCK"));
                cceCapRiScheduleVaccinesColumn.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_SCHEDULE_VACCINES"));
                cceCapRiContigencyFactorColumn.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_CONTINGENCY_FACTOR"));
                cceCapRiOthersInterventionsColumn.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_OTHERS_INTERVENTIONS"));
                cceCapRiTotalVolumeColumn.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_TOTAL_VOLUME"));
                cceCapRiIfMoreThan3MonthsColumn.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_IF_MORE_THAN_3_MONTHS_STORAGE"));
                cceCapRiAt2cTo8cColumn.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_AT_2C_TO_8C"));
                cceCapRiAt25cTo15c.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_AT_25C_TO_15C"));
                cceCapRiAt2cTo8c2Column.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_AT_2C_TO_8C2"));
                cceCapRiAt25cTo15c3Column.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_AT_25C_TO_15C3"));
                cceCapRiAt2cTo8c4Column.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_AT_2C_TO_8C4"));
                cceCapRiAt25cTo15c5Column.setCellValueFactory(new PropertyValueFactory<CCECapRiBean, String>("x_CCE_CAP_RI_AT_25C_TO_15C5"));

		cceCapRiTable
				.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	public void setRole(LabelValueBean role) {
		this.role = role;
		switch (role.getLabel()) {
		case "LIO": // LIO - SUPER USER
//                    x_VBOX.getChildren().remove(x_FILTER_TOOLBAR);
                    x_CCE_CAP_RI_LEVEL.setDisable(true);
			break;
		case "MOH": // LIO - SUPER USER
                    x_CCE_CAP_RI_LEVEL.getItems().addAll("LGA","Ward");
			break;
		case "SIO": // SIO
                    x_CCE_CAP_RI_LEVEL.getItems().addAll("LGA","Ward");
			break;
		case "SCCO": // SCCO
                    x_CCE_CAP_RI_LEVEL.getItems().addAll("LGA","Ward");
			break;
		case "SIFP": // SIFP
                    x_CCE_CAP_RI_LEVEL.getItems().addAll("LGA","Ward");
			break;
		case "CCO": // CCO - EMPLOYEE
//                    x_VBOX.getChildren().remove(x_FILTER_TOOLBAR);√
                    x_CCE_CAP_RI_LEVEL.setDisable(true);
			break;
		}
	}

	@FXML
	void handleOnLevelSelected() {
		System.out.println("**In CustomerMainController.handleOnStateSelected() listener **");
		if (x_CCE_CAP_RI_LEVEL.getValue() != null) {
			cceCapRiTable.setItems(cceCapRiService.getCCECapRiList(x_CCE_CAP_RI_VACCINE.getValue(),x_CCE_CAP_RI_LEVEL.getValue()));
			x_ROW_COUNT.setText("Row Count : "+ cceCapRiTable.getItems().size());
		}
	}
        
        @FXML
	void handleOnVaccineSelected() {
		System.out.println("**In CustomerMainController.handleOnStateSelected() listener **");
		if (x_CCE_CAP_RI_VACCINE.getValue() != null) {
			cceCapRiTable.setItems(cceCapRiService.getCCECapRiList(x_CCE_CAP_RI_VACCINE.getValue(),x_CCE_CAP_RI_LEVEL.getValue()));
			x_ROW_COUNT.setText("Row Count : "+ cceCapRiTable.getItems().size());
		}
	}
        
        public void refreshCCECapRiTable() {
		System.out
				.println("In CCECapRiStatusDashboardcontroller.refreshCCECapRiTable() method: ");
		int selectedIndex = cceCapRiTable.getSelectionModel()
				.getSelectedIndex();
		cceCapRiTable.setItems(null);
		cceCapRiTable.layout();
		cceCapRiTable.setItems(cceCapRiService.getCCECapRiList(x_CCE_CAP_RI_VACCINE.getValue(),x_CCE_CAP_RI_LEVEL.getValue()));
		cceCapRiTable.getSelectionModel().select(selectedIndex);
		x_ROW_COUNT.setText("Row Count : "+cceCapRiTable.getItems().size());
	}

	public void refreshCCECapRiTable(ObservableList<CCECapRiBean> list) {
		System.out.println("in refreshCCECapRiTable(list)");
		if (list == null) {
			org.controlsfx.dialog.Dialogs.create().owner(getPrimaryStage())
					.title("Information").masthead("Search Result")
					.message("Record(s) not found!").showInformation();
		} else {
			cceCapRiTable.setItems(list);
			org.controlsfx.dialog.Dialogs.create().owner(getPrimaryStage())
					.title("Information").masthead("Search Result")
					.message(list.size() + " Record(s) found!")
					.showInformation();
		}
	}


	@FXML
	public boolean handleSearchAction() {
		
                return false;
	}


	// public void setwarehouseLvb(LabelValueBean warehouseLbv) {
	// this.warehouseLbv = warehouseLbv;
	// x_USER_WAREHOUSE_NAME.setText("Warehouse: "+warehouseLbv.getLabel());
	// }
        @FXML
	public void handleHideCheckBox() {
		System.out.println("entered handleHideCheckBox()");
		if(!x_CCE_CAP_RI_HIDE_COLUMNS.isSelected()){
//                    cceCapRiOFColumn.setVisible(false);
//                    cceCapRiONFColumn.setVisible(false);
                }
                else{
//                    cceCapRiOFColumn.setVisible(true);
//                    cceCapRiONFColumn.setVisible(true);
                }
	}
        
	@FXML
	public void handleHomeDashBoardBtn() {
		System.out.println("entered handleHomeDashBoardBtn()");
		rootLayoutController.handleHomeMenuAction();
	}

	public void setHomePageController(HomePageController homePageController) {
		this.homePageController = homePageController;
	}
}