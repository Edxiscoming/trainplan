/*
 <table style="width:100%;height:20px;">
    <tr style="width:100%;height:20px;">
     <td style="width:60%;height:20px;">
  		<span class="pull-left">共<span data-bind="html: totalCount()"></span>条  当前<span data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span data-bind="html: endIndex()"></span>条   共<span data-bind="text: pageCount()"></span>页</span> 								 
  	 </td>
     <td style="width:40%;height:20px;padding:0px;pading-bottom:-14">   
		<span data-bind="attr:{class:currentPage() == 0 ? 'disabed': ''}"><a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-right:-5px;padding:0px 5px;" data-bind="text:'<<', click: currentPage() == 0 ? null: loadPre"></a>
	    <input type="text"  style="padding-left:8px;margin-bottom:0px;padding-bottom:0;width:30px;height: 19px;background-color: #ffffff;border: 1px solid #dddddd;" data-bind="value: parseInt(currentPage())+1, event:{keyup: pageNbrChange}"/>
		<a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-left:-5px;padding:0px 5px;" data-bind="text:'>>', click: (currentPage() == pageCount()-1 || totalCount() == 0) ? null: loadNext"  style="padding:0px 5px;"></a>
       </ul> 
	 
     </td >
  </tr>
</table> 
 <script type="text/html" id="tablefooter-short-template"> 
  <table style="width:100%;height:20px;">
    <tr style="width:100%;height:20px;">
     <td style="width:60%;height:20px;">
  		<span class="pull-left">共<span data-bind="html: totalCount()"></span>条  当前<span data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span data-bind="html: endIndex()"></span>条   共<span data-bind="text: pageCount()"></span>页</span> 								 
  	 </td>
     <td style="width:40%;height:20px;padding:0px;pading-bottom:-14">   
		<span data-bind="attr:{class:currentPage() == 0 ? 'disabed': ''}"><a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-right:-5px;padding:0px 5px;" data-bind="text:'<<', click: currentPage() == 0 ? null: loadPre"></a>
	    <input type="text"  style="padding-left:8px;margin-bottom:0px;padding-bottom:0;width:30px;height: 19px;background-color: #ffffff;border: 1px solid #dddddd;" data-bind="value: parseInt(currentPage())+1, event:{keyup: pageNbrChange}"/>
		<a style="cursor:pointer;background-color: #ffffff;border: 1px solid #dddddd;margin-left:-5px;padding:0px 5px;" data-bind="text:'>>', click: (currentPage() == pageCount()-1 || totalCount() == 0) ? null: loadNext"  style="padding:0px 5px;"></a>
       </ul> 
	 
     </td >
  </tr>
</table> 
</script> 
<script type="text/html" id="tablefooter-long-template">  
	<span class="pagination pull-left">共<span data-bind="html: totalCount()"></span>条</td><td>当前<span data-bind="html: totalCount() > 0 ? (currentIndex() + 1) : '0'"></span>到<span data-bind="html: endIndex()"></span>条   共<span data-bind="text: pageCount()"></span>页</span>
	<ul data-bind="foreach: new Array(pageCount())" class="pagination pull-right" style="margin: 0px; display: block;">
     
	  <!-- ko if: $index() == 0 -->
		<li data-bind="attr:{class: $parent.currentPage() == 0 ? 'disabled' : ''}"><a data-bind="text:'<<', click: $parent.loadPre"></a></li>
	  <!-- /ko --> 
	  <!-- ko if: $parent.pageCount() > 7 && $index() > 0 && $index() < $parent.pageCount() --> 
	         <!-- ko if: $index() < 3 --> 
	         		<li data-bind="attr:{class: $parent.currentPage() == $index() ? 'active' : ''}" style="cursor:pointer"><a data-bind="text: $index()+1, click: $parent.loadPage.bind($data, $index())"></a></li>
	          <!-- /ko --> 
	          <!-- ko if: ($index() == $parent.pageCount()/2 - 1) || ($index() == $parent.pageCount()/2 - 3) -->  
	                 <li><a>......</a></li>
	                 <li data-bind="attr:{class: $parent.currentPage() == $index() ? 'active' : ''}" style="cursor:pointer"><a data-bind="text: $index()+1, click: $parent.loadPage.bind($data, $index())"></a></li>
	          <!-- /ko -->  
	          <!-- ko if: ($index() >= $parent.pageCount()/2 + 1) || (($index() >= $parent.pageCount() - 3) && ($index() <= $parent.pageCount() - 1) -->   
	                 <li data-bind="attr:{class: $parent.currentPage() == $index() ? 'active' : ''}" style="cursor:pointer"><a data-bind="text: $index()+1, click: $parent.loadPage.bind($data, $index())"></a></li>
	          <!-- /ko -->  
	  <!-- /ko -->   
	  <!-- ko if: $parent.pageCount() < 7 && $index() > 0 && $index() < $parent.pageCount()--> 
	      <li data-bind="attr:{class: $parent.currentPage() == $index() ? 'active' : ''}" style="cursor:pointer"><a data-bind="text: $index()+1, click: $parent.loadPage.bind($data, $index())"></a></li>
	  <!-- /ko -->   
	  <!-- ko if: $index() == $parent.pageCount() - 1 -->
		<li data-bind="attr:{class: $parent.currentPage() == $parent.pageCount()-1 ? 'disabled' : ''}" style="cursor:pointer"><a data-bind="text:'>>', click: $parent.loadNext"></a></li>
	  <!-- /ko -->
   </ul> 
</script> */

