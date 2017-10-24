package com.chai.inv.service;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import com.chai.inv.CustomChoiceDialog;
import com.chai.inv.MainApp;
import com.chai.inv.DAO.DatabaseOperation;
import com.chai.inv.logger.MyLogger;
import com.chai.inv.model.TransportBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.util.CalendarUtil;

public class TransportService {
	DatabaseOperation dao;
	PreparedStatement pstmt;
	ResultSet rs;

	public ObservableList<LabelValueBean> getDropdownList(String... action) {
		String x_QUERY = null;
		switch (action[0]) {
		case "StateList":
			x_QUERY = "SELECT STATE_ID,"
					+ "		  STATE_NAME "
					+ "  FROM VIEW_STATES "
					+ " WHERE STATE_NAME IS NOT NULL "
					+ "   AND STATE_NAME <> '' AND STATUS='A' AND COUNTRY_ID = "
					+ action[1] + " ORDER BY STATE_NAME ";
			break;
		case "defaultstorelist":
			if (MainApp.getUserRole() != null
					&& CustomChoiceDialog.selectedLGA == null
					&& (MainApp.getUserRole().getLabel().equals("SCCO")
							|| MainApp.getUserRole().getLabel().equals("SIO") 
							|| MainApp.getUserRole().getLabel().equals("SIFP"))) {
				x_QUERY = "SELECT WAREHOUSE_ID, "
						+ "		  WAREHOUSE_NAME "
						+ "  FROM VIEW_INVENTORY_WAREHOUSES "
						+ " WHERE STATUS = 'A' AND DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()
						+ " ORDER BY WAREHOUSE_NAME ASC";
			} else if (MainApp.getUserRole().getLabel().equals("NTO")) {
				if (action[1] == null) {
					x_QUERY = "SELECT WAREHOUSE_ID, "
							+ "		  WAREHOUSE_NAME "
							+ "  FROM VIEW_INVENTORY_WAREHOUSES "
							+ " WHERE STATUS = 'A' AND DEFAULT_ORDERING_WAREHOUSE_ID = "
							+ MainApp.getUSER_WAREHOUSE_ID()
							+ " ORDER BY WAREHOUSE_NAME ASC";
				} else {
					x_QUERY = "SELECT WAREHOUSE_ID, "
							+ "		  WAREHOUSE_NAME "
							+ "  FROM VIEW_INVENTORY_WAREHOUSES "
							+ " WHERE STATUS = 'A' AND DEFAULT_ORDERING_WAREHOUSE_ID = "
							+ action[1] + " ORDER BY WAREHOUSE_NAME ASC";
				}

			} else {
				System.out.println("***************s**************");
				x_QUERY = "SELECT WAREHOUSE_ID, " 
						+ "		  WAREHOUSE_NAME "
						+ "  FROM VIEW_INVENTORY_WAREHOUSES "
						+ " WHERE STATUS = 'A' AND WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID();
			}
			break;
		case "WardList":
			x_QUERY = " SELECT TYPE_ID, "
					+ "        TYPE_CODE  "
					+ "   FROM TYPES  "
					+ "  WHERE SOURCE_TYPE='CUSTOMER TYPE' AND STATUS='A' AND WAREHOUSE_ID = "
					+ action[1] + "  ORDER BY TYPE_CODE ";
			break;
		case "FacilityList":
			if (action[1] != null) {
				x_QUERY = " SELECT CUSTOMER_ID, " 
						+ "        CUSTOMER_NAME  "
						+ "   FROM VIEW_CUSTOMERS  "
						//+ "  WHERE DEFAULT_STORE_ID = "+ action[1] 
						+ "  WHERE CUSTOMER_TYPE_ID = "+ action[1] 
						+ "  ORDER BY CUSTOMER_NAME ";
			} else {
				x_QUERY = " SELECT CUSTOMER_ID, "
						+ "        CUSTOMER_NAME  "
						+ "   FROM VIEW_CUSTOMERS  "
						+ "  WHERE DEFAULT_STORE_ID IN "
						+ " (SELECT WAREHOUSE_ID FROM VIEW_INVENTORY_WAREHOUSES "
						+ " WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID() + ") "
						+ "  ORDER BY CUSTOMER_NAME ";
			}
			break;
//		case "EquipmentLocationList":
//			x_QUERY = " SELECT TYPE_ID, TYPE_NAME"
//					+ "   FROM TYPES WHERE TYPE_ID = ("
//                                        + " SELECT WAREHOUSE_TYPE_ID FROM VIEW_STORE_TYPES "
//                                        + " WHERE FACILITY_ID = "+action[1]+")";
//			break;
		case "EquipmentLocationList":
			x_QUERY = " SELECT WAREHOUSE_TYPE_ID, LOCATION"
					+ "   FROM VIEW_STORE_TYPES "
                                + "WHERE FACILITY_ID = "+action[1]
                                + " OR WAREHOUSE_TYPE_ID = "+action[1];
			break;
		case "TransportTypeList":
			x_QUERY = " SELECT TRANSPORT_DATA_ID, TYPE_OF_TRANSPORT "
					+ "   FROM E002 GROUP BY TYPE_OF_TRANSPORT ASC ";
			break;
		case "MakeList":
			x_QUERY =  " SELECT TRANSPORT_DATA_ID, MAKE "
					+ "   FROM E002 "
                                        + " WHERE TYPE_OF_TRANSPORT = '"+ action[1]
                                        + "' GROUP BY MAKE ASC  ";
			break;
		case "ModelList":
			x_QUERY = " SELECT TRANSPORT_DATA_ID, MODEL "
					+ "   FROM E002 "
                                + " WHERE MAKE = '"+ action[1]
                                + "' GROUP BY MODEL ASC  ";
			break;
		case "OwnerList":
			x_QUERY = " SELECT TRANSPORT_DATA_ID, OWNER "
					+ "   FROM E002 GROUP BY OWNER ASC  ";
			break;
		}
		try {
			return DatabaseOperation.getDropdownList(x_QUERY);
		} catch (SQLException | NullPointerException ex) {
			System.out.println("An error occured while getting Transport form "+ " drop down menu lists, error: "
		+ ex.getMessage());
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe("An error occured while getting Transport form "
					+ " drop down menu lists, error: \n"+
			MyLogger.getStackTrace(ex));
		}
		System.out.println("return null");
		return null;
	}

	public ObservableList<TransportBean> getTransportList() {
		String WHERE_CONDITION = "";
		ObservableList<TransportBean> transportData = FXCollections.observableArrayList();
		try {
			if (dao == null || dao.getConnection() == null || dao.getConnection().isClosed()) {
				System.out.println("**In TransportService.getTransportList() ");
				dao = DatabaseOperation.getDbo();
			}
		} catch (SQLException e) {
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe(MyLogger.getStackTrace(e));
			e.printStackTrace();
		}

		if (MainApp.getUserRole() != null) {
			if ((MainApp.getUserRole().getLabel().equals("SIO")
					|| MainApp.getUserRole().getLabel().equals("SCCO") || MainApp
					.getUserRole().getLabel().equals("SIFP"))
					&& CustomChoiceDialog.selectedLGA == null) {
				// IF STATE ADMINS DOES NOT SELECT LGA
				WHERE_CONDITION = " WHERE FACILITY_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()
						+ " OR DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()
						+ " OR FACILITY_ID IN ( SELECT FACILITY_ID FROM VIEW_E002 WHERE DEFAULT_ORDERING_WAREHOUSE_ID"
                                        + " IN (SELECT FACILITY_ID FROM VIEW_E002 WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()+" ) )";
                                
			} else if ((MainApp.getUserRole().getLabel().equals("SIO")
					|| MainApp.getUserRole().getLabel().equals("SCCO") || MainApp
					.getUserRole().getLabel().equals("SIFP"))
					&& CustomChoiceDialog.selectedLGA != null) {
				// IF STATE ADMINS SELECTS LGA
                                WHERE_CONDITION = " WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()
						+ " OR FACILITY_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID();

			} else if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA != null) {
				WHERE_CONDITION = " WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()
						+ " OR FACILITY_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID();
			} else if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA == null) {
				WHERE_CONDITION = "";
			} else if (MainApp.getUserRole().getLabel().equals("LIO")||MainApp.getUserRole().getLabel().equals("CCO")) {
				WHERE_CONDITION = " WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()
						+ " OR FACILITY_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID();
			} else {
				WHERE_CONDITION = " WHERE FACILITY_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID();
			}
		}
		pstmt = dao.getPreparedStatement("SELECT STATE_ID, "
                                + "STATE, "
                                + "LGA_ID, "
                                + "LGA, "
                                + "FACILITY_ID, "
                                + "FACILITY_NAME, "
                                + "WARD, "
                                + "WAREHOUSE_TYPE_ID, "
                                + "LOCATION, "
                                + "DEFAULT_ORDERING_WAREHOUSE_ID, "
                                + "MONTHLY_TARGET_POPULATION, "
                                + "TRANSPORT_DATA_ID, "
                                + "NUMBER_OF_HF_SERVED, "
                                + "TYPE_OF_TRANSPORT, "
                                + "MAKE, "
                                + "MODEL, "
                                + "OWNER, "
                                + "DATE_FORMAT(AGE, '%d-%b-%Y') AGE, "
                                + "VEHICLE_SERVICED, "
                                + "STATUS, "
                                + "FUEL_PURCHASED, "
                                + "PPM_CONDUCTED, "
                                + "AWAITING_FUNDS, "
                                + "DURATION_NF, "
                                + "FUND_AVAILABLE, "
                                + "DISTANCE_FROM_VACCINE_SOURCE"
                                + " FROM VIEW_E002 "+ WHERE_CONDITION);
		try {
			rs = pstmt.executeQuery();
			while (rs.next()) {
				TransportBean transportBean = new TransportBean();
                                transportBean.setX_TRANSPORT_STATE_ID(rs.getString("STATE_ID"));
				transportBean.setX_TRANSPORT_STATE(rs.getString("STATE"));
                                transportBean.setX_TRANSPORT_LGA_ID(rs.getString("LGA_ID"));
				transportBean.setX_TRANSPORT_LGA(rs.getString("LGA"));
				transportBean.setX_TRANSPORT_WARD_ID(rs.getString("WAREHOUSE_TYPE_ID"));
				transportBean.setX_TRANSPORT_WARD(rs.getString("WARD"));
                                transportBean.setX_TRANSPORT_FACILITY_ID(rs.getString("FACILITY_ID"));
				transportBean.setX_TRANSPORT_FACILITY_NAME(rs.getString("FACILITY_NAME"));
                                transportBean.setX_TRANSPORT_TARGET_POPULATION(rs.getString("MONTHLY_TARGET_POPULATION"));
                                transportBean.setX_TRANSPORT_DATA_ID(rs.getString("TRANSPORT_DATA_ID"));
                                transportBean.setX_TRANSPORT_LOCATION(rs.getString("LOCATION"));
                                transportBean.setX_TRANSPORT_NUMBER_OF_HF(rs.getString("NUMBER_OF_HF_SERVED"));
                                transportBean.setX_TRANSPORT_TYPE(rs.getString("TYPE_OF_TRANSPORT"));
                                transportBean.setX_TRANSPORT_MAKE(rs.getString("MAKE"));
                                transportBean.setX_TRANSPORT_MODEL(rs.getString("MODEL"));
                                transportBean.setX_TRANSPORT_OWNER(rs.getString("OWNER"));
                                transportBean.setX_TRANSPORT_AGE(rs.getString("AGE"));
                                transportBean.setX_TRANSPORT_SERVICED(rs.getString("VEHICLE_SERVICED"));
                                transportBean.setX_TRANSPORT_FUNCTIONAL(rs.getString("STATUS"));
                                transportBean.setX_TRANSPORT_FUEL_AVAILABLE(rs.getString("FUEL_PURCHASED"));
                                transportBean.setX_TRANSPORT_PPM_CONDUCTED(rs.getString("PPM_CONDUCTED"));
                                transportBean.setX_TRANSPORT_AWAITING_FUND(rs.getString("AWAITING_FUNDS"));
                                transportBean.setX_TRANSPORT_DURATION_NF(rs.getString("DURATION_NF"));
                                transportBean.setX_TRANSPORT_FUND_AVAILABLE(rs.getString("FUND_AVAILABLE"));
                                transportBean.setX_TRANSPORT_DISTANCE(rs.getString("DISTANCE_FROM_VACCINE_SOURCE"));
				
				
				transportData.add(transportBean);
			}
		} catch (Exception ex) {
			System.out.println("An error occured while Transport list, error: "+ ex.getMessage());
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe(MyLogger.getStackTrace(ex));
		} finally {
			System.out.println("Get Transport list query : " + pstmt.toString());
		}
		return transportData;
	}

	public boolean saveTransport(TransportBean transportBean,String actionBtnString) throws SQLException {
		boolean flag = true;
		try {
			if (dao == null || dao.getConnection() == null || dao.getConnection().isClosed()) {
				System.out.println("**In TransportService.getTransportList() ");
				dao = DatabaseOperation.getDbo();
			}
			if (actionBtnString.equals("add")) {
				pstmt = dao.getPreparedStatement("INSERT INTO E002"
                                                                + "(NUMBER_OF_HF_SERVED, "
                                                                + "TYPE_OF_TRANSPORT, "
                                                                + "MAKE, "
                                                                + "MODEL, "
                                                                + "OWNER, "
                                                                + "AGE, "
                                                                + "VEHICLE_SERVICED, "
                                                                + "STATUS, "
                                                                + "FUEL_PURCHASED, "
                                                                + "PPM_CONDUCTED, "
                                                                + "AWAITING_FUNDS, "
                                                                + "DURATION_NF, "
                                                                + "FUND_AVAILABLE, "//13
                                                                + "DISTANCE_FROM_VACCINE_SOURCE, "//14
                                                                + "LOCATION, "//15
                                                                + "FACILITY_ID, "
//                                                                + "START_DATE, "
//                                                                + "END_DATE, "
                                                                + "CREATED_BY, "
                                                                + "CREATED_ON, "
                                                                + "UPDATED_BY, "
                                                                + "LAST_UPDATED_ON) "
								+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,NOW(),?,NOW())");
                                
				pstmt.setString(16, MainApp.getUSER_WAREHOUSE_ID());
				pstmt.setString(17, transportBean.getX_TRANSPORT_CREATED_BY());
				pstmt.setString(18, transportBean.getX_TRANSPORT_UPDATED_BY());
			} else {
				pstmt = dao.getPreparedStatement("UPDATE E002 SET "
                                                                + "NUMBER_OF_HF_SERVED = ?, "
                                                                + "TYPE_OF_TRANSPORT = ?, "
                                                                + "MAKE = ?, "
                                                                + "MODEL = ?, "
                                                                + "OWNER = ?, "
                                                                + "AGE = ?, "
                                                                + "VEHICLE_SERVICED = ?, "
                                                                + "STATUS = ?, "
                                                                + "FUEL_PURCHASED = ?, "
                                                                + "PPM_CONDUCTED = ?, "
                                                                + "AWAITING_FUNDS = ?, "
                                                                + "DURATION_NF= ?, "
                                                                + "FUND_AVAILABLE = ?, "
                                                                + "DISTANCE_FROM_VACCINE_SOURCE = ?, "
                                                                + "LOCATION=?, " //15
                                                                + "UPDATED_BY=?, " //16
                                                                + "LAST_UPDATED_ON=NOW()"
								+ " WHERE TRANSPORT_DATA_ID=?"); //16
                                pstmt.setString(16,transportBean.getX_TRANSPORT_UPDATED_BY());
				pstmt.setString(17, transportBean.getX_TRANSPORT_DATA_ID());
			}
                        pstmt.setString(1, transportBean.getX_TRANSPORT_NUMBER_OF_HF());
                        pstmt.setString(2, transportBean.getX_TRANSPORT_TYPE());
                        pstmt.setString(3, transportBean.getX_TRANSPORT_MAKE());
                        pstmt.setString(4, transportBean.getX_TRANSPORT_MODEL());
                        pstmt.setString(5, transportBean.getX_TRANSPORT_OWNER());
                        pstmt.setString(6, transportBean.getX_TRANSPORT_AGE());
                        pstmt.setString(7, transportBean.getX_TRANSPORT_SERVICED());
                        pstmt.setString(8, transportBean.getX_TRANSPORT_FUNCTIONAL());
                        pstmt.setString(9, transportBean.getX_TRANSPORT_FUEL_AVAILABLE());
                        pstmt.setString(10, transportBean.getX_TRANSPORT_PPM_CONDUCTED());
                        pstmt.setString(11, transportBean.getX_TRANSPORT_AWAITING_FUND());
                        pstmt.setString(12, transportBean.getX_TRANSPORT_DURATION_NF());
                        pstmt.setString(13, transportBean.getX_TRANSPORT_FUND_AVAILABLE());
                        pstmt.setString(14, transportBean.getX_TRANSPORT_DISTANCE());
                        pstmt.setString(15, transportBean.getX_TRANSPORT_LOCATION());
			pstmt.executeUpdate();
		} catch (SQLException | NullPointerException ex) {
			flag = false;
			System.out.println("An error occured while saving/editing Transport, error: "+ ex.getMessage());
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe("An error occured while saving/editing Transport, error: "+MyLogger.getStackTrace(ex));
			return flag;
		} finally {
			System.out.println("warehouse insert/update query : "+ pstmt.toString());
		}
		return flag;
	}


}
