package org.railway.com.trainplan.common.constants;

public class OperationConstants {
	public static enum REQUEST_METHOD {
		POST(1, "post"),	
		DELETE(2, "delete"),
		GET(3, "get"),
		PUT(4, "put");
		private final int num;
        private final String msg;
        REQUEST_METHOD(int num, String msg) {
        	this.num = num;
        	this.msg = msg;
        }
		public int getNum() {
			return num;
		}
		public String getMsg() {
			return msg;
		}
		public static REQUEST_METHOD getEnum(String msg) {
			
			for(REQUEST_METHOD en:REQUEST_METHOD.values()){
				if(en.getMsg().equals(msg))
					return en;
			}		
			return REQUEST_METHOD.valueOf(msg);
		}
	}
	
	public static enum POSITION {
		URL(1, "url"),	
		BODY(2, "body"),
		HEADER(3, "header");
		private final int num;
        private final String msg;
        POSITION(int num, String msg) {
        	this.num = num;
        	this.msg = msg;
        }
		public int getNum() {
			return num;
		}
		public String getMsg() {
			return msg;
		}
	}

}
