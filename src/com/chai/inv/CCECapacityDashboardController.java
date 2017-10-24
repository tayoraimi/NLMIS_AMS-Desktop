package com.chai.inv;

import java.text.DecimalFormat;
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
import com.chai.inv.model.CCECapacityBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.service.AssetDashboardService;
import com.chai.inv.service.UserService;
import java.sql.SQLException;
import java.util.Date;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.VBox;

public class CCECapacityDashboardController {
        
        
        @FXML
        private TableView<CCECapacityBean> cceCapacityTable;
        @FXML
        private TableColumn<CCECapacityBean, String> cceCapacityHPVColumn;
        @FXML
        private TableColumn<CCECapacityBean, String> cceCapacityLgaColumn;
        @FXML
        private TableColumn<CCECapacityBean, String> cceCapacityLocationColumn;
        @FXML
        private TableColumn<CCECapacityBean, String> cceCapacityMRColumn;
        @FXML
        private TableColumn<CCECapacityBean, String> cceCapacityRotaColumn;
        @FXML
        private TableColumn<CCECapacityBean, String> cceCapacity2020Column;
        @FXML
        private TableColumn<CCECapacityBean, String> cceCapacityRIColumn;
        @FXML
        private TableColumn<CCECapacityBean, String> cceCapacityStateColumn;
        @FXML
        private TableColumn<CCECapacityBean, String> cceCapacityWardColumn;
        @FXML
        private TableColumn<CCECapacityBean, String> cceCapacityMenAColumn;
    
        @FXML
        private ComboBox<String> x_CCE_CAPACITY_LEVEL;
	@FXML
	private VBox x_VBOX;
	@FXML
	private ToolBar x_FILTER_TOOLBAR;
        

	@FXML
	private Label x_ROW_COUNT;
	@FXML
	private ToolBar x_TOOLBAR;
        
