package ui;

import config.BurpConfig;

import java.awt.*;
import java.awt.event.ActionEvent;
import javax.swing.*;

public class ConfigTag {
	private JCheckBox jCheckBox;
	private JTextField jTextField_Identifier;
	private JTextField jTextField_token;
	private JButton applyTextbtButton;
	
    public ConfigTag(JTabbedPane tabs) {
        JPanel baseSetting = new JPanel(new GridBagLayout());
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        this.config_jcheckbox(baseSetting,gridBagConstraints);
        this.config_jLabel1(baseSetting, gridBagConstraints);
        this.config_jTextField1(baseSetting, gridBagConstraints);
        this.config_jLabel2(baseSetting, gridBagConstraints);       
        this.config_jTextField2(baseSetting, gridBagConstraints);
        this.config_Button(baseSetting, gridBagConstraints);
		if(BurpConfig.getBoolean("dnslog_url") && BurpConfig.getBoolean("dnslog_token")){
			loadConfig();
		}
        tabs.addTab("基本设置", baseSetting);
    }
    
    public void config_jcheckbox(JPanel baseSetting,GridBagConstraints gridBagConstraints) {
    	this.jCheckBox = new JCheckBox("启动插件",true);
    	this.jCheckBox.setFont(new Font("Serif", Font.PLAIN, this.jCheckBox.getFont().getSize()));
    	gridBagConstraints.insets = new Insets(5, 5, 5, 5);
    	gridBagConstraints.anchor = gridBagConstraints.WEST;
    	gridBagConstraints.gridx = 0;
    	gridBagConstraints.gridy = 1;
    	baseSetting.add(jCheckBox,gridBagConstraints);
    }
    
    public void config_jLabel1(JPanel baseSetting,GridBagConstraints gridBagConstraints) {
    	JLabel jLabel = new JLabel("Dnslog Url:");
    	gridBagConstraints.insets = new Insets(5, 5, 5, 5);
    	gridBagConstraints.gridx = 0;
    	gridBagConstraints.gridy = 2;
    	baseSetting.add(jLabel,gridBagConstraints);
    }
    
    public void config_jTextField1(JPanel baseSetting,GridBagConstraints gridBagConstraints) {
    	this.jTextField_Identifier = new JTextField();
    	gridBagConstraints.gridwidth = 3;
    	gridBagConstraints.insets = new Insets(5, 5, 5, 5);
    	gridBagConstraints.gridx = 1;
    	gridBagConstraints.gridy = 2;
    	gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    	this.jTextField_Identifier.setColumns(20);
    	baseSetting.add(this.jTextField_Identifier,gridBagConstraints);
    }
    
    public void config_jLabel2(JPanel baseSetting,GridBagConstraints gridBagConstraints) {
    	JLabel jLabel = new JLabel("Api Token:");
    	gridBagConstraints.insets = new Insets(5, 5, 5, 5);
    	gridBagConstraints.gridx = 0;
    	gridBagConstraints.gridy = 3;
    	baseSetting.add(jLabel,gridBagConstraints);
    }
    
    public void config_jTextField2(JPanel baseSetting,GridBagConstraints gridBagConstraints) {
    	this.jTextField_token = new JTextField();
    	gridBagConstraints.gridwidth = 3;
    	gridBagConstraints.insets = new Insets(5, 5, 5, 5);  	
    	gridBagConstraints.gridx = 1;
    	gridBagConstraints.gridy = 3;
    	gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    	this.jTextField_token.setColumns(20);
    	baseSetting.add(this.jTextField_token,gridBagConstraints);
    }
    
    public void config_Button(JPanel baseSetting,GridBagConstraints gridBagConstraints) {
		this.applyTextbtButton = new JButton("apply");
    	gridBagConstraints.gridwidth = 3;
    	gridBagConstraints.insets = new Insets(5, 5, 5, 5);
    	gridBagConstraints.gridx = 0;
    	gridBagConstraints.gridy = 4;
    	gridBagConstraints.anchor = gridBagConstraints.WEST;
    	gridBagConstraints.fill = GridBagConstraints.HORIZONTAL;
    	baseSetting.add(applyTextbtButton,gridBagConstraints);
		applyTextbtButton.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String dnslogurl = jTextField_Identifier.getText();
				String dnstoken = jTextField_token.getText();
				if(dnslogurl.length()>0 && dnstoken.length()>0){
					JOptionPane.showMessageDialog(null, "输入成功");
				}else{
					JOptionPane.showMessageDialog(null, "检查你的url或者token是否输入正确");
				}
				BurpConfig.setDnslogService("dnslog_url",dnslogurl);
				BurpConfig.setDnslogService("dnslog_token",dnstoken);
				loadConfig();
			}
		});
	}
    
    public Boolean isEnabled() {
    	return this.jCheckBox.isSelected();
    }
    
    public String jTextFieldIdentifier() {
    	return this.jTextField_Identifier.getText();
    }
    
    public String jTextFieldToken() {return this.jTextField_token.getText();}

	public void loadConfig(){
		this.jTextField_Identifier.setText(BurpConfig.getDnslogService("dnslog_url"));
		this.jTextField_token.setText(BurpConfig.getDnslogService("dnslog_token"));
	}
}
