<#macro left self page>
	<div class="box fl span2">
		<div class="title">管理项目</div>
		<div class="">
			<div class="sidebar">
			<#if self??>
				<ul>
					<#if self.identity==4 || self.permissionUser><li><a href="${root}/mc/user.html" <#if page=="user">class="active"</#if>>用户管理</a></li></#if>
					<#if self.identity==4 || self.permissionDocument><li><a href="${root}/doc/manage.html" <#if page=="document">class="active"</#if>>文档管理</a></li></#if>
					<#if self.identity==4 || self.permissionCourseStructure><li><a href="${root}/chapter/construct.html" <#if page=="chapter">class="active"</#if>>章节结构</a></li></#if>
					<#if self.identity==4 || self.permissionForum><li><a href="#" <#if page=="forum">class="active"</#if>>论坛管理</a></li></#if>
					<#if self.identity==4 || self.permissionForwardBack><li><a href="#" <#if page=="forward_back">class="active"</#if>>反馈管理</a></li></#if>
				</ul>
			</#if>
			</div>
		</div>
	</div>
</#macro>