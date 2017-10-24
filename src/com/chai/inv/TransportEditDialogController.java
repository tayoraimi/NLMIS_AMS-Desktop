package com.chai.inv;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import org.controlsfx.dialog.Dialogs;

import com.chai.inv.model.TransportBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.model.UserBean;
import com.chai.inv.service.CommonService;
import com.chai.inv.service.TransportService;
import com.chai.inv.service.FacilityService;
import com.chai.inv.util.CalendarUtil;
import com.chai.inv.util.SelectKeyComboBoxListener;

public class TransportEditDialogController {
        
        
        @FXML
        private CheckBox x_TRANSPORT_FUNDS;
        @FXML
        private CheckBox x_TRANSPORT_SERVICED;
        @FXML
        private CheckBox x_TRANSPORT_FUEL_AVAILABLE;
        @FXML
        private CheckBox x_TRANSPORT_AWAITING_FUNDS;
        @FXML
        private TextField x_TRANSPORT_DISTANCE;
        @FXML
        private ComboBox<LabelValueBean> x_TRANSPORT_STATE;
        @FXML
        private ComboBox<LabelValueBean> x_TRANSPORT_MODEL;
        @FXML
        private TextField x_TRANSPORT_NUMBER_OF_HF;
        @FXML
        private ComboBox<LabelValueBean> x_TRANSPORT_WARD;
        @FXML
        private ComboBox<LabelValueBean> x_TRANSPORT_OWNER;
        @FXML
        private CheckBox x_TRANSPORT_STATUS;
        @FXML
        private ComboBox<LabelValueBean> x_TRANSPORT_TYPE;
        @FXML
        private DatePicker x_TRANSPORT_AGE;
        @FXML
        private ComboBox<String> x_TRANSPORT_LOCATION;
        @FXML
        private ComboBox<LabelValueBean> x_TRANSPORT_FACILITY;
        @FXML
        private ComboBox<LabelValueBean> x_TRANSPORT_MAKE;
        @FXML
        private CheckBox x_TRANSPORT_PPM;
        @FXML
        private ComboBox<LabelValueBean> x_TRANSPORT_LGA;
        @FXML
        private TextField x_TRANSPORT_NF_DURATION;
        @FXML
        private TextField x_TRANSPORT_TARGET_POPULATION;
        
        @FXML
        private Button x_CANCEL_BTN;
        @FXML
        private Button x_OK_BTN;
        
	private boolean okClicked = false;
	private String actionBtnString;
	private boolean state_transport_record = false;
	private TransportBean transportBean;
	private RootLayoutController rootLayoutController;
	private Stage dialogStage;
	private TransportService transportService;
	private TransportMainController transportMain;
	private UserBean userBean;
	private LabelValueBean role;
        private int nfDuration = 0;
        private String status;

	public void setUserBean(UserBean userBean) {
		this.userBean = userBean;
	}

	public TransportMainController getTransportMain() {
		return transportMain;
	}

	public void setTransportMain(TransportMainController transportMain) {
		this.transportMain = transportMain;
	}

