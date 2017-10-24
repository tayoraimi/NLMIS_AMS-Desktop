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
import com.chai.inv.model.GeneratorBean;
import com.chai.inv.model.LabelValueBean;
import com.chai.inv.util.CalendarUtil;

public class GeneratorService {
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
		case "ManufacturerList":
			x_QUERY = " SELECT GEN_DATA_ID, MANUFACTURER "
					+ "   FROM E001 GROUP BY MANUFACTURER ASC ";
			break;
		case "PowerList":
			x_QUERY =  " SELECT GEN_DATA_ID, POWER "
					+ "   FROM E001 GROUP BY POWER ASC  ";
			break;
		case "ModelList":
			x_QUERY = " SELECT GEN_DATA_ID, MODEL "
					+ "   FROM E001 GROUP BY MODEL ASC  ";
			break;
		case "StatusList":
			x_QUERY =  " SELECT GEN_DATA_ID, FUNCTIONAL "
					+ "   FROM E001 GROUP BY FUNCTIONAL ASC ";
			break;
		case "FuelTypeList":
			x_QUERY = " SELECT GEN_DATA_ID, FUEL_TYPE "
					+ "   FROM E001 GROUP BY FUEL_TYPE ASC  ";
			break;
//		case "EquipmentLocationList":
//			x_QUERY = " SELECT TYPE_ID, TYPE_DESCRIPTION"
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
		}
		try {
			return DatabaseOperation.getDropdownList(x_QUERY);
		} catch (SQLException | NullPointerException ex) {
			System.out.println("An error occured while getting Generator form "+ " drop down menu lists, error: "
		+ ex.getMessage());
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe("An error occured while getting Generator form "
					+ " drop down menu lists, error: \n"+
			MyLogger.getStackTrace(ex));
		}
		System.out.println("return null");
		return null;
	}

	public ObservableList<GeneratorBean> getGeneratorList() {
		String WHERE_CONDITION = "";
		ObservableList<GeneratorBean> generatorData = FXCollections.observableArrayList();
		try {
			if (dao == null || dao.getConnection() == null || dao.getConnection().isClosed()) {
				System.out.println("**In GeneratorService.getGeneratorList() ");
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
						+ " OR FACILITY_ID IN ( SELECT FACILITY_ID FROM VIEW_E001 WHERE DEFAULT_ORDERING_WAREHOUSE_ID"
                                        + " IN (SELECT FACILITY_ID FROM VIEW_E001 WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()+" ) )";
                                
			} else if ((MainApp.getUserRole().getLabel().equals("SIO")
					|| MainApp.getUserRole().getLabel().equals("SCCO") 
                                        || MainApp.getUserRole().getLabel().equals("SIFP"))
					&& CustomChoiceDialog.selectedLGA != null) {
				// IF STATE ADMINS SELECTS LGA
                                WHERE_CONDITION = " WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID()
						+ " OR FACILITY_ID = "
						+ MainApp.getUSER_WAREHOUSE_ID();
//				WHERE_CONDITION = " WHERE DEFAULT_ORDERING_WAREHOUSE_ID IN (SELECT viw.FACILITY_ID FROM VIEW_E001 viw"
//						+ " WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "
//						+ MainApp.getUSER_WAREHOUSE_ID()
//						+ " OR DEFAULT_ORDERING_WAREHOUSE_ID = "
//						+ MainApp.getUSER_WAREHOUSE_ID();

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
		pstmt = dao.getPreparedStatement("SELECT STATE_ID,"
                                + "STATE, "
                                + "LGA_ID, "
                                + "LGA, "
                                + "WAREHOUSE_TYPE_ID, "
                                + "WARD, "
                                + "FACILITY_ID, "
                                + "FACILITY_NAME, "
                                + "LOCATION, "
                                + "DEFAULT_ORDERING_WAREHOUSE_ID, "
                                + "FACILITY_HAS_ELECTRICITY, "
                                + "ELECTRICITY_HRS, "
                                + "MANUFACTURER, "
                                + "MODEL, "
                                + "POWER, "
                                + "FUNCTIONAL, "
                                + "DATE_FORMAT(DATE_NOT_FUNCTIONAL, '%d-%b-%Y') DATE_NOT_FUNCTIONAL, "
                                + "AGE, "
                                + "FUEL_TYPE, "
                                + "FUEL_AVAILABLE, "
                                + "PPM, "
                                + "PLANNED_REPAIRS, "
                                + "DURATION_NF"
                                + " FROM VIEW_E001 "+ WHERE_CONDITION);
		try {
			rs = pstmt.executeQuery();
			while (rs.next()) {
				GeneratorBean generatorBean = new GeneratorBean();
                                generatorBean.setX_GENERATOR_STATE_ID(rs.getString("STATE_ID"));
				generatorBean.setX_GENERATOR_STATE(rs.getString("STATE"));
                                generatorBean.setX_GENERATOR_LGA_ID(rs.getString("LGA_ID"));
				generatorBean.setX_GENERATOR_LGA(rs.getString("LGA"));
				generatorBean.setX_GENERATOR_WARD_ID(rs.getString("WAREHOUSE_TYPE_ID"));
				generatorBean.setX_GENERATOR_WARD(rs.getString("WARD"));
                                generatorBean.setX_GENERATOR_FACILITY_ID(rs.getString("FACILITY_ID"));
				generatorBean.setX_GENERATOR_FACILITY_NAME(rs.getString("FACILITY_NAME"));
                                generatorBean.setX_GENERATOR_LOCATION(rs.getString("LOCATION"));
				generatorBean.setX_GENERATOR_DEFAULT_ORDERING_WAREHOUSE_ID(rs.getString("DEFAULT_ORDERING_WAREHOUSE_ID"));
                                generatorBean.setX_GENERATOR_LOCATION_HAS_ELECTRICITY(rs.getString("FACILITY_HAS_ELECTRICITY"));
				generatorBean.setX_GENERATOR_ELECTRICITY_HRS(rs.getString("ELECTRICITY_HRS"));
                                generatorBean.setX_GENERATOR_MANUFACTURER(rs.getString("MANUFACTURER"));
				generatorBean.setX_GENERATOR_MODEL(rs.getString("MODEL"));
                                generatorBean.setX_GENERATOR_POWER(rs.getString("POWER"));
				generatorBean.setX_GENERATOR_FUNCTIONAL(rs.getString("FUNCTIONAL"));
                                generatorBean.setX_GENERATOR_NF_DATE(rs.getString("DATE_NOT_FUNCTIONAL"));
				generatorBean.setX_GENERATOR_AGE(rs.getString("AGE"));
				generatorBean.setX_GENERATOR_FUEL_TYPE(rs.getString("FUEL_TYPE"));
				generatorBean.setX_GENERATOR_FUEL_AVAILABLE(rs.getString("FUEL_AVAILABLE"));
				generatorBean.setX_GENERATOR_PPM(rs.getString("PPM"));
				generatorBean.setX_GENERATOR_PLANNED_REPAIRS(rs.getString("PLANNED_REPAIRS"));
				generatorBean.setX_GENERATOR_DURATION_NF(rs.getString("DURATION_NF"));
				
				
				generatorData.add(generatorBean);
			}
		} catch (Exception ex) {
			System.out.println("An error occured while Generator list, error: "+ ex.getMessage());
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe(MyLogger.getStackTrace(ex));
		} finally {
			System.out.println("Get Generator list query : " + pstmt.toString());
		}
		return generatorData;
	}

	public boolean saveGenerator(GeneratorBean generatorBean,String actionBtnString) throws SQLException {
		boolean flag = true;
		try {
			if (dao == null || dao.getConnection() == null || dao.getConnection().isClosed()) {
				System.out.println("**In GeneratorService.getGeneratorList() ");
				dao = DatabaseOperation.getDbo();
			}
			if (actionBtnString.equals("add")) {
				pstmt = dao.getPreparedStatement("INSERT INTO E001"
                                                                + " (FACILITY_ID, " //1
                                                                + "FACILITY_HAS_ELECTRICITY, " //2
                                                                + "ELECTRICITY_HRS, " //3
                                                                + "MANUFACTURER, " //4
                                                                + "MODEL, " //5
                                                                + "POWER, " //6
                                                                + "FUNCTIONAL, " //7
                                                                + "DATE_NOT_FUNCTIONAL, " //8
                                                                + "AGE, " //9
                                                                + "FUEL_TYPE, " //10
                                                                + "FUEL_AVAILABLE, " //11
                                                                + "PPM, " //12
                                                                + "PLANNED_REPAIRS, " //13
                                                                + "LOCATION, " //14
                                                                + "DURATION_NF, " //15
                                                                + "CREATED_BY, " //16
                                                                + "CREATED_ON, " 
                                                                + "UPDATED_BY, " //17
                                                                + "LAST_UPDATED_ON) "
								+ " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,TIMESTAMPDIFF(MONTH, ?, NOW()),?,NOW(),?,NOW())");
                                
				pstmt.setString(16, generatorBean.getX_GENERATOR_CREATED_BY());
				pstmt.setString(17, generatorBean.getX_GENERATOR_UPDATED_BY());
			} else {
				pstmt = dao.getPreparedStatement("UPDATE E001 SET "
                                                                + "FACILITY_ID=?, " //1
                                                                + "FACILITY_HAS_ELECTRICITY=?, " //2
                                                                + "ELECTRICITY_HRS=?, " //3
                                                                + "MANUFACTURER=?, " //4
                                                                + "MODEL=?, " //5
                                                                + "POWER=?, " //6
                                                                + "FUNCTIONAL=?, " //7
                                                                + "DATE_NOT_FUNCTIONAL=?, " //8
                                                                + "AGE=?, " //9
                                                                + "FUEL_TYPE=?, " //10
                                                                + "FUEL_AVAILABLE=?, " //11
                                                                + "PPM=?, " //12
                                                                + "PLANNED_REPAIRS=?, " //13
                                                                + "LOCATION=?, " //14
                                                                + "DURATION_NF=TIMESTAMPDIFF(MONTH, ?, NOW()), " //15
                                                                + "UPDATED_BY=?, " //16
                                                                + "LAST_UPDATED_ON=NOW()"
								+ " WHERE FACILITY_ID=?"); //17
                                pstmt.setString(16,generatorBean.getX_GENERATOR_UPDATED_BY());
				pstmt.setString(17, generatorBean.getX_GENERATOR_FACILITY_ID());
			}
                        pstmt.setString(1, generatorBean.getX_GENERATOR_FACILITY_ID());
			pstmt.setString(2, generatorBean.getX_GENERATOR_LOCATION_HAS_ELECTRICITY());
			pstmt.setString(3, generatorBean.getX_GENERATOR_ELECTRICITY_HRS());
			pstmt.setString(4, generatorBean.getX_GENERATOR_MANUFACTURER());
			pstmt.setString(5, generatorBean.getX_GENERATOR_MODEL());
			pstmt.setString(6, generatorBean.getX_GENERATOR_POWER());
			pstmt.setString(7, generatorBean.getX_GENERATOR_FUNCTIONAL());
			pstmt.setString(8, generatorBean.getX_GENERATOR_NF_DATE());
			pstmt.setString(9, generatorBean.getX_GENERATOR_AGE());
			pstmt.setString(10, generatorBean.getX_GENERATOR_FUEL_TYPE());
                        pstmt.setString(11, generatorBean.getX_GENERATOR_FUEL_AVAILABLE());
                        pstmt.setString(12, generatorBean.getX_GENERATOR_PPM());
			pstmt.setString(13,generatorBean.getX_GENERATOR_PLANNED_REPAIRS());
			pstmt.setString(14,generatorBean.getX_GENERATOR_LOCATION());
			pstmt.setString(15,generatorBean.getX_GENERATOR_NF_DATE());
			pstmt.executeUpdate();
		} catch (SQLException | NullPointerException ex) {
			flag = false;
			System.out.println("An error occured while saving/editing Generator, error: "+ ex.getMessage());
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe("An error occured while saving/editing Generator, error: "+MyLogger.getStackTrace(ex));
			return flag;
		} finally {
			System.out.println("warehouse insert/update query : "+ pstmt.toString());
		}
		return flag;
	}

	public ObservableList<GeneratorBean> getSearchList(GeneratorBean toSearchGeneratorBean) {
		ObservableList<GeneratorBean> searchData = FXCollections.observableArrayList();
		try {
			if (dao == null || dao.getConnection() == null || dao.getConnection().isClosed()) {
				System.out.println("**In GeneratorService.getGeneratorList() ");
				dao = DatabaseOperation.getDbo();
			}
		} catch (SQLException e) {
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe(MyLogger.getStackTrace(e));
			e.printStackTrace();
		}
		pstmt = dao.getPreparedStatement("SELECT COMPANY_ID,"
						+ " WAREHOUSE_ID, "
						+ " WAREHOUSE_CODE, "
						+ " WAREHOUSE_NAME, "
						+ " WAREHOUSE_DESCRIPTION, "
						+ " WAREHOUSE_TYPE_NAME,"
						+ " WAREHOUSE_TYPE_ID,"
						+ " ADDRESS1,"
						// + " CITY_NAME, "
						+ " STATE_NAME,"
						+ " COUNTRY_NAME,"
						// + " ZIP_CODE,"
						+ " ADDRESS2,"
						+ " ADDRESS3,"
						+ " COUNTRY_ID,"
						+ " STATE_ID,"
						// + " CITY_ID,"
						+ " TELEPHONE_NUMBER, "
						+ " FAX_NUMBER,"
						+ " STATUS, "
						+ " DATE_FORMAT(START_DATE, '%d-%b-%Y') START_DATE, "
						+ " DATE_FORMAT(END_DATE, '%d-%b-%Y') END_DATE, "
						+ " DEFAULT_ORDERING_WAREHOUSE_ID, "
						+ " DEFAULT_ORDERING_WAREHOUSE_CODE "
						+ " FROM VIEW_INVENTORY_WAREHOUSES "
						+ " WHERE UPPER(WAREHOUSE_CODE) LIKE CONCAT('%',UPPER(IFNULL(?, WAREHOUSE_CODE)),'%') "
						+ "  AND UPPER(WAREHOUSE_NAME) LIKE CONCAT('%',UPPER(IFNULL(?, WAREHOUSE_NAME)),'%') "
						+ "  AND UPPER(WAREHOUSE_DESCRIPTION) LIKE CONCAT('%',UPPER(IFNULL(?, WAREHOUSE_DESCRIPTION)),'%') "
						+ "  AND WAREHOUSE_TYPE_NAME = IFNULL(?, WAREHOUSE_TYPE_NAME) "
						+ "  AND UPPER(IFNULL(ADDRESS1, 'ASDFGHJK1234567')) LIKE CONCAT('%',UPPER(IFNULL(?, IFNULL(ADDRESS1, 'ASDFGHJK1234567'))),'%') "
						+ "  AND STATE_NAME = IFNULL(?, STATE_NAME) "
						+ "  AND COUNTRY_NAME = IFNULL(?, COUNTRY_NAME) "
						+ "  AND IFNULL(TELEPHONE_NUMBER, 'ASDFGHJK1234567') = IFNULL(?, IFNULL(TELEPHONE_NUMBER, 'ASDFGHJK1234567')) "
						+ "  AND STATUS = IFNULL(?, STATUS) "
						+ "  AND IFNULL(DATE_FORMAT(START_DATE, '%Y-%m-%d'), 'AAAAA') = IFNULL(?, IFNULL(DATE_FORMAT(START_DATE, '%Y-%m-%d'), 'AAAAA')) "
						+ "  AND IFNULL(DATE_FORMAT(END_DATE, '%Y-%m-%d'), 'AAAAA') = IFNULL(?, IFNULL(DATE_FORMAT(END_DATE, '%Y-%m-%d'), 'AAAAA')) "
						+ "  AND UPPER(DEFAULT_ORDERING_WAREHOUSE_CODE) LIKE CONCAT('%',UPPER(IFNULL(?, DEFAULT_ORDERING_WAREHOUSE_CODE)),'%') ");
		
		return searchData;
	}

}
