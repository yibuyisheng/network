<!DOCTYPE html>
<html>
<head>
	<meta charset="utf-8"/>
	<title>文档浏览</title>
	<link href="${static_root}/css/global.css" rel="stylesheet" type="text/css">
	<script type="text/javascript" src="${static_root}/js/package.js"></script>
</head>
<body>
	<div class="header dn">
		文档浏览
	</div>
	<div class="content">
        <div class="box">
            <div class="title">文档浏览</div>
            <div class="content">
                <#if docs??>
                    <ul class="listview">
                        <#list documents as doc>
                            <li>
                                <a href="${root}/doc/get.html?id=${doc.id}" target="_blank">
                                    <div>
                                        <h2>${doc.title}</h2>
                                        <p>
                                            <span>上传者：${doc.provider.nickName!""}</span>
                                            <span>上传时间：<span node-type="long_time" time="${doc.uploadTime!''}" server-time="${servertime!''}"></span></span>
                                        </p>
                                    </div>
                                </a>
                            </li>
                        </#list>
                    </ul>
                    <div class="page">
                        <a href="<#if page_index gt 1>${root}/doc/ava.html?page=${page_index-1}<#else>javascript:;</#if>" class="<#if page_index gt 1>button_gray<#else>button_disable</#if>">上一页</a>
                        <a href="<#if page_index lt page_count>${root}/doc/ava.html?page=${page_index+1}<#else>javascript:;</#if>" class="<#if page_index lt page_count>button_gray<#else>button_disable</#if>">下一页</a>
                        <label>第${page_index!""}页/共${page_count!""}页</label>
                    </div>
                </#if>
            </div>
        </div>
	</div>
	<div class="footer dn">
		底部
	</div>
	<script>
        seajs.use(['thirdparty/jquery','lib/timeFormat','ui/ui_strengthen'],function($,timeFormat){
            var $longTime = $('*[node-type="long_time"]');
            for(var i= 0,il=$longTime.length;i<il;i++){
                var $ele = $longTime.eq(i);
                var time = parseInt($ele.attr('time').replace(/,/g,''));
                var serverTime = parseInt($ele.attr('server-time').replace(/,/g,''));
                $ele.text(timeFormat.convertDate2Offset(time,serverTime));
            }
        });
	</script>
</body>
</html>