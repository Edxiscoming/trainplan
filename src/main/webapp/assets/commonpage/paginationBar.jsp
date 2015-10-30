<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="panelBar">
	<div class="pages">
		<span>显示</span>
		<select class="combox" name="numPerPage" onchange="navTabPageBreak({numPerPage:this.value})">
			<option value="10" ${pagination.numPerPage==10 ? 'selected="selected"' : ''}>10</option>
			<option value="20" ${pagination.numPerPage==20 ? 'selected="selected"' : ''}>20</option>
			<option value="30" ${pagination.numPerPage==30 ? 'selected="selected"' : ''}>30</option>
			<option value="50" ${pagination.numPerPage==50 ? 'selected="selected"' : ''}>50</option>
		</select>
		<span>条，共${pager.total}条</span>
	</div>
	
	<div class="pagination" targetType="navTab" totalCount="${pager.total}" numPerPage="${pagination.numPerPage}"
	 pageNumShown="${pagination.pageNumShown}" currentPage="${pagination.currentPage}"></div>

</div>