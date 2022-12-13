package ui;

import java.awt.*;
import javax.swing.JTabbedPane;

import burp.ITab;
import burp.IBurpExtenderCallbacks;

public class Tags implements ITab{
    private final JTabbedPane tabs;
    private String tagName;
	private QueueTag queueTag;
    private ConfigTag configTag;

    
    public Tags(IBurpExtenderCallbacks callbacks, String name) {
    	this.tagName = name;
    	this.tabs = new JTabbedPane();
		this.queueTag = new QueueTag(callbacks, tagName, tabs);
    	this.configTag = new ConfigTag(tabs);

    	callbacks.customizeUiComponent(tabs);
    	callbacks.addSuiteTab(Tags.this);
    }
    
    public QueueTag QueueTagFuntion() {
    	return this.queueTag;
    }
    
    public ConfigTag configTagFunction() {
    	return this.configTag;
    }

	@Override
	public String getTabCaption() {
		return this.tagName;
	}

	@Override
	public Component getUiComponent() {
		return this.tabs;
	}
}