function PageModle(pageSize, fun, jg,flag){
	
	var self = this; 
	 
	self.loadFunc = fun;
	
	self.jg = ko.observable(jg);
	
	self.currentIndex = ko.observable(0);
	
	self.pageSize = ko.observable(pageSize);  
	
	self.totalCount = ko.observable(0);
	
	self.rows = ko.observableArray();  
	
	self.endIndex = ko.observable(0); 
	
	self.currentPage = ko.observable(0); 
	
	self.pageCount = ko.observable(0); 
	

	self.getPageEndIndex = function(){
	
		return self.currentIndex() + self.pageSize();
	};
	self.loadPre = function(){
//		if(self.currentPage()*self.pageSize() > self.totalCount()){
//			
//		}
//		var currentIndex = self.currentIndex();   
//		if(currentIndex == 0){
//			return;
//		}
//		self.clear();
//		self.currentIndex(currentIndex - self.pageSize());  
//		self.loadFunc(self.currentIndex(), self.getPageEndIndex()); 
		self.loadPage(parseInt(self.currentPage()) - 1);
		
	}; 
	self.loadNext = function(){ 
		self.loadPage(parseInt(self.currentPage()) + 1);
	};
	
	self.clear = function(){ 
		self.rows.remove(function(item) {
			return true;
		});
		self.currentPage(0); 
		self.currentIndex(0); 
		self.endIndex(0);
		self.pageCount(0);
		self.totalCount(0);
	};
	
	self.loadRows = function(flag){ 
		self.clear(); 
		self.loadFunc(self.currentIndex(), self.currentIndex() + self.pageSize(),flag); 
	}; 
	
	self.reFresh = function(){  
		self.rows.remove(function(item) {
			return true;
		});
		self.loadFunc(self.currentIndex(), self.currentIndex() + self.pageSize()); 
	};
	
	self.addLoadRows = function(delItems){    
		var len = delItems.length;
		$.each(delItems, function(i, n){ 
			self.rows.remove(n); 
		}); 
		self.loadFunc(parseInt(self.endIndex()) - len + 1, self.currentIndex() + self.pageSize()); 
		
	};  
	
	self.addRows = function(result){   
		$.each(result, function(i, n){
			self.rows.push(n);
		}); 
	};
	self.loadPageRows = function(totalCount, rows){
		self.addRows(rows);
		self.totalCount(totalCount); 
		
		self.endIndex(self.currentIndex() + self.pageSize() <= totalCount ? self.currentIndex() + self.pageSize() : self.currentIndex() + totalCount%self.pageSize());
		self.pageCount((self.totalCount() + (self.totalCount()% self.pageSize() == 0 ? 0 : (self.pageSize() - self.totalCount()% self.pageSize())))/self.pageSize());
	}; 
	
	self.loadPage = function(pageIndex){  
		if(pageIndex == self.currentPage()){
			return;
		}   
		self.clear();
		self.currentPage(pageIndex);
		self.currentIndex(pageIndex * self.pageSize());
		self.loadFunc(self.currentIndex() + 1, self.currentIndex() + self.pageSize());  
	};  
	self.pageNbrChange = function(page, event){ 
	    var keycode = (event.keyCode ? event.keyCode : event.which);  
	    var value = event.target.value; 
	    if(!(/[123456789]+/.test(value)) || (value <= 0 || value > self.pageCount())){  
	    	$(event.target).val(self.currentPage() + 1); 
    	} 
	    if(keycode == 13){  
	    	self.loadPage(parseInt(event.target.value) - 1); 
	    } 
	};
}