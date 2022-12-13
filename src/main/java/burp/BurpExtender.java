package burp;

import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ui.Tags;

public class BurpExtender implements IBurpExtender,IScannerCheck {
	private final String NAME = "SSRFScan";
	private final String VERSION = "1.0";
	public static IBurpExtenderCallbacks callback;
	private IExtensionHelpers helpers;
	private PrintWriter stdout;
	private PrintWriter stderr;
	private Tags tags;

	@Override
	public void registerExtenderCallbacks(IBurpExtenderCallbacks callbacks) {
		//获取IBurpExtenderCallbacks对象
		this.callback = callbacks; 
		//获取IExtensionHelpers对象
		this.helpers = callbacks.getHelpers(); 
		
		this.stdout = new PrintWriter(callbacks.getStdout(),true);
		this.stderr = new PrintWriter(callbacks.getStdout(),true);
		//输出插件信息
		this.stdout.println(extenderInfo()); 
		//设置插件名字
		callback.setExtensionName(NAME); 
		//注册扫描
		callback.registerScannerCheck(this);
		
		this.tags = new Tags(callbacks, NAME);
	}

	public String extenderInfo() {
		String  infoString = "[+] NAME: "+NAME+"\n";
		infoString += "[+] VERSION: "+VERSION+"\n";
		infoString += "[+] AUTHOR: root0er"+"\n";
		return infoString;
	}
	
	public List<IScanIssue> checkVul(IHttpRequestResponse baseRequestResponse,List<IScanIssue> issues) {
		IRequestInfo analyzeRequest = helpers.analyzeRequest(baseRequestResponse);//对HTTP请求进行分析
		String method = analyzeRequest.getMethod().toString(); //获取请求方法
		byte[] request = baseRequestResponse.getRequest(); //获取请求包
		IHttpService Service = baseRequestResponse.getHttpService(); 
		String path = analyzeRequest.getUrl().getPath(); //获取漏洞的路径
		String host = analyzeRequest.getUrl().getHost(); //获取主机头
		URL url = analyzeRequest.getUrl();
		List<IParameter> parameters = analyzeRequest.getParameters();
		
		for(IParameter parameter:parameters) {
			String genatePayload = random_payload();
			String Identifier_url = this.tags.configTagFunction().jTextFieldIdentifier();
			String payload = String.format("http://%s.%s", genatePayload, Identifier_url);
			String key = parameter.getName(); //获取参数名
			String value = parameter.getValue(); //获取参数值
			byte type = parameter.getType(); //获取参数类型
			if(type == 0 || type == 1) {
				IParameter newParameter = helpers.buildParameter(key, payload, type);
				byte[] newRequest = helpers.updateParameter(request, newParameter);
				IHttpRequestResponse makeHttpRequest = callback.makeHttpRequest(Service, newRequest);
				byte[] response = makeHttpRequest.getResponse();
				IResponseInfo analyzedResponse = helpers.analyzeResponse(response);
				String resp = new String(response);
				int bodyOffset = analyzedResponse.getBodyOffset();
				String body = resp.substring(bodyOffset);
				int statusCode = analyzedResponse.getStatusCode();
				String vulString = "Not found SSRF";
				boolean flag1 = dnslogResult(genatePayload,Identifier_url);
				if(flag1) {
					String title = "SSRF VUL";
					vulString = "Found SSRF VUL";
					String message="<br>Method: <b>"  + method + "\n</b><br>Host: <b>" + host + "\n</b><br>Endpoint: <b>" + path + "\n</b><br>Location: <b>" + key + "</b>\n";
					issues.add(new CustomScanIssue(Service, url, new IHttpRequestResponse[]{makeHttpRequest}, title, message, "High", "Certain", "Panic"));
				}
				this.tags.QueueTagFuntion().add(method,url.getHost().toString(),statusCode,key,vulString,makeHttpRequest);				
			}
		}
		return issues;
	}
	
	public String random_payload() {
		String baseString = "abcdefghijklmnopqrstuvwxyz1234567890";
		Random random = new Random();
		String flag = "ssrf-";
		for(int i=0;i<10;i++) {
			flag = flag + baseString.charAt(random.nextInt(baseString.length()));
		}
		return flag;
	}
	
	public boolean dnslogResult(String payload,String suburl) {
		String token = this.tags.configTagFunction().jTextFieldToken();
		String subkey = suburl.replace(".eyes.sh","");
		String platformUrl = String.format("http://eyes.sh/api/web/%s/%s/?token=%s", subkey, payload, token);
		try {
			byte[] rawRequest = this.helpers.buildHttpRequest(new URL(platformUrl));
			IHttpService service = this.helpers.buildHttpService("eyes.sh", 80, "HTTP");
			IHttpRequestResponse requestResponse = this.callback.makeHttpRequest(service, rawRequest);
			byte[] rawResponse = requestResponse.getResponse();
			IResponseInfo responseInfo = this.helpers.analyzeResponse(rawResponse);
			String body = new String(rawResponse).substring(responseInfo.getBodyOffset()).trim().toLowerCase();
			if(body.contains("true")) {
				return true;
			}
		}catch (Exception e) {
			stderr.println(e.getMessage());
		}
		return false;
	}

	@Override
	public List<IScanIssue> doPassiveScan(IHttpRequestResponse baseRequestResponse) {
		if(!(tags.configTagFunction().isEnabled())){
			return null;
		}
		List<IScanIssue> issues = new ArrayList<IScanIssue>();
		this.checkVul(baseRequestResponse,issues);
		if(!(issues.isEmpty())) {
			return issues;
		}else {
			return null;
		}
	}

	@Override
	public List<IScanIssue> doActiveScan(IHttpRequestResponse baseRequestResponse,
			IScannerInsertionPoint insertionPoint) {
		return null;
	}

	@Override
	public int consolidateDuplicateIssues(IScanIssue existingIssue, IScanIssue newIssue) {
		return 0;
	}
	
	class CustomScanIssue implements IScanIssue{
	    private IHttpService httpService;
	    private URL url;
	    private IHttpRequestResponse[] httpMessages;
	    private String name;
	    private String detail;
	    private String severity;
	    private String confidence;
	    private String remediation;
	    
	    CustomScanIssue(
	            IHttpService httpService,
	            URL url,
	            IHttpRequestResponse[] httpMessages,
	            String name,
	            String detail,
	            String severity,
	            String confidence,
	            String remediation) {
	        this.name = name;
	        this.detail = detail;
	        this.severity = severity;
	        this.httpService = httpService;
	        this.url = url;
	        this.httpMessages = httpMessages;
	        this.confidence = confidence;
	        this.remediation = remediation;
	    }

		@Override
		public URL getUrl() {
			return url;
		}

		@Override
		public String getIssueName() {
			return name;
		}

		@Override
		public int getIssueType() {
			return 0;
		}

		@Override
		public String getSeverity() {
			return severity;
		}

		@Override
		public String getConfidence() {
			return confidence;
		}

		@Override
		public String getIssueBackground() {
			return null;
		}

		@Override
		public String getRemediationBackground() {
			return null;
		}

		@Override
		public String getIssueDetail() {
			return detail;
		}

		@Override
		public String getRemediationDetail() {
			return null;
		}

		@Override
		public IHttpRequestResponse[] getHttpMessages() {
			return httpMessages;
		}

		@Override
		public IHttpService getHttpService() {
			return httpService;
		}
		
	}
}
