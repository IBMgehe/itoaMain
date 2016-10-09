package com.ibm.automation.product.controller;

import java.util.Arrays;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.ibm.automation.core.util.PropertyUtil;

@RestController
public class ProductController {
	public static Logger logger = Logger.getLogger(ProductController.class);
	Properties amsprop = PropertyUtil.getResourceFile("config/properties/ams2.properties");
	/**
	 * @param pro
	 * @author hujin
	 * @see 判断用户购买了哪些产品服务
	 */
	/*@RequestMapping("/judgeProduct.do")
	private String judgeProductIsExist(String pro) {
		String allProduct = amsprop.getProperty("product");
		if (allProduct.contains(pro.toLowerCase())) {
			String ifExists = "com.ibm.automation." + pro + ".controller." + pro.toUpperCase() + "Controller";
			// System.out.println(ifExists);
			try {
				Class.forName(ifExists);
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				// e.printStackTrace();
				logger.debug("该产品不存在，请联系IBM购买");
				return "ERROR";
			}

		}
		return "success";
	}*/
	/**
	 * 为了健康检查用的下拉框判断
	 */
	@RequestMapping("/getProduct.do")
	public ArrayNode getProductName()
	{
		String allProduct = amsprop.getProperty("product");
		ObjectMapper om = new ObjectMapper();
		ArrayNode an = om.createArrayNode();
		for (String s : allProduct.split(","))
		{
			an.add(s);
		}
		
		return an;
	}
	public static void main(String[] args) {
		ProductController pc = new ProductController();
		System.out.println(pc .getProductName());
	}
}
