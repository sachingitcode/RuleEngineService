/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gl.rule_engine.rules;

import com.gl.rule_engine.ExecutionInterface;
import com.gl.rule_engine.RuleInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.ResultSet;


public class IMEI_NULL implements ExecutionInterface {

    static final Logger logger = LogManager.getLogger(IMEI_NULL.class);

    @Override
    public String executeRule(RuleInfo ruleEngine) {
        String res = "";
        String pattern = "999999999999999";
        logger.info("CONNNN " + ruleEngine.connection + "ACTUAL " + ruleEngine.actualImei);
        try (var stt = ruleEngine.connection.createStatement(); ResultSet rs = stt.executeQuery("select value from sys_param where tag ='CDR_NULL_IMEI_REPLACE_PATTERN'")) {
            while (rs.next()) {
                pattern = rs.getString("value");
            }
        } catch (Exception e) {
            logger.error(e + e.getLocalizedMessage());
        }
        if ((ruleEngine.actualImei == null) || ruleEngine.actualImei.equals("") || ruleEngine.actualImei.equalsIgnoreCase(pattern)) {
            res = "Yes";
        } else {
            res = "No";
        }
        logger.debug("NULL response : if Yes then Fails ::  " + res);

        return res;
    }

    @Override
    public String executeAction(RuleInfo ruleEngine) {
        String res = "Success";
        try {
            String fileString = ruleEngine.fileArray + ", Error Code :CON_RULE_0014,Error Description:IMEI/ESN/MEID is missing in the record. ";
            ruleEngine.bw.write(fileString);
            ruleEngine.bw.newLine();
        } catch (Exception e) {
            logger.debug("Error e ");
            res = "Error";
        }
        return res;
    }

}
