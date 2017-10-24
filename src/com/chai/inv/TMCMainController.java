package com.chai.inv;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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
import com.chai.inv.model.TMCBean;
import com.chai.inv.model.HistoryBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.service.TMCService;

public class TMCMainController {
	@FXML
	private TableView<TMCBean> categoryTable;
	@FXML
	private TableColumn<TMCBean, String> companyIdColumn;
	@FXML
	private TableColumn<TMCBean, String> categoryIdColumn;
	@FXML
	private TableColumn<TMCBean, String> categoryCodeColumn;
	@FXML
	private TableColumn<TMCBean, String> categoryNameColumn;
	@FXML
	private TableColumn<TMCBean, String> decsriptionColumn;
	@FXML
	private TableColumn<TMCBean, String> categoryTypeCodeColumn;
	@FXML
	private TableColumn<TMCBean, String> categoryTypeIdColumn;
	@FXML
	private TableColumn<TMCBean, String> categoryTypeNameColumn;
	@FXML
	private TableColumn<TMCBean, String> sourceCodeColumn;
	@FXML
	private TableColumn<TMCBean, String> statusColumn;
	@FXML
	private TableColumn<TMCBean, String> startDateColumn;
	@FXML
	private TableColumn<TMCBean, String> endDateColumn;
	@FXML
	private ToolBar x_TOOLBAR;

	private boolean view_btn_pressed = false;
	private MainApp mainApp;
	private TMCService categoryService;
	private RootLayoutController rootLayoutController;
	private TMCBean categoryBean;
	private Stage primaryStage;
	private UserBean userBean;
	private LabelValueBean role;
	private HomePageController homePageController;
	public static  ProductPopupBtnController productPopupBtnController;
	

	public void setUserBean(UserBean userBean) {
		this.userBean = new UserBean();
		this.userBean = userBean;
	}

	public TMCBean getTMCBean() {
		return categoryBean;
	}

