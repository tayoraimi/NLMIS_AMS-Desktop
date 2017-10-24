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
import com.chai.inv.model.GeneratorBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.service.GeneratorService;

public class GeneratorMainController {
        @FXML
        private TableColumn<GeneratorBean, String> generatorFunctionalColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorDurationNFColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorPowerColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorPlannedRepairsColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorFacilityHasElectricityColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorModelColumn;
        @FXML
        private TableView<GeneratorBean> generatorTable;
        @FXML
        private TableColumn<GeneratorBean, String> generatorFuelAvailableColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorRepairFundAvailableColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorLgaColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorAgeColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorPPMColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorLocationColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorElectricityHrsColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorManufacturerColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorWardColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorFacilityNameColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorStateColumn;
        @FXML
        private TableColumn<GeneratorBean, String> generatorFuelTypeColumn;


	@FXML
	private Button x_ADD_GENERATOR_BTN;
	@FXML
	private Button x_EDIT_GENERATOR_BTN;
	@FXML
	private Label x_ROW_COUNT;
	@FXML
	private ToolBar x_TOOLBAR;
	private MainApp mainApp;
	private GeneratorService generatorService;
	private RootLayoutController rootLayoutController;
	private GeneratorBean facilityBean;
	private Stage primaryStage;
	private ObservableList<GeneratorBean> list;
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
		rootLayoutController.getX_ROOT_COMMON_LABEL().setText("Related Equipment - Standby "
                        + "Generators\n\t\t\t\t\t&\nVoltage Stabilizers for larger vaccine stores");
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		generatorService = new GeneratorService();
		generatorTable.setItems(generatorService.getGeneratorList());
		x_ROW_COUNT.setText("Row Count : "+generatorTable.getItems().size());
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = new UserBean();
		this.userBean = userBean;
	}

	@FXML
	private void initialize() {
            
                generatorFunctionalColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>(
                                                        "x_GENERATOR_FUNCTIONAL"));
                generatorDurationNFColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>(
                                                        "x_GENERATOR_DURATION_NF"));
                generatorPowerColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>(
                                                        "x_GENERATOR_POWER"));
                generatorPlannedRepairsColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>("x_GENERATOR_PLANNED_REPAIRS"));
                generatorFacilityHasElectricityColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>("x_GENERATOR_LOCATION_HAS_ELECTRICITY"));
                generatorModelColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>("x_GENERATOR_MODEL"));
                generatorFuelAvailableColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>("x_GENERATOR_FUEL_AVAILABLE"));
//                generatorRepairFundAvailableColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>("x_GENERATOR_REPAIR_FUND_AVAILABLE"));
                generatorLgaColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>("x_GENERATOR_LGA"));
                generatorAgeColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>("x_GENERATOR_AGE"));
                generatorPPMColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>("x_GENERATOR_PPM"));
                generatorLocationColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>("x_GENERATOR_LOCATION"));
                generatorElectricityHrsColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>("x_GENERATOR_ELECTRICITY_HRS"));
                generatorManufacturerColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>("x_GENERATOR_MANUFACTURER"));
                generatorWardColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>("x_GENERATOR_WARD"));
                generatorFacilityNameColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>("x_GENERATOR_FACILITY_NAME"));
                generatorStateColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>("x_GENERATOR_STATE"));
                generatorFuelTypeColumn.setCellValueFactory(new PropertyValueFactory<GeneratorBean, String>("x_GENERATOR_FUEL_TYPE"));

		generatorTable
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

	public void refreshGeneratorTable() {
		System.out
				.println("In GeneratorMaincontroller.refreshGeneratorTable() method: ");
		int selectedIndex = generatorTable.getSelectionModel()
				.getSelectedIndex();
		generatorTable.setItems(null);
		generatorTable.layout();
		generatorTable.setItems(generatorService.getGeneratorList());
		generatorTable.getSelectionModel().select(selectedIndex);
		x_ROW_COUNT.setText("Row Count : "+generatorTable.getItems().size());
	}

	public void refreshGeneratorTable(ObservableList<GeneratorBean> list) {
		System.out.println("in refreshGeneratorTable(list)");
		if (list == null) {
			org.controlsfx.dialog.Dialogs.create().owner(getPrimaryStage())
					.title("Information").masthead("Search Result")
					.message("Record(s) not found!").showInformation();
		} else {
			generatorTable.setItems(list);
			org.controlsfx.dialog.Dialogs.create().owner(getPrimaryStage())
					.title("Information").masthead("Search Result")
					.message(list.size() + " Record(s) found!")
					.showInformation();
		}
	}

	@FXML
	public boolean handleAddAction() {
		System.out.println("Hey We are in Add Generator Action Handler");
		FXMLLoader loader = new FXMLLoader(
				MainApp.class
						.getResource("/com/chai/inv/view/AddGeneratorData.fxml"));
		try {
			// Load the fxml file and create a new stage for the popup
			AnchorPane generatorAddEditDialog = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add Generator Form");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(generatorAddEditDialog);
			dialogStage.setScene(scene);
			GeneratorEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setGeneratorMain(this);
			controller.setUserBean(userBean);
                        controller.setRole(role);
			controller.setGeneratorService(generatorService, "add");
			controller.setGeneratorBeanFields(new GeneratorBean(),
					new LabelValueBean("Select Generator Type", "0"),
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
		GeneratorBean selectedGeneratorBean = generatorTable.getSelectionModel()
				.getSelectedItem();
		if (selectedGeneratorBean != null) {
			LabelValueBean selectedStateLabelValueBean = new LabelValueBean(
					selectedGeneratorBean.getX_GENERATOR_STATE(),
					selectedGeneratorBean.getX_GENERATOR_STATE_ID());
			LabelValueBean selectedLGALabelValueBean = new LabelValueBean(
					selectedGeneratorBean.getX_GENERATOR_LGA(),
					selectedGeneratorBean.getX_GENERATOR_LGA_ID());
			LabelValueBean selectedWardLabelValueBean = new LabelValueBean(
					selectedGeneratorBean.getX_GENERATOR_WARD(),
					selectedGeneratorBean.getX_GENERATOR_WARD_ID());
			System.out.println("selectedStateLVB value and id is "+selectedGeneratorBean.getX_GENERATOR_STATE()
                        +"  "+selectedGeneratorBean.getX_GENERATOR_STATE_ID());
			FXMLLoader loader = new FXMLLoader(MainApp.class
							.getResource("/com/chai/inv/view/AddGeneratorData.fxml"));
			try {
				// Load the fxml file and create a new stage for the popup
				AnchorPane generatorAddEditDialog = (AnchorPane) loader.load();
				Stage dialogStage = new Stage();
				dialogStage.setTitle("Edit Generator Form");
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(primaryStage);
				Scene scene = new Scene(generatorAddEditDialog);
				dialogStage.setScene(scene);
				// Set the person into the controller
				GeneratorEditDialogController controller = loader
						.getController();
				controller.setDialogStage(dialogStage);
				controller.setGeneratorMain(this);
				controller.setUserBean(userBean);
				controller.setRole(role);
				controller.setGeneratorService(generatorService, "edit");
				controller.setGeneratorBeanFields(selectedGeneratorBean,
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
					.masthead("No Generator Selected")
					.message("Please select a Generator in the table to edit")
					.showWarning();
			return false;
		}
	}

	@FXML
	public boolean handleSearchAction() {
		System.out.println("Hey We are in Search Action Handler");
		FXMLLoader loader = new FXMLLoader(
				MainApp.class
						.getResource("/com/chai/inv/view/AddGeneratorData.fxml"));
		try {
			// Load the fxml file and create a new stage for the popup
			AnchorPane generatorAddEditDialog = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Search Generators");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(generatorAddEditDialog);
			dialogStage.setScene(scene);
			GeneratorEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setGeneratorMain(this);
			controller.setUserBean(userBean);
			controller.setGeneratorService(generatorService, "search");
			controller.setGeneratorBeanFields(new GeneratorBean(),
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