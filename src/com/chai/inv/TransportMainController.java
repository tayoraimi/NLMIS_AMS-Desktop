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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.ToolBar;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import com.chai.inv.logger.MyLogger;
import com.chai.inv.model.TransportBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.service.TransportService;

public class TransportMainController {
        
        @FXML
        private TableView<TransportBean> transportTable;
        
        @FXML
        private TableColumn<TransportBean, String> transportStateColumn;
        @FXML
        private TableColumn<TransportBean, String> transportLocationColumn;
        @FXML
        private TableColumn<TransportBean, String> transportModelColumn;
        @FXML
        private TableColumn<TransportBean, String> transportMaintenanceColumn;
        @FXML
        private TableColumn<TransportBean, String> transportDurationNFColumn;
        @FXML
        private TableColumn<TransportBean, String> transportAgeColumn;
        @FXML
        private TableColumn<TransportBean, String> transportAwaitingFundColumn;
        @FXML
        private TableColumn<TransportBean, String> transportMakeColumn;
        @FXML
        private TableColumn<TransportBean, String> transportFacilityNameColumn;
        @FXML
        private TableColumn<TransportBean, String> transportOwnerColumn;
        @FXML
        private TableColumn<TransportBean, String> transportLgaColumn;
        @FXML
        private TableColumn<TransportBean, String> transportTargetPopulationColumn;
        @FXML
        private TableColumn<TransportBean, String> transportWardColumn;
        @FXML
        private TableColumn<TransportBean, String> transportFunctionalColumn;
        @FXML
        private TableColumn<TransportBean, String> transportTypeColumn;
        @FXML
        private TableColumn<TransportBean, String> transportPlannedRepairsColumn;
        @FXML
        private TableColumn<TransportBean, String> transportFuelAvailableColumn;
        @FXML
        private TableColumn<TransportBean, String> transportDistanceColumn;
        @FXML
        private TableColumn<TransportBean, String> transportNumberOfHFColumn;
        @FXML
        private TableColumn<TransportBean, String> transportPublicFundColumn;
        

	@FXML
	private Button x_ADD_TRANSPORT_BTN;
	@FXML
	private Button x_EDIT_TRANSPORT_BTN;
	@FXML
	private Label x_ROW_COUNT;
	@FXML
	private ToolBar x_TOOLBAR;
        
