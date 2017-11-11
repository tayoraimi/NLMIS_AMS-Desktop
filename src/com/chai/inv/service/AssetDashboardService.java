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
import com.chai.inv.model.CCECapacityBean;
import com.chai.inv.model.CCECapRiBean;
import com.chai.inv.model.CCEStatusBean;
import com.chai.inv.model.CustProdMonthlyDetailBean;
import com.chai.inv.model.LgaDashBoardPerfBean;

public class AssetDashboardService {
	DatabaseOperation dao;
	PreparedStatement pstmt;
	ResultSet rs;
	/**
	 * this method is used for cce functionality dashboard data
	 * */
	public ObservableList<CCEStatusBean> getCCEStatusList(String action){

		String x_WHERE_CONDITION = " WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "+MainApp.getUSER_WAREHOUSE_ID()
                        + " OR FACILITY_ID = "+MainApp.getUSER_WAREHOUSE_ID()
                        +" OR DEFAULT_ORDERING_WAREHOUSE_ID IN ( SELECT FACILITY_ID FROM view_cce_status_dashboard WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "+MainApp.getUSER_WAREHOUSE_ID()+")";
						
		ObservableList<CCEStatusBean > cceStatusDashboardlist=FXCollections.observableArrayList();
		String x_QUERY=null;
                if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA == null) {
                            x_QUERY = "SELECT STATE_ID, STATE, LGA_ID, LGA, WARD, WAREHOUSE_TYPE_ID, LOCATION,"
                                        + " SUM(F) AS tF, SUM(NF) AS tNF, SUM(NI) AS tNI, SUM(O_F) AS tO_F, SUM(O_NF) AS tO_NF, SUM(F)+SUM(NF)+SUM(NI) AS TOTAL"
                                        + " FROM view_cce_status_dashboard "+x_WHERE_CONDITION+" GROUP BY "+(action.equals("")?"State":action);
			} else if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA != null) {
                            x_QUERY = "SELECT STATE_ID, STATE, LGA_ID, LGA, WARD, WAREHOUSE_TYPE_ID, LOCATION,"
                                        + " SUM(F) AS tF, SUM(NF) AS tNF, SUM(NI) AS tNI, SUM(O_F) AS tO_F, SUM(O_NF) AS tO_NF, SUM(F)+SUM(NF)+SUM(NI) AS TOTAL"
                                        + " FROM view_cce_status_dashboard "+x_WHERE_CONDITION+" GROUP BY "+(action.equals("")?"Ward":action);
			} else if (MainApp.getUserRole().getLabel().equals("SCCO")
					&& CustomChoiceDialog.selectedLGA == null) {
                            x_QUERY = "SELECT STATE_ID, STATE, LGA_ID, LGA, WARD, WAREHOUSE_TYPE_ID, LOCATION,"
                                        + " SUM(F) AS tF, SUM(NF) AS tNF, SUM(NI) AS tNI, SUM(O_F) AS tO_F, SUM(O_NF) AS tO_NF, SUM(F)+SUM(NF)+SUM(NI) AS TOTAL"
                                        + " FROM view_cce_status_dashboard "+x_WHERE_CONDITION+" GROUP BY "+(action.equals("")?"LGA":action);
			} else if ((MainApp.getUserRole().getLabel().equals("SCCO") && CustomChoiceDialog.selectedLGA != null)
					|| MainApp.getUserRole().getLabel().equals("CCO")) {
                            x_QUERY = "SELECT STATE_ID, STATE, LGA_ID, LGA, WARD, WAREHOUSE_TYPE_ID, LOCATION,"
                                        + " SUM(F) AS tF, SUM(NF) AS tNF, SUM(NI) AS tNI, SUM(O_F) AS tO_F, SUM(O_NF) AS tO_NF, SUM(F)+SUM(NF)+SUM(NI) AS TOTAL"
                                        + " FROM view_cce_status_dashboard "+x_WHERE_CONDITION+" GROUP BY "+(action.equals("")?"Ward":action);
			} else if (MainApp.getUserRole().getLabel().equals("LIO")) {
                            x_QUERY = "SELECT STATE_ID, STATE, LGA_ID, LGA, WARD, WAREHOUSE_TYPE_ID, LOCATION,"
                                        + " SUM(F) AS tF, SUM(NF) AS tNF, SUM(NI) AS tNI, SUM(O_F) AS tO_F, SUM(O_NF) AS tO_NF, SUM(F)+SUM(NF)+SUM(NI) AS TOTAL"
                                        + " FROM view_cce_status_dashboard "+x_WHERE_CONDITION+" GROUP BY "+(action.equals("")?"Ward":action);
                            
			}
	
		
		try {
			if (dao == null || dao.getConnection() == null || dao.getConnection().isClosed()) {
				dao = DatabaseOperation.getDbo();
			}
			pstmt = dao.getPreparedStatement(x_QUERY);
			rs = pstmt.executeQuery();
			while(rs.next()){
				CCEStatusBean bean=new CCEStatusBean();
                                bean.setX_CCE_STATUS_NF(rs.getString("tNF"));
                                bean.setX_CCE_STATUS_OF(rs.getString("tO_F"));
                                bean.setX_CCE_STATUS_ONF(rs.getString("tO_NF"));
//                                bean.setX_CCE_STATUS_FACILITY_NAME(rs.getString("FACILITY_NAME"));
                                bean.setX_CCE_STATUS_STATE(rs.getString("STATE"));
                                bean.setX_CCE_STATUS_STATE_ID(rs.getString("STATE_ID"));
                                bean.setX_CCE_STATUS_WARD(rs.getString("WARD"));
                                bean.setX_CCE_STATUS_LOCATION(rs.getString("LOCATION"));
                                bean.setX_CCE_STATUS_LOCATION_ID(rs.getString("WAREHOUSE_TYPE_ID"));
                                bean.setX_CCE_STATUS_TOTAL(rs.getString("TOTAL"));
                                bean.setX_CCE_STATUS_F(rs.getString("tF")+"/"+
                                        ((Integer.parseInt(rs.getString("tF"))* 100.0f) / 
                                                ((Integer.parseInt(rs.getString("TOTAL"))==0)?1:Integer.parseInt(rs.getString("TOTAL")))));
                                bean.setX_CCE_STATUS_LGA(rs.getString("LGA"));
                                bean.setX_CCE_STATUS_LGA_ID(rs.getString("LGA_ID"));
                                bean.setX_CCE_STATUS_NI(rs.getString("tNI"));
				cceStatusDashboardlist.add(bean);
			}
		} catch (SQLException e) {
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe(MyLogger.getStackTrace(e));
			e.printStackTrace();
		}finally{
			System.out.println("dashboard check Query: \n "+ pstmt.toString());
		}
		return cceStatusDashboardlist;
	}
	/**
	 * this method is used for cce antigen dashboard data
	 * */
	public ObservableList<CCECapRiBean> getCCECapRiList(String... action){

		String x_WHERE_CONDITION = " WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "+MainApp.getUSER_WAREHOUSE_ID()
                        + " OR FACILITY_ID = "+MainApp.getUSER_WAREHOUSE_ID()
                        +" OR DEFAULT_ORDERING_WAREHOUSE_ID IN ( SELECT FACILITY_ID FROM view_cce_status_dashboard WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "+MainApp.getUSER_WAREHOUSE_ID()+")";
                String x_VIEW_NAME="Cap_RI";
                switch (action[0]) {
		case "Cap_RI": // Default Selection
                    x_VIEW_NAME = "view_cap_ri_sub";
                        break;
		case "Men_A": // 
                    x_VIEW_NAME = "view_cap_men_a_sub";
                        break;
		case "Cap_Rota": 
                    x_VIEW_NAME = "view_cap_rota_sub";
                        break;
		case "Cap_MR": 
                    x_VIEW_NAME = "view_cap_mr_sub";
                        break;
		case "Cap_HPV": 
                    x_VIEW_NAME = "view_cap_hpv_sub";
                        break;
		}
						
		ObservableList<CCECapRiBean > cceCapRiDashboardlist=FXCollections.observableArrayList();
		String x_QUERY=null;
                if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA == null) {
                            x_QUERY = "SELECT * FROM "+x_VIEW_NAME+" "+x_WHERE_CONDITION;//+" GROUP BY "+(action[1]==null?"State":action[1]);
			} else if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA != null) {
                            x_QUERY = "SELECT * FROM "+x_VIEW_NAME+" "+x_WHERE_CONDITION;//+" GROUP BY "+(action[1]==null?"Ward":action[1]);
			} else if (MainApp.getUserRole().getLabel().equals("SCCO")
					&& CustomChoiceDialog.selectedLGA == null) {
                            x_QUERY = "SELECT * FROM "+x_VIEW_NAME+" "+x_WHERE_CONDITION;//+" GROUP BY "+(action[1]==null?"LGA":action[1]);
			} else if ((MainApp.getUserRole().getLabel().equals("SCCO") && CustomChoiceDialog.selectedLGA != null)
					|| MainApp.getUserRole().getLabel().equals("CCO")) {
                            x_QUERY = "SELECT * FROM "+x_VIEW_NAME+" "+x_WHERE_CONDITION;//+" GROUP BY "+(action[1]==null?"Ward":action[1]);
			} else if (MainApp.getUserRole().getLabel().equals("LIO")) {
                            x_QUERY = "SELECT * FROM "+x_VIEW_NAME+" "+x_WHERE_CONDITION;//+" GROUP BY "+(action[1]==null?"Ward":action[1]);
                            
			}
	
		
		try {
			if (dao == null || dao.getConnection() == null || dao.getConnection().isClosed()) {
				dao = DatabaseOperation.getDbo();
			}
			pstmt = dao.getPreparedStatement(x_QUERY);
			rs = pstmt.executeQuery();
			while(rs.next()){
				CCECapRiBean bean=new CCECapRiBean();
                                bean.setX_CCE_CAP_RI_SUPPLY_CHAIN_LEVEL(rs.getString("SUPPLY_CHAIN_LEVEL"));
                                bean.setX_CCE_CAP_RI_STATE(rs.getString("STATE"));
                                bean.setX_CCE_CAP_RI_STATE_ID(rs.getString("STATE_ID"));
                                bean.setX_CCE_CAP_RI_LGA(rs.getString("LGA"));
                                bean.setX_CCE_CAP_RI_LGA_ID(rs.getString("LGA_ID"));
                                bean.setX_CCE_CAP_RI_WARD(rs.getString("WARD"));
                                bean.setX_CCE_CAP_RI_FACILITY_NAME(rs.getString("FACILITY_NAME"));
                                bean.setX_CCE_CAP_RI_ANNUAL_BIRTH(rs.getString("ANNUAL_BIRTH"));
                                bean.setX_CCE_CAP_RI_SAFETY_STOCK(rs.getString("SAFETY_STOCK"));
                                bean.setX_CCE_CAP_RI_SUPPLY_INTERVAL_FOR_SAFE_INJECTION_EQUIPT(rs.getString("SUPPLY_INTERVAL_FOR_SAFE_INJECTION_EQUIPT"));
                                bean.setX_CCE_CAP_RI_CONTINGENCY_FACTOR(Double.toString(Double.parseDouble(rs.getString("CONTINGENCY_FACTOR"))*100)+"%");
                                bean.setX_CCE_CAP_RI_MAXIMUM_STOCK(rs.getString("MAXIMUM_STOCK"));
                                bean.setX_CCE_CAP_RI_SCHEDULE_VACCINES(rs.getString("SCHEDULE_VACCINES"));
                                bean.setX_CCE_CAP_RI_OTHERS_INTERVENTIONS(rs.getString("OTHERS_INTERVENTIONS"));
                                bean.setX_CCE_CAP_RI_TOTAL_VOLUME(rs.getString("TOTAL_VOLUME"));
                                bean.setX_CCE_CAP_RI_IF_MORE_THAN_3_MONTHS_STORAGE(rs.getString("IF_MORE_THAN_3_MONTHS_STORAGE"));
                                bean.setX_CCE_CAP_RI_AT_2C_TO_8C(rs.getString("AT_2C_TO_8C"));
                                bean.setX_CCE_CAP_RI_AT_25C_TO_15C(rs.getString("AT_25C_TO_15C"));
                                bean.setX_CCE_CAP_RI_AT_2C_TO_8C2(rs.getString("AT_2C_TO_8C2"));
                                bean.setX_CCE_CAP_RI_AT_25C_TO_15C3(rs.getString("AT_25C_TO_15C3"));
                                bean.setX_CCE_CAP_RI_AT_2C_TO_8C4(rs.getString("AT_2C_TO_8C4"));
                                bean.setX_CCE_CAP_RI_AT_25C_TO_15C5(rs.getString("AT_25C_TO_15C5"));
                                
				cceCapRiDashboardlist.add(bean);
			}
		} catch (SQLException e) {
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe(MyLogger.getStackTrace(e));
			e.printStackTrace();
		}finally{
			System.out.println("dashboard check Query: \n "+ pstmt.toString());
		}
		return cceCapRiDashboardlist;
	}
	/**
	 * this method is used for cce capacity dashboard data
	 * */
	public ObservableList<CCECapacityBean> getCCECapacityList(String action){

		String x_WHERE_CONDITION = " WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "+MainApp.getUSER_WAREHOUSE_ID()
                        + " OR FACILITY_ID = "+MainApp.getUSER_WAREHOUSE_ID()
                        +" OR DEFAULT_ORDERING_WAREHOUSE_ID IN ( SELECT FACILITY_ID FROM view_cce_capacity_dashboard WHERE DEFAULT_ORDERING_WAREHOUSE_ID = "+MainApp.getUSER_WAREHOUSE_ID()+")";
                			
		ObservableList<CCECapacityBean > cceCapacityDashboardlist=FXCollections.observableArrayList();
		String x_QUERY= "SELECT STATE_ID, STATE, LGA_ID, LGA, WARD,"
                        + " FACILITY_ID, WAREHOUSE_TYPE_ID, SUPPLY_CHAIN_LEVEL,"
                        + " DEFAULT_ORDERING_WAREHOUSE_ID,"
                        + " CONCAT(SUM(RI_A),'/',SUM(RI_R)) AS RI,"
                        + " CONCAT(SUM(MEN_A_A),'/',SUM(MEN_A_R)) AS MEN_A,"
                        + " CONCAT(SUM(ROTA_A),'/',SUM(ROTA_R)) AS ROTA,"
                        + " CONCAT(SUM(MR_A),'/',SUM(MR_R)) AS MR,"
                        + " CONCAT(SUM(HPV_A),'/',SUM(HPV_R)) AS HPV"
                        + " FROM view_cce_capacity_dashboard";
                if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA == null) {
                            x_QUERY = x_QUERY+x_WHERE_CONDITION+" GROUP BY "+(action.equals("")?"State":action);
			} else if (MainApp.getUserRole().getLabel().equals("NTO")
					&& CustomChoiceDialog.selectedLGA != null) {
                            x_QUERY = x_QUERY+x_WHERE_CONDITION+" GROUP BY "+(action.equals("")?"Ward":action);
			} else if (MainApp.getUserRole().getLabel().equals("SCCO")
					&& CustomChoiceDialog.selectedLGA == null) {
                            x_QUERY = x_QUERY+x_WHERE_CONDITION+" GROUP BY "+(action.equals("")?"LGA":action);
			} else if ((MainApp.getUserRole().getLabel().equals("SCCO") && CustomChoiceDialog.selectedLGA != null)
					|| MainApp.getUserRole().getLabel().equals("CCO")) {
                            x_QUERY = x_QUERY+x_WHERE_CONDITION+" GROUP BY "+(action.equals("")?"Ward":action);
			} else if (MainApp.getUserRole().getLabel().equals("LIO")) {
                            x_QUERY = x_QUERY+x_WHERE_CONDITION+" GROUP BY "+(action.equals("")?"Ward":action);
                            
			}
	
		
		try {
			if (dao == null || dao.getConnection() == null || dao.getConnection().isClosed()) {
                            System.out.println("Reached here for CCECAP DASH "+DatabaseOperation.CONNECT_TO_SERVER);
				dao = DatabaseOperation.getDbo();
                            System.out.println("Reached here for CCECAP DASH 22 "+DatabaseOperation.CONNECT_TO_SERVER);
			}
			pstmt = dao.getPreparedStatement(x_QUERY);
			rs = pstmt.executeQuery();
			while(rs.next()){
				CCECapacityBean bean=new CCECapacityBean();
                                bean.setX_CCE_CAPACITY_LOCATION(rs.getString("SUPPLY_CHAIN_LEVEL"));
                                bean.setX_CCE_CAPACITY_STATE(rs.getString("STATE"));
                                bean.setX_CCE_CAPACITY_LGA(rs.getString("LGA"));
                                bean.setX_CCE_CAPACITY_WARD(rs.getString("WARD"));
                                bean.setX_CCE_CAPACITY_RI(rs.getString("RI"));
                                bean.setX_CCE_CAPACITY_HPV(rs.getString("HPV"));
                                bean.setX_CCE_CAPACITY_MEN_A(rs.getString("MEN_A"));
                                bean.setX_CCE_CAPACITY_MR(rs.getString("MR"));
                                bean.setX_CCE_CAPACITY_ROTA(rs.getString("ROTA"));
                                
				cceCapacityDashboardlist.add(bean);
			}
		} catch (SQLException e) {
			MainApp.LOGGER.setLevel(Level.SEVERE);
			MainApp.LOGGER.severe(MyLogger.getStackTrace(e));
			e.printStackTrace();
		}finally{
			System.out.println("dashboard check Query: \n "+ pstmt.toString());
		}
		return cceCapacityDashboardlist;
	}


}
