<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8"/>
    <title><#if doc??>${doc.title!""}-</#if>文档</title>
    <link href="${static_root}/css/global.css" rel="stylesheet" type="text/css">
    <script type="text/javascript" src="${static_root}/js/package.js"></script>
</head>
<body>
<div class="header dn">
    文档浏览
</div>
<div class="content">
    <#if status==-3>
        ${msg}
    <#elseif status==-2>
        404
    <#elseif status==1>
        <h1>${doc.title}</h1>
        <p node-type="operation">
            <#if doc.provider??>
                <label>提供者：${doc.provider.nickName}</label>
                <#if self?? && doc.provider.id?? && doc.provider.id==self.id>
                    <a href="javascript:void(0);" action-type="re_edit">重新编辑</a>
                    <a href="javascript:void(0);" action-type="save" class="dn">保存</a>
                    <a href="javascript:void(0);" action-type="back" class="dn">返回</a>
                </#if>
            </#if>
        </p>
        <div class="doc_content">${doc.docContent}</div>
        <div node-type="edit_container" class="dn">
            <form action="${root}/doc/update.json" method="POST">
                <input type="hidden" name="title" value="${doc.title}"/>
                <textarea name="doc" class="dn">${doc.docContent}</textarea>
                <input type="hidden" name="status" value="${doc.status}"/>
                <input type="hidden" name="modify" value="true"/>
            </form>
        </div>
    </#if>
</div>
<div class="footer dn">
    底部
</div>

<script>
    seajs.use(['thirdparty/jquery','thirdparty/ckeditor/ckeditor','ui/notice'],function($,CKEDITOR,notice){
        var $operation = $('p[node-type="operation"]');
        var $edit_container = $('div[node-type="edit_container"]');

        var $re_edit = $('a[action-type="re_edit"]'),
                $save = $('a[action-type="save"]'),
                $back = $('a[action-type="back"]');

        CKEDITOR.replace('doc',{
            filebrowserBrowseUrl: '${root}/doc/browse/images?type=Images'
        });

        $re_edit.click(function(){
            $('.doc_content').addClass('dn');
            $edit_container.removeClass('dn');

            $re_edit.addClass('dn');
            $save.removeClass('dn');
            $back.removeClass('dn');
        });
        $save.click(function(){
            var $form = $edit_container.find('form');
            $form.find('textarea[name="doc"]').val(CKEDITOR.instances.doc.getData());
            $.ajax({
                url:$form.attr('action'),
                type:$form.attr('method'),
                data:$form.serialize(),
                dataType:'text',
                success:function(data){
                    var json = eval('('+data+')');
                    if(json.status!==1){
                        if(json.status===-1){
                            new LoginDialog().show();
                        }else{
                            new notice.Error('失败',json.msg);
                        }
                    }else{
                        window.location.reload();
                    }
                    $target.removeClass('button_disable').addClass('button_blue');
                }
            });
        });
        $back.click(function(){
            $('.doc_content').removeClass('dn');
            $edit_container.addClass('dn');

            $re_edit.removeClass('dn');
            $save.addClass('dn');
            $back.addClass('dn');
        });
    });
</script>
</body>
</html>