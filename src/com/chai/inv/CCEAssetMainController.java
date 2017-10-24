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
import com.chai.inv.model.CCEAssetBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.service.CCEAssetService;
import com.chai.inv.service.UserService;
import java.sql.SQLException;
import java.util.Date;
import javafx.scene.control.CheckBox;

public class CCEAssetMainController {
        
        @FXML
        private TableView<CCEAssetBean> cceAssetTable;
        @FXML
        private TableColumn<CCEAssetBean, String> cceAssetModelColumn;
        @FXML
        private TableColumn<CCEAssetBean, String> cceAssetVolPosColumn;
        @FXML
        private TableColumn<CCEAssetBean, String> cceAssetExpectedLifeColumn;
        @FXML
        private TableColumn<CCEAssetBean, String> cceAssetCompanyColumn;
        @FXML
        private TableColumn<CCEAssetBean, String> cceAssetCategoryColumn;
        @FXML
        private TableColumn<CCEAssetBean, String> cceAssetRefrigerantColumn;
        @FXML
        private TableColumn<CCEAssetBean, String> cceAssetVolNegColumn;
        @FXML
        private TableColumn<CCEAssetBean, String> cceAssetTypeColumn;
        @FXML
        private TableColumn<CCEAssetBean, String> cceAssetEnergyColumn;
        @FXML
        private TableColumn<CCEAssetBean, String> cceAssetDesignationColumn;
        @FXML
        private TableColumn<CCEAssetBean, String> cceAssetPriceColumn;


	@FXML
	private Button x_ADD_CCE_ASSET_BTN;
	@FXML
	private Button x_EDIT_CCE_ASSET_BTN;
	@FXML
	private Label x_ROW_COUNT;
	@FXML
	private ToolBar x_TOOLBAR;
        
	private MainApp mainApp;
	private CCEAssetService cceAssetService;
	private RootLayoutController rootLayoutController;
	private Stage primaryStage;
	private ObservableList<CCEAssetBean> list;
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
		rootLayoutController.getX_ROOT_COMMON_LABEL().setText("Cold Rooms & Freezer Rooms\nRefrigerators & Freezers");
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		cceAssetService = new CCEAssetService();
		cceAssetTable.setItems(cceAssetService.getCCEAssetList());
		x_ROW_COUNT.setText("Row Count : "+cceAssetTable.getItems().size());
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = new UserBean();
		this.userBean = userBean;
	}
	public void setCCEAssetListData() throws SQLException {

		cceAssetService = new CCEAssetService();

		list = cceAssetService.getCCEAssetList();
		cceAssetTable.setItems(list);
		x_ROW_COUNT.setText("Row Count : "+cceAssetTable.getItems().size());
	}


	@FXML
	private void initialize() {
                
                cceAssetModelColumn.setCellValueFactory(new PropertyValueFactory<CCEAssetBean, String>("x_CCE_ASSET_MODEL"));
                cceAssetVolPosColumn.setCellValueFactory(new PropertyValueFactory<CCEAssetBean, String>("x_CCE_ASSET_VOL_POS"));
                cceAssetExpectedLifeColumn.setCellValueFactory(new PropertyValueFactory<CCEAssetBean, String>("x_CCE_ASSET_EXPECTED_LIFE"));
                cceAssetCompanyColumn.setCellValueFactory(new PropertyValueFactory<CCEAssetBean, String>("x_CCE_ASSET_COMPANY"));
                cceAssetCategoryColumn.setCellValueFactory(new PropertyValueFactory<CCEAssetBean, String>("x_CCE_ASSET_CATEGORY"));
                cceAssetRefrigerantColumn.setCellValueFactory(new PropertyValueFactory<CCEAssetBean, String>("x_CCE_ASSET_REFRIGERANT"));
                cceAssetVolNegColumn.setCellValueFactory(new PropertyValueFactory<CCEAssetBean, String>("x_CCE_ASSET_VOL_NEG"));
                cceAssetTypeColumn.setCellValueFactory(new PropertyValueFactory<CCEAssetBean, String>("x_CCE_ASSET_TYPE"));
                cceAssetEnergyColumn.setCellValueFactory(new PropertyValueFactory<CCEAssetBean, String>("x_CCE_ASSET_ENERGY"));
                cceAssetDesignationColumn.setCellValueFactory(new PropertyValueFactory<CCEAssetBean, String>("x_CCE_ASSET_DESIGNATION"));
                cceAssetPriceColumn.setCellValueFactory(new PropertyValueFactory<CCEAssetBean, String>("x_CCE_ASSET_PRICE"));
                
		cceAssetTable
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

	public void refreshCCEAssetTable() {
		System.out
				.println("In CCEAssetMaincontroller.refreshCCEAssetTable() method: ");
		int selectedIndex = cceAssetTable.getSelectionModel()
				.getSelectedIndex();
		cceAssetTable.setItems(null);
		cceAssetTable.layout();
		cceAssetTable.setItems(cceAssetService.getCCEAssetList());
		cceAssetTable.getSelectionModel().select(selectedIndex);
		x_ROW_COUNT.setText("Row Count : "+cceAssetTable.getItems().size());
	}

	public void refreshCCEAssetTable(ObservableList<CCEAssetBean> list) {
		System.out.println("in refreshCCEAssetTable(list)");
		if (list == null) {
			org.controlsfx.dialog.Dialogs.create().owner(getPrimaryStage())
					.title("Information").masthead("Search Result")
					.message("Record(s) not found!").showInformation();
		} else {
			cceAssetTable.setItems(list);
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
						.getResource("/com/chai/inv/view/AddCCEAsset.fxml"));
		try {
			// Load the fxml file and create a new stage for the popup
			AnchorPane cceAddEditDialog = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add CCE Form");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(cceAddEditDialog);
			dialogStage.setScene(scene);
			CCEAssetEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCCEAssetMain(this);
			controller.setUserBean(userBean);
			controller.setCCEAssetService(cceAssetService, "add");
			controller.setCCEAssetBeanFields(new CCEAssetBean());
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
		CCEAssetBean selectedCCEAssetBean = cceAssetTable.getSelectionModel()
				.getSelectedItem();
		if (selectedCCEAssetBean != null) {
			FXMLLoader loader = new FXMLLoader(MainApp.class
							.getResource("/com/chai/inv/view/AddCCEAsset.fxml"));
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
				CCEAssetEditDialogController controller = loader
						.getController();
				controller.setDialogStage(dialogStage);
				controller.setCCEAssetMain(this);
				controller.setUserBean(userBean);
				controller.setCCEAssetService(cceAssetService, "edit");
				controller.setCCEAssetBeanFields(selectedCCEAssetBean);
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
						.getResource("/com/chai/inv/view/AddCCEAsset.fxml"));
		try {
			// Load the fxml file and create a new stage for the popup
			AnchorPane cceAddEditDialog = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Search CCEs");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(cceAddEditDialog);
			dialogStage.setScene(scene);
			CCEAssetEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setCCEAssetMain(this);
			controller.setUserBean(userBean);
			controller.setCCEAssetService(cceAssetService, "search");
			controller.setCCEAssetBeanFields(new CCEAssetBean());
			dialogStage.showAndWait();
			return controller.isOkClicked();
		} catch (IOException e) {
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe(MyLogger.getStackTrace(e));
			e.printStackTrace();
			return false;
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