	private MainApp mainApp;
	private AssetDashboardService cceCapacityService;
	private RootLayoutController rootLayoutController;
	private CCECapacityBean facilityBean;
	private Stage primaryStage;
	private ObservableList<CCECapacityBean> list;
	private String actionBtnString;
	private UserBean userBean;
	private LabelValueBean warehouseLbv;
	private LabelValueBean role;
	private HomePageController homePageController;
        private DecimalFormat df;
        private String[] itemElement;

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}

	public void setRootLayoutController(
			RootLayoutController rootLayoutController) {
		this.rootLayoutController = rootLayoutController;
		rootLayoutController.getX_ROOT_COMMON_LABEL().setText("CCE Capacity Dashboard");
	}

	public void setMainApp(MainApp mainApp) {
		this.mainApp = mainApp;
		cceCapacityService = new AssetDashboardService();
		//cceCapacityTable.setItems(cceCapacityService.getCCECapacityList());
		x_ROW_COUNT.setText("Row Count : "+cceCapacityTable.getItems().size());
	}

	public void setUserBean(UserBean userBean) {
		this.userBean = new UserBean();
		this.userBean = userBean;
	}
	public void setCCECapacityListData() throws SQLException {

		cceCapacityService = new AssetDashboardService();

		list = cceCapacityService.getCCECapacityList("");
		cceCapacityTable.setItems(list);
		x_ROW_COUNT.setText("Row Count : "+cceCapacityTable.getItems().size());
	}


	@FXML
	private void initialize() {
            
                df = new DecimalFormat("#.##");
                cceCapacityStateColumn.setCellValueFactory(new PropertyValueFactory<CCECapacityBean, String>("x_CCE_CAPACITY_STATE"));
                cceCapacityWardColumn.setCellValueFactory(new PropertyValueFactory<CCECapacityBean, String>("x_CCE_CAPACITY_WARD"));
                cceCapacityLocationColumn.setCellValueFactory(new PropertyValueFactory<CCECapacityBean, String>("x_CCE_CAPACITY_LOCATION"));
                cceCapacityLgaColumn.setCellValueFactory(new PropertyValueFactory<CCECapacityBean, String>("x_CCE_CAPACITY_LGA"));             
                cceCapacityRIColumn.setCellValueFactory(new PropertyValueFactory<CCECapacityBean, String>("x_CCE_CAPACITY_RI"));
                cceCapacityRIColumn.setCellFactory(column -> {
			return new TableCell<CCECapacityBean, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
                                    Double doubleItem;
					super.updateItem(item, empty);
                                        setStyle("");
					if (item == null || empty) {
						setText(item);
					} else {
						// Format date.
						//setText(item);
                                                itemElement=item.split("/");
						if (Double.parseDouble(itemElement[1])==0.0) {
                                                    setText("Cap. required not specified");
						} else{
                                                    doubleItem = (Double.parseDouble(itemElement[0])/Double.parseDouble(itemElement[1]))*100;
                                                    if(doubleItem<70.0){//capacity is less than 70% of required
                                                        setStyle("-fx-background-color: red");
                                                        setText(df.format(doubleItem)+"%");
                                                        setTextFill(Color.WHITE);
                                                    } else if(doubleItem>=70.0&&doubleItem<100.0){
                                                        setText("");
//                                                        setTextFill(Color.BLACK);
                                                        setStyle("-fx-background-color: orange");
                                                        setText(df.format(doubleItem)+"%");
                                                    } else if(doubleItem>=100.0){
                                                        setText("");
//                                                        setTextFill(Color.BLACK);
                                                        setStyle("-fx-background-color: green");
                                                    }
                                                    else{
                                                        setText("");
                                                        setStyle("");
                                                    }
                                                }
                                        
					}
				}
			};
		});
                cceCapacityMenAColumn.setCellValueFactory(new PropertyValueFactory<CCECapacityBean, String>("x_CCE_CAPACITY_MEN_A"));
                cceCapacityMenAColumn.setCellFactory(column -> {
			return new TableCell<CCECapacityBean, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
                                    Double doubleItem;
					super.updateItem(item, empty);
                                        setStyle("");
					if (item == null || empty) {
						setText(item);
					} else {
						// Format date.
						//setText(item);
                                                itemElement=item.split("/");
						if (Double.parseDouble(itemElement[1])==0.0) {
                                                    setText("Cap. required not specified");
						} else{
                                                    doubleItem = (Double.parseDouble(itemElement[0])/Double.parseDouble(itemElement[1]))*100;
                                                    if(doubleItem<70.0){//capacity is less than 70% of required
                                                        setStyle("-fx-background-color: red");
                                                        setText(df.format(doubleItem)+"%");
                                                        setTextFill(Color.WHITE);
                                                    } else if(doubleItem>=70.0&&doubleItem<100.0){
                                                        setText("");
//                                                        setTextFill(Color.BLACK);
                                                        setStyle("-fx-background-color: orange");
                                                    } else if(doubleItem>=100.0){
                                                        setText("");
//                                                        setTextFill(Color.BLACK);
                                                        setStyle("-fx-background-color: green");
                                                    }
                                                    else{
                                                        setText("");
                                                        setStyle("");
                                                    }
                                                }
					}
				}
			};
		});
                cceCapacityRotaColumn.setCellValueFactory(new PropertyValueFactory<CCECapacityBean, String>("x_CCE_CAPACITY_ROTA"));
                cceCapacityRotaColumn.setCellFactory(column -> {
			return new TableCell<CCECapacityBean, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
                                    Double doubleItem;
					super.updateItem(item, empty);
                                        setStyle("");
					if (item == null || empty) {
						setText(item);
						//setStyle("");
					} else {
						// Format date.
						//setText(item);
                                                itemElement=item.split("/");
						if (Double.parseDouble(itemElement[1])==0.0) {
                                                    setText("Cap. required not specified");
						} else{
                                                    doubleItem = (Double.parseDouble(itemElement[0])/Double.parseDouble(itemElement[1]))*100;
                                                    if(doubleItem<70.0){//capacity is less than 70% of required
                                                        setStyle("-fx-background-color: red");
                                                        setText(df.format(doubleItem)+"%");
                                                        setTextFill(Color.WHITE);
                                                    } else if(doubleItem>=70.0&&doubleItem<100.0){
                                                        setText("");
//                                                        setTextFill(Color.BLACK);
                                                        setStyle("-fx-background-color: orange");
                                                    } else if(doubleItem>=100.0){
                                                        setText("");
//                                                        setTextFill(Color.BLACK);
                                                        setStyle("-fx-background-color: green");
                                                    }
                                                    else{
                                                        setText("");
                                                        setStyle("");
                                                    }
                                                }
                                        }
				}
			};
		});
                cceCapacityMRColumn.setCellValueFactory(new PropertyValueFactory<CCECapacityBean, String>("x_CCE_CAPACITY_MR"));
                cceCapacityMRColumn.setCellFactory(column -> {
			return new TableCell<CCECapacityBean, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
                                    Double doubleItem;
					super.updateItem(item, empty);
                                        setStyle("");
					if (item == null || empty) {
						setText(item);
						//setStyle("");
					} else {
						// Format date.
						//setText(item);
                                                itemElement=item.split("/");
						if (Double.parseDouble(itemElement[1])==0.0) {
                                                    setText("Cap. required not specified");
						} else{
                                                    doubleItem = (Double.parseDouble(itemElement[0])/Double.parseDouble(itemElement[1]))*100;
                                                    if(doubleItem<70.0){//capacity is less than 70% of required
                                                        setStyle("-fx-background-color: red");
                                                        setText(df.format(doubleItem)+"%");
                                                        setTextFill(Color.WHITE);
                                                    } else if(doubleItem>=70.0&&doubleItem<100.0){
                                                        setText("");
//                                                        setTextFill(Color.BLACK);
                                                        setStyle("-fx-background-color: orange");
                                                    } else if(doubleItem>=100.0){
                                                        setText("");
//                                                        setTextFill(Color.BLACK);
                                                        setStyle("-fx-background-color: green");
                                                    }
                                                    else{
                                                        setText("");
                                                        setStyle("");
                                                    }
                                                }
					}
				}
			};
		});
                cceCapacityHPVColumn.setCellValueFactory(new PropertyValueFactory<CCECapacityBean, String>("x_CCE_CAPACITY_HPV"));
                cceCapacityHPVColumn.setCellFactory(column -> {
			return new TableCell<CCECapacityBean, String>() {
				@Override
				protected void updateItem(String item, boolean empty) {
                                    Double doubleItem;
					super.updateItem(item, empty);
                                        setStyle("");
					if (item == null || empty) {
						setText(item);
					} else {
						// Format date.
						//setText(item);
                                                itemElement=item.split("/");
						if (Double.parseDouble(itemElement[1])==0.0) {
                                                    setText("Cap. required not specified");
						} else{
                                                    doubleItem = (Double.parseDouble(itemElement[0])/Double.parseDouble(itemElement[1]))*100;
                                                    if(doubleItem<70.0){//capacity is less than 70% of required
                                                        setStyle("-fx-background-color: red");
                                                        setText(df.format(doubleItem)+"%");
                                                        setTextFill(Color.WHITE);
                                                    } else if(doubleItem>=70.0&&doubleItem<100.0){
                                                        setText("");
//                                                        setTextFill(Color.BLACK);
                                                        setStyle("-fx-background-color: orange");
                                                    } else if(doubleItem>=100.0){
                                                        setText("");
//                                                        setTextFill(Color.BLACK);
                                                        setStyle("-fx-background-color: green");
                                                    }
                                                    else{
                                                        setText("");
                                                        setStyle("");
                                                    }
                                                }
					}
				}
			};
		});

		cceCapacityTable
				.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	public void setRole(LabelValueBean role) {
		this.role = role;
		switch (role.getLabel()) {
		case "LIO": // LIO - SUPER USER
                    x_VBOX.getChildren().remove(x_FILTER_TOOLBAR);
			break;
		case "MOH": // LIO - SUPER USER
                    x_CCE_CAPACITY_LEVEL.getItems().addAll("LGA","Ward");
                    x_CCE_CAPACITY_LEVEL.setValue("LGA");
			break;
		case "SIO": // SIO
                    x_CCE_CAPACITY_LEVEL.getItems().addAll("LGA","Ward");
                    x_CCE_CAPACITY_LEVEL.setValue("LGA");
			break;
		case "SCCO": // SCCO
                    x_CCE_CAPACITY_LEVEL.getItems().addAll("LGA","Ward");
                    x_CCE_CAPACITY_LEVEL.setValue("LGA");
			break;
		case "SIFP": // SIFP
                    x_CCE_CAPACITY_LEVEL.getItems().addAll("LGA","Ward");
                    x_CCE_CAPACITY_LEVEL.setValue("LGA");
			break;
		case "CCO": // CCO - EMPLOYEE
                    x_VBOX.getChildren().remove(x_FILTER_TOOLBAR);
			break;
		}
	}

	@FXML
	void handleOnLevelSelected() {
		System.out.println("**In CustomerMainController.handleOnStateSelected() listener **");
		if (x_CCE_CAPACITY_LEVEL.getValue() != null) {
			cceCapacityTable.setItems(cceCapacityService.getCCECapacityList(x_CCE_CAPACITY_LEVEL.getValue()));
			x_ROW_COUNT.setText("Row Count : "+ cceCapacityTable.getItems().size());
		}
	}
        
        public void refreshCCECapacityTable() {
		System.out
				.println("In CCECapacityStatusDashboardcontroller.refreshCCECapacityTable() method: ");
		int selectedIndex = cceCapacityTable.getSelectionModel()
				.getSelectedIndex();
		cceCapacityTable.setItems(null);
		cceCapacityTable.layout();
		cceCapacityTable.setItems(cceCapacityService.getCCECapacityList(""));
		cceCapacityTable.getSelectionModel().select(selectedIndex);
		x_ROW_COUNT.setText("Row Count : "+cceCapacityTable.getItems().size());
	}

	public void refreshCCECapacityTable(ObservableList<CCECapacityBean> list) {
		System.out.println("in refreshCCETable(list)");
		if (list == null) {
			org.controlsfx.dialog.Dialogs.create().owner(getPrimaryStage())
					.title("Information").masthead("Search Result")
					.message("Record(s) not found!").showInformation();
		} else {
			cceCapacityTable.setItems(list);
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
	public void handleHomeDashBoardBtn() {
		System.out.println("entered handleHomeDashBoardBtn()");
		rootLayoutController.handleHomeMenuAction();
	}

	public void setHomePageController(HomePageController homePageController) {
		this.homePageController = homePageController;
	}
}