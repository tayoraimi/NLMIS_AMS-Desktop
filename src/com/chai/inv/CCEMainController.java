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
import com.chai.inv.model.CCEBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.service.CCEService;
import com.chai.inv.service.UserService;
import java.sql.SQLException;
import java.util.Date;
import javafx.scene.control.CheckBox;

public class CCEMainController {
        
        @FXML
        private TableColumn<CCEBean, String> cceStateColumn;
        @FXML
        private TableColumn<CCEBean, String> cceWardColumn;
        @FXML
        private TableColumn<CCEBean, String> cceFacilityNameColumn;
        @FXML
        private TableColumn<CCEBean, String> cceSerialNoColumn;
        @FXML
        private TableColumn<CCEBean, String> cceDateNFColumn;
        @FXML
        private TableColumn<CCEBean, String> cceModelColumn;
        @FXML
        private TableColumn<CCEBean, String> cceLocationColumn;
        @FXML
        private TableColumn<CCEBean, String> cceSummaryColumn;
        @FXML
        private TableView<CCEBean> cceTable;
        @FXML
        private TableColumn<CCEBean, String> cceVolPosColumn;
        @FXML
        private TableColumn<CCEBean, String> cceLgaColumn;
        @FXML
        private TableColumn<CCEBean, String> cceDesignationColumn;
        @FXML
        private TableColumn<CCEBean, String> cceVolNegColumn;
        @FXML
        private TableColumn<CCEBean, String> cceRefrigerantColumn;
        @FXML
        private TableColumn<CCEBean, String> cceEnergyColumn;
        @FXML
        private TableColumn<CCEBean, String> cceSourceColumn;
        @FXML
        private TableColumn<CCEBean, String> cceMakeColumn;
        @FXML
        private TableColumn<CCEBean, String> cceYearOfAcquisitionColumn;
        @FXML
        private TableColumn<CCEBean, String> cceExpectedWorkingLifeColumn;


	@FXML
	private Button x_ADD_CCE_BTN;
	@FXML
	private Button x_EDIT_CCE_BTN;
	@FXML
	private Label x_ROW_COUNT;
	@FXML
	private ToolBar x_TOOLBAR;
        @FXML
        private CheckBox x_CCE_HIDE_COLUMNS;
        
	private MainApp mainApp;
	private CCEService cceService;
	private RootLayoutController rootLayoutController;
	private Stage primaryStage;
	private ObservableList<CCEBean> list;
	private String actionBtnString;
	private UserBean userBean;
	private LabelValueBean warehouseLbv;
	private LabelValueBean role;
	private HomePageController homePageController;

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setRootLayoutController(
			RootLayoutController rootLayoutController) {
		this.rootLayoutController = rootLayoutController;
		rootLayoutController.getX_ROOT_COMMON_LABEL().setText("Update and New CCE");
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		cceService = new CCEService();
		cceTable.setItems(cceService.getCCEList());
		x_ROW_COUNT.setText("Row Count : "+cceTable.getItems().size());
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = new UserBean();
		this.userBean = userBean;
	}
	public void setCCEListData() throws SQLException {

		cceService = new CCEService();

		list = cceService.getCCEList();
		cceTable.setItems(list);
		x_ROW_COUNT.setText("Row Count : "+cceTable.getItems().size());
	}


