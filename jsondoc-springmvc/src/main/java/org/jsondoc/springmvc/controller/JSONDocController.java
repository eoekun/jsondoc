package org.jsondoc.springmvc.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jsondoc.core.pojo.ApiObjectDoc;
import org.jsondoc.core.pojo.JSONDoc;
import org.jsondoc.core.pojo.JSONDoc.MethodDisplay;
import org.jsondoc.core.scanner.JSONDocScanner;
import org.jsondoc.springmvc.scanner.Spring3JSONDocScanner;
import org.jsondoc.springmvc.scanner.Spring4JSONDocScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.SpringVersion;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Sets;

@Controller
public class JSONDocController {
	private String version;
	/**
	 * API请求的基础路径
	 */
	private String basePath;
	/**
	 * 需要扫描的包(支持多个,逗号分割),一般设置为模块名
	 */
	private String packageNames;
	/**
	 * 扫描包的基础包路径,一般设置为项目域名
	 */
	private String packagePathPre;
	/**
	 * api是否可用,一般生产环境设置为false
	 */
	private boolean apiEnabled = true;
	private boolean playgroundEnabled = true;
	private MethodDisplay displayMethodAs = MethodDisplay.URI;
	private JSONDocScanner jsondocScanner;

	public final static String JSONDOC_DEFAULT_PATH = "/jsondoc";
	public final static String PACKAGE_PATH_SPILT = ".";
	public final static String PACKAGE_NAME_SPILT = ",";
	private final static Integer SPRING_VERSION_3_X = 3;
	private final static Logger LOGGER = LoggerFactory.getLogger(JSONDocController.class);

	public JSONDocController(String version, String basePath, String packagePathPre, String packageNames) {
		this.version = version;
		this.basePath = basePath;
		this.packagePathPre = packagePathPre;
		this.packageNames = packageNames;
		String springVersion = SpringVersion.getVersion();
		if (springVersion != null && !springVersion.isEmpty()) {
			Integer majorSpringVersion = Integer.parseInt(springVersion.split("\\.")[0]);
			if (majorSpringVersion > SPRING_VERSION_3_X) {
				this.jsondocScanner = new Spring4JSONDocScanner();
			} else {
				this.jsondocScanner = new Spring3JSONDocScanner();
			}
		} else {
			try {
				Class.forName("org.springframework.web.bind.annotation.RestController");
				this.jsondocScanner = new Spring4JSONDocScanner();

			} catch (ClassNotFoundException e) {
				this.jsondocScanner = new Spring3JSONDocScanner();
			}
		}
	}

	public boolean isPlaygroundEnabled() {
		return playgroundEnabled;
	}

	public void setPlaygroundEnabled(boolean playgroundEnabled) {
		this.playgroundEnabled = playgroundEnabled;
	}

	public boolean isApiEnabled() {
		return apiEnabled;
	}

	public void setApiEnabled(boolean apiEnabled) {
		this.apiEnabled = apiEnabled;
	}

	public MethodDisplay getDisplayMethodAs() {
		return displayMethodAs;
	}

	public void setDisplayMethodAs(MethodDisplay displayMethodAs) {
		this.displayMethodAs = displayMethodAs;
	}

	@RequestMapping(value = JSONDocController.JSONDOC_DEFAULT_PATH, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody JSONDoc getApi() {
		JSONDoc result = null;
		if (!apiEnabled) {
			result = new JSONDoc(version, basePath);
			result.setErrorMsg("Jsondoc plugin is not allow at this environment");
			return result;
		}
		try {
			List<String> packages = getPackages();
			result = jsondocScanner.getJSONDoc(version, basePath, packages, playgroundEnabled, displayMethodAs);
			result.setUniqueObjects(getUniqueObjects(result.getObjects()));
			return result;
		} catch (Exception e) {
			LOGGER.error("get jsondoc api json exception,", e);
			result = new JSONDoc(version, basePath);
			result.setErrorMsg(e.getMessage());
			return result;
		}
	}

	private Map<String, ApiObjectDoc> getUniqueObjects(Map<String, Set<ApiObjectDoc>> objects) {
		Map<String, ApiObjectDoc> map = new HashMap<>();
		if (null != objects && !objects.isEmpty()) {
			Collection<Set<ApiObjectDoc>> objectSet = objects.values();
			for (Set<ApiObjectDoc> set : objectSet) {
				if (null != set && !set.isEmpty()) {
					for (ApiObjectDoc apiObjectDoc : set) {
						String key = apiObjectDoc.getName();
						if (map.containsKey(key)) {
							LOGGER.warn("object name 存在重复,name:{}", key);
						}
						map.put(key, apiObjectDoc);
					}
				}
			}
		}
		return map;
	}

	/**
	 * 根据
	 * {@link JSONDocController#packageNames}与{@link JSONDocController#packagePathPre}获取所有需要扫描的包
	 * 
	 * @return List<String>
	 * @author chenkun
	 * @createTime 2017年10月20日 上午10:54:23
	 */
	private List<String> getPackages() {
		if (StringUtils.isEmpty(packageNames)) {
			LOGGER.error("jsondoc 未配置基础扫描包:{}", packageNames);
			throw new NullPointerException("jsondoc property of packageNames");
		}
		if (null != packagePathPre) {
			packagePathPre = packagePathPre.replaceAll(" ", "");
			if (!"".equals(packagePathPre) && !packagePathPre.endsWith(PACKAGE_PATH_SPILT)) {
				packagePathPre += PACKAGE_PATH_SPILT;
			}
		}
		List<String> packages = new ArrayList<>();
		if (packageNames.contains(PACKAGE_NAME_SPILT)) {
			String[] packageNameArray = packageNames.split(PACKAGE_NAME_SPILT);
			Set<String> packageNameSet = Sets.newHashSet(packageNameArray);
			for (Iterator<String> iterator = packageNameSet.iterator(); iterator.hasNext();) {
				String packageName = iterator.next();
				packages.add(packagePathPre + packageName);
			}
		}
		return packages;
	}

}
