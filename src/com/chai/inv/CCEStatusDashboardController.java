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
import com.chai.inv.model.CCEStatusBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.service.AssetDashboardService;
import com.chai.inv.service.UserService;
import java.sql.SQLException;
import java.util.Date;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class CCEStatusDashboardController {
        
        
        @FXML
        private TableView<CCEStatusBean> cceStatusTable;

        @FXML
        private TableColumn<CCEStatusBean, String> cceStatusNFColumn;
        @FXML
        private TableColumn<CCEStatusBean, String> cceStatusOFColumn;
        @FXML
        private TableColumn<CCEStatusBean, String> cceStatusONFColumn;
//        @FXML
//        private TableColumn<CCEStatusBean, String> cceStatusFacilityNameColumn;
        @FXML
        private TableColumn<CCEStatusBean, String> cceStatusStateColumn;
        @FXML
        private TableColumn<CCEStatusBean, String> cceStatusWardColumn;
        @FXML
        private TableColumn<CCEStatusBean, String> cceStatusLocationColumn;
        @FXML
        private TableColumn<CCEStatusBean, String> cceStatusFColumn;
        @FXML
        private TableColumn<CCEStatusBean, String> cceStatusLgaColumn;
        @FXML
        private TableColumn<CCEStatusBean, String> cceStatusTotalColumn;
        @FXML
        private TableColumn<CCEStatusBean, String> cceStatusNIColumn;
        @FXML
        private ComboBox<String> x_CCE_STATUS_LEVEL;
	@FXML
	private VBox x_VBOX;
	@FXML
	private ToolBar x_FILTER_TOOLBAR;
        

	@FXML
	private Label x_ROW_COUNT;
	@FXML
	private ToolBar x_TOOLBAR;
        @FXML
        private CheckBox x_CCE_STATUS_HIDE_COLUMNS;
        
	private MainApp mainApp;
	private AssetDashboardService cceStatusService;
	private RootLayoutController rootLayoutController;
	private CCEStatusBean facilityBean;
	private Stage primaryStage;
	private ObservableList<CCEStatusBean> list;
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
		rootLayoutController.getX_ROOT_COMMON_LABEL().setText("CCE Status Dashboard");
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		cceStatusService = new AssetDashboardService();
		//cceStatusTable.setItems(cceStatusService.getCCEStatusList());
		x_ROW_COUNT.setText("Row Count : "+cceStatusTable.getItems().size());
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = new UserBean();
		this.userBean = userBean;
	}
	public void setCCEStatusListData() throws SQLException {

		cceStatusService = new AssetDashboardService();

		list = cceStatusService.getCCEStatusList("");
		cceStatusTable.setItems(list);
		x_ROW_COUNT.setText("Row Count : "+cceStatusTable.getItems().size());
	}


	@FXML
	private void initialize() {
            
            
                cceStatusStateColumn.setCellValueFactory(new PropertyValueFactory<CCEStatusBean, String>("x_CCE_STATUS_STATE"));
                cceStatusWardColumn.setCellValueFactory(new PropertyValueFactory<CCEStatusBean, String>("x_CCE_STATUS_WARD"));
//                cceStatusFacilityNameColumn.setCellValueFactory(new PropertyValueFactory<CCEStatusBean, String>("x_CCE_STATUS_FACILITY_NAME"));
                cceStatusLocationColumn.setCellValueFactory(new PropertyValueFactory<CCEStatusBean, String>("x_CCE_STATUS_LOCATION"));
                cceStatusLgaColumn.setCellValueFactory(new PropertyValueFactory<CCEStatusBean, String>("x_CCE_STATUS_LGA"));             
                cceStatusNFColumn.setCellValueFactory(new PropertyValueFactory<CCEStatusBean, String>("x_CCE_STATUS_NF"));
                cceStatusNFColumn.setCellFactory(column -> {
			return new TableCell<CCEStatusBean, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					int itemNumber;
					if (item == null || empty) {
						setText(item);
						//setStyle("");
					} else {
						// Format date.
						setText(item);
						itemNumber = Integer.parseInt(item);
						// Style all dates in March with a different color.
						if (itemNumber==0) {
							setTextFill(Color.BLACK);
							setStyle("-fx-background-color: green");
						} else if (itemNumber>=1){
							setTextFill(Color.BLACK);
							setStyle("-fx-background-color: red");
						}
					}
				}
			};
		});
                cceStatusOFColumn.setCellValueFactory(new PropertyValueFactory<CCEStatusBean, String>("x_CCE_STATUS_OF"));
                cceStatusONFColumn.setCellValueFactory(new PropertyValueFactory<CCEStatusBean, String>("x_CCE_STATUS_ONF"));
                cceStatusFColumn.setCellValueFactory(new PropertyValueFactory<CCEStatusBean, String>("x_CCE_STATUS_F"));
                cceStatusFColumn.setCellFactory(column -> {
			return new TableCell<CCEStatusBean, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					float itemNumber;
					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						// Format date.
                                                splittedValue = item.split("/");
						setText(splittedValue[0]);
						itemNumber = Float.valueOf(splittedValue[1]);
						// Style all dates in March with a different color.
						if (itemNumber>=90.0) {
							setTextFill(Color.BLACK);
							setStyle("-fx-background-color: green");
						} else if ((itemNumber>=50.0)&&(itemNumber<90.0)){
							setTextFill(Color.BLACK);
							setStyle("-fx-background-color: yellow");
						} else if (itemNumber<50.0){
							setTextFill(Color.BLACK);
							setStyle("-fx-background-color: red");
						}
					}
				}
			};
		});
                cceStatusNIColumn.setCellValueFactory(new PropertyValueFactory<CCEStatusBean, String>("x_CCE_STATUS_NI"));
                cceStatusNIColumn.setCellFactory(column -> {
			return new TableCell<CCEStatusBean, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					int itemNumber;
					if (item == null || empty) {
						setText(item);
						//setStyle("");
					} else {
						// Format date.
						setText(item);
						itemNumber = Integer.parseInt(item);
						if (itemNumber==0) {
							setTextFill(Color.BLACK);
							setStyle("-fx-background-color: green");
						} else if (itemNumber>=1){
							setTextFill(Color.BLACK);
							setStyle("-fx-background-color: red");
						}
					}
				}
			};
		});
                cceStatusTotalColumn.setCellValueFactory(new PropertyValueFactory<CCEStatusBean, String>("x_CCE_STATUS_TOTAL")); 

		cceStatusTable
				.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	public void setRole(LabelValueBean role) {
		this.role = role;
		switch (role.getLabel()) {
		case "LIO": // LIO - SUPER USER
                    x_VBOX.getChildren().remove(x_FILTER_TOOLBAR);
			break;
		case "MOH": // LIO - SUPER USER
                    x_CCE_STATUS_LEVEL.getItems().addAll("LGA","Ward");
			break;
		case "SIO": // SIO
                    x_CCE_STATUS_LEVEL.getItems().addAll("LGA","Ward");
			break;
		case "SCCO": // SCCO
                    x_CCE_STATUS_LEVEL.getItems().addAll("LGA","Ward");
			break;
		case "SIFP": // SIFP
                    x_CCE_STATUS_LEVEL.getItems().addAll("LGA","Ward");
			break;
		case "CCO": // CCO - EMPLOYEE
                    x_VBOX.getChildren().remove(x_FILTER_TOOLBAR);
			break;
		}
	}

	@FXML
	void handleOnLevelSelected() {
		System.out.println("**In CustomerMainController.handleOnStateSelected() listener **");
		if (x_CCE_STATUS_LEVEL.getValue() != null) {
			cceStatusTable.setItems(cceStatusService.getCCEStatusList(x_CCE_STATUS_LEVEL.getValue()));
			x_ROW_COUNT.setText("Row Count : "+ cceStatusTable.getItems().size());
		}
	}
        
        public void refreshCCEStatusTable() {
		System.out
				.println("In CCEStatusStatusDashboardcontroller.refreshCCEStatusTable() method: ");
		int selectedIndex = cceStatusTable.getSelectionModel()
				.getSelectedIndex();
		cceStatusTable.setItems(null);
		cceStatusTable.layout();
		cceStatusTable.setItems(cceStatusService.getCCEStatusList(""));
		cceStatusTable.getSelectionModel().select(selectedIndex);
		x_ROW_COUNT.setText("Row Count : "+cceStatusTable.getItems().size());
	}

	public void refreshCCEStatusTable(ObservableList<CCEStatusBean> list) {
		System.out.println("in refreshCCETable(list)");
		if (list == null) {
			org.controlsfx.dialog.Dialogs.create().owner(getPrimaryStage())
					.title("Information").masthead("Search Result")
					.message("Record(s) not found!").showInformation();
		} else {
			cceStatusTable.setItems(list);
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
		if(!x_CCE_STATUS_HIDE_COLUMNS.isSelected()){
                    cceStatusOFColumn.setVisible(false);
                    cceStatusONFColumn.setVisible(false);
                }
                else{
                    cceStatusOFColumn.setVisible(true);
                    cceStatusONFColumn.setVisible(true);
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