	@FXML
	private void initialize() {
            
//                cceStateColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_STATE"));
//                cceLgaColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_LGA"));
                cceWardColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_WARD"));
                cceFacilityNameColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_FACILITY_NAME"));
                cceSerialNoColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_SERIAL_NO"));
                cceDateNFColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_DATE_NF"));
                cceModelColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_MODEL"));
                cceLocationColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_LOCATION"));
                cceSummaryColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_SUMMARY"));
//                cceSummaryColumn.setCellValueFactory(cellData -> cellData.getValue().summaryProperty());
                cceSummaryColumn.setCellFactory(column -> {
			return new TableCell<CCEBean, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					
					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						
						setText(item);
						
						// Style all dates in March with a different color.
						if (item.endsWith("Functional-Obsolete")||item.endsWith("Not Functional-Obsolete")) {
							setTextFill(Color.BLACK);
							setStyle("-fx-background-color: red");
						} else if (item.endsWith("Not Installed")||item.endsWith("Functional-Good")){
							setTextFill(Color.BLACK);
							setStyle("-fx-background-color: green");
						} else{
							setTextFill(Color.BLACK);
							setStyle("-fx-background-color: yellow");
						}
					}
				}
			};
		});
                cceVolPosColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_VOL_POS"));
                cceDesignationColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_DESIGNATION"));
                cceVolNegColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_VOL_NEG"));
                cceRefrigerantColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_REFRIGERANT"));
                cceEnergyColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_ENERGY"));
                cceSourceColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_SOURCE"));
                cceMakeColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_MAKE"));
                cceYearOfAcquisitionColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_MONTHYEAR_OF_ACQUISITION"));
                cceExpectedWorkingLifeColumn.setCellValueFactory(new PropertyValueFactory<CCEBean, String>("x_CCE_EXPECTED_WORKING_LIFE"));
                
		cceTable
				.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	public void setRole(LabelValueBean role) {
		this.role = role;
		switch (role.getLabel()) {
		case "LIO": // LIO - SUPER USER
			break;
		case "MOH": // LIO - SUPER USER
			break;
		case "SIO": // SIO
			break;
		case "SCCO": // SCCO
			break;
		case "SIFP": // SIFP
			break;
		case "CCO": // CCO - EMPLOYEE
			break;
		}
	}

	public void refreshCCETable() {
		System.out
				.println("In CCEMaincontroller.refreshCCETable() method: ");
		int selectedIndex = cceTable.getSelectionModel()
				.getSelectedIndex();
		cceTable.setItems(null);
		cceTable.layout();
		cceTable.setItems(cceService.getCCEList());
		cceTable.getSelectionModel().select(selectedIndex);
		x_ROW_COUNT.setText("Row Count : "+cceTable.getItems().size());
	}

	public void refreshCCETable(ObservableList<CCEBean> list) {
		System.out.println("in refreshCCETable(list)");
		if (list == null) {
			org.controlsfx.dialog.Dialogs.create().owner(getPrimaryStage())
					.title("Information").masthead("Search Result")
					.message("Record(s) not found!").showInformation();
		} else {
			cceTable.setItems(list);
			org.controlsfx.dialog.Dialogs.create().owner(getPrimaryStage())
					.title("Information").masthead("Search Result")
					.message(list.size() + " Record(s) found!")
					.showInformation();
		}
	}

	@FXML
	public boolean handleAddAction() {
		System.out.println("Hey We are in Add CCE Action Handler");
		FXMLLoader loader = new FXMLLoader(
				MainApp.class
						.getResource("/com/chai/inv/view/AddCCEData.fxml"));
		try {
			// Load the fxml file and create a new stage for the popup
			AnchorPane cceAddEditDialog = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add CCE Form");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(cceAddEditDialog);
			dialogStage.setScene(scene);
			CCEEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCCEMain(this);
			controller.setUserBean(userBean);
                        controller.setRole(role);
			controller.setCCEService(cceService, "add");
			controller.setCCEBeanFields(new CCEBean(),
					new LabelValueBean("Select CCE Type", "0"),
					new LabelValueBean("Select Country", "0"),
					new LabelValueBean("Select State", "0"));
			// Show the dialog and wait until the user closes it
			dialogStage.showAndWait();
			return controller.isOkClicked();
		} catch (IOException e) {
			// Exception gets thrown if the fxml file could not be loaded
			e.printStackTrace();
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe(MyLogger.getStackTrace(e));
			return false;
		}
	}

	@FXML
	public boolean handleEditAction() {
		System.out.println("In Edit Action Handler");
		CCEBean selectedCCEBean = cceTable.getSelectionModel()
				.getSelectedItem();
		if (selectedCCEBean != null) {
			LabelValueBean selectedStateLabelValueBean = new LabelValueBean(
					selectedCCEBean.getX_CCE_STATE(),
					selectedCCEBean.getX_CCE_STATE_ID());
			LabelValueBean selectedLGALabelValueBean = new LabelValueBean(
					selectedCCEBean.getX_CCE_LGA(),
					selectedCCEBean.getX_CCE_LGA_ID());
			LabelValueBean selectedWardLabelValueBean = new LabelValueBean(
					selectedCCEBean.getX_CCE_WARD(),
					selectedCCEBean.getX_CCE_WARD_ID());
			System.out.println("selectedStateLVB value and id is "+selectedCCEBean.getX_CCE_STATE()
                        +"  "+selectedCCEBean.getX_CCE_STATE_ID());
			FXMLLoader loader = new FXMLLoader(MainApp.class
							.getResource("/com/chai/inv/view/AddCCEData.fxml"));
			try {
				// Load the fxml file and create a new stage for the popup
				AnchorPane cceAddEditDialog = (AnchorPane) loader.load();
				Stage dialogStage = new Stage();
				dialogStage.setTitle("Edit CCE Form");
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(primaryStage);
				Scene scene = new Scene(cceAddEditDialog);
				dialogStage.setScene(scene);
				// Set the person into the controller
				CCEEditDialogController controller = loader
						.getController();
				controller.setDialogStage(dialogStage);
				controller.setCCEMain(this);
				controller.setUserBean(userBean);
				controller.setRole(role);
				controller.setCCEService(cceService, "edit");
				controller.setCCEBeanFields(selectedCCEBean,
						selectedStateLabelValueBean,
						selectedLGALabelValueBean,
						selectedWardLabelValueBean);
				// Show the dialog and wait until the user closes it
				dialogStage.showAndWait();
				return controller.isOkClicked();
			} catch (IOException e) {
				MainApp.LOGGER.setLevel(Level.SEVERE);
				MainApp.LOGGER.severe(MyLogger.getStackTrace(e));
				e.printStackTrace();
				return false;
			}
		} else {
			// Nothing selected
			Dialogs.create().owner(primaryStage).title("Warning")
					.masthead("No CCE Selected")
					.message("Please select a CCE in the table to edit")
					.showWarning();
			return false;
		}
	}

	@FXML
	public boolean handleSearchAction() {
		System.out.println("Hey We are in Search Action Handler");
		FXMLLoader loader = new FXMLLoader(
				MainApp.class
						.getResource("/com/chai/inv/view/AddCCEData.fxml"));
		try {
			// Load the fxml file and create a new stage for the popup
			AnchorPane cceAddEditDialog = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Search CCEs");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(cceAddEditDialog);
			dialogStage.setScene(scene);
			CCEEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCCEMain(this);
			controller.setUserBean(userBean);
			controller.setCCEService(cceService, "search");
			controller.setCCEBeanFields(new CCEBean(),
					new LabelValueBean("Select State", "0"),
					new LabelValueBean("Select LGA", "0"),
					new LabelValueBean("Select Ward", "0"));
			dialogStage.showAndWait();
			return controller.isOkClicked();
		} catch (IOException e) {
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe(MyLogger.getStackTrace(e));
			e.printStackTrace();
			return false;
		}
	}


	// public void setwarehouseLvb(LabelValueBean warehouseLbv) {
	// this.warehouseLbv = warehouseLbv;
	// x_USER_WAREHOUSE_NAME.setText("Warehouse: "+warehouseLbv.getLabel());
	// }
        
	@FXML
	public void handleHideCheckBox() {
		System.out.println("entered handleHideCheckBox()");
		if(!x_CCE_HIDE_COLUMNS.isSelected()){
                    cceRefrigerantColumn.setVisible(false);
                    cceEnergyColumn.setVisible(false);
                }
                else{
                    cceRefrigerantColumn.setVisible(true);
                    cceEnergyColumn.setVisible(true);
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