	private MainApp mainApp;
	private TransportService transportService;
	private RootLayoutController rootLayoutController;
	private TransportBean facilityBean;
	private Stage primaryStage;
	private ObservableList<TransportBean> list;
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
		rootLayoutController.getX_ROOT_COMMON_LABEL().setText("Transport");
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		transportService = new TransportService();
		transportTable.setItems(transportService.getTransportList());
		x_ROW_COUNT.setText("Row Count : "+transportTable.getItems().size());
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = new UserBean();
		this.userBean = userBean;
	}

	@FXML
	private void initialize() {
            
            transportStateColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_STATE"));
            transportLocationColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_LOCATION"));
            transportModelColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_MODEL"));
            transportMaintenanceColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_SERVICED"));
            transportDurationNFColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_DURATION_NF"));
            transportAgeColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_AGE"));
            transportAwaitingFundColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_AWAITING_FUND"));
            transportMakeColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_MAKE"));
            transportFacilityNameColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_FACILITY_NAME"));
            transportOwnerColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_OWNER"));
            transportLgaColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_LGA"));
            transportTargetPopulationColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_TARGET_POPULATION"));
            transportWardColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_WARD"));
            transportFunctionalColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_FUNCTIONAL"));
            transportTypeColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_TYPE"));
            transportPlannedRepairsColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_PLANNED_REPAIRS"));
            transportFuelAvailableColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_FUEL_AVAILABLE"));
            transportDistanceColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_DISTANCE"));
            transportNumberOfHFColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_NUMBER_OF_HF"));
            transportPublicFundColumn.setCellValueFactory(new PropertyValueFactory<TransportBean, String>("x_TRANSPORT_PUBLIC_FUND"));

		transportTable
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

	public void refreshTransportTable() {
		System.out
				.println("In TransportMaincontroller.refreshTransportTable() method: ");
		int selectedIndex = transportTable.getSelectionModel()
				.getSelectedIndex();
		transportTable.setItems(null);
		transportTable.layout();
		transportTable.setItems(transportService.getTransportList());
		transportTable.getSelectionModel().select(selectedIndex);
		x_ROW_COUNT.setText("Row Count : "+transportTable.getItems().size());
	}

	public void refreshTransportTable(ObservableList<TransportBean> list) {
		System.out.println("in refreshTransportTable(list)");
		if (list == null) {
			org.controlsfx.dialog.Dialogs.create().owner(getPrimaryStage())
					.title("Information").masthead("Search Result")
					.message("Record(s) not found!").showInformation();
		} else {
			transportTable.setItems(list);
			org.controlsfx.dialog.Dialogs.create().owner(getPrimaryStage())
					.title("Information").masthead("Search Result")
					.message(list.size() + " Record(s) found!")
					.showInformation();
		}
	}

	@FXML
	public boolean handleAddAction() {
		System.out.println("Hey We are in Add Transport Action Handler");
		FXMLLoader loader = new FXMLLoader(
				MainApp.class
						.getResource("/com/chai/inv/view/AddTransportData.fxml"));
		try {
			// Load the fxml file and create a new stage for the popup
			AnchorPane transportAddEditDialog = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add Transport Form");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(transportAddEditDialog);
			dialogStage.setScene(scene);
			TransportEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setTransportMain(this);
			controller.setUserBean(userBean);
                        controller.setRole(role);
			controller.setTransportService(transportService, "add");
			controller.setTransportBeanFields(new TransportBean(),
					new LabelValueBean("Select Transport Type", "0"),
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
		TransportBean selectedTransportBean = transportTable.getSelectionModel()
				.getSelectedItem();
		if (selectedTransportBean != null) {
			LabelValueBean selectedStateLabelValueBean = new LabelValueBean(
					selectedTransportBean.getX_TRANSPORT_STATE(),
					selectedTransportBean.getX_TRANSPORT_STATE_ID());
			LabelValueBean selectedLGALabelValueBean = new LabelValueBean(
					selectedTransportBean.getX_TRANSPORT_LGA(),
					selectedTransportBean.getX_TRANSPORT_LGA_ID());
			LabelValueBean selectedWardLabelValueBean = new LabelValueBean(
					selectedTransportBean.getX_TRANSPORT_WARD(),
					selectedTransportBean.getX_TRANSPORT_WARD_ID());
			System.out.println("selectedStateLVB value and id is "+selectedTransportBean.getX_TRANSPORT_STATE()
                        +"  "+selectedTransportBean.getX_TRANSPORT_STATE_ID());
			FXMLLoader loader = new FXMLLoader(MainApp.class
							.getResource("/com/chai/inv/view/AddTransportData.fxml"));
			try {
				// Load the fxml file and create a new stage for the popup
				AnchorPane transportAddEditDialog = (AnchorPane) loader.load();
				Stage dialogStage = new Stage();
				dialogStage.setTitle("Edit Transport Form");
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(primaryStage);
				Scene scene = new Scene(transportAddEditDialog);
				dialogStage.setScene(scene);
				// Set the person into the controller
				TransportEditDialogController controller = loader
						.getController();
				controller.setDialogStage(dialogStage);
				controller.setTransportMain(this);
				controller.setUserBean(userBean);
				controller.setRole(role);
				controller.setTransportService(transportService, "edit");
				controller.setTransportBeanFields(selectedTransportBean,
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
					.masthead("No Transport Selected")
					.message("Please select a Transport in the table to edit")
					.showWarning();
			return false;
		}
	}

	@FXML
	public boolean handleSearchAction() {
		System.out.println("Hey We are in Search Action Handler");
		FXMLLoader loader = new FXMLLoader(
				MainApp.class
						.getResource("/com/chai/inv/view/AddTransportData.fxml"));
		try {
			// Load the fxml file and create a new stage for the popup
			AnchorPane transportAddEditDialog = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Search Transports");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(transportAddEditDialog);
			dialogStage.setScene(scene);
			TransportEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setTransportMain(this);
			controller.setUserBean(userBean);
			controller.setTransportService(transportService, "search");
			controller.setTransportBeanFields(new TransportBean(),
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
	public void handleHomeDashBoardBtn() {
		System.out.println("entered handleHomeDashBoardBtn()");
		rootLayoutController.handleHomeMenuAction();
	}

	public void setHomePageController(HomePageController homePageController) {
		this.homePageController = homePageController;
	}
}