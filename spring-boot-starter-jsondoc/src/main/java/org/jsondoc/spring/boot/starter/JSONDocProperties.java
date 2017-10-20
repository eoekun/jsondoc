package org.jsondoc.spring.boot.starter;

import org.jsondoc.core.pojo.JSONDoc.MethodDisplay;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = JSONDocConfig.JSONDOC_PROPERTIES_PREFIX)
public class JSONDocProperties {

	/**
	 * The version of your API.
	 */
	private String version;

	/**
	 * The base path of your API, for example http://localhost:8080.
	 */
	private String basePath;

	/**
	 * The list of packages that JSONDoc will scan to look for annotated classes
	 * to be documented.
	 */
	// private List<String> packages = new ArrayList<String>();
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
	/**
	 * Whether the playground should be enabled in the UI or not. Defaults to
	 * true.
	 */
	private boolean playgroundEnabled = true;

	/**
	 * Whether to display methods as URIs or with a short description (summary
	 * attribute in the @ApiMethod annotation). Allowed values are URI and
	 * SUMMARY.
	 */
	private MethodDisplay displayMethodAs = MethodDisplay.URI;

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getBasePath() {
		return basePath;
	}

	public void setBasePath(String basePath) {
		this.basePath = basePath;
	}

	// public List<String> getPackages() {
	// return packages;
	// }
	//
	// public void setPackages(List<String> packages) {
	// this.packages = packages;
	// }

	public boolean isPlaygroundEnabled() {
		return playgroundEnabled;
	}

	public String getPackageNames() {
		return packageNames;
	}

	public void setPackageNames(String packageNames) {
		this.packageNames = packageNames;
	}

	public String getPackagePathPre() {
		return packagePathPre;
	}

	public void setPackagePathPre(String packagePathPre) {
		this.packagePathPre = packagePathPre;
	}

	public boolean isApiEnabled() {
		return apiEnabled;
	}

	public void setApiEnabled(boolean apiEnabled) {
		this.apiEnabled = apiEnabled;
	}

	public void setPlaygroundEnabled(boolean playgroundEnabled) {
		this.playgroundEnabled = playgroundEnabled;
	}

	public MethodDisplay getDisplayMethodAs() {
		return displayMethodAs;
	}

	public void setDisplayMethodAs(MethodDisplay displayMethodAs) {
		this.displayMethodAs = displayMethodAs;
	}

}
