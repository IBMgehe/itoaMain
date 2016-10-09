package com.ibm.automation.healcheck;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.ibm.automation.ams.service.AmsRestService;

@RestController
public class HostsController {
	public static Logger logger = Logger.getLogger(HostsController.class);
	@Autowired
	private AmsRestService amsRestService;
	@RequestMapping(value="getIPByGroup.do",method = RequestMethod.GET)
	public ArrayNode getIPByGroup(HttpServletRequest request, HttpSession session) {
		String groupName = request.getParameter("groupName");
		ObjectNode retNodes = amsRestService.getList_one(null, null,
				"/api/v1/hosts?action=query&obj=ip&ip=all"+"&group="+groupName);
		logger.info("getIPByGroup::获取到的参数为:"+retNodes.toString());
		return (ArrayNode)retNodes.get("msg");
	}
}