	public void setTMCBean(TMCBean TMCBean) {
		this.categoryBean = TMCBean;
	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setRootLayoutController(
			RootLayoutController rootLayoutController) {
		this.rootLayoutController = rootLayoutController;
		rootLayoutController.getX_ROOT_COMMON_LABEL().setText(
				"TMC");
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		categoryService = new TMCService();
		categoryTable.setItems(categoryService.getTMCList());
	}

	@FXML
	private void initialize() {

		categoryIdColumn
				.setCellValueFactory(new PropertyValueFactory<TMCBean, String>(
						"x_CATEGORY_ID"));
		companyIdColumn
				.setCellValueFactory(new PropertyValueFactory<TMCBean, String>(
						"x_COMPANY_ID"));
		categoryCodeColumn
				.setCellValueFactory(new PropertyValueFactory<TMCBean, String>(
						"x_CATEGORY_CODE"));
		categoryCodeColumn.setVisible(false);
		categoryNameColumn
				.setCellValueFactory(new PropertyValueFactory<TMCBean, String>(
						"x_CATEGORY_NAME"));
		decsriptionColumn
				.setCellValueFactory(new PropertyValueFactory<TMCBean, String>(
						"x_CATEGORY_DESCRIPTION"));
		categoryTypeCodeColumn
				.setCellValueFactory(new PropertyValueFactory<TMCBean, String>(
						"x_CATEGORY_TYPE_CODE"));
		categoryTypeIdColumn
				.setCellValueFactory(new PropertyValueFactory<TMCBean, String>(
						"x_CATEGORY_TYPE_ID"));
		categoryTypeNameColumn
				.setCellValueFactory(new PropertyValueFactory<TMCBean, String>(
						"x_CATEGORY_TYPE_NAME"));
		sourceCodeColumn
				.setCellValueFactory(new PropertyValueFactory<TMCBean, String>(
						"x_SOURCE_CODE"));
		sourceCodeColumn.setVisible(false);
		statusColumn
				.setCellValueFactory(new PropertyValueFactory<TMCBean, String>(
						"x_STATUS"));
		startDateColumn
				.setCellValueFactory(new PropertyValueFactory<TMCBean, String>(
						"x_START_DATE"));
		endDateColumn
				.setCellValueFactory(new PropertyValueFactory<TMCBean, String>(
						"x_END_DATE"));
		startDateColumn.setVisible(false);
		endDateColumn.setVisible(false);
		categoryTable
				.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	public void setRole(LabelValueBean role) {
		this.role = role;

		switch (role.getLabel()) {
		case "LIO": // LIO -
			x_TOOLBAR.getItems().remove(0, 2);
			break;
		case "MOH": // MOH -
			x_TOOLBAR.getItems().remove(0, 2);
			break;
		case "SIO": // SIO
			x_TOOLBAR.getItems().remove(0, 2);
			break;
		case "SCCO": // SCCO
			break;
		case "SIFP": // SIFP
			x_TOOLBAR.getItems().remove(0, 2);
			break;
		case "CCO": // CCO - EMPLOYEE
			x_TOOLBAR.getItems().remove(0, 2);
			break;
		}
	}

	public void refreshTMCTable() {
		System.out.println("in refreshTMCTable()");
		int selectedIndex = categoryTable.getSelectionModel()
				.getSelectedIndex();
		categoryTable.setItems(null);
		categoryTable.layout();
		categoryTable.setItems(categoryService.getTMCList());
		categoryTable.getSelectionModel().select(selectedIndex);
	}

	public void refreshTMCTable(ObservableList<TMCBean> list) {
		System.out.println("in refreshTMCTable(list)");
		if (list == null) {
			org.controlsfx.dialog.Dialogs.create().owner(getPrimaryStage())
					.title("Information").masthead("Search Result")
					.message("Record(s) not found!").showInformation();
		} else {
			categoryTable.setItems(null);
			categoryTable.layout();
			categoryTable.setItems(list);
			org.controlsfx.dialog.Dialogs.create().owner(getPrimaryStage())
					.title("Information").masthead("Search Result")
					.message(list.size() + " Record(s) found!")
					.showInformation();
		}
	}

	@FXML
	public boolean handleAddTMCAction() {
		System.out.println("Hey We are in Add TMC Action Handler");
		FXMLLoader loader = new FXMLLoader(
				MainApp.class
						.getResource("/com/chai/inv/view/AddTMC.fxml"));
		try {
			// Load the fxml file and create a new stage for the popup
			AnchorPane categoryAddEditDialog = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Add TMC Form");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(categoryAddEditDialog);
			dialogStage.setScene(scene);
			// Set the person into the controller
			TMCEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setTMCService(categoryService, "add");
			controller.setTMCBeanFields(new TMCBean(),
					new LabelValueBean("Select TMC Type", "0"));
			controller.setTMCMain(this);
			controller.setUserBean(userBean);
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
		System.out.println("Hey We are in Edit Action Handler");
		TMCBean selectedTMCBean = categoryTable.getSelectionModel()
				.getSelectedItem();
		if (selectedTMCBean != null) {
			LabelValueBean selectedLabelValueBean = new LabelValueBean(
					selectedTMCBean.getX_CATEGORY_TYPE_NAME(),
					selectedTMCBean.getX_CATEGORY_TYPE_ID(),
					selectedTMCBean.getX_COMPANY_ID(),
					selectedTMCBean.getX_SOURCE_CODE());
			FXMLLoader loader = new FXMLLoader(
					MainApp.class
							.getResource("/com/chai/inv/view/AddTMC.fxml"));
			try {
				// Load the fxml file and create a new stage for the popup
				AnchorPane categoryAddEditDialog = (AnchorPane) loader.load();
				Stage dialogStage = new Stage();
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(primaryStage);
				Scene scene = new Scene(categoryAddEditDialog);
				dialogStage.setScene(scene);
				// Set the person into the controller
				TMCEditDialogController controller = loader
						.getController();
				controller.setDialogStage(dialogStage);
				if (view_btn_pressed) {
					view_btn_pressed = false;
					dialogStage.setTitle("Edit TMC");
					controller.setTMCService(categoryService, "view");
				} else {
					dialogStage.setTitle("Edit TMC");
					controller.setTMCService(categoryService, "edit");
				}
				controller.setTMCMain(this);
				controller.setUserBean(userBean);
				controller.setTMCBeanFields(selectedTMCBean,
						selectedLabelValueBean);
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
		} else {
			// Nothing selected
			Dialogs.create().owner(primaryStage).title("Warning")
					.masthead("No category Selected")
					.message("Please select a category in the table to edit")
					.showWarning();
			return false;
		}
	}

	@FXML
	void handleViewTMCAction() {
		System.out.println("Hey We are in View Action Handler");
		view_btn_pressed = true;
		handleEditAction();
	}

	@FXML
	public boolean handleSearchAction() {
		System.out.println("Hey We are in TMC's Search Action Handler");
		FXMLLoader loader = new FXMLLoader(
				MainApp.class
						.getResource("/com/chai/inv/view/AddTMC.fxml"));
		try {
			// Load the fxml file and create a new stage for the popup
			AnchorPane categoryAddEditDialog = (AnchorPane) loader.load();
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Search TMC");
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(categoryAddEditDialog);
			dialogStage.setScene(scene);
			// Set the User into the controller
			TMCEditDialogController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setTMCService(categoryService, "search");
			controller.setTMCBeanFields(new TMCBean(),
					new LabelValueBean("Select Login Type", "0", ""));
			controller.setTMCMain(this);
			controller.setUserBean(userBean);
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
	public boolean handleHistoryAction() {
		System.out.println("Hey We are in TMC's History Action Handler");
		TMCBean selectedTMCBean = categoryTable.getSelectionModel()
				.getSelectedItem();
		if (selectedTMCBean != null) {
			FXMLLoader loader = new FXMLLoader(
					MainApp.class
							.getResource("/com/chai/inv/view/HistoryInformation.fxml"));
			try {
				GridPane historyDialog = (GridPane) loader.load();
				Stage dialogStage = new Stage();
				dialogStage.setTitle("TMC Record History");
				dialogStage.initModality(Modality.WINDOW_MODAL);
				dialogStage.initOwner(primaryStage);
				Scene scene = new Scene(historyDialog);
				dialogStage.setScene(scene);
				// Set the Type into the controller
				HistoryController controller = loader.getController();
				controller.setDialogStage(dialogStage);
				HistoryBean historyBean = new HistoryBean();
				historyBean.setX_TABLE_NAME("CATEGORIES");
				historyBean.setX_PRIMARY_KEY_COLUMN("CATEGORY_ID");
				historyBean.setX_PRIMARY_KEY(selectedTMCBean
						.getX_CATEGORY_ID());
				controller.setHistoryBean(historyBean);
				controller.setupHistoryDetails();
				dialogStage.showAndWait();
				return controller.isOkClicked();
			} catch (IOException e) {
				// Exception gets thrown if the fxml file could not be loaded
				e.printStackTrace();
				MainApp.LOGGER.setLevel(Level.SEVERE);
				MainApp.LOGGER.severe(MyLogger.getStackTrace(e));
				return false;
			}
		} else {
			// Nothing selected
			Dialogs.create().owner(primaryStage).title("Warning")
					.masthead("No User Selected")
					.message("Please select a user in the table for history")
					.showWarning();
			return false;
		}
	}

	@FXML
	public void handleExportAction() {
		System.out.println("Hey We are in TMC's Export Action Handler");
		ObservableList<TMCBean> categoryExportData = categoryTable
				.getItems();
		String csv = categoryCodeColumn.getText() + ","
				+ categoryNameColumn.getText() + ","
				+ decsriptionColumn.getText() + ","
				+ categoryTypeNameColumn.getText() + ","
				+ sourceCodeColumn.getText() + ","
				+ categoryTypeCodeColumn.getText() + ","
				+ statusColumn.getText() + "," + startDateColumn.getText()
				+ "," + endDateColumn.getText();
		String csvVal;
		for (TMCBean u : categoryExportData) {
			csv += "\n";
			csvVal = u.getX_CATEGORY_CODE() + "," + u.getX_CATEGORY_NAME()
					+ "," + u.getX_CATEGORY_DESCRIPTION() + ","
					+ u.getX_CATEGORY_TYPE_NAME() + "," + u.getX_SOURCE_CODE()
					+ "," + u.getX_CATEGORY_TYPE_CODE() + "," + u.getX_STATUS()
					+ "," + u.getX_START_DATE() + "," + u.getX_END_DATE();

			csvVal = csvVal.replaceAll("\\n", "");
			csv += csvVal;
		}
		csv = csv.replaceAll("null", "");
		FileChooser fileChooser = new FileChooser();
		// Set extension filter
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter(
				"CSV files (*.csv)", "*.csv");
		fileChooser.getExtensionFilters().add(extFilter);
		// Show save file dialog
		fileChooser.setInitialFileName("Categories List");
		File file = fileChooser.showSaveDialog(mainApp.getPrimaryStage());
		if (file != null) {
			// Make sure it has the correct extension
			if (!file.getPath().endsWith(".xml")
					&& !file.getPath().endsWith(".xlsx")
					&& !file.getPath().endsWith(".csv")) {
				file = new File(file.getPath() + ".txt");
			}
			mainApp.saveDataToFile(file, csv);
		}
	}

	public void setwarehouseLvb(LabelValueBean warehouseLbv) {
		// this.warehouseLbv = warehouseLbv;
	}

	@FXML
	public void handleHomeDashBoardBtn() {
		System.out.println("entered handleHomeDashBoardBtn()");
		rootLayoutController.handleHomeMenuAction();
	}

	@FXML
	public void handleBackToAdminSubMenu() throws Exception {
		System.out.println("entered handleAdminSubMenuBackBtn()");
		homePageController.handleProductsDashBoardBtn();
	}

	public void setHomePageController(HomePageController homePageController) {
		this.homePageController = homePageController;
	}

	public void setProdPopBtnCont(
			ProductPopupBtnController productPopupBtnController) {
		TMCMainController.productPopupBtnController=productPopupBtnController;
		
	}
}