	public void setTransportService(TransportService transportService,
			String actionBtnString) {
		this.transportService = transportService;
		this.actionBtnString = actionBtnString;
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public boolean isOkClicked() {
		return okClicked;
	}

	public void disableOkButton() {
		x_OK_BTN.setDisable(true);
	}

	public void setTransportBeanFields(TransportBean transportBean,
			LabelValueBean statelabelValueBean,
			LabelValueBean lgalabelValueBean,
			LabelValueBean wardlabelValueBean) {
		this.transportBean = new TransportBean();
		this.transportBean = transportBean;

		System.out.println("Warehouse ID: " + userBean.getX_USER_WAREHOUSE_ID());
                
                
                if (actionBtnString.equals("add")) {
			System.out.println("action button string: " + actionBtnString);
			if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA == null) {
                                x_TRANSPORT_STATE.getItems().addAll(new LabelValueBean("", null));
				x_TRANSPORT_STATE.setItems(transportService.getDropdownList("defaultstorelist", null));
				x_TRANSPORT_STATE.setPromptText("State");
			} else if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA != null) {
				x_TRANSPORT_STATE.setValue(new LabelValueBean(userBean.getX_USER_WAREHOUSE_NAME(), userBean.getX_USER_WAREHOUSE_ID()));
				x_TRANSPORT_STATE.setDisable(true);
                                x_TRANSPORT_LGA.getItems().addAll(new LabelValueBean("", null));
				x_TRANSPORT_LGA.setItems(transportService.getDropdownList("defaultstorelist",
								userBean.getX_USER_WAREHOUSE_ID()));
			} else if (MainApp.getUserRole().getLabel().equals("SCCO")
					&& CustomChoiceDialog.selectedLGA == null) {
				x_TRANSPORT_STATE.setValue(new LabelValueBean(userBean
						.getX_USER_WAREHOUSE_NAME(), userBean
						.getX_USER_WAREHOUSE_ID()));
				x_TRANSPORT_STATE.setDisable(true);
                                x_TRANSPORT_LGA.getItems().addAll(new LabelValueBean("", null));
				x_TRANSPORT_LGA.setItems(transportService
						.getDropdownList("defaultstorelist","ALL_LGA_UNDER_STATE"));
				x_TRANSPORT_LGA
						.setPromptText("LGA");
			} else if ((MainApp.getUserRole().getLabel().equals("SCCO") && CustomChoiceDialog.selectedLGA != null)
					|| MainApp.getUserRole().getLabel().equals("CCO")) {
				// get only the selected LGA name in the list

                                x_TRANSPORT_STATE.setValue(new FacilityService().getStateStoreId(userBean.getX_USER_WAREHOUSE_ID()));
				x_TRANSPORT_STATE.setDisable(true);
				x_TRANSPORT_LGA.setItems(transportService.getDropdownList("defaultstorelist"));
				x_TRANSPORT_LGA.setValue(x_TRANSPORT_LGA.getItems().get(0));
				x_TRANSPORT_LGA.setDisable(true);
                                System.out.println("To search for ward, LGA value is "+x_TRANSPORT_LGA.getItems().get(0).getValue());
                                x_TRANSPORT_WARD.getItems().addAll(new LabelValueBean("", null));
				x_TRANSPORT_WARD.setItems(transportService.getDropdownList("WardList",
						x_TRANSPORT_LGA.getItems().get(0).getValue()));
//                                x_TRANSPORT_LOCATION.setItems(transportService.getDropdownList("EquipmentLocationList",
//                                                userBean.getX_USER_WAREHOUSE_ID()));
//				x_TRANSPORT_LOCATION.setValue(new LabelValueBean(
//                                                x_TRANSPORT_LOCATION.getItems().get(0).getLabel(),null));
                             
			}
                        
                                x_TRANSPORT_TYPE.setItems(transportService.getDropdownList("TransportTypeList"));

		}else if (actionBtnString.equals("edit")) {
			System.out.println("action button string: " + actionBtnString);
			if (MainApp.getUserRole().getLabel().equals("SCCO")
					&& CustomChoiceDialog.selectedLGA == null) {
				x_TRANSPORT_STATE.setValue(new LabelValueBean(transportBean
						.getX_TRANSPORT_STATE(), transportBean
						.getX_TRANSPORT_STATE_ID()));
				x_TRANSPORT_STATE.setDisable(true);
				x_TRANSPORT_LGA.setDisable(false);
				x_TRANSPORT_LGA.setItems(transportService
						.getDropdownList("defaultstorelist", null));
				x_TRANSPORT_LGA.setValue(new LabelValueBean(
						transportBean.getX_TRANSPORT_LGA(), transportBean
								.getX_TRANSPORT_LGA_ID()));
				x_TRANSPORT_WARD.setItems(transportService.getDropdownList("WardList",
						transportBean.getX_TRANSPORT_LGA_ID()));
				x_TRANSPORT_WARD.setValue(new LabelValueBean(
						transportBean.getX_TRANSPORT_WARD(), transportBean
								.getX_TRANSPORT_WARD_ID()));
				x_TRANSPORT_FACILITY.setItems(transportService.getDropdownList("FacilityList",
						transportBean.getX_TRANSPORT_WARD_ID()));
				x_TRANSPORT_FACILITY.setValue(new LabelValueBean(
                                                transportBean.getX_TRANSPORT_FACILITY_NAME(),transportBean
                                                                .getX_TRANSPORT_FACILITY_ID()));
//                                x_TRANSPORT_LOCATION.setItems(transportService.getDropdownList("EquipmentLocationList",
//                                                transportBean.getX_TRANSPORT_FACILITY_ID()));
				x_TRANSPORT_LOCATION.setValue(transportBean.getX_TRANSPORT_LOCATION());
			} else if (MainApp.getUserRole().getLabel().equals("SCCO")
					&& CustomChoiceDialog.selectedLGA != null) {
				// get only the selected LGA name in the list
				x_TRANSPORT_STATE.setValue(new FacilityService()
						.getStateStoreId(userBean.getX_USER_WAREHOUSE_ID()));
				x_TRANSPORT_STATE.setDisable(true);
				x_TRANSPORT_LGA.setDisable(true);
				x_TRANSPORT_LGA.setItems(transportService
						.getDropdownList("defaultstorelist", null));
				x_TRANSPORT_LGA.setValue(new LabelValueBean(
						transportBean.getX_TRANSPORT_LGA(), transportBean
								.getX_TRANSPORT_LGA_ID()));
				x_TRANSPORT_WARD.setItems(transportService.getDropdownList("WardList",
						transportBean.getX_TRANSPORT_LGA_ID()));
				x_TRANSPORT_WARD.setValue(new LabelValueBean(
						transportBean.getX_TRANSPORT_WARD(), transportBean
								.getX_TRANSPORT_WARD_ID()));
				new SelectKeyComboBoxListener(x_TRANSPORT_WARD);
				x_TRANSPORT_FACILITY.setItems(transportService.getDropdownList("FacilityList",
						transportBean.getX_TRANSPORT_WARD_ID()));
				x_TRANSPORT_FACILITY.setValue(new LabelValueBean(transportBean.getX_TRANSPORT_FACILITY_NAME(),
						transportBean.getX_TRANSPORT_FACILITY_ID()));
//                                x_TRANSPORT_LOCATION.setItems(transportService.getDropdownList("EquipmentLocationList",
//                                                transportBean.getX_TRANSPORT_FACILITY_ID()));
				x_TRANSPORT_LOCATION.setValue(transportBean.getX_TRANSPORT_LOCATION());
			} else if (MainApp.getUserRole().getLabel().equals("CCO")) {
				// get only the selected LGA name in the list
				x_TRANSPORT_STATE.setValue(new FacilityService()
						.getStateStoreId(transportBean.getX_TRANSPORT_LGA_ID()));
				x_TRANSPORT_STATE.setDisable(true);
				x_TRANSPORT_LGA.setDisable(true);
				x_TRANSPORT_LGA.setValue(new LabelValueBean(
						transportBean.getX_TRANSPORT_LGA(), transportBean
								.getX_TRANSPORT_LGA_ID()));
				x_TRANSPORT_WARD.setItems(transportService.getDropdownList("WardList",
						transportBean.getX_TRANSPORT_LGA_ID()));
				x_TRANSPORT_WARD.setValue(new LabelValueBean(transportBean.getX_TRANSPORT_WARD(),
						transportBean.getX_TRANSPORT_WARD_ID()));
				new SelectKeyComboBoxListener(x_TRANSPORT_WARD);
				x_TRANSPORT_FACILITY.setItems(transportService.getDropdownList("FacilityList",
						transportBean.getX_TRANSPORT_WARD_ID()));
				x_TRANSPORT_FACILITY.setValue(new LabelValueBean(transportBean.getX_TRANSPORT_FACILITY_NAME(),
						transportBean.getX_TRANSPORT_FACILITY_ID()));
//                                x_TRANSPORT_LOCATION.setItems(transportService.getDropdownList("EquipmentLocationList",
//                                                transportBean.getX_TRANSPORT_FACILITY_ID()));
				x_TRANSPORT_LOCATION.setValue(transportBean.getX_TRANSPORT_LOCATION());
			}

			if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA == null) {
				x_TRANSPORT_STATE.setItems(transportService.getDropdownList(
						"defaultstorelist", null));
				new SelectKeyComboBoxListener(x_TRANSPORT_STATE);
				x_TRANSPORT_STATE.setValue((new LabelValueBean(transportBean
						.getX_TRANSPORT_STATE(), transportBean
						.getX_TRANSPORT_STATE_ID())));
				x_TRANSPORT_LGA.setItems(transportService
						.getDropdownList("defaultstorelist", x_TRANSPORT_STATE
								.getValue().getValue()));
				x_TRANSPORT_LGA.setValue(new LabelValueBean(
						transportBean.getX_TRANSPORT_LGA(), transportBean
								.getX_TRANSPORT_LGA_ID()));
				x_TRANSPORT_WARD.setItems(transportService.getDropdownList("WardList",
						transportBean.getX_TRANSPORT_LGA_ID()));
				x_TRANSPORT_WARD.setValue(new LabelValueBean(transportBean.getX_TRANSPORT_WARD(),
						transportBean.getX_TRANSPORT_WARD_ID()));
				x_TRANSPORT_FACILITY.setItems(transportService.getDropdownList("FacilityList",
						transportBean.getX_TRANSPORT_WARD_ID()));
				x_TRANSPORT_FACILITY.setValue(new LabelValueBean(transportBean.getX_TRANSPORT_FACILITY_NAME(),
						transportBean.getX_TRANSPORT_FACILITY_ID()));
//                                x_TRANSPORT_LOCATION.setItems(transportService.getDropdownList("EquipmentLocationList",
//                                                transportBean.getX_TRANSPORT_FACILITY_ID()));
				x_TRANSPORT_LOCATION.setValue(transportBean.getX_TRANSPORT_LOCATION());
			} else if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA != null) {
				// get only the selected LGA name in the list
				x_TRANSPORT_STATE.setDisable(true);
				// x_TRANSPORT_STATE.setItems(transportService.getDropdownList("defaultstorelist",null));
				x_TRANSPORT_STATE.setValue(new LabelValueBean(transportBean
						.getX_TRANSPORT_STATE(), transportBean
						.getX_TRANSPORT_STATE_ID()));
				x_TRANSPORT_LGA.setItems(transportService
						.getDropdownList("defaultstorelist", x_TRANSPORT_STATE
								.getValue().getValue()));
				x_TRANSPORT_LGA.setValue(new LabelValueBean(
						transportBean.getX_TRANSPORT_LGA(), transportBean
								.getX_TRANSPORT_LGA_ID()));
				x_TRANSPORT_WARD.setItems(transportService.getDropdownList("WardList",
						transportBean.getX_TRANSPORT_LGA_ID()));
				x_TRANSPORT_WARD.setValue(new LabelValueBean(transportBean.getX_TRANSPORT_WARD(),
						transportBean.getX_TRANSPORT_WARD_ID()));
				x_TRANSPORT_FACILITY.setItems(transportService.getDropdownList("FacilityList",
						transportBean.getX_TRANSPORT_WARD_ID()));
				x_TRANSPORT_FACILITY.setValue(new LabelValueBean(transportBean.getX_TRANSPORT_FACILITY_NAME(),
						transportBean.getX_TRANSPORT_FACILITY_ID()));
//                                x_TRANSPORT_LOCATION.setItems(transportService.getDropdownList("EquipmentLocationList",
//                                                transportBean.getX_TRANSPORT_FACILITY_ID()));
				x_TRANSPORT_LOCATION.setValue(transportBean.getX_TRANSPORT_LOCATION());
			}
                        
                        if (!(transportBean.getX_TRANSPORT_TYPE() == null)) {
                                x_TRANSPORT_TYPE.setItems(transportService.getDropdownList("TransportTypeList"));
                                x_TRANSPORT_TYPE.setValue(new LabelValueBean(transportBean.getX_TRANSPORT_TYPE(),null));
                        }
                        
                        if (!(transportBean.getX_TRANSPORT_MAKE() == null)) {
                                x_TRANSPORT_MAKE.setItems(transportService.getDropdownList("MakeList",x_TRANSPORT_TYPE.getValue().getLabel()));
                                System.out.println("Value of Type re o "+x_TRANSPORT_TYPE.getValue().getLabel());
                                x_TRANSPORT_MAKE.setValue(new LabelValueBean(transportBean.getX_TRANSPORT_MAKE(),null));
                        }
                        
                        if (!(transportBean.getX_TRANSPORT_MODEL() == null)) {
                                x_TRANSPORT_MODEL.setItems(transportService.getDropdownList("ModelList",x_TRANSPORT_MAKE.getValue().toString()));
                                System.out.println("Value of Make re o "+x_TRANSPORT_MAKE.getValue().getLabel());
                                x_TRANSPORT_MODEL.setValue(new LabelValueBean(transportBean.getX_TRANSPORT_MODEL(),null));
                        }

                        if (!(transportBean.getX_TRANSPORT_AGE() == null)) {
                            System.out.println("The transport Age is "+transportBean.getX_TRANSPORT_AGE());
                             x_TRANSPORT_AGE.setValue(CalendarUtil.fromString(transportBean.getX_TRANSPORT_AGE()));
//                            x_TRANSPORT_AGE.setValue(LocalDate.parse(transportBean.getX_TRANSPORT_AGE()));
                        }

                        if (transportBean.getX_TRANSPORT_FUNCTIONAL() != null){
                            System.out.println("transportBean.Functional is "+transportBean.getX_TRANSPORT_FUNCTIONAL());
                            status = transportBean.getX_TRANSPORT_FUNCTIONAL();
                            if(status.equals("F")){
                                x_TRANSPORT_STATUS.setSelected(true);
                                handleFunctionalStatus();
                            }else{
                                x_TRANSPORT_STATUS.setSelected(false); 
                                handleFunctionalStatus(); 
                            }
                        }

                        if (transportBean.getX_TRANSPORT_FUEL_AVAILABLE() != null){
                            System.out.println("transportBean.FuelAvailable is "+transportBean.getX_TRANSPORT_FUEL_AVAILABLE());
                            if(transportBean.getX_TRANSPORT_FUEL_AVAILABLE().equals("Y")){
                                x_TRANSPORT_STATUS.setSelected(true);
                            }else{
                                x_TRANSPORT_STATUS.setSelected(false);
                            }
                        }

                        if (transportBean.getX_TRANSPORT_SERVICED()!= null){
                            System.out.println("transportBean.Serviced is "+transportBean.getX_TRANSPORT_SERVICED());
                            if(transportBean.getX_TRANSPORT_SERVICED().equals("Y")){
                                x_TRANSPORT_SERVICED.setSelected(true);
                            }else{
                                x_TRANSPORT_SERVICED.setSelected(false);
                            }
                        }

                        if (transportBean.getX_TRANSPORT_AWAITING_FUND()!= null){
                            System.out.println("transportBean.AwaitingFunds is "+transportBean.getX_TRANSPORT_AWAITING_FUND());
                            if(transportBean.getX_TRANSPORT_AWAITING_FUND().equals("Y")){
                                x_TRANSPORT_AWAITING_FUNDS.setSelected(true);
                            }else{
                                x_TRANSPORT_AWAITING_FUNDS.setSelected(false);
                            }
                        }

                        if (transportBean.getX_TRANSPORT_FUND_AVAILABLE()!= null){
                            System.out.println("transportBean.FundAvailable is "+transportBean.getX_TRANSPORT_FUND_AVAILABLE());
                            if(transportBean.getX_TRANSPORT_FUND_AVAILABLE().equals("Y")){
                                x_TRANSPORT_FUNDS.setSelected(true);
                            }else{
                                x_TRANSPORT_FUNDS.setSelected(false);
                            }
                        }

                        if (transportBean.getX_TRANSPORT_PPM_CONDUCTED()!= null){
                            System.out.println("transportBean.PPMConducted is "+transportBean.getX_TRANSPORT_PPM_CONDUCTED());
                            if(transportBean.getX_TRANSPORT_PPM_CONDUCTED().equals("Y")){
                                x_TRANSPORT_PPM.setSelected(true);
                            }else{
                                x_TRANSPORT_PPM.setSelected(false);
                            }
                        }

                        if (transportBean.getX_TRANSPORT_NUMBER_OF_HF()!= null){
                            System.out.println("transportBean.NoOfHF is "+transportBean.getX_TRANSPORT_NUMBER_OF_HF());
                            x_TRANSPORT_NUMBER_OF_HF.setText(transportBean.getX_TRANSPORT_NUMBER_OF_HF());
                        }

                        if (transportBean.getX_TRANSPORT_TARGET_POPULATION()!= null){
                            System.out.println("transportBean.TargetPop is "+transportBean.getX_TRANSPORT_TARGET_POPULATION());
                            x_TRANSPORT_TARGET_POPULATION.setText(transportBean.getX_TRANSPORT_TARGET_POPULATION());
                        }

                        if (transportBean.getX_TRANSPORT_DISTANCE()!= null){
                            System.out.println("transportBean.Distance is "+transportBean.getX_TRANSPORT_DISTANCE());
                            x_TRANSPORT_DISTANCE.setText(transportBean.getX_TRANSPORT_DISTANCE());
                        }
                
		}
		
		
	}


	@FXML
	private void handleFunctionalStatus() {
            if(x_TRANSPORT_STATUS.isSelected() ){
//                x_TRANSPORT_NF_DATE.setDisable(true);
//                x_TRANSPORT_NF_DATE.setValue(null);
                x_TRANSPORT_NF_DURATION.setDisable(true);
                x_TRANSPORT_NF_DURATION.setText("0");
            }
            else{
//                x_TRANSPORT_NF_DATE.setDisable(false);
                x_TRANSPORT_NF_DURATION.setDisable(false);
            }
	}
        
        @FXML
        private void handleOnStateStoreChange() {
//                x_TRANSPORT_LGA.setPromptText("LGA");                                                                                                        // initial
//                x_TRANSPORT_WARD.setPromptText("Ward");                                                                                                        // initial
//                x_TRANSPORT_FACILITY.setPromptText("Facility Name");                                                                                                        // initial
//                x_TRANSPORT_LOCATION.setPromptText("Equipment Location");
                if (x_TRANSPORT_STATE.getValue() != null 
                        && !x_TRANSPORT_STATE.getValue().getLabel().equalsIgnoreCase("State")) {
//			x_TRANSPORT_LGA.getItems().addAll(
//					new LabelValueBean("LGA", null));
                        x_TRANSPORT_LGA.setValue(new LabelValueBean("", null));
                        x_TRANSPORT_WARD.setValue(new LabelValueBean("", null));
                        x_TRANSPORT_FACILITY.setValue(new LabelValueBean("", null));
                        x_TRANSPORT_LGA.setItems(transportService.getDropdownList(
                                        "defaultstorelist", x_TRANSPORT_STATE.getValue().getValue()));
                        new SelectKeyComboBoxListener(x_TRANSPORT_LGA);
			x_TRANSPORT_WARD.setItems(transportService.getDropdownList("WardList",
					x_TRANSPORT_STATE.getValue().getValue()));
//			x_TRANSPORT_FACILITY.setItems(transportService.getDropdownList("FacilityList",
//					x_TRANSPORT_WARD.getValue().getValue()));
//			x_TRANSPORT_LOCATION.setValue(transportService.getDropdownList("EquipmentLocationList",
//					x_TRANSPORT_STATE.getValue().getValue()).get(0));
                }
        }
        @FXML
	private void handleOnLgaStoreChange() {
//                x_TRANSPORT_WARD.setPromptText("Ward");                                                                                                    // initial
//                x_TRANSPORT_LOCATION.setPromptText("Equipment Location");
//                x_TRANSPORT_FACILITY.setPromptText("Facility Name");
		if (x_TRANSPORT_LGA.getValue() != null
                        && !x_TRANSPORT_LGA.getValue().getLabel().equalsIgnoreCase("LGA")) {
                        x_TRANSPORT_WARD.setValue(new LabelValueBean("", null));
                        x_TRANSPORT_FACILITY.setValue(new LabelValueBean("", null));
//			x_TRANSPORT_WARD.getItems().addAll(
//					new LabelValueBean("Ward", null));
//			x_TRANSPORT_FACILITY.getItems().addAll(
//					new LabelValueBean("Facility Name", null));
			x_TRANSPORT_WARD.setItems(transportService.getDropdownList("WardList",
					x_TRANSPORT_LGA.getValue().getValue()));
			new SelectKeyComboBoxListener(x_TRANSPORT_WARD);
			x_TRANSPORT_FACILITY.setItems(transportService.getDropdownList("FacilityList",
					x_TRANSPORT_LGA.getValue().getValue()));
//			x_TRANSPORT_LOCATION.setValue(transportService.getDropdownList("EquipmentLocationList",
//					x_TRANSPORT_LGA.getValue().getValue()).get(0));
		}

	}
        @FXML
	private void handleOnWardStoreChange() {
//		x_TRANSPORT_FACILITY.setPromptText("Facility Name");
//                x_TRANSPORT_LOCATION.setPromptText("Equipment Location");
		if (x_TRANSPORT_WARD.getValue() != null
                        && !x_TRANSPORT_WARD.getValue().getLabel().equalsIgnoreCase("Ward")) {
                        x_TRANSPORT_LOCATION.setValue("");
                        x_TRANSPORT_FACILITY.setValue(new LabelValueBean("", null));
			x_TRANSPORT_FACILITY.getItems().addAll(
					new LabelValueBean("Facility Name", null));
			x_TRANSPORT_FACILITY.setItems(transportService.getDropdownList("FacilityList",
					x_TRANSPORT_WARD.getValue().getValue()));
			new SelectKeyComboBoxListener(x_TRANSPORT_FACILITY);
//			x_TRANSPORT_LOCATION.setValue(transportService.getDropdownList("EquipmentLocationList",
//					x_TRANSPORT_WARD.getValue().getValue()).get(0));
		}

	}
        @FXML
	private void handleOnFacilityChange() {
		x_TRANSPORT_LOCATION.setPromptText("Equipment Location");
		if (x_TRANSPORT_FACILITY.getValue() != null
                        && !x_TRANSPORT_FACILITY.getValue().getLabel().equalsIgnoreCase("Facility Name")) {
//			x_TRANSPORT_LOCATION.getItems().addAll(
//					new LabelValueBean("Equipment Location", null));
//			x_TRANSPORT_LOCATION.setValue(transportService.getDropdownList("EquipmentLocationList",
//					x_TRANSPORT_FACILITY.getValue().getValue()).get(0));
			//x_TRANSPORT_LOCATION.setValue(x_TRANSPORT_LOCATION.getItems().get(0));
		}

	}
        
        @FXML
	private void handleOnTypeChange() {
		x_TRANSPORT_MAKE.setPromptText("Make");
                //System.out.println("On handleType ni o"+x_TRANSPORT_TYPE.getValue().getLabel());
		if (x_TRANSPORT_TYPE.getValue() != null
                        && !x_TRANSPORT_TYPE.getValue().getLabel().equalsIgnoreCase("Type of Transport")) {
//			x_TRANSPORT_LOCATION.getItems().addAll(
//					new LabelValueBean("Equipment Location", null));
			x_TRANSPORT_MAKE.setItems(transportService.getDropdownList("MakeList",
					x_TRANSPORT_TYPE.getValue().getLabel()));
                        x_TRANSPORT_OWNER.setItems(transportService.getDropdownList("OwnerList"));
			//x_TRANSPORT_LOCATION.setValue(x_TRANSPORT_LOCATION.getItems().get(0));
		}

	}
        
        @FXML
	private void handleOnMakeChange() {
		x_TRANSPORT_MODEL.setPromptText("Make");
		if (x_TRANSPORT_MAKE.getValue() != null
                        && !x_TRANSPORT_MAKE.getValue().getLabel().equalsIgnoreCase("Make")) {
			x_TRANSPORT_MODEL.setItems(transportService.getDropdownList("ModelList",
					x_TRANSPORT_MAKE.getValue().getLabel()));
		}

	}
        
	@FXML
	private void handleSubmitTransport() throws SQLException {
		if (isValidate(actionBtnString)) {
                        transportBean.setX_TRANSPORT_CREATED_BY(userBean.getX_USER_ID());
			transportBean.setX_TRANSPORT_UPDATED_BY(userBean.getX_USER_ID());
//			if (x_TRANSPORT_STATE != null
//					&& x_TRANSPORT_STATE.getValue() != null
//					&& !x_TRANSPORT_STATE.getValue().getLabel().equals("State")) {
//				transportBean.setX_TRANSPORT_STATE(x_TRANSPORT_STATE.getValue().getLabel());
//				transportBean.setX_TRANSPORT_STATE_ID(x_TRANSPORT_STATE.getValue().getValue());
//			}
//			if (x_TRANSPORT_LGA != null
//					&& x_TRANSPORT_LGA.getValue() != null
//					&& !x_TRANSPORT_LGA.getValue().getLabel().equals("LGA")) {
//				transportBean.setX_TRANSPORT_LGA(x_TRANSPORT_LGA.getValue().getLabel());
//				transportBean.setX_TRANSPORT_LGA_ID(x_TRANSPORT_LGA.getValue().getValue());
//			}
//			if (x_TRANSPORT_WARD != null 
//                                        && x_TRANSPORT_WARD.getValue() != null
//					&& !x_TRANSPORT_WARD.getValue().getLabel().equals("Ward")) {
//				transportBean.setX_TRANSPORT_WARD(x_TRANSPORT_WARD.getValue().getLabel());
//				transportBean.setX_TRANSPORT_WARD_ID(x_TRANSPORT_WARD.getValue().getValue());
////			}
//			if (x_TRANSPORT_LOCATION != null
//					&& !x_TRANSPORT_LOCATION.getValue().getLabel().equals("Equipment Location")) {
//				transportBean.setX_TRANSPORT_LOCATION(x_TRANSPORT_LOCATION.getValue().getLabel());
//                                System.out.println("Transport Location Label: "+transportBean.getX_TRANSPORT_LOCATION());
//			}
//			transportBean.setX_TRANSPORT_LOCATION_HAS_ELECTRICITY(x_LOCATION_HAS_ELECTRICITY.isSelected() ? "Y" : "N");
                        
//			if (x_TRANSPORT_FACILITY != null
//					&& !x_TRANSPORT_FACILITY.getValue().getLabel().equals("Facility Name")) {
//				transportBean.setX_TRANSPORT_FACILITY_NAME(x_TRANSPORT_FACILITY.getValue().getLabel());
//				transportBean.setX_TRANSPORT_FACILITY_ID(x_TRANSPORT_FACILITY.getValue().getValue());
//                                System.out.println("Transport Facility Name Label: "+transportBean.getX_TRANSPORT_FACILITY_NAME());
//			}
                        
                        if (x_TRANSPORT_TYPE != null
					&& !String.valueOf(x_TRANSPORT_TYPE.getValue()).equals("Type")) {
				transportBean.setX_TRANSPORT_TYPE(String.valueOf(x_TRANSPORT_TYPE.getValue()));
                                System.out.println("Transport Type Label: "+transportBean.getX_TRANSPORT_TYPE());
			}
                        
//                        if (x_TRANSPORT_MAKE != null
//					&& !String.valueOf(x_TRANSPORT_MAKE.getValue()).equals("Make")) {
//				transportBean.setX_TRANSPORT_MAKE(String.valueOf(x_TRANSPORT_MAKE.getValue()));
//                                System.out.println("Transport Make Label: "+transportBean.getX_TRANSPORT_MAKE());
//			}
//                        
//                        if (x_TRANSPORT_MODEL != null
//					&& !String.valueOf(x_TRANSPORT_MODEL.getValue()).equals("Model")) {
//				transportBean.setX_TRANSPORT_MODEL(String.valueOf(x_TRANSPORT_MODEL.getValue()));
//                                System.out.println("Transport Model Label: "+transportBean.getX_TRANSPORT_MODEL());
//			}
                        if (x_TRANSPORT_AGE.getValue() != null) {
				transportBean.setX_TRANSPORT_AGE(x_TRANSPORT_AGE.getValue().toString());
			} else {
				transportBean.setX_TRANSPORT_AGE(null);
			}
                        
                        if (x_TRANSPORT_OWNER != null
					&& !String.valueOf(x_TRANSPORT_OWNER.getValue()).equals("Owner")) {
				transportBean.setX_TRANSPORT_OWNER(String.valueOf(x_TRANSPORT_OWNER.getValue()));
                                System.out.println("Transport Owner Label: "+transportBean.getX_TRANSPORT_OWNER());
			}
                        
			transportBean.setX_TRANSPORT_FUNCTIONAL(x_TRANSPORT_STATUS.isSelected() ? "Y": "N");
			transportBean.setX_TRANSPORT_FUEL_AVAILABLE(x_TRANSPORT_FUEL_AVAILABLE.isSelected() ? "Y": "N");
			transportBean.setX_TRANSPORT_SERVICED(x_TRANSPORT_SERVICED.isSelected() ? "Y": "N");
                        
			transportBean.setX_TRANSPORT_PPM_CONDUCTED(x_TRANSPORT_PPM.isSelected() ? "Y": "N");
                        transportBean.setX_TRANSPORT_DURATION_NF(x_TRANSPORT_NF_DURATION.getText());
			
			if (transportService == null)
				transportService = new TransportService();
			if (actionBtnString.equals("search")) {
//				transportMain.refreshTransportTable(transportService
//						.getSearchList(transportBean));
				okClicked = true;
				dialogStage.close();
				// DatabaseOperation.getDbo().closeConnection();
				// DatabaseOperation.setDbo(null);
			} else {
				String masthead;
				String message;
				if (actionBtnString.equals("add")) {
					masthead = "Successfully Added!";
					message = "Transport is Saved to the Transport List";
				} else {
					masthead = "Successfully Updated!";
					message = "Transport is Updated to the Transport List";
				}
				transportService.saveTransport(transportBean, actionBtnString);
				transportMain.refreshTransportTable();
				okClicked = true;
				org.controlsfx.dialog.Dialogs.create().owner(dialogStage)
						.title("Information").masthead(masthead)
						.message(message).showInformation();
				dialogStage.close();
				// DatabaseOperation.getDbo().closeConnection();
				// DatabaseOperation.setDbo(null);
			}
		}
	}

	public boolean isValidate(String actionBtnString) {
		if (!actionBtnString.equals("search")) {
			String errorMessage = "";
//			if (x_WAREHOUSE_CODE.getText() == null
//					|| x_WAREHOUSE_CODE.getText().length() == 0) {
//				errorMessage += "No valid Transport code!\n";
//			}
                            System.out.println("Here is the Ward "+x_TRANSPORT_WARD.getValue());
			
//			if (x_TRANSPORT_STATE.getValue() == null
//                                || x_TRANSPORT_STATE.getValue().toString().length() == 0) {
//				errorMessage += "No State selected!\n";
//			}
//			if (x_TRANSPORT_LGA.getValue() == null
//                                || x_TRANSPORT_LGA.getValue().toString().length() == 0) {
//				errorMessage += "No LGA selected!\n";
//			}
//			if (x_TRANSPORT_WARD.getValue() == null
//                                || x_TRANSPORT_WARD.getValue().toString().length() == 0) {
//                            //System.out.println("Here is the Ward 2 "+x_TRANSPORT_WARD.getValue().toString());
//				errorMessage += "No Ward selected!\n";
//			}
//			if (x_TRANSPORT_LOCATION.getValue()==null
//                                || x_TRANSPORT_LOCATION.getValue().toString().length() == 0) {
//				errorMessage += "No Location Type selected!\n";
//			}
//                        
//			if (x_TRANSPORT_FACILITY.getValue()==null
//                                || x_TRANSPORT_FACILITY.getValue().toString().length() == 0) {
//				errorMessage += "No Facility selected!\n";
//			}
                        
			if (x_TRANSPORT_TYPE.getValue()==null
                                || String.valueOf(x_TRANSPORT_TYPE.getValue()).length() == 0) {
				errorMessage += "No Type selected!\n";
			}
                        
			if (!x_TRANSPORT_STATUS.isSelected() 
                                && ((x_TRANSPORT_NF_DURATION.getText().equals("0"))||(x_TRANSPORT_NF_DURATION.getText().equals("")))){
				errorMessage += "Enter NF Duration!\n";
			}
//			
//                        if (x_TRANSPORT_MODEL.getValue()==null
//                                || String.valueOf(x_TRANSPORT_MODEL.getValue()).length() == 0) {
//				errorMessage += "No Transport Model selected!\n";
//			}
                        if (x_TRANSPORT_AGE.getValue() == null 
                                || x_TRANSPORT_AGE.getValue().toString().length() == 0) {
				errorMessage += "No Valid Age date selected!\n";
			}
                        if (x_TRANSPORT_OWNER.getValue()==null
                                || String.valueOf(x_TRANSPORT_OWNER.getValue()).length() == 0) {
				errorMessage += "No Transport Owner selected!\n";
			}
                        
                        if (errorMessage.length() == 0) {
				return true;
			} else {
				// Show the error message
				Dialogs.create().owner(dialogStage)
						.title("Invalid Fields Error")
						.masthead("Please correct invalid fields")
						.message(errorMessage).showError();
				return false;
			}
		} else
			return true;
	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleCancelTransport() {
		dialogStage.close();
		// DatabaseOperation.getDbo().closeConnection();
		// DatabaseOperation.setDbo(null);
	}

	public void setRole(LabelValueBean role) {
		this.role = role;
		switch (role.getLabel()) {
		case "LIO": // LIO - SUPER USER
                    x_TRANSPORT_STATE.setDisable(true);
                    x_TRANSPORT_LGA.setDisable(true);
                    x_TRANSPORT_LOCATION.getItems().addAll("LGA","Ward","Facility");
			break;
		case "MOH": 
                    x_TRANSPORT_LOCATION.getItems().addAll("LGA","Ward","Facility");
			break;
		case "SIO": // SIO
                    x_TRANSPORT_STATE.setDisable(true);
                    x_TRANSPORT_LOCATION.getItems().addAll("State","LGA","Ward","Facility");
			break;
		case "SCCO": // SCCO
                    x_TRANSPORT_STATE.setDisable(true);
                    x_TRANSPORT_LOCATION.getItems().addAll("State","LGA","Ward","Facility");
			break;
		case "SIFP": // SIFP
                    x_TRANSPORT_STATE.setDisable(true);
                    x_TRANSPORT_LOCATION.getItems().addAll("State","LGA","Ward","Facility");
			break;
		case "CCO": // CCO - EMPLOYEE
                    x_TRANSPORT_STATE.setDisable(true);
                    x_TRANSPORT_LGA.setDisable(true);
                    x_TRANSPORT_LOCATION.getItems().addAll("LGA","Ward","Facility");
			break;
		}
	}
}
