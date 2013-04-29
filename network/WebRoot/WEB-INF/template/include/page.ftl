<#macro page page_index page_count url>
	<#--构造上一页链接-->
	<#if page_index gt 1>
		<#if url?last_index_of("?") == -1>
			<#assign pre_url=url+"?page="+(page_index-1) />
		<#elseif url?last_index_of("?") == url?length-1>
			<#assign pre_url=url+"page="+(page_index-1) />
		<#else>
			<#assign pre_url=url+"&page="+(page_index-1) />
		</#if>
	<#else>
		<#assign pre_url="javascript:;" />
	</#if>
	<#--构造下一页链接-->
	<#if page_index lt page_count>
		<#if url?last_index_of("?") == -1>
			<#assign next_url=url+"?page="+(page_index+1) />
		<#elseif url?last_index_of("?") == url?length-1>
			<#assign next_url=url+"page="+(page_index+1) />
		<#else>
			<#assign next_url=url+"&page="+(page_index+1) />
		</#if>
	<#else>
		<#assign next_url="javascript:;" />
	</#if>
	<div class="page">
        <a href="${pre_url}" class="<#if page_index gt 1>button_gray<#else>button_disable</#if>">上一页</a>
        <a href="${next_url}" class="<#if page_index lt page_count>button_gray<#else>button_disable</#if>">下一页</a>
        <label>第${page_index!""}页/共${page_count!""}页</label>
    </div>
</#macro>