package ui;

import java.awt.Component;
import java.awt.EventQueue;
import java.util.ArrayList;
import java.util.List;


import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableModel;

import burp.*;

public class QueueTag extends AbstractTableModel implements ITab,IMessageEditorController{
	private IBurpExtenderCallbacks callbacks;
    private IExtensionHelpers helpers;
    private String tagName;
    private JSplitPane mjSplitPane;
    private List<QueueTag.TablesData> Udatas = new ArrayList();
    private IMessageEditor HRequestTextEditor;
    private IMessageEditor HResponseTextEditor;
    private IHttpRequestResponse currentlyDisplayedItem;
    private QueueTag.URLTable Utable;
    private JScrollPane UscrollPane;
    private JSplitPane HjSplitPane;
    private JTabbedPane Ltable;
    private JTabbedPane Rtable;

	/**
	 * Launch the application.
	 */
	public QueueTag(IBurpExtenderCallbacks callbacks,String name,JTabbedPane tabs) {
		this.callbacks = callbacks;
		this.tagName = name;
		this.helpers = helpers;
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				mjSplitPane = new JSplitPane(0); //主分隔面板
				Utable = new URLTable(QueueTag.this); // 任务栏面板
				UscrollPane = new JScrollPane(Utable);
                //请求与响应界面的分隔面板规则
                HjSplitPane = new JSplitPane();
                HjSplitPane.setDividerLocation(0.5D);
                //请求的面板
                Ltable = new JTabbedPane();
                HRequestTextEditor = callbacks.createMessageEditor(QueueTag.this, false);
                Ltable.addTab("Request", HRequestTextEditor.getComponent());
                //响应的面板
                Rtable = new JTabbedPane();
                HResponseTextEditor = callbacks.createMessageEditor(QueueTag.this, false);
                Rtable.addTab("Response", HResponseTextEditor.getComponent());
                //自定义程序UI组件
                HjSplitPane.add(Ltable, "left");
                HjSplitPane.add(Rtable, "right");
                mjSplitPane.add(UscrollPane, "left");
                mjSplitPane.add(HjSplitPane, "right");
                tabs.addTab("扫描面板", mjSplitPane);
			}
		});
	}
	

	public static class TablesData {
	        final int id;
	        final String method;
	        final String url;
	        final int statusCode;
	        final String key;
	        final String vulString;
	        final IHttpRequestResponse requestResponse;

	        public TablesData(int id,  String method, String url, int statusCode, String key, String vulString, IHttpRequestResponse requestResponse) {
	            this.id = id;
	            this.method = method;
	            this.url = url;
	            this.statusCode = statusCode;
	            this.key = key;
	            this.vulString = vulString;
	            this.requestResponse = requestResponse;

	        }
	    }

	    public class URLTable extends JTable {
	        public URLTable(TableModel tableModel) {
	            super(tableModel);
	        }

	        public void changeSelection(int row, int col, boolean toggle, boolean extend) {
	            QueueTag.TablesData dataEntry = (QueueTag.TablesData)Udatas.get(this.convertRowIndexToModel(row));
	            HRequestTextEditor.setMessage(dataEntry.requestResponse.getRequest(), true);
	            HResponseTextEditor.setMessage(dataEntry.requestResponse.getResponse(), false);
	            currentlyDisplayedItem = dataEntry.requestResponse;
	            super.changeSelection(row, col, toggle, extend);
	        }
	    }
	    
	    public String getColumnName(int columnIndex) {
	        switch(columnIndex) {
	            case 0:
	                return "id";
	            case 1:
	                return "method";
	            case 2:
	                return "url";
	            case 3:
	            	return "statusCode";
	            case 4:
	            	return "key";
	            case 5:
	            	return "issue";
	            default:
	                return null;
	        }
	    }

		@Override
		public int getRowCount() {
			// TODO 自动生成的方法存根
			return this.Udatas.size();
		}

		@Override
		public int getColumnCount() {
			// TODO 自动生成的方法存根
			return 6;
		}
		
	    public Class<?> getColumnClass(int columnIndex) {
	        return String.class;
	    }

		@Override
		public Object getValueAt(int rowIndex, int columnIndex) {
			// TODO 自动生成的方法存根
			QueueTag.TablesData datas = (QueueTag.TablesData)this.Udatas.get(rowIndex);
	        switch(columnIndex) {
	            case 0:
	                return datas.id;
	            case 1:
	                return datas.method;
	            case 2:
	                return datas.url;
	            case 3:
	            	return datas.statusCode;
	            case 4:
	            	return datas.key;
	            case 5:
	            	return datas.vulString;
	            default:
	                return null;
	        }
		}

		@Override
		public IHttpService getHttpService() {
			// TODO 自动生成的方法存根
			return this.currentlyDisplayedItem.getHttpService();
		}

		@Override
		public byte[] getRequest() {
			// TODO 自动生成的方法存根
			return this.currentlyDisplayedItem.getRequest();
		}

		@Override
		public byte[] getResponse() {
			// TODO 自动生成的方法存根
			return this.currentlyDisplayedItem.getResponse();
		}
		
	    public int add(String method,String url, int statusCode, String key, String vulString, IHttpRequestResponse requestResponse) {
	        synchronized(this.Udatas) {
	            int id = this.Udatas.size();
	            this.Udatas.add(new QueueTag.TablesData(id, method, url, statusCode, key, vulString, requestResponse));
	            this.fireTableRowsInserted(id, id);
	            return id;
	        }
	    }
	    
	    public int save(int id, String method, String url, int statusCode, String key, String vulString, IHttpRequestResponse requestResponse) {
	        QueueTag.TablesData dataEntry = (QueueTag.TablesData)this.Udatas.get(id);
	        synchronized(this.Udatas) {
	            this.Udatas.set(id, new QueueTag.TablesData(id, method, url, statusCode, key, vulString, requestResponse));
	            this.fireTableRowsUpdated(id, id);
	            return id;
	        }
	    }

		@Override
		public String getTabCaption() {
			// TODO 自动生成的方法存根
			return this.tagName;
		}

		@Override
		public Component getUiComponent() {
			// TODO 自动生成的方法存根
			return this.mjSplitPane;
		}